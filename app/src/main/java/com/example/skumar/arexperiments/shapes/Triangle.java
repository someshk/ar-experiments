package com.example.skumar.arexperiments.shapes;

import android.opengl.GLES31;

import com.example.skumar.arexperiments.MainActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by skumar on 12/18/17.
 */

public class Triangle {

    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;

    private FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    private static final int COORDS_PER_VERTEX = 3;
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private static  float triangleCoords[] = {
            0.0f, 0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f // bottom right
    };

    // Set color with red, green, blue and alpha values
    float color [] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };


    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    public Triangle() {
        // initialize the vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                triangleCoords.length * 4
        );

        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // Set the cbuffer to read the first coordinate
        vertexBuffer.position(0);

        int vertexShader = MainActivity.loadShader(GLES31.GL_VERTEX_SHADER,
                vertexShaderCode);

        int fragmentShader = MainActivity.loadShader(GLES31.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES31.glCreateProgram();

        // add the vertex shader to the program
        GLES31.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to the program
        GLES31.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ED program executables
        GLES31.glLinkProgram(mProgram);
    }

    public void draw() {
        // Add program to OpenGL ES Environment
        GLES31.glUseProgram(mProgram);

        // Get the handle to vertex shader's vPosition member
        mPositionHandle = GLES31.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES31.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES31.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES31.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES31.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw the triangle
        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES31.glDisableVertexAttribArray(mPositionHandle);
    }
}

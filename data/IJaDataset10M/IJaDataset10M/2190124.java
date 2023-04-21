package com.android.cubeUI;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A vertex shaded cube.
 */
class Cube {

    public Cube() {
        int one = 0x10000;
        int two = one;
        int vertices[] = { -one, -one, -one, one, -one, -one, one, one, -one, -one, one, -one, -one, -one, one, one, -one, one, one, one, one, -one, one, one };
        int colors[] = { 0, 0, 0, one, one, 0, 0, one, one, one, 0, one, 0, one, 0, one, 0, 0, one, one, one, 0, one, one, one, one, one, one, 0, one, one, one };
        byte indices[] = { 0, 4, 5, 0, 5, 1, 1, 5, 6, 1, 6, 2, 2, 6, 7, 2, 7, 3, 3, 7, 4, 3, 4, 0, 4, 7, 6, 4, 6, 5, 3, 0, 1, 3, 1, 2 };
        int texCoords[] = { 0, two, two, two, two, 0, 0, 0, 0, two, two, two, two, 0, 0, 0, 0, two, two, two, two, 0, 0, 0, 0, two, two, two, two, 0, 0, 0, 0, two, two, two, two, 0, 0, 0, 0, two, two, two, two, 0, 0, 0 };
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asIntBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        mTextureBuffer = tbb.asIntBuffer();
        mTextureBuffer.put(texCoords);
        mTextureBuffer.position(0);
    }

    static void loadTexture(GL10 gl, Context context, int resource) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resource);
        ByteBuffer bb = extract(bmp);
        load(gl, bb, bmp.getWidth(), bmp.getHeight());
    }

    private static ByteBuffer extract(Bitmap bmp) {
        ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight() * bmp.getWidth() * 4);
        bb.order(ByteOrder.BIG_ENDIAN);
        IntBuffer ib = bb.asIntBuffer();
        for (int y = bmp.getHeight() - 1; y > -1; y--) {
            for (int x = 0; x < bmp.getWidth(); x++) {
                int pix = bmp.getPixel(x, bmp.getHeight() - y - 1);
                int red = ((pix >> 16) & 0xFF);
                int green = ((pix >> 8) & 0xFF);
                int blue = ((pix) & 0xFF);
                ib.put(red << 24 | green << 16 | blue << 8 | ((red + blue + green) / 3));
            }
        }
        bb.position(0);
        return bb;
    }

    private static void load(GL10 gl, ByteBuffer bb, int width, int height) {
        int[] tmp_tex = new int[1];
        gl.glGenTextures(1, tmp_tex, 0);
        int tex = tmp_tex[0];
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, width, height, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(gl.GL_CW);
        gl.glVertexPointer(3, gl.GL_FIXED, 0, mVertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, mTextureBuffer);
        gl.glDrawElements(gl.GL_TRIANGLES, 36, gl.GL_UNSIGNED_BYTE, mIndexBuffer);
    }

    private IntBuffer mVertexBuffer;

    private IntBuffer mColorBuffer;

    private ByteBuffer mIndexBuffer;

    private IntBuffer mTextureBuffer;
}

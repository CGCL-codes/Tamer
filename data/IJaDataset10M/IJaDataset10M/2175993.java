package com.jogamp.opengl.test.junit.jogl.offscreen;

import java.nio.*;
import javax.media.opengl.*;
import javax.media.opengl.fixedfunc.*;
import com.jogamp.opengl.util.*;
import javax.media.opengl.fixedfunc.GLPointerFunc;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.GLArrayDataClient;
import com.jogamp.opengl.util.GLArrayDataServer;

public class ReadBuffer2Screen extends ReadBufferBase {

    PMVMatrix pmvMatrix;

    GLArrayDataClient readTextureVertices = null;

    GLArrayDataClient readTextureCoords = null;

    boolean enableBufferAlways = false;

    boolean enableBufferVBO = true;

    public ReadBuffer2Screen(GLDrawable externalRead) {
        super(externalRead, true);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        GL gl = drawable.getGL();
        pmvMatrix = new PMVMatrix();
        float f_edge = 1f;
        if (null == readTextureVertices) {
            readTextureVertices = GLArrayDataServer.createFixed(GLPointerFunc.GL_VERTEX_ARRAY, "mgl_Vertex", 2, GL.GL_FLOAT, true, 4, GL.GL_STATIC_DRAW);
            readTextureVertices.setEnableAlways(enableBufferAlways);
            readTextureVertices.setVBOEnabled(enableBufferVBO);
            {
                FloatBuffer vb = (FloatBuffer) readTextureVertices.getBuffer();
                vb.put(-f_edge);
                vb.put(-f_edge);
                vb.put(f_edge);
                vb.put(-f_edge);
                vb.put(-f_edge);
                vb.put(f_edge);
                vb.put(f_edge);
                vb.put(f_edge);
            }
            readTextureVertices.seal(gl, true);
            System.out.println(readTextureVertices);
        }
        gl.glClearColor(0.5f, 0.5f, 0.5f, 0.4f);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        super.reshape(drawable, x, y, width, height);
        GL gl = drawable.getGL();
        gl.glViewport(0, 0, width, height);
        if (gl instanceof GLLightingFunc) {
            ((GLLightingFunc) gl).glShadeModel(GLLightingFunc.GL_SMOOTH);
        }
        GLMatrixFunc glM;
        if (gl instanceof GLMatrixFunc) {
            glM = (GLMatrixFunc) gl;
        } else {
            throw new GLException("ES2 currently unhandled .. ");
        }
        pmvMatrix.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        pmvMatrix.glLoadIdentity();
        pmvMatrix.glTranslatef(0, 0, -2.5f);
        if (null != glM) {
            glM.glMatrixMode(PMVMatrix.GL_MODELVIEW);
            glM.glLoadMatrixf(pmvMatrix.glGetMvMatrixf());
        }
        pmvMatrix.glMatrixMode(PMVMatrix.GL_PROJECTION);
        pmvMatrix.glLoadIdentity();
        pmvMatrix.gluPerspective(45.0f, (float) width / (float) height, 1.0f, 100.0f);
        if (null != glM) {
            glM.glMatrixMode(PMVMatrix.GL_PROJECTION);
            glM.glLoadMatrixf(pmvMatrix.glGetPMatrixf());
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        super.dispose(drawable);
    }

    void renderOffscreenTexture(GL gl) {
        if (!readBufferUtil.isValid()) return;
        readBufferUtil.getTexture().enable(gl);
        readBufferUtil.getTexture().bind(gl);
        if (gl.isGL2ES1()) {
            gl.getGL2ES1().glTexEnvi(GL2ES1.GL_TEXTURE_ENV, GL2ES1.GL_TEXTURE_ENV_MODE, GL2ES1.GL_MODULATE);
        }
        updateTextureCoords(gl, false);
        readTextureVertices.enableBuffer(gl, true);
        if (null != readTextureCoords) {
            readTextureCoords.enableBuffer(gl, true);
        }
        gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, readTextureVertices.getElementNumber());
        readBufferUtil.getTexture().disable(gl);
    }

    void updateTextureCoords(GL gl, boolean force) {
        if (force || null == readTextureCoords) {
            readTextureCoords = GLArrayDataServer.createFixed(GLPointerFunc.GL_TEXTURE_COORD_ARRAY, "mgl_MultiTexCoord0", 2, GL.GL_FLOAT, true, 4, GL.GL_STATIC_DRAW);
            readTextureCoords.setEnableAlways(enableBufferAlways);
            readTextureCoords.setVBOEnabled(enableBufferVBO);
            {
                TextureCoords coords = readBufferUtil.getTexture().getImageTexCoords();
                FloatBuffer cb = (FloatBuffer) readTextureCoords.getBuffer();
                cb.put(coords.left());
                cb.put(coords.bottom());
                cb.put(coords.right());
                cb.put(coords.bottom());
                cb.put(coords.left());
                cb.put(coords.top());
                cb.put(coords.right());
                cb.put(coords.top());
            }
            readTextureCoords.seal(gl, true);
            System.out.println(readTextureCoords);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        super.display(drawable);
        GL gl = drawable.getGL();
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
        if (gl instanceof GLLightingFunc) {
            ((GLLightingFunc) gl).glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        renderOffscreenTexture(gl);
    }
}

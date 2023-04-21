package com.sun.opengl.util;

import javax.media.opengl.*;

public class FBObject {

    private int width, height, attr;

    private int fb, fbo_tex, depth_rb, stencil_rb, vStatus;

    private int texInternalFormat, texDataFormat, texDataType;

    public static final int ATTR_DEPTH = 1 << 0;

    public static final int ATTR_STENCIL = 1 << 1;

    public FBObject(int width, int height, int attributes) {
        this.width = width;
        this.height = height;
        this.attr = attributes;
    }

    public boolean validateStatus(GL gl) {
        if (!gl.glIsFramebuffer(fb)) {
            vStatus = -1;
            return false;
        }
        vStatus = gl.glCheckFramebufferStatus(gl.GL_FRAMEBUFFER);
        switch(vStatus) {
            case GL.GL_FRAMEBUFFER_COMPLETE:
                return true;
            case GL.GL_FRAMEBUFFER_UNSUPPORTED:
            case GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
            case GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
            case GL.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
            case GL.GL_FRAMEBUFFER_INCOMPLETE_FORMATS:
            case 0:
            default:
                return false;
        }
    }

    public String getStatusString() {
        switch(vStatus) {
            case -1:
                return "NOT A FBO";
            case GL.GL_FRAMEBUFFER_COMPLETE:
                return "OK";
            case GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
                return ("GL FBO: incomplete,incomplete attachment\n");
            case GL.GL_FRAMEBUFFER_UNSUPPORTED:
                return ("GL FBO: Unsupported framebuffer format");
            case GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                return ("GL FBO: incomplete,missing attachment");
            case GL.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
                return ("GL FBO: incomplete,attached images must have same dimensions");
            case GL.GL_FRAMEBUFFER_INCOMPLETE_FORMATS:
                return ("GL FBO: incomplete,attached images must have same format");
            case 0:
                return ("GL FBO: incomplete, implementation fault");
            default:
                return ("GL FBO: incomplete, implementation ERROR");
        }
    }

    public void init(GL gl) {
        int textureInternalFormat, textureDataFormat, textureDataType;
        if (gl.isGL2()) {
            textureInternalFormat = GL.GL_RGBA8;
            textureDataFormat = GL2.GL_BGRA;
            textureDataType = GL2.GL_UNSIGNED_INT_8_8_8_8_REV;
        } else if (gl.isGLES()) {
            textureInternalFormat = GL.GL_RGBA;
            textureDataFormat = GL.GL_RGBA;
            textureDataType = GL.GL_UNSIGNED_BYTE;
        } else {
            textureInternalFormat = GL.GL_RGB;
            textureDataFormat = GL.GL_RGB;
            textureDataType = GL.GL_UNSIGNED_BYTE;
        }
        init(gl, textureInternalFormat, textureDataFormat, textureDataType);
    }

    public void init(GL gl, int textureInternalFormat, int textureDataFormat, int textureDataType) {
        texInternalFormat = textureInternalFormat;
        texDataFormat = textureDataFormat;
        texDataType = textureDataType;
        int name[] = new int[1];
        gl.glGenFramebuffers(1, name, 0);
        fb = name[0];
        System.out.println("fb: " + fb);
        gl.glGenTextures(1, name, 0);
        fbo_tex = name[0];
        System.out.println("fbo_tex: " + fbo_tex);
        if (0 != (attr & ATTR_DEPTH)) {
            gl.glGenRenderbuffers(1, name, 0);
            depth_rb = name[0];
            System.out.println("depth_rb: " + depth_rb);
        } else {
            depth_rb = 0;
        }
        if (0 != (attr & ATTR_STENCIL)) {
            gl.glGenRenderbuffers(1, name, 0);
            stencil_rb = name[0];
            System.out.println("stencil_rb: " + stencil_rb);
        } else {
            stencil_rb = 0;
        }
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fb);
        gl.glBindTexture(GL.GL_TEXTURE_2D, fbo_tex);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, texInternalFormat, width, height, 0, texDataFormat, texDataType, null);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, fbo_tex, 0);
        if (depth_rb != 0) {
            gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depth_rb);
            gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT16, width, height);
            gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depth_rb);
        }
        if (stencil_rb != 0) {
            gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, stencil_rb);
            gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_STENCIL_INDEX8, width, height);
            gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_STENCIL_ATTACHMENT, GL.GL_RENDERBUFFER, stencil_rb);
        }
        if (validateStatus(gl)) {
            System.out.println("Framebuffer " + fb + " is complete");
        } else {
            System.out.println("Framebuffer " + fb + " is incomplete: status = 0x" + Integer.toHexString(vStatus) + " : " + getStatusString());
        }
        unbind(gl);
    }

    public void destroy(GL gl) {
        unbind(gl);
        int name[] = new int[1];
        if (0 != stencil_rb) {
            name[0] = stencil_rb;
            gl.glDeleteRenderbuffers(1, name, 0);
            stencil_rb = 0;
        }
        if (0 != depth_rb) {
            name[0] = depth_rb;
            gl.glDeleteRenderbuffers(1, name, 0);
            depth_rb = 0;
        }
        if (0 != fbo_tex) {
            name[0] = fbo_tex;
            gl.glDeleteTextures(1, name, 0);
            fbo_tex = 0;
        }
        if (0 != fb) {
            name[0] = fb;
            gl.glDeleteFramebuffers(1, name, 0);
            fb = 0;
        }
    }

    public void bind(GL gl) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, fbo_tex);
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fb);
    }

    public void unbind(GL gl) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    }

    public void use(GL gl) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, fbo_tex);
    }

    public int getFBName() {
        return fb;
    }

    public int getTextureName() {
        return fbo_tex;
    }
}

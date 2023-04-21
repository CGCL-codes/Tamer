package drk.graphics;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import drk.KarnaughLog;

public class FrameBufferObject {

    int fbo_handle;

    hdrTexture tex;

    public static int getCurrentBoundFBO() {
        GL gl = GLU.getCurrentGL();
        int[] tm = new int[1];
        gl.glGetIntegerv(GL.GL_FRAMEBUFFER_BINDING_EXT, tm, 0);
        return tm[0];
    }

    public FrameBufferObject() {
        super();
    }

    public void genObject(int width, int height, int format, int datatype) {
        GL gl = GLU.getCurrentGL();
        tex = new hdrTexture();
        tex.createTexture(GL.GL_TEXTURE_RECTANGLE_ARB, format, width, height, GL.GL_RGB, datatype, null);
        int[] tm = new int[1];
        gl.glGenFramebuffersEXT(1, tm, 0);
        gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, tm[0]);
        gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, tex.getTarget(), tex.getTextureObject(), 0);
        fbo_handle = tm[0];
        int status = gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT);
        System.err.println(fbo_error(status));
    }

    public void genObject(hdrTexture t) {
        tex = t;
        tex.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        tex.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        tex.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
        tex.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
        GL gl = GLU.getCurrentGL();
        int[] tm = new int[1];
        gl.glGenFramebuffersEXT(1, tm, 0);
        gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, tm[0]);
        gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, tex.getTarget(), tex.getTextureObject(), 0);
        fbo_handle = tm[0];
        int status = gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT);
        System.err.println(fbo_error(status));
    }

    public int getFboHandle() {
        return fbo_handle;
    }

    public int getHeight() {
        return tex.getHeight();
    }

    public int getWidth() {
        return tex.getWidth();
    }

    public hdrTexture getTexture() {
        return tex;
    }

    String fbo_error(int error) {
        switch(error) {
            case GL.GL_FRAMEBUFFER_COMPLETE_EXT:
                return "GL.GL_FRAMEBUFFER_COMPLETE_EXT";
            case GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
                return "GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT";
            case GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
                return "GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT";
            case GL.GL_FRAMEBUFFER_INCOMPLETE_DUPLICATE_ATTACHMENT_EXT:
                return "GL.GL_FRAMEBUFFER_INCOMPLETE_DUPLICATE_ATTACHMENT_EXT";
            case GL.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
                return "GL.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT";
            case GL.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
                return "GL.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT";
            case GL.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
                return "GL.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT";
            case GL.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
                return "GL.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT";
            case GL.GL_FRAMEBUFFER_UNSUPPORTED_EXT:
                return "GL.GL_FRAMEBUFFER_UNSUPPORTED_EXT";
            default:
                return "GL_UNKNOWN";
        }
    }

    int pushbf;

    public void bind() {
        GL gl = GLU.getCurrentGL();
        pushbf = FrameBufferObject.getCurrentBoundFBO();
        gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo_handle);
        gl.glDrawBuffer(GL.GL_COLOR_ATTACHMENT0_EXT);
        int status = gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT);
        if (status != GL.GL_FRAMEBUFFER_COMPLETE_EXT) {
            KarnaughLog.log(fbo_error(status));
        }
    }

    public void unbind(int i) {
        GL gl = GLU.getCurrentGL();
        gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, i);
    }

    public void unbind() {
        unbind(pushbf);
    }
}

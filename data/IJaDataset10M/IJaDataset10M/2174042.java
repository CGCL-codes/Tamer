package com.sun.opengl.impl.macosx.cgl;

import javax.media.opengl.*;
import com.sun.opengl.impl.*;

public class MacOSXOffscreenCGLContext extends MacOSXPbufferCGLContext {

    public MacOSXOffscreenCGLContext(MacOSXPbufferCGLDrawable drawable, GLContext shareWith) {
        super(drawable, shareWith);
    }

    public int getOffscreenContextPixelDataType() {
        GL gl = getGL();
        return gl.isGL2() ? GL2.GL_UNSIGNED_INT_8_8_8_8_REV : GL.GL_UNSIGNED_SHORT_5_5_5_1;
    }

    public int getOffscreenContextReadBuffer() {
        return GL.GL_FRONT;
    }

    public boolean offscreenImageNeedsVerticalFlip() {
        return true;
    }
}

package com.sun.opengl.impl.macosx;

import javax.media.opengl.*;
import com.sun.opengl.impl.*;

public class MacOSXOffscreenGLDrawable extends MacOSXPbufferGLDrawable {

    public MacOSXOffscreenGLDrawable(GLCapabilities capabilities) {
        super(capabilities, 0, 0);
    }

    public GLContext createContext(GLContext shareWith) {
        return new MacOSXOffscreenGLContext(this, shareWith);
    }

    public void setSize(int width, int height) {
        destroy();
        initWidth = width;
        initHeight = height;
        createPbuffer();
    }
}

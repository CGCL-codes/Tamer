package com.sun.opengl.impl.macosx.cgl;

import javax.media.opengl.*;
import javax.media.nativewindow.*;
import com.sun.opengl.impl.*;
import com.sun.nativewindow.impl.NullWindow;

public class MacOSXPbufferCGLDrawable extends MacOSXCGLDrawable {

    private static final boolean DEBUG = Debug.debug("MacOSXPbufferCGLDrawable");

    private int textureTarget;

    private int texture;

    protected long pBuffer;

    public MacOSXPbufferCGLDrawable(GLDrawableFactory factory, AbstractGraphicsScreen absScreen, GLCapabilities caps, GLCapabilitiesChooser chooser, int width, int height) {
        super(factory, new NullWindow(MacOSXCGLGraphicsConfigurationFactory.chooseGraphicsConfigurationStatic(caps, chooser, absScreen, true)), true);
        NullWindow nw = (NullWindow) getNativeWindow();
        nw.setSize(width, height);
        initOpenGLImpl();
        createPbuffer();
    }

    public GLContext createContext(GLContext shareWith) {
        return new MacOSXPbufferCGLContext(this, shareWith);
    }

    public void destroy() {
        if (this.pBuffer != 0) {
            impl.destroy(pBuffer);
            this.pBuffer = 0;
            if (DEBUG) {
                System.err.println("Destroyed pbuffer: " + pBuffer);
            }
        }
    }

    public long getPbuffer() {
        return pBuffer;
    }

    public void swapBuffers() throws GLException {
    }

    private void createPbuffer() {
        NullWindow nw = (NullWindow) getNativeWindow();
        DefaultGraphicsConfiguration config = (DefaultGraphicsConfiguration) nw.getGraphicsConfiguration().getNativeGraphicsConfiguration();
        GLCapabilities capabilities = (GLCapabilities) config.getChosenCapabilities();
        GLProfile glProfile = capabilities.getGLProfile();
        int renderTarget;
        if (glProfile.isGL2() && capabilities.getPbufferRenderToTextureRectangle()) {
            renderTarget = GL2.GL_TEXTURE_RECTANGLE_EXT;
        } else {
            int w = getNextPowerOf2(getWidth());
            int h = getNextPowerOf2(getHeight());
            nw.setSize(w, h);
            renderTarget = GL.GL_TEXTURE_2D;
        }
        int internalFormat = GL.GL_RGBA;
        if (capabilities.getPbufferFloatingPointBuffers()) {
            if (glProfile.isGL2()) {
                switch(capabilities.getRedBits()) {
                    case 16:
                        internalFormat = GL2.GL_RGBA_FLOAT16_APPLE;
                        break;
                    case 32:
                        internalFormat = GL2.GL_RGBA_FLOAT32_APPLE;
                        break;
                    default:
                        throw new GLException("Invalid floating-point bit depth (only 16 and 32 supported)");
                }
            } else {
                internalFormat = GL.GL_RGBA;
            }
        }
        pBuffer = impl.create(renderTarget, internalFormat, getWidth(), getHeight());
        if (pBuffer == 0) {
            throw new GLException("pbuffer creation error: CGL.createPBuffer() failed");
        }
        if (DEBUG) {
            System.err.println("Created pbuffer " + nw + " for " + this);
        }
    }

    private int getNextPowerOf2(int number) {
        if (((number - 1) & number) == 0) {
            return number;
        }
        int power = 0;
        while (number > 0) {
            number = number >> 1;
            power++;
        }
        return (1 << power);
    }

    private boolean haveSetOpenGLMode = false;

    private int openGLMode = NSOPENGL_MODE;

    protected Impl impl;

    public void setOpenGLMode(int mode) {
        if (mode == openGLMode) {
            return;
        }
        if (haveSetOpenGLMode) {
            throw new GLException("Can't switch between using NSOpenGLPixelBuffer and CGLPBufferObj more than once");
        }
        destroy();
        openGLMode = mode;
        haveSetOpenGLMode = true;
        if (DEBUG) {
            System.err.println("Switching PBuffer drawable mode to " + ((mode == MacOSXCGLDrawable.NSOPENGL_MODE) ? "NSOPENGL_MODE" : "CGL_MODE"));
        }
        initOpenGLImpl();
        createPbuffer();
    }

    public int getOpenGLMode() {
        return openGLMode;
    }

    private void initOpenGLImpl() {
        switch(openGLMode) {
            case NSOPENGL_MODE:
                impl = new NSOpenGLImpl();
                break;
            case CGL_MODE:
                impl = new CGLImpl();
                break;
            default:
                throw new InternalError("Illegal implementation mode " + openGLMode);
        }
    }

    interface Impl {

        public long create(int renderTarget, int internalFormat, int width, int height);

        public void destroy(long pbuffer);
    }

    class NSOpenGLImpl implements Impl {

        public long create(int renderTarget, int internalFormat, int width, int height) {
            return CGL.createPBuffer(renderTarget, internalFormat, width, height);
        }

        public void destroy(long pbuffer) {
            CGL.destroyPBuffer(0, pbuffer);
        }
    }

    class CGLImpl implements Impl {

        public long create(int renderTarget, int internalFormat, int width, int height) {
            long[] pbuffer = new long[1];
            int res = CGL.CGLCreatePBuffer(width, height, renderTarget, internalFormat, 0, pbuffer, 0);
            if (res != CGL.kCGLNoError) {
                throw new GLException("Error creating CGL-based pbuffer: error code " + res);
            }
            return pbuffer[0];
        }

        public void destroy(long pbuffer) {
            int res = CGL.CGLDestroyPBuffer(pbuffer);
            if (res != CGL.kCGLNoError) {
                throw new GLException("Error destroying CGL-based pbuffer: error code " + res);
            }
        }
    }
}

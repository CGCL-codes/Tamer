package jogamp.opengl.egl;

import javax.media.nativewindow.*;
import javax.media.nativewindow.egl.EGLGraphicsDevice;
import javax.media.opengl.*;
import com.jogamp.common.JogampRuntimeException;
import com.jogamp.common.util.*;
import jogamp.opengl.*;
import jogamp.nativewindow.WrappedSurface;
import java.util.HashMap;
import java.util.List;

public class EGLDrawableFactory extends GLDrawableFactoryImpl {

    private static final GLDynamicLookupHelper eglES1DynamicLookupHelper;

    private static final GLDynamicLookupHelper eglES2DynamicLookupHelper;

    static {
        new EGLGraphicsConfigurationFactory();
        if (NativeWindowFactory.TYPE_X11.equals(NativeWindowFactory.getNativeWindowType(true))) {
            try {
                ReflectionUtil.createInstance("jogamp.opengl.x11.glx.X11GLXGraphicsConfigurationFactory", EGLDrawableFactory.class.getClassLoader());
            } catch (JogampRuntimeException jre) {
            }
        }
        GLDynamicLookupHelper tmp = null;
        try {
            tmp = new GLDynamicLookupHelper(new EGLES1DynamicLibraryBundleInfo());
        } catch (GLException gle) {
            if (DEBUG) {
                gle.printStackTrace();
            }
        }
        eglES1DynamicLookupHelper = tmp;
        if (null != eglES1DynamicLookupHelper && eglES1DynamicLookupHelper.isLibComplete()) {
            EGL.resetProcAddressTable(eglES1DynamicLookupHelper);
        }
        tmp = null;
        try {
            tmp = new GLDynamicLookupHelper(new EGLES2DynamicLibraryBundleInfo());
        } catch (GLException gle) {
            if (DEBUG) {
                gle.printStackTrace();
            }
        }
        eglES2DynamicLookupHelper = tmp;
        if (null != eglES2DynamicLookupHelper && eglES2DynamicLookupHelper.isLibComplete()) {
            EGL.resetProcAddressTable(eglES2DynamicLookupHelper);
        }
    }

    public EGLDrawableFactory() {
        super();
        defaultDevice = new EGLGraphicsDevice(AbstractGraphicsDevice.DEFAULT_CONNECTION, AbstractGraphicsDevice.DEFAULT_UNIT);
    }

    static class SharedResource {

        private EGLGraphicsDevice device;

        SharedResource(EGLGraphicsDevice dev) {
            device = dev;
        }

        EGLGraphicsDevice getDevice() {
            return device;
        }
    }

    HashMap sharedMap = new HashMap();

    EGLGraphicsDevice defaultDevice;

    public final AbstractGraphicsDevice getDefaultDevice() {
        return defaultDevice;
    }

    public final boolean getIsDeviceCompatible(AbstractGraphicsDevice device) {
        return true;
    }

    private SharedResource getOrCreateShared(AbstractGraphicsDevice device) {
        String connection = device.getConnection();
        SharedResource sr;
        synchronized (sharedMap) {
            sr = (SharedResource) sharedMap.get(connection);
        }
        if (null == sr) {
            long eglDisplay = EGL.eglGetDisplay(EGL.EGL_DEFAULT_DISPLAY);
            if (eglDisplay == EGL.EGL_NO_DISPLAY) {
                throw new GLException("Failed to created EGL default display: error 0x" + Integer.toHexString(EGL.eglGetError()));
            } else if (DEBUG) {
                System.err.println("eglDisplay(EGL_DEFAULT_DISPLAY): 0x" + Long.toHexString(eglDisplay));
            }
            if (!EGL.eglInitialize(eglDisplay, null, null)) {
                throw new GLException("eglInitialize failed" + ", error 0x" + Integer.toHexString(EGL.eglGetError()));
            }
            EGLGraphicsDevice sharedDevice = new EGLGraphicsDevice(eglDisplay, connection, device.getUnitID());
            sr = new SharedResource(sharedDevice);
            synchronized (sharedMap) {
                sharedMap.put(connection, sr);
            }
            if (DEBUG) {
                System.err.println("!!! SharedDevice: " + sharedDevice);
            }
        }
        return sr;
    }

    protected final GLContext getOrCreateSharedContextImpl(AbstractGraphicsDevice device) {
        return null;
    }

    protected AbstractGraphicsDevice getOrCreateSharedDeviceImpl(AbstractGraphicsDevice device) {
        SharedResource sr = getOrCreateShared(device);
        if (null != sr) {
            return sr.getDevice();
        }
        return null;
    }

    SharedResource getOrCreateSharedResource(AbstractGraphicsDevice device) {
        return (SharedResource) getOrCreateShared(device);
    }

    public GLDynamicLookupHelper getGLDynamicLookupHelper(int esProfile) {
        if (2 == esProfile) {
            if (null == eglES2DynamicLookupHelper) {
                throw new GLException("GLDynamicLookupHelper for ES2 not available");
            }
            return eglES2DynamicLookupHelper;
        } else if (1 == esProfile) {
            if (null == eglES1DynamicLookupHelper) {
                throw new GLException("GLDynamicLookupHelper for ES1 not available");
            }
            return eglES1DynamicLookupHelper;
        } else {
            throw new GLException("Unsupported: ES" + esProfile);
        }
    }

    protected final void shutdownInstance() {
    }

    protected List getAvailableCapabilitiesImpl(AbstractGraphicsDevice device) {
        return EGLGraphicsConfigurationFactory.getAvailableCapabilities(this, device);
    }

    protected GLDrawableImpl createOnscreenDrawableImpl(NativeSurface target) {
        if (target == null) {
            throw new IllegalArgumentException("Null target");
        }
        return new EGLOnscreenDrawable(this, target);
    }

    protected GLDrawableImpl createOffscreenDrawableImpl(NativeSurface target) {
        if (target == null) {
            throw new IllegalArgumentException("Null target");
        }
        AbstractGraphicsConfiguration config = target.getGraphicsConfiguration().getNativeGraphicsConfiguration();
        GLCapabilitiesImmutable caps = (GLCapabilitiesImmutable) config.getChosenCapabilities();
        if (!caps.isPBuffer()) {
            throw new GLException("Not yet implemented");
        }
        return new EGLPbufferDrawable(this, target);
    }

    public boolean canCreateGLPbuffer(AbstractGraphicsDevice device) {
        return true;
    }

    protected NativeSurface createOffscreenSurfaceImpl(AbstractGraphicsDevice device, GLCapabilitiesImmutable capsChosen, GLCapabilitiesImmutable capsRequested, GLCapabilitiesChooser chooser, int width, int height) {
        WrappedSurface ns = new WrappedSurface(EGLGraphicsConfigurationFactory.createOffscreenGraphicsConfiguration(device, capsChosen, capsRequested, chooser));
        ns.setSize(width, height);
        return ns;
    }

    protected ProxySurface createProxySurfaceImpl(AbstractGraphicsDevice device, long windowHandle, GLCapabilitiesImmutable capsRequested, GLCapabilitiesChooser chooser) {
        WrappedSurface ns = new WrappedSurface(EGLGraphicsConfigurationFactory.createOffscreenGraphicsConfiguration(device, capsRequested, capsRequested, chooser), windowHandle);
        return ns;
    }

    protected GLContext createExternalGLContextImpl() {
        AbstractGraphicsScreen absScreen = DefaultGraphicsScreen.createDefault(NativeWindowFactory.TYPE_EGL);
        return new EGLExternalContext(absScreen);
    }

    public boolean canCreateExternalGLDrawable(AbstractGraphicsDevice device) {
        return false;
    }

    protected GLDrawable createExternalGLDrawableImpl() {
        throw new GLException("Not yet implemented");
    }

    public boolean canCreateContextOnJava2DSurface(AbstractGraphicsDevice device) {
        return false;
    }

    public GLContext createContextOnJava2DSurface(Object graphics, GLContext shareWith) throws GLException {
        throw new GLException("Unimplemented on this platform");
    }
}

package org.lwjgl.openal;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;

/**
 * <p>
 * The AL class implements the actual creation code for linking to the native library
 * OpenAL.
 * </p>
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 3418 $
 * $Id: AL.java 3418 2010-09-28 21:11:35Z spasi $
 */
public final class AL {

    /** ALCdevice instance. */
    static ALCdevice device;

    /** Current ALCcontext. */
    static ALCcontext context;

    /** Have we been created? */
    private static boolean created;

    static {
        Sys.initialize();
    }

    private AL() {
    }

    /**
	 * Native method to create AL instance
	 *
	 * @param oalPath Path to search for OpenAL library
	 */
    private static native void nCreate(String oalPath) throws LWJGLException;

    /**
	 * Native method to create AL instance from the Mac OS X 10.4 OpenAL framework.
	 * It is only defined in the Mac OS X native library.
	 */
    private static native void nCreateDefault() throws LWJGLException;

    /**
	 * Native method the destroy the AL
	 */
    private static native void nDestroy();

    /**
	 * @return true if AL has been created
	 */
    public static boolean isCreated() {
        return created;
    }

    /**
	 * Creates an OpenAL instance. Using this constructor will cause OpenAL to
	 * open the device using supplied device argument, and create a context using the context values
	 * supplied.
	 *
	 * @param deviceArguments Arguments supplied to native device
	 * @param contextFrequency Frequency for mixing output buffer, in units of Hz (Common values include 11025, 22050, and 44100).
	 * @param contextRefresh Refresh intervalls, in units of Hz.
	 * @param contextSynchronized Flag, indicating a synchronous context.*
	 */
    public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized) throws LWJGLException {
        create(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, true);
    }

    /**
	 * @param openDevice Whether to automatically open the device
	 * @see #create(String, int, int, boolean)
	 */
    public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized, boolean openDevice) throws LWJGLException {
        if (created) throw new IllegalStateException("Only one OpenAL context may be instantiated at any one time.");
        String libname;
        String[] library_names;
        switch(LWJGLUtil.getPlatform()) {
            case LWJGLUtil.PLATFORM_WINDOWS:
                libname = "OpenAL32";
                library_names = new String[] { "OpenAL64.dll", "OpenAL32.dll" };
                break;
            case LWJGLUtil.PLATFORM_LINUX:
                libname = "openal";
                library_names = new String[] { "libopenal64.so", "libopenal.so", "libopenal.so.0" };
                break;
            case LWJGLUtil.PLATFORM_MACOSX:
                libname = "openal";
                library_names = new String[] { "openal.dylib" };
                break;
            default:
                throw new LWJGLException("Unknown platform: " + LWJGLUtil.getPlatform());
        }
        String[] oalPaths = LWJGLUtil.getLibraryPaths(libname, library_names, AL.class.getClassLoader());
        LWJGLUtil.log("Found " + oalPaths.length + " OpenAL paths");
        for (String oalPath : oalPaths) {
            try {
                nCreate(oalPath);
                created = true;
                init(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, openDevice);
                break;
            } catch (LWJGLException e) {
                LWJGLUtil.log("Failed to load " + oalPath + ": " + e.getMessage());
            }
        }
        if (!created && LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_MACOSX) {
            nCreateDefault();
            created = true;
            init(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, openDevice);
        }
        if (!created) throw new LWJGLException("Could not locate OpenAL library.");
    }

    private static void init(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized, boolean openDevice) throws LWJGLException {
        try {
            AL10.initNativeStubs();
            ALC10.initNativeStubs();
            if (openDevice) {
                device = ALC10.alcOpenDevice(deviceArguments);
                if (device == null) {
                    throw new LWJGLException("Could not open ALC device");
                }
                if (contextFrequency == -1) {
                    context = ALC10.alcCreateContext(device, null);
                } else {
                    context = ALC10.alcCreateContext(device, ALCcontext.createAttributeList(contextFrequency, contextRefresh, contextSynchronized ? ALC10.ALC_TRUE : ALC10.ALC_FALSE));
                }
                ALC10.alcMakeContextCurrent(context);
            }
        } catch (LWJGLException e) {
            destroy();
            throw e;
        }
        ALC11.initialize();
        if (ALC10.alcIsExtensionPresent(device, EFX10.ALC_EXT_EFX_NAME)) {
            EFX10.initNativeStubs();
        }
    }

    /**
	 * Creates an OpenAL instance. The empty create will cause OpenAL to
	 * open the default device, and create a context using default values.
	 * This method used to use default values that the OpenAL implementation
	 * chose but this produces unexpected results on some systems; so now
	 * it defaults to 44100Hz mixing @ 60Hz refresh.
	 */
    public static void create() throws LWJGLException {
        create(null, 44100, 60, false);
    }

    /**
	 * Exit cleanly by calling destroy.
	 */
    public static void destroy() {
        if (context != null) {
            ALC10.alcMakeContextCurrent(null);
            ALC10.alcDestroyContext(context);
            context = null;
        }
        if (device != null) {
            boolean result = ALC10.alcCloseDevice(device);
            device = null;
        }
        resetNativeStubs(AL10.class);
        resetNativeStubs(AL11.class);
        resetNativeStubs(ALC10.class);
        resetNativeStubs(ALC11.class);
        resetNativeStubs(EFX10.class);
        if (created) nDestroy();
        created = false;
    }

    private static native void resetNativeStubs(Class clazz);

    /**
	 * @return handle to the default AL context.
	 */
    public static ALCcontext getContext() {
        return context;
    }

    /**
	 * @return handle to the default AL device.
	 */
    public static ALCdevice getDevice() {
        return device;
    }
}

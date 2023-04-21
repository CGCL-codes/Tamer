package com.shingarov.damianiza.ni.driver;

/**
 * Low-level Platform interface to NVidia CUDA Driver API.
 * It is low-level in the sense that it tries to stay as close as possible
 * to the native API, not introducing any object-oriented sugar.
 * In particular, arguments and return values are translated as literally
 * as possible across the JNI boundary.  In large JNI projects (such as
 * SWT), this proves to be the only workable approach, because debugging
 * across both sides of JNI would quickly outgrow human capability.
 * There are three departures from SWT-PI design here: (1) Conversion of
 * Strings between C and Java is done on the native side by the JVM;
 * (2) when we get an error code back from a platform call, we throw an
 * exception from JNI, which allows us to (3) return output parameters as
 * the return value -- much simpler to return an int than to stuff it into
 * an array.
 *
 * @author Boris Shingarov
 */
public class CudaDriver {

    static {
        System.loadLibrary("damianiza");
    }

    /**
	 * Initialize the CUDA driver API 
	 */
    public static native void cuInit(int flags);

    /**
	 * Returns the compute capability of the device.
	 * 
	 * @param dev Native device handle
	 * @return ComputeCapability of the specified device
	 */
    public static native ComputeCapability cuDeviceComputeCapability(long dev);

    /**
	 * Returns the native handle to a compute device given its ordinal
	 * in the range [0, cuDeviceGetCount()-1].
	 * 
	 * @param ordinal  
	 * @return Native device handle
	 */
    public static native long cuDeviceGet(int ordinal);

    /**
	 * Returns the integer value of the attribute attrib on device dev.
	 * 
	 * @param attrib One of the integers specified in CUdevice_attribute
	 * @param dev Native device handle
	 * @return 
	 */
    public static native int cuDeviceGetAttribute(int attrib, long dev);

    /**
	 * Returns the number of compute-capable devices.
	 * @return Number of compute-capable devices
	 */
    public static native int cuDeviceGetCount();

    /**
	 * Returns an identifer string for the device.
	 * 
	 * @param dev Native device handle
	 * @return String Device Identifier
	 */
    public static native String cuDeviceGetName(long dev);

    /**
	 * Returns properties for a selected device.
	 * 
	 * @param dev Native device handle
	 * @return The device properties
	 */
    public static native CUdevprop cuDeviceGetProperties(long dev);

    /**
	 * Returns the total amount of memory on the device.
	 *  
	 * @param dev Native device handle
	 * @return Number of bytes
	 */
    public static native long cuDeviceTotalMem(long dev);

    public static native long cuModuleLoad(String fname);

    public static native void cuModuleUnload(long handle);
}

package com.trendsoft.eye;

import com.trendsoft.eye.classloader.EyeClassLoader;

/**
 * @author vasiliy
 *
 * Internal interface to be used for debugging. Implementations of this
 * interface can be registered as a listener on a {@link EyeClassLoader}.
 */
public interface EyeListener {

    /**
     * Notify that class was patched.
     * @param className name of the patched class
     * @param patchedByteCode TODO
     */
    void classPatched(String className, byte[] patchedByteCode);
}

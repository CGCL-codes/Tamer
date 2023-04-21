package com.apple.mrj.jdirect;

/**
 * This is a simple stub for this class which is specific to the 1.1.x
 * virtual machine from Apple. It allows developers to compile the
 * MRJ Adapter library on platforms other than Mac OS with the 1.1.x VM.
 * You don't need to use these stubs if you're using the precompiled
 * version of MRJ Adapter.
 *
 * @author Steve Roy
 */
public class MethodClosureUPP extends MethodClosure {

    protected MethodClosureUPP(Object targetObject, String methodName, String methodSignature, int procInfo) {
        super(targetObject, methodName, methodSignature);
    }

    public int getProc() {
        return -1;
    }
}

package org.ogre4j;

import org.xbig.base.*;

public class PassthroughControllerFunction extends org.xbig.base.NativeObject implements org.ogre4j.IPassthroughControllerFunction {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public PassthroughControllerFunction(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected PassthroughControllerFunction(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public PassthroughControllerFunction(org.xbig.base.WithoutNativeObject val) {
        super(val);
    }

    public void delete() {
        if (this.remote) {
            throw new RuntimeException("can't dispose object created by native library");
        }
        if (!this.deleted) {
            __delete(object.pointer);
            this.deleted = true;
            this.object.pointer = 0;
        }
    }

    public void finalize() {
        if (!this.remote && !this.deleted) {
            delete();
        }
    }

    private final native void __delete(long _pointer_);

    /** 
    Constructor. **/
    public PassthroughControllerFunction(boolean deltaInput) {
        super(new org.xbig.base.InstancePointer(__createPassthroughControllerFunction__bv(deltaInput)), false);
    }

    private static native long __createPassthroughControllerFunction__bv(boolean deltaInput);

    /** 
    Overriden function. **/
    public float calculate(float source) {
        return _calculate__Realv(this.object.pointer, source);
    }

    private native float _calculate__Realv(long _pointer_, float source);
}

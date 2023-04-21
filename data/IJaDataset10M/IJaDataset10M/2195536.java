package org.ogre4j;

import org.xbig.base.*;

public class ScaleControllerFunction extends org.xbig.base.NativeObject implements org.ogre4j.IScaleControllerFunction {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public ScaleControllerFunction(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected ScaleControllerFunction(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public ScaleControllerFunction(org.xbig.base.WithoutNativeObject val) {
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
    Constructor, requires a scale factor. **/
    public ScaleControllerFunction(float scalefactor, boolean deltaInput) {
        super(new org.xbig.base.InstancePointer(__createScaleControllerFunction__Realvbv(scalefactor, deltaInput)), false);
    }

    private static native long __createScaleControllerFunction__Realvbv(float scalefactor, boolean deltaInput);

    /** 
    Overridden method. **/
    public float calculate(float source) {
        return _calculate__Realv(this.object.pointer, source);
    }

    private native float _calculate__Realv(long _pointer_, float source);
}

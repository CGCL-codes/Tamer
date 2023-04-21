package org.ogre4j.overlayelementcommands;

import org.xbig.base.*;

public class CmdVerticalAlign extends org.xbig.base.NativeObject implements org.ogre4j.overlayelementcommands.ICmdVerticalAlign {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public CmdVerticalAlign(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected CmdVerticalAlign(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public CmdVerticalAlign(org.xbig.base.WithoutNativeObject val) {
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

    /** **/
    public String doGet(VoidPointer target) {
        return _doGet__vP_const(this.object.pointer, target.object.pointer);
    }

    private native String _doGet__vP_const(long _pointer_, long target);

    /** **/
    public void doSet(VoidPointer target, String val) {
        _doSet__vpStringR(this.object.pointer, target.object.pointer, val);
    }

    private native void _doSet__vpStringR(long _pointer_, long target, String val);

    /** **/
    public CmdVerticalAlign() {
        super(new org.xbig.base.InstancePointer(__createCmdVerticalAlign()), false);
    }

    private static native long __createCmdVerticalAlign();
}

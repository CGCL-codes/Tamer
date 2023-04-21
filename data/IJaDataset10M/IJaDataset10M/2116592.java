package org.ogre4j;

import org.xbig.base.*;

public class AnimableValuePtr extends org.xbig.base.NativeObject implements org.ogre4j.IAnimableValuePtr {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public AnimableValuePtr(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected AnimableValuePtr(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public AnimableValuePtr(org.xbig.base.WithoutNativeObject val) {
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
    public AnimableValuePtr() {
        super(new org.xbig.base.InstancePointer(__createAnimableValuePtr()), false);
    }

    private static native long __createAnimableValuePtr();

    /** **/
    public org.ogre4j.IAnimableValuePtr operatorAssignment(org.ogre4j.IAnimableValuePtr r) {
        return new org.ogre4j.AnimableValuePtr(new InstancePointer(_operatorAssignment___Ogre_SharedPtrR(this.object.pointer, r.getInstancePointer().pointer)));
    }

    private native long _operatorAssignment___Ogre_SharedPtrR(long _pointer_, long r);

    /** **/
    public org.ogre4j.IAnimableValue operatorMultiplication() {
        return new org.ogre4j.AnimableValue(new InstancePointer(_operatorMultiplication_const(this.object.pointer)));
    }

    private native long _operatorMultiplication_const(long _pointer_);

    /** **/
    public org.ogre4j.IAnimableValue operatorMemberAccessFromAPointer() {
        return new org.ogre4j.AnimableValue(new InstancePointer(_operatorMemberAccessFromAPointer_const(this.object.pointer)));
    }

    private native long _operatorMemberAccessFromAPointer_const(long _pointer_);

    /** **/
    public org.ogre4j.IAnimableValue get() {
        return new org.ogre4j.AnimableValue(new InstancePointer(_get_const(this.object.pointer)));
    }

    private native long _get_const(long _pointer_);

    /** **/
    public void bind(org.ogre4j.IAnimableValue rep, org.ogre4j.SharedPtrFreeMethod freeMethod) {
        _bind__Ogre_AnimableValuep_Ogre_SharedPtrFreeMethodv(this.object.pointer, rep.getInstancePointer().pointer, freeMethod.getValue());
    }

    private native void _bind__Ogre_AnimableValuep_Ogre_SharedPtrFreeMethodv(long _pointer_, long rep, int freeMethod);

    /** **/
    public boolean unique() {
        return _unique_const(this.object.pointer);
    }

    private native boolean _unique_const(long _pointer_);

    /** **/
    public long useCount() {
        return _useCount_const(this.object.pointer);
    }

    private native long _useCount_const(long _pointer_);

    /** **/
    public LongPointer useCountPointer() {
        return new LongPointer(new InstancePointer(_useCountPointer_const(this.object.pointer)));
    }

    private native long _useCountPointer_const(long _pointer_);

    /** **/
    public org.ogre4j.IAnimableValue getPointer() {
        return new org.ogre4j.AnimableValue(new InstancePointer(_getPointer_const(this.object.pointer)));
    }

    private native long _getPointer_const(long _pointer_);

    /** **/
    public org.ogre4j.SharedPtrFreeMethod freeMethod() {
        return org.ogre4j.SharedPtrFreeMethod.toEnum(_freeMethod_const(this.object.pointer));
    }

    private native int _freeMethod_const(long _pointer_);

    /** **/
    public boolean isNull() {
        return _isNull_const(this.object.pointer);
    }

    private native boolean _isNull_const(long _pointer_);

    /** **/
    public void setNull() {
        _setNull(this.object.pointer);
    }

    private native void _setNull(long _pointer_);
}

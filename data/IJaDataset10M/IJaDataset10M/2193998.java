package org.ogre4j;

import org.xbig.base.*;

public class RaySceneQueryResultEntry extends org.xbig.base.NativeObject implements org.ogre4j.IRaySceneQueryResultEntry {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public RaySceneQueryResultEntry(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected RaySceneQueryResultEntry(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public RaySceneQueryResultEntry(org.xbig.base.WithoutNativeObject val) {
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
    public boolean operatorLessThan(org.ogre4j.IRaySceneQueryResultEntry rhs) {
        return _operatorLessThan__RaySceneQueryResultEntryR_const(this.object.pointer, rhs.getInstancePointer().pointer);
    }

    private native boolean _operatorLessThan__RaySceneQueryResultEntryR_const(long _pointer_, long rhs);

    /** **/
    public RaySceneQueryResultEntry() {
        super(new org.xbig.base.InstancePointer(__createRaySceneQueryResultEntry()), false);
    }

    private static native long __createRaySceneQueryResultEntry();

    /** **/
    public float getdistance() {
        return _getdistance(this.object.pointer);
    }

    private native float _getdistance(long _pointer_);

    /** **/
    public void setdistance(float _jni_value_) {
        _setdistance(this.object.pointer, _jni_value_);
    }

    private native void _setdistance(long _pointer_, float _jni_value_);

    /** **/
    public org.ogre4j.IMovableObject getmovable() {
        return new org.ogre4j.MovableObject(new InstancePointer(_getmovable(this.object.pointer)));
    }

    private native long _getmovable(long _pointer_);

    /** **/
    public void setmovable(org.ogre4j.IMovableObject _jni_value_) {
        _setmovable(this.object.pointer, _jni_value_.getInstancePointer().pointer);
    }

    private native void _setmovable(long _pointer_, long _jni_value_);

    /** **/
    public org.ogre4j.ISceneQuery.IWorldFragment getworldFragment() {
        return new org.ogre4j.SceneQuery.WorldFragment(new InstancePointer(_getworldFragment(this.object.pointer)));
    }

    private native long _getworldFragment(long _pointer_);

    /** **/
    public void setworldFragment(org.ogre4j.ISceneQuery.IWorldFragment _jni_value_) {
        _setworldFragment(this.object.pointer, _jni_value_.getInstancePointer().pointer);
    }

    private native void _setworldFragment(long _pointer_, long _jni_value_);
}

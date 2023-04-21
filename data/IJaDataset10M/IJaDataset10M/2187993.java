package org.ogre4j;

import org.xbig.base.*;

public class RaySceneQueryListener extends org.xbig.base.NativeObject implements org.ogre4j.IRaySceneQueryListener {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public RaySceneQueryListener(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected RaySceneQueryListener(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public RaySceneQueryListener(org.xbig.base.WithoutNativeObject val) {
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
    Called when a movable objects intersects the ray. **/
    public boolean queryResult(org.ogre4j.IMovableObject obj, float distance) {
        return _queryResult__MovableObjectpRealv(this.object.pointer, obj.getInstancePointer().pointer, distance);
    }

    private native boolean _queryResult__MovableObjectpRealv(long _pointer_, long obj, float distance);

    /** 
    Called when a world fragment is intersected by the ray. **/
    public boolean queryResult(org.ogre4j.ISceneQuery.IWorldFragment fragment, float distance) {
        return _queryResult__SceneQuery_WorldFragmentpRealv(this.object.pointer, fragment.getInstancePointer().pointer, distance);
    }

    private native boolean _queryResult__SceneQuery_WorldFragmentpRealv(long _pointer_, long fragment, float distance);
}

package org.ogre4j;

import org.xbig.base.*;

public class OverlayElementFactory extends org.xbig.base.NativeObject implements org.ogre4j.IOverlayElementFactory {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public OverlayElementFactory(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected OverlayElementFactory(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public OverlayElementFactory(org.xbig.base.WithoutNativeObject val) {
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
    Creates a new  instance with the name supplied. **/
    public org.ogre4j.IOverlayElement createOverlayElement(String instanceName) {
        return new org.ogre4j.OverlayElement(new InstancePointer(_createOverlayElement__StringR(this.object.pointer, instanceName)));
    }

    private native long _createOverlayElement__StringR(long _pointer_, String instanceName);

    /** 
    Destroys a  which this factory created previously. **/
    public void destroyOverlayElement(org.ogre4j.IOverlayElement pElement) {
        _destroyOverlayElement__OverlayElementp(this.object.pointer, pElement.getInstancePointer().pointer);
    }

    private native void _destroyOverlayElement__OverlayElementp(long _pointer_, long pElement);

    /** 
    Gets the string uniquely identifying the type of element this factory creates. **/
    public String getTypeName() {
        return _getTypeName_const(this.object.pointer);
    }

    private native String _getTypeName_const(long _pointer_);
}

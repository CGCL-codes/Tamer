package org.ogre4j;

import org.xbig.base.*;

public class MeshSerializerImpl extends org.xbig.base.NativeObject implements org.ogre4j.IMeshSerializerImpl {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public MeshSerializerImpl(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected MeshSerializerImpl(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public MeshSerializerImpl(org.xbig.base.WithoutNativeObject val) {
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
    public MeshSerializerImpl() {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** 
    Exports a mesh to the file specified. **/
    public void exportMesh(org.ogre4j.IMesh pMesh, String filename, org.ogre4j.Serializer.Endian endianMode) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** 
    Imports  and (optionally)  data from a .mesh file . **/
    public void importMesh(org.ogre4j.IDataStreamPtr stream, org.ogre4j.IMesh pDest, org.ogre4j.IMeshSerializerListener listener) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }
}

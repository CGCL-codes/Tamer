package org.ogre4j;

import org.xbig.base.*;

public class ZipArchive extends org.xbig.base.NativeObject implements org.ogre4j.IZipArchive {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public ZipArchive(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected ZipArchive(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public ZipArchive(org.xbig.base.WithoutNativeObject val) {
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
    public ZipArchive(String name, String archType) {
        super(new org.xbig.base.InstancePointer(__createZipArchive__StringRStringR(name, archType)), false);
    }

    private static native long __createZipArchive__StringRStringR(String name, String archType);

    /** **/
    public boolean isCaseSensitive() {
        return _isCaseSensitive_const(this.object.pointer);
    }

    private native boolean _isCaseSensitive_const(long _pointer_);

    /** **/
    public void load() {
        _load(this.object.pointer);
    }

    private native void _load(long _pointer_);

    /** **/
    public void unload() {
        _unload(this.object.pointer);
    }

    private native void _unload(long _pointer_);

    /** **/
    public void open(org.ogre4j.IDataStreamPtr returnValue, String filename) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_open__StringR_const(this.object.pointer, filename)), false);
    }

    private native long _open__StringR_const(long _pointer_, String filename);

    /** **/
    public void list(org.ogre4j.IStringVectorPtr returnValue, boolean recursive, boolean dirs) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_list__bvbv(this.object.pointer, recursive, dirs)), false);
    }

    private native long _list__bvbv(long _pointer_, boolean recursive, boolean dirs);

    /** **/
    public void listFileInfo(org.ogre4j.IFileInfoListPtr returnValue, boolean recursive, boolean dirs) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_listFileInfo__bvbv(this.object.pointer, recursive, dirs)), false);
    }

    private native long _listFileInfo__bvbv(long _pointer_, boolean recursive, boolean dirs);

    /** **/
    public void find(org.ogre4j.IStringVectorPtr returnValue, String pattern, boolean recursive, boolean dirs) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_find__StringRbvbv(this.object.pointer, pattern, recursive, dirs)), false);
    }

    private native long _find__StringRbvbv(long _pointer_, String pattern, boolean recursive, boolean dirs);

    /** **/
    public void findFileInfo(org.ogre4j.IFileInfoListPtr returnValue, String pattern, boolean recursive, boolean dirs) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_findFileInfo__StringRbvbv(this.object.pointer, pattern, recursive, dirs)), false);
    }

    private native long _findFileInfo__StringRbvbv(long _pointer_, String pattern, boolean recursive, boolean dirs);

    /** **/
    public boolean exists(String filename) {
        return _exists__StringR(this.object.pointer, filename);
    }

    private native boolean _exists__StringR(long _pointer_, String filename);

    /** **/
    public long getModifiedTime(String filename) {
        return _getModifiedTime__StringR(this.object.pointer, filename);
    }

    private native long _getModifiedTime__StringR(long _pointer_, String filename);

    /** **/
    public String getName() {
        return _getName_const(this.object.pointer);
    }

    private native String _getName_const(long _pointer_);

    /** **/
    public String getType() {
        return _getType_const(this.object.pointer);
    }

    private native String _getType_const(long _pointer_);
}

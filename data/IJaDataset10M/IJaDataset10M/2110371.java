package org.ogre4j;

import org.xbig.base.*;

public class ILImageCodec extends org.xbig.base.NativeObject implements org.ogre4j.IILImageCodec {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public ILImageCodec(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected ILImageCodec(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public ILImageCodec(org.xbig.base.WithoutNativeObject val) {
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
    public ILImageCodec(String type, long ilType) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** **/
    public void code(org.ogre4j.IDataStreamPtr returnValue, org.ogre4j.IMemoryDataStreamPtr input, org.ogre4j.ICodec.ICodecDataPtr pData) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** **/
    public void codeToFile(org.ogre4j.IMemoryDataStreamPtr input, String outFileName, org.ogre4j.ICodec.ICodecDataPtr pData) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** **/
    public void decode(org.ogre4j.ICodec.IDecodeResult returnValue, org.ogre4j.IDataStreamPtr input) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** **/
    public void initialiseIL() {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** **/
    public String getType() {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** **/
    public String magicNumberToFileExt(String magicNumberPtr, int maxbytes) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** **/
    public String getDataType() {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** 
    Returns whether a magic number header matches this codec. **/
    public boolean magicNumberMatch(String magicNumberPtr, int maxbytes) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** 
    Registers a new codec in the database. **/
    public static void registerCodec(org.ogre4j.ICodec pCodec) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** 
    Unregisters a codec from the database. **/
    public static void unRegisterCodec(org.ogre4j.ICodec pCodec) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** 
    Gets the iterator for the registered codecs. **/
    public static void getCodecIterator(org.ogre4j.ICodec.ICodecIterator returnValue) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** 
    Gets the file extension list for the registered codecs. **/
    public static void getExtensions(org.ogre4j.IStringVector returnValue) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** 
    Gets the codec registered for the passed in file extension. **/
    public static org.ogre4j.ICodec getCodec(String extension) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }

    /** 
    Gets the codec that can handle the given 'magic' identifier. **/
    public static org.ogre4j.ICodec getCodec(BytePointer magicNumberPtr, int maxbytes) {
        throw new UnsupportedOperationException("This type is on ignore list!");
    }
}

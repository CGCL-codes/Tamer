package org.ogre4j;

import org.xbig.base.*;

public class NedAllocImpl extends org.xbig.base.NativeObject implements org.ogre4j.INedAllocImpl {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public NedAllocImpl(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected NedAllocImpl(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public NedAllocImpl(org.xbig.base.WithoutNativeObject val) {
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
    public static VoidPointer allocBytes(int count, String file, int line, String func) {
        return new VoidPointer(new InstancePointer(_allocBytes__ivcPivcP(count, file, line, func)));
    }

    private static native long _allocBytes__ivcPivcP(int count, String file, int line, String func);

    /** **/
    public static void deallocBytes(VoidPointer ptr) {
        _deallocBytes__vp(ptr.object.pointer);
    }

    private static native void _deallocBytes__vp(long ptr);

    /** **/
    public static VoidPointer allocBytesAligned(int align, int count, String file, int line, String func) {
        return new VoidPointer(new InstancePointer(_allocBytesAligned__ivcPivcP(align, count, file, line, func)));
    }

    private static native long _allocBytesAligned__ivcPivcP(int align, int count, String file, int line, String func);

    /** **/
    public static void deallocBytesAligned(int align, VoidPointer ptr) {
        _deallocBytesAligned__vp(align, ptr.object.pointer);
    }

    private static native void _deallocBytesAligned__vp(int align, long ptr);
}

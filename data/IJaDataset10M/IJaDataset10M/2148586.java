package sun.misc;

public class VMInspector {

    public static native void exit(int status);

    public static native boolean enableGC();

    public static native boolean disableGC();

    public static native boolean gcIsDisabled();

    public static native void keepAllObjectsAlive(boolean keepAlive);

    /**
     * Returns an object reference for the specified object address.
     *
     * @return	an object reference which refers to the specified object
     * 		address.
     * @exception IllegalStateException if the GC is not disabled before this
     *		  method is called.
     * @exception IllegalArgumentException if the specified object address
     *		  does not point to a valid object.
     */
    public static native Object addrToObject(long objAddr) throws IllegalStateException, IllegalArgumentException;

    public static native long objectToAddr(Object obj) throws IllegalStateException;

    public static native void dumpObject(long objAddr);

    public static native void dumpClassBlock(long cbAddr);

    public static native void dumpObjectReferences(long objAddr);

    public static native void dumpClassReferences(String classname);

    public static native void dumpClassBlocks(String classname);

    public static native void dumpSysInfo();

    public static native void dumpHeapSimple();

    public static native void dumpHeapVerbose();

    public static native void dumpHeapStats();

    public static native void dumpObjectGCRoots(long objAddr);

    public static final int SORT_NONE = 0;

    public static final int SORT_BY_OBJ = 1;

    public static final int SORT_BY_OBJCLASS = 2;

    public static native void captureHeapState(String name);

    public static native void releaseHeapState(int id);

    public static native void releaseAllHeapState();

    public static native void listHeapStates();

    public static native void dumpHeapState(int id, int sortKey);

    public static native void compareHeapState(int id1, int id2);

    public static native void listAllClasses();

    public static native void listAllThreads();

    public static native void dumpAllThreads();

    public static native void dumpStack(long eeAddr);
}

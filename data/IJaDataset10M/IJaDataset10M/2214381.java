package org.jikesrvm.objectmodel;

import org.jikesrvm.ArchitectureSpecific.VM_Assembler;
import org.jikesrvm.VM;
import org.jikesrvm.VM_SizeConstants;
import org.jikesrvm.classloader.VM_Array;
import org.jikesrvm.classloader.VM_Class;
import org.jikesrvm.classloader.VM_Type;
import org.jikesrvm.memorymanagers.mminterface.MM_Interface;
import org.jikesrvm.runtime.VM_Magic;
import org.jikesrvm.scheduler.VM_Lock;
import org.jikesrvm.scheduler.VM_Thread;
import org.vmmagic.pragma.Entrypoint;
import org.vmmagic.pragma.Inline;
import org.vmmagic.pragma.Interruptible;
import org.vmmagic.pragma.Uninterruptible;
import org.vmmagic.unboxed.Address;
import org.vmmagic.unboxed.Extent;
import org.vmmagic.unboxed.ObjectReference;
import org.vmmagic.unboxed.Offset;
import org.vmmagic.unboxed.Word;

/**
 * The interface to the object model definition accessible to the
 * virtual machine. <p>
 *
 * Conceptually each Java object is composed of the following pieces:
 * <ul>
 * <li> The JavaHeader defined by {@link VM_JavaHeader}. This portion of the
 *      object supports language-level functions such as locking, hashcodes,
 *      dynamic type checking, virtual function invocation, and array length.
 * <li> The GCHeader defined by {@link MM_Interface}. This portion
 *      of the object supports allocator-specific requirements such as
 *      mark/barrier bits, reference counts, etc.
 * <li> The MiscHeader defined by {@link VM_MiscHeader}. This portion supports
 *      various other clients that want to add bits/words to all objects.
 *      Typical uses are profiling and instrumentation (basically this is a
 *      way to add an instance field to java.lang.Object).
 * <li> The instance fields.  Currently defined by various classloader classes.
 *      Factoring this code out and making it possible to lay out the instance
 *      fields in different ways is a todo item.
 * </ul>
 *
 * Every object's header contains the three portions outlined above.
 *
 * <pre>
 * |<- lo memory                                        hi memory ->|
 *
 *   SCALAR LAYOUT:
 * |<---------- scalar header --------->|
 * +----------+------------+------------+------+------+------+--------+
 * | GCHeader | MiscHeader | JavaHeader | fldO | fld1 | fldx | fldN-1 |
 * +----------+------------+------------+------+------+------+--------+
 *                         ^ JHOFF             ^objref
 *                                             .
 *    ARRAY LAYOUT:                            .
 * |<---------- array header ----------------->|
 * +----------+------------+------------+------+------+------+------+------+
 * | GCHeader | MiscHeader | JavaHeader | len  | elt0 | elt1 | ...  |eltN-1|
 * +----------+------------+------------+------+------+------+------+------+
 *                         ^ JHOFF             ^objref
 * </pre>
 *
 * Assumptions:
 * <ul>
 * <li> Each portion of the header (JavaHeader, GCHeader, MiscHeader)
 *      is some multiple of 4 bytes (possibly 0).  This simplifies access, since we
 *      can access each portion independently without having to worry about word tearing.
 * <li> The JavaHeader exports k (>=0) unused contiguous bits that can be used
 *      by the GCHeader and MiscHeader.  The GCHeader gets first dibs on these bits.
 *      The GCHeader should use buts 0..i, MiscHeader should use bits i..k.
 * <li> JHOFF is a constant for a given configuration.
 * </ul>
 *
 * This model allows efficient array access: the array pointer can be
 * used directly in the base+offset subscript calculation, with no
 * additional constant required.<p>
 *
 * This model allows free null pointer checking for most reads: a
 * small offset from that reference will wrap around to either very
 * high or very low unmapped memory in the case of a null pointer. As
 * long as these segments of memory are not mapped to the current
 * process, loads/stores through such a pointer will cause a trap that
 * we can catch with a unix signal handler.<p>
 *
 * Note that on AIX we are forced to perform explicit null checks on
 * scalar field accesses as we are unable to protect low memory.
 *
 * Note the key invariant that all elements of the header are
 * available at the same offset from an objref for both arrays and
 * scalar objects.
 *
 * Note that this model allows for arbitrary growth of the GC header
 * to the left of the object.  A possible TODO item is to modify the
 * necessary interfaces within this class and JavaHeader to allow
 * moveObject, bytesUsed, bytesRequiredWhenCopied, etc. to tell this
 * class how many GC header bytes have been allocated. As these calls
 * would be constant within the constant of the call the optimising
 * compiler should be able to allow this at minimal cost.
 *
 * Another possible TODO item is to include support for linear
 * scanning, where it is possible to move from one object to the next
 * under contiguous allocation. At the moment this is in conflict with
 * object alignment code for objects with long/double fields. We could
 * possibly include the code anyway but require that the alignment
 * code is switched off, or that all objects are aligned.  Linear
 * scanning is used in several GC algorithms including card-marking
 * and compaction.
 *
 * @see VM_JavaHeader
 * @see VM_MiscHeader
 * @see MM_Interface
 */
@Uninterruptible
public class VM_ObjectModel implements VM_JavaHeaderConstants, VM_SizeConstants {

    /** Should we gather stats on hash code state transitions for address-based hashing? */
    public static final boolean HASH_STATS = false;

    /** count number of Object.hashCode() operations */
    public static int hashRequests = 0;

    /** count transitions from UNHASHED to HASHED */
    public static int hashTransition1 = 0;

    /** count transitions from HASHED to HASHED_AND_MOVED */
    public static int hashTransition2 = 0;

    /** Whether to pack bytes and shorts into 32bit fields*/
    private static final boolean PACKED = true;

    /** Layout widget */
    private static final VM_FieldLayout layout;

    static {
        if (PACKED) {
            layout = new VM_FieldLayoutPacked(true, false);
        } else {
            layout = new VM_FieldLayoutUnpacked(true, false);
        }
    }

    /**
   * Layout the instance fields declared in this class.
   * @param klass the class to layout
   */
    @Interruptible
    public static void layoutInstanceFields(VM_Class klass) {
        layout.layoutInstanceFields(klass);
    }

    /**
   * Given a reference, return an address which is guaranteed to be inside
   * the memory region allocated to the object.
   */
    public static Address getPointerInMemoryRegion(ObjectReference ref) {
        return VM_JavaHeader.getPointerInMemoryRegion(ref);
    }

    /**
   * Return the offset of the array length field from an object reference
   * (in bytes)
   */
    public static Offset getArrayLengthOffset() {
        return ARRAY_LENGTH_OFFSET;
    }

    /**
   * Get the TIB for an object.
   */
    public static VM_TIB getTIB(ObjectReference ptr) {
        return getTIB(ptr.toObject());
    }

    /**
   * Get the TIB for an object.
   */
    public static VM_TIB getTIB(Object o) {
        return VM_JavaHeader.getTIB(o);
    }

    /**
   * Set the TIB for an object.
   */
    public static void setTIB(ObjectReference ptr, VM_TIB tib) {
        setTIB(ptr.toObject(), tib);
    }

    /**
   * Set the TIB for an object.
   */
    public static void setTIB(Object ref, VM_TIB tib) {
        VM_JavaHeader.setTIB(ref, tib);
    }

    /**
   * Set the TIB for an object.
   */
    @Interruptible
    public static void setTIB(BootImageInterface bootImage, Address refAddress, Address tibAddr, VM_Type type) {
        VM_JavaHeader.setTIB(bootImage, refAddress, tibAddr, type);
    }

    /**
   * Get the pointer just past an object
   */
    public static Address getObjectEndAddress(Object obj) {
        VM_TIB tib = getTIB(obj);
        VM_Type type = tib.getType();
        if (type.isClassType()) {
            return getObjectEndAddress(obj, type.asClass());
        } else {
            int numElements = VM_Magic.getArrayLength(obj);
            return getObjectEndAddress(obj, type.asArray(), numElements);
        }
    }

    /**
   * Get the pointer just past an object
   */
    public static Address getObjectEndAddress(Object object, VM_Class type) {
        return VM_JavaHeader.getObjectEndAddress(object, type);
    }

    /**
   * Get the pointer just past an object
   */
    public static Address getObjectEndAddress(Object object, VM_Array type, int elements) {
        return VM_JavaHeader.getObjectEndAddress(object, type, elements);
    }

    /**
   * Get an object reference from the address the lowest word of the object was allocated.
   */
    public static ObjectReference getObjectFromStartAddress(Address start) {
        return VM_JavaHeader.getObjectFromStartAddress(start);
    }

    /**
   * Get an object reference from the address the lowest word of the object was allocated.
   */
    public static ObjectReference getScalarFromStartAddress(Address start) {
        return VM_JavaHeader.getScalarFromStartAddress(start);
    }

    /**
   * Get an object reference from the address the lowest word of the object was allocated.
   */
    public static ObjectReference getArrayFromStartAddress(Address start) {
        return VM_JavaHeader.getArrayFromStartAddress(start);
    }

    /**
   * Get the next object in the heap under contiguous allocation.
   */
    public static ObjectReference getNextObject(ObjectReference obj) {
        VM_TIB tib = getTIB(obj);
        VM_Type type = tib.getType();
        if (type.isClassType()) {
            return getNextObject(obj, type.asClass());
        } else {
            int numElements = VM_Magic.getArrayLength(obj);
            return getNextObject(obj, type.asArray(), numElements);
        }
    }

    /**
   * Get the next object after this scalar under contiguous allocation.
   */
    public static ObjectReference getNextObject(ObjectReference obj, VM_Class type) {
        return VM_JavaHeader.getNextObject(obj, type);
    }

    /**
   * Get the next object after this array under contiguous allocation.
   */
    public static ObjectReference getNextObject(ObjectReference obj, VM_Array type, int numElements) {
        return VM_JavaHeader.getNextObject(obj, type, numElements);
    }

    /**
   * how many bytes are used by the object?
   */
    public static Object getReferenceWhenCopiedTo(Object obj, Address to) {
        VM_TIB tib = getTIB(obj);
        VM_Type type = tib.getType();
        if (type.isClassType()) {
            return getReferenceWhenCopiedTo(obj, to, type.asClass());
        } else {
            return getReferenceWhenCopiedTo(obj, to, type.asArray());
        }
    }

    /**
   * how many bytes are used by the object?
   */
    public static int bytesUsed(Object obj) {
        VM_TIB tib = getTIB(obj);
        VM_Type type = tib.getType();
        if (type.isClassType()) {
            return bytesUsed(obj, type.asClass());
        } else {
            int numElements = VM_Magic.getArrayLength(obj);
            return bytesUsed(obj, type.asArray(), numElements);
        }
    }

    /**
   * how many bytes are used by the scalar?
   */
    public static int bytesUsed(Object obj, VM_Class type) {
        return VM_JavaHeader.bytesUsed(obj, type);
    }

    /**
   * how many bytes are used by the array?
   */
    public static int bytesUsed(Object obj, VM_Array type, int numElements) {
        return VM_JavaHeader.bytesUsed(obj, type, numElements);
    }

    /**
   * how many bytes are required when the object is copied by GC?
   */
    public static int bytesRequiredWhenCopied(Object obj) {
        VM_TIB tib = getTIB(obj);
        VM_Type type = tib.getType();
        if (type.isClassType()) {
            return bytesRequiredWhenCopied(obj, type.asClass());
        } else {
            int numElements = VM_Magic.getArrayLength(obj);
            return bytesRequiredWhenCopied(obj, type.asArray(), numElements);
        }
    }

    /**
   * how many bytes are needed when the scalar object is copied by GC?
   */
    public static int bytesRequiredWhenCopied(Object fromObj, VM_Class type) {
        return VM_JavaHeader.bytesRequiredWhenCopied(fromObj, type);
    }

    /**
   * how many bytes are needed when the array object is copied by GC?
   */
    public static int bytesRequiredWhenCopied(Object fromObj, VM_Array type, int numElements) {
        return VM_JavaHeader.bytesRequiredWhenCopied(fromObj, type, numElements);
    }

    /**
   * Map from the object ref to the lowest address of the storage
   * associated with the object
   */
    @Inline
    public static Address objectStartRef(ObjectReference obj) {
        return VM_JavaHeader.objectStartRef(obj);
    }

    /**
   * Get the reference of an object after copying to a specified region.
   */
    public static Object getReferenceWhenCopiedTo(Object obj, Address region, VM_Class type) {
        return VM_JavaHeader.getReferenceWhenCopiedTo(obj, region, type);
    }

    /**
   * Get the reference of an object after copying to a specified region.
   */
    public static Object getReferenceWhenCopiedTo(Object obj, Address region, VM_Array type) {
        return VM_JavaHeader.getReferenceWhenCopiedTo(obj, region, type);
    }

    /**
   * Copy a scalar object to the given raw storage address
   */
    public static Object moveObject(Object fromObj, Object toObj, int numBytes, boolean noGCHeader, VM_Class type) {
        return VM_JavaHeader.moveObject(fromObj, toObj, numBytes, noGCHeader, type);
    }

    /**
   * Copy an array object to the given raw storage address
   */
    public static Object moveObject(Object fromObj, Object toObj, int numBytes, boolean noGCHeader, VM_Array type) {
        return VM_JavaHeader.moveObject(fromObj, toObj, numBytes, noGCHeader, type);
    }

    /**
   * Copy a scalar object to the given raw storage address
   */
    public static Object moveObject(Address toAddress, Object fromObj, int numBytes, boolean noGCHeader, VM_Class type) {
        return VM_JavaHeader.moveObject(toAddress, fromObj, numBytes, noGCHeader, type);
    }

    /**
   * Copy an array object to the given raw storage address
   */
    public static Object moveObject(Address toAddress, Object fromObj, int numBytes, boolean noGCHeader, VM_Array type) {
        return VM_JavaHeader.moveObject(toAddress, fromObj, numBytes, noGCHeader, type);
    }

    /**
   * Get the type of an object.
   */
    public static VM_Type getObjectType(Object o) {
        return VM_Magic.getObjectType(o);
    }

    /**
   * Get the length of an array
   */
    public static int getArrayLength(Object o) {
        return VM_Magic.getIntAtOffset(o, getArrayLengthOffset());
    }

    /**
   * Set the length of an array
   */
    public static void setArrayLength(Object o, int len) {
        VM_Magic.setIntAtOffset(o, getArrayLengthOffset(), len);
    }

    /**
   * Get the hash code of an object.
   */
    public static int getObjectHashCode(Object o) {
        if (HASH_STATS) hashRequests++;
        return VM_JavaHeader.getObjectHashCode(o);
    }

    /**
   * Get the offset of the thin lock word in this object
   */
    public static Offset getThinLockOffset(Object o) {
        return VM_JavaHeader.getThinLockOffset(o);
    }

    /**
   * what is the default offset for a thin lock?
   */
    public static Offset defaultThinLockOffset() {
        return VM_JavaHeader.defaultThinLockOffset();
    }

    /**
   * Allocate a thin lock word for instances of the type
   * (if they already have one, then has no effect).
   */
    public static void allocateThinLock(VM_Type t) {
        VM_JavaHeader.allocateThinLock(t);
    }

    /**
   * Generic lock
   */
    @Entrypoint
    public static void genericLock(Object o) {
        VM_JavaHeader.genericLock(o);
    }

    /**
   * Generic unlock
   */
    @Entrypoint
    public static void genericUnlock(Object o) {
        VM_JavaHeader.genericUnlock(o);
    }

    /**
   * @param obj an object
   * @param thread a thread
   * @return <code>true</code> if the lock on obj is currently owned
   *         by thread <code>false</code> if it is not.
   */
    public static boolean holdsLock(Object obj, VM_Thread thread) {
        return VM_JavaHeader.holdsLock(obj, thread);
    }

    /**
   * Obtains the heavy-weight lock, if there is one, associated with the
   * indicated object.  Returns <code>null</code>, if there is no
   * heavy-weight lock associated with the object.
   *
   * @param o the object from which a lock is desired
   * @param create if true, create heavy lock if none found
   * @return the heavy-weight lock on the object (if any)
   */
    public static VM_Lock getHeavyLock(Object o, boolean create) {
        return VM_JavaHeader.getHeavyLock(o, create);
    }

    /**
   * Non-atomic read of word containing available bits
   */
    public static Word readAvailableBitsWord(Object o) {
        return VM_JavaHeader.readAvailableBitsWord(o);
    }

    /**
   * Non-atomic read of byte containing available bits
   */
    public static byte readAvailableByte(Object o) {
        return VM_JavaHeader.readAvailableByte(o);
    }

    /**
   * Non-atomic write of word containing available bits
   */
    public static void writeAvailableBitsWord(Object o, Word val) {
        VM_JavaHeader.writeAvailableBitsWord(o, val);
    }

    /**
   * Non-atomic write of byte containing available bits
   */
    public static void writeAvailableByte(Object o, byte val) {
        VM_JavaHeader.writeAvailableByte(o, val);
    }

    /**
   * Return true if argument bit is 1, false if it is 0
   */
    public static boolean testAvailableBit(Object o, int idx) {
        return VM_JavaHeader.testAvailableBit(o, idx);
    }

    /**
   * Set argument bit to 1 if flag is true, 0 if flag is false
   */
    public static void setAvailableBit(Object o, int idx, boolean flag) {
        VM_JavaHeader.setAvailableBit(o, idx, flag);
    }

    /**
   * Freeze the other bits in the byte containing the available bits
   * so that it is safe to update them using setAvailableBits.
   */
    public static void initializeAvailableByte(Object o) {
        VM_JavaHeader.initializeAvailableByte(o);
    }

    /**
   * A prepare on the word containing the available bits
   */
    public static Word prepareAvailableBits(Object o) {
        return VM_JavaHeader.prepareAvailableBits(o);
    }

    /**
   * An attempt on the word containing the available bits
   */
    public static boolean attemptAvailableBits(Object o, Word oldVal, Word newVal) {
        return VM_JavaHeader.attemptAvailableBits(o, oldVal, newVal);
    }

    /**
   * Given the smallest base address in a region, return the smallest
   * object reference that could refer to an object in the region.
   */
    public static Address minimumObjectRef(Address regionBaseAddr) {
        return VM_JavaHeader.minimumObjectRef(regionBaseAddr);
    }

    /**
   * Given the largest base address in a region, return the largest
   * object reference that could refer to an object in the region.
   */
    public static Address maximumObjectRef(Address regionHighAddr) {
        return VM_JavaHeader.maximumObjectRef(regionHighAddr);
    }

    /**
   * Compute the header size of an instance of the given type.
   */
    @Inline
    public static int computeHeaderSize(VM_Type type) {
        if (type.isArrayType()) {
            return computeArrayHeaderSize(type.asArray());
        } else {
            return computeScalarHeaderSize(type.asClass());
        }
    }

    /**
   * Compute the header size of an object
   */
    @Interruptible
    public static int computeHeaderSize(Object ref) {
        return computeHeaderSize(getObjectType(ref));
    }

    /**
   * Compute the header size of an instance of the given type.
   */
    @Inline
    public static int computeScalarHeaderSize(VM_Class type) {
        return VM_JavaHeader.computeScalarHeaderSize(type);
    }

    /**
   * Compute the header size of an instance of the given type.
   */
    public static int computeArrayHeaderSize(VM_Array type) {
        return VM_JavaHeader.computeArrayHeaderSize(type);
    }

    /**
   * Given a TIB, compute the header size of an instance of the TIB's class
   */
    public static int computeHeaderSize(Object[] tib) {
        return computeHeaderSize(VM_Magic.objectAsType(tib[0]));
    }

    /**
   * For a reference to an object, what is the offset in bytes to the
   * last word of the header from an out-to-in perspective for the object?
   */
    public static int getHeaderEndOffset() {
        return VM_JavaHeader.getHeaderEndOffset();
    }

    /**
   * For a reference to an object, what is the offset in bytes to the bottom
   * word of the object?
   */
    public static int objectStartOffset(VM_Class t) {
        return VM_JavaHeader.objectStartOffset(t);
    }

    /**
   * Return the desired aligment of the alignment point in the object returned
   * by getScalarOffsetForAlignment.
   * @param t VM_Class instance being created
   */
    public static int getAlignment(VM_Class t) {
        return VM_JavaHeader.getAlignment(t);
    }

    /**
   * Return the desired aligment of the alignment point returned by
   * getOffsetForAlignment in instances of the argument VM_Class.
   * @param t VM_Class instance being copied
   * @param obj the object being copied
   */
    public static int getAlignment(VM_Class t, Object obj) {
        return VM_JavaHeader.getAlignment(t, obj);
    }

    /**
   * Return the desired aligment of the alignment point returned by
   * getOffsetForAlignment in instances of the argument VM_Array.
   * @param t VM_Array instance being created
   */
    public static int getAlignment(VM_Array t) {
        return VM_JavaHeader.getAlignment(t);
    }

    /**
   * Return the desired aligment of the alignment point returned by
   * getOffsetForAlignment in instances of the argument VM_Array.
   * @param t VM_Array instance being copied
   * @param obj the object being copied
   */
    public static int getAlignment(VM_Array t, Object obj) {
        return VM_JavaHeader.getAlignment(t, obj);
    }

    /**
   * Return the offset relative to physical beginning of object
   * that must be aligned.
   * @param t VM_Class instance being created
   */
    public static int getOffsetForAlignment(VM_Class t) {
        return VM_JavaHeader.getOffsetForAlignment(t);
    }

    /**
   * Return the offset relative to physical beginning of object
   * that must be aligned.
   * @param t VM_Class instance being copied
   * @param obj the object being copied
   */
    public static int getOffsetForAlignment(VM_Class t, ObjectReference obj) {
        return VM_JavaHeader.getOffsetForAlignment(t, obj);
    }

    /**
   * Return the offset relative to physical beginning of object that must
   * be aligned.
   * @param t VM_Array instance being created
   */
    public static int getOffsetForAlignment(VM_Array t) {
        return VM_JavaHeader.getOffsetForAlignment(t);
    }

    /**
   * Return the offset relative to physical beginning of object that must
   * be aligned.
   * @param t VM_Array instance being copied
   * @param obj the object being copied
   */
    public static int getOffsetForAlignment(VM_Array t, ObjectReference obj) {
        return VM_JavaHeader.getOffsetForAlignment(t, obj);
    }

    /**
   * Initialize raw storage with low memory word ptr of size bytes
   * to be an uninitialized instance of the (scalar) type specified by tib.
   *
   * @param ptr address of raw storage
   * @param tib the type information block
   * @param size number of bytes of raw storage allocated.
   */
    @Inline
    public static Object initializeScalar(Address ptr, VM_TIB tib, int size) {
        Object ref = VM_JavaHeader.initializeScalarHeader(ptr, tib, size);
        VM_MiscHeader.initializeHeader(ref, tib, size, true);
        setTIB(ref, tib);
        return ref;
    }

    /**
   * Allocate and initialize space in the bootimage (at bootimage writing time)
   * to be an uninitialized instance of the (scalar) type specified by klass.
   * NOTE: TIB is set by BootImageWriter2
   *
   * @param bootImage the bootimage to put the object in
   * @param klass the VM_Class object of the instance to create.
   * @return the offset of object in bootimage (in bytes)
   */
    @Interruptible
    public static Address allocateScalar(BootImageInterface bootImage, VM_Class klass) {
        VM_TIB tib = klass.getTypeInformationBlock();
        int size = klass.getInstanceSize();
        int align = getAlignment(klass);
        int offset = getOffsetForAlignment(klass);
        Address ptr = bootImage.allocateDataStorage(size, align, offset);
        Address ref = VM_JavaHeader.initializeScalarHeader(bootImage, ptr, tib, size);
        MM_Interface.initializeHeader(bootImage, ref, tib, size, true);
        VM_MiscHeader.initializeHeader(bootImage, ref, tib, size, true);
        return ref;
    }

    /**
   * Fill an alignment gap with the alignment value
   */
    @Interruptible
    public static void fillAlignmentGap(BootImageInterface bootImage, Address address, Extent size) {
        while (size.GT(Extent.zero())) {
            bootImage.setFullWord(address, VM_JavaHeader.ALIGNMENT_VALUE);
            address = address.plus(BYTES_IN_INT);
            size = size.minus(BYTES_IN_INT);
        }
    }

    /**
   * Initialize raw storage with low memory word ptr of size bytes
   * to be an uninitialized instance of the array type specific by tib
   * with numElems elements.
   *
   * @param ptr address of raw storage
   * @param tib the type information block
   * @param numElems number of elements in the array
   * @param size number of bytes of raw storage allocated.
   */
    @Inline
    public static Object initializeArray(Address ptr, VM_TIB tib, int numElems, int size) {
        Object ref = VM_JavaHeader.initializeArrayHeader(ptr, tib, size);
        VM_MiscHeader.initializeHeader(ref, tib, size, false);
        setTIB(ref, tib);
        setArrayLength(ref, numElems);
        return ref;
    }

    /**
   * Allocate and initialize space in the bootimage (at bootimage writing time)
   * to be an uninitialized instance of the (array) type specified by array.
   * NOTE: TIB is set by BootimageWriter2
   *
   * @param bootImage the bootimage to put the object in
   * @param array VM_Array object of array being allocated.
   * @param numElements number of elements
   * @return Address of object in bootimage (in bytes)
   */
    @Interruptible
    public static Address allocateArray(BootImageInterface bootImage, VM_Array array, int numElements) {
        VM_TIB tib = array.getTypeInformationBlock();
        int size = array.getInstanceSize(numElements);
        int align = getAlignment(array);
        int offset = getOffsetForAlignment(array);
        Address ptr = bootImage.allocateDataStorage(size, align, offset);
        Address ref = VM_JavaHeader.initializeArrayHeader(bootImage, ptr, tib, size);
        bootImage.setFullWord(ref.plus(getArrayLengthOffset()), numElements);
        MM_Interface.initializeHeader(bootImage, ref, tib, size, false);
        VM_MiscHeader.initializeHeader(bootImage, ref, tib, size, false);
        return ref;
    }

    /**
   * Allocate and initialize space in the bootimage (at bootimage writing time)
   * to be an uninitialized instance of the (array) type specified by array.
   * NOTE: TIB is set by BootimageWriter2
   *
   * @param bootImage the bootimage to put the object in
   * @param array VM_Array object of array being allocated.
   * @param numElements number of elements
   * @return Address of object in bootimage
   */
    @Interruptible
    public static Address allocateCode(BootImageInterface bootImage, VM_Array array, int numElements) {
        VM_TIB tib = array.getTypeInformationBlock();
        int size = array.getInstanceSize(numElements);
        int align = getAlignment(array);
        int offset = getOffsetForAlignment(array);
        Address ptr = bootImage.allocateCodeStorage(size, align, offset);
        Address ref = VM_JavaHeader.initializeArrayHeader(bootImage, ptr, tib, size);
        bootImage.setFullWord(ref.plus(getArrayLengthOffset()), numElements);
        MM_Interface.initializeHeader(bootImage, ref, tib, size, false);
        VM_MiscHeader.initializeHeader(bootImage, ref, tib, size, false);
        return ref;
    }

    /**
   * For low level debugging of GC subsystem.
   * Dump the header word(s) of the given object reference.
   * @param ptr the object reference whose header should be dumped
   */
    public static void dumpHeader(ObjectReference ptr) {
        dumpHeader(ptr.toObject());
    }

    /**
   * For low level debugging of GC subsystem.
   * Dump the header word(s) of the given object reference.
   * @param ref the object reference whose header should be dumped
   */
    public static void dumpHeader(Object ref) {
        VM.sysWrite(" TIB=");
        VM.sysWrite(VM_Magic.objectAsAddress(getTIB(ref)));
        VM_JavaHeader.dumpHeader(ref);
        VM_MiscHeader.dumpHeader(ref);
    }

    /**
   * For debugging.
   */
    public static void describeObject(ObjectReference addr) {
        Object obj = addr.toObject();
        VM_Type type = VM_Magic.getObjectType(obj);
        VM.sysWrite(type.getDescriptor());
    }

    /**
   * The following method will emit code that moves a reference to an
   * object's TIB into a destination register.
   *
   * @param asm the assembler object to emit code with
   * @param dest the number of the destination register
   * @param object the number of the register holding the object reference
   */
    @Interruptible
    public static void baselineEmitLoadTIB(VM_Assembler asm, int dest, int object) {
        VM_JavaHeader.baselineEmitLoadTIB(asm, dest, object);
    }
}

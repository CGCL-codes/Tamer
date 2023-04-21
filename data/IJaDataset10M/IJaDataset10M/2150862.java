package potato.objects;

import java.math.BigInteger;
import potato.*;
import potato.image.SqueakObjectHeader;
import potato.image.SqueakImage;
import java.util.*;

/**
 * @author Daniel Ingalls
 * 
 * @author Frank Feinbube
 * @author Robert Wierschke
 *
 * Squeak objects are modelled by Java objects with separate binary and pointer data.
 * Later this could be optimized for objects that have only one or the other, but for
 * now it is simple, and handles the inhomogeneous case of CompiledMethods nicely.
 *
 * Weak fields are not currently supported.  The plan for doing this would be
 * to make those objects a subclass, and put pointers in a WeakField.  We would 
 * need to replace all patterns of obj.pointers with an access function.
 * Then we would associate a finalization routine with those pointers.
 */
public class SqueakObject {

    public short hash;

    public short format;

    public int size = 0;

    public int baseAddr = 0;

    public Object sqClass;

    public Object bits;

    private boolean fixByteOrder;

    private SqueakObjectHeader header;

    public SqueakObject(SqueakObjectHeader header, int objectOffset, int[] content, boolean fixByteOrder) {
        this.format = header.format;
        this.hash = header.hash;
        this.baseAddr = objectOffset + header.getHeaderSize() - 4;
        this.size = header.getHeaderSize() + header.objectSizeInWords * 4;
        this.sqClass = header.classIndex;
        this.bits = content;
        this.header = header;
        this.fixByteOrder = fixByteOrder;
    }

    public Object[] pointers;

    SqueakObject(SqueakImage img) {
        hash = img.registerObject(this);
    }

    public SqueakObject(SqueakImage img, SqueakObject cls, int indexableSize, SqueakObject filler) {
        this(img);
        sqClass = cls;
        int instSpec = SmallInteger.intFromSmall(cls.fetchInteger(Constants.Class_format));
        int instSize = ((instSpec >> 1) & 0x3F) + ((instSpec >> 10) & 0xC0) - 1;
        format = ((short) ((instSpec >> 7) & 0xF));
        if (format < 8) {
            if (format != 6) {
                pointers = new Object[instSize + indexableSize];
                Arrays.fill(pointers, filler);
            } else if (indexableSize >= 0) {
                bits = new int[indexableSize];
            }
        } else {
            bits = new byte[indexableSize];
        }
    }

    public SqueakObject getSqClass() {
        return (SqueakObject) sqClass;
    }

    public Object fetchPointer(int zeroBasedIndex) {
        return pointers[zeroBasedIndex];
    }

    public SqueakObject fetchPointerNI(int zeroBasedIndex) {
        return (SqueakObject) pointers[zeroBasedIndex];
    }

    public Integer fetchInteger(int zeroBasedIndex) {
        return (Integer) pointers[zeroBasedIndex];
    }

    public void setPointer(int zeroBasedIndex, Object aPointer) {
        pointers[zeroBasedIndex] = aPointer;
    }

    public int pointersSize() {
        return pointers == null ? 0 : pointers.length;
    }

    public int bitsSize() {
        if (bits == null) {
            return 0;
        }
        if (bits instanceof byte[]) {
            return ((byte[]) bits).length;
        }
        if (bits instanceof Double) {
            return 2;
        }
        return ((int[]) bits).length;
    }

    public int instSize() {
        if (format > 4 || format == 2) {
            return 0;
        }
        if (format < 2) {
            return pointers.length;
        }
        return ((SqueakObject) sqClass).classInstSize();
    }

    public int classInstSize() {
        int instSpec = SmallInteger.intFromSmall(this.fetchInteger(Constants.Class_format));
        return ((instSpec >> 1) & 0x3F) + ((instSpec >> 10) & 0xC0) - 1;
    }

    public SqueakObject classGetName() {
        return this.fetchPointerNI(Constants.Class_name);
    }

    public SqueakObject cloneIn(SqueakImage img) {
        SqueakObject clone = new SqueakObject(img);
        clone.copyStateFrom(this);
        return clone;
    }

    private void copyStateFrom(SqueakObject other) {
        sqClass = other.sqClass;
        format = other.format;
        pointers = (Object[]) other.pointers.clone();
        Object otherBits = other.bits;
        if (otherBits == null) {
            return;
        }
        if (otherBits instanceof byte[]) {
            bits = ((byte[]) other.bits).clone();
        } else if (otherBits instanceof int[]) {
            bits = ((int[]) other.bits).clone();
        }
    }

    public double getFloatBits() {
        return ((Double) bits).doubleValue();
    }

    public void setFloatBits(double value) {
        bits = new Double(value);
    }

    public int methodHeader() {
        return ((Integer) fetchPointer(0)).intValue();
    }

    public int methodNumLits() {
        return (methodHeader() >> 9) & 0xFF;
    }

    public int methodNumArgs() {
        return (methodHeader() >> 24) & 0xF;
    }

    public int methodPrimitiveIndex() {
        int primBits = (methodHeader()) & 0x300001FF;
        if (primBits > 0x1FF) {
            return (primBits & 0x1FF) + (primBits >> 19);
        } else {
            return primBits;
        }
    }

    public SqueakObject methodClassForSuper() {
        SqueakObject assn = fetchPointerNI(methodNumLits());
        return assn.fetchPointerNI(Constants.Assn_value);
    }

    public boolean methodNeedsLargeFrame() {
        return (methodHeader() & 0x20000) > 0;
    }

    public void methodAddPointers(Object[] headerAndLits) {
        pointers = headerAndLits;
    }

    public int methodTempCount() {
        return (methodHeader() >> 18) & 63;
    }

    public Object methodGetLiteral(int zeroBasedIndex) {
        return fetchPointer(1 + zeroBasedIndex);
    }

    public SqueakObject methodGetSelector(int zeroBasedIndex) {
        return fetchPointerNI(1 + zeroBasedIndex);
    }

    public void methodSetLiteral(int zeroBasedIndex, Object rawValue) {
        setPointer(1 + zeroBasedIndex, rawValue);
    }

    public void install(Map<Integer, SqueakObject> oopMap, Integer[] ccArray, SqueakObject floatClass) {
        int ccInt = ((Integer) sqClass).intValue();
        if ((ccInt > 0) && (ccInt < 32)) {
            sqClass = oopMap.get(ccArray[ccInt - 1]);
        } else {
            sqClass = oopMap.get(sqClass);
        }
        int nWords = ((int[]) bits).length;
        if (format < 5) {
            pointers = decodePointers(nWords, ((int[]) bits), oopMap);
            bits = null;
        } else {
            if (format >= 12) {
                int methodHeader = ((int[]) bits)[0];
                int numLits = ((methodHeader >> 10) & 255);
                pointers = decodePointers(numLits + 1, ((int[]) bits), oopMap);
                bits = decodeBytes(nWords - (numLits + 1), ((int[]) bits), numLits + 1, format & 3);
            } else if (format >= 8) {
                bits = decodeBytes(nWords, ((int[]) bits), 0, format & 3);
            } else if (sqClass == floatClass) {
                long longBits = (((long) ((int[]) bits)[0]) << 32) | (((long) ((int[]) bits)[0]) & 0xFFFFFFFF);
                bits = new Double(Double.longBitsToDouble(longBits));
            }
        }
    }

    private Object[] decodePointers(int nWords, int[] theBits, Map<Integer, SqueakObject> oopMap) {
        Object[] ptrs = new Object[nWords];
        for (int i = 0; i < nWords; i++) {
            int oldOop = theBits[i];
            if ((oldOop & 1) == 1) {
                ptrs[i] = SmallInteger.smallFromInt(oldOop >> 1);
            } else {
                ptrs[i] = oopMap.get(new Integer(oldOop));
            }
        }
        return ptrs;
    }

    private byte[] decodeBytes(int nWords, int[] theBits, int wordOffset, int fmtLoBits) {
        byte[] newBits = null;
        int nBytes = (nWords * 4) - (format & 3);
        newBits = new byte[nBytes];
        int wordIx = wordOffset;
        int fourBytes = 0;
        for (int i = 0; i < nBytes; i++) {
            if ((i & 3) == 0) {
                fourBytes = theBits[wordIx++];
                if (fixByteOrder) fourBytes = Integer.reverseBytes(fourBytes);
            }
            int pickByte = (fourBytes >> (8 * (3 - (i & 3)))) & 255;
            if (pickByte >= 128) {
                pickByte = pickByte - 256;
            }
            newBits[i] = (byte) pickByte;
        }
        return newBits;
    }

    public int oldOopAt(int zeroBasedOffset) {
        return ((int[]) bits)[zeroBasedOffset];
    }

    public boolean isWords() {
        return !isPointers() && !((SqueakObject) this.sqClass).isBytes();
    }

    public boolean isPointers() {
        return !isBits();
    }

    boolean isBits() {
        return format >= 6;
    }

    boolean isBytes() {
        return format >= 8;
    }

    public int byteSizeOf() {
        if (((SqueakObject) sqClass).isBytes()) {
            return slotSizeOf();
        } else {
            return slotSizeOf() * 4;
        }
    }

    public boolean isWordsOrBytes() {
        return isBytes() || isWords();
    }

    public int slotSizeOf() {
        int result = bitsSize();
        if (result == 0) {
            result = fetchPointerNI(0).bitsSize();
        }
        return result;
    }

    public int lengthOf() {
        return header.lengthOf();
    }

    public String asString() {
        if (bits != null && bits instanceof byte[]) {
            if (pointers != null) {
                return "a CompiledMethod";
            } else {
                return new String((byte[]) bits);
            }
        } else {
            SqueakObject itsClass = this.getSqClass();
            if (itsClass.pointersSize() >= 9) {
                return "a " + itsClass.classGetName().asString();
            } else {
                return "Class " + this.classGetName().asString();
            }
        }
    }

    @Override
    public String toString() {
        return this.asString();
    }

    /**
     * FIXME: what is the right way to achieve this?
     */
    public void setByte(int zeroBasedIndex, byte value) {
        byte[] bytes = (byte[]) bits;
        bytes[zeroBasedIndex] = value;
    }

    /**
     * FIXME: what is the right way to achieve this?
     */
    public byte getByte(int zeroBasedIndex) {
        byte[] bytes = (byte[]) bits;
        return bytes[zeroBasedIndex];
    }

    /**
     * Check whether this object, which is supposed to be a Large****Integer,
     * already has a BigInteger representation. No security checks are performed.
     * This method is supposed to be called in correct contexts.
     * 
     * @return true if this object already holds a BigInteger.
     */
    public boolean hasLarge() {
        return pointers != null && pointers[0] instanceof BigInteger;
    }

    /**
     * This method returns this object's BigInteger representation. No security
     * checks are performed to ensure this is a Large****Integer. This method
     * is simply expected to be called in appropriate situations.
     * 
     * @return a BigInteger representing this object.
     */
    public BigInteger large() {
        return (BigInteger) pointers[0];
    }

    /**
     * Assign the passed BigInteger to this object's pointers array. No security
     * checks are done in this case: this method is simply not supposed to be
     * invoked in illegal situations. That is, it might well overwrite the
     * pointers array with something unexpected. (More detail: the above method
     * hasLarge() is supposed to be called before this one.)
     */
    public void assignLarge(BigInteger big) {
        pointers = new Object[] { big };
    }

    /**
     * Invalidate the large representation of this object by setting it to null.
     * No security checks are performed to ensure that this object represents a
     * Large****tiveInteger. This method is expected to be called in appropriate
     * situations.
     */
    public void invalidateLarge() {
        pointers[0] = null;
    }
}

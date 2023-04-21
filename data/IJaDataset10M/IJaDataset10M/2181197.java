package fr.x9c.cadmium.kernel;

import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import fr.x9c.cadmium.util.Misc;

/**
 * This class represents all possible values of a block. <br/>
 * It is thus a kind of <i>sum type</i> as a value can be:
 * <ul>
 *   <li>a string;</li>
 *   <li>a double;</li>
 *   <li>a double array;</li>
 *   <li>a closure;</li>
 *   <li>an infix block;</li>
 *   <li>a weak block;</li>
 *   <li>an atom;</li>
 *   <li>a <i>plain</i> block containing some values;</li>
 *   <li>a custom block.</li>
 * </ul> <br/>
 * A block is always defined by a header holding a tag (stored as the 8 lowest
 * bits of the header) and its size (also noted "wosize") of the block (stored
 * as the 22 highest bits of the header).In Cadmium, the color of the block is
 * not kept, as garbage collection is done by the JVM and not by the Cadmium
 * runtime itself. <br/>
 *
 * @see fr.x9c.cadmium.kernel.Value
 *
 * @author <a href="mailto:cadmium@x9c.fr">Xavier Clerc</a>
 * @version 1.0
 * @since 1.0
 */
public final class Block {

    /** Tag for "cons" ("::") operator. */
    public static final int TAG_CONS = 0;

    /** Maximum size of allocated elements. */
    public static final int MAX_WOSIZE = (1 << 22) - 1;

    /** Number of possible tags. */
    public static final int NUM_TAGS = 1 << 8;

    /** Below this tag value, value are not scanned. */
    public static final int NO_SCAN_TAG = 251;

    /** Tag for 'forward' value. */
    public static final int FORWARD_TAG = 250;

    /** Tag for 'infix' value. */
    public static final int INFIX_TAG = 249;

    /** Tag for 'object' value. */
    public static final int OBJECT_TAG = 248;

    /** Tag for 'closure' value. */
    public static final int CLOSURE_TAG = 247;

    /** Tag for 'lazy' value. */
    public static final int LAZY_TAG = 246;

    /** Tag for 'abstract' value. */
    public static final int ABSTRACT_TAG = 251;

    /** Tag for 'string' value. */
    public static final int STRING_TAG = 252;

    /** Tag for 'double' value. */
    public static final int DOUBLE_TAG = 253;

    /** Tag for 'double array' value. */
    public static final int DOUBLE_ARRAY_TAG = 254;

    /** Tag for 'custom' value. */
    public static final int CUSTOM_TAG = 255;

    /** Size of double values in bytes (size of double / size of value). */
    public static final int DOUBLE_SIZE = 2;

    /** Next magic number. */
    private static final AtomicLong nextMagicNumber = new AtomicLong();

    /** Magic number, used for comparison. */
    private long magicNumber;

    /**
     * Block header. <br/>
     * The color is not encoded as garbage collection is done by the
     * underlying JVM.
     */
    private int header;

    /**
     * Reference to parent block
     * (if block is part of a infix/"recursive" closure).
     */
    private Block parent;

    /** Reference to 'custom' value - set when custom or abstract. */
    private Object obj;

    /** 'double' value. */
    private double dbl;

    /** 'double array' value. */
    private double[] dblArray;

    /**
     * Used for optimized 'int32'/'nativeint' custom
     * (avoid the use of an {@link java.lang.Integer} instance stored in
     * {@link #obj}).
     */
    private int int32;

    /**
     * Used for optimized 'int64' custom.
     * (avoid the use of an {@link java.lang.Long} instance stored in
     * {@link #obj}).
     */
    private long int64;

    /** Used for strings. */
    private byte[] bytes;

    /** Reference to custom operations - set when custom. */
    private Custom.Operations custom;

    /** References to 'subvalues' in closure, block, <i>etc</i>. */
    private Value[] values;

    /** References to weak 'subvalues' in weak block, <i>etc</i>. */
    private WeakReference[] weakValues;

    /** List of finalizers for value, <tt>null</tt> if none. */
    private List<Value> finalizers;

    /**
     * Set only when finalizers are registered
     * (runner is then used for callback).
     */
    private CodeRunner codeRunner;

    /**
     * Constructs a block.
     */
    private Block() {
        this.magicNumber = Block.nextMagicNumber.getAndIncrement();
    }

    /**
     * Creates a string block - all bytes are set to zero.
     * @param len number of character in the string block
     *            - should be >= 0
     * @return string block
     */
    public static Block createString(final int len) {
        assert len >= 0 : "len should be >= 0";
        final Block res = new Block();
        final int wosize = (len + 4) / 4;
        res.header = makeHeader(wosize, Block.STRING_TAG);
        res.bytes = new byte[len];
        return res;
    }

    /**
     * Creates a string block.
     * @param len number of character in the string block
     *            - should be >= 0
     * @param s initial value of string - should not be <tt>null</tt> <br/>
     *          if <tt>s</tt> has more than <tt>len</tt> characters, additional
     *          characters are ignored <br/>
     *          if <tt>s</tt> has less than <tt>len</tt> characters, additional
     *          bytes are set to zero
     * @return string block
     */
    public static Block createString(final int len, final String s) {
        assert len >= 0 : "len should be >= 0";
        assert s != null : "null s";
        final Block res = createString(len);
        final int l = s.length();
        if (l <= len) {
            Misc.convertStringToBytes(s, res.bytes, 0);
        } else {
            Misc.convertStringToBytes(s.substring(0, len), res.bytes, 0);
        }
        return res;
    }

    /**
     * Creates a string block.
     * @param len number of character in the string block
     *            - should be >= 0
     * @param s initial value of string - should not be <tt>null</tt>
     *          if <tt>s</tt> has more than <tt>len</tt> elements, additional
     *          characters are ignored <br/>
     *          if <tt>s</tt> has less than <tt>len</tt> elements, additional
     *          bytes are set to zero
     * @return string block
     */
    public static Block createString(final int len, final byte[] s) {
        assert len >= 0 : "len should be >= 0";
        assert s != null : "null s";
        final Block res = createString(len);
        System.arraycopy(s, 0, res.bytes, 0, Math.min(s.length, len));
        return res;
    }

    /**
     * Creates a string block.
     * The block has length of the given array.
     * @param s initial value of string - should not be <tt>null</tt>
     * @return string block
     */
    public static Block createString(final byte[] s) {
        assert s != null : "null s";
        return createString(s.length, s);
    }

    /**
     * Creates a string block.
     * @param s initial value of string - should not be <tt>null</tt>
     * @return string block
     */
    public static Block createString(final String s) {
        assert s != null : "null s";
        return createString(s.length(), s);
    }

    /**
     * Creates a block containing the strings of a given array.
     * @param s array - should not be <tt>null</tt>
     * @return a block containing as many values as s contains strings,
     *         each element of the block being the corresponding element of s
     */
    public static Block createStringArray(final String[] s) {
        assert s != null : "null s";
        final int len = s.length;
        final Block res = new Block();
        res.header = makeHeader(len, 0);
        res.values = new Value[len];
        for (int i = 0; i < len; i++) {
            res.values[i] = Value.createFromBlock(Block.createString(s[i]));
        }
        return res;
    }

    /**
     * Creates a custom block.
     * @param sz size of custom - should be >= 0
     * @param ops custom operations associated with this block
     *             - should not be <tt>null</tt>
     * @return custom block
     */
    public static Block createCustom(final int sz, final Custom.Operations ops) {
        assert sz >= 0 : "sz should be >= 0";
        assert ops != null : "null ops";
        final Block res = new Block();
        res.header = makeHeader(1 + (sz + 4 - 1) / 4, Block.CUSTOM_TAG);
        res.custom = ops;
        return res;
    }

    /**
     * Creates a double array block. <br/>
     * All elements are set to zero.
     * @param len length of array - should be >= 0
     * @return double array block
     */
    public static Block createDoubleArray(final int len) {
        assert len >= 0 : "len should be >= 0";
        final Block res = new Block();
        res.header = makeHeader(len * Block.DOUBLE_SIZE, Block.DOUBLE_ARRAY_TAG);
        res.dblArray = new double[len];
        return res;
    }

    /**
     * Creates a double array block containing the passed elements. <br/>
     * @param values array values - should not be <tt>null</tt>
     * @return double array block
     */
    public static Block createDoubleArray(final double[] values) {
        assert values != null : "null values";
        final Block res = new Block();
        res.header = makeHeader(values.length * Block.DOUBLE_SIZE, Block.DOUBLE_ARRAY_TAG);
        res.dblArray = Arrays.copyOf(values, values.length);
        return res;
    }

    /**
     * Creates a double block.
     * @param d initial value of block
     * @return double block
     */
    public static Block createDouble(final double d) {
        final Block res = new Block();
        res.header = makeHeader(Block.DOUBLE_SIZE, Block.DOUBLE_TAG);
        res.dbl = d;
        return res;
    }

    /**
     * Creates a closure block. <br/>
     * All elements are set to zero (the long value).
     * @param sz number of values in the closure - should be >= 0
     * @return closure block
     */
    public static Block createClosure(final int sz) {
        assert sz >= 0 : "sz should be >= 0";
        final Block res = new Block();
        res.header = makeHeader(sz, Block.CLOSURE_TAG);
        res.values = new Value[sz];
        if (sz > 0) {
            res.values[0] = Value.createFromCodeOffset(0);
            for (int i = 1; i < sz; i++) {
                res.values[i] = Value.ZERO;
            }
        }
        return res;
    }

    /**
     * Creates an infix block. <br/>
     * @param t <i>tag</i> of the infix block
     * @return infix block
     */
    public static Block createInfix(final int t) {
        final Block res = new Block();
        res.header = makeHeader(t, Block.INFIX_TAG);
        res.values = new Value[1];
        res.values[0] = Value.createFromCodeOffset(0);
        return res;
    }

    /**
     * Creates a 'plain' block. <br/>
     * All elements are set to zero (the long value). <br/>
     * @param sz number of values in the closure - should be >= 0
     * @param tag block tag
     * @return block
     */
    public static Block createBlock(final int sz, final int tag) {
        assert sz >= 0 : "sz should be >= 0";
        final Block res = new Block();
        res.header = makeHeader(sz, tag);
        res.values = new Value[sz];
        for (int i = 0; i < sz; i++) {
            res.values[i] = Value.ZERO;
        }
        return res;
    }

    /**
     * Creates a 'plain' block of size 1.
     * @param tag block tag
     * @param v1 value of the element at index 0
     * @return block
     */
    public static Block createBlock(final int tag, final Value v1) {
        assert v1 != null : "null v1";
        final Block res = new Block();
        res.header = makeHeader(1, tag);
        res.values = new Value[1];
        res.values[0] = v1;
        return res;
    }

    /**
     * Creates a 'plain' block of size 2.
     * @param tag block tag
     * @param v1 value of the element at index 0
     * @param v2 value of the element at index 1
     * @return block
     */
    public static Block createBlock(final int tag, final Value v1, final Value v2) {
        assert v1 != null : "null v1";
        assert v2 != null : "null v2";
        final Block res = new Block();
        res.header = makeHeader(2, tag);
        res.values = new Value[2];
        res.values[0] = v1;
        res.values[1] = v2;
        return res;
    }

    /**
     * Creates a 'plain' block of size 3.
     * @param tag block tag
     * @param v1 value of the element at index 0
     * @param v2 value of the element at index 1
     * @param v3 value of the element at index 2
     * @return block
     */
    public static Block createBlock(final int tag, final Value v1, final Value v2, final Value v3) {
        assert v1 != null : "null v1";
        assert v2 != null : "null v2";
        assert v3 != null : "null v3";
        final Block res = new Block();
        res.header = makeHeader(3, tag);
        res.values = new Value[3];
        res.values[0] = v1;
        res.values[1] = v2;
        res.values[2] = v3;
        return res;
    }

    /**
     * Creates a 'plain' block of size 4.
     * @param tag block tag
     * @param v1 value of the element at index 0
     * @param v2 value of the element at index 1
     * @param v3 value of the element at index 2
     * @param v4 value of the element at index 3
     * @return block
     */
    public static Block createBlock(final int tag, final Value v1, final Value v2, final Value v3, final Value v4) {
        assert v1 != null : "null v1";
        assert v2 != null : "null v2";
        assert v3 != null : "null v3";
        assert v4 != null : "null v4";
        final Block res = new Block();
        res.header = makeHeader(4, tag);
        res.values = new Value[4];
        res.values[0] = v1;
        res.values[1] = v2;
        res.values[2] = v3;
        res.values[3] = v4;
        return res;
    }

    /**
     * Creates a block of 'weak' values. <br/>
     * All elements are set to <i>None</i>. <br/>
     * Block is tagged as 'abstract'. <br/>
     * Weak values can be garbage collected even if they are still reachable.
     * @param sz number of weak values
     * @return block
     */
    public static Block createWeakBlock(final int sz) {
        assert sz >= 0 : "sz should be >= 0";
        final Block res = new Block();
        res.header = makeHeader(sz, Block.ABSTRACT_TAG);
        res.weakValues = new WeakReference[sz];
        for (int i = 0; i < sz; i++) {
            res.weakValues[i] = new WeakReference<Value>(null);
        }
        return res;
    }

    /**
     * Creates an atom block.
     * @param atm 'index' or 'order' of atom
     * @return atom block
     */
    static Block createAtom(final int atm) {
        final Block res = new Block();
        res.header = makeHeader(0, atm);
        return res;
    }

    /**
     * Constructs a header from wosize and tag. <br/>
     * Color is not used as garbage collection is done by the underlying JVM.
     * @param wosize wosize
     * @param tag tag
     * @return header with passed wosize and tag
     */
    private static int makeHeader(final int wosize, final int tag) {
        assert (wosize >= 0) && (wosize <= Block.MAX_WOSIZE) : "invalid wosize";
        assert (tag & 0xFF) <= 0xFF : "invalid tag";
        return (wosize << 10) + tag;
    }

    /**
     * Extract tag from a block header.
     * @param hd header
     * @return tag of passed header
     */
    static int tagFromHeader(final int hd) {
        return hd & 0xFF;
    }

    /**
     * Extract wosize from a block header.
     * @param hd header
     * @return wosize of passed header
     */
    static int wosizeFromHeader(final int hd) {
        return hd >> 10;
    }

    /**
     * Returns the wosize of the block.
     * @return the wosize of the block
     */
    public int getWoSize() {
        return wosizeFromHeader(this.header);
    }

    /**
     * Returns the header of the block.
     * @return the header of the block
     */
    public int getHeader() {
        return this.header;
    }

    /**
     * Sets the header of the block.
     * @param hd new header
     */
    public void setHeader(final int hd) {
        if (getTag() == Block.INFIX_TAG) {
            final Block parent = this.parent;
            if (parent != null) {
                final Value[] blocks = parent.values;
                final int len = blocks.length;
                int i = 0;
                while ((i < len) && (!blocks[i].isBlock() || (this != blocks[i].asBlock()))) {
                    i++;
                }
                if (i - 1 >= 0) {
                    blocks[i - 1] = Value.createFromRawValue(hd);
                }
            }
        }
        this.header = hd;
    }

    /**
     * Returns the tag of the block.
     * @return the tag of the block
     */
    public int getTag() {
        return tagFromHeader(this.header);
    }

    /**
     * Changes the tag of the block. <br/>
     * Wosize is unchanged.
     * @param t new tag
     */
    public void setTag(final int t) {
        setHeader(makeHeader(getWoSize(), t));
    }

    /**
     * Returns the magic number of the block (used for comparison).
     * @return the magic number of the block
     */
    public long getMagicNumber() {
        return this.magicNumber;
    }

    /**
     * Returns a value from the block.
     * @param idx value index
     * @return the value at index <tt>idx</tt>
     */
    public Value get(final int idx) throws ArrayIndexOutOfBoundsException {
        if ((getTag() == Block.INFIX_TAG) && (idx == 0)) {
            return this.values[0];
        } else if (((idx >= this.values.length) || (idx < 0)) || (getTag() == Block.INFIX_TAG)) {
            final Block parent = this.parent;
            if (parent == null) {
                throw new ArrayIndexOutOfBoundsException(idx);
            }
            final Value[] blocks = parent.values;
            final int len = blocks.length;
            int i = 0;
            while ((i < len) && (!blocks[i].isBlock() || (this != blocks[i].asBlock()))) {
                i++;
            }
            return blocks[i + idx];
        } else {
            return this.values[idx];
        }
    }

    /**
     * Sets a value of the block.
     * @param idx value index
     * @param x new value
     * @throws ArrayIndexOutOfBoundsException if index is out of bounds
     */
    public void set(final int idx, final Value x) throws ArrayIndexOutOfBoundsException {
        if ((getTag() == Block.INFIX_TAG) && (idx == 0)) {
            this.values[0] = x;
        } else if (((idx >= this.values.length) || (idx < 0)) || (getTag() == Block.INFIX_TAG)) {
            final Block parent = this.parent;
            if (parent == null) {
                throw new ArrayIndexOutOfBoundsException(idx);
            }
            final Value[] blocks = parent.values;
            final int len = blocks.length;
            int i = 0;
            while ((i < len) && (!blocks[i].isBlock() || (this != blocks[i].asBlock()))) {
                i++;
            }
            final int index = i + idx;
            blocks[index] = x;
            if (x.isLong() && (index + 1 < len) && blocks[index + 1].isBlock() && blocks[index + 1].asBlock().getTag() == Block.INFIX_TAG) {
                blocks[index + 1].asBlock().header = x.asLong();
            }
            if (x.isBlock() && (x.asBlock().getTag() == Block.INFIX_TAG) && (index - 1 >= 0)) {
                blocks[index - 1] = Value.createFromRawValue(x.asBlock().getHeader());
            }
        } else {
            this.values[idx] = x;
        }
    }

    /**
     * Returns the size of the block in term of underlying values.
     * @return the size of the block
     */
    public int sizeValues() {
        return this.values != null ? this.values.length : 0;
    }

    /**
     * Returns the code value of the block. <br/>
     * The code value of a block is stored at index 0 with a 'code offfset'
     * value. <br/>
     * Should be called on a 'closure' or 'infix' block.
     * @return the code value of the block
     */
    public int getCode() {
        assert (getTag() == Block.CLOSURE_TAG) || (getTag() == Block.INFIX_TAG) : "invalid type";
        return this.get(0).asCodeOffset();
    }

    /**
     * Sets the code value of the block. <br/>
     * The code value of a block is stored at index 0 with a 'code offfset'
     * value. <br/>
     * Should be called on a 'closure' or 'infix' block.
     * @param code new code value
     */
    public void setCode(final int code) {
        assert (getTag() == Block.CLOSURE_TAG) || (getTag() == Block.INFIX_TAG) : "invalid type";
        set(0, Value.createFromCodeOffset(code));
    }

    /**
     * Returns the string value of the block. <br/>
     * Should be called on a 'string' block.
     * @return the string value of the block
     */
    public String asString() {
        assert getTag() == Block.STRING_TAG : "invalid type";
        return Misc.convertBytesToString(this.bytes);
    }

    /**
     * Returns the underlying array of bytes (containing the string elements).
     * @return the underlying array of bytes
     */
    public byte[] getBytes() {
        return this.bytes;
    }

    /**
     * Returns the underlying array of unsigned bytes
     * (containing the string elements).
     * @return the underlying array of bytes, as unsigned
     */
    public int[] getUnsignedBytes() {
        final int len = this.bytes.length;
        final int[] res = new int[len];
        for (int i = 0; i < len; i++) {
            res[i] = Misc.signedToUnsignedByte(this.bytes[i]);
        }
        return res;
    }

    /**
     * Returns a byte of the the string value of the block. <br/>
     * Should be called on a 'string' block.
     * @param idx byte index
     * @return byte of string at index <tt>idx</tt>
     * @throws ArrayIndexOutOfBoundsException if index is out of bounds
     */
    public byte getByte(final int idx) throws ArrayIndexOutOfBoundsException {
        assert getTag() == Block.STRING_TAG : "invalid type";
        return this.bytes[idx];
    }

    /**
     * Sets a byte of the the string value of the block. <br/>
     * Should be called on a 'string' block.
     * @param idx byte index
     * @param x new byte value
     * @throws ArrayIndexOutOfBoundsException if index is out of bounds
     */
    public void setByte(final int idx, final byte x) throws ArrayIndexOutOfBoundsException {
        assert getTag() == Block.STRING_TAG : "invalid type";
        this.bytes[idx] = x;
    }

    /**
     * Returns an unsigned byte of the the string value of the block. <br/>
     * Should be called on a 'string' block.
     * @param idx byte index
     * @return unsigned byte of string at index <tt>idx</tt>
     * @throws ArrayIndexOutOfBoundsException if index is out of bounds
     */
    public int getUnsignedByte(final int idx) throws ArrayIndexOutOfBoundsException {
        assert getTag() == Block.STRING_TAG : "invalid type";
        return Misc.signedToUnsignedByte(getByte(idx));
    }

    /**
     * Sets an unsigned byte of the the string value of the block. <br/>
     * Should be called on a 'string' block.
     * @param idx byte index
     * @param x new byte value
     * @throws ArrayIndexOutOfBoundsException if index is out of bounds
     */
    public void setUnsignedByte(final int idx, final int x) throws ArrayIndexOutOfBoundsException {
        assert getTag() == Block.STRING_TAG : "invalid type";
        setByte(idx, (byte) Misc.unsignedToSignedByte(x));
    }

    /**
     * Returns the size of the string value. <br/>
     * Should be called on a 'string' block.
     * @return the size of the string
     */
    public int sizeBytes() {
        assert getTag() == Block.STRING_TAG : "invalid type";
        return this.bytes != null ? this.bytes.length : 0;
    }

    /**
     * Returns the double value of the block. <br/>
     * Should be called on a 'double' block.
     * @return the double value of the block
     */
    public double asDouble() {
        assert getTag() == Block.DOUBLE_TAG : "invalid type";
        return this.dbl;
    }

    /**
     * Sets the double value of the block. <br/>
     * Should be called on a 'double' block.
     * @param d new double value of the block
     */
    public void setDouble(final double d) {
        assert getTag() == Block.DOUBLE_TAG : "invalid type";
        this.dbl = d;
    }

    /**
     * Returns the double at a given index. <br/>
     * Should be called on a 'double array' block.
     * @param idx index of double to return
     * @return the double at index <tt>idx</tt>
     * @throws ArrayIndexOutOfBoundsException if index is out of bounds
     */
    public double getDouble(final int idx) throws ArrayIndexOutOfBoundsException {
        assert getTag() == Block.DOUBLE_ARRAY_TAG : "invalid type";
        return this.dblArray[idx];
    }

    /**
     * Sets the double at a given index. <br/>
     * Should be called on a 'double array' block.
     * @param idx index of double to return
     * @param d new double value
     * @throws ArrayIndexOutOfBoundsException if index is out of bounds
     */
    public void setDouble(final int idx, final double d) throws ArrayIndexOutOfBoundsException {
        assert getTag() == Block.DOUBLE_ARRAY_TAG : "invalid type";
        this.dblArray[idx] = d;
    }

    /**
     * Returns the size of the double array. <br/>
     * Should be called on a 'double array' block.
     * @return the size of the double array
     */
    public int sizeDoubles() {
        assert getTag() == Block.DOUBLE_ARRAY_TAG : "invalid type";
        return this.dblArray != null ? this.dblArray.length : 0;
    }

    /**
     * Returns the custom operations of the block. <br/>
     * Should be called on a 'custom' block.
     * @return the custom operations of the block
     */
    public Custom.Operations getCustomOperations() {
        assert getTag() == Block.CUSTOM_TAG : "invalid type";
        return this.custom;
    }

    /**
     * Returns the custom value of the block. <br/>
     * Should be called on a 'custom' or 'abstract' block.
     * @return the custom value of the block
     */
    public Object asCustom() {
        assert (getTag() == Block.CUSTOM_TAG) || (getTag() == Block.ABSTRACT_TAG) || (getTag() == Block.CLOSURE_TAG) || (getTag() == Block.INFIX_TAG) : "invalid type";
        return this.obj;
    }

    /**
     * Sets the custom value of the block. <br/>
     * Should be called on a 'custom' or 'abstract' block.
     * @param o new custom value
     */
    public void setCustom(final Object o) {
        assert (getTag() == Block.CUSTOM_TAG) || (getTag() == Block.ABSTRACT_TAG) || (getTag() == Block.CLOSURE_TAG) || (getTag() == Block.INFIX_TAG) : "invalid type";
        this.obj = o;
    }

    /**
     * Equivalent to {@link #get(int)} with weak storage. <br/>
     * Weak storage means that a value can be garbage collected even if it is
     * still reachable.
     * @param i index of value
     * @return the value at the given index
     * @throws ArrayIndexOutOfBoundsException if index is out of bounds
     */
    public Value getWeak(final int i) throws ArrayIndexOutOfBoundsException {
        return (Value) this.weakValues[i].get();
    }

    /**
     * Equivalent to {@link #set(int, Value)} with weak storage. <br/>
     * Weak storage means that a value can be garbage collected even if it is
     * still reachable.
     * @param i index of value to change
     * @param v new value
     * @throws ArrayIndexOutOfBoundsException if index is out of bounds
     */
    public void setWeak(final int i, final Value v) throws ArrayIndexOutOfBoundsException {
        this.weakValues[i] = new WeakReference<Value>(v);
    }

    /**
     * Returns the size of the block in term of underlying weak values.
     * @return the size of the block
     */
    public int sizeWeakValues() {
        return this.weakValues != null ? this.weakValues.length : 0;
    }

    /**
     * Returns the custom value of the block. <br/>
     * Should be called on a 'custom' block containing an 'int32' value.
     * @return the custom value of the block
     */
    public int asInt32() {
        assert getTag() == Block.CUSTOM_TAG : "invalid type";
        return this.int32;
    }

    /**
     * Sets the custom value of the block. <br/>
     * Should be called on a 'custom' block containing an 'int32' value.
     * @param v new custom value
     */
    public void setInt32(final int v) {
        assert getTag() == Block.CUSTOM_TAG : "invalid type";
        this.int32 = v;
    }

    /**
     * Returns the custom value of the block. <br/>
     * Should be called on a 'custom' block containing a 'nativeint' value. <br/>
     * Equivalent to {@link #asInt32()}, as Cadmium is 32-bit.
     * @return the custom value of the block
     */
    public int asNativeInt() {
        assert getTag() == Block.CUSTOM_TAG : "invalid type";
        return this.int32;
    }

    /**
     * Sets the custom value of the block. <br/>
     * Should be called on a 'custom' block containing a 'nativeint' value. <br/>
     * Equivalent to {@link #setInt32(int)}, as Cadmium is 32-bit.
     * @param v new custom value
     */
    public void setNativeInt(final int v) {
        assert getTag() == Block.CUSTOM_TAG : "invalid type";
        this.int32 = v;
    }

    /**
     * Returns the custom value of the block. <br/>
     * Should be called on a 'custom' block containing an 'int64' value.
     * @return the custom value of the block
     */
    public long asInt64() {
        assert getTag() == Block.CUSTOM_TAG : "invalid type";
        return this.int64;
    }

    /**
     * Sets the custom value of the block. <br/>
     * Should be called on a 'custom' block containing an 'int64' value.
     * @param v new custom value
     */
    public void setInt64(final long v) {
        assert getTag() == Block.CUSTOM_TAG : "invalid type";
        this.int64 = v;
    }

    /**
     * Sets the reference to the parent block. <br/>
     * Used if block is part of a infix/"recursive" closure.
     * @param b block to be considered as parent
     */
    void setParent(final Block b) {
        this.parent = b;
    }

    /**
     * Returns the reference to the parent block. <br/>
     * Used if block is part of a infix/"recursive" closure.
     * @return the reference to the next block
     */
    public Block getParent() {
        return this.parent;
    }

    /**
     * Returns the value at a given offset, relatively to current block.
     * @param ofs offset
     * @return the value at offset <tt>ofs</tt> relatively to current block
     */
    public Value offset(final int ofs) {
        if ((getTag() == Block.CLOSURE_TAG) || (getTag() == Block.OBJECT_TAG)) {
            return get(ofs);
        } else if (getTag() == Block.INFIX_TAG) {
            final Block parent = getParent();
            final int len = parent.sizeValues();
            int i = 0;
            while ((i < len) && (!parent.get(i).isBlock() || (this != parent.get(i).asBlock()))) {
                i++;
            }
            final int idx = i + ofs;
            if (idx == 0) {
                return Value.createFromBlock(parent);
            } else {
                return parent.get(idx);
            }
        } else {
            assert false : "invalid block for offset";
            return null;
        }
    }

    /**
     * Adds a finalizer for the value represented by this block. <br/>
     * When the current block is garbage collected, the passed unary function
     * is applied with the block as parameter.
     * @param f unary function to run over current block when it is
     *          garbage collected - should not be <tt>null</tt> <br/>
     * @param runner code runner, needed as passed function is called as a
     *               callback - should not be <tt>null</tt> <br/>
     */
    public void addFinalizer(final Value f, final CodeRunner runner) {
        assert f != null : "null f";
        assert runner != null : "null runner";
        assert (this.codeRunner == null) || (this.codeRunner != runner) : "value already registered with another code runner";
        if (this.finalizers == null) {
            this.finalizers = new LinkedList<Value>();
            this.codeRunner = runner;
        }
        this.finalizers.add(f);
    }

    /**
     * Truncate a block.
     * @param size new block size
     * @throws Fail.Exception if requested size is <= 0 or > current size
     */
    public void truncate(final int size) throws Fail.Exception {
        final int oldSize = getWoSize();
        final int oldTag = getTag();
        final int newSize = oldTag == Block.DOUBLE_TAG ? size * Block.DOUBLE_SIZE : size;
        if ((newSize <= 0) || (newSize > oldSize)) {
            Fail.invalidArgument("Obj.truncate");
        }
        setHeader(makeHeader(newSize, oldTag));
        if (this.dblArray != null) {
            this.dblArray = Arrays.copyOf(this.dblArray, newSize / Block.DOUBLE_SIZE);
        }
        if (this.bytes != null) {
            this.bytes = Arrays.copyOf(this.bytes, newSize * 4);
        }
        if (this.values != null) {
            this.values = Arrays.copyOf(this.values, newSize);
        }
        if (this.weakValues != null) {
            final WeakReference[] newValues = new WeakReference[newSize];
            System.arraycopy(this.weakValues, 0, newValues, 0, newSize);
            this.weakValues = newValues;
        }
    }

    /**
     * Copies a given block into this one. <br/>
     * Magic number, header, parent, finalizers and code runner are not
     * modified.
     * @param src block to copy - should not be <tt>null</tt>
     */
    public void copy(final Block src) {
        assert src != null : "null src";
        this.obj = src.obj;
        this.dbl = src.dbl;
        if (src.dblArray != null) {
            this.dblArray = Arrays.copyOf(src.dblArray, src.dblArray.length);
        } else {
            this.dblArray = null;
        }
        this.int32 = src.int32;
        this.int64 = src.int64;
        if (src.bytes != null) {
            this.bytes = Arrays.copyOf(src.bytes, src.bytes.length);
        } else {
            this.bytes = null;
        }
        this.custom = src.custom;
        if (src.values != null) {
            this.values = Arrays.copyOf(src.values, src.values.length);
        } else {
            this.values = null;
        }
        if (src.weakValues != null) {
            final int l = src.weakValues.length;
            this.weakValues = new WeakReference[l];
            System.arraycopy(src.weakValues, 0, this.weakValues, 0, l);
        } else {
            this.weakValues = null;
        }
    }

    /**
     * Duplicates the block.
     * @return a (shallow) copy of the block
     */
    public Block duplicate() {
        final Block res = new Block();
        res.header = this.header;
        res.parent = null;
        res.finalizers = null;
        res.codeRunner = null;
        res.copy(this);
        return res;
    }

    /**
     * Runs the finalizers for the block.
     * @throws Throwable as thrown by a finalizer
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.custom != null) {
            this.custom.finalize(Value.createFromBlock(this));
        }
        if (this.finalizers != null) {
            final Context ctxt = this.codeRunner.getContext();
            final List<Value> finals = new LinkedList<Value>(this.finalizers);
            this.finalizers = null;
            for (Value closure : finals) {
                try {
                    ctxt.acquireFinalizeLock();
                } catch (final Fail.Exception fe) {
                    return;
                } catch (final FalseExit fe) {
                    return;
                }
                try {
                    this.codeRunner.callback(closure, Value.createFromBlock(duplicate()));
                } catch (final Fail.Exception fe) {
                    final Channel ch = ctxt.getChannel(Channel.STDERR);
                    if ((ch != null) && (ch.asOutputStream() != null)) {
                        final String msg = fr.x9c.cadmium.kernel.Misc.convertException(fe.asValue(ctxt.getGlobalData()));
                        final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                        err.println("Error in finalizer: exception " + msg);
                        err.close();
                    }
                } catch (final Fatal.Exception fe) {
                    final Channel ch = ctxt.getChannel(Channel.STDERR);
                    if ((ch != null) && (ch.asOutputStream() != null)) {
                        final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                        err.println("Error in finalizer: exception " + fe.getMessage());
                        err.close();
                    }
                } catch (final CadmiumException ie) {
                    final Channel ch = ctxt.getChannel(Channel.STDERR);
                    if ((ch != null) && (ch.asOutputStream() != null)) {
                        final PrintStream err = new PrintStream(ch.asOutputStream(), true);
                        err.println("Error in finalizer: exception " + ie.getMessage());
                        err.close();
                    }
                } finally {
                    ctxt.releaseFinalizeLock();
                }
            }
        }
    }
}

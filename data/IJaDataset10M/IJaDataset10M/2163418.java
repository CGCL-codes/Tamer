package fr.x9c.cadmium.primitives.bigarray;

import java.io.DataInput;
import java.io.DataOutput;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import fr.x9c.cadmium.kernel.Block;
import fr.x9c.cadmium.kernel.Context;
import fr.x9c.cadmium.kernel.Custom;
import fr.x9c.cadmium.kernel.Fail;
import fr.x9c.cadmium.kernel.Fatal;
import fr.x9c.cadmium.kernel.Value;
import fr.x9c.cadmium.util.Misc;

/**
 * This class implements the actual array,
 * backed up by a {@link java.nio.ByteBuffer}. <br/>
 * (Roughly equivalent to "caml_bigarray" type).
 *
 * @author <a href="mailto:cadmium@x9c.fr">Xavier Clerc</a>
 * @version 1.0
 * @since 1.0
 */
final class MemArray {

    /** Data buffer. */
    private final ByteBuffer data;

    /** Array Dimensions. */
    private final int[] dim;

    /** Flags. */
    private final int flags;

    /**
     * Allocates a big array.
     * @param flags flags controlling array type
     * @param dim dimensions - should not be <tt>null</tt>
     * @param dat data buffer if already allocated - can be <tt>null</tt>
     * @throws Fail.Exception if there is not enough memory left
     * @throws Fatal.Exception if requested size does not fit into a
     *                         32-bit integer
     */
    MemArray(final int flags, final int[] dim, final ByteBuffer dat) throws Fail.Exception, Fatal.Exception {
        final int numDims = dim.length;
        assert (numDims >= 1) && (numDims <= BigArray.MAX_NUM_DIMS) : "invalid number of dimensions";
        assert (flags & BigArray.CAML_BA_KIND_MASK) <= BigArray.CAML_BA_COMPLEX64 : "invalid element kind";
        final int[] dimCopy = new int[numDims];
        System.arraycopy(dim, 0, dimCopy, 0, numDims);
        if (dat == null) {
            long numElems = 1;
            for (int i = 0; i < numDims; i++) {
                numElems *= dimCopy[i];
                if ((numElems < 0) || (numElems > Integer.MAX_VALUE)) {
                    Fail.raiseOutOfMemory();
                }
            }
            final long size = numElems * BigArray.CAML_BA_ELEMENT_SIZE[flags & BigArray.CAML_BA_KIND_MASK];
            if ((size < 0) || (size > Integer.MAX_VALUE)) {
                Fail.raiseOutOfMemory();
            }
            this.data = ByteBuffer.allocate(Misc.ensure32s(size));
            this.flags = (flags & (BigArray.CAML_BA_KIND_MASK | BigArray.CAML_BA_LAYOUT_MASK)) | BigArray.CAML_BA_EXTERNAL;
        } else {
            this.data = dat;
            this.flags = (flags & (BigArray.CAML_BA_KIND_MASK | BigArray.CAML_BA_LAYOUT_MASK)) | BigArray.CAML_BA_MAPPED_FILE;
        }
        this.dim = dimCopy;
    }

    /**
     * Returns the underlying byte buffer containing array data.
     * @return the underlying byte buffer containing array data
     */
    ByteBuffer getData() {
        return this.data;
    }

    /**
     * Returns the number of dimensions of the array.
     * @return the number of dimensions of the array
     */
    int getNumDims() {
        return this.dim.length;
    }

    /**
     * Returns the array of array dimensions.
     * @return the array of array dimensions
     */
    int[] getDims() {
        return this.dim;
    }

    /**
     * Returns the value of a dimension.
     * @param d dimension index
     * @return value of given dimension
     * @throws ArrayIndexOutOfBoundsException if <tt>d</tt> is invalid
     */
    int getDim(final int d) throws ArrayIndexOutOfBoundsException {
        return this.dim[d];
    }

    /**
     * Returns the array flags (layout, elements kind, <i>etc</i>).
     * @return the array flags
     */
    int getFlags() {
        return this.flags;
    }

    /**
     * Gets an element from the array.
     * @param index index array (one index per array dimension)
     * @return the array element at the given index
     */
    Value get(final int[] index) throws Fail.Exception {
        assert index != null : "null index";
        if (index.length != this.dim.length) {
            Fail.invalidArgument("Bigarray.get: wrong number of indices");
        }
        final int offset = offset(index);
        switch(this.flags & BigArray.CAML_BA_KIND_MASK) {
            case BigArray.CAML_BA_FLOAT32:
                return Value.createFromBlock(Block.createDouble(this.data.getFloat(offset * BigArray.SIZE_FLOAT32)));
            case BigArray.CAML_BA_FLOAT64:
                return Value.createFromBlock(Block.createDouble(this.data.getDouble(offset * BigArray.SIZE_FLOAT64)));
            case BigArray.CAML_BA_SINT8:
                return Value.createFromLong(this.data.get(offset * BigArray.SIZE_SINT8));
            case BigArray.CAML_BA_UINT8:
                return Value.createFromLong(Misc.signedToUnsignedByte(this.data.get(offset * BigArray.SIZE_UINT8)));
            case BigArray.CAML_BA_SINT16:
                return Value.createFromLong(this.data.getShort(offset * BigArray.SIZE_SINT16));
            case BigArray.CAML_BA_UINT16:
                return Value.createFromLong(Misc.signedToUnsignedShort(this.data.getShort(offset * BigArray.SIZE_UINT16)));
            case BigArray.CAML_BA_INT32:
                final Block b32 = Block.createCustom(Custom.INT_32_SIZE, Custom.INT_32_OPS);
                b32.setInt32(this.data.getInt(offset * BigArray.SIZE_INT32));
                return Value.createFromBlock(b32);
            case BigArray.CAML_BA_INT64:
                final Block b64 = Block.createCustom(Custom.INT_64_SIZE, Custom.INT_64_OPS);
                b64.setInt64(this.data.getLong(offset * BigArray.SIZE_INT64));
                return Value.createFromBlock(b64);
            case BigArray.CAML_BA_NATIVE_INT:
                final Block bn = Block.createCustom(Custom.INT_NAT_SIZE, Custom.INT_NAT_OPS);
                bn.setNativeInt(this.data.getInt(offset * BigArray.SIZE_NATIVE_INT));
                return Value.createFromBlock(bn);
            case BigArray.CAML_BA_CAML_INT:
                return Value.createFromLong(this.data.getInt(offset * BigArray.SIZE_CAML_INT));
            case BigArray.CAML_BA_COMPLEX32:
                final Block c32 = Block.createDoubleArray(2);
                c32.setDouble(0, this.data.getFloat(offset * BigArray.SIZE_COMPLEX32));
                c32.setDouble(1, this.data.getFloat(offset * BigArray.SIZE_COMPLEX32) + (BigArray.SIZE_COMPLEX32 / 2));
                return Value.createFromBlock(c32);
            case BigArray.CAML_BA_COMPLEX64:
                final Block c64 = Block.createDoubleArray(2);
                c64.setDouble(0, this.data.getDouble(offset * BigArray.SIZE_COMPLEX64));
                c64.setDouble(1, this.data.getDouble(offset * BigArray.SIZE_COMPLEX64) + (BigArray.SIZE_COMPLEX64 / 2));
                return Value.createFromBlock(c64);
            default:
                assert false : "invalid elements kind";
                return Value.UNIT;
        }
    }

    /**
     * Sets an element of the array.
     * @param index index array (one index per array dimension)
     * @param newval value of the array element at the given index
     */
    void set(final int[] index, final Value newval) throws Fail.Exception {
        assert index != null : "null index";
        if (index.length != this.dim.length) {
            Fail.invalidArgument("Bigarray.set: wrong number of indices");
        }
        final int offset = offset(index);
        switch(this.flags & BigArray.CAML_BA_KIND_MASK) {
            case BigArray.CAML_BA_FLOAT32:
                this.data.putFloat(offset * BigArray.SIZE_FLOAT32, (float) newval.asBlock().asDouble());
                break;
            case BigArray.CAML_BA_FLOAT64:
                this.data.putDouble(offset * BigArray.SIZE_FLOAT64, newval.asBlock().asDouble());
                break;
            case BigArray.CAML_BA_SINT8:
            case BigArray.CAML_BA_UINT8:
                this.data.put(offset * BigArray.SIZE_SINT8, (byte) newval.asLong());
                break;
            case BigArray.CAML_BA_SINT16:
            case BigArray.CAML_BA_UINT16:
                this.data.putShort(offset * BigArray.SIZE_SINT16, (short) newval.asLong());
                break;
            case BigArray.CAML_BA_INT32:
                this.data.putInt(offset * BigArray.SIZE_INT32, newval.asBlock().asInt32());
                break;
            case BigArray.CAML_BA_INT64:
                this.data.putLong(offset * BigArray.SIZE_INT64, newval.asBlock().asInt64());
                break;
            case BigArray.CAML_BA_NATIVE_INT:
                this.data.putInt(offset * BigArray.SIZE_NATIVE_INT, newval.asBlock().asNativeInt());
                break;
            case BigArray.CAML_BA_CAML_INT:
                this.data.putInt(offset * BigArray.SIZE_CAML_INT, newval.asLong());
                break;
            case BigArray.CAML_BA_COMPLEX32:
                this.data.putFloat(offset * BigArray.SIZE_COMPLEX32, (float) newval.asBlock().getDouble(0));
                this.data.putFloat(offset * BigArray.SIZE_COMPLEX32 + (BigArray.SIZE_COMPLEX32 / 2), (float) newval.asBlock().getDouble(1));
                break;
            case BigArray.CAML_BA_COMPLEX64:
                this.data.putDouble(offset * BigArray.SIZE_COMPLEX64, newval.asBlock().getDouble(0));
                this.data.putDouble(offset * BigArray.SIZE_COMPLEX64 + (BigArray.SIZE_COMPLEX64 / 2), newval.asBlock().getDouble(1));
                break;
            default:
                assert false : "invalid elements kind";
        }
    }

    /**
     * Returns the number of elements of the array.
     * @return the number of elements of the array
     */
    int getNumElems() {
        int res = 1;
        final int len = this.dim.length;
        for (int i = 0; i < len; i++) {
            res *= this.dim[i];
        }
        return res;
    }

    /**
     * Returns the offset of an array element in the byte buffer.
     * @param index index array (one index per array dimension)
     * @return the offset of the given index in the byte buffer
     */
    int offset(int[] index) throws Fail.Exception {
        assert index != null : "null index";
        int offset = 0;
        if ((this.flags & BigArray.CAML_BA_LAYOUT_MASK) == BigArray.CAML_BA_C_LAYOUT) {
            for (int i = 0; i < index.length; i++) {
                if (index[i] >= this.dim[i]) {
                    Fail.arrayBoundError();
                }
                offset = offset * this.dim[i] + index[i];
            }
        } else {
            for (int i = index.length - 1; i >= 0; i--) {
                if (index[i] - 1 >= this.dim[i]) {
                    Fail.arrayBoundError();
                }
                offset = offset * this.dim[i] + (index[i] - 1);
            }
        }
        return offset;
    }

    /**
     * Unmaps the underlying byte buffer (that should be mapped). <br/>
     * This implementation only ensures that file data is synchronized with
     * memory data.
     */
    void unMap() {
        ((MappedByteBuffer) this.data).force();
    }
}

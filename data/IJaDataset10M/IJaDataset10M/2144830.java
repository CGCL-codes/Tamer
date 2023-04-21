package edu.rice.cs.cunit.classFile.constantPool;

import edu.rice.cs.cunit.classFile.constantPool.APoolInfo;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Constant pool.
 *
 * @author Mathias Ricken
 */
public class ConstantPool extends ArrayList<APoolInfo> {

    /**
     * Constructs an empty constant pool with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the constant pool.
     *
     * @throws IllegalArgumentException if the specified initial capacity is negative
     */
    public ConstantPool(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs an empty constant pool with an initial capacity of ten.
     */
    public ConstantPool() {
    }

    /**
     * Constructs a constant pool containing the elements of the specified collection, in the order they are returned
     * by the collection's iterator.  The <tt>ConstantPool</tt> instance has an initial capacity of 110% the size of
     * the specified collection.
     *
     * @param aPoolInfos the collection whose elements are to be placed into this constant pool.
     *
     * @throws NullPointerException if the specified collection is null.
     */
    public ConstantPool(Collection<? extends APoolInfo> aPoolInfos) {
        super(aPoolInfos);
    }

    /**
     * Return the index of the pool item in the pool.
     *
     * @param item          item
     *
     * @return index
     */
    public short indexOf(APoolInfo item) {
        for (int i = 0; i < size(); i++) {
            if (item == get(i)) {
                return (short) i;
            }
        }
        throw new AssertionError("Item not in pool: " + item.toStringVerbose());
    }
}

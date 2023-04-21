package com.ibm.tuningfork.infra.data;

import java.nio.ByteBuffer;
import com.ibm.tuningfork.infra.event.AttributeType;
import com.ibm.tuningfork.infra.event.TupleIndexOutOfBoundsException;
import com.ibm.tuningfork.infra.stream.precise.PersistenceUtil;

/**
 * A Tuple with no string or relation values, at least one long or double value,
 * and "many" int and boolean values (if there are "few" or no int and boolean values use Tuple_Big).
 */
public class Tuple_BigSmall extends Tuple {

    /** Number of ints  */
    private int numInts;

    /** Number of longs  */
    private int numLongs;

    /** The 8-byte data, always encoded as longs, in the order longs, doubles. The number of
     *   doubles is implicit knowing the number of longs */
    private long[] bigData;

    /** The small data, always encoded as ints, in the order ints, booleans. The number of
     *   booleans is implicit knowing the number of ints */
    private int[] smallData;

    /**
     * Make a Tuple_BigSmall.  At least one of longs or doubles should be non-null for efficient use of this
     *   class and at least one of ints or booleans should more than a few members.
     * @param ints the int values or null if none
     * @param longs the long values or null if none
     * @param doubles the double values or null if none
     * @param booleans the boolean values or null if none
     */
    public Tuple_BigSmall(int[] ints, long[] longs, double[] doubles, boolean[] booleans) {
        numInts = (ints == null) ? 0 : ints.length;
        numLongs = (longs == null) ? 0 : longs.length;
        bigData = new long[numLongs + ((doubles == null) ? 0 : doubles.length)];
        smallData = new int[numInts + ((booleans == null) ? 0 : booleans.length)];
        int index = 0;
        if (numLongs > 0) {
            for (int i = 0; i < numLongs; i++) {
                bigData[index++] = longs[i];
            }
        }
        if (doubles != null) {
            for (int i = 0; i < doubles.length; i++) {
                bigData[index++] = Double.doubleToLongBits(doubles[i]);
            }
        }
        index = 0;
        if (numInts > 0) {
            for (int i = 0; i < numInts; i++) {
                smallData[index++] = ints[i];
            }
        }
        if (booleans != null) {
            for (int i = 0; i < booleans.length; i++) {
                smallData[index++] = booleans[i] ? 1 : 0;
            }
        }
    }

    public boolean getBoolean(int booleanIndex) {
        int index = booleanIndex + numInts;
        if (booleanIndex < 0 || index >= smallData.length) {
            throw new TupleIndexOutOfBoundsException(booleanIndex, AttributeType.BOOLEAN);
        }
        return smallData[index] != 0;
    }

    public double getDouble(int attribute) {
        int index = attribute + numLongs;
        if (attribute < 0 || index >= bigData.length) {
            throw new TupleIndexOutOfBoundsException(attribute, AttributeType.DOUBLE);
        }
        return Double.longBitsToDouble(bigData[index]);
    }

    public int getInt(int attribute) {
        if (attribute < 0 || attribute >= numInts) {
            throw new TupleIndexOutOfBoundsException(attribute, AttributeType.INT);
        }
        return (int) smallData[attribute];
    }

    public long getLong(int attribute) {
        if (attribute < 0 || attribute >= numLongs) {
            throw new TupleIndexOutOfBoundsException(attribute, AttributeType.LONG);
        }
        return bigData[attribute];
    }

    public int numBooleans() {
        return smallData.length - numInts;
    }

    public int numDoubles() {
        return bigData.length - numLongs;
    }

    public int numInts() {
        return numInts;
    }

    public int numLongs() {
        return numLongs;
    }

    /**
     * The TupleFactory for this kind of Tuple
     */
    static final class Factory implements TupleFactory {

        private int numberOfInts;

        private int numberOfLongs;

        private int numberOfDoubles;

        private int numberOfBooleans;

        /**
         * Make a new Factory
         * @param numberOfInts number of ints
         * @param numberOfLongs number of longs
         * @param numberOfDoubles number of doubles
         * @param numberOfBooleans number of booleans
         */
        Factory(int numberOfInts, int numberOfLongs, int numberOfDoubles, int numberOfBooleans) {
            this.numberOfInts = numberOfInts;
            this.numberOfLongs = numberOfLongs;
            this.numberOfDoubles = numberOfDoubles;
            this.numberOfBooleans = numberOfBooleans;
        }

        public ITuple newInstance(Long time, int[] ints, long[] longs, double[] doubles, String[] strings, boolean[] booleans, IRelation[] relations) {
            return new Tuple_BigSmall(ints, longs, doubles, booleans);
        }

        public ITuple read(ByteBuffer buffer) {
            return new Tuple_BigSmall(PersistenceUtil.readIntArray(buffer, numberOfInts), PersistenceUtil.readLongArray(buffer, numberOfLongs), PersistenceUtil.readDoubleArray(buffer, numberOfDoubles), PersistenceUtil.readBooleanArray(buffer, numberOfBooleans));
        }
    }
}

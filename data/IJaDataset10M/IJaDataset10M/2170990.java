package ucar.multiarray;

import java.lang.reflect.Array;

/**
 * Use with MultiArrayProxy to reduce the apparent rank of
 * the delegate by fixing an index at particular value.
 *
 * @see IndexMap
 * @see MultiArrayProxy
 *
 * @author $Author: rboller_cvs $
 * @version $Revision: 1.1 $ $Date: 2009/04/17 18:05:26 $
 */
public class SliceMap extends ConcreteIndexMap {

    /**
	 * Create an ConcreteIndexMap which fixes the key for a particular
	 * dimension at a particular value.
	 *
	 * @param position the dimension number on which to fix the key.
	 * @param value the value at which to fix the key.
	 */
    public SliceMap(int position, int value) {
        init(new IMap(), new LengthsMap());
        position_ = position;
        value_ = value;
    }

    /**
	 * Create an ConcreteIndexMap which fixes the key for a particular
	 * dimension at a particular value and is functionally composed
	 * with another ConcreteIndexMap.
	 *
	 * @param prev ConcreteIndexMap to be composed with this.
	 * @param position the dimension number on which to fix the key.
	 * @param value the value at which to fix the key.
	 */
    public SliceMap(ConcreteIndexMap prev, int position, int value) {
        link(prev, new IMap(), new LengthsMap());
        position_ = position;
        value_ = value;
    }

    private class IMap extends ZZMap {

        public synchronized int get(int key) {
            if (key < position_) return super.get(key);
            if (key == position_) return value_;
            return super.get(key - 1);
        }

        public synchronized int size() {
            return super.size() + 1;
        }
    }

    private class LengthsMap extends ZZMap {

        public synchronized int get(int key) {
            final int adjust = key < position_ ? key : key + 1;
            return super.get(adjust);
        }

        public synchronized int size() {
            return super.size() - 1;
        }
    }

    private int position_;

    private int value_;

    public static void main(String[] args) {
        final int[] shape = { 48, 64 };
        MultiArrayImpl delegate = new MultiArrayImpl(Integer.TYPE, shape);
        {
            final int size = MultiArrayImpl.numberOfElements(shape);
            for (int ii = 0; ii < size; ii++) java.lang.reflect.Array.setInt(delegate.storage, ii, ii);
        }
        IndexMap im = new SliceMap(1, 1);
        MultiArray ma = new MultiArrayProxy(delegate, im);
        try {
            System.out.println("Rank  " + ma.getRank());
            int[] lengths = ma.getLengths();
            System.out.println("Shape { " + lengths[0] + " }");
            System.out.println(ma.getInt(new int[] { 1 }));
        } catch (java.io.IOException ee) {
        }
    }
}

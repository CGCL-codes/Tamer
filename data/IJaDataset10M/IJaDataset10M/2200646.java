package cunei.sort;

import java.util.Comparator;
import cunei.bits.UnsignedArray;
import cunei.util.Log;

public class Sorter {

    private static class BufferedSorter implements Comparator<Integer>, Swapable {

        private final Comparator<Integer> comparator;

        protected final Swapable swapper;

        protected final UnsignedArray buffer;

        public BufferedSorter(int size, Comparator<Integer> comparator, Swapable swapper) {
            this.comparator = comparator;
            this.swapper = swapper;
            buffer = new UnsignedArray(size, size);
        }

        public final int compare(Integer aLoc, Integer bLoc) {
            return comparator.compare(get(aLoc), get(bLoc));
        }

        public final void flush(int size) {
            for (int aLoc = 0; aLoc < size; aLoc++) {
                int bLoc = get(aLoc);
                while (bLoc < aLoc) bLoc = get(bLoc);
                if (aLoc != bLoc) swapper.swap(aLoc, bLoc);
            }
        }

        private final int get(int loc) {
            int value = (int) buffer.get(loc);
            return value > 0 ? value - 1 : loc;
        }

        public final void swap(int aLoc, int bLoc) {
            if (aLoc == bLoc) return;
            int aValue = get(aLoc) + 1;
            int bValue = get(bLoc) + 1;
            buffer.set(aLoc, bValue);
            buffer.set(bLoc, aValue);
        }
    }

    public static void sort(int size, Comparator<Integer> comparator, Swapable swapper) {
        BufferedSorter sorter = null;
        try {
            sorter = new BufferedSorter(size, comparator, swapper);
        } catch (OutOfMemoryError e) {
        }
        if (sorter == null) {
            Log.getInstance().info("Sorting collection (not bufffered)");
            HeapSort.sort(0, size, comparator, swapper);
        } else {
            Log.getInstance().info("Sorting collection (buffered)");
            HeapSort.sort(0, size, sorter, sorter);
            sorter.flush(size);
        }
    }
}

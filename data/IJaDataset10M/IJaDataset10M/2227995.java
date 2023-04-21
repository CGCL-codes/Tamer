package cunei.bits;

import cunei.util.Log;

public class ResizableUnsignedHash extends UnsignedHash {

    private static final long serialVersionUID = 1L;

    public ResizableUnsignedHash() {
        super();
    }

    public ResizableUnsignedHash(int size, long maxValue) {
        super(size, maxValue);
    }

    protected final void resize(int length, long maxValue) {
        Log.getInstance().finer("Resizing " + getClass().getName() + " to " + length + " (was " + length() + ") to hold " + maxValue + " (was " + array.mask() + ")");
        ManagedBuffer result = newBuffer(length, maxValue);
        int size = size();
        for (int pos = 0; size > 0; pos++) {
            long id = getKey(pos);
            if (id == -1) continue;
            UnsignedHash.set(result, id, getValue(pos));
            size--;
        }
        array = result;
    }

    public final UnsignedHash save(String path, String file) {
        final int size = size();
        final int bestLength = (int) Math.ceil(size / LOAD_FACTOR);
        if (bestLength != length()) resize(bestLength, array.mask());
        return new UnsignedHash(array, size).save(path, file);
    }

    public final void set(final long id, final long value) {
        long maxValue = Math.max(value, id + 1);
        if (array.mask() != -1L && maxValue > array.mask() || size + 1 > length() * LOAD_FACTOR && locate(array, id) < 0) {
            if (array.mask() == -1L || maxValue < array.mask()) maxValue = array.mask();
            for (double resize = 2.0; ; resize -= (resize - 1) / 2) try {
                final int length = Math.max((int) (resize * (size + 1) / LOAD_FACTOR), length());
                resize(length, maxValue);
                break;
            } catch (IllegalArgumentException e) {
                if (resize * size - size < 100) throw e;
            } catch (OutOfMemoryError e) {
                if (resize * size - size < 100) throw e;
            }
        }
        super.set(id, value);
    }
}

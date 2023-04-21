package ijaux.iter.array;

import ijaux.iter.ForwardIterator;
import java.util.NoSuchElementException;

public final class IntForwardIterator extends ArrayIterator<Integer> implements ForwardIterator<Integer> {

    private int[] ipixels;

    public IntForwardIterator(Object cpixels) {
        super.setPixels(cpixels);
        ipixels = (int[]) pixels;
    }

    @Override
    public Integer next() {
        return nextInt();
    }

    @Override
    public byte nextByte() {
        return (byte) (nextInt() & byteMask);
    }

    @Override
    public double nextDouble() {
        return nextInt();
    }

    @Override
    public float nextFloat() {
        return nextInt();
    }

    @Override
    public int nextInt() {
        return ipixels[i++];
    }

    @Override
    public short nextShort() {
        return (short) (nextInt() & shortMask);
    }

    @Override
    public void put(Integer val) {
        putInt(val);
    }

    @Override
    public void putInt(int val) {
        ipixels[i] = val;
        i++;
    }

    @Override
    public void putByte(byte val) {
        putInt(val);
    }

    @Override
    public void putShort(short val) {
        putInt(val);
    }

    @Override
    public void putFloat(float val) {
        putInt((int) val);
    }

    @Override
    public void putDouble(double val) {
        putInt((int) val);
    }

    @Override
    public Integer first() {
        i = 0;
        return ipixels[i];
    }
}

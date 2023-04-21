package ijaux.datatype.access;

import ijaux.datatype.Pair;
import ijaux.hypergeom.index.Indexing;

public class FloatAccess extends Access<Float> {

    private float[] fpixels;

    public FloatAccess(Object cpixels, Indexing<int[]> aind) {
        super.setPixels(cpixels);
        super.setIndexing(aind);
        fpixels = (float[]) pixels;
    }

    @Override
    public Float element(int index) {
        return elementFloat(index);
    }

    @Override
    public Float element(int[] coords) {
        return elementFloat(coords);
    }

    @Override
    public boolean elementBool(int index) {
        return (elementFloat(index) > 0);
    }

    @Override
    public boolean elementBool(int[] coords) {
        return (elementFloat(coords) > 0);
    }

    @Override
    public byte elementByte(int index) {
        return (byte) (elementInt(index) & byteMask);
    }

    @Override
    public byte elementByte(int[] coords) {
        return (byte) (elementInt(coords) & byteMask);
    }

    @Override
    public char elementChar(int index) {
        return 0;
    }

    @Override
    public char elementChar(int[] coords) {
        return 0;
    }

    @Override
    public double elementDouble(int index) {
        return elementFloat(index);
    }

    @Override
    public double elementDouble(int[] coords) {
        return elementFloat(coords);
    }

    @Override
    public float elementFloat(int index) {
        return fpixels[index];
    }

    @Override
    public float elementFloat(int[] coords) {
        index = pIndex.indexOf(coords);
        return elementFloat(index);
    }

    @Override
    public int elementInt(int index) {
        return (int) elementFloat(index);
    }

    @Override
    public int elementInt(int[] coords) {
        return (int) elementFloat(coords);
    }

    @Override
    public short elementShort(int index) {
        return (short) (elementInt(index) & shortMask);
    }

    @Override
    public short elementShort(int[] coords) {
        return (short) (elementInt(coords) & shortMask);
    }

    @Override
    public void putBool(int index, boolean value) {
        final float b = (value) ? 1 : 0;
        putFloat(index, b);
    }

    @Override
    public void putByte(int index, byte value) {
        putFloat(index, value);
    }

    @Override
    public void putChar(int index, char value) {
    }

    @Override
    public void putDouble(int index, double value) {
        putFloat(index, (float) value);
    }

    @Override
    public void putFloat(int index, float value) {
        fpixels[index] = value;
    }

    @Override
    public void putInt(int index, int value) {
        putFloat(index, value);
    }

    @Override
    public void putShort(int index, short value) {
        putFloat(index, value);
    }

    @Override
    public void putE(int index, Float value) {
        putFloat(index, value);
    }

    @Override
    public void put(Pair<int[], Float> pair) {
        putV(pair.first, pair.second);
    }

    @Override
    public void putV(int[] coords, Float value) {
        index = pIndex.indexOf(coords);
        putFloat(index, value);
    }
}

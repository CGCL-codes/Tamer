package ijaux.datatype.access;

import ijaux.datatype.Pair;
import ijaux.hypergeom.*;
import ijaux.hypergeom.index.Indexing;

public class PagedIntAccess extends PagedAccess<Integer> {

    private int[][] ipixels;

    private int mask = intMask;

    public PagedIntAccess(Object cpixels, Indexing<int[]> aind) {
        super(cpixels, aind);
        ipixels = (int[][]) pixels;
        pageSize = ipixels[0].length;
    }

    public void setMask(int m) {
        mask = m;
    }

    @Override
    public Integer element(int index) {
        return elementInt(index);
    }

    @Override
    public Integer element(int[] coords) {
        return elementInt(coords);
    }

    @Override
    public boolean elementBool(int index) {
        return (elementInt(index) > 0);
    }

    @Override
    public boolean elementBool(int[] coords) {
        return (elementInt(coords) > 0);
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
        return (char) elementInt(index);
    }

    @Override
    public char elementChar(int[] coords) {
        return (char) elementInt(coords);
    }

    @Override
    public double elementDouble(int index) {
        return elementInt(index);
    }

    @Override
    public double elementDouble(int[] coords) {
        return elementInt(coords);
    }

    @Override
    public float elementFloat(int index) {
        return elementInt(index);
    }

    @Override
    public float elementFloat(int[] coords) {
        return elementInt(coords);
    }

    @Override
    public int elementInt(int index) {
        x = index / pageSize;
        y = index - pageSize * x;
        return ipixels[x][y];
    }

    @Override
    public int elementInt(int[] coords) {
        index = pIndex.indexOf(coords);
        return elementInt(index);
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
        final int b = (value) ? -1 : 0;
        putInt(index, b);
    }

    @Override
    public void putByte(int index, byte value) {
        putInt(index, (value & byteMask));
    }

    @Override
    public void putChar(int index, char value) {
    }

    @Override
    public void putDouble(int index, double value) {
        putInt(index, (int) value);
    }

    @Override
    public void putFloat(int index, float value) {
        putInt(index, (int) value);
    }

    @Override
    public void putInt(int index, int value) {
        x = index / pageSize;
        y = index - pageSize * x;
        ipixels[x][y] = value;
    }

    @Override
    public void putShort(int index, short value) {
        putInt(index, (value & shortMask));
    }

    @Override
    public void putE(int index, Integer value) {
        putInt(index, value);
    }

    @Override
    public void put(Pair<int[], Integer> pair) {
        putV(pair.first, pair.second);
    }

    @Override
    public void putV(int[] coords, Integer value) {
        index = pIndex.indexOf(coords);
        putInt(index, value);
    }
}

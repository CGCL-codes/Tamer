package ktp.rpg;

import java.nio.ByteBuffer;

final class MultiBufferAccumulator implements Accumulator {

    private int originalCount;

    private int count;

    private int sum;

    private int faces;

    private ByteBuffer resultSet;

    public void init(int count, int faces, ByteBuffer dst) {
        this.count = this.originalCount = count;
        this.sum = 0;
        this.resultSet = dst;
        this.faces = faces;
    }

    public void dispose() {
        this.resultSet = null;
    }

    @Override
    public void add(int value) {
        sum += value;
    }

    @Override
    public int nextDie() {
        return count > 0 ? faces : 0;
    }

    @Override
    public int minCount() {
        return originalCount;
    }

    @Override
    public void result(int value) {
        count--;
        sum += value;
        resultSet.put((byte) value);
    }

    public int sum() {
        return sum;
    }
}

package gov.lanl.Database.poet;

public final class Sequence_ {

    private long sequence = 0;

    private int chunkSize = 30;

    public Sequence_() {
    }

    public Sequence_(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public long getMaxValue() {
        return sequence;
    }

    public synchronized long getNextValue() {
        long retSequence = sequence;
        sequence += chunkSize;
        return retSequence;
    }
}

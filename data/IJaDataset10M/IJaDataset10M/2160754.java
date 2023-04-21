package gov.lanl.Database.poet;

public final class POETSequence_ {

    private long sequence = 0;

    private int chunkSize = 30;

    public POETSequence_() {
    }

    public POETSequence_(int chunkSize) {
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

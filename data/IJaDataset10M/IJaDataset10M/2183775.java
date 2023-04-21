package com.tomgibara.crinch.record;

public abstract class AdaptedSequence<R, S> implements RecordSequence<S> {

    protected RecordSequence<R> sequence;

    public AdaptedSequence(RecordSequence<R> sequence) {
        if (sequence == null) throw new IllegalArgumentException("null sequence");
        this.sequence = sequence;
    }

    @Override
    public void remove() {
        sequence.remove();
    }

    @Override
    public boolean hasNext() {
        return sequence.hasNext();
    }

    @Override
    public void close() {
        sequence.close();
    }
}

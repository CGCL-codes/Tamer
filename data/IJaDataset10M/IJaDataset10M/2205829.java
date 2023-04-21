package org.achup.elgenerador.generator.basic;

public class LongGenerator extends AbstractBasicGenerator<Long> {

    private boolean incremental = false;

    private long min = Long.MIN_VALUE;

    private long max = Long.MAX_VALUE;

    private long nextValue = min;

    public LongGenerator() {
        super();
        incremental = false;
    }

    public LongGenerator(boolean incremental) {
        super();
        if (incremental) {
            this.incremental = true;
            nextValue = min;
        } else {
            this.incremental = false;
        }
    }

    @Override
    public String getName() {
        return "Long Generator";
    }

    @Override
    public Long next() {
        if (incremental) {
            if (nextValue < min) {
                nextValue = min;
            } else if (nextValue > max) {
                nextValue = min;
            }
            return (++nextValue) - 1;
        } else {
            return randomData.nextLong(min, max);
        }
    }

    @Override
    public void reset() {
        if (incremental) {
            nextValue = min;
        }
        super.reset();
    }

    public boolean isIncremental() {
        return incremental;
    }

    public void setIncremental(boolean incremental) {
        this.incremental = incremental;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }
}

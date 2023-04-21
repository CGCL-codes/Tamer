package uk.ac.liv.jt.codec;

public class Int32ProbCtxtEntry {

    long occCount;

    long cumCount;

    long associatedValue;

    long symbol;

    int nextContext;

    public Int32ProbCtxtEntry(long symbol, long occCount, long cumCount, long associatedValue, int nextContext) {
        super();
        this.symbol = symbol;
        this.occCount = occCount;
        this.cumCount = cumCount;
        this.associatedValue = associatedValue;
        this.nextContext = nextContext;
    }

    public long getAssociatedValue() {
        return associatedValue;
    }

    public long getSymbol() {
        return symbol;
    }

    public long getCumCount() {
        return cumCount;
    }

    public long getOccCount() {
        return occCount;
    }

    public int getNextContext() {
        return nextContext;
    }

    @Override
    public String toString() {
        return String.format("%d - %d(%d) - => %d - %d", symbol, occCount, cumCount, associatedValue, nextContext);
    }
}

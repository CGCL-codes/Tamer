package dovetaildb.scan;

public class IntegerScanner extends AbstractScanner {

    long upTo, docId;

    public IntegerScanner(long upTo) {
        docId = -1;
        this.upTo = upTo;
    }

    public IntegerScanner(long start, long upTo) {
        docId = start - 1;
        this.upTo = upTo;
    }

    public long doc() {
        return docId;
    }

    public boolean next() {
        docId++;
        return docId < upTo;
    }

    public boolean skipTo(long target) {
        docId = (target <= docId) ? docId + 1 : target;
        return docId < upTo;
    }
}

package net.metadata.mdb.callbacks;

public class ThrottledProgressCallback implements ProgressCallback {

    private final ProgressCallback delegate;

    private final long tableCount;

    private final long rowCount;

    private boolean ignoreNextEnd;

    public ThrottledProgressCallback(ProgressCallback delegate, long tableCount, long rowCount) {
        this.delegate = delegate;
        this.tableCount = tableCount;
        this.rowCount = rowCount;
    }

    @Override
    public void startTable(String name, long current, long total) {
        if ((current == total) || (current % tableCount == 0)) {
            delegate.startTable(name, current, total);
            ignoreNextEnd = false;
        } else {
            ignoreNextEnd = true;
        }
    }

    @Override
    public void endTable() {
        if (!ignoreNextEnd) {
            delegate.endTable();
        }
    }

    @Override
    public void startRow(long current, long total) {
        if ((current == total) || (current % rowCount == 0)) {
            delegate.startRow(current, total);
            ignoreNextEnd = false;
        } else {
            ignoreNextEnd = true;
        }
    }

    @Override
    public void endRow() {
        if (!ignoreNextEnd) {
            delegate.endRow();
        }
    }
}

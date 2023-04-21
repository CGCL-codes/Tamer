package streamcruncher.test.func;

import java.util.LinkedList;
import java.util.List;

/**
 * All the Test cases use this Class to hold the results of the Query and then
 * verify the results against expected data.
 */
public class BatchResult {

    protected long timestamp;

    protected final List<Object[]> rows;

    public BatchResult() {
        this.timestamp = System.currentTimeMillis();
        this.rows = new LinkedList<Object[]>();
    }

    public List<Object[]> getRows() {
        return rows;
    }

    public void addRow(Object[] row) {
        rows.add(row);
    }

    public long getTimestamp() {
        return timestamp;
    }
}

package index.query;

import engine.query.Plan;
import engine.query.Scan;
import index.Index;
import index.metadata.IndexInfo;
import storage.record.Schema;
import storage.tx.Transaction;

public class IndexJoinPlan implements Plan {

    private Plan p;

    private IndexInfo ii;

    private String joinfield;

    public IndexJoinPlan(Plan p, IndexInfo ii, String joinfield, Transaction tx) {
        this.p = p;
        this.ii = ii;
        this.joinfield = joinfield;
    }

    public Scan open() {
        Scan s = p.open();
        Index idx = ii.open();
        return new IndexJoinScan(s, idx, joinfield);
    }

    public int blocksAccessed() {
        return p.blocksAccessed() + (p.recordsOutput() * ii.blocksAccessed());
    }

    public int recordsOutput() {
        return p.recordsOutput() * ii.recordsOutput();
    }

    public int distinctValues(String fldname) {
        return p.distinctValues(fldname);
    }

    public Schema schema() {
        return p.schema();
    }
}

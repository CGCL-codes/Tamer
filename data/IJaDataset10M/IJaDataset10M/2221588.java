package index.query;

import compiler.absyn.Constant;
import engine.query.Plan;
import engine.query.Scan;
import index.Index;
import index.metadata.IndexInfo;
import storage.record.Schema;
import storage.tx.Transaction;

public class IndexSelectPlan implements Plan {

    private IndexInfo ii;

    private Constant val;

    public IndexSelectPlan(IndexInfo ii, Constant val, Transaction tx) {
        this.ii = ii;
        this.val = val;
    }

    public Scan open() {
        Index idx = ii.open();
        return new IndexSelectScan(idx, val);
    }

    public int blocksAccessed() {
        return ii.blocksAccessed();
    }

    public int recordsOutput() {
        return ii.recordsOutput();
    }

    public int distinctValues(String fname) {
        throw new RuntimeException("field " + fname + " not found.");
    }

    public Schema schema() {
        return new Schema();
    }
}

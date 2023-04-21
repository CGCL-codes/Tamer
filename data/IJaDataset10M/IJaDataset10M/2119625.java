package index.query;

import compiler.absyn.Constant;
import engine.query.Scan;
import index.Index;
import storage.record.RID;

public class IndexJoinScan implements RidScan {

    private Scan s;

    private Index idx;

    private String joinfield;

    public IndexJoinScan(Scan s, Index idx, String joinfield) {
        this.s = s;
        this.idx = idx;
        this.joinfield = joinfield;
        beforeFirst();
    }

    public void beforeFirst() {
        s.beforeFirst();
        s.next();
        resetIndex();
    }

    public boolean next() {
        while (true) {
            if (idx.next()) return true;
            if (!s.next()) return false;
            resetIndex();
        }
    }

    public void close() {
        s.close();
        idx.close();
    }

    public Constant getVal(String fldname) {
        return s.getVal(fldname);
    }

    public int getInt(String fldname) {
        return s.getInt(fldname);
    }

    public String getString(String fldname) {
        return s.getString(fldname);
    }

    public boolean hasField(String fldname) {
        return s.hasField(fldname);
    }

    public RID getDataRid() {
        return idx.getDataRid();
    }

    private void resetIndex() {
        Constant searchkey = s.getVal(joinfield);
        idx.beforeFirst(searchkey);
    }
}

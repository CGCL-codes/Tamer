package nts.node;

import nts.base.Num;
import nts.base.Dimen;
import nts.base.Glue;
import nts.io.Log;
import nts.io.CntxLog;

public class InsertNode extends MigratingNode {

    protected final Insertion ins;

    public InsertNode(Insertion ins) {
        this.ins = ins;
    }

    public boolean isInsertion() {
        return true;
    }

    public Insertion getInsertion() {
        return ins;
    }

    public void addOn(Log log, CntxLog cntx) {
        ins.addOn(log, cntx);
    }

    public String toString() {
        return "Insert(" + ins + ')';
    }
}

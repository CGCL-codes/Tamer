package com.gorillalogic.dal.common.table;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;

class EnumeratedTable extends ReferenceTable {

    private CommonTable table;

    EnumeratedTable(CommonTable table) {
        this.table = table;
        compute();
    }

    public void apply(Sweep sweep, boolean end) {
        super.apply(sweep, false);
        sweep.fd("table", table);
        sweep.end(end);
    }

    protected String doPath(PathStrategy strategy) {
        return "<<Enumerated Table>>";
    }

    protected CommonTable tableTarget() {
        return table;
    }

    public void eval() throws AccessException {
    }

    private void compute() {
        clearMapForUpdate();
        CommonItr itr = table.commonLoopLock();
        while (itr.next()) {
            long rowId = itr.getRowId();
            map().add(rowId, true);
        }
    }

    protected CommonTable srcForOptimization() {
        return tableTarget();
    }

    protected boolean alwaysRepresentsExactlyOneRow() {
        return rowCount() == 1;
    }

    protected CommonTable doOptimizeCommon(int level) throws AccessException {
        return this;
    }

    protected CommonRow doAddCommonRef(CommonRow row) throws AccessException {
        warnBad();
        return null;
    }

    protected CommonRow doAddCommonRow(CommonTable.InitializationStrategy izy) throws AccessException {
        warnBad();
        return null;
    }

    private void warnBad() throws AccessException {
        String msg = "Cannot add rows to an enumerationg table.";
        throw new StructureException(msg);
    }
}

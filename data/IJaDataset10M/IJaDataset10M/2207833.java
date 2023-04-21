package com.gorillalogic.dal.common.table.store.incore;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.dal.common.table.store.*;

abstract class ScalarColumn extends GridExplicitColumn implements StoredCellTable.StoredCellHdr {

    ScalarColumn(GridTable gridTable, String name, CommonType type) {
        super(gridTable, name, type);
    }

    ScalarColumn(GridTable gridTable, String name, Linker linker) {
        super(gridTable, name, linker);
    }

    ScalarColumn(GridTable gridTable, String name, GridExplicitColumn hdr) {
        super(gridTable, name, hdr);
    }

    public int getMinCard() {
        return 1;
    }

    public final int getMaxCard() {
        return 1;
    }

    public long nthRowId(int owningOrd, int nestedOrd) throws BoundsException {
        return 0;
    }

    public int ordinalPosition(int owningOrd, long rowId) {
        return 0;
    }

    public void addLinkDirectly(int ord, long rowId) {
        throw new InternalException("Can't add links to " + path());
    }

    public long addCellRow(int ord, long rowId) throws Txn.TxnException {
        throw new InternalException("ScalarColumn.addCellRow");
    }

    public void removeCellRow(int ord, long rowId) throws Txn.TxnException {
        throw new InternalException("ScalarColumn.removeCellRow");
    }

    IncoreColumn morphToMandatory(IncoreColumn sc) {
        return this;
    }

    public void writeCell(CommonRow destRow, CommonTable srcTable) throws AccessException {
        doWriteTypedTable(destRow, srcTable);
    }

    abstract static class Optional extends ScalarColumn {

        private boolean[] set;

        Optional(GridTable gridTable, String name, CommonType type, int rowSpace) {
            super(gridTable, name, type);
            set = new boolean[rowSpace];
        }

        final boolean isSet(int index) {
            return true;
        }

        final void set(int index, boolean which) {
            set[index] = which;
        }

        public int rowCount(int ord) {
            return isSet(ord) ? 1 : 0;
        }

        public final void allocateMoreRowSpace(int numberOfAdditionalUnassignedRows) {
            boolean[] temp = new boolean[set.length + numberOfAdditionalUnassignedRows];
            System.arraycopy(set, 0, temp, 0, set.length);
            set = temp;
            doAllocateMoreRowSpace(numberOfAdditionalUnassignedRows);
        }

        public final void compress(int from, int to, int howMany) {
            System.arraycopy(set, from, set, to, howMany);
            doCompress(from, to, howMany);
        }

        public final void copyCellFromTo(int fromOrd, int toOrd) {
            set[toOrd] = set[fromOrd];
            doCopyCellFromTo(fromOrd, toOrd);
        }

        abstract void doAllocateMoreRowSpace(int numberOfAdditionalUnassignedRows);

        abstract void doCompress(int from, int to, int howMany);

        abstract void doCopyCellFromTo(int fromOrd, int toOrd);
    }
}

package com.gorillalogic.dal.common.table.store.incore;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.dal.common.table.store.*;

class LinkVectorColumn extends VectorColumn {

    public String abbrev() {
        return "TV";
    }

    private long[][] data;

    LinkVectorColumn(GridTable gridTable, String name, CommonType type, int rowSpace) {
        super(gridTable, name, type);
        data = new long[rowSpace][];
    }

    LinkVectorColumn(GridTable gridTable, String name, Linker linker, int rowSpace) {
        super(gridTable, name, linker);
        data = new long[rowSpace][];
    }

    LinkVectorColumn(GridTable gridTable, String name, GridExplicitColumn fromHdr, long[] singleRows, int min, int max) {
        super(gridTable, name, fromHdr, min, max);
        data = new long[singleRows.length][];
        for (int i = 0; i < singleRows.length; i++) {
            if (singleRows[i] >= 0) {
                data[i] = new long[1];
                data[i][0] = singleRows[i];
            }
        }
    }

    public void initCell(int ord, CommonExpr expr, CommonRow row) throws AccessException {
        data[ord] = null;
        super.initCell(ord, expr, row);
    }

    public void copyCellFromTo(int fromOrd, int toOrd) {
        if (data[fromOrd] == null) {
            data[toOrd] = null;
        } else {
            int sz = data[fromOrd].length;
            data[toOrd] = new long[sz];
            for (int i = 0; i < sz; i++) {
                data[toOrd][i] = data[fromOrd][i];
            }
        }
    }

    public int rowSpaceCount() {
        return data == null ? 0 : data.length;
    }

    public void allocateMoreRowSpace(int numberOfAdditionalUnassignedRows) {
        long[][] temp = new long[data.length + numberOfAdditionalUnassignedRows][];
        System.arraycopy(data, 0, temp, 0, data.length);
        data = temp;
    }

    public void compress(int from, int to, int howMany) {
        System.arraycopy(data, from, data, to, howMany);
    }

    public int rowCount(int ord) {
        long[] links = data[ord];
        return links == null ? 0 : links.length;
    }

    public long nthRowId(int owningOrd, int nestedOrd) throws BoundsException {
        long[] links = data[owningOrd];
        if (links == null || nestedOrd >= links.length) {
            int rc = rowCount(owningOrd);
            String msg = "Ordinal " + nestedOrd + " beyond row count " + rc + " for " + path();
            throw new BoundsException(msg);
        }
        return links[nestedOrd];
    }

    public int ordinalPosition(int owningOrd, long rowId) {
        long[] links = data[owningOrd];
        int sz = links == null ? 0 : links.length;
        for (int lx = 0; lx < sz; lx++) {
            if (links[lx] == rowId) return lx;
        }
        return -1;
    }

    public long addCellRow(int ord, long rowId) throws Txn.TxnException {
        int cell = 0;
        long[] temp = data[ord];
        if (temp == null) {
            data[ord] = new long[1];
        } else {
            cell = temp.length;
            data[ord] = new long[cell + 1];
            System.arraycopy(temp, 0, data[ord], 0, cell);
        }
        data[ord][cell] = rowId;
        return cell;
    }

    public void removeCellRow(int owningOrd, long rowId) throws Txn.TxnException {
        long[] links = data[owningOrd];
        int sz = links == null ? 0 : links.length;
        for (int lx = 0; lx < sz; lx++) {
            if (links[lx] == rowId) {
                long[] temp = new long[sz - 1];
                System.arraycopy(links, 0, temp, 0, lx);
                System.arraycopy(links, lx + 1, temp, lx, sz - lx - 1);
                data[owningOrd] = temp;
                return;
            }
        }
    }

    public void setStoredTable(int ord, Table value) throws AccessException {
        int rc = value == null ? 0 : value.rowCount();
        if (rc == 0) {
            data[ord] = null;
        } else {
            if (data[ord] == null || data[ord].length != rc) {
                data[ord] = new long[rc];
            }
            Table.Itr itr = value.loopLock();
            while (itr.next()) {
                data[ord][itr.count()] = itr.getRowId();
            }
        }
    }

    IncoreColumn morphToNullable(IncoreColumn sc) {
        return new LinkScalarColumn(getGridTable(), getName(), this, data, 0);
    }

    IncoreColumn morphToMandatory(IncoreColumn sc) {
        return new LinkScalarColumn(getGridTable(), getName(), this, data, 1);
    }

    public boolean isNull(int ord) throws AccessException {
        long[] vector = data[ord];
        if (vector == null) {
            return true;
        } else if (vector.length == 0) {
            return true;
        } else if (vector.length == 1 && vector[0] == -1) {
            return true;
        }
        return false;
    }

    public LinkDerefTable getLinkTable(CommonRow row) {
        return new LinkTable(row);
    }

    public int bequeth(TypeBuilder builder) throws AccessException {
        return builder.addColumn(getName(), getLinker());
    }
}

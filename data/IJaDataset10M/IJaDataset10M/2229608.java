package com.gorillalogic.dal.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.dal.common.expr.GCLOps;

/**
 * <code>BagTable</code> collects value types from multi-card cells
 * from multiple rows. It is the analog to <code>CrossTable</code> for
 * value types.
 *
 * @author <a href="mailto:Brendan@Gosh"></a>
 * @version 1.0
 */
public class BagTable extends CommonTable implements CommonDomain {

    private CommonTable derefThruTable;

    private int derefThruColumnIndex;

    private CommonTable[] ref = null;

    private int rowCount = 0;

    private BagColumn[] columns = null;

    public BagTable(CommonTable derefThruTable, int rx) throws AccessException {
        this.derefThruTable = derefThruTable.optimizeCommon(0);
        this.derefThruColumnIndex = rx;
        CommonType type = derefThruTable.commonColumn(derefThruColumnIndex).commonType();
        final int cc = type.columnCount();
        columns = new BagColumn[cc];
        for (int cx = 0; cx < cc; cx++) {
            columns[cx] = new BagColumn(type.commonColumn(cx), cx);
        }
        recompute();
    }

    public void apply(Sweep sweep, boolean end) {
        super.apply(sweep, false);
        sweep.fd("derefThruTable", derefThruTable);
        sweep.fd("derefThruColumnIndex", derefThruColumnIndex);
        sweep.end(end);
    }

    protected String doPath(PathStrategy strategy) throws AccessException {
        return CrossTable.doPath(strategy, derefThruTable, derefThruColumnIndex);
    }

    protected CommonTable doOptimizeCommon(int level) throws AccessException {
        CommonTable opt = derefThruTable.optimizeCommon(level);
        if (opt == derefThruTable) {
            return this;
        }
        return new BagTable(opt, derefThruColumnIndex);
    }

    public boolean isValueType() {
        return true;
    }

    public CommonDomain commonDomain() {
        return this;
    }

    public void setFrom(CommonTable fromTable) throws AccessException {
        hdr().ambiguousUpdate();
    }

    private class BagColumn extends ProxyExpr {

        private final CommonColumnHdr hdr;

        private final int cx;

        BagColumn(CommonColumnHdr hdr, int cx) {
            this.hdr = hdr;
            this.cx = cx;
        }

        protected CommonExpr exprTarget() {
            return hdr;
        }

        protected CommonExpr exprTarget(CommonScope scope) {
            return scope.commonData().commonColumn(0);
        }

        protected CommonColumnHdr hdrTarget() {
            return hdr;
        }

        protected CommonScope remapScope(CommonScope scope) throws AccessException {
            CommonRow row;
            if (scope instanceof BagRow) {
                row = ((BagRow) scope).unrow();
            } else if (scope instanceof BagItr) {
                row = ((BagItr) scope).unrow();
            } else {
                long rowId = scope.asRowId();
                row = mapUnderlyingRow(BagTable.this, rowId);
            }
            return row;
        }
    }

    private CommonColumnHdr hdr() {
        return derefThruTable.commonColumn(derefThruColumnIndex);
    }

    protected CommonColumnHdr doCommonColumn(int column) throws BoundsException {
        return columns[column];
    }

    public int columnCount() {
        return columns.length;
    }

    public void eval() throws AccessException {
        derefThruTable.eval();
        recompute();
    }

    private void recompute() throws AccessException {
        rowCount = 0;
        int sz = derefThruTable.rowCount();
        if (ref == null || ref.length != sz) {
            ref = new CommonTable[sz];
        }
        int pos = 0;
        CommonItr itr = derefThruTable.commonLoopLock();
        while (itr.next()) {
            CommonTable nextTable = itr.commonTable(derefThruColumnIndex, TableStrategy.rawNullable);
            if (nextTable != null) {
                rowCount += nextTable.rowCount();
                ref[pos++] = nextTable;
            }
        }
    }

    protected int doRowCount() {
        return rowCount;
    }

    public CommonRow commonRowWithNoCheck(long rowId) throws BoundsException {
        return makeRow(this, rowId);
    }

    private CommonRow makeRow(CommonTable overlayTable, long rowId) throws BoundsException {
        CommonRow row = mapUnderlyingRow(overlayTable, rowId);
        return new BagRow(overlayTable, row, rowId);
    }

    private CommonRow mapUnderlyingRow(CommonTable overlayTable, long rowId) throws BoundsException {
        int rx = 0;
        int total = 0;
        while (ref != null && rx < ref.length) {
            int rc = ref[rx].rowCount();
            if (rowId < rc + total) {
                CommonRow under = ref[rx].nthCommonRow((int) (rowId - total));
                return under;
            }
            total += rc;
            rx++;
        }
        throw new BoundsException("RowId " + rowId + " out-of-bounds");
    }

    public CommonItr commonLoopLock() {
        return new BagItr(this);
    }

    private static class BagRow extends CommonRow {

        private CommonTable owner;

        private CommonRow row;

        private long rowId;

        BagRow(CommonTable owner, CommonRow row, long rowId) {
            this.owner = owner;
            this.row = row;
            this.rowId = rowId;
        }

        public CommonTable commonOwner() {
            return owner;
        }

        public long getRowId(boolean toss) throws BoundsException {
            return rowId;
        }

        CommonRow unrow() {
            return row;
        }

        protected void invalidate() throws OperationException {
            row.invalidate();
        }
    }

    private class BagItr extends CommonItr {

        private CommonTable owner;

        private CommonItr itr = null;

        private int srcIndex = -1;

        private int count = 0;

        BagItr(CommonTable owner) {
            this.owner = owner;
        }

        public CommonTable commonOwner() {
            return owner;
        }

        CommonRow unrow() {
            return itr;
        }

        public long getRowId(boolean toss) throws BoundsException {
            return count - 1;
        }

        public boolean next() {
            if (hasNext()) {
                count++;
                return itr.next();
            }
            return false;
        }

        public boolean hasNext() {
            do {
                if (itr != null && itr.hasNext()) {
                    return true;
                }
                if (++srcIndex >= ref.length) {
                    return false;
                }
                CommonTable refTable = ref[srcIndex];
                if (refTable == null) {
                    return false;
                }
                itr = refTable.commonLoopLock();
            } while (true);
        }

        public int count() {
            return count;
        }

        public void restart() {
            srcIndex = -1;
            itr = null;
        }

        public void release() {
            if (itr != null) itr.release();
            restart();
        }

        public CommonRow asCommonRow() throws AccessException {
            return new BagRow(owner, itr.asCommonRow(), getRowId(false));
        }

        protected void adjustPositionRelative(int offset) {
            throw new UnsupportedException("BagTable.adjustPositionRelative");
        }
    }
}

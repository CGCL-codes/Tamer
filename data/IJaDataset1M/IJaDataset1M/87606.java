package com.gorillalogic.dal.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.expr.GCLOps;
import com.gorillalogic.dal.common.utils.IndexMap;

/**
 * <code>ReferenceTable</code> is a common base class for 
 * tables that reference other tables for their values.
 *
 * Subclasses: explicit manipulation of the IndexMap is
 * provided, pay attention to the semantics of each.
 * addMapEntry/deleteMapEntry are also provided as convenience routines.
 *
 * @author bpm
 * @version 1.0
 */
public abstract class ReferenceTable extends ProxyTable {

    private IndexMap map = null;

    private boolean holdRefs = false;

    public void apply(Sweep sweep, boolean end) {
        super.apply(sweep, false);
        sweep.fd("map", map);
        sweep.end(end);
    }

    /**
	 * Overriden from CommonTable
	 *
	 * @param row our row index
	 * @return remapped to underlying table
	 */
    public long nthRowId(int ordinalRowPos) throws BoundsException {
        int rc = rowCount();
        if (ordinalRowPos >= rc) {
            if (rc == 0) {
                throw new BoundsException("Access pos=" + ordinalRowPos + " on empty table");
            }
            BoundsException.checkOrdinalRow(this, ordinalRowPos);
        }
        return map.map(ordinalRowPos);
    }

    public int ordinalPosition(long rowId) throws StructureException {
        return map == null ? -1 : map.posOfFirst(rowId);
    }

    protected int doRowCount() {
        return map == null ? 0 : map.length();
    }

    /**
	 * Proxy.tableTarget() is hidden, but some ops on a reference
	 * table can be passed safely through. Subclasses can redefine
	 * to provide access to that target.
	 *
	 * TBO: generally faster to limit search through this reference table.
	 *
	 * @return a <code>CommonTable</code> value
	 */
    protected CommonTable safeReferenceTarget() {
        return tableTarget();
    }

    public long rowId(int keyIndex, String keyValue, boolean toss) throws AccessException {
        return safeReferenceTarget().rowId(keyIndex, keyValue, toss);
    }

    public CommonTable commonShave() {
        return tableTarget();
    }

    protected void copyReferencesFrom(CommonTable table) {
        copyReferencesFrom(table.commonLoopLock());
    }

    protected void copyReferencesFrom(CommonItr itr) {
        ensureMap();
        map.clear();
        while (itr.next()) {
            map.add(itr.getRowId(), true);
        }
    }

    /**
	 * Retrieve the map -- without a check to ensure
	 * that it exists. This avoids the map check, but
	 * of course is dangerous.
	 *
	 * @return the <code>IndexMap</code> 
	 */
    protected IndexMap map() {
        return map;
    }

    /**
	 * Ensure a map exists and return it.
	 *
	 * @return the <code>IndexMap</code>
	 */
    protected IndexMap ensureMap() {
        if (map == null) {
            map = new IndexMap();
        }
        return map;
    }

    /**
	 * Remove the contents by removing the map.
	 */
    protected void removeAll() {
        map = null;
    }

    /**
	 * Ensure a map exists and in either event ensure that
	 * is empty.
	 */
    protected void clearMapForUpdate() {
        ensureMap();
        map = map.clearForUpdate();
    }

    protected boolean doContains(long rowId) {
        return map.contains(rowId);
    }

    public CommonTableExtender commonExtend(boolean toss) throws OperationException {
        CommonTableExtender tex = tableTarget().commonExtend(false);
        if (tex == null) {
            if (toss) {
                CommonTable.cantCommonExtend(this, toss);
            }
            return null;
        }
        return this;
    }

    protected void ensureRefIsContained(CommonRow row) {
        addMapEntry(row.getRowId());
    }

    protected void doDeleteCommonRef(CommonRow row, boolean deep) throws AccessException {
        if (deep) {
            tableTarget().extend(true).deleteRef(row, deep);
        }
        dropRef(row);
    }

    protected void doDeleteRow(CommonRow row, boolean force) throws AccessException {
        TableExtender extend = tableTarget().extend(true);
        extend.deleteRow(row, force);
        dropRef(row);
    }

    protected synchronized void doDeleteAllRows(boolean force) throws AccessException {
        CommonTable table = commonExtent();
        ResetRow row = new ResetRow();
        int rc = map.length();
        for (int rx = 0; rx < rc; rx++) {
            long rowId = map.map(rx);
            row.reset(table, rowId);
            row.deleteRow(force);
        }
        map.clear();
    }

    protected final void dropRef(CommonRow row) throws AccessException {
        long rowId = row.getRowId();
        if (!map.remove(rowId, false)) {
            throw new BoundsException("Can't unref row " + row + " not in " + path());
        }
    }

    protected void addMapEntry(long rowId) {
        ensureMap().add(rowId, false);
    }

    public void deleteMapEntry(long rowId) {
        if (map != null) {
            map.remove(rowId, false);
        }
    }

    /**
	 * Produces the most efficient path that directly captures
	 * the rows in this table. This only applies to strategies
	 * that are persistent or registered; otherwise it returns
	 * null.
	 *
	 * See PathTest-ReferencePath for tests.
	 *
	 * @param strategy a <code>PathStrategy</code> value
	 * @return a path or null if strategy does not meet conditions above.
	 * @exception AccessException if an error occurs
	 */
    protected final String pathByReference(PathStrategy strategy) throws AccessException {
        CommonType tt = typeTarget().commonExtent();
        final boolean registered = strategy.registered();
        final boolean persistent = strategy.persistent();
        final int ridColumnIndex = tt.getRowIdColumnIndex();
        final int kx = getPrimaryKeyIndex();
        final int[] key = kx >= 0 ? keyIndex(kx) : null;
        if (persistent) {
            if (key == null) {
                throw new StructureException("Can't make persistent path on table with no key " + tt.path());
            }
        } else if (registered) {
            if (ridColumnIndex < 0) {
                throw new StructureException("Can't make registered path on table with no rowId " + tt.path());
            }
        } else {
            return null;
        }
        String rez = tt.path(strategy);
        if (rowCount() == 0) {
            rez = "(" + rez + "){}";
        } else if (rowCount() == 1) {
            CommonRow row = asCommonRow();
            if (persistent) {
                String v1 = row.getString(key[0]);
                if (key.length == 1) {
                    rez += "@" + v1;
                } else {
                    String v2 = row.getString(key[1]);
                    rez += '[';
                    for (int kc = 0; kc < key.length; kc++) {
                        if (kc > 0) rez += " and ";
                        CommonColumnHdr hdr = tt.commonColumn(key[kc]);
                        String value = hdr.commonType().quoteForGCL(row.getString(key[kc]), true);
                        rez += hdr.getName() + "=" + value;
                    }
                    rez += ']';
                }
            } else {
                rez += "@@" + asRowId();
            }
        } else {
            if (persistent) {
                if (key.length == 1) {
                    rez += "[" + tt.column(key[0]).getName() + " in {";
                    CommonItr itr = commonLoopLock();
                    while (itr.next()) {
                        if (itr.count() > 0) rez += ',';
                        rez += '"' + itr.getString(key[0]) + '"';
                    }
                    rez += "}]";
                } else {
                    rez += "[";
                    CommonItr itr = commonLoopLock();
                    while (itr.next()) {
                        if (itr.count() > 0) rez += " or ";
                        rez += '(';
                        for (int kc = 0; kc < key.length; kc++) {
                            if (kc > 0) rez += " and ";
                            CommonColumnHdr hdr = tt.commonColumn(key[kc]);
                            String value = hdr.commonType().quoteForGCL(itr.getString(key[kc]), true);
                            rez += hdr.getName() + "=" + value;
                        }
                        rez += ')';
                    }
                    rez += "]";
                }
            } else {
                CommonColumnHdr hdr = tt.commonColumn(ridColumnIndex);
                rez += "[" + hdr.path(strategy) + " in {";
                CommonItr itr = commonLoopLock();
                while (itr.next()) {
                    if (itr.count() > 0) rez += ',';
                    rez += itr.asRowId();
                }
                rez += "}]";
            }
        }
        return rez;
    }

    public abstract static class Frozen extends ReferenceTable {

        public CommonTableExtender commonExtend(boolean toss) throws OperationException {
            return CommonTable.cantCommonExtend(this, toss);
        }
    }
}

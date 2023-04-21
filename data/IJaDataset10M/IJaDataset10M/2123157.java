package br.com.dreamsource.mobile.jmesql;

import br.com.dreamsource.mobile.jmesql.exceptions.SQLException;
import java.util.Vector;

class Table {

    private String sName;

    private Vector vColumn;

    private Vector vIndex;

    private int iVisibleColumns;

    private int iColumnCount;

    private int iPrimaryKey;

    private boolean bCached;

    private Databases dDatabase;

    private Log lLog;

    private int iIndexCount;

    private int iIdentityColumn;

    private int iIdentityId;

    private Vector vConstraint;

    private int iConstraintCount;

    Cache cCache;

    Vector vTrigs[];

    private int RowCount;

    Table(Databases db, boolean log, String name, boolean cached) {
        dDatabase = db;
        lLog = log ? db.getLog() : null;
        if (cached) {
            cCache = lLog.cCache;
            bCached = true;
        }
        sName = name;
        iPrimaryKey = -1;
        iIdentityColumn = -1;
        vColumn = new Vector();
        vIndex = new Vector();
        vConstraint = new Vector();
        vTrigs = new Vector[TriggerDef.numTrigs()];
        for (int vi = 0; vi < TriggerDef.numTrigs(); vi++) {
            vTrigs[vi] = new Vector();
        }
    }

    void addConstraint(Constraint c) {
        vConstraint.addElement(c);
        iConstraintCount++;
    }

    Vector getConstraints() {
        return vConstraint;
    }

    void addColumn(String name, int type) throws SQLException {
        addColumn(name, type, true, false);
    }

    void addColumn(Column c) throws SQLException {
        addColumn(c.sName, c.iType, c.isNullable(), c.isIdentity());
    }

    void addColumn(String name, int type, boolean nullable, boolean identity) throws SQLException {
        if (identity) {
            Trace.check(type == Column.INTEGER, Trace.WRONG_DATA_TYPE, name);
            Trace.check(iIdentityColumn == -1, Trace.SECOND_PRIMARY_KEY, name);
            iIdentityColumn = iColumnCount;
        }
        Trace.assert1(iPrimaryKey == -1, "Table.addColumn");
        vColumn.addElement(new Column(name, nullable, type, identity));
        iColumnCount++;
    }

    void addColumns(Result result) throws SQLException {
        for (int i = 0; i < result.getColumnCount(); i++) {
            addColumn(result.sLabel[i], result.iType[i], true, false);
        }
    }

    String getName() {
        return sName;
    }

    int getInternalColumnCount() {
        return iColumnCount;
    }

    Table moveDefinition(String withoutindex) throws SQLException {
        Table tn = new Table(dDatabase, true, getName(), isCached());
        for (int i = 0; i < getColumnCount(); i++) {
            tn.addColumn(getColumn(i));
        }
        if (iVisibleColumns < iColumnCount) {
            tn.createPrimaryKey();
        } else {
            tn.createPrimaryKey(getPrimaryIndex().getColumns()[0]);
        }
        Index idx = null;
        while (true) {
            idx = getNextIndex(idx);
            if (idx == null) {
                break;
            }
            if (withoutindex != null && idx.getName().equals(withoutindex)) {
                continue;
            }
            if (idx == getPrimaryIndex()) {
                continue;
            }
            tn.createIndex(idx);
        }
        for (int i = 0; i < iConstraintCount; i++) {
            Constraint c = (Constraint) vConstraint.elementAt(i);
            c.replaceTable(this, tn);
        }
        tn.vConstraint = vConstraint;
        return tn;
    }

    int getColumnCount() {
        return iVisibleColumns;
    }

    int getIndexCount() {
        return iIndexCount;
    }

    int getIdentityColumn() {
        return iIdentityColumn;
    }

    int getColumnNr(String c) throws SQLException {
        int i = searchColumn(c);
        if (i == -1) {
            throw Trace.error(Trace.COLUMN_NOT_FOUND, c);
        }
        return i;
    }

    int searchColumn(String c) {
        for (int i = 0; i < iColumnCount; i++) {
            if (c.equals(((Column) vColumn.elementAt(i)).sName)) {
                return i;
            }
        }
        return -1;
    }

    String getColumnName(int i) {
        return getColumn(i).sName;
    }

    int getColumnType(int i) {
        return getColumn(i).iType;
    }

    boolean getColumnIsNullable(int i) {
        return getColumn(i).isNullable();
    }

    Index getPrimaryIndex() throws SQLException {
        if (iPrimaryKey == -1) {
            return null;
        }
        return getIndex(0);
    }

    Index getIndexForColumn(int column) throws SQLException {
        for (int i = 0; i < iIndexCount; i++) {
            Index h = getIndex(i);
            if (h.getColumns()[0] == column) {
                return h;
            }
        }
        return null;
    }

    Index getIndexForColumns(int col[]) throws SQLException {
        for (int i = 0; i < iIndexCount; i++) {
            Index h = getIndex(i);
            int icol[] = h.getColumns();
            int j = 0;
            for (; j < col.length; j++) {
                if (j >= icol.length) {
                    break;
                }
                if (icol[j] != col[j]) {
                    break;
                }
            }
            if (j == col.length) {
                return h;
            }
        }
        return null;
    }

    String getIndexRoots() throws SQLException {
        Trace.assert1(bCached, "Table.getIndexRootData");
        String s = "";
        for (int i = 0; i < iIndexCount; i++) {
            Node f = getIndex(i).getRoot();
            if (f != null) {
                s = s + f.getKey() + " ";
            } else {
                s = s + "-1 ";
            }
        }
        s += iIdentityId;
        return s;
    }

    void setIndexRoots(String s) throws SQLException {
        Trace.check(bCached, Trace.TABLE_NOT_FOUND);
        int j = 0;
        for (int i = 0; i < iIndexCount; i++) {
            int n = s.indexOf(' ', j);
            int p = Integer.parseInt(s.substring(j, n));
            if (p != -1) {
                Row r = cCache.getRow(p, this);
                Node f = r.getNode(i);
                getIndex(i).setRoot(f);
            }
            j = n + 1;
        }
        iIdentityId = Integer.parseInt(s.substring(j));
    }

    Index getNextIndex(Index index) {
        int i = 0;
        if (index != null) {
            for (; i < iIndexCount && getIndex(i) != index; i++) ;
            i++;
        }
        if (i < iIndexCount) {
            return getIndex(i);
        }
        return null;
    }

    int getType(int i) {
        return getColumn(i).iType;
    }

    void createPrimaryKey(int column) throws SQLException {
        Trace.assert1(iPrimaryKey == -1, "Table.createPrimaryKey(column)");
        iVisibleColumns = iColumnCount;
        iPrimaryKey = column;
        int col[] = { column };
        createIndex(col, "SYSTEM_PK", true);
    }

    void createPrimaryKey() throws SQLException {
        Trace.assert1(iPrimaryKey == -1, "Table.createPrimaryKey");
        addColumn("SYSTEM_ID", Column.INTEGER, true, true);
        createPrimaryKey(iColumnCount - 1);
        iVisibleColumns = iColumnCount - 1;
    }

    void createIndex(Index index) throws SQLException {
        createIndex(index.getColumns(), index.getName(), index.isUnique());
    }

    void createIndex(int column[], String name, boolean unique) throws SQLException {
        Trace.assert1(iPrimaryKey != -1, "createIndex");
        for (int i = 0; i < iIndexCount; i++) {
            Index index = getIndex(i);
            if (name.equals(index.getName())) {
                throw Trace.error(Trace.INDEX_ALREADY_EXISTS);
            }
        }
        int s = column.length;
        int col[] = new int[unique ? s : s + 1];
        int type[] = new int[unique ? s : s + 1];
        for (int j = 0; j < s; j++) {
            col[j] = column[j];
            type[j] = getColumn(col[j]).iType;
        }
        if (!unique) {
            col[s] = iPrimaryKey;
            type[s] = getColumn(iPrimaryKey).iType;
        }
        Index index = new Index(name, col, type, unique);
        if (iIndexCount != 0) {
            Trace.assert1(isEmpty(), "createIndex");
        }
        vIndex.addElement(index);
        iIndexCount++;
    }

    void checkDropIndex(String index) throws SQLException {
        for (int i = 0; i < iIndexCount; i++) {
            if (index.equals(getIndex(i).getName())) {
                Trace.check(i != 0, Trace.DROP_PRIMARY_KEY);
                return;
            }
        }
        throw Trace.error(Trace.INDEX_NOT_FOUND, index);
    }

    boolean isEmpty() {
        return getIndex(0).getRoot() == null;
    }

    Object[] getNewRow() {
        return new Object[iColumnCount];
    }

    void moveData(Table from) throws SQLException {
        Index index = from.getPrimaryIndex();
        Node n = index.first();
        while (n != null) {
            if (Trace.STOP) {
                Trace.stop();
            }
            Object o[] = n.getData();
            insertNoCheck(o, null);
            n = index.next(n);
        }
        index = getPrimaryIndex();
        n = index.first();
        while (n != null) {
            if (Trace.STOP) {
                Trace.stop();
            }
            Object o[] = n.getData();
            from.deleteNoCheck(o, null);
            n = index.next(n);
        }
    }

    void checkUpdate(int col[], Result deleted, Result inserted) throws SQLException {
        if (dDatabase.isReferentialIntegrity()) {
            for (int i = 0; i < iConstraintCount; i++) {
                Constraint v = (Constraint) vConstraint.elementAt(i);
                v.checkUpdate(col, deleted, inserted);
            }
        }
    }

    void insert(Result result, Channel c) throws SQLException {
        Record r = result.rRoot;
        int len = result.getColumnCount();
        while (r != null) {
            Object row[] = getNewRow();
            RowCount++;
            if (RowCount == 100) {
                throw new SQLException("N�mero maximo de registros atingidos");
            }
            for (int i = 0; i < len; i++) {
                row[i] = r.data[i];
            }
            insert(row, c);
            r = r.next;
        }
    }

    void insert(Object row[], Channel c) throws SQLException {
        fireAll(TriggerDef.INSERT_BEFORE, row);
        if (dDatabase.isReferentialIntegrity()) {
            for (int i = 0; i < iConstraintCount; i++) {
                ((Constraint) vConstraint.elementAt(i)).checkInsert(row);
            }
        }
        insertNoCheck(row, c);
        fireAll(TriggerDef.INSERT_AFTER, row);
    }

    void insertNoCheck(Object row[], Channel c) throws SQLException {
        insertNoCheck(row, c, true);
    }

    void insertNoCheck(Object row[], Channel c, boolean log) throws SQLException {
        if (iIdentityColumn != -1) {
            Integer id = (Integer) row[iIdentityColumn];
            if (id == null) {
                if (c != null) {
                    c.setLastIdentity(iIdentityId);
                }
                if (iIdentityId == 0) {
                    iIdentityId++;
                }
                row[iIdentityColumn] = new Integer(iIdentityId++);
            } else {
                int i = id.intValue();
                if (iIdentityId <= i) {
                    if (c != null) {
                        c.setLastIdentity(i);
                    }
                    iIdentityId = i + 1;
                }
            }
        }
        for (int i = 0; i < iColumnCount; i++) {
            if (row[i] == null && !getColumn(i).isNullable()) {
                throw Trace.error(Trace.TRY_TO_INSERT_NULL);
            }
        }
        int i = 0;
        try {
            Row r = new Row(this, row);
            for (; i < iIndexCount; i++) {
                Node n = r.getNode(i);
                getIndex(i).insert(n);
            }
        } catch (SQLException e) {
            for (--i; i >= 0; i--) {
                getIndex(i).delete(row, i == 0);
            }
            throw e;
        }
        if (c != null) {
            c.addTransactionInsert(this, row);
        }
        if (lLog != null) {
            if (log) {
                lLog.write(c, getInsertStatement(row));
            }
        }
    }

    void fireAll(int trigVecIndx, Object row[]) {
        if (!dDatabase.isReferentialIntegrity()) {
            return;
        }
        Vector trigVec = vTrigs[trigVecIndx];
        int trCount = trigVec.size();
        for (int i = 0; i < trCount; i++) {
            TriggerDef td = (TriggerDef) trigVec.elementAt(i);
            td.push(row);
        }
    }

    void fireAll(int trigVecIndx) {
        Object row[] = new Object[1];
        row[0] = new String("Statement-level");
        fireAll(trigVecIndx, row);
    }

    void addTrigger(TriggerDef trigDef) {
        if (Trace.TRACE) {
            Trace.trace("Trigger added " + String.valueOf(trigDef.vectorIndx));
        }
        vTrigs[trigDef.vectorIndx].addElement(trigDef);
    }

    void delete(Object row[], Channel c) throws SQLException {
        fireAll(TriggerDef.DELETE_BEFORE_ROW, row);
        if (dDatabase.isReferentialIntegrity()) {
            for (int i = 0; i < iConstraintCount; i++) {
                ((Constraint) vConstraint.elementAt(i)).checkDelete(row);
            }
        }
        deleteNoCheck(row, c);
        fireAll(TriggerDef.DELETE_AFTER_ROW, row);
    }

    void updateLine(String s, String newS) throws SQLException {
        lLog.updateLine(s, newS);
    }

    void deleteNoCheck(Object row[], Channel c) throws SQLException {
        deleteNoCheck(row, c, true);
    }

    void deleteNoCheck(Object row[], Channel c, boolean log) throws SQLException {
        for (int i = 1; i < iIndexCount; i++) {
            getIndex(i).delete(row, false);
        }
        getIndex(0).delete(row, true);
        if (c != null) {
            c.addTransactionDelete(this, row);
        }
        if (lLog != null) {
            if (log) {
                lLog.write(c, getDeleteStatement(row));
            }
        }
    }

    String getInsertStatement(Object row[]) throws SQLException {
        StringBuffer a = new StringBuffer("INSERT INTO ");
        a.append(getName());
        a.append(" VALUES(");
        for (int i = 0; i < iVisibleColumns; i++) {
            a.append(Column.createString(row[i], getColumn(i).iType));
            a.append(',');
        }
        a.setCharAt(a.length() - 1, ')');
        return a.toString();
    }

    boolean isCached() {
        return bCached;
    }

    Index getIndex(String s) {
        for (int i = 0; i < iIndexCount; i++) {
            Index h = getIndex(i);
            if (s.equals(h.getName())) {
                return h;
            }
        }
        return null;
    }

    Column getColumn(int i) {
        return (Column) vColumn.elementAt(i);
    }

    private Index getIndex(int i) {
        return (Index) vIndex.elementAt(i);
    }

    private String getDeleteStatement(Object row[]) throws SQLException {
        StringBuffer a = new StringBuffer("DELETE FROM ");
        a.append(sName);
        a.append(" WHERE ");
        if (iVisibleColumns < iColumnCount) {
            for (int i = 0; i < iVisibleColumns; i++) {
                a.append(getColumn(i).sName);
                a.append('=');
                a.append(Column.createString(row[i], getColumn(i).iType));
                if (i < iVisibleColumns - 1) {
                    a.append(" AND ");
                }
            }
        } else {
            a.append(getColumn(iPrimaryKey).sName);
            a.append("=");
            a.append(Column.createString(row[iPrimaryKey], getColumn(iPrimaryKey).iType));
        }
        return a.toString();
    }
}

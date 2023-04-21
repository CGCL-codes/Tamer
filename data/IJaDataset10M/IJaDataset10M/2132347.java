package org.maverickdbms.database.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.maverickdbms.basic.Factory;
import org.maverickdbms.basic.ConstantString;
import org.maverickdbms.basic.MaverickException;
import org.maverickdbms.basic.Key;
import org.maverickdbms.basic.SortField;
import org.maverickdbms.basic.MaverickString;

public class List implements org.maverickdbms.basic.List {

    private Factory factory;

    private Key key;

    private ResultSet rs;

    private RecordList records = new RecordList();

    private int recordIndex = 0;

    private int[] sortTypes;

    private boolean isAfterLast = false;

    List(Factory factory, Key key, ResultSet rs) {
        this.factory = factory;
        this.key = key;
        this.rs = rs;
        SortField sort = key.getSortFields();
        SortField tmp = sort;
        int sc = 0;
        while (tmp != null) {
            sc++;
            tmp = tmp.next;
        }
        sortTypes = new int[sc];
        for (int i = 0; i < sc; i++) {
            sortTypes[i] = sort.type;
            sort = sort.next;
        }
    }

    public void close() {
        isAfterLast = true;
    }

    public void eval() throws MaverickException {
        try {
            if (key.hasRecords()) {
                File file = (File) key.getFile();
                org.maverickdbms.basic.List list = key.getRecords();
                Record record = new Record(factory, sortTypes);
                MaverickString val = factory.getString();
                MaverickString subval = factory.getString();
                MaverickString var = factory.getString();
                MaverickString status = factory.getString();
                ConstantString rtnval = list.READNEXT(record.id, val, subval);
                while (rtnval.equals(ConstantString.RETURN_SUCCESS)) {
                    if (file.READ(var, record.id, status).equals(ConstantString.RETURN_SUCCESS)) {
                        records.add(record);
                        record = new Record(factory, sortTypes);
                    } else {
                    }
                    rtnval = list.READNEXT(record.id, val, subval);
                }
            } else {
                while (rs.next()) {
                    Record record = new Record(factory, sortTypes);
                    record.id.set(rs.getString(1));
                    records.add(record);
                }
            }
        } catch (SQLException e) {
            throw new MaverickException(0, e);
        }
    }

    public boolean isAfterLast() {
        return isAfterLast;
    }

    public long length() {
        return (key.isImmediate()) ? records.getLength() : -1;
    }

    public ConstantString READLIST(MaverickString result) throws MaverickException {
        throw new MaverickException(0, "Sorry READLIST is not yet implemented");
    }

    public ConstantString READNEXT(MaverickString id, MaverickString val, MaverickString subval) throws MaverickException {
        try {
            if (recordIndex < records.getLength()) {
                id.set(records.get(recordIndex++).id);
                return ConstantString.RETURN_SUCCESS;
            } else if (!key.isImmediate() && !key.hasRecords()) {
                boolean success = rs.next();
                if (success) {
                    id.set(rs.getString(1));
                    return ConstantString.RETURN_SUCCESS;
                }
                isAfterLast = true;
            }
            return ConstantString.RETURN_ELSE;
        } catch (SQLException e) {
            throw new MaverickException(0, e);
        }
    }
}

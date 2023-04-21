package org.maverickdbms.database.pgsql;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.maverickdbms.basic.Factory;
import org.maverickdbms.basic.Condition;
import org.maverickdbms.basic.ConstantString;
import org.maverickdbms.basic.MaverickException;
import org.maverickdbms.basic.Key;
import org.maverickdbms.basic.Program;
import org.maverickdbms.basic.SortField;
import org.maverickdbms.basic.MaverickString;

public class List implements org.maverickdbms.basic.List {

    private Program program;

    private Factory factory;

    private File file;

    private Key key;

    private ResultSet resultSet;

    private RecordList records = new RecordList();

    private Condition cond;

    private int recordIndex = 0;

    private int[] sortTypes;

    List(Program program, Factory factory, File file, Key key, ResultSet resultSet) {
        this.program = program;
        this.factory = factory;
        this.file = file;
        this.key = key;
        this.resultSet = resultSet;
        cond = key.getCondition();
        if (cond == null) {
            cond = factory.getCondition(Condition.NUMBER, ConstantString.ONE);
        }
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
        file.addProgram(program);
    }

    public void close() throws MaverickException {
        if (key.isImmediate() || key.hasRecords()) {
            recordIndex = records.getLength();
        } else if (resultSet != null) {
            try {
                resultSet.close();
                resultSet = null;
                file.CLOSE(program, factory.getString());
            } catch (SQLException sqle) {
                throw new MaverickException(0, sqle);
            }
        }
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
                File file = (File) key.getFile();
                MaverickString var = factory.getString();
                MaverickString status = factory.getString();
                while (resultSet != null && resultSet.next()) {
                    Record record = new Record(factory, sortTypes);
                    record.id.set(resultSet.getString(1));
                    if (file.READ(var, record.id, status).equals(ConstantString.RETURN_SUCCESS)) {
                        if (!cond.eval(record.id, var).equals(ConstantString.ZERO)) {
                            records.add(record);
                        }
                    }
                }
            }
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
                file.CLOSE(program, factory.getString());
            }
        } catch (SQLException e) {
            throw new MaverickException(0, e);
        }
    }

    public boolean isAfterLast() {
        if (key.isImmediate() || key.hasRecords()) {
            return (recordIndex >= records.getLength());
        }
        return (resultSet == null);
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
                if (resultSet != null) {
                    if (resultSet.next()) {
                        id.set(resultSet.getString(1));
                        return ConstantString.RETURN_SUCCESS;
                    }
                    resultSet.close();
                    resultSet = null;
                    file.CLOSE(program, factory.getString());
                }
            }
            return ConstantString.RETURN_ELSE;
        } catch (SQLException e) {
            throw new MaverickException(0, e);
        }
    }
}

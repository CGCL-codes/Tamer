package org.carp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.carp.engine.MetaData;

public class DataSet {

    private List<List<Object>> row = new ArrayList<List<Object>>();

    private int dataCount;

    private MetaData cqmd;

    private ResultSet rs;

    private int index = -1;

    public DataSet(MetaData cqmd, ResultSet rs) throws Exception {
        try {
            this.cqmd = cqmd;
            this.rs = rs;
            while (rs.next()) {
                processResultSet();
            }
            rs.close();
            dataCount = row.size() - 1;
        } finally {
            rs.close();
        }
    }

    private void processResultSet() throws Exception {
        List<Object> data = new ArrayList<Object>();
        for (int i = 1, count = this.cqmd.getColumnCount(); i <= count; ++i) {
            this.cqmd.getAssemble(i - 1).setValue(rs, data, i);
        }
        row.add(data);
    }

    public boolean next() {
        if (index < dataCount) {
            ++index;
            return true;
        }
        return false;
    }

    public Object getData(String name) {
        return row.get(index).get(cqmd.getColumns().indexOf(name.toUpperCase()));
    }

    public List<List<Object>> getData() {
        return row;
    }

    public List<String> getTitle() {
        return cqmd.getColumns();
    }

    public List<Object> getRowData(int index) {
        return row.get(index);
    }

    public List<Class<?>> getColumnType() {
        return this.cqmd.getColumnJavaType();
    }

    public int count() {
        return dataCount + 1;
    }
}

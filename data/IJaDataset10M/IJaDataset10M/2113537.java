package jsqlalchemy.datatypes;

import jsqlalchemy.MetaData;
import error.JSQLException;

public class BoolCol extends Column {

    public BoolCol() {
        size = 1;
        type = "INT";
    }

    public boolean hasSize() {
        return false;
    }

    public String getDataForQuery() throws JSQLException {
        if (value == null) return super.getDataForQuery();
        if ((Boolean) value) return "1"; else return "0";
    }

    public Object getFromDatabase(MetaData metadata, String tableName, String colName) throws JSQLException {
        try {
            Integer val = (Integer) metadata.getSession().getConnection().res.getInt(tableName + "." + colName);
            value = (val == 1);
            return value;
        } catch (Exception e) {
            throw new JSQLException("Column not found", colName, e);
        }
    }
}

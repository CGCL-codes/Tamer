package jsqlalchemy.datatypes;

import java.util.Calendar;
import jsqlalchemy.MetaData;
import error.JSQLException;

public class DateTimeCol extends Column {

    public DateTimeCol() {
        type = "LONG";
    }

    public String getDataForQuery() {
        if (value != null) {
            Calendar v = (Calendar) value;
            return Long.toString(v.getTimeInMillis());
        } else {
            return "'0'";
        }
    }

    public Object getFromDatabase(MetaData metadata, String tableName, String colName) throws JSQLException {
        try {
            Long date = (Long) metadata.getSession().getConnection().res.getLong(tableName + "." + colName);
            Calendar cal = Calendar.getInstance();
            if (date != null) cal.setTimeInMillis(date);
            this.value = cal;
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new JSQLException("Column not found", colName, e);
        }
    }

    public boolean hasSize() {
        return false;
    }
}

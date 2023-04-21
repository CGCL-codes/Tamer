package org.nakedobjects.nos.store.sql.jdbc;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.NakedValue;
import org.nakedobjects.noa.reflect.NakedObjectField;
import org.nakedobjects.noa.reflect.ValueAssociation;
import org.nakedobjects.nof.reflect.java.value.TimeStampAdapter;
import org.nakedobjects.nos.store.sql.Results;
import org.nakedobjects.nos.store.sql.SqlObjectStoreException;
import org.nakedobjects.nos.store.sql.ValueMapper;

public class JdbcTimestampMapper implements ValueMapper {

    public String valueAsDBString(final NakedValue value) throws SqlObjectStoreException {
        String ts = new String(value.asEncodedString());
        if (ts.equals("NULL")) {
            return ts;
        }
        String dbts = ts.substring(0, 4) + "-" + ts.substring(4, 6) + "-" + ts.substring(6, 8) + " " + ts.substring(8, 10) + ":" + ts.substring(10, 12) + ":" + ts.substring(12, 14) + "." + ts.substring(14, 17);
        return "'" + dbts + "'";
    }

    public void setFromDBColumn(final String columnName, final NakedObjectField field, final NakedObject object, final Results rs) throws SqlObjectStoreException {
        String val = rs.getString(columnName);
        if (val != null) {
            int length = val.length();
            val = val.substring(0, 4) + val.substring(5, 7) + val.substring(8, 10) + val.substring(11, 13) + val.substring(14, 16) + val.substring(17, 19) + val.substring(20, length);
            if (length < 21) {
                val = val + "000";
            } else if (length < 22) {
                val = val + "00";
            } else if (length < 23) {
                val = val + "0";
            }
        }
        val = val == null ? "NULL" : val;
        NakedValue adaptedValue = new TimeStampAdapter();
        adaptedValue.restoreFromEncodedString(val);
        ((ValueAssociation) field).initValue(object, adaptedValue);
    }

    public String columnType() {
        return "DATETIME";
    }
}

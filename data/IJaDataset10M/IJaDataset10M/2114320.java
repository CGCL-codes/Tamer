package org.nakedobjects.persistence.sql.jdbc;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.reflect.NakedObjectField;
import org.nakedobjects.object.reflect.OneToOneAssociation;
import org.nakedobjects.persistence.sql.Results;
import org.nakedobjects.persistence.sql.SqlObjectStoreException;
import org.nakedobjects.persistence.sql.ValueMapper;

public class JdbcDateTimeMapper implements ValueMapper {

    public String valueAsDBString(NakedValue value) throws SqlObjectStoreException {
        String ts = new String(value.asEncodedString());
        if (ts.equals("NULL")) {
            return ts;
        }
        String dbts = ts.substring(0, 4) + "-" + ts.substring(4, 6) + "-" + ts.substring(6, 8) + " " + ts.substring(8, 10) + ":" + ts.substring(10, 12);
        return "'" + dbts + "'";
    }

    public void setFromDBColumn(String columnName, NakedObjectField field, NakedObject object, Results rs) throws SqlObjectStoreException {
        String val = rs.getString(columnName);
        val = val.substring(0, 4) + val.substring(5, 7) + val.substring(8, 10) + val.substring(11, 13) + val.substring(14, 16) + val.substring(17, 19);
        val = val == null ? "NULL" : val;
        object.initValue((OneToOneAssociation) field, val);
    }

    public String columnType() {
        return "DATETIME";
    }
}

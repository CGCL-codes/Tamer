package in.co.codedoc.db;

import in.co.codedoc.cg.annotations.DBType;
import in.co.codedoc.cg.annotations.IsADBOutputColumnMap;
import in.co.codedoc.encrypt.HashValue;
import in.co.codedoc.sql.DBOutputColumnMap;
import in.co.codedoc.sql.SQLStatement;
import in.co.codedoc.sql.mapping.ObjectOutputColumnMap;
import java.sql.ResultSet;
import java.sql.SQLException;

@IsADBOutputColumnMap(javaType = HashValue.class, dbType = DBType.CHAR)
public class CharHashValueOutputColumnMap extends DBOutputColumnMap implements ObjectOutputColumnMap {

    public static DBOutputColumnMap GetDBOutputColumnMap() {
        return new CharHashValueOutputColumnMap();
    }

    @Override
    public void ReadColumn(int index, ResultSet rs) throws SQLException {
        String temp = rs.getString(index);
        if (Logger.logger.isDebugEnabled()) {
            if (index > 1) {
                SQLStatement.GetDebugStringBuffer().append(",");
            }
        }
        if (rs.wasNull()) {
            if (Logger.logger.isDebugEnabled()) {
                SQLStatement.GetDebugStringBuffer().append("<NULL>");
            }
            SetWasNull();
        } else {
            v = new HashValue().SetEncodedHash(temp);
            if (Logger.logger.isDebugEnabled()) {
                SQLStatement.GetDebugStringBuffer().append(v);
            }
        }
    }

    public HashValue GetValue() {
        return v;
    }

    private HashValue v;
}

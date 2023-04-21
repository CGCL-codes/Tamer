package tsqldeveloper.db.providers;

import sqldeveloper.db.DBException;
import sqldeveloper.db.DBManager;
import sqldeveloper.db.objects.PrimaryKey;
import sqldeveloper.db.providers.DBObjectsProvider;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Database object Primary Key
 */
public class PrimaryKeysProvider extends DBObjectsProvider<PrimaryKey> {

    public List<PrimaryKey> getByPattern(String pattern, String... params) throws DBException {
        List<PrimaryKey> values = new ArrayList<PrimaryKey>();
        try {
            DatabaseMetaData data = DBManager.getConnection().getMetaData();
            ResultSet rs = data.getPrimaryKeys(params[0], null, params[1]);
            try {
                String lastAddedName = "";
                String keyName;
                while (rs.next()) {
                    keyName = rs.getString("PK_NAME");
                    if ((keyName != null) && (!keyName.equals(lastAddedName))) {
                        values.add(new PrimaryKey(keyName));
                        lastAddedName = keyName;
                    }
                }
            } finally {
                DBManager.closeResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return values;
    }
}

package uk.co.pointofcare.echobase.io;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * @author RCHALLEN
 *
 */
public class SqlIO {

    static Logger log = Logger.getLogger(SqlIO.class);

    public static String dumpToString(ResultSet rs) throws SQLException {
        StringBuilder out = new StringBuilder();
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();
        for (int i = 1; i <= numberOfColumns; i++) {
            out.append(rsMetaData.getColumnName(i) + "\t");
        }
        out.append("\n");
        int count = 0;
        while (rs.next()) {
            for (int i = 1; i <= numberOfColumns; i++) {
                Object output = rs.getObject(i);
                if (output == null) out.append("NULL" + "\t"); else out.append(output.toString() + "\t");
            }
            out.append("\n");
            count++;
        }
        rs.close();
        return out.toString();
    }
}

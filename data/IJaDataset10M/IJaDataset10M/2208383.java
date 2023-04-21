package net.sourceforge.jdbcutils.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 
 * @author <a href="mailto:stripodi@users.sourceforge.net">Simone Tripodi</a>
 * @version $Id: ResultSet2CSV.java,v 1.1 2006/08/10 09:48:07 stripodi Exp $
 */
public final class ResultSet2CSV extends OutputStreamHandler {

    /**
	 * 
	 */
    private static final char COMMA = ';';

    /**
	 * 
	 */
    private char separator;

    /**
	 * 
	 *
	 */
    public ResultSet2CSV() {
        this.separator = COMMA;
    }

    /**
	 * 
	 *
	 */
    public ResultSet2CSV(char separator) {
        this.separator = separator;
    }

    /**
	 * 
	 */
    public Object handleRS(ResultSet rs) throws SQLException {
        ResultSetMetaData rsMeta = rs.getMetaData();
        int cols = rsMeta.getColumnCount();
        while (rs.next()) {
            try {
                for (int i = 1; i <= cols; i++) {
                    this.outputStreamPtr.write(rs.getBytes(i));
                    if (i < cols) {
                        this.outputStreamPtr.write((int) separator);
                    }
                }
                this.outputStreamPtr.write((int) '\n');
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

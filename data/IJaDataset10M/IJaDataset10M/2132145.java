package org.makumba.db.sql;

import java.sql.SQLException;
import java.util.Properties;

/** the database adapter for PostgreSQL */
public class QedDatabase extends org.makumba.db.sql.Database {

    /** simply calls super */
    public QedDatabase(Properties p) {
        super(p);
    }

    /** Postgres column names are case-insensitive */
    protected String getFieldName(String s) {
        return super.getFieldName(s).toUpperCase();
    }

    /** the postgres jdbc driver does not return sql states...
   * we just let every state pass, but print the exception */
    protected void checkState(SQLException e, String state) {
        System.out.println(e);
    }

    protected String getJdbcUrl(Properties p) {
        String qedUrl = "jdbc:";
        String qedEng = p.getProperty("#sqlEngine");
        qedUrl += qedEng + ":";
        String local = getEngineProperty(qedEng + ".localJDBC");
        if (local == null || !local.equals("true")) qedUrl += "//" + p.getProperty("#host") + "/";
        return qedUrl + p.getProperty("#database");
    }
}

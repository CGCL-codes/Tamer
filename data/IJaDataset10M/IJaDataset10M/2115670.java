package ch.rgw.IO;

import java.sql.*;
import java.util.Iterator;
import ch.rgw.tools.ExHandler;
import ch.rgw.tools.JdbcLink;
import ch.rgw.tools.JdbcLink.Stm;

/**
 * settings-IMplementation, die eine SQL-Datenbank zur Speicherung verwendet.
 * In der jetzigen Version wird nur eine flat table �hnlich wie bei cfgSettings
 * verwendet. Mehrere Anwendungen k�nnen dieselbe Datenbank verwenden, wenn
 * sie unterschiedliche Tabellennamen verwenden.
 */
@SuppressWarnings("serial")
public class SqlSettings extends Settings {

    public static final String Version() {
        return "1.1.0";
    }

    volatile JdbcLink j;

    volatile String tbl;

    public SqlSettings(JdbcLink j, String tablename) {
        this.j = j;
        tbl = tablename;
        undo();
    }

    protected void flush_absolute() {
        Iterator it = iterator();
        Stm stm = j.getStatement();
        while (it.hasNext()) {
            String a = (String) it.next();
            String v = get(a, null);
            String sql = "SELECT wert FROM " + tbl + " WHERE param=" + JdbcLink.wrap(a);
            if (stm.queryString(sql) != null) {
                if (v == null) {
                    sql = "DELETE from " + tbl + " WHERE param=" + JdbcLink.wrap(a);
                } else {
                    sql = "UPDATE " + tbl + " SET wert=" + JdbcLink.wrap(v) + " WHERE param=" + JdbcLink.wrap(a);
                }
            } else {
                if (v == null) {
                    continue;
                }
                sql = "INSERT INTO " + tbl + " (param,wert) VALUES (" + JdbcLink.wrap(a) + "," + JdbcLink.wrap(v) + ")";
            }
            stm.exec(sql);
        }
        j.releaseStatement(stm);
    }

    public void undo() {
        ResultSet r;
        Stm stm = j.getStatement();
        try {
            r = stm.query("SELECT * from " + tbl);
            while ((r != null) && r.next()) {
                String parm = r.getString("param");
                String val = r.getString("wert");
                set(parm, val);
            }
            cleaned();
        } catch (Exception ex) {
            ExHandler.handle(ex);
        } finally {
            j.releaseStatement(stm);
        }
    }
}

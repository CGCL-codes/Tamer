package org.sulweb.infustore;

import org.sulweb.infustore.transdaemon.DaemonsMultiplex;
import java.util.*;
import java.sql.*;
import java.util.logging.*;
import org.sulweb.infumon.common.BindingsLog;
import org.sulweb.infumon.common.Infusions;
import org.sulweb.infumon.common.Samples;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class Eraser {

    private Timer t;

    private TimerTask tt;

    protected final Connection conn;

    private long nMillis;

    private static final long ONE_DAY_MILLIS = 24L * 60L * 60L * 1000L;

    private boolean propagateSetNDays;

    public Eraser(Connection conn, int nDays) {
        this.conn = conn;
        setNDays(nDays);
        t = new Timer();
        tt = new TimerTask() {

            public void run() {
                erase();
            }
        };
        t.scheduleAtFixedRate(tt, 10000, ONE_DAY_MILLIS);
    }

    private void erase() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        try {
            eraseOldData();
        } catch (SQLException sqle) {
            Logger l = Logger.getLogger(getClass().getName());
            l.log(Level.WARNING, "Vecchi dati non ripuliti", sqle);
        }
    }

    protected java.util.Date getLimit() {
        java.util.Date now = new java.util.Date();
        java.util.Date min = new java.util.Date(now.getTime() - nMillis);
        return min;
    }

    public final void setNDays(int nDays) {
        nMillis = ((long) nDays) * ONE_DAY_MILLIS;
        propagateSetNDays = true;
    }

    private void managePendingSetNDays() {
        if (propagateSetNDays) {
            Main.getInstance().getSamplesMigrator().historyLengthChanged(getNDays());
            propagateSetNDays = false;
        }
    }

    public final int getNDays() {
        return (int) (nMillis / ONE_DAY_MILLIS);
    }

    protected void eraseOldData() throws SQLException {
        java.util.Date min = getLimit();
        eraseFromTable(conn, Infusions.name, "End", min);
        eraseFromTable(conn, Samples.name, "DateRec", min);
        eraseFromTable(conn, BindingsLog.name, "DateRec", min);
        managePendingSetNDays();
    }

    protected static void eraseFromTable(Connection conn, String tabName, String fieldName, java.util.Date min) throws SQLException {
        String sql = "DELETE FROM " + tabName + " WHERE " + fieldName + " < ? AND " + fieldName + " IS NOT NULL";
        synchronized (conn) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(min.getTime()));
            ps.executeUpdate();
            ps.close();
            conn.commit();
        }
    }
}

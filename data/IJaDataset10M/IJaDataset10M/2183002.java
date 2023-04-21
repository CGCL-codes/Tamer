package org.jugile.util;

import java.util.List;
import org.apache.log4j.Logger;
import org.jugile.util.DBConnection;
import org.jugile.util.DBPool;
import org.jugile.util.Jugile;
import org.jugile.util.Props;

public class HiLo extends Jugile {

    static Logger log = Logger.getLogger(HiLo.class);

    private static long nextid = 1;

    private static long idHi = 0;

    private static long idLo = 0;

    private static long idIncrement = 100;

    public static synchronized long nextid() {
        if (!hasdb()) {
            return nextid++;
        }
        if (idHi == 0 || idLo >= idHi) {
            idHi = getNextIdHiFromDb("global");
            if (idHi <= idIncrement) idHi = getNextIdHiFromDb("global");
            idLo = idHi - idIncrement;
        }
        return idLo++;
    }

    /**
	 * This is for setting the nextid after data import for testing purposes
	 * when hasdb == false.
	 * @param nid next id to give.
	 */
    public static void setNextid(long nid) {
        nextid = nid;
    }

    public static long getNextIdHiFromDb(String obj) {
        long nid = 0;
        DBPool pool = DBPool.getPool();
        DBConnection c = pool.getConnection();
        try {
            c.writeTx();
            c.prepare("select nextid from idpool where obj=?");
            c.param(obj);
            List<List> res = c.select();
            nid = (Integer) res.get(0).get(0);
            c.prepare("update idpool set nextid=? where obj=?");
            c.param(nid + idIncrement);
            c.param(obj);
            c.execute();
            c.commit();
        } catch (Exception e) {
            try {
                c.rollback();
            } catch (Exception e2) {
            }
            fail(e);
        } finally {
            try {
                c.free();
            } catch (Exception e) {
                log.fatal("could not free connection", e);
            }
        }
        return nid;
    }

    private static Boolean hasdb = null;

    public static boolean hasdb() {
        if (hasdb == null) {
            String incr = Props.get("jugile.hilo.increment");
            idIncrement = parseLongSafe(incr);
            if (idIncrement > 0) hasdb = true; else hasdb = false;
        }
        return hasdb;
    }

    public static void setHasdb(boolean v) {
        hasdb = v;
    }

    public static void resetHasdb() {
        hasdb = null;
    }
}

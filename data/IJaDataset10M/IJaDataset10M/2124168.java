package org.epoline.jsf.jdms;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.epoline.jsf.jdms.dl.JDmsRequest;

/**
 * The TSManager manages a set TSConMagers.
 * <br>It uses an internal Hashable to maintain the open database
 * connections;
 */
public final class TSManager {

    private Logger logger = null;

    private TSConManager conpool;

    TSManager(TSConManager conmgr) {
        logger = Logger.getLogger(getClass());
        logger.info("+TSManager(" + conmgr + ")");
        conpool = conmgr;
        logger.info("-TSManager(" + conmgr + ")");
    }

    public JDmsRequest handleRequest(TSUser user, JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            info = "handleRequest(" + user + "," + req + ") ";
            logger.debug("+" + info);
        }
        TSProgram program = null;
        if ((req.program != null) && (req.program.trim().length() != 0)) {
            try {
                program = TSProgManager.getProgram(req.program);
            } catch (Exception e) {
                logger.warn("handleRequest(" + user + "," + req + ")  " + info + req.program + "->" + e.getMessage());
                return errProgram(req, e);
            }
        }
        JDmsRequest rc = null;
        if (req.Luw_Token == JDmsRequest.LUW_NEW) {
            if (req.Luw_Mode == JDmsRequest.MODE_NULL) {
                rc = doNewNull(req, program);
            } else if (req.Luw_Mode == JDmsRequest.MODE_EXTEND) {
                rc = doExtNew(req, program, user);
            } else {
                logger.warn(" handleRequest(" + user + "," + req + ") " + info + " LUW_NEW : invalide MODE");
                rc = new JDmsRequest();
                rc.TSType = JDmsRequest.RESP;
                rc.TSCode = JDmsRequest.RC_ERRMODE;
                rc.TSMessage = JDmsRequest.MSG_ERRMODE;
            }
        } else if (req.Luw_Token > JDmsRequest.LUW_NEW) {
            rc = doExtUow(req, program, user);
        } else {
            logger.warn("handleRequest(" + user + "," + req + ") " + info + " invalid token : " + req.Luw_Token);
            rc = new JDmsRequest();
            rc.TSType = JDmsRequest.RESP;
            rc.TSCode = JDmsRequest.RC_ERRLUW;
            rc.TSMessage = JDmsRequest.MSG_ERRLUW;
        }
        if (logger.isDebugEnabled()) {
            logEnd(info, l0);
        }
        return rc;
    }

    private JDmsRequest doExtNew(JDmsRequest req, TSProgram program, TSUser user) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            info = "doExtNew(" + req + "," + program + "," + user + ")";
            logStart(info);
        }
        try {
            if (program == null) {
                return errProgram(req, null);
            }
            if (user == null) {
                return errUser(req);
            }
            int i = JDmsRequest.LUW_NEW + 1;
            Connection con = conpool.getConnection();
            user.setConnection(con);
            JDmsRequest rc = program.exec(con, req);
            rc.Luw_Token = i;
            rc.TSMessage = "luw open, ID=" + i;
            return rc;
        } finally {
            if (logger.isDebugEnabled()) {
                logEnd(info, l0);
            }
        }
    }

    private JDmsRequest doExtUow(JDmsRequest req, TSProgram program, TSUser user) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            info = "doExtUow(" + req + "," + program + "," + user + ")";
            logStart(info);
        }
        try {
            if (user == null) {
                return errUser(req);
            }
            JDmsRequest rc = new JDmsRequest();
            rc.TSType = JDmsRequest.RESP;
            Connection con = user.getConnection();
            if (con == null) {
                logger.warn(" doExtUow(" + req + "," + program + "," + user + ")" + req.program + ":" + user + " unable to find connection.");
                rc.Luw_Token = JDmsRequest.LUW_NEW;
                rc.TSCode = JDmsRequest.RC_ERRLUWNULL;
                rc.TSMessage = JDmsRequest.MSG_ERRLUWNULL;
                return rc;
            }
            rc.Luw_Token = JDmsRequest.LUW_NEW;
            if ((req.Luw_Mode == JDmsRequest.MODE_NULL) || (req.Luw_Mode == JDmsRequest.MODE_COMMIT)) {
                if (logger.isDebugEnabled()) {
                    logger.debug(" " + info + " Commit requested");
                }
                rc.SQLCode = 0;
                rc.SQLMessage = "Oke";
                rc.TSMessage = "Commit oke";
                try {
                    con.commit();
                } catch (SQLException e) {
                    logger.warn(" doExtUow(" + req + "," + program + "," + user + ") " + e.getMessage());
                    rc.SQLCode = e.getErrorCode();
                    rc.SQLMessage = e.getSQLState();
                    rc.TSMessage = "Commit failed.";
                } finally {
                    conpool.freeConnection(con);
                    user.setConnection(null);
                    user.cleanup();
                }
            } else if (req.Luw_Mode == JDmsRequest.MODE_EXTEND) {
                rc = program.exec(con, req);
                rc.Luw_Token = req.Luw_Token;
            } else if ((req.Luw_Mode == JDmsRequest.MODE_BACKOUT) || (req.Luw_Mode == JDmsRequest.MODE_CANCEL)) {
                if (logger.isDebugEnabled()) {
                    logger.debug(" " + info + " Backout requested");
                }
                rc.SQLCode = 0;
                rc.SQLMessage = "Oke";
                rc.TSMessage = "Backout oke";
                try {
                    con.rollback();
                } catch (SQLException e) {
                    logger.warn(" doExtUow(" + req + "," + program + "," + user + ") " + e.getMessage());
                    rc.SQLCode = e.getErrorCode();
                    rc.SQLMessage = e.getSQLState();
                    rc.TSMessage = "Rollback failed.";
                } finally {
                    conpool.freeConnection(con);
                    user.setConnection(null);
                    user.cleanup();
                }
            } else {
                logger.warn(" doExtUow(" + req + "," + program + "," + user + ")-Invalid mode :" + JDmsRequest.MSG_ERRMODE);
                rc.Luw_Token = JDmsRequest.LUW_NEW;
                rc.TSCode = JDmsRequest.RC_ERRMODE;
                rc.TSMessage = JDmsRequest.MSG_ERRMODE;
                conpool.freeConnection(con);
                user.setConnection(null);
                user.cleanup();
            }
            return rc;
        } finally {
            if (logger.isDebugEnabled()) {
                logEnd(info, l0);
            }
        }
    }

    private JDmsRequest doNewNull(JDmsRequest req, TSProgram program) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            info = "doNewNull(" + req + "," + program + ") ";
            logger.debug("+" + info);
        }
        JDmsRequest rc = null;
        Connection con = null;
        try {
            if (program == null) {
                return errProgram(req, null);
            }
            con = conpool.getConnection();
            con.setAutoCommit(true);
            rc = program.exec(con, req);
            con.setAutoCommit(false);
        } catch (SQLException e) {
            logger.warn(" doNewNull(" + req + "," + program + ") " + e.getMessage(), e);
            if (rc == null) {
                rc = new JDmsRequest();
                rc.TSType = JDmsRequest.RESP;
                rc.SQLCode = e.getErrorCode();
                rc.SQLMessage = e.getSQLState();
                rc.TSMessage = "AutoCommit failed.";
            }
        } finally {
            if (con != null) {
                conpool.freeConnection(con);
            }
            if (logger.isDebugEnabled()) {
                logEnd(info, l0);
            }
        }
        return rc;
    }

    private JDmsRequest errProgram(JDmsRequest req) {
        logger.warn("errProgram(" + req + ")");
        JDmsRequest rc = new JDmsRequest();
        rc.TSType = JDmsRequest.RESP;
        rc.TSCode = JDmsRequest.RC_ERRPROG;
        rc.TSMessage = JDmsRequest.MSG_ERRPROG + req.program;
        rc.Luw_Mode = req.Luw_Mode;
        rc.Luw_Token = req.Luw_Token;
        return rc;
    }

    private JDmsRequest errProgram(JDmsRequest req, Exception e) {
        logger.error("errProgram(" + req + ")", e);
        JDmsRequest rc = new JDmsRequest();
        rc.TSType = JDmsRequest.RESP;
        rc.TSCode = JDmsRequest.RC_ERRPROG;
        rc.TSMessage = JDmsRequest.MSG_ERRPROG + req.program + e.getMessage();
        rc.Luw_Mode = req.Luw_Mode;
        rc.Luw_Token = req.Luw_Token;
        return rc;
    }

    private JDmsRequest errUser(JDmsRequest req) {
        logger.warn("errUser(" + req + ")");
        JDmsRequest rc = new JDmsRequest();
        rc.TSType = JDmsRequest.RESP;
        rc.TSCode = JDmsRequest.RC_ERRUSER;
        rc.TSMessage = JDmsRequest.MSG_ERRUSER;
        rc.Luw_Mode = req.Luw_Mode;
        rc.Luw_Token = req.Luw_Token;
        return rc;
    }

    private void logStart(String info) {
        logger.debug("+" + info);
    }

    private void logEnd(String info, long startime) {
        logger.debug("-" + info + "[" + (System.currentTimeMillis() - startime) + " ms]");
    }
}

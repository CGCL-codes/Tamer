package org.epoline.jsf.jdms;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.epoline.jsf.jdms.dl.JDmsRequest;

/**
 * This class represent the connection to an active user.
 * A connection can only be to one datastore.
 */
public class TSHandler extends Thread {

    /** theLogger is used for normal Logging */
    private Logger theLogger = null;

    /** theJdmsID is used for differentiating several servers */
    private static String theJdmsID = null;

    /** Last assigned connection number */
    private static long theConNumber = 0;

    private static final Object conNumberLock = new Object();

    /** Last assigned connection number */
    private static long theThreads = 0;

    private static final Object threadsLock = new Object();

    private Socket wsConnection;

    private ObjectOutputStream out;

    private ObjectInputStream in;

    private boolean bOke;

    /**
	* Creates a TSUser for a conection.
	* @param long theConNumberber : connection number
	* @param Socket s : the socket.
	*/
    public TSHandler(Socket s) {
        theLogger = Logger.getLogger(getClass());
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "TSHandler(" + s + ") ";
            theLogger.debug("+" + info);
        }
        wsConnection = s;
        bOke = false;
        if (s != null) {
            try {
                in = new ObjectInputStream(s.getInputStream());
                out = new ObjectOutputStream(s.getOutputStream());
                bOke = true;
            } catch (IOException e) {
                theLogger.error(" " + info, e);
            }
        }
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
        }
    }

    public static void setJdmsID(String id) {
        if (id != null) {
            theJdmsID = id;
        }
    }

    public static String getJdmsID() {
        return theJdmsID;
    }

    public void run() {
        final String info = "run()";
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("+" + info);
        }
        if (!bOke) {
            theLogger.error("-" + info + " : Unable to handle request");
            doCleanup();
            return;
        }
        JDmsRequest req = null;
        try {
            incActiveUsers();
            req = (JDmsRequest) in.readObject();
            if (req != null) {
                handle(req);
            } else {
                theLogger.error(" " + info + " : Error while reading, null request");
            }
        } catch (Exception e) {
            req = null;
            theLogger.error(" " + info + " : Error while reading ", e);
        } catch (Throwable e) {
            req = null;
            theLogger.error(" " + info + " : Error while reading ", e);
        } finally {
            decActiveUsers();
            doCleanup();
            if (theLogger.isDebugEnabled()) {
                theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
            }
        }
    }

    static long getActiveUsers() {
        long rc = 0;
        synchronized (threadsLock) {
            rc = theThreads;
        }
        return rc;
    }

    static long getConNumber() {
        long rc = 0;
        synchronized (conNumberLock) {
            rc = theConNumber;
        }
        return rc;
    }

    static long newConnection() {
        long rc = 0L;
        synchronized (conNumberLock) {
            theConNumber++;
            rc = theConNumber;
        }
        return rc;
    }

    private JDmsRequest _handleDetails(JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "_handleDetails(" + req + ")";
            theLogger.debug("+" + info);
        }
        JDmsRequest resp = new JDmsRequest();
        resp.TSType = JDmsRequest.RESP;
        resp.TSCode = JDmsRequest.RC_OKE;
        resp.TSMessage = TSConstants.INFO_NAME + "-" + TSConstants.INFO_VERSION + " " + theJdmsID;
        resp.TSInfo = theConNumber;
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
        }
        return resp;
    }

    private JDmsRequest _handleError(JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "_handleError(" + req + ")";
            theLogger.debug("+" + info);
        }
        JDmsRequest resp = new JDmsRequest();
        resp.TSType = JDmsRequest.RESP;
        resp.TSCode = JDmsRequest.RC_ERRMODE;
        resp.TSMessage = JDmsRequest.MSG_ERRMODE;
        resp.TSInfo = 0L;
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
        }
        return resp;
    }

    private JDmsRequest _handleHits(JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "_handleHits(" + req + ")";
            theLogger.debug("+" + info);
        }
        JDmsRequest resp = new JDmsRequest();
        resp.TSType = JDmsRequest.RESP;
        resp.TSCode = JDmsRequest.RC_OKE;
        resp.TSMessage = JDmsRequest.MSG_OKE;
        resp.TSInfo = theConNumber;
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
        }
        return resp;
    }

    private JDmsRequest _handleLoad(JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "_handleLoad(" + req + ")";
            theLogger.debug("+" + info);
        }
        JDmsRequest resp = new JDmsRequest();
        resp.TSType = JDmsRequest.RESP;
        resp.TSCode = JDmsRequest.RC_OKE;
        resp.TSMessage = JDmsRequest.MSG_OKE;
        resp.TSInfo = (5L * TSUser.getUsers() + getActiveUsers());
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
        }
        return resp;
    }

    private JDmsRequest _handleRequest(JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "_handleRequest(" + req + ")";
            theLogger.debug("+" + info);
        }
        JDmsRequest resp = null;
        if (req.Luw_Token == JDmsRequest.LUW_NEW) {
            if (req.Luw_Mode == JDmsRequest.MODE_NULL) {
                resp = handleNoUow(req);
            } else if (req.Luw_Mode == JDmsRequest.MODE_EXTEND) {
                resp = handleUowNew(req);
            } else {
                info = "_handleRequest(" + req + ")";
                theLogger.warn(" " + info + ":LUW_NEW: invalide MODE");
                resp = new JDmsRequest();
                resp.TSType = JDmsRequest.RESP;
                resp.TSCode = JDmsRequest.RC_ERRMODE;
                resp.TSMessage = JDmsRequest.MSG_ERRMODE;
            }
        } else if (req.Luw_Token > JDmsRequest.LUW_NEW) {
            resp = handleUowExt(req);
        } else {
            info = "_handleRequest(" + req + ")";
            theLogger.warn(" " + info + ": Invalid LUW-token");
            resp = new JDmsRequest();
            resp.TSCode = JDmsRequest.RC_ERR;
            resp.TSMessage = JDmsRequest.MSG_ERRTYPE;
            resp.TSType = JDmsRequest.RESP;
        }
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
        }
        return resp;
    }

    private JDmsRequest _handleStatus(JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "_handleStatus(" + req + ")";
            theLogger.debug("+" + info);
        }
        JDmsRequest resp = new JDmsRequest();
        resp.TSType = JDmsRequest.RESP;
        resp.TSCode = JDmsRequest.RC_OKE;
        resp.TSMessage = JDmsRequest.MSG_OKE;
        resp.TSInfo = theConNumber;
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
        }
        return resp;
    }

    /**
	 * Cleans up the resources for this user.
	 */
    private void doCleanup() {
        final String info = "doCleanup()";
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("+" + info);
        }
        if (wsConnection != null) {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    theLogger.error(info, e);
                    out = null;
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    theLogger.error(info, e);
                    in = null;
                }
            }
            try {
                wsConnection.close();
            } catch (IOException e) {
                theLogger.error(info, e);
                wsConnection = null;
            }
        } else {
            out = null;
            in = null;
        }
        bOke = false;
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
        }
    }

    private static void incActiveUsers() {
        synchronized (threadsLock) {
            theThreads++;
        }
    }

    private static void decActiveUsers() {
        synchronized (threadsLock) {
            theThreads--;
        }
    }

    private void handle(JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "handle(" + req + ")";
            theLogger.debug("+" + info);
        }
        JDmsRequest resp = null;
        switch(req.TSType) {
            case JDmsRequest.LINK:
                resp = _handleRequest(req);
                break;
            case JDmsRequest.JSF_DETAILS:
                resp = _handleDetails(req);
                break;
            case JDmsRequest.JSF_LOAD:
                resp = _handleLoad(req);
                break;
            case JDmsRequest.JSF_HITS:
                resp = _handleHits(req);
                break;
            case JDmsRequest.JSF_STATUS:
                resp = _handleStatus(req);
                break;
            default:
                resp = _handleError(req);
                break;
        }
        write(resp);
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
        }
    }

    private JDmsRequest errNoConmgr(JDmsRequest req) {
        theLogger.warn(" " + req.server + "connect:failed");
        JDmsRequest resp = new JDmsRequest();
        resp.TSCode = JDmsRequest.RC_ERRCONNECT;
        resp.TSMessage = JDmsRequest.MSG_ERRCONNECT + req.server;
        resp.TSType = JDmsRequest.RESP;
        return resp;
    }

    private JDmsRequest errUser(JDmsRequest req) {
        theLogger.warn(" " + req.userid + " : no user found in UOW handling");
        JDmsRequest rc = new JDmsRequest();
        rc.TSType = JDmsRequest.RESP;
        rc.TSCode = JDmsRequest.RC_ERRUSER;
        rc.TSMessage = JDmsRequest.MSG_ERRUSER;
        rc.Luw_Mode = req.Luw_Mode;
        rc.Luw_Token = req.Luw_Token;
        return rc;
    }

    private JDmsRequest handleNoUow(JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "handleNoUow(" + req + ")";
            theLogger.debug("+" + info);
        }
        JDmsRequest resp = null;
        String sDatastore = req.server;
        try {
            TSManager conmgr = TSConManager.getInstance(sDatastore).getHandler();
            if (conmgr == null) {
                resp = errNoConmgr(req);
            } else {
                resp = conmgr.handleRequest(null, req);
            }
        } catch (TSException e) {
            theLogger.warn(" " + info + e.getMessage());
        } finally {
            if (theLogger.isDebugEnabled()) {
                theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
            }
        }
        return resp;
    }

    private JDmsRequest handleUowExt(JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "handleUowExt(" + req + ")";
            theLogger.debug("+" + info);
        }
        JDmsRequest resp = null;
        TSUser user = TSUser.getUser(req.userid, req.connection);
        if (user == null) {
            resp = errUser(req);
        } else {
            TSManager conmgr = null;
            try {
                conmgr = TSConManager.getInstance(req.server).getHandler();
            } catch (TSException e) {
                conmgr = null;
            }
            if (conmgr == null) {
                resp = errNoConmgr(req);
            } else {
                resp = conmgr.handleRequest(user, req);
            }
        }
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
        }
        return resp;
    }

    private JDmsRequest handleUowNew(JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "handleUowNew(" + req + ")";
            theLogger.debug("+" + info);
        }
        TSManager conmgr = null;
        try {
            conmgr = TSConManager.getInstance(req.server).getHandler();
            if (conmgr == null) {
                return errNoConmgr(req);
            } else {
                long lNumber = newConnection();
                String conid = theJdmsID + lNumber;
                TSUser user = new TSUser(req.userid, conid, req.server);
                JDmsRequest resp = conmgr.handleRequest(user, req);
                resp.connection = conid;
                return resp;
            }
        } catch (TSException e) {
            return errNoConmgr(req);
        } finally {
            if (theLogger.isDebugEnabled()) {
                theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
            }
        }
    }

    private void write(JDmsRequest req) {
        String info = null;
        long l0 = System.currentTimeMillis();
        if (theLogger.isDebugEnabled()) {
            info = "write(" + req + ")";
            theLogger.debug("+" + info);
        }
        if ((out == null) && bOke) {
            try {
                out = new ObjectOutputStream(wsConnection.getOutputStream());
            } catch (Exception e) {
                info = "write(" + req + ")";
                theLogger.error(" " + info, e);
                out = null;
                bOke = false;
            }
        }
        if (bOke) {
            try {
                if (req != null) {
                    if (req.sCommarea != null) {
                        if (req.sCommarea.length() > 2255000) {
                            try {
                                req.bCommarea = req.sCommarea.getBytes("UTF-8");
                                req.sCommarea = null;
                            } catch (UnsupportedEncodingException e) {
                                info = "write(" + req + ")";
                                theLogger.warn(" " + info + "UTF-8 Problem: ->" + e);
                            }
                        }
                    }
                    out.writeObject(req);
                }
                out.flush();
            } catch (Exception e) {
                info = "write(" + req + ")";
                theLogger.error(" " + info, e);
            }
        }
        if (theLogger.isDebugEnabled()) {
            theLogger.debug("-" + info + (System.currentTimeMillis() - l0) + " ms");
        }
    }
}

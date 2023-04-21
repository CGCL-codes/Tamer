package org.epo.jbpa.clients.admin;

import java.rmi.*;
import org.apache.log4j.Logger;
import org.epo.jbpa.dl.JBpaException;
import org.epo.jbpa.rmi.JBpaAdminInterface;
import org.epo.jbpa.rmi.JBpaMonitoring;
import com.infotel.util.RMIAdminConsole;
import org.epo.jphoenix.util.OrderProperties;
import java.util.Properties;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * <B>Administration Console for JBPA</B>
 * <BR>Use System.out to communicate with the Administrator
 * @author INFOTEL CONSEIL
 */
public class JBpaAdminConsole extends RMIAdminConsole {

    private static final String SERVER_URL = "JBpaServer";

    private static String URL_IN_USE = null;

    private static final String[] SHUTDOWN = { "shutdown" };

    private static final String[] STOP = { "stop" };

    private static final String[] START = { "start" };

    private static final String[] INFOSRV = { "infoSrv" };

    private static final String[] LSREQ = { "lsReq" };

    private static final String[] REQSTATUS = { "reqStatus" };

    private static final String[] LSPERMAPP = { "lsPermApp" };

    private static final String[] LSTEMPAPP = { "lsTempApp" };

    private static final String[] KILL = { "kill" };

    private static final String[][][] RESERVED_WORDS = { { SHUTDOWN, { "Stops the jBPA server (JVM down, unbind in RMI registry)" } }, { LSREQ, { "Gives back the list of requests of the jBPA with their status", "/p : page per page list" }, null, { "/p" } }, { START, { "Starts connected JBpa server" } }, { REQSTATUS, { "Gives back the status on a given request" }, { "requestNumber" } }, { STOP, { "Stops connected JBpa server" } }, { INFOSRV, { "Gives back information on the jBPA server (server instance name, URL, status)" } }, { LSPERMAPP, { "Gives back the list of permanent appdocs stored in the jBPA", "/p : page per page list" }, null, { "/p" } }, { LSTEMPAPP, { "Gives back the list of current temporary appdocs stored in the jBPA", "/p : page per page list" }, null, { "/p" } }, { KILL, { "Kills the JVM of current jBPA server" }, { "securityKey" } } };

    /**
 * JBpaAdminConsole Standard Constructor
 * Creation date: (02/01/2002 14:41:15)
 */
    public JBpaAdminConsole() {
        super();
        setServerPrompt("JBpaServer");
        addReservedWords(RESERVED_WORDS);
    }

    /**
 * <B>Close the RMI communication with remote server</B><BR>
 */
    protected void closeComm() {
        logout();
        setServer(null);
    }

    /**
 * <B>Standard accessor</B><BR>
 * Creation date: (02/01/2002 14:54:26)
 * @return org.epo.jbpa.rmi.JBpaAdminInterface
 */
    private JBpaAdminInterface getJBpaServer() {
        return (JBpaAdminInterface) getRMIServer();
    }

    public void INFOSRV() throws RemoteException {
        if (getJBpaServer() == null) {
        } else {
        }
    }

    /**
 * <B>Kill JVM for current jBPA server</B><BR>
 * Creation date: (09/04/01 09:58:31)
 * @param theCommand String[] 
 * @exception RemoteException
 */
    public void KILL(String[] theCommand) throws RemoteException {
        if (getJBpaServer() == null) {
        } else {
            if (theCommand.length < 2) printHelp(theCommand[0], KILL); else {
                String mySrvInstanceId = getJBpaServer().getSrvInstanceId();
                try {
                    getJBpaServer().kill(theCommand[1]);
                } catch (UnmarshalException e) {
                } catch (Exception e) {
                }
                setServer(null);
            }
        }
    }

    /**
 * <B>Connect the remote server</B><BR>
 * Creation date: (02/01/2002 14:51:48)
 * @param theUser String
 * @param thePwd String
 */
    protected boolean login(String theUser, String thePwd) {
        if (getServer() == null) {
            trace(1, "Use OPEN %URL% before");
            return false;
        } else try {
            logout();
            setSessionUID(getJBpaServer().openJBpaSession(theUser, thePwd));
            setServerId(getJBpaServer().getSrvInstanceId());
            consoleInfo();
            return true;
        } catch (RemoteException re) {
            dispatchRemoteException(re);
            closeComm();
            return false;
        } catch (JBpaException e) {
            RemoteException re = new RemoteException("JBpaException[" + e.getReturnCode() + "] : " + e.getMessage());
            dispatchRemoteException(re);
            closeComm();
            return false;
        }
    }

    /**
 * Log out method
 * Creation date: (07/01/2002 14:38:19)
 */
    protected void logout() {
        try {
            if (getServer() != null) {
                if (getSessionUID() != null) {
                    try {
                        getJBpaServer().closeJBpaSession(getSessionUID());
                    } catch (JBpaException e) {
                        throw new RemoteException("JBpaException[" + e.getReturnCode() + "] : " + e.getMessage());
                    }
                }
            }
        } catch (RemoteException re) {
        } finally {
            setSessionUID(null);
        }
    }

    /**
 * The list of permanent appdocs stored in the jBPA is given back.
 * Creation date: (07/01/2002 13:54:55)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void LSPERMAPP(String[] theCommand) throws RemoteException {
        Properties myProperties = null;
        boolean myDataReturned = false;
        boolean myPagePerPageFlag = false;
        boolean myBreakout = false;
        if (getJBpaServer() == null) {
        } else {
            if (theCommand.length == 1) {
                myProperties = getJBpaServer().getPermAppdocList(new Properties());
                myDataReturned = true;
            } else {
                if (!theCommand[1].equalsIgnoreCase("/p")) {
                    printHelp(theCommand[0], LSPERMAPP);
                } else {
                    myPagePerPageFlag = true;
                    myProperties = getJBpaServer().getPermAppdocList(new Properties());
                    myDataReturned = true;
                }
            }
        }
        if (myDataReturned) {
            LinkedList my_APPDOC_ID_LinkedList = OrderProperties.getStartsAndEndsWith(myProperties, JBpaMonitoring.APPDOC_ID, "value");
            Iterator APPDOC_ID_iter = my_APPDOC_ID_LinkedList.iterator();
            StringBuffer appDocId = new StringBuffer("LIST OF PERMANENTS APP DOC ID");
            appDocId.setLength(30);
            String toPrint = null;
            int i = 0;
            char[] charbuff = new char[4];
            BufferedReader myBr = new BufferedReader(new InputStreamReader(System.in));
            while (APPDOC_ID_iter.hasNext()) {
                if (i < 20) {
                    if (myPagePerPageFlag) i++;
                    toPrint = myProperties.getProperty((String) APPDOC_ID_iter.next());
                } else {
                    try {
                        System.out.println("Press q to stop, or any other key to continue :");
                        myBr.read(charbuff);
                        System.out.println();
                        if (charbuff[0] == 'q' || charbuff[0] == 'Q') {
                            myBreakout = true;
                            charbuff[0] = ' ';
                        }
                    } catch (IOException e) {
                        return;
                    }
                    if (myBreakout) break; else i = 0;
                }
            }
        }
    }

    /**
 * The list of requests of the jBPA is given back with request status
 * Creation date: (07/01/2002 10:31:38)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void LSREQ(String[] theCommand) throws RemoteException {
        Properties myProperties = null;
        boolean myPagePerPageFlag = false;
        boolean myDataReturned = false;
        boolean myBreakout = false;
        BufferedReader myBr = new BufferedReader(new InputStreamReader(System.in));
        if (getJBpaServer() == null) {
        } else {
            if (theCommand.length == 1) {
                myProperties = getJBpaServer().getJBpaRequestListAndStatus(new Properties());
                myDataReturned = true;
            } else {
                if (!theCommand[1].equalsIgnoreCase("/p")) {
                    printHelp(theCommand[0], LSREQ);
                } else {
                    myPagePerPageFlag = true;
                    myProperties = getJBpaServer().getJBpaRequestListAndStatus(new Properties());
                    myDataReturned = true;
                }
            }
        }
        if (myDataReturned) {
            LinkedList my_REQ_TICKETNUM_LinkedList = OrderProperties.getStartsAndEndsWith(myProperties, JBpaMonitoring.REQ_TICKETNUM, "value");
            Iterator REQ_TICKETNUM_iter = my_REQ_TICKETNUM_LinkedList.iterator();
            LinkedList my_BPAREQ_TICKETNUM_LinkedList = OrderProperties.getStartsAndEndsWith(myProperties, JBpaMonitoring.BPAREQ_TICKETNUM, "value");
            Iterator BPAREQ_TICKETNUM_iter = my_BPAREQ_TICKETNUM_LinkedList.iterator();
            LinkedList my_REQ_JBPS_ID_LinkedList = OrderProperties.getStartsAndEndsWith(myProperties, JBpaMonitoring.REQ_JBPS_ID, "value");
            Iterator REQ_JBPS_ID_iter = my_REQ_JBPS_ID_LinkedList.iterator();
            LinkedList my_REQ_USER_LinkedList = OrderProperties.getStartsAndEndsWith(myProperties, JBpaMonitoring.REQ_USER, "value");
            Iterator REQ_USER_iter = my_REQ_USER_LinkedList.iterator();
            LinkedList my_REQ_STATUS_LinkedList = OrderProperties.getStartsAndEndsWith(myProperties, JBpaMonitoring.REQ_STATUS, "value");
            Iterator REQ_STATUS_iter = my_REQ_STATUS_LinkedList.iterator();
            StringBuffer myTicknum = new StringBuffer("Ticket number");
            myTicknum.setLength(18);
            StringBuffer myBpaticknum = new StringBuffer("BPA ticket number");
            myBpaticknum.setLength(18);
            StringBuffer myJbpsid = new StringBuffer("JBps ID");
            myJbpsid.setLength(18);
            StringBuffer myRequser = new StringBuffer("Req User");
            myRequser.setLength(15);
            StringBuffer myReqstat = new StringBuffer("Req Status");
            myReqstat.setLength(15);
            StringBuffer tmpREQ_TICKETNUM = null;
            StringBuffer tmpBPAREQ_TICKETNUM = null;
            StringBuffer tmpREQ_JBPS_ID = null;
            StringBuffer tmpREQ_USER = null;
            StringBuffer tmpREQ_STATUS = null;
            TreeMap mySortedMap = new TreeMap();
            Vector myVector = new Vector();
            while (REQ_TICKETNUM_iter.hasNext()) {
                tmpREQ_TICKETNUM = new StringBuffer(myProperties.getProperty((String) REQ_TICKETNUM_iter.next()));
                tmpREQ_TICKETNUM.setLength(18);
                tmpBPAREQ_TICKETNUM = new StringBuffer(myProperties.getProperty((String) BPAREQ_TICKETNUM_iter.next()));
                tmpBPAREQ_TICKETNUM.setLength(18);
                tmpREQ_JBPS_ID = new StringBuffer(myProperties.getProperty((String) REQ_JBPS_ID_iter.next()));
                tmpREQ_JBPS_ID.setLength(18);
                tmpREQ_USER = new StringBuffer(myProperties.getProperty((String) REQ_USER_iter.next()));
                tmpREQ_USER.setLength(15);
                tmpREQ_STATUS = new StringBuffer(myProperties.getProperty((String) REQ_STATUS_iter.next()));
                tmpREQ_STATUS.setLength(14);
                tmpREQ_STATUS.append("\n");
                mySortedMap.put(new Integer(tmpREQ_TICKETNUM.toString().trim()), tmpREQ_TICKETNUM.toString() + tmpBPAREQ_TICKETNUM.toString() + tmpREQ_JBPS_ID.toString() + tmpREQ_USER.toString() + tmpREQ_STATUS.toString());
            }
            Iterator myIter = mySortedMap.values().iterator();
            while (myIter.hasNext()) myVector.insertElementAt(myIter.next(), 0);
            Iterator myPrintIter = myVector.iterator();
            int i = 0;
            char[] myCharbuff = new char[4];
            while (myPrintIter.hasNext()) {
                if (i < 20) {
                    if (myPagePerPageFlag) i++;
                } else {
                    try {
                        System.out.print("Press q to stop, or any other key to continue :");
                        myBr.read(myCharbuff);
                        System.out.println();
                        if (myCharbuff[0] == 'q' || myCharbuff[0] == 'Q') {
                            myBreakout = true;
                            myCharbuff[0] = ' ';
                        }
                    } catch (IOException e) {
                        return;
                    }
                    if (myBreakout) break; else i = 0;
                }
            }
        }
    }

    /**
 * The list of temporary appdocs stored in the jBPA is given back.
 * NOT YET IMPLEMENTED
 * Creation date: (07/01/2002 14:29:08)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void LSTEMPAPP() throws RemoteException {
    }

    /**
 * <B>Main JBpaAdminConsole launch method</B><BR>
 * Creation date: (02/01/2002 14:27:01)
 * @param arg String[]
 */
    public static void main(String[] arg) {
        System.out.println("Current version of the JBpaAdminConsole is 1.0");
        JBpaAdminConsole myJBpaConsole = new JBpaAdminConsole();
        if (arg.length > 0) myJBpaConsole.openComm(arg[0]);
        if (arg.length > 2) myJBpaConsole.login(arg[1], arg[2]);
        myJBpaConsole.start();
    }

    /**
 * <B>Open the RMI communication with remote server</B><BR>
 * Creation date: (02/01/2002 15:48:26)
 * @return boolean
 * @param theURL String
 */
    protected boolean openComm(String theURL) {
        try {
            String myURL = null;
            if (theURL.indexOf('/') != -1) myURL = "rmi://" + theURL; else myURL = "rmi://" + theURL + "/" + SERVER_URL;
            setServer(java.rmi.Naming.lookup(myURL));
            setServerURL(theURL);
            setServerVersion(getJBpaServer().getVersion());
            setServerId(getJBpaServer().getSrvInstanceId());
            URL_IN_USE = theURL;
            return true;
        } catch (java.net.MalformedURLException mue) {
            trace(1, "MalformedURLException: " + mue.getMessage());
        } catch (NotBoundException nbe) {
            trace(1, "NotBoundException: " + nbe.getMessage());
        } catch (RemoteException re) {
            dispatchRemoteException(re);
        }
        return false;
    }

    /**
 * The status of a given request is given back
 * Creation date: (07/01/2002 13:45:06)
 * @param theCommand String[]
 * @exception RemoteException 
 */
    public void REQSTATUS(String[] theCommand) throws RemoteException {
        if (getJBpaServer() == null) {
        } else {
            if (theCommand.length != 2) {
                printHelp(theCommand[0], REQSTATUS);
            } else {
                int myRequestStatus;
                try {
                    myRequestStatus = getJBpaServer().getRequestStatus(theCommand[1]);
                } catch (JBpaException e) {
                    throw new RemoteException("JBpaException[" + e.getReturnCode() + "] : " + e.getMessage());
                }
            }
        }
    }

    /**
 * <B>Call the shutdown() remote server method</B><BR>
 * Creation date: (09/04/01 09:58:31)
 * @param theCommand String[] 
 * @exception RemoteException
 */
    public void SHUTDOWN() throws RemoteException {
        if (getJBpaServer() == null) {
        } else {
            getJBpaServer().shutdown(getSessionUID());
            closeComm();
        }
    }

    /**
 * <B>Call the startServer() remote server method</B><BR>
 * Creation date: (09/04/01 09:58:31)
 * @param theCommand String[] 
 * @exception RemoteException
 */
    public void START() throws RemoteException {
        if (getJBpaServer() == null) {
        } else {
            getJBpaServer().startJBpaServer(getSessionUID());
        }
    }

    /**
 * <B>Call the stopServer() remote server method</B><BR>
 * Creation date: (09/04/01 09:58:31)
 * @param theCommand String[] 
 * @exception RemoteException
 */
    public void STOP() throws RemoteException {
        if (getJBpaServer() == null) {
        } else {
            getJBpaServer().stopJBpaServer(getSessionUID());
        }
    }
}

package org.epo.jbps.clients.admin;

import java.rmi.*;
import org.epo.jbps.generic.BpsException;
import org.epo.jbps.rmi.JBpsAdminInterface;
import java.io.*;
import org.epo.jbps.rmi.BpaRequestInfo;
import java.util.*;

/**
 * <B>Administration Console for JBPS</B>
 * Creation date: 02/01/2002
 * @author Infotel Conseil
 */
public class JBpsAdminConsole extends com.infotel.util.RMIAdminConsole {

    private static final String SERVER_URL = "JBpsServer";

    private static String URL_IN_USE = null;

    private static final String[] SHUTDOWN = { "shutdown" };

    private static final String[] STOP = { "stop" };

    private static final String[] START = { "start" };

    private static final String[] INFOSRV = { "infoSrv" };

    private static final String[] LSSTAT = { "lsStat" };

    private static final String[] LSREQ = { "lsReq" };

    private static final String[] STOPMGR = { "stopMgr" };

    private static final String[] STARTMGR = { "startMgr" };

    private static final String[] INFOMGR = { "infoMgr" };

    private static final String[] CANCEL = { "cancel" };

    private static final String[] NBPROC = { "nbProc" };

    private static final String[] TRACELVL = { "traceLvl" };

    private static final String[] KILL = { "kill" };

    private static final String[][][] RESERVED_WORDS = { { SHUTDOWN, { "Stops the jBPs server (JVM down, unbind in RMI registry)" } }, { LSSTAT, { "Gives back the list of archived requests of a jBPS manager", "Parameter : ManagerName", "option(s) : /p : page per page list" }, { "ManagerName" }, { "/p" } }, { LSREQ, { "Gives back the list of active (under processing) requests of a jBPS manager", "Parameter : ManagerName", "Option(s) : /p : page per page list", "            /details RequestNumber" }, { "ManagerName" }, { "/p", "/details RequestNumber" } }, { START, { "Starts the jBPS server functionalities" } }, { STOP, { "Stops the jBPS server functionalities" } }, { INFOSRV, { "Gives back information on the jBPS server" } }, { STARTMGR, { "Starts the desired jBPS manager", "Parameter : ManagerName" }, { "ManagerName" } }, { STOPMGR, { "Stops the desired jBPS manager and its processes treatments", "Parameters : ManagerName", "             StopMode : Q - Quiescing stop mode", "                        N - No pending stop mode", "                        I - Immediate stop mode", "                        Other : Quiescing Mode" }, { "ManagerName", "StopMode" } }, { INFOMGR, { "Gives back information on the jBPS manager", "Parameter : ManagerName" }, { "ManagerName" } }, { CANCEL, { "Cancels the desired request", "Parameters : ManagerName", "             RequestNumber" }, { "ManagerName", "RequestNumber" } }, { NBPROC, { "Displays and/or sets the number of processes of a jBPS manager", "Parameter : ManagerName", "Option(s) : NewNumberOfProcesses" }, { "ManagerName" }, { "NewNumberOfProcesses" } }, { TRACELVL, { "Displays and/or sets the trace level of a jBPS manager", "Parameter : ManagerName", "Option(s) : NewTraceLevel" }, { "ManagerName" }, { "NewTraceLevel" } }, { KILL, { "Kills the JVM of current jBPS server" }, { "securityKey" } } };

    /**
 * JBpsAdminConsole Standard Constructor
 * Creation date: (11/01/2002 11:58:30)
 */
    public JBpsAdminConsole() {
        super();
        setServerPrompt("JBpsServer");
        addReservedWords(RESERVED_WORDS);
    }

    /**
 * The desired request is cancelled.
 * Creation date: (11/01/2002 12:10:34)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void CANCEL(String[] theCommand) throws RemoteException {
        if (getJBpsServer() == null) System.out.println("No connection is established"); else {
            if (theCommand.length != 3) {
                printHelp(theCommand[0], CANCEL);
            } else {
                System.out.println("Try to CANCEL a JBPS manager request");
                System.out.println("Send cancel request command for " + theCommand[1] + ", request number " + theCommand[2]);
                getJBpsServer().cancelActiveRequest(getSessionUID(), theCommand[1], theCommand[2]);
                System.out.println("Cancel completed");
            }
        }
    }

    /**
 * <B>Close the RMI communication with remote server</B><BR>
 * Creation date: (11/01/2002 12:00:09)
 */
    protected void closeComm() {
        logout();
        setServer(null);
    }

    /**
 * Information on the jBPS manager is given back (description, status, number of processes, trace level)
 * Creation date: (11/01/2002 12:09:39)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void INFOMGR(String[] theCommand) throws RemoteException {
        String theTraceLevel = null;
        int numOfProccessManager = 0;
        boolean isAlive = false, isAskedToStop = false, isStarted = false;
        if (getJBpsServer() == null) {
            System.out.println("No connection is established");
        } else {
            if (theCommand.length == 2) {
                isAlive = getJBpsServer().isManagerAlive(theCommand[1]);
                isAskedToStop = getJBpsServer().isManagerAskedToStop(theCommand[1]);
                isStarted = getJBpsServer().isManagerStarted(theCommand[1]);
                numOfProccessManager = getJBpsServer().getNbProcessesFor_a_Manager(getSessionUID(), theCommand[1]);
                theTraceLevel = getJBpsServer().getTraceLevel(getSessionUID(), theCommand[1]);
                System.out.println("The manager " + theCommand[1] + " is alive ?               :" + isAlive);
                System.out.println("The manager " + theCommand[1] + " has been asked to stop ? :" + isAskedToStop);
                System.out.println("The manager " + theCommand[1] + " is started ?             :" + isStarted);
                System.out.println("The manager " + theCommand[1] + " has a trace level of     :" + theTraceLevel);
                System.out.println("The manager " + theCommand[1] + " has got " + numOfProccessManager + " processes");
            } else printHelp(theCommand[0], INFOMGR);
        }
    }

    /**
 * Information on the jBPS server is given back (server instance name, URL, status)
 * Creation date: (11/01/2002 12:09:22)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void INFOSRV(String[] theCommand) throws RemoteException {
        if (getJBpsServer() == null) {
            System.out.println("No connection is established");
        } else {
            System.out.println("The server Instance ID is           :" + getJBpsServer().getSrvInstanceId());
            System.out.println("The server Version is               :" + getJBpsServer().getServerVersion());
            System.out.println("The server Instance  URL name is    :" + SERVER_URL);
            System.out.println("The server URL in use is            :" + URL_IN_USE);
            System.out.println("The server Status is                :" + getJBpsServer().isStatusUp());
            String[] myManagerListName = getJBpsServer().getManagerListForPrint();
            System.out.println("Number of Started manager(s)        :" + myManagerListName.length);
            for (int i = 0; i < myManagerListName.length; i++) System.out.println("    Manager Name                    :" + myManagerListName[i]);
        }
    }

    /**
 * Admin console login method
 * Creation date: (11/01/2002 12:00:24)
 * @return boolean
 * @param theUser java.lang.String
 * @param thePwd java.lang.String
 */
    protected boolean login(String theUser, String thePwd) {
        if (getServer() == null) {
            trace(1, "Use OPEN %URL% before");
            return false;
        } else try {
            logout();
            setSessionUID(getJBpsServer().openJBpsSession(theUser, thePwd));
            setServerId(getJBpsServer().getSrvInstanceId());
            consoleInfo();
            return true;
        } catch (RemoteException re) {
            dispatchRemoteException(re);
            closeComm();
            return false;
        } catch (BpsException be) {
            RemoteException re = new RemoteException("BpsException : " + be.getMessage());
            dispatchRemoteException(re);
            closeComm();
            return false;
        }
    }

    /**
 * Admin console logout method
 * Creation date: (11/01/2002 12:00:38)
 */
    protected void logout() {
        try {
            if (getServer() != null) {
                if (getSessionUID() != null) getJBpsServer().closeJBpsSession(getSessionUID());
            }
        } catch (BpsException be) {
        } catch (RemoteException re) {
        } finally {
            setSessionUID(null);
        }
    }

    /**
 * The list of active (under processing) requests of a jBPS manager is given back
 * Creation date: (11/01/2002 12:10:01)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void LSREQ(String[] theCommand) throws RemoteException {
        LinkedList myLinkedList = null;
        char[] charbuff = new char[5];
        boolean breakOut = false;
        BufferedReader myBr = new BufferedReader(new InputStreamReader(System.in));
        if (getJBpsServer() == null) {
            System.out.println("No connection is established");
        } else {
            if (theCommand.length == 3) {
                if (!theCommand[2].equalsIgnoreCase("/p")) {
                    printHelp(theCommand[0], LSREQ);
                    return;
                }
            }
            if ((theCommand.length == 2) || (theCommand.length == 3)) {
                myLinkedList = getJBpsServer().getActiveReqList(theCommand[1]);
                if (myLinkedList.size() == 0) {
                    System.out.println("No active requests for the jBPS manager " + theCommand[1]);
                    return;
                }
                Iterator myLinkedListIter = myLinkedList.iterator();
                BpaRequestInfo myBpaRequestInfo = null;
                TreeMap mySortedMap = new TreeMap();
                while (myLinkedListIter.hasNext()) {
                    myBpaRequestInfo = (BpaRequestInfo) myLinkedListIter.next();
                    mySortedMap.put(myBpaRequestInfo.getRequestNumber(), "Request number :" + myBpaRequestInfo.getRequestNumber() + "    Status :" + myBpaRequestInfo.getRequestStatus());
                }
                Iterator mySortedMapIter = mySortedMap.values().iterator();
                int printedLine = 0;
                Vector myVector = new Vector();
                while (mySortedMapIter.hasNext()) {
                    myVector.insertElementAt(mySortedMapIter.next(), 0);
                }
                Iterator myVectorIter = myVector.iterator();
                while (myVectorIter.hasNext()) {
                    System.out.println(myVectorIter.next());
                    printedLine++;
                    if (printedLine == 20) {
                        printedLine = 0;
                        System.out.print("Press q to stop, or any other key to continue :");
                        try {
                            myBr.read(charbuff);
                        } catch (IOException e) {
                            System.out.println("Exception while reading request list");
                            return;
                        }
                        if (charbuff[0] == 'q' || charbuff[0] == 'Q') {
                            breakOut = true;
                            charbuff[0] = ' ';
                        } else System.out.println(" ");
                    }
                    if (breakOut) break;
                }
            }
            if (theCommand.length == 4) {
                if (theCommand[2].equalsIgnoreCase("/details")) {
                    myLinkedList = getJBpsServer().getActiveReqList(theCommand[1]);
                    if (myLinkedList.size() == 0) {
                        System.out.println("No active requests for the jBPS manager " + theCommand[1]);
                        return;
                    }
                    Iterator myLinkedListIter = myLinkedList.iterator();
                    BpaRequestInfo myBpaRequest = null;
                    while (myLinkedListIter.hasNext()) {
                        myBpaRequest = (BpaRequestInfo) myLinkedListIter.next();
                        if (myBpaRequest.getRequestNumber().trim().equalsIgnoreCase(theCommand[3])) {
                            System.out.println("Request number                  :" + myBpaRequest.getRequestNumber());
                            System.out.println("Status                          :" + myBpaRequest.getRequestStatus());
                            System.out.println("Job Name                        :" + myBpaRequest.getJobName());
                            System.out.println("Request Creation Time Stamp     :" + myBpaRequest.getReqCreationTimestamp());
                            System.out.println("Request Completion Time Stamp   :" + myBpaRequest.getReqCompletionTimestamp());
                            System.out.println("Request Printed Time Stamp      :" + myBpaRequest.getReqPrintedTimestamp());
                            System.out.println("Number of Pages                 :" + myBpaRequest.getNbPages());
                            System.out.println("Request Number of copy          :" + myBpaRequest.getRequestNbCopy());
                            System.out.println("Request BPS ID                  :" + myBpaRequest.getRequestBPSId());
                            System.out.println("Request User                    :" + myBpaRequest.getRequestUser());
                            System.out.println("Number of Processed Document    :" + myBpaRequest.getNbProcessedDoc());
                            System.out.println("Number of Retry                 :" + myBpaRequest.getNbRetry());
                            System.out.println("Number of Tapes Processed       :" + myBpaRequest.getNbTapesProcessed());
                            System.out.println("Number document                 :" + myBpaRequest.getNumberDoc());
                            System.out.println("Request Printer                 :" + myBpaRequest.getRequestPrinter());
                            System.out.println("Request Printer Type            :" + myBpaRequest.getRequestPrinterType());
                            System.out.println("Request Printer Sub-Type        :" + myBpaRequest.getRequestSubType());
                            System.out.println("Request Profile                 :" + myBpaRequest.getRequestProfile());
                            System.out.println("Request Type                    :" + myBpaRequest.getRequestType());
                            System.out.println("Is the request Workable         :" + myBpaRequest.isWorkable());
                        }
                    }
                } else {
                    printHelp(theCommand[0], LSREQ);
                    return;
                }
            }
            if (!((theCommand.length == 2) || (theCommand.length == 3) || (theCommand.length == 4))) {
                printHelp(theCommand[0], LSREQ);
            }
        }
    }

    /**
 * The list of archived requests of a jBPS manager is given back
 * Creation date: (11/01/2002 12:10:18)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void LSSTAT(String[] theCommand) throws RemoteException {
        BufferedReader myBr = new BufferedReader(new InputStreamReader(System.in));
        char[] charbuff = new char[5];
        boolean breakOut = false;
        LinkedList myResult = null;
        if (getJBpsServer() == null) {
            System.out.println("No connection is established");
        } else {
            if ((theCommand.length == 2) || (theCommand.length == 3)) {
                myResult = getJBpsServer().getArchivedReqList(null, theCommand[1]);
                if (myResult.size() == 0) {
                    System.out.println("No request found for the jBPS manager " + theCommand[1]);
                    return;
                }
                Iterator iter = myResult.iterator();
                int printedLine = 0;
                System.out.println("Ticket Number      Status        User            Job Name       Printer ");
                while (iter.hasNext()) {
                    if (theCommand.length == 3) if (theCommand[2].equalsIgnoreCase("/p")) {
                        printedLine++;
                    } else {
                        printHelp(theCommand[0], LSSTAT);
                        return;
                    }
                    String[] myResultForPrint = (String[]) iter.next();
                    for (int i = 0; i < myResultForPrint.length; i++) System.out.print("     " + myResultForPrint[i] + "    ");
                    System.out.println();
                    if (printedLine == 20) {
                        printedLine = 0;
                        System.out.print("Press q to stop, or any other key to continue :");
                        try {
                            myBr.read(charbuff);
                        } catch (IOException e) {
                            System.out.println("IOException while reading appdoc list");
                            return;
                        }
                        if (charbuff[0] == 'q' || charbuff[0] == 'Q') {
                            breakOut = true;
                            charbuff[0] = ' ';
                        } else System.out.println("Ticket Number      Status        User            Job Name       Printer ");
                    }
                    if (breakOut) break;
                }
            } else {
                printHelp(theCommand[0], LSSTAT);
            }
        }
    }

    /**
 * <B>Main JBpsAdminConsole launch method</B><BR>
 * Creation date: (02/01/2002 14:27:01)
 * @param arg java.lang.String[]
 */
    public static void main(String[] args) {
        System.out.println("Current version of the JBpsAdminConsole is 1.0");
        JBpsAdminConsole myJBpsConsole = new JBpsAdminConsole();
        if (args.length > 0) myJBpsConsole.openComm(args[0]);
        if (args.length > 2) myJBpsConsole.login(args[1], args[2]);
        myJBpsConsole.start();
    }

    /**
 * <B>Call a remote server method to get or/and set the number 
 *    of processes of a jBPS manager  level </B><BR>
 * Creation date: (11/01/2002 12:10:50)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void NBPROC(String[] theCommand) throws RemoteException {
        if (getJBpsServer() == null) System.out.println("No connection is established"); else {
            if (theCommand.length == 2) {
                int nbproc = getJBpsServer().getNbProcessesFor_a_Manager(getSessionUID(), theCommand[1]);
                System.out.println("The maximum number of processes for the manager " + theCommand[1] + " is :" + nbproc);
            }
            if (theCommand.length == 3) {
                try {
                    getJBpsServer().updateNbProcesses(getSessionUID(), theCommand[1], Integer.parseInt(theCommand[2]));
                } catch (NumberFormatException e) {
                    printHelp(theCommand[0], NBPROC);
                }
            }
            if ((theCommand.length > 3) || (theCommand.length == 1)) {
                printHelp(theCommand[0], NBPROC);
            }
        }
    }

    /**
 * <B>Open the RMI communication with remote server</B><BR>
 * Creation date: (11/01/2002 12:00:56)
 * @return boolean
 * @param theURL String
 */
    protected boolean openComm(String theURL) {
        try {
            trace(8, "Try to connect JBps server at URL " + theURL + " ...");
            String myURL = null;
            if (theURL.indexOf('/') != -1) myURL = "rmi://" + theURL; else myURL = "rmi://" + theURL + "/" + SERVER_URL;
            Remote myServer = java.rmi.Naming.lookup(myURL);
            JBpsAdminInterface myJBpsServer = (JBpsAdminInterface) myServer;
            setServer(myJBpsServer);
            setServerURL(theURL);
            System.out.println(myURL + " connection openned");
            setServerVersion(getJBpsServer().getServerVersion());
            setServerId(getJBpsServer().getSrvInstanceId());
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
 * <B>Call the shutdown() remote server method</B><BR>
 * Creation date: (11/01/2002 12:07:42)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void SHUTDOWN(String[] theCommand) throws RemoteException {
        if (getJBpsServer() == null) {
            System.out.println("No connection is established");
        } else {
            getJBpsServer().shutdown(getSessionUID());
            System.out.println(getJBpsServer().getSrvInstanceId() + " server shutdown !!!");
            closeComm();
        }
    }

    /**
 * <B>Call the start Server remote server method</B><BR>
 * Creation date: (11/01/2002 12:07:03)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void START(String[] theCommand) throws RemoteException {
        if (getJBpsServer() == null) {
            System.out.println("No connection is established");
        } else {
            System.out.println("try to start JBPS server");
            System.out.println("send start command");
            getJBpsServer().startJBpsServer(getSessionUID());
            System.out.println(getJBpsServer().getSrvInstanceId() + " server started");
        }
    }

    /**
 * <B>Call the start manager remote server method</B><BR>
 * Creation date: (11/01/2002 12:08:21)
 * @param theCommand String[]
 * @exception RemoteException
 */
    public void STARTMGR(String[] theCommand) throws RemoteException {
        if (getJBpsServer() == null) System.out.println("No connection is established"); else {
            if (theCommand.length != 2) {
                printHelp(theCommand[0], STARTMGR);
            } else {
                System.out.println("Try to start a new JBPS manager");
                System.out.println("Send start " + theCommand[1] + " manager command");
                getJBpsServer().startJBpsManager(getSessionUID(), theCommand[1]);
                System.out.println(getJBpsServer().getSrvInstanceId() + " JBPS Manager " + theCommand[1] + " started");
            }
        }
    }

    /**
* <B>Call the stop Server remote server method</B><BR>
 * Creation date: (11/01/2002 12:07:19)
 * @param theCommand java.lang.String[]
 * @exception java.rmi.RemoteException The exception description.
 */
    public void STOP(String[] theCommand) throws java.rmi.RemoteException {
        System.out.println("Stop Method invoked");
        if (getJBpsServer() == null) {
            System.out.println("No connection is established");
        } else {
            System.out.println("Try to stop the JBPS server");
            getJBpsServer().stopJBpsServer(getSessionUID());
            System.out.println("Server stopped");
        }
    }

    /**
* <B>Call the stop JBps Manager remote server method</B><BR>
 * Creation date: (11/01/2002 12:08:41)
 * @param theCommand java.lang.String[]
 * @exception java.rmi.RemoteException The exception description.
 */
    public void STOPMGR(String[] theCommand) throws RemoteException {
        if (getJBpsServer() == null) System.out.println("No connection is established"); else {
            if (theCommand.length != 3) {
                printHelp(theCommand[0], STOPMGR);
            } else {
                System.out.println("Stop manager Method invoked");
                if (getJBpsServer() == null) {
                    System.out.println("No connection is established");
                } else {
                    System.out.println("Try to stop a JBPS manager");
                    getJBpsServer().stopJBpsManager(getSessionUID(), theCommand[1], theCommand[2]);
                    System.out.println("JBPS manager stopped");
                }
            }
        }
    }

    /**
 * <B>Call a remote server method to get or/and set the trace level </B><BR>
 * Creation date: (11/01/2002 12:11:13)
 * @param theCommand java.lang.String[]
 * @exception java.rmi.RemoteException The exception description.
 */
    public void TRACELVL(String[] theCommand) throws java.rmi.RemoteException {
        if (getJBpsServer() == null) {
            System.out.println("No connection is established");
        } else {
            if (theCommand.length < 2) {
                printHelp(theCommand[0], TRACELVL);
            } else {
                if (theCommand.length == 2) {
                    System.out.println("The current Trace level is :" + getJBpsServer().getTraceLevel(getSessionUID(), theCommand[1]));
                }
                if (theCommand.length == 3) {
                    try {
                        getJBpsServer().updateTraceLevel(getSessionUID(), theCommand[1], theCommand[2]);
                    } catch (NumberFormatException e) {
                        printHelp(theCommand[0], TRACELVL);
                    }
                }
            }
        }
    }

    private JBpsAdminInterface getJBpsServer() {
        return (JBpsAdminInterface) getRMIServer();
    }

    /**
 * <B>Kill JVM for current jPXI server</B><BR>
 * Creation date: (09/04/01 09:58:31)
 * @param theCommand java.lang.String[] 
 * @exception java.rmi.RemoteException
 */
    public void KILL(String[] theCommand) throws RemoteException {
        if (getJBpsServer() == null) {
            System.out.println("No connection is established");
        } else {
            if (theCommand.length < 2) printHelp(theCommand[0], KILL); else {
                String mySrvInstanceId = getJBpsServer().getSrvInstanceId();
                try {
                    getJBpsServer().kill(theCommand[1]);
                } catch (UnmarshalException e) {
                } catch (Exception e) {
                    System.out.println("Exception : " + e.getClass().getName() + " - msg : " + e.getMessage());
                }
                setServer(null);
                System.out.println(mySrvInstanceId + " server process has been killed.");
            }
        }
    }
}

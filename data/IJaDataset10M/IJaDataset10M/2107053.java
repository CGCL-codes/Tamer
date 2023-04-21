package jade.imtp.leap.JICP;

import jade.core.FEConnectionManager;
import jade.core.FrontEnd;
import jade.core.BackEnd;
import jade.core.IMTPException;
import jade.core.MicroRuntime;
import jade.core.Specifier;
import jade.core.Timer;
import jade.core.TimerListener;
import jade.core.TimerDispatcher;
import jade.mtp.TransportAddress;
import jade.imtp.leap.BackEndStub;
import jade.imtp.leap.MicroSkeleton;
import jade.imtp.leap.FrontEndSkel;
import jade.imtp.leap.Dispatcher;
import jade.imtp.leap.ICPException;
import jade.imtp.leap.ConnectionListener;
import jade.util.Logger;
import jade.util.leap.Properties;
import java.io.*;
import java.util.Vector;

/**
 * Single full-duplex connection based Front End side dispatcher class
 * @author Giovanni Caire - TILAB
 */
public class FrontEndDispatcher implements FEConnectionManager, Dispatcher, TimerListener, Runnable {

    private static final int RESPONSE_TIMEOUT = 30000;

    private MicroSkeleton mySkel = null;

    private BackEndStub myStub = null;

    protected String myMediatorClass = "jade.imtp.leap.nio.BackEndDispatcher";

    private Properties myProperties;

    private String[] backEndAddresses;

    private TransportAddress mediatorTA;

    private String myMediatorID;

    private long retryTime = JICPProtocol.DEFAULT_RETRY_TIME;

    private long maxDisconnectionTime = JICPProtocol.DEFAULT_MAX_DISCONNECTION_TIME;

    private long keepAliveTime = -1;

    private long connectionDropDownTime = -1;

    private Timer kaTimer, cdTimer;

    private IncomingCommandServer myCommandServer;

    private ConnectionReader myConnectionReader;

    private Connection myConnection = null;

    public boolean refreshingConnection = false;

    private Object connectionLock = new Object();

    private Object responseLock = new Object();

    private ConnectionListener myConnectionListener;

    private boolean active = true;

    private boolean connectionDropped = false;

    private boolean waitingForFlush = false;

    private byte lastSid = 0x0f;

    private int outCnt = 0;

    private JICPPacket lastOutgoingResponse = null;

    private Thread terminator;

    private Logger myLogger = Logger.getMyLogger(getClass().getName());

    /**
	 * Connect to a remote BackEnd and return a stub to communicate with it
	 */
    public BackEnd getBackEnd(FrontEnd fe, Properties props) throws IMTPException {
        myProperties = props;
        myMediatorID = myProperties.getProperty(JICPProtocol.MEDIATOR_ID_KEY);
        try {
            String tmp = props.getProperty(FrontEnd.REMOTE_BACK_END_ADDRESSES);
            backEndAddresses = parseBackEndAddresses(tmp);
            String host = props.getProperty(MicroRuntime.HOST_KEY);
            if (host == null) {
                host = "localhost";
            }
            int port = JICPProtocol.DEFAULT_PORT;
            try {
                port = Integer.parseInt(props.getProperty(MicroRuntime.PORT_KEY));
            } catch (NumberFormatException nfe) {
            }
            mediatorTA = JICPProtocol.getInstance().buildAddress(host, String.valueOf(port), null, null);
            mediatorTA = JICPProtocol.getInstance().buildAddress(host, String.valueOf(port), null, null);
            if (myLogger.isLoggable(Logger.CONFIG)) {
                myLogger.log(Logger.CONFIG, "Remote URL=" + JICPProtocol.getInstance().addrToString(mediatorTA));
            }
            tmp = props.getProperty(JICPProtocol.MEDIATOR_CLASS_KEY);
            if (tmp != null) {
                myMediatorClass = tmp;
            } else {
                props.setProperty(JICPProtocol.MEDIATOR_CLASS_KEY, myMediatorClass);
            }
            if (myLogger.isLoggable(Logger.CONFIG)) {
                myLogger.log(Logger.CONFIG, "Mediator class=" + myMediatorClass);
            }
            tmp = props.getProperty(JICPProtocol.RECONNECTION_RETRY_TIME_KEY);
            try {
                retryTime = Long.parseLong(tmp);
            } catch (Exception e) {
            }
            if (myLogger.isLoggable(Logger.CONFIG)) {
                myLogger.log(Logger.CONFIG, "Reconnection time=" + retryTime);
            }
            tmp = props.getProperty(JICPProtocol.MAX_DISCONNECTION_TIME_KEY);
            try {
                maxDisconnectionTime = Long.parseLong(tmp);
            } catch (Exception e) {
            }
            if (myLogger.isLoggable(Logger.CONFIG)) {
                myLogger.log(Logger.CONFIG, "Max discon. time=" + maxDisconnectionTime);
            }
            tmp = props.getProperty(JICPProtocol.KEEP_ALIVE_TIME_KEY);
            try {
                keepAliveTime = Long.parseLong(tmp);
            } catch (Exception e) {
                props.setProperty(JICPProtocol.KEEP_ALIVE_TIME_KEY, String.valueOf(keepAliveTime));
            }
            if (myLogger.isLoggable(Logger.CONFIG)) {
                myLogger.log(Logger.CONFIG, "Keep-alive time=" + keepAliveTime);
            }
            tmp = props.getProperty(JICPProtocol.DROP_DOWN_TIME_KEY);
            try {
                connectionDropDownTime = Long.parseLong(tmp);
            } catch (Exception e) {
            }
            if (myLogger.isLoggable(Logger.CONFIG)) {
                myLogger.log(Logger.CONFIG, "Connection-drop-down time=" + connectionDropDownTime);
            }
            try {
                Object obj = props.get("connection-listener");
                if (obj instanceof ConnectionListener) {
                    myConnectionListener = (ConnectionListener) obj;
                } else {
                    myConnectionListener = (ConnectionListener) Class.forName(obj.toString()).newInstance();
                }
            } catch (Exception e) {
            }
            mySkel = new FrontEndSkel(fe);
            myStub = new BackEndStub(this);
            Connection c = createBackEnd();
            active = true;
            startConnectionReader(c);
            return myStub;
        } catch (ICPException icpe) {
            throw new IMTPException("Connection error", icpe);
        }
    }

    /**
	   Send the CREATE_MEDIATOR command with the necessary parameter
	   in order to create the BackEnd in the fixed network.
	   Executed 
	   - at bootstrap time by the thread that creates the FrontEndContainer. 
	   - To re-attach to the platform after a fault of the BackEnd
	 */
    private JICPConnection createBackEnd() throws IMTPException {
        StringBuffer sb = BackEndStub.encodeCreateMediatorRequest(myProperties);
        if (myMediatorID != null) {
            BackEndStub.appendProp(sb, JICPProtocol.MEDIATOR_ID_KEY, myMediatorID);
            BackEndStub.appendProp(sb, "outcnt", String.valueOf(outCnt));
            BackEndStub.appendProp(sb, "lastsid", String.valueOf(lastSid));
        }
        JICPPacket pkt = new JICPPacket(JICPProtocol.CREATE_MEDIATOR_TYPE, JICPProtocol.DEFAULT_INFO, null, sb.toString().getBytes());
        for (int i = -1; i < backEndAddresses.length; i++) {
            if (i >= 0) {
                String addr = backEndAddresses[i];
                int colonPos = addr.indexOf(':');
                String host = addr.substring(0, colonPos);
                String port = addr.substring(colonPos + 1, addr.length());
                mediatorTA = new JICPAddress(host, port, myMediatorID, "");
            }
            try {
                myLogger.log(Logger.INFO, "Creating BackEnd on jicp://" + mediatorTA.getHost() + ":" + mediatorTA.getPort());
                JICPConnection con = openConnection(mediatorTA);
                writePacket(pkt, con);
                pkt = con.readPacket();
                String replyMsg = new String(pkt.getData());
                if (pkt.getType() != JICPProtocol.ERROR_TYPE) {
                    BackEndStub.parseCreateMediatorResponse(replyMsg, myProperties);
                    myMediatorID = myProperties.getProperty(JICPProtocol.MEDIATOR_ID_KEY);
                    mediatorTA = new JICPAddress(mediatorTA.getHost(), mediatorTA.getPort(), myMediatorID, null);
                    myLogger.log(Logger.INFO, "BackEnd OK: mediator-id = " + myMediatorID);
                    return con;
                } else {
                    myLogger.log(Logger.WARNING, "Mediator error: " + replyMsg);
                    if (myConnectionListener != null && replyMsg != null && replyMsg.startsWith(JICPProtocol.NOT_AUTHORIZED_ERROR)) {
                        myConnectionListener.handleConnectionEvent(ConnectionListener.NOT_AUTHORIZED, replyMsg);
                    }
                }
            } catch (IOException ioe) {
                myLogger.log(Logger.WARNING, "Connection error. " + ioe.toString());
            }
        }
        throw new IMTPException("Error creating the BackEnd.");
    }

    private String[] parseBackEndAddresses(String addressesText) {
        Vector addrs = Specifier.parseList(addressesText, ';');
        String[] result = new String[addrs.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (String) addrs.elementAt(i);
        }
        return result;
    }

    /**
	   Make this FrontEndDispatcher terminate.
	 */
    public synchronized void shutdown() {
        if (active) {
            active = false;
            terminator = Thread.currentThread();
            if (terminator != myCommandServer) {
                JICPPacket terminationPacket = null;
                try {
                    if (connectionDropped) {
                        myConnection = openConnection(mediatorTA);
                        terminationPacket = new JICPPacket(JICPProtocol.CONNECT_MEDIATOR_TYPE, JICPProtocol.TERMINATED_INFO, mediatorTA.getFile(), null);
                    } else {
                        terminationPacket = new JICPPacket(JICPProtocol.COMMAND_TYPE, JICPProtocol.TERMINATED_INFO, null);
                    }
                    if (myConnection != null) {
                        myLogger.log(Logger.INFO, "Sending termination notification");
                        writePacket(terminationPacket, myConnection);
                        myConnection.close();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
	 Deliver a serialized command to the BackEnd.
	 @return The serialized response
	 */
    public synchronized byte[] dispatch(byte[] payload, boolean flush) throws ICPException {
        if (connectionDropped) {
            myLogger.log(Logger.INFO, "Dispatching with connection dropped. Reconnecting...");
            undrop();
            throw new ICPException("Connection dropped");
        } else {
            if (myConnection != null) {
                if (waitingForFlush && !flush) {
                    throw new ICPException("Upsetting dispatching order");
                }
                waitingForFlush = false;
                if (myLogger.isLoggable(Logger.FINE)) {
                    myLogger.log(Logger.FINE, "Issuing outgoing command " + outCnt);
                }
                JICPPacket pkt = new JICPPacket(JICPProtocol.COMMAND_TYPE, JICPProtocol.DEFAULT_INFO, payload);
                pkt.setSessionID((byte) outCnt);
                try {
                    lastOutgoingResponse = null;
                    writePacket(pkt, myConnection);
                    JICPPacket response = waitForResponse(outCnt, RESPONSE_TIMEOUT);
                    if (response != null) {
                        if (myLogger.isLoggable(Logger.FINER)) {
                            myLogger.log(Logger.FINER, "Response received " + response.getSessionID());
                        }
                        if (response.getType() == JICPProtocol.ERROR_TYPE) {
                            throw new ICPException(new String(response.getData()));
                        }
                        outCnt = (outCnt + 1) & 0x0f;
                        return response.getData();
                    } else {
                        myLogger.log(Logger.WARNING, "Response timeout expired " + pkt.getSessionID());
                        handleDisconnection();
                        throw new ICPException("Response timeout expired");
                    }
                } catch (IOException ioe) {
                    myLogger.log(Logger.WARNING, ioe.toString());
                    handleDisconnection();
                    throw new ICPException("Dispatching error.", ioe);
                }
            } else {
                throw new ICPException("Unreachable");
            }
        }
    }

    private void startConnectionReader(Connection c) {
        myConnection = c;
        refreshingConnection = false;
        myConnectionReader = new ConnectionReader(myConnection);
        myConnectionReader.start();
    }

    private int cnt = 0;

    /**
	 Inner class ConnectionReader.
	 This class is responsible for reading incoming packets (incoming commands and responses
	 to outgoing commands)
	 */
    private class ConnectionReader extends Thread {

        private int myId;

        private Connection myConnection = null;

        public ConnectionReader(Connection c) {
            super();
            myConnection = c;
            setName("ConnectionReader-" + myId);
        }

        public void run() {
            myId = cnt++;
            myLogger.log(Logger.INFO, "CR-" + myId + " started");
            try {
                while (isConnected()) {
                    JICPPacket pkt = myConnection.readPacket();
                    pkt = handleIncomingPacket(pkt);
                    if (pkt != null) {
                        writePacket(pkt, myConnection);
                    }
                }
            } catch (IOException ioe) {
                synchronized (connectionLock) {
                    if (active && !connectionDropped) {
                        myLogger.log(Logger.WARNING, "CR Exception " + ioe);
                        if (this == FrontEndDispatcher.this.myConnectionReader) {
                            handleDisconnection();
                        }
                    }
                }
            }
            myLogger.log(Logger.INFO, "CR-" + myId + " terminated");
        }

        private JICPPacket handleIncomingPacket(JICPPacket pkt) {
            switch(pkt.getType()) {
                case JICPProtocol.COMMAND_TYPE:
                    serveCommand(pkt);
                    break;
                case JICPProtocol.KEEP_ALIVE_TYPE:
                    return handleIncomingKeepAlive(pkt);
                case JICPProtocol.RESPONSE_TYPE:
                case JICPProtocol.ERROR_TYPE:
                    notifyOutgoingResponseReceived(pkt);
                    break;
                default:
                    myLogger.log(Logger.WARNING, "Unexpected incoming packet type: " + pkt.getType());
            }
            return null;
        }

        private final boolean isConnected() {
            return myConnection != null;
        }
    }

    private JICPPacket handleIncomingCommand(JICPPacket cmd) {
        if (myLogger.isLoggable(Logger.FINE)) {
            myLogger.log(Logger.FINE, "Incoming command received " + cmd.getSessionID());
        }
        byte[] rspData = mySkel.handleCommand(cmd.getData());
        if (myLogger.isLoggable(Logger.FINER)) {
            myLogger.log(Logger.FINER, "Incoming command served " + cmd.getSessionID());
        }
        return new JICPPacket(JICPProtocol.RESPONSE_TYPE, JICPProtocol.DEFAULT_INFO, rspData);
    }

    private JICPPacket handleIncomingKeepAlive(JICPPacket ka) {
        return new JICPPacket(JICPProtocol.RESPONSE_TYPE, JICPProtocol.DEFAULT_INFO, null);
    }

    private JICPConnection openConnection(TransportAddress ta) throws IOException {
        if (myConnectionListener != null) {
            myConnectionListener.handleConnectionEvent(ConnectionListener.BEFORE_CONNECTION, null);
        }
        JICPConnection c = getConnection(ta);
        return c;
    }

    /**
         * subclasses may overwrite this to provide their version of a JICPConnection
         * @param ta
         * @return
         * @throws IOException
         */
    protected JICPConnection getConnection(TransportAddress ta) throws IOException {
        return new JICPConnection(ta);
    }

    private synchronized void writePacket(JICPPacket pkt, Connection c) throws IOException {
        c.writePacket(pkt);
        if (Thread.currentThread() == terminator) {
            myConnection.close();
        } else {
            updateKeepAlive();
            if (pkt.getType() != JICPProtocol.KEEP_ALIVE_TYPE && pkt.getType() != JICPProtocol.DROP_DOWN_TYPE) {
                updateConnectionDropDown();
            }
        }
    }

    private JICPPacket waitForResponse(int sessionID, long timeout) {
        synchronized (responseLock) {
            try {
                while (lastOutgoingResponse == null) {
                    responseLock.wait(timeout);
                    if (lastOutgoingResponse != null && (sessionID != -1 && lastOutgoingResponse.getSessionID() != sessionID)) {
                        myLogger.log(Logger.WARNING, "Wrong sessionID in response from BE: type=" + lastOutgoingResponse.getType() + " info=" + lastOutgoingResponse.getInfo() + " SID=" + lastOutgoingResponse.getSessionID() + " while " + sessionID + " was expected.");
                        lastOutgoingResponse = null;
                        continue;
                    }
                    break;
                }
            } catch (Exception e) {
            }
            return lastOutgoingResponse;
        }
    }

    private void notifyOutgoingResponseReceived(JICPPacket rsp) {
        synchronized (responseLock) {
            lastOutgoingResponse = rsp;
            responseLock.notifyAll();
        }
    }

    public void run() {
        int cnt = 0;
        long startTime = System.currentTimeMillis();
        boolean backEndExists = true;
        while (active) {
            try {
                if (backEndExists) {
                    myLogger.log(Logger.INFO, "Connecting to " + mediatorTA.getHost() + ":" + mediatorTA.getPort() + " " + cnt);
                    Connection c = openConnection(mediatorTA);
                    JICPPacket pkt = new JICPPacket(JICPProtocol.CONNECT_MEDIATOR_TYPE, JICPProtocol.DEFAULT_INFO, mediatorTA.getFile(), null);
                    writePacket(pkt, c);
                    pkt = c.readPacket();
                    if (pkt.getType() == JICPProtocol.ERROR_TYPE) {
                        String errorMsg = new String(pkt.getData());
                        c.close();
                        if (errorMsg.equals(JICPProtocol.NOT_FOUND_ERROR)) {
                            handleBENotFound();
                            backEndExists = false;
                            continue;
                        } else {
                            handleReconnectionError("JICP Error. " + errorMsg);
                            return;
                        }
                    } else {
                        myProperties.setProperty(JICPProtocol.LOCAL_HOST_KEY, new String(pkt.getData()));
                        myLogger.log(Logger.INFO, "Connect OK");
                        handleReconnection(c);
                        return;
                    }
                } else {
                    Connection c = createBackEnd();
                    handleReconnection(c);
                    return;
                }
            } catch (IOException ioe) {
                myLogger.log(Logger.WARNING, "Connect failed. " + ioe);
            } catch (IMTPException imtpe) {
                myLogger.log(Logger.WARNING, "BE recreation failed.");
            }
            if ((System.currentTimeMillis() - startTime) > maxDisconnectionTime) {
                handleReconnectionError("Max disconnection time expired (" + System.currentTimeMillis() + ")");
                return;
            }
            cnt++;
            waitABit(retryTime);
        }
    }

    /**
	 * Start the asynchronous reconnection process implemented in the run() method
	 */
    private void handleDisconnection() {
        synchronized (connectionLock) {
            if (!refreshingConnection) {
                refreshingConnection = true;
                if (myConnection != null) {
                    try {
                        myConnection.close();
                    } catch (Exception e) {
                    }
                    myConnection = null;
                    if (myConnectionListener != null) {
                        myConnectionListener.handleConnectionEvent(ConnectionListener.DISCONNECTED, null);
                    }
                }
                Thread t = new Thread(this);
                t.start();
            }
        }
    }

    protected void handleReconnection(Connection c) {
        synchronized (connectionLock) {
            startConnectionReader(c);
            waitingForFlush = myStub.flush();
            if (myConnectionListener != null) {
                myConnectionListener.handleConnectionEvent(ConnectionListener.RECONNECTED, null);
            }
        }
    }

    private void handleReconnectionError(String msg) {
        myLogger.log(Logger.SEVERE, "Can't reconnect: " + msg);
        if (myConnectionListener != null) {
            myConnectionListener.handleConnectionEvent(ConnectionListener.RECONNECTION_FAILURE, null);
        }
        active = false;
    }

    private void handleBENotFound() {
        if (myConnectionListener != null) {
            myConnectionListener.handleConnectionEvent(ConnectionListener.BE_NOT_FOUND, null);
        }
    }

    private void waitABit(long period) {
        try {
            Thread.sleep(period);
        } catch (Exception e) {
        }
    }

    /**
	 * Refresh the keep-alive timer.
	 * Mutual exclusion with updateKeepAlive() and updateConnectionDropDown()
	 */
    private synchronized void updateKeepAlive() {
        if (keepAliveTime > 0) {
            TimerDispatcher td = TimerDispatcher.getTimerDispatcher();
            if (kaTimer != null) {
                td.remove(kaTimer);
            }
            kaTimer = td.add(new Timer(System.currentTimeMillis() + keepAliveTime, this));
        }
    }

    /**
	 * Refresh the connection drop-down timer.
	 * Mutual exclusion with updateKeepAlive(), updateConnectionDropDown() and doTimeOut()
	 */
    private synchronized void updateConnectionDropDown() {
        if (connectionDropDownTime > 0) {
            TimerDispatcher td = TimerDispatcher.getTimerDispatcher();
            if (cdTimer != null) {
                td.remove(cdTimer);
            }
            cdTimer = td.add(new Timer(System.currentTimeMillis() + connectionDropDownTime, this));
        }
    }

    /**
	 * Mutual exclusion with updateKeepAlive(), updateConnectionDropDown() and doTimeOut()
	 */
    public synchronized void doTimeOut(Timer t) {
        if (t == kaTimer) {
            sendKeepAlive();
        } else if (t == cdTimer) {
            dropDownConnection();
        }
    }

    /**
	   Send a KEEP_ALIVE packet to the BE.
	   This is executed within a synchronized block --> Mutual exclusion
	   with dispatch() is guaranteed.
	 */
    protected void sendKeepAlive() {
    }

    /**
	 Send a DROP_DOWN packet to the BE. The latter will also close
	 the INP connection.
	 This is executed within a synchronized block --> Mutual exclusion
	 with dispatch() is guaranteed.
	 */
    private void dropDownConnection() {
        if (myConnection != null && !connectionDropped) {
            myLogger.log(Logger.INFO, "Writing DROP_DOWN request");
            JICPPacket pkt = prepareDropDownRequest();
            try {
                writePacket(pkt, myConnection);
                JICPPacket rsp = waitForResponse(-1, RESPONSE_TIMEOUT);
                myLogger.log(Logger.INFO, "DROP_DOWN response received");
                if (rsp.getType() != JICPProtocol.ERROR_TYPE) {
                    synchronized (connectionLock) {
                        try {
                            myConnection.close();
                        } catch (Exception e) {
                        }
                        myConnection = null;
                        connectionDropped = true;
                        if (myConnectionListener != null) {
                            myConnectionListener.handleConnectionEvent(ConnectionListener.DROPPED, null);
                        }
                        myLogger.log(Logger.INFO, "Connection dropped");
                    }
                } else {
                    myLogger.log(Logger.INFO, "DROP_DOWN refused");
                }
            } catch (Exception e) {
                myLogger.log(Logger.WARNING, "Exception sending DROP_DOWN request. " + e);
                handleDisconnection();
            }
        }
    }

    protected JICPPacket prepareDropDownRequest() {
        return new JICPPacket(JICPProtocol.DROP_DOWN_TYPE, JICPProtocol.DEFAULT_INFO, null);
    }

    protected void undrop() {
        connectionDropped = false;
        handleDisconnection();
    }

    private void serveCommand(JICPPacket command) {
        if (myCommandServer == null) {
            myCommandServer = new IncomingCommandServer();
            myCommandServer.start();
        }
        myCommandServer.serve(command);
    }

    /**
	 * Inner class IncomingCommandServer
	 * Serving incoming commands asynchronously is necessary to support commands whose serving process 
	 * involves issuing one or more outgoing commands. If such commands were served directly by the 
	 * ConnectionReader thread, in facts, there would be no chance to get the response to triggered 
	 * outgoing commands. 
	 */
    private class IncomingCommandServer extends Thread {

        private JICPPacket currentCommand = null;

        private JICPPacket lastResponse = null;

        public IncomingCommandServer() {
            super();
            setName("CommandServer");
        }

        public synchronized void serve(JICPPacket command) {
            try {
                while (currentCommand != null) {
                    wait();
                }
            } catch (Exception e) {
            }
            currentCommand = command;
            notifyAll();
        }

        public void run() {
            while (active) {
                acquireCurrentCommand();
                byte sid = currentCommand.getSessionID();
                if (sid == lastSid) {
                    myLogger.log(Logger.WARNING, "Duplicated command from BE: info=" + currentCommand.getInfo() + " SID=" + sid);
                } else {
                    lastResponse = handleIncomingCommand(currentCommand);
                    if (Thread.currentThread() == terminator) {
                        lastResponse.setTerminatedInfo(true);
                    }
                    lastResponse.setSessionID(sid);
                    lastSid = sid;
                }
                try {
                    writePacket(lastResponse, myConnection);
                } catch (Exception e) {
                    myLogger.log(Logger.WARNING, "Communication error sending back response. " + e);
                }
                releaseCurrentCommand();
            }
        }

        private synchronized void acquireCurrentCommand() {
            try {
                while (currentCommand == null) {
                    wait();
                }
            } catch (Exception e) {
            }
        }

        private synchronized void releaseCurrentCommand() {
            currentCommand = null;
            notifyAll();
        }
    }
}

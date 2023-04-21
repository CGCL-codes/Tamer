package net.sourceforge.rcontrol.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.sourceforge.rcontrol.base.LogListener;
import net.sourceforge.rcontrol.base.ServerCommands;
import net.sourceforge.rcontrol.query.LogHost;
import net.sourceforge.rcontrol.swing.base.ErrorDialog;

public class ServerLog {

    /**
	 * Complete log
	 */
    private LinkedList log;

    /**
	 * Buffer for next read
	 */
    private LinkedList logBuffer;

    private LogHost logHost;

    private String IPPort;

    private String logRegister;

    private String logUnregister;

    private boolean logState, logtoFile;

    private String logwritetoFile;

    private LogListener listener;

    private BufferedWriter logwriter;

    RconNotifyOptions rconNotify;

    String sayStart;

    String sayStop;

    private String lineseparator = (String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

    /**
	 * Parent server object
	 */
    private Server server;

    public ServerLog(Server server) {
        if (server == null) throw new IllegalArgumentException("server NULL not allowed !");
        this.server = server;
        this.log = new LinkedList();
        this.logBuffer = new LinkedList();
    }

    private void init() {
        String IP = null;
        if (server.queryCvar("sv_lan").equals("1")) {
            IP = Constants.getLocalIP();
        } else {
            IP = Constants.getIP();
        }
        this.IPPort = IP + ":" + logHost.getPort();
        sayStart = "started logging";
        sayStop = "stopped logging";
        if (rconNotify != null && rconNotify.isShowIP()) {
            sayStart += " from " + IP;
            sayStop += " from " + IP;
        }
        String addIP = ServerCommands.LOG_ADDIP + " " + this.IPPort;
        String delIP = ServerCommands.LOG_DELIP + " " + this.IPPort;
        if (server.queryLog() == false) {
            this.logRegister = addIP + "; " + ServerCommands.LOG_ON;
            this.logUnregister = ServerCommands.LOG_OFF + "; " + delIP;
        } else {
            this.logRegister = addIP;
            this.logUnregister = delIP;
        }
        this.logState = false;
    }

    private final List getLogBufferList() {
        LinkedList tmp = (LinkedList) this.logBuffer.clone();
        this.log.add(this.logBuffer);
        logBuffer.clear();
        return tmp;
    }

    public String getLog() {
        return convertListToString(this.log);
    }

    public String getLogBuffer() {
        LinkedList tmp = (LinkedList) this.getLogBufferList();
        return convertListToString(tmp);
    }

    private String convertListToString(LinkedList list) {
        String retVal = "";
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String tmpString = (String) it.next();
            retVal += tmpString;
        }
        return retVal;
    }

    public boolean hasLogBuffer() {
        return (!this.logBuffer.isEmpty());
    }

    public void appendLog(String log) {
        sendToLogListener(log);
        if (logtoFile) writeToLogFile(log);
        logBuffer.addLast(log);
        if (log.indexOf("Log file closed") != -1) {
            server.fireObserverUpdate(ServerMessageEnum.MSG_LOG_STOPPED);
        }
        if (log.indexOf("Started map ") != -1) {
            server.fireObserverUpdate(ServerMessageEnum.MSG_MAPCHANGED);
        }
    }

    public boolean start() {
        this.logHost = new LogHost(this);
        if (logtoFile) startLogFile();
        this.init();
        logHost.query(server.getHost(), server.getPort());
        logHost.start();
        server.send(logRegister);
        server.notify(rconNotify, sayStart);
        logState = true;
        return true;
    }

    public boolean stop() {
        server.notify(rconNotify, sayStop);
        server.send(logUnregister);
        logHost.close();
        logHost.interrupt();
        if (logtoFile) stopLogFile();
        logHost = null;
        logState = false;
        return true;
    }

    public boolean isLogging() {
        return logState;
    }

    public void setLogListener(LogListener listener) {
        this.listener = listener;
        this.logtoFile = false;
    }

    public void setLogListener(LogListener listener, String logFile) {
        this.listener = listener;
        this.logwritetoFile = logFile;
        this.logtoFile = true;
    }

    public void removeLogListener() {
        this.listener = null;
    }

    private void sendToLogListener(String log) {
        if (listener != null) listener.recieveLog(log + "\n");
    }

    private boolean startLogFile() {
        try {
            this.logwriter = new BufferedWriter(new FileWriter(logwritetoFile, true));
            logwriter.write(lineseparator + "---------Start Logging ---------" + lineseparator);
            logwriter.flush();
            return true;
        } catch (IOException e) {
            new ErrorDialog("Unable to write into File: " + logwritetoFile, ErrorDialog.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean writeToLogFile(String log2) {
        try {
            this.logwriter.write(log2 + lineseparator);
            this.logwriter.flush();
            return true;
        } catch (IOException e) {
            new ErrorDialog("Unable to write into File: " + this.logwritetoFile, ErrorDialog.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean stopLogFile() {
        try {
            this.logwriter.write(lineseparator + "--------- End  Logging ---------" + lineseparator);
            this.logwriter.close();
            return true;
        } catch (IOException e) {
            new ErrorDialog("Unable to close File: " + logwritetoFile, ErrorDialog.ERROR_MESSAGE);
            return false;
        }
    }

    public void setRconNotify(RconNotifyOptions rconNotify) {
        this.rconNotify = rconNotify;
    }
}

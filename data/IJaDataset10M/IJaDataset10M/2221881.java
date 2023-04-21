package iwork.icrafter.services.apps;

import iwork.icrafter.util.*;
import iwork.icrafter.system.*;
import iwork.state.*;
import java.io.*;
import iwork.eheap2.*;

/**
 * <b> For internal use only. </b>
 */
public class ButlerImpl extends ICrafterService implements Butler {

    String scriptsPath = null;

    String errLogFile = null;

    String debugLogFile = null;

    boolean legacy = false;

    final int MB_EVENT_TYPE = 1345;

    final String COMMAND_TO_EXECUTE = "CommandToExecute";

    final String RUN_MODE = "RunMode";

    protected void init() throws ICrafterException {
        errLogFile = getInitParameter("errorLogFile");
        debugLogFile = getInitParameter("debugLogFile");
        if (debugLogFile == null) debugLogFile = "debugLog";
        if (errLogFile == null) errLogFile = "errLog";
        scriptsPath = getInitParameter("scriptsPath");
        if (scriptsPath == null) scriptsPath = ".";
        if (getInitParameter("legacy") != null) legacy = true;
    }

    public void display(String dataURL) throws ButlerException {
        System.out.println("Butler:display called with: " + dataURL);
        browse(dataURL, "SHOWMAXIMIZED");
    }

    public void consume(String dataURL) throws ButlerException {
        System.out.println("Butler:consume called with: " + dataURL);
        display(dataURL);
    }

    public String produce() throws ButlerException {
        System.out.println("Butler:produce called!");
        return getDisplayedResource();
    }

    /**
     * Executes the passed command on the machine
     * @param cmdLine Command (with arguments) to execute
     * @param runMode Flag that indicate whether to run maximized,
     * hidden, etc 
     * @throws ButlerException
     */
    public void execute(String cmdLine, String runMode) throws ButlerException {
        try {
            System.out.println("Butler: execute called with cmdLine: " + cmdLine + " and runMode: " + runMode + " name: " + getName());
            cmdLine = cmdLine.trim();
            int index = cmdLine.indexOf(' ');
            String cmd, args;
            if (index != -1) {
                cmd = cmdLine.substring(0, index).trim();
                args = cmdLine.substring(index).trim();
                if (args.equals("")) args = null;
            } else {
                cmd = cmdLine;
                args = null;
            }
            String scriptPath = scriptsPath + "execute.py";
            Runtime rt = Runtime.getRuntime();
            Process p;
            if (args == null) {
                System.out.println("Launching python script!");
                p = rt.exec("python " + scriptPath + " " + runMode + " " + cmd);
            } else {
                p = rt.exec("python " + scriptPath + " " + runMode + " " + cmd + " " + args);
            }
            getProcessOutput(p, false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ButlerException(e);
        }
    }

    /**
     * Opens a browser on the machine with the given URL
     * @param url URL to open
     * @param runMode Flag that indicates whether to run maximized,
     * hidden, etc 
     */
    public void browse(String url, String runMode) throws ButlerException {
        execute(url, runMode);
    }

    private String getProcessOutput(Process p, boolean output) throws IOException, FileNotFoundException {
        byte[] b = new byte[500];
        InputStream pis = p.getInputStream();
        InputStream err = p.getErrorStream();
        FileOutputStream fos = null;
        int numRead = pis.read(b);
        if (debugLogFile != null) {
            fos = new FileOutputStream(debugLogFile);
        }
        ByteArrayOutputStream bst = new ByteArrayOutputStream();
        while (numRead != -1) {
            if (output) {
                bst.write(b, 0, numRead);
            }
            if (debugLogFile != null) {
                fos.write(b, 0, numRead);
            }
            numRead = pis.read(b);
        }
        pis.close();
        if (debugLogFile != null) {
            fos.close();
        }
        if (errLogFile != null) {
            fos = new FileOutputStream(errLogFile);
            numRead = err.read(b);
            while (numRead != -1) {
                Utils.debug("ButlerImpl", "Number of bytes read " + numRead);
                fos.write(b, 0, numRead);
                numRead = err.read(b);
            }
            fos.close();
        }
        if (output) return bst.toString(); else return null;
    }

    /**
     * Returns the URL displayed in the topmost browser window on the
     * machine
     * @return URL displayed in the topmost browser window.
     * Returns null if no browser window is currently open */
    public String getURL() throws ButlerException {
        try {
            String scriptPath = scriptsPath + "getURL.py";
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("python " + scriptPath);
            String url = getProcessOutput(p, true);
            Utils.debug("ButlerImpl", "URL : " + url);
            if (url.trim().startsWith("Result:")) {
                url = url.trim().substring(7).trim();
                System.out.println("Trimmed URL : " + url);
                if (!url.startsWith("http://")) {
                    return null;
                }
            } else {
                return null;
            }
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ButlerException(e);
        }
    }

    /**
     * Currently just calls getURL(). To be changed in the future
     */
    public String getDisplayedResource() throws ButlerException {
        return getURL();
    }

    protected void receivedNonOperationEvent(Event evt, EventHeap eh) throws ICrafterException {
        try {
            String evtType = evt.getEventType();
            if (evtType.equals("" + MB_EVENT_TYPE)) {
                String cmd = (String) evt.getPostValue(COMMAND_TO_EXECUTE);
                String runMode = (String) evt.getPostValue(RUN_MODE);
                if (cmd.trim().startsWith("http://")) {
                    browse(cmd, runMode);
                } else {
                    execute(cmd, runMode);
                }
            }
        } catch (Exception e) {
            throw new ICrafterException(e);
        }
    }

    /** The sole reason for the existence of this method is to account for
	legacy multibrowse events */
    public Event[] getSubscribeEvents() throws ICrafterException {
        try {
            Class[] ifs = getAdvertizeInterfaces();
            Event[] sEvents = null;
            if (legacy) sEvents = new Event[2 + ifs.length]; else sEvents = new Event[1 + ifs.length];
            for (int i = 0; i < ifs.length; i++) {
                sEvents[i] = new Event();
                sEvents[i].setFieldValue(Event.EVENTTYPE, ifs[i].getName());
                sEvents[i].addField(ICrafterConstants.EVENTCLASS, ICrafterConstants.INTERFACE_OPERATION_EVENT_CLASS);
            }
            sEvents[ifs.length] = new Event();
            sEvents[ifs.length].setFieldValue(Event.EVENTTYPE, getName());
            sEvents[ifs.length].addField(ICrafterConstants.EVENTCLASS, ICrafterConstants.OPERATION_EVENT_CLASS);
            if (legacy) {
                int targetID = 0;
                String hostName = getInitParameter("hostName");
                if (hostName.equals("iw-smartboard1") || hostName.equals("iw-smartboard1.stanford.edu")) {
                    targetID = 1;
                } else if (hostName.equals("iw-smartboard2") || hostName.equals("iw-smartboard2.stanford.edu")) {
                    targetID = 2;
                } else if (hostName.equals("iw-smartboard3") || hostName.equals("iw-smartboard3.stanford.edu")) {
                    targetID = 3;
                } else if (hostName.equals("iw-table") || hostName.equals("iw-table.stanford.edu")) {
                    targetID = 5;
                } else if (hostName.equals("iw-controlpanel") || hostName.equals("iw-controlpanel.stanford.edu")) {
                    targetID = 7;
                }
                sEvents[ifs.length + 1] = new Event();
                sEvents[ifs.length + 1].setFieldValue(Event.EVENTTYPE, "" + MB_EVENT_TYPE);
                sEvents[ifs.length + 1].setFieldValue(Event.TARGET, "" + targetID);
            }
            return sEvents;
        } catch (Exception e) {
            throw new ICrafterException(e);
        }
    }
}

package de.dgrid.wisent.gridftp.xfer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;

public class DummyTransferLogger implements ITransferLogger {

    private final Map faultDescriptions;

    private final StringBuffer log;

    private int cnt;

    public DummyTransferLogger() {
        faultDescriptions = new HashMap();
        log = new StringBuffer();
    }

    public void eraseTransfer(Transfer xfer) {
    }

    public String getLog() {
        return log.toString();
    }

    public synchronized String getFaultDescription(Transfer xfer, String faultDescrPath) {
        return (String) faultDescriptions.get(faultDescrPath);
    }

    public synchronized String logFault(Transfer xfer, Throwable t, String oldFaultDescrPath) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.close();
        String path = "fault" + cnt;
        faultDescriptions.put(path, sw.toString());
        cnt++;
        return path;
    }

    public void logTransferActionStateChange(TransferAction action) {
        StringBuffer buf = new StringBuffer();
        buf.append(action.getPath());
        buf.append(';');
        buf.append(action.getStateString());
        buf.append(';');
        buf.append(action.getTransferredSize());
        buf.append(';');
        buf.append(action.getNotBefore());
        buf.append(';');
        buf.append(action.getFaultDescrPath());
        log.append(buf.toString());
        log.append('\n');
    }

    public Transfer[] restoreTransfers(TransferQueue queue) throws CoreException {
        return new Transfer[0];
    }

    public void saveTransfer(Transfer xfer) {
    }
}

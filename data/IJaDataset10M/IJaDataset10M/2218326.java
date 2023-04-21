package net.sourceforge.psplink.launcher;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;

/**
 * @author keith
 * 
 */
public class PspProcess extends PlatformObject implements IProcess, IDebugEventSetListener {

    private final PspLinkConnection connection;

    private final String label;

    private final ILaunch launch;

    private final Map<String, String> attributes = new HashMap<String, String>();

    private final IPath mainFilePath;

    private boolean isTerminated;

    public PspProcess(final PspLinkConnection connection, IPath mainFilePath, final String label, final ILaunch launch) {
        super();
        this.connection = connection;
        this.mainFilePath = mainFilePath;
        this.label = label;
        this.launch = launch;
        listenForEvents();
        fireCreated();
    }

    private void listenForEvents() {
        DebugPlugin.getDefault().addDebugEventListener(this);
    }

    public synchronized String getAttribute(String key) {
        return attributes.get(key);
    }

    public int getExitValue() throws DebugException {
        return 0;
    }

    public String getLabel() {
        return label;
    }

    public ILaunch getLaunch() {
        return launch;
    }

    public IStreamsProxy getStreamsProxy() {
        return null;
    }

    public synchronized void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public synchronized boolean canTerminate() {
        return true;
    }

    public synchronized boolean isTerminated() {
        return isTerminated;
    }

    public synchronized void terminate() throws DebugException {
        if (!isTerminated) {
            connection.reset();
            isTerminated = true;
            fireTerminated();
        }
    }

    private void fireTerminated() {
        fireEvent(DebugEvent.TERMINATE);
    }

    private void fireCreated() {
        fireEvent(DebugEvent.CREATE);
    }

    public synchronized void run() {
        connection.runProgram(getProgramName());
    }

    private void fireEvent(int eventKind) {
        DebugEvent[] events = { new DebugEvent(this, eventKind) };
        DebugPlugin.getDefault().fireDebugEventSet(events);
    }

    public void handleDebugEvents(DebugEvent[] events) {
        for (DebugEvent e : events) {
            if (PspLinkUtils.sourceIsUsbHostFs(e) && e.getKind() == DebugEvent.TERMINATE) {
                connection.disconnect();
                isTerminated = true;
                fireTerminated();
            }
        }
    }

    PspLinkConnection getConnection() {
        return connection;
    }

    public String getProgramName() {
        return mainFilePath.makeAbsolute().toString();
    }
}

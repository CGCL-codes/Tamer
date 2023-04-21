package de.walware.statet.nico.core.runtime;

import java.io.IOException;
import org.eclipse.debug.core.model.IStreamsProxy;

/**
 * None buffered streams
 */
public class ToolStreamProxy implements IStreamsProxy {

    private final ToolStreamMonitor fInputMonitor = new ToolStreamMonitor();

    private final ToolStreamMonitor fInfoMonitor = new ToolStreamMonitor();

    private final ToolStreamMonitor fOutputMonitor = new ToolStreamMonitor();

    private final ToolStreamMonitor fErrorMonitor = new ToolStreamMonitor();

    public ToolStreamProxy() {
    }

    public void write(final String input) throws IOException {
        throw new IOException("Function is not supported.");
    }

    public ToolStreamMonitor getInputStreamMonitor() {
        return fInputMonitor;
    }

    public ToolStreamMonitor getInfoStreamMonitor() {
        return fInfoMonitor;
    }

    public ToolStreamMonitor getOutputStreamMonitor() {
        return fOutputMonitor;
    }

    public ToolStreamMonitor getErrorStreamMonitor() {
        return fErrorMonitor;
    }

    /**
	 * 
	 */
    public void dispose() {
        fInputMonitor.dispose();
        fInfoMonitor.dispose();
        fOutputMonitor.dispose();
        fErrorMonitor.dispose();
    }
}

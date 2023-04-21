package org.tigr.cloe.view.traceManager;

import java.awt.event.ActionListener;
import org.tigr.cloe.controller.traceManager.TraceListener;
import org.tigr.cloe.model.traceManager.TraceFetcherWatcher;
import org.tigr.cloe.model.traceManager.TraceManager;
import org.tigr.seq.display.IWindow;
import org.tigr.seq.display.IWindowListener;
import org.tigr.seq.seqdata.IBaseAssemblySequence;
import org.tigr.seq.seqdata.display.IAssemblyDisplayPreferences;

public interface ITraceManagerWindow extends IWindow, IWindowListener, ActionListener, TraceListener {

    public abstract TraceManager getTraceManager();

    public abstract void killThread(String threadName);

    public abstract boolean checkIfRunning(String name);

    public abstract void traceFinish(TraceFetcherWatcher traceThatIsFinished);

    public abstract void fetchThread(IBaseAssemblySequence seq, IAssemblyDisplayPreferences displayPrefs);

    public abstract void removeThread(TraceFetcherWatcher t);
}

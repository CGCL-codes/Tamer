package net.sourceforge.eclipsex.debugger;

import java.util.List;
import net.sourceforge.eclipsex.loader.ClassName;
import org.eclipse.core.runtime.IProgressMonitor;

public interface EXDebugger {

    public boolean connect(EXDebugTarget debugTarget, final IProgressMonitor monitor) throws Exception;

    public void setBreakPoint(final ClassName className, final int line) throws Exception;

    public void removeBreakPoint(final ClassName className, final int line) throws Exception;

    public void resume() throws Exception;

    public void stepInto() throws Exception;

    public void stepOver() throws Exception;

    public void stepOut() throws Exception;

    public void disconnect() throws Exception;

    public void terminate() throws Exception;

    public boolean isTerminated();

    public boolean isSuspended();

    public void suspend() throws Exception;

    public boolean isConnected();

    public List<EXStackFrame> getFrames(EXThread thread) throws Exception;
}

package as.ide.core.debug.model;

import java.util.ArrayList;
import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;
import as.ide.core.utils.Synchronizer;
import as.ide.core.utils.Tools;

public class ASStackFrame extends ASDebugElement implements IStackFrame {

    private IThread fThread;

    private String context;

    private boolean fTerminated;

    private ASDebugTarget fDebugTarget;

    private IFile fFile;

    private int lineNumber;

    private String qualifiedClassName;

    private String fObjectCode;

    public ASStackFrame(ASDebugTarget target, ASThread thread, String context) {
        super(target);
        this.fDebugTarget = target;
        this.fThread = thread;
        this.context = context;
        parseContext(context);
    }

    private void parseContext(String context) {
        int start = context.lastIndexOf("at ") + 3;
        int end = context.lastIndexOf(":");
        String name = context.substring(start, end);
        fFile = fDebugTarget.resolveFile(name);
        this.lineNumber = Integer.parseInt(context.substring(end + 1));
        String mark = "[Object";
        start = context.indexOf(mark) + mark.length();
        end = context.indexOf(',', start);
        this.fObjectCode = context.substring(start, end).trim();
        mark = "class='";
        start = context.indexOf(mark) + mark.length();
        end = context.indexOf("\'", start);
        qualifiedClassName = context.substring(start, end).replace("::", ".");
    }

    @Override
    public int getCharEnd() throws DebugException {
        return -1;
    }

    @Override
    public int getCharStart() throws DebugException {
        return -1;
    }

    @Override
    public int getLineNumber() throws DebugException {
        return lineNumber;
    }

    @Override
    public String getName() throws DebugException {
        return context;
    }

    public String toString() {
        return context;
    }

    @Override
    public IRegisterGroup[] getRegisterGroups() throws DebugException {
        return null;
    }

    @Override
    public IThread getThread() {
        return fThread;
    }

    @Override
    public IVariable[] getVariables() throws DebugException {
        if (fTerminated) {
            return new IVariable[0];
        }
        final ArrayList<IVariable> list = new ArrayList<IVariable>();
        final Synchronizer syn = new Synchronizer();
        IStreamHandler handler = new IStreamHandler() {

            @Override
            public void handleText(String text) {
                String[] ss = Tools.getSepLines(text);
                if (ss != null) {
                    for (String s : ss) {
                        int start = s.indexOf("=");
                        if (start <= 0) continue;
                        String name = s.substring(0, start).trim();
                        String value = s.substring(start + 1).trim();
                        list.add(new ASVariable(fDebugTarget, name, value));
                    }
                }
                syn.doNotify();
            }
        };
        fDebugTarget.sendCommand("i a", handler);
        syn.doWait(1000);
        fDebugTarget.sendCommand("i l", handler);
        syn.doWait(1000);
        return list.toArray(new IVariable[0]);
    }

    @Override
    public boolean hasRegisterGroups() throws DebugException {
        return false;
    }

    @Override
    public boolean hasVariables() throws DebugException {
        return true;
    }

    @Override
    public boolean canStepInto() {
        return this.fThread.canStepInto();
    }

    @Override
    public boolean canStepOver() {
        return this.fThread.canStepOver();
    }

    @Override
    public boolean canStepReturn() {
        return fThread.canStepReturn();
    }

    @Override
    public boolean isStepping() {
        return fThread.isStepping();
    }

    @Override
    public void stepInto() throws DebugException {
        this.fThread.stepInto();
    }

    @Override
    public void stepOver() throws DebugException {
        this.fThread.stepOver();
    }

    @Override
    public void stepReturn() throws DebugException {
        this.fThread.stepReturn();
    }

    @Override
    public boolean canResume() {
        return this.fThread.canResume();
    }

    @Override
    public boolean canSuspend() {
        return fThread.canSuspend();
    }

    @Override
    public boolean isSuspended() {
        return fThread.isSuspended();
    }

    @Override
    public void resume() throws DebugException {
        this.fThread.resume();
    }

    @Override
    public void suspend() throws DebugException {
        this.fThread.suspend();
    }

    @Override
    public boolean canTerminate() {
        return !fTerminated;
    }

    @Override
    public boolean isTerminated() {
        return fTerminated;
    }

    @Override
    public void terminate() throws DebugException {
        if (!fTerminated) {
            fTerminated = true;
            this.fThread.terminate();
        }
    }

    public boolean equalsTo(String context) {
        return this.context.equals(context);
    }

    public IFile getFile() {
        return fFile;
    }
}

package hub.sam.mof.simulator.debug.model;

import hub.sam.mof.simulator.util.M3ActionsHelper;
import java.util.ArrayList;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;
import M3Actions.MAction;
import M3Actions.MActivityNode;
import M3Actions.MPlace;
import M3Actions.Runtime.MContext;

/**
 * @author Michael Soden
 */
public class M3StackFrame extends M3DebugElement implements IStackFrame {

    private final M3Thread thread;

    private String name;

    private final MContext context;

    public M3StackFrame(M3Thread thread, MContext context) {
        super(thread.getDebugTarget());
        this.thread = thread;
        this.context = context;
        MActivityNode currentNode = context.getCurrentNode();
        if (currentNode != null) this.name = M3ActionsHelper.getQualifiedName(currentNode); else this.name = "<unknown>";
    }

    public IThread getThread() {
        return thread;
    }

    public int getLineNumber() throws DebugException {
        return -1;
    }

    public String getName() throws DebugException {
        return name;
    }

    public IVariable[] getVariables() throws DebugException {
        ArrayList<IVariable> result = new ArrayList<IVariable>();
        MActivityNode currentNode = context.getCurrentNode();
        if (currentNode instanceof MAction) {
            MAction action = (MAction) currentNode;
            for (MPlace place : M3ActionsHelper.getInputPlaces(action)) {
                IVariable var = new M3Variable(this, place);
                result.add(var);
            }
            for (MPlace place : M3ActionsHelper.getOutputPlaces(action)) {
                IVariable var = new M3Variable(this, place);
                result.add(var);
            }
        }
        return result.toArray(new IVariable[result.size()]);
    }

    public boolean hasVariables() throws DebugException {
        return true;
    }

    public void resume() throws DebugException {
        thread.resume();
    }

    public void suspend() throws DebugException {
        thread.suspend();
    }

    public void stepInto() throws DebugException {
        thread.stepInto();
    }

    public void stepOver() throws DebugException {
        thread.stepOver();
    }

    public void stepReturn() throws DebugException {
        thread.stepReturn();
    }

    public void terminate() throws DebugException {
        thread.terminate();
    }

    public boolean canResume() {
        return thread.canResume();
    }

    public boolean canSuspend() {
        return thread.canSuspend();
    }

    public boolean isSuspended() {
        return thread.isSuspended();
    }

    public boolean canTerminate() {
        return thread.canTerminate();
    }

    public boolean isTerminated() {
        return thread.isTerminated();
    }

    public boolean canStepInto() {
        return thread.canStepInto();
    }

    public boolean canStepOver() {
        return thread.canStepOver();
    }

    public boolean canStepReturn() {
        return thread.canStepReturn();
    }

    public boolean isStepping() {
        return thread.isStepping();
    }

    public int getCharEnd() throws DebugException {
        return -1;
    }

    public int getCharStart() throws DebugException {
        return -1;
    }

    public boolean hasRegisterGroups() throws DebugException {
        return false;
    }

    public IRegisterGroup[] getRegisterGroups() throws DebugException {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((thread == null) ? 0 : thread.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final M3StackFrame other = (M3StackFrame) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (thread == null) {
            if (other.thread != null) return false;
        } else if (!thread.equals(other.thread)) return false;
        return true;
    }

    /**
	 * @return the context
	 */
    public MContext getContext() {
        return context;
    }
}

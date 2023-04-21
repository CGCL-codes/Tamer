package com.aptana.ide.debug.test;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;
import com.aptana.ide.debug.core.model.IJSStackFrame;

/**
 * @author Max Stepanov
 */
public abstract class AbstractVariableInstruction implements IInstruction {

    private IThread thread;

    private int frameId;

    private String variableName;

    /**
	 * 
	 */
    protected AbstractVariableInstruction(IThread thread, int frameId, String variableName) {
        this.thread = thread;
        this.frameId = frameId;
        this.variableName = variableName;
    }

    protected final IVariable getVariable() throws DebugException {
        IStackFrame frame = null;
        if (frameId <= 0) {
            frame = thread.getTopStackFrame();
        } else {
            IStackFrame[] frames = thread.getStackFrames();
            if (frames.length > frameId) {
                frame = frames[frameId];
            } else {
                System.out.println("No such stack frame #" + frameId);
                return null;
            }
        }
        if (!(frame instanceof IJSStackFrame)) {
            System.out.println("Invalid stack frame");
            return null;
        }
        return ((IJSStackFrame) frame).findVariable(variableName);
    }
}

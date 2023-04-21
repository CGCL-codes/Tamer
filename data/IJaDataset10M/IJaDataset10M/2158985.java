package org.sodbeans.compiler.api;

import org.openide.filesystems.FileObject;

/**
 *
 * @author Andreas Stefik
 */
public abstract class CompilerEvent {

    private int breakpointToggleLine;

    private int breakpointToggleCaretPosition;

    private FileObject file;

    /**
     * Determines whether this event indicates that the code is executing
     * forward.
     * @return
     */
    public boolean isExecuteEvent() {
        return false;
    }

    /**
     * Determines whether this event is indicating a virtual machine stop
     * message.
     * @return
     */
    public boolean isVirtualMachineStopError() {
        return false;
    }

    /**
     * Indicates that the debugger is starting.
     * @return
     */
    public boolean isDebuggerStartEvent() {
        return false;
    }

    /**
     * Indicating that the debugger is stopping.
     * @return
     */
    public boolean isDebuggerStopEvent() {
        return false;
    }

    /**
     * If this flag is true, then force the current debugger engine to die.
     * 
     * @return
     */
    public boolean isForceKillDebuggerEngineEvent() {
        return false;
    }

    /**
     * This event is fired if the program has terminated, but that the
     * termination was ended by the virtual machine, not by user request.
     * 
     * @return
     */
    public boolean isNaturalTerminationEvent() {
        return false;
    }

    /**
     * Indicates that a breakpoint has been toggled.
     * @return
     */
    public boolean isBreakpointToggleEvent() {
        return false;
    }

    /**
     * Gives a message regarding the execution of the program.
     * @return
     */
    public String getExecutionMessage() {
        return "Step backward auditory debugging is not yet implemented.";
    }

    /**
     * Gives a message regarding executing the code in reverse.
     * @return
     */
    public String getUnexecutionMessage() {
        return "Step backward auditory debugging is not yet implemented.";
    }

    /**
     * @return the breakpointToggleLine
     */
    public int getBreakpointToggleLine() {
        return breakpointToggleLine;
    }

    public int getBreakpointToggleCarretPosition() {
        return breakpointToggleCaretPosition;
    }

    /**
     * Returns the file in which the code is currently being executed.
     * @return
     */
    public FileObject getFile() {
        return file;
    }

    /**
     * Sets the file that is currently being executed
     * 
     * @param f
     */
    public void setFile(FileObject f) {
        file = f;
    }

    /**
     * @param breakpointToggleCaretPosition the breakpointToggleCaretPosition to set
     */
    public void setBreakpointToggleLine(int line) {
        breakpointToggleLine = line;
    }

    public void setBreakpointToggleCarretPosition(int carretPos) {
        breakpointToggleCaretPosition = carretPos;
    }

    /**
     * Determines whether this CompilerEvent is signifying that the
     * code was recently built.
     * @return
     */
    public boolean isBuildEvent() {
        return false;
    }

    /**
     * Signifies when the compiler event is a build event that
     * builds all of the code.
     * @return
     */
    public boolean isBuildAllEvent() {
        return false;
    }

    public boolean requiresHighlight() {
        return true;
    }

    public boolean isBuildSuccessful() {
        return false;
    }
}

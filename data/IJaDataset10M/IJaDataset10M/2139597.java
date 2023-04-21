package org.norecess.nolatte.primitives.system;

import org.norecess.nolatte.ast.IText;
import org.norecess.nolatte.interpreters.InterpreterException;
import org.norecess.nolatte.system.IProcess;

public class ProcessOutputException extends InterpreterException {

    private static final long serialVersionUID = 1740156669415524932L;

    private final IProcess myProcess;

    private final IText myErrors;

    public ProcessOutputException(IProcess process, IText errors) {
        super();
        myProcess = process;
        myErrors = errors;
    }

    public IProcess getProcess() {
        return myProcess;
    }

    public IText getErrors() {
        return myErrors;
    }
}

package tefkat.plugin.debug;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IRegister;
import org.eclipse.debug.core.model.IRegisterGroup;

public class DebugRegister extends DebugVariable implements IRegister {

    public DebugRegister(AbstractStackFrame frame, Object var, Object val) {
        super(frame, var, val);
    }

    public IRegisterGroup getRegisterGroup() throws DebugException {
        return null;
    }
}

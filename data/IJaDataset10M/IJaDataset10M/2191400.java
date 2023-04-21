package org.jactr.tools.async.message.command.state;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.tools.async.message.BaseMessage;

/**
 * @author developer
 */
public class RuntimeStateCommand extends BaseMessage implements IRuntimeStateCommand, Serializable {

    /**
   * 
   */
    private static final long serialVersionUID = 8174291191453013555L;

    /**
   * logger definition
   */
    private static final Log LOGGER = LogFactory.getLog(RuntimeStateCommand.class);

    State _state;

    boolean _suspendImmediately = false;

    /**
   * assumes State.START
   * 
   * @param suspendImmediately
   */
    public RuntimeStateCommand(boolean suspendImmediately) {
        this(State.START);
        _suspendImmediately = suspendImmediately;
    }

    public RuntimeStateCommand(State requestedState) {
        _state = requestedState;
    }

    /**
   * @see org.jactr.tools.async.message.command.state.IRuntimeStateCommand#getState()
   */
    public State getState() {
        return _state;
    }

    public boolean shouldSuspendImmediately() {
        return _suspendImmediately;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(getClass().getSimpleName()).append(":").append(_state).append(",").append(_suspendImmediately).append("]");
        return sb.toString();
    }
}

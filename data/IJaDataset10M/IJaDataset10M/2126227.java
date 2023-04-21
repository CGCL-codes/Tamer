package L1;

import L1.MessageEvent;
import L1.Operation;

/**
 * A receive operation event specifies the event of receiving an operation invocation for a particular operation by the target entity.
 */
public interface ReceiveOperationEvent extends MessageEvent {

    /**
    * The operation associated with this event.
    */
    public abstract Operation getOperation();

    /**
    * The operation associated with this event.
    */
    public abstract void setOperation(Operation operation);
}

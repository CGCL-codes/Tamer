package org.activebpel.rt.bpel.impl.activity.wsio.consume;

import org.activebpel.rt.bpel.AeBusinessProcessException;
import org.activebpel.rt.bpel.IAeVariable;
import org.activebpel.rt.bpel.impl.activity.assign.AeAtomicCopyOperationContext;
import org.activebpel.rt.bpel.impl.activity.wsio.AeAnonymousMessageVariable;
import org.activebpel.rt.message.AeMessagePartsMap;
import org.activebpel.rt.message.IAeMessageData;

/**
 * The base class for message data consumers that provides access to a delegate
 * {@link org.activebpel.rt.bpel.impl.activity.wsio.consume.IAeMessageDataConsumerContext}.
 */
public abstract class AeAbstractMessageDataConsumer implements IAeMessageDataConsumer {

    /**
    * The copy operation context that accesses variables in the BPEL object and
    * that can rollback changes made made to those variables.
    */
    private AeAtomicCopyOperationContext mAtomicCopyOperationContext;

    /**
    * Constructs a message data consumer with access to the given delegate
    * message data consumer context.
    *
    */
    protected AeAbstractMessageDataConsumer() {
    }

    /**
    * Returns an anonymous variable that wraps the given message data.
    *
    * @param aMessageData
    */
    protected IAeVariable createAnonymousVariable(IAeMessageData aMessageData, AeMessagePartsMap aMap) throws AeBusinessProcessException {
        IAeVariable variable = new AeAnonymousMessageVariable(aMap);
        variable.setMessageData(aMessageData);
        return variable;
    }

    /**
    * Returns the copy operation context that accesses variables in the BPEL
    * object and that can rollback changes made to those variables.
    */
    protected AeAtomicCopyOperationContext getAtomicCopyOperationContext(IAeMessageDataConsumerContext aContext) {
        if (mAtomicCopyOperationContext == null) {
            mAtomicCopyOperationContext = new AeAtomicCopyOperationContext(aContext.getBpelObject());
        }
        return mAtomicCopyOperationContext;
    }
}

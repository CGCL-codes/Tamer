package org.activebpel.rt.bpel.impl.activity.wsio.consume;

import org.activebpel.rt.bpel.IAeVariable;
import org.activebpel.rt.bpel.def.activity.AeActivityReceiveDef;
import org.activebpel.rt.bpel.def.activity.IAeMessageDataConsumerDef;
import org.activebpel.rt.bpel.impl.AeAbstractBpelObject;
import org.activebpel.rt.bpel.impl.activity.AeActivityReceiveImpl;

/**
 * Implements access to a <code>receive</code> activity for a message data
 * consumer.
 */
public class AeActivityReceiveConsumerContext implements IAeMessageDataConsumerContext {

    /** The <code>receive</code> activity implementation object. */
    private final AeActivityReceiveImpl mReceiveImpl;

    /**
    * Constructs the context for the given <code>receive</code> activity
    * implementation object.
    *
    * @param aReceiveImpl
    */
    public AeActivityReceiveConsumerContext(AeActivityReceiveImpl aReceiveImpl) {
        mReceiveImpl = aReceiveImpl;
    }

    /**
    * Returns the <code>receive</code> activity definition object.
    */
    protected AeActivityReceiveDef getDef() {
        return (AeActivityReceiveDef) getReceiveImpl().getDefinition();
    }

    /**
    * Returns the <code>receive</code> activity implementation object.
    */
    protected AeActivityReceiveImpl getReceiveImpl() {
        return mReceiveImpl;
    }

    /**
    * @see org.activebpel.rt.bpel.impl.activity.wsio.consume.IAeMessageDataConsumerContext#getBpelObject()
    */
    public AeAbstractBpelObject getBpelObject() {
        return getReceiveImpl();
    }

    /**
    * @see org.activebpel.rt.bpel.impl.activity.wsio.consume.IAeMessageDataConsumerContext#getVariable()
    */
    public IAeVariable getVariable() {
        return getReceiveImpl().findVariable(getDef().getVariable());
    }

    /**
    * @see org.activebpel.rt.bpel.impl.activity.wsio.consume.IAeMessageDataConsumerContext#getMessageConsumerDef()
    */
    public IAeMessageDataConsumerDef getMessageConsumerDef() {
        return getDef();
    }
}

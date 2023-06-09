package org.argouml.model.euml;

import org.argouml.model.MessageSort;

/**
 * The implementation of the OrderingKindEUMLImpl.java for EUML2.
 */
class MessageSortEUMLImpl implements MessageSort {

    public Object getASynchCall() {
        return org.eclipse.uml2.uml.MessageSort.ASYNCH_CALL_LITERAL;
    }

    public Object getCreateMessage() {
        return org.eclipse.uml2.uml.MessageSort.CREATE_MESSAGE_LITERAL;
    }

    public Object getDeleteMessage() {
        return org.eclipse.uml2.uml.MessageSort.DELETE_MESSAGE_LITERAL;
    }

    public Object getReply() {
        return org.eclipse.uml2.uml.MessageSort.REPLY_LITERAL;
    }

    public Object getSynchCall() {
        return org.eclipse.uml2.uml.MessageSort.SYNCH_CALL_LITERAL;
    }

    public Object getASynchSignal() {
        return org.eclipse.uml2.uml.MessageSort.ASYNCH_SIGNAL_LITERAL;
    }
}

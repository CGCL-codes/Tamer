package org.babbly.core.protocol.sip.event;

import java.util.HashMap;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.header.CSeqHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class SipGeneralListener implements SipListener {

    public enum Handler {

        CALL, REGISTRATION
    }

    ;

    protected HashMap<Handler, SipListener> listeners = new HashMap<Handler, SipListener>();

    public void addListener(Handler handler, SipListener listener) {
        listeners.put(handler, listener);
    }

    private SipListener getListenerForRequest(String method) {
        if (method == Request.INVITE || method == Request.ACK || method == Request.CANCEL || method == Request.BYE) {
            return listeners.get(Handler.CALL);
        } else if (method == Request.REGISTER) {
            return listeners.get(Handler.REGISTRATION);
        }
        return null;
    }

    public void removeListeners() {
        listeners.clear();
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {
    }

    @Override
    public void processRequest(RequestEvent requestEvent) {
        System.out.println("general REQ:" + requestEvent.getRequest());
        Request request = requestEvent.getRequest();
        String method = request.getMethod();
        SipListener listener = getListenerForRequest(method);
        listener.processRequest(requestEvent);
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        Response response = responseEvent.getResponse();
        CSeqHeader cSeq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
        String method = cSeq.getMethod();
        SipListener listener = getListenerForRequest(method);
        listener.processResponse(responseEvent);
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        Transaction transaction = null;
        if (timeoutEvent.isServerTransaction()) {
            transaction = timeoutEvent.getServerTransaction();
        } else {
            transaction = timeoutEvent.getClientTransaction();
        }
        Request request = transaction.getRequest();
        String method = request.getMethod();
        SipListener listener = getListenerForRequest(method);
        listener.processTimeout(timeoutEvent);
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        Transaction transaction = null;
        if (transactionTerminatedEvent.isServerTransaction()) {
            transaction = transactionTerminatedEvent.getServerTransaction();
        } else {
            transaction = transactionTerminatedEvent.getClientTransaction();
        }
        Request request = transaction.getRequest();
        String method = request.getMethod();
        SipListener listener = getListenerForRequest(method);
        listener.processTransactionTerminated(transactionTerminatedEvent);
    }
}

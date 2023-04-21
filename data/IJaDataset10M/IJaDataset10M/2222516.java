package net.jsrb.runtime.impl.request;

import net.jsrb.runtime.request.*;
import net.jsrb.util.BufferHolder;

public class TopServiceRequestBuilderImpl implements TopServiceRequestBuilder {

    RequestMgrImpl transactionMgr;

    private int clientId;

    private BufferHolder bufferHolder;

    private String serviceName;

    private int timeout;

    private TopServiceRequestNotifier notifier;

    private boolean autoClose = true;

    private long topReqId;

    public TopServiceRequestBuilderImpl(RequestMgrImpl transactionMgr) {
        this.transactionMgr = transactionMgr;
        timeout = transactionMgr.getTopreqTimeout();
    }

    public void setTopRequestId(long topReqId) {
        this.topReqId = topReqId;
    }

    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    public TopServiceRequest createTopRequest() {
        long id = topReqId;
        if (id == 0L) {
            id = transactionMgr.nextSerial();
        }
        TopServiceRequestImpl result = new TopServiceRequestImpl(clientId, id);
        result.setTimeout(timeout);
        result.setNotifier(notifier);
        result.setServiceName(serviceName);
        result.setBufferHolder(bufferHolder);
        result.setAutoClose(autoClose);
        return result;
    }

    public void setNotifier(TopServiceRequestNotifier notifier) {
        this.notifier = notifier;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setRequestBuffer(BufferHolder bufferHolder) {
        this.bufferHolder = bufferHolder;
    }

    public void setTopServiceName(String name) {
        this.serviceName = name;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}

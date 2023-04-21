package org.opennms.sms.reflector.smsservice;

import org.opennms.protocols.rt.Response;

/**
 * SmsResponse
 *
 * @author brozow
 */
public abstract class MobileMsgResponse implements Response {

    private MobileMsgRequest m_request;

    private long m_receiveTime;

    public MobileMsgResponse(long receiveTime) {
        m_receiveTime = receiveTime;
    }

    public void setRequest(MobileMsgRequest req) {
        m_request = req;
    }

    public MobileMsgRequest getRequest() {
        return m_request;
    }

    public long getReceiveTime() {
        return m_receiveTime;
    }

    public abstract String getText();
}

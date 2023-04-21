package org.opennms.api.integration.reporting;

import java.io.Serializable;

public class DeliveryOptions implements Serializable {

    private static final long serialVersionUID = 7983363859009905407L;

    protected String m_mailTo;

    protected Boolean m_persist;

    protected Boolean m_canPersist;

    protected Boolean m_sendMail;

    protected String m_mailFormat;

    public String getMailTo() {
        return m_mailTo;
    }

    public void setMailTo(String email) {
        m_mailTo = email;
    }

    public String getMailFormat() {
        return m_mailFormat;
    }

    public void setMailFormat(String format) {
        m_mailFormat = format;
    }

    public void setCanPersist(Boolean canPersist) {
        m_canPersist = canPersist;
    }

    public Boolean getCanPersist() {
        return m_canPersist;
    }

    public void setPersist(Boolean persist) {
        m_persist = persist;
    }

    public Boolean getPersist() {
        return m_persist;
    }

    public void setSendMail(Boolean sendEmail) {
        m_sendMail = sendEmail;
    }

    public Boolean getSendMail() {
        return m_sendMail;
    }
}

package org.openuss.messaging;

import java.util.Date;

/**
 *@author Ingo Dueppe
 */
public interface Recipient extends org.openuss.foundation.DomainObject {

    public Long getId();

    public void setId(Long id);

    public org.openuss.messaging.SendState getState();

    public void setState(org.openuss.messaging.SendState state);

    public Date getSend();

    public void setSend(Date send);

    public String getEmail();

    public void setEmail(String email);

    public String getLocale();

    public void setLocale(String locale);

    public String getSms();

    public void setSms(String sms);

    public org.openuss.messaging.MessageJob getJob();

    public void setJob(org.openuss.messaging.MessageJob job);

    public abstract boolean hasSmsNotification();
}

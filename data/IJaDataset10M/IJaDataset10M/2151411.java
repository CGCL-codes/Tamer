package com.voztele.sipspy;

public interface SpySessionListener {

    public void messageEvent(SpySession ss, SipMessage tm, boolean lateMessage);

    public void sessionEvent(SpySession ss);
}

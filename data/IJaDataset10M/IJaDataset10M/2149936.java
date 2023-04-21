package org.mobicents.slee.util;

import javax.naming.NamingException;

public interface SipUtilsFactory {

    public SipUtils getSipUtils() throws NamingException;
}

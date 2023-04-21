package com.jvantage.ce.presentation.ejb;

import javax.ejb.Remote;

/**
 *
 * @author Owner
 */
@Remote
public interface PageContextUtilRemote {

    public void sendUserToApplicationAccessDeniedPage(com.jvantage.ce.presentation.PageContext pageContext) throws com.jvantage.ce.presentation.PresentationException;

    public void sendUserToApplicationLogoutPage(com.jvantage.ce.presentation.PageContext pageContext) throws com.jvantage.ce.presentation.PresentationException;

    public void sendUserToApplicationStoppedPage(com.jvantage.ce.presentation.PageContext pageContext) throws com.jvantage.ce.presentation.PresentationException;

    public void sendUserToHomePage(com.jvantage.ce.presentation.PageContext pageContext) throws com.jvantage.ce.presentation.PresentationException;

    public void sendUserToInvalidLicensePage(com.jvantage.ce.presentation.PageContext pageContext) throws com.jvantage.ce.presentation.PresentationException;

    public void sendUserToLoginPage(com.jvantage.ce.presentation.PageContext pageContext) throws com.jvantage.ce.presentation.PresentationException;
}

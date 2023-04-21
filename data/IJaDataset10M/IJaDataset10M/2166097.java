package com.phloc.webbasics.login;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.string.StringHelper;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.domain.ISessionWebScope;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.app.html.HTMLResponseHelper;
import com.phloc.webbasics.app.html.IHTMLProvider;
import com.phloc.webbasics.security.login.LoggedInUserManager;

/**
 * Handle the application login process.
 * 
 * @author philip
 */
public class LoginManager {

    private static final String SESSION_ATTR_AUTHINPROGRESS = "$authinprogress";

    /**
   * Create the HTML code used to render the login screen
   * 
   * @param bLoginError
   *        If <code>true</code> an error occurred in a previous login action
   * @return Never <code>null</code>.
   */
    @OverrideOnDemand
    protected IHTMLProvider createLoginScreen(final boolean bLoginError) {
        return new BasicLoginHTML(bLoginError);
    }

    @Nonnull
    public final EContinue checkUserAndShowLogin(@Nonnull final IRequestWebScope aRequestScope) throws ServletException {
        String sSessionUserID = LoggedInUserManager.getInstance().getCurrentUserID();
        if (sSessionUserID == null) {
            boolean bLoginError = false;
            final ISessionWebScope aSessionScope = WebScopeManager.getSessionScope();
            if (Boolean.TRUE.equals(aSessionScope.getAttributeObject(SESSION_ATTR_AUTHINPROGRESS))) {
                final String sUserID = aRequestScope.getAttributeAsString(CLogin.REQUEST_ATTR_USERID);
                final String sPassword = aRequestScope.getAttributeAsString(CLogin.REQUEST_ATTR_PASSWORD);
                if (LoggedInUserManager.getInstance().loginUser(sUserID, sPassword).isSuccess()) {
                    aSessionScope.removeAttribute(SESSION_ATTR_AUTHINPROGRESS);
                    sSessionUserID = sUserID;
                } else {
                    bLoginError = StringHelper.hasText(sUserID) || StringHelper.hasText(sPassword);
                }
            }
            if (sSessionUserID == null) {
                aSessionScope.setAttribute(SESSION_ATTR_AUTHINPROGRESS, Boolean.TRUE);
                HTMLResponseHelper.createHTMLResponse(aRequestScope, createLoginScreen(bLoginError));
            }
        }
        return EContinue.valueOf(sSessionUserID != null);
    }
}

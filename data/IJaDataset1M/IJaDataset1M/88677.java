package com.netx.ebs;

import javax.servlet.http.Cookie;
import com.netx.data.Connection;
import com.netx.data.DatabaseException;

class AuthenticationChecker implements SecurityChecker {

    public AuthenticationChecker() {
        super();
    }

    public void check(EbsRequest request, EbsResponse response) throws SecurityCheckException, DatabaseException {
        EbsContext ebsCtx = request.getEbsContext();
        Cookie[] cookies = Cookies.getCookies(request);
        if (cookies[0] == null) {
            throw new NotAuthenticatedException();
        } else if (cookies[1] == null) {
            String clientID = cookies[0].getValue();
            NotificationMessage nm = ebsCtx.internalGetSessionManager().getNotificationMessage(clientID);
            if (nm != null) {
                ebsCtx.internalGetSessionManager().removeNotificationMessage(clientID);
                throw new SessionTerminatedException(nm);
            } else {
                throw new NotAuthenticatedException();
            }
        } else {
            String clientID = cookies[0].getValue();
            Session session = ebsCtx.internalGetSessionManager().getSession(clientID);
            if (session == null) {
                if (ebsCtx.getKeepSessionsOnReload()) {
                    Connection c = request.getDatabaseConnection();
                    User user = Entities.getUsers(c).find(new Long(cookies[1].getValue()));
                    session = ebsCtx.internalGetSessionManager().createSession(clientID, request.getRemoteAddr(), user);
                    c.close();
                } else {
                    throw new NotAuthenticatedException();
                }
            }
            Cookies.refreshCookies(cookies, session.getUser(), response);
            request.setAttribute(Constants.REQUEST_SESSION, session);
        }
    }
}

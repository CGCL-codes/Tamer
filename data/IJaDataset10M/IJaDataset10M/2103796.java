package org.jrest4guice.security;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jrest4guice.security.exception.LoginErrorException;

public class SecurityHelper {

    public synchronized void login(HttpClient httpClient, String securityHost, String loginUrl, String userName, String password) throws Exception {
        Exception exception = null;
        GetMethod authget = null;
        PostMethod authpost = null;
        GetMethod redirect = null;
        try {
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            authget = new GetMethod(securityHost + loginUrl);
            httpClient.executeMethod(authget);
            int statuscode = authget.getStatusCode();
            authpost = new PostMethod(securityHost + "/j_security_check");
            NameValuePair j_username = new NameValuePair("j_username", userName);
            NameValuePair j_password = new NameValuePair("j_password", password);
            authpost.setRequestBody(new NameValuePair[] { j_username, j_password });
            httpClient.executeMethod(authpost);
            statuscode = authpost.getStatusCode();
            if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER) || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                Header header = authpost.getResponseHeader("location");
                if (header != null) {
                    String newuri = header.getValue();
                    if ((newuri == null) || (newuri.equals(""))) {
                        newuri = "/";
                    }
                    redirect = new GetMethod(newuri);
                    httpClient.executeMethod(redirect);
                    redirect.releaseConnection();
                }
            } else {
                exception = new LoginErrorException("用户名或者密码错误");
            }
        } catch (Exception e) {
            exception = e;
        } finally {
            if (authget != null) authget.releaseConnection();
            if (authpost != null) authpost.releaseConnection();
            if (redirect != null) redirect.releaseConnection();
        }
        if (exception != null) throw exception;
    }
}

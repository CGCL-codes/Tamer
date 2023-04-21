package edu.pitt.dbmi.odie.gapp.gwt.server.user;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.logging.Logger;
import com.allen_sauer.gwt.log.client.Log;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pitt.dbmi.odie.gapp.gwt.client.user.ODIE_LoginInfo;
import edu.pitt.dbmi.odie.gapp.gwt.client.user.ODIE_LoginService;
import edu.pitt.dbmi.odie.gapp.gwt.server.navigator.ODIE_SelectionCacher;
import edu.pitt.dbmi.odie.gapp.gwt.server.util.gdata.ODIE_DocsServiceFetcher;
import edu.pitt.dbmi.odie.gapp.gwt.server.util.gdata.ODIE_GDataDocsService;
import edu.pitt.dbmi.odie.gapp.gwt.server.util.umls.ODIE_UmlsService;
import edu.pitt.dbmi.odie.gapp.gwt.server.util.umls.ODIE_UmlsServiceFetcher;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ODIE_LoginServiceImpl extends RemoteServiceServlet implements ODIE_LoginService {

    private static final Logger log = Logger.getLogger(ODIE_LoginServiceImpl.class.getName());

    public ODIE_LoginInfo login(String requestUri, String xmlizedClientParams) {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        ODIE_LoginInfo loginInfo = new ODIE_LoginInfo();
        ODIE_ServerSideLoginInfo serverSideLoginInfo = (ODIE_ServerSideLoginInfo) this.getServletContext().getAttribute(ODIE_ServerSideLoginInfo.class.getName());
        if (serverSideLoginInfo != null) {
            loginInfo.setLoggedIn(true);
            loginInfo.setEmailAddress(serverSideLoginInfo.getEmailAddress());
            loginInfo.setNickname(serverSideLoginInfo.getNickName());
            String logoutUrl = userService.createLogoutURL(requestUri + "odie_client/signout", "webodie.net");
            logoutUrl = requestUri + "odie_client/signout";
            log.info("ODIE_LoginServiceImpl logoutUrl ==> " + logoutUrl);
            loginInfo.setLogoutUrl(logoutUrl);
        } else {
            loginInfo.setLoggedIn(false);
            loginInfo.setLoginUrl("/odie_client/signin?action=loginWebOdie");
        }
        return loginInfo;
    }

    private String createTwiceRedirectedURL(String nextUrl) {
        UserService userService = UserServiceFactory.getUserService();
        String protocol = "http";
        String domain = "www.google.com";
        String hostedDomain = "webodie.net";
        String scope = "http://docs.google.com/feeds/";
        boolean secure = false;
        boolean session = true;
        log.info("nextUrl ==> " + nextUrl);
        String authSubUrl = AuthSubUtil.getRequestUrl(protocol, domain, hostedDomain, nextUrl, scope, secure, session);
        try {
            log.info("authSubUrl ==> " + URLDecoder.decode(authSubUrl, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            ;
        }
        String loginUrl = userService.createLoginURL(authSubUrl, "webodie.net");
        try {
            log.info("loginUrl ==> " + URLDecoder.decode(loginUrl, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            ;
        }
        return loginUrl;
    }

    public ODIE_LoginInfo loginLocal(String requestUri, String xmlizedClientParams) {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        ODIE_LoginInfo loginInfo = new ODIE_LoginInfo();
        ODIE_ServerSideLoginInfo serverSideLoginInfo = new ODIE_ServerSideLoginInfo();
        if (user != null) {
            loginInfo.setLoggedIn(true);
            loginInfo.setEmailAddress(user.getEmail());
            loginInfo.setNickname(user.getNickname());
            loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
            serverSideLoginInfo.setEmailAddress(loginInfo.getEmailAddress());
            serverSideLoginInfo.setXmlizedClientParams(xmlizedClientParams);
            serverSideLoginInfo.setXmlizedInitParams(xmlizeInitParameters());
            serverSideLoginInfo.cacheParameters();
            if (serverSideLoginInfo.getAuthSubSessionToken() != null || serverSideLoginInfo.getPassword() != null) {
                ODIE_GDataDocsService docsService = ODIE_DocsServiceFetcher.fetchOrCreateDocsService(this, serverSideLoginInfo);
                ODIE_SelectionCacher selectionCacher = ODIE_SelectionCacher.fetchOrCreateOdieSelectionCacher(this);
                selectionCacher.fetchOrCreateUser(user.getEmail(), user.getEmail());
                selectionCacher.setLoginInfo(serverSideLoginInfo);
                this.getServletContext().setAttribute(ODIE_ServerSideLoginInfo.class.getName(), serverSideLoginInfo);
            }
        } else {
            loginInfo.setLoggedIn(false);
            loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
            ODIE_DocsServiceFetcher.releaseDocsService(this);
            ODIE_SelectionCacher.releaseOdieSelectionCacher(this);
        }
        return loginInfo;
    }

    private String createOnlyAuthsubRequestURL(String requestUri) {
        String nextUrl = requestUri + "odie_client/authz_gdocs";
        String scope = "http://docs.google.com/feeds/";
        boolean secure = false;
        boolean session = true;
        String authSubUrl = AuthSubUtil.getRequestUrl("webodie.net", nextUrl, scope, secure, session);
        return authSubUrl;
    }

    private String createOnceRedirectedURL(String nextUrl) {
        UserService userService = UserServiceFactory.getUserService();
        String result = userService.createLoginURL(nextUrl, "webodie.net");
        ;
        return result;
    }

    public ODIE_LoginInfo loginOriginal(String requestUri, String xmlizedClientParams) {
        Log.debug(xmlizedClientParams);
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        ODIE_LoginInfo loginInfo = new ODIE_LoginInfo();
        ODIE_ServerSideLoginInfo serverSideLoginInfo = new ODIE_ServerSideLoginInfo();
        if (user != null) {
            loginInfo.setLoggedIn(true);
            loginInfo.setEmailAddress(user.getEmail());
            loginInfo.setNickname(user.getNickname());
            loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri + "signout", "webodie.net"));
            serverSideLoginInfo.setEmailAddress(loginInfo.getEmailAddress());
            serverSideLoginInfo.setXmlizedClientParams(xmlizedClientParams);
            serverSideLoginInfo.setXmlizedInitParams(xmlizeInitParameters());
            serverSideLoginInfo.cacheParameters();
            if (serverSideLoginInfo.getAuthSubSessionToken() != null || serverSideLoginInfo.getPassword() != null) {
                ODIE_GDataDocsService docsService = ODIE_DocsServiceFetcher.fetchOrCreateDocsService(this, serverSideLoginInfo);
                ODIE_SelectionCacher selectionCacher = ODIE_SelectionCacher.fetchOrCreateOdieSelectionCacher(this);
                selectionCacher.fetchOrCreateUser(user.getEmail(), user.getEmail());
                selectionCacher.setLoginInfo(serverSideLoginInfo);
            }
        } else {
            loginInfo.setLoggedIn(false);
            loginInfo.setLoginUrl(userService.createLoginURL(requestUri + "signin", "webodie.net"));
            ODIE_DocsServiceFetcher.releaseDocsService(this);
            ODIE_SelectionCacher.releaseOdieSelectionCacher(this);
        }
        return loginInfo;
    }

    private void establishUmlsConnectivity(ODIE_GDataDocsService docsService, ODIE_ServerSideLoginInfo serverSideLoginInfo) {
        String umlsLicense = docsService.pullUmlsLicenseContents();
        ODIE_UmlsService umlsService = ODIE_UmlsServiceFetcher.fetchOrCreateUmlsService(this, serverSideLoginInfo);
        umlsService.setLicense(umlsLicense);
        umlsService.fetchUmlLicense();
    }

    @SuppressWarnings("unchecked")
    private String xmlizeInitParameters() {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\"?>");
        sb.append("<params>");
        for (Enumeration<String> initParamNamesEnum = this.getInitParameterNames(); initParamNamesEnum.hasMoreElements(); ) {
            String initParamKey = initParamNamesEnum.nextElement();
            String initParamValue = this.getInitParameter(initParamKey);
            sb.append("<param>");
            sb.append("<key>");
            sb.append(initParamKey);
            sb.append("</key>");
            sb.append("<value>");
            sb.append(initParamValue);
            sb.append("</value>");
            sb.append("</param>");
        }
        sb.append("</params>");
        return sb.toString();
    }
}

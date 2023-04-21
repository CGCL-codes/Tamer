package org.brucalipto.loginplugin;

import org.brucalipto.loginplugin.util.HttpClient;

public interface RemoteLoggable {

    String URL_PARAM = "url";

    String USERNAME_PARAM = "usernameURLParam";

    String PASSWD_PARAM = "passwordURLParam";

    String GOOD_LOGIN_PARAM = "goodlogin";

    String TIMEOUT_PARAM = "timeout";

    String ENCODING_PARAM = "encoding";

    String REFERRER_PARAM = "referrer";

    String USER_AGENT_PARAM = "useragent";

    String COOKIE_POLICY_PARAM = "cookiepolicy";

    long DEFAULT_TIMEOUT_VALUE = HttpClient.DEFAULT_TIMEOUT;

    String DEFAULT_ENCODING_VALUE = "UTF-8";

    String DEFAULT_COOKIE_POLICY_VALUE = HttpClient.DEFAULT_COOKIE_POLICY;

    boolean login(String login, String password) throws RemoteLoginException;

    String getConfiguration();

    String getPluginName();

    String getPluginDescription();
}

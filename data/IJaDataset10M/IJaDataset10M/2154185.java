package com.google.jplurk.net;

import com.google.jplurk.ISettings;
import com.google.jplurk.org.apache.commons.lang.StringUtils;

/**
 * Provide the setting of proxy.
 * Using setProvider() or setAuthInfo() for Programmatically Setting.
 * The priority from high to low is: Programmatically, Dynamic, Default Setting.
 * @author askeing_yen
 */
public class ProxyProvider {

    static boolean isProgrammaticallySetHost = false;

    static boolean isProgrammaticallySetAuth = false;

    static String host = "";

    static int port = 80;

    static String user = "";

    static String password = "";

    /**
     * Setup proxy host with host and port.
     * @param host
     * @param port
     */
    public static void setProvider(String host, int port) {
        setProvider(host);
        ProxyProvider.port = port;
        isProgrammaticallySetHost = true;
    }

    /**
     * Setup proxy host.
     * @param host
     */
    public static void setProvider(String host) {
        ProxyProvider.host = host;
        isProgrammaticallySetHost = true;
    }

    /**
     * Setup proxy auth info
     * @param user
     * @param password
     */
    public static void setAuthInfo(String user, String password) {
        ProxyProvider.user = user;
        ProxyProvider.password = password;
        isProgrammaticallySetAuth = true;
    }

    /**
     * Load the default proxy setting in proprety file.
     * If user already set ProxyProvider, this method will do nothing.
     * Default proxy setting: default_proxy_host, default_proxy_port, default_proxy_user, default_proxy_password in property file.
     * @param config
     */
    public static void loadDefaultProxy(ISettings config) {
        if (!ProxyProvider.isProgrammaticallySetHost) {
            String defaultProxyHost = config.getDefaultProxyHost();
            int defaultProxyPort = config.getDefaultProxyPort();
            if (StringUtils.isNotBlank(defaultProxyHost)) {
                ProxyProvider.host = defaultProxyHost;
                if (defaultProxyPort != 80) {
                    ProxyProvider.port = defaultProxyPort;
                }
            }
        }
        if (!ProxyProvider.isProgrammaticallySetAuth) {
            String defaultProxyUser = config.getDefaultProxyUser();
            String defaultProxyPassword = config.getDefaultProxyPassword();
            if (StringUtils.isNotBlank(defaultProxyUser)) {
                ProxyProvider.user = defaultProxyUser;
            }
            if (StringUtils.isNotBlank(defaultProxyPassword)) {
                ProxyProvider.password = defaultProxyPassword;
            }
        }
    }

    /**
     * Load the dynamic proxy setting.
     * If user already set ProxyProvider, this method will do nothing.
     * Usage: java -DproxyHost=http_proxy.mydomain.com -DproxyPort=8080 -DproxyUser=name -DproxyPassword=pwd your.plurk.agent.Main
     */
    public static void loadDynamicProxy() {
        if (!ProxyProvider.isProgrammaticallySetHost) {
            String dynamicProxyHost = System.getProperty("proxyHost");
            int dynamicProxyPort = 80;
            try {
                dynamicProxyPort = Integer.valueOf(System.getProperty("proxyPort"));
            } catch (NumberFormatException e) {
            }
            if (StringUtils.isNotBlank(dynamicProxyHost)) {
                ProxyProvider.host = dynamicProxyHost;
                if (dynamicProxyPort != 80) {
                    ProxyProvider.port = dynamicProxyPort;
                }
            }
        }
        if (!ProxyProvider.isProgrammaticallySetAuth) {
            String dynamicProxyUser = System.getProperty("proxyUser");
            String dynamicProxyPassword = System.getProperty("proxyPassword");
            if (StringUtils.isNotBlank(dynamicProxyUser)) {
                ProxyProvider.user = dynamicProxyUser;
            }
            if (StringUtils.isNotBlank(dynamicProxyPassword)) {
                ProxyProvider.password = dynamicProxyPassword;
            }
        }
    }

    /**
     * @return proxy host. If have no setting, then return "".
     */
    public static String getHost() {
        return ProxyProvider.host;
    }

    /**
     * @return proxy port. The default value is 80.
     */
    public static int getPort() {
        return ProxyProvider.port;
    }

    /**
     * @return username of Auth Proxy. If have no setting, then return "".
     */
    public static String getUser() {
        return ProxyProvider.user;
    }

    /**
     * @return password of Auth Proxy. If have no setting, then return "".
     */
    public static String getPassword() {
        return ProxyProvider.password;
    }
}

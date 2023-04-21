package com.rapidminer.tools;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

/**
 * Handles XMLRPC connections to BugZilla.
 * 
 * @author Marco Boeck
 */
public class XmlRpcHandler {

    public static final String BUGZILLA_URL = "http://bugs.rapid-i.com";

    private static final String BUGZILLA_APPENDIX = "xmlrpc.cgi";

    /**
	 * Handles the login to a given BugZilla XmlRpc server.
	 * 
	 * @param serverURL the URL to the server, e.g. "http://my.bug-server.com"
	 * @param login the BugZilla login
	 * @param password the BugZilla password
	 * @return the logged in XmlRpcClient instance
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */
    public static synchronized XmlRpcClient login(String serverURL, String login, char[] password) throws MalformedURLException, XmlRpcException {
        String server;
        if (serverURL.endsWith("/")) {
            server = serverURL + BUGZILLA_APPENDIX;
        } else {
            server = serverURL + "/" + BUGZILLA_APPENDIX;
        }
        HttpClient httpClient = new HttpClient();
        XmlRpcClient rpcClient = new XmlRpcClient();
        XmlRpcCommonsTransportFactory factory = new XmlRpcCommonsTransportFactory(rpcClient);
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        factory.setHttpClient(httpClient);
        rpcClient.setTransportFactory(factory);
        config.setServerURL(new URL(server));
        rpcClient.setConfig(config);
        Map<String, String> loginMap = new HashMap<String, String>();
        loginMap.put("login", login);
        loginMap.put("password", new String(password));
        loginMap.put("rememberlogin", "true");
        Map resultMap = (Map) rpcClient.execute("User.login", new Object[] { loginMap });
        LogService.getRoot().fine("Logged into BugZilla at '" + serverURL + "' as user '" + resultMap.get("id") + "'.");
        for (int i = 0; i < password.length; i++) {
            password[i] = 0;
        }
        return rpcClient;
    }
}

package tbaclient;

import java.io.*;
import java.net.*;

public class NetwConfig {

    private static boolean blnProxyOn;

    private static String proxy;

    private static int proxyPort;

    private static String user;

    private static String pass;

    private static int timeOut;

    public NetwConfig() {
        proxy = null;
        proxyPort = 8080;
        user = null;
        pass = null;
        timeOut = 30;
        blnProxyOn = false;
    }

    public NetwConfig(String newProxy, int newProxyPort) {
        proxy = newProxy;
        proxyPort = newProxyPort;
        user = null;
        pass = null;
        timeOut = 30;
        blnProxyOn = true;
    }

    public static void initialise() {
        proxy = null;
        proxyPort = 8080;
        user = null;
        pass = null;
        timeOut = 30;
        blnProxyOn = false;
    }

    public static Socket getProxySocket() {
        Socket s = null;
        InetSocketAddress ISockAddressProxy = null;
        ISockAddressProxy = new InetSocketAddress(proxy, proxyPort);
        try {
            s = new Socket(new Proxy(Proxy.Type.SOCKS, ISockAddressProxy));
        } catch (Exception e) {
            System.out.println("problem making proxy or socket before connect: ");
            System.out.println("exception: " + e.getMessage());
        }
        return s;
    }

    public static Socket getProxySocket(String newProxy, int newProxyPort) {
        Socket s = null;
        InetSocketAddress ISockAddressProxy = null;
        ISockAddressProxy = new InetSocketAddress(newProxy, newProxyPort);
        try {
            s = new Socket(new Proxy(Proxy.Type.DIRECT, ISockAddressProxy));
        } catch (Exception e) {
            System.out.println("problem making proxy or socket before connect: ");
            System.out.println("exception: " + e.getMessage());
        }
        return s;
    }

    public static String getProxy() {
        return proxy;
    }

    public static void setProxy(String newProxy) {
        proxy = newProxy;
    }

    public static void setProxyPort(int newProxyPort) {
        proxyPort = newProxyPort;
    }

    public static int getProxyPort() {
        return proxyPort;
    }

    public static void setUser(String t) {
        user = t;
    }

    public static String getUser() {
        return user;
    }

    public static void setPass(String t) {
        pass = t;
    }

    public static String getPass() {
        return pass;
    }

    public static void setTimeOut(int newTimeOut) {
        timeOut = newTimeOut;
    }

    public static int getTimeOut() {
        return timeOut;
    }

    public static void Authenticate() {
        Authenticator.setDefault(new ProxyAuthenticator(user, pass));
    }

    public static void Authenticate(String newUser, String newPass) {
        Authenticator.setDefault(new ProxyAuthenticator(newUser, newPass));
    }

    public static boolean isProxyOn() {
        return blnProxyOn;
    }

    public static void setProxyOn(boolean blnNew) {
        blnProxyOn = blnNew;
    }
}

class ProxyAuthenticator extends Authenticator {

    private String user;

    private String pass;

    public ProxyAuthenticator(String username, String password) {
        this.user = username;
        this.pass = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, pass.toCharArray());
    }
}

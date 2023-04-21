package migool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import migool.GlobalOptions;

/**
 * @author Denis Migol
 *
 */
public final class NetUtil {

    public static String getPage(URL url, Proxy proxy) {
        try {
            StringBuffer ret = new StringBuffer("");
            URLConnection connection = (proxy == null) ? url.openConnection() : url.openConnection(proxy);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                ret.append(line + StringUtil.CRLF);
            }
            return ret.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public static String getPage(URL url) {
        return getPage(url, null);
    }

    public static String getPage(String url) {
        try {
            return getPage(new URL(url));
        } catch (Exception e) {
            return null;
        }
    }

    public static String getPage(String url, Proxy proxy) {
        try {
            return getPage(new URL(url), proxy);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getPage(String url, String proxy) {
        try {
            ProxyUtil.Proxy p = ProxyUtil.parse(proxy);
            return getPage(new URL(url), new Proxy(Proxy.Type.HTTP, new InetSocketAddress(p.getHost(), p.getPort())));
        } catch (Exception e) {
            return null;
        }
    }

    public static URL newURL(String url) {
        try {
            if (GlobalOptions.isProxySetted()) {
                return new URL("http", GlobalOptions.getProxyHost(), GlobalOptions.getProxyPort(), url);
            } else {
                return new URL(url);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void main(String[] args) {
        migool.GlobalOptions.setProxyServer("127.0.0.1:8081");
        System.out.println(getPage(newURL("http://www.redtube.com/?page=463")));
    }
}

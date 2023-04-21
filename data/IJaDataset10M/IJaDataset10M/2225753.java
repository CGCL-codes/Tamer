package org.hyk.proxy.framework.util;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import org.hyk.proxy.android.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hyk.util.net.NetUtil;

/**
 *
 */
public class CommonUtil {

    protected static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static URLConnection openRemoteDescriptionFile(String urlstr) throws MalformedURLException {
        URL url = new URL(urlstr);
        try {
            URLConnection conn = url.openConnection();
            conn.connect();
            return conn;
        } catch (Exception e) {
            Config conf = Config.getInstance();
            SimpleSocketAddress localServAddr = conf.getLocalProxyServerAddress();
            Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(localServAddr.host, localServAddr.port));
            URLConnection conn;
            try {
                conn = url.openConnection(proxy);
                conn.connect();
                return conn;
            } catch (IOException e1) {
                logger.error("Failed to retrive desc file:" + url, e1);
            }
        }
        return null;
    }

    public static File downloadFile(String url, File destPath) {
        try {
            return NetUtil.downloadFile(new URL(url), destPath);
        } catch (Exception e) {
            logger.error("Failed to download file:" + url, e);
            Config conf = Config.getInstance();
            SimpleSocketAddress localServAddr = conf.getLocalProxyServerAddress();
            Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(localServAddr.host, localServAddr.port));
            try {
                return NetUtil.downloadFile(proxy, new URL(url), destPath);
            } catch (Exception e1) {
                logger.error("Failed to download file:" + url, e1);
            }
        }
        return null;
    }
}

package com.samsung.mproject.util;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProxyUtil {

    private static Log logger = LogFactory.getLog(ProxyUtil.class);

    private static final int BUF_SIZE = 1024;

    private static final String PROXY_HOST = "168.219.61.250";

    private static final int PROXY_PORT = 8080;

    public static String getRemoteResult(String url) {
        String response = "";
        try {
            HttpClient client = new HttpClient();
            client.getHostConfiguration().setProxy(PROXY_HOST, PROXY_PORT);
            GetMethod method = new GetMethod(url);
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                InputStreamReader reader = new InputStreamReader(method.getResponseBodyAsStream(), "utf-8");
                Writer writer = new StringWriter();
                int len = 0;
                char[] buf = new char[BUF_SIZE];
                while ((len = reader.read(buf, 0, BUF_SIZE)) != -1) {
                    writer.write(buf, 0, len);
                }
                response = writer.toString();
                writer.close();
                reader.close();
            }
            if (method != null) {
                method.releaseConnection();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = "";
        }
        return response;
    }

    public static String getRemoteResultPost(String api, String param) {
        String response = "";
        try {
            HttpClient client = new HttpClient();
            client.getHostConfiguration().setProxy(PROXY_HOST, PROXY_PORT);
            PostMethod method = new PostMethod(api);
            String[] params = param.split("&");
            for (int i = 0; i < params.length; ++i) {
                String[] token = params[i].split("=");
                method.addParameter(token[0], token[1]);
            }
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                InputStreamReader reader = new InputStreamReader(method.getResponseBodyAsStream(), "utf-8");
                Writer writer = new StringWriter();
                int len = 0;
                char[] buf = new char[BUF_SIZE];
                while ((len = reader.read(buf, 0, BUF_SIZE)) != -1) {
                    writer.write(buf, 0, len);
                }
                response = writer.toString();
                writer.close();
                reader.close();
            }
            if (method != null) {
                method.releaseConnection();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = "";
        }
        return response;
    }
}

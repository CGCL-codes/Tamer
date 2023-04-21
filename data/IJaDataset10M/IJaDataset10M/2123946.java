package com.nitbcn.curl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import utils.CoreUtils;

/**
 *
 * @author raimon
 */
public class Curl {

    private HttpURLConnection con;

    private String url;

    private String method;

    private int mlisecondstimeout;

    public Curl(String url, String method, int mlisecondstimeout) {
        this.setUrl(url);
        this.setMethod(method);
        this.setMlisecondstimeout(mlisecondstimeout);
        loadUrl(url);
    }

    public Curl(String url, String method) {
        this.setUrl(url);
        this.setMethod(method);
        this.setMlisecondstimeout(DEFAULT_TIMEOUT);
        loadUrl(url);
    }

    public Curl(String url) {
        this.setUrl(url);
        this.setMethod(DEFAULT_METHOD);
        this.setMlisecondstimeout(DEFAULT_TIMEOUT);
        loadUrl(url);
    }

    public Curl() {
    }

    private void loadUrl(String url) {
        try {
            System.out.println("url: " + url);
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setConnectTimeout(mlisecondstimeout);
            con.setRequestMethod(method);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int putParameterMap(Map params) {
        try {
            String data = "";
            Iterator it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                data += (data.length() > 0) ? "&" : "";
                data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(CoreUtils.collapseArray((String[]) params.get(key)), "UTF-8");
            }
            System.out.println("data: " + data);
            con.getOutputStream().write(data.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public int putParameterMap(Map params, String extrakey, String extravalue) {
        try {
            String data = "";
            Iterator it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                data += (data.length() > 0) ? "&" : "";
                data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(CoreUtils.collapseArray((String[]) params.get(key)), "UTF-8");
            }
            data += (data.length() > 0) ? "&" : "";
            data += URLEncoder.encode(extrakey, "UTF-8") + "=" + URLEncoder.encode(extravalue, "UTF-8");
            System.out.println("data: " + data);
            con.getOutputStream().write(data.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public int putParameters(Hashtable<String, String> params) {
        try {
            String data = "";
            Enumeration<String> senum = params.keys();
            while (senum.hasMoreElements()) {
                String key = senum.nextElement();
                data += (data.length() > 0) ? "&" : "";
                data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8");
            }
            con.getOutputStream().write(data.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    public String getContent() {
        StringBuffer sb = new StringBuffer();
        try {
            DataInputStream inputStream = new DataInputStream(con.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while (br.ready()) {
                sb.append(br.readLine());
            }
            inputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the mlisecondstimeout
     */
    public int getMlisecondstimeout() {
        return mlisecondstimeout;
    }

    /**
     * @param mlisecondstimeout the mlisecondstimeout to set
     */
    public void setMlisecondstimeout(int mlisecondstimeout) {
        this.mlisecondstimeout = mlisecondstimeout;
    }

    public static int DEFAULT_TIMEOUT = 10000;

    public static String DEFAULT_METHOD = "POST";
}

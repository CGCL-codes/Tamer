package com.yxl.util.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Title:http 连接工具类
 * <p>Copyright: Copyright 2010 Shenzhen Taiji SoftwareCorparation
 * <p>Company: 深圳太极软件有限公司
 * <p>CreateTime: Feb 24, 2011
 * @author 杨雪令
 * @version 1.0
 */
public class HttpConnection {

    String REGIP = "([0-9]{1,3})(.[0-9]{1,3}){3}";

    /**
	 * 发送请求并接收返回数据 Socket 方式
	 * @param host 访问地址 localhost
	 * @param port 访问端口号 8080
	 * @param url 访问路径 /dzjc_portal/rest/dzjc/supervise/xzxk/attention/AttentionList
	 * @param type 访问类型 GET,POST,PUT,DELETE
	 * @throws IOException 
	 */
    public String sendOfSocket(String host, int port, String url, String type, String charSet) throws IOException {
        StringBuffer context = new StringBuffer();
        BufferedWriter wr = null;
        InetAddress addr = InetAddress.getByName(host);
        System.out.println(addr);
        Socket socket = new Socket(addr, port);
        wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charSet));
        type = type.toUpperCase().trim();
        wr.write(type + " " + url + " HTTP/1.1\r\n");
        wr.write("Host:" + host + "\r\n");
        wr.write("Accept: */*\r\n");
        wr.write("Accept-Language: zh-cn\r\n");
        wr.write("Accept-Encoding: gzip,deflate\r\n");
        wr.write("User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.8)\r\n");
        wr.write("Connection: keep-alive\r\n\r\n");
        wr.write("\r\n");
        wr.flush();
        InputStream in = socket.getInputStream();
        context = this.inputStreamToString(in, charSet);
        if (wr != null) wr.close();
        return context.toString();
    }

    /**
	 * 发送请求并接收返回数据 URLConnection 方式
	 * @param address 访问地址 localhost
	 * @param type 访问类型 GET,POST,PUT,DELETE
	 * @param charSet 字符编码
	 * @throws IOException 
	 */
    public String sendOfURLConn(String address, String type, String charSet) throws IOException {
        return sendOfURLConn(address, type, charSet, null);
    }

    /**
	 * 发送请求并接收返回数据 URLConnection 方式
	 * @param address 访问地址 localhost
	 * @param type 访问类型 GET,POST,PUT,DELETE
	 * @param charSet 字符编码
	 * @param proxy 代理
	 * @throws IOException 
	 * 
	 */
    public String sendOfURLConn(String address, String type, String charSet, Proxy proxy) throws IOException {
        StringBuffer context = new StringBuffer();
        HttpURLConnection connection = null;
        URL url = new URL(address);
        if (proxy == null) connection = (HttpURLConnection) url.openConnection(); else connection = (HttpURLConnection) url.openConnection(proxy);
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
        type = type.toUpperCase().trim();
        connection.setRequestMethod(type);
        InputStream in = connection.getInputStream();
        context = this.inputStreamToString(in, charSet);
        connection = null;
        return context.toString();
    }

    /**
	 * <p>Description: 设置代理
	 * <p>Copyright　深圳太极软件公司
	 * @param proxyIP 代理IP
	 * @param proxyPort 代理端口
	 * @return 设置成功返回true
	 * @author  杨雪令
	 */
    public boolean setProxyOfSys(String proxyIP, int proxyPort) {
        if (getDataOfReg(REGIP, proxyIP) != null) {
            proxyIP = getDataOfReg(REGIP, proxyIP);
        } else {
            return false;
        }
        if (proxyPort > 60000 || proxyPort < 1) {
            return false;
        }
        System.getProperties().setProperty("proxySet", "true");
        System.getProperties().setProperty("http.proxyHost", proxyIP);
        System.getProperties().setProperty("http.proxyPort", proxyPort + "");
        return true;
    }

    /**
	 * <p>Description: 设置代理
	 * <p>Copyright　深圳太极软件公司
	 * @param proxyIP 代理IP
	 * @param proxyPort 代理端口
	 * @return 设置成功返回true
	 * @author  杨雪令
	 */
    public Proxy createProxy(String proxyIP, int proxyPort) {
        if (getDataOfReg(REGIP, proxyIP) != null) {
            proxyIP = getDataOfReg(REGIP, proxyIP);
        } else {
            return null;
        }
        if (proxyPort > 60000 || proxyPort < 1) {
            return null;
        }
        SocketAddress proxyAddress = new InetSocketAddress(proxyIP, proxyPort);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
        return proxy;
    }

    /**
	 * <p>Description: 根据正则表达式检验字符串，如果正确则返回字符串，否则返回 null
	 * <p>Copyright　深圳太极软件公司
	 * @author  杨雪令
	 */
    public String getDataOfReg(String reg, String str) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher;
        matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return null;
        }
    }

    /**
	 * <p>Copyright　深圳太极软件公司
	 * @param inputStream
	 * @param charSet 字符编码
	 * @throws IOException 
	 * @author  杨雪令
	 */
    public StringBuffer inputStreamToString(InputStream inputStream, String charSet) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, charSet));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
            buffer.append("\r\n");
        }
        in.close();
        return buffer;
    }

    /**
	 * <p>Description:测试
	 * <p>Copyright　深圳太极软件公司
	 * @param args 
	 * @author  杨雪令
	 */
    public static void main(String[] args) {
        HttpConnection wu = new HttpConnection();
        String result = "";
        String type = "get";
        String address = "http://ip138.com/ip2city.asp";
        String ip = "80.191.94.1";
        int port = 80;
        try {
            wu.setProxyOfSys(ip, port);
            result = wu.sendOfURLConn(address, type, "gb2312");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}

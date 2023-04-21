package edu.fudan.cse.medlab.event.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class Client {

    public static String proxyhost;

    public static int proxyport;

    public static String proxyusername;

    public static String proxypassword;

    public static String getPage(String urlstring, String folder, String appendname) throws Exception {
        HttpClient client = new HttpClient();
        if (proxyhost != null && !proxyhost.equals("")) {
            client.getHostConfiguration().setProxy(proxyhost, proxyport);
        }
        if (proxyusername != null && !proxyusername.equals("")) {
            client.getState().setProxyCredentials(AuthScope.ANY, new UsernamePasswordCredentials(proxyusername, proxypassword));
        }
        client.getParams().setAuthenticationPreemptive(true);
        client.getParams().setHttpElementCharset("GBK");
        URI uri = null;
        try {
            uri = new URI(urlstring, true, "GBK");
        } catch (URIException e) {
            uri = new URI(urlstring, false, "GBK");
        }
        if (uri == null) return null;
        GetMethod get = null;
        InputStream in = null;
        FileOutputStream out = null;
        String html = "";
        try {
            get = new GetMethod();
            get.setURI(uri);
            get.setFollowRedirects(true);
            client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20 * 1000);
            client.getHttpConnectionManager().getParams().setConnectionTimeout(30 * 1000);
            if (client.executeMethod(get) != HttpStatus.SC_OK) {
                System.err.println("Connection to " + urlstring + "  failed: " + get.getStatusLine());
                return null;
            }
            if (folder != null) {
                System.out.println("connecting to " + urlstring);
                edu.fudan.cse.medlab.event.extraction.DB.addLog("connecting to " + urlstring);
                String localurl = urlstring.substring(urlstring.indexOf("//") + 2);
                localurl = localurl.replace("/", "\\");
                localurl = localurl.replace("?", "@");
                if (appendname == null) {
                    while (localurl.endsWith("\\")) localurl = localurl.substring(0, localurl.length() - 1);
                }
                String filepath = folder + localurl;
                if (appendname != null) filepath += "\\" + appendname;
                if (filepath.endsWith("\\index.php")) filepath = filepath.replace("\\index.php", "\\indexpage");
                File f = new File(filepath);
                File folderfile = new File(f.getParent());
                if (folderfile.exists() && folderfile.isFile()) {
                    folderfile.renameTo(new File(f.getParent() + "(index)"));
                }
                if (f.exists() && f.isDirectory()) {
                    filepath += "(index)";
                }
                if (!f.exists()) {
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                }
                out = new FileOutputStream(filepath);
                System.out.println("downloading  " + urlstring + "     as  " + filepath);
                edu.fudan.cse.medlab.event.extraction.DB.addLog("downloading  " + urlstring + "     as  " + filepath);
                in = get.getResponseBodyAsStream();
                byte[] bytes = new byte[1024];
                int c, total = 0;
                while ((c = in.read(bytes)) != -1 && total < 1000) {
                    out.write(bytes, 0, c);
                    html += new String(bytes, 0, c, "gbk");
                    total++;
                }
                if (html.contains("charset=utf-8")) html = new String(html.getBytes("gbk"), "utf-8");
            } else {
                in = get.getResponseBodyAsStream();
                byte[] bytes = new byte[1024];
                int c, total = 0;
                while ((c = in.read(bytes)) != -1 && total < 1000) {
                    html += new String(bytes, 0, c, "gbk");
                    total++;
                }
                if (html.contains("charset=utf-8")) html = new String(html.getBytes("gbk"), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (get != null) get.releaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return html;
    }

    public static void main(String[] args) {
        try {
            Proxy.setProxy();
            String s = Client.getPage("http://sh.piaohang.net/helpcenter.asp", "E:\\jmj\\", "testpage1");
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

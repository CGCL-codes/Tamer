package com.patientis.framework.utility;

import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.cookie.Cookie;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonNode;
import com.patientis.framework.locale.SystemUtil;
import com.patientis.framework.logging.Log;
import com.patientis.model.common.Converter;

/**
	 * 1) export server certificate to server.crt
	 * 2) keytool -import -alias "my server cert" -file server.crt -keystore my.truststore
	 *    (use password: password)
	 * 3) keytool -genkey -v -alias "my client key" -validity 365 -keystore my.keystore
	 * 4) keytool -certreq -alias "my client key" -file mycertreq.csr -keystore my.keystore
	 *    * sign certificate by verisign or Open SSL
	 * 5) keytool -import -alias "api-3t.sandbox.paypal.com" -file caroot.crt -keystore my.keystore
	 * 6) keytool -import -alias "my client key" -file mycert.p7 -keystore my.keystore
	 * 7) keytool -list -v -keystore my.keystore
	 * 
	 * 
	 * *** new ones execute 5)
 * 
 * @author gcaulton
 *
 */
public class HttpUtil {

    private static File defaultKeyStore = new File("C:\\dev\\patientis\\trunk\\content\\surescripts\\ssl\\surescripts.truststore");

    private static String defaultKeyStorePassword = "patientos";

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        String url = "https://files-cert.rxhub.net/webdav/Formulary/PBMB/COV/ALPBMB_20100111_20100111";
        File tempFile = SystemUtil.getTemporaryFile();
        System.out.println(FileSystemUtil.getTextContents(tempFile));
    }

    /**
	 * 
	 * @param hostname
	 * @param contents
	 * @return
	 */
    public static String executePost(String hostname, List<NameValuePair> params) throws Exception {
        return executePost(hostname, params, null);
    }

    /**
	 * 
	 * @param hostname
	 * @param contents
	 * @return
	 */
    public static String executePost(String hostname, List<NameValuePair> params, Cookie cookies) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getCookieStore().addCookie(cookies);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(getPost(hostname, params), responseHandler);
        return responseBody;
    }

    /**
	 * 
	 * @param hostname
	 * @param contents
	 * @return
	 */
    public static String executePost(DefaultHttpClient httpclient, String hostname, List<NameValuePair> params) throws Exception {
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(getPost(hostname, params), responseHandler);
        return responseBody;
    }

    /**
	 * 
	 * @param hostname
	 * @param contents
	 * @return
	 */
    public static String executePost(String hostname, String name, String value) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(getPost(hostname, name, value), responseHandler);
        return responseBody;
    }

    /**
	 * 
	 * @param hostname
	 * @return
	 * @throws Exception
	 */
    private static HttpPost getPost(final String hostname, final String paramName, final String paramValue) throws Exception {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        NameValuePair p1 = new NameValuePair() {

            public String getName() {
                return paramName;
            }

            public String getValue() {
                return paramValue;
            }
        };
        params.add(p1);
        return getPost(hostname, params);
    }

    /**
	 * 
	 * @param hostname
	 * @return
	 * @throws Exception
	 */
    private static HttpPost getPost(final String hostname, List<NameValuePair> params) throws Exception {
        HttpPost httppost = new HttpPost(hostname);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
        httppost.setEntity(entity);
        return httppost;
    }

    /**
	 * 
	 * @param hostname
	 * @param contents
	 * @return
	 */
    public static String executeGet(String hostname) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(hostname);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        return responseBody;
    }

    /**
	 *   
	 * @param hostname
	 * @return
	 * @throws Exception
	 */
    public static String executeSSLGet(String hostname) throws Exception {
        return executeSSLGet(hostname, null, null);
    }

    /**
	 *   
	 * @param hostname
	 * @return
	 * @throws Exception
	 */
    public static String executeSSLGet(String hostname, String username, String password) throws Exception {
        HttpGet httpget = new HttpGet(hostname);
        HttpResponse response = getSSLClient(username, password).execute(httpget);
        HttpEntity entity = response.getEntity();
        String s = response.getStatusLine().toString();
        if (entity != null) {
            s = Converter.convertInputStreamToString(entity.getContent());
            entity.consumeContent();
        }
        return s;
    }

    /**
	 * @param hostname
	 * @return
	 * @throws Exception
	 */
    public static String executeSSLPost(String hostname, String name, String value) throws Exception {
        DefaultHttpClient httpclient = getSSLClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(getPost(hostname, name, value), responseHandler);
        return responseBody;
    }

    /**
	 * @param hostname
	 * @return
	 * @throws Exception
	 */
    public static String executeSSLPost(String hostname, List<NameValuePair> params) throws Exception {
        DefaultHttpClient httpclient = getSSLClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(getPost(hostname, params), responseHandler);
        return responseBody;
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    public static DefaultHttpClient getSSLClient() throws Exception {
        return getSSLClient(defaultKeyStore, defaultKeyStorePassword, null, null);
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    public static DefaultHttpClient getSSLClient(String username, String password) throws Exception {
        return getSSLClient(defaultKeyStore, defaultKeyStorePassword, username, password);
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    public static DefaultHttpClient getSSLClient(File keystore, String keystorePassword, final String userName, final String password) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(keystore);
        try {
            trustStore.load(instream, keystorePassword.toCharArray());
        } finally {
            instream.close();
        }
        SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
        Scheme sch = new Scheme("https", socketFactory, 443);
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
        if (userName != null) {
            httpclient.getCredentialsProvider().setCredentials(new AuthScope("localhost", 4463), new Credentials() {

                @Override
                public Principal getUserPrincipal() {
                    return new Principal() {

                        @Override
                        public String getName() {
                            return userName;
                        }
                    };
                }

                @Override
                public String getPassword() {
                    return password;
                }
            });
        }
        return httpclient;
    }

    /**
	 * 
	 * @param httpUrl
	 * @return
	 * @throws Exception
	 */
    public static String downloadTextFile(String httpUrl) throws Exception {
        File tempFile = SystemUtil.getTemporaryFile("txt");
        downloadFile(httpUrl, tempFile);
        return FileSystemUtil.getTextContents(tempFile);
    }

    /**
	 * 
	 * @param httpUrl
	 * @param targetFile
	 * @throws Exception
	 */
    public static void downloadFile(String httpUrl, File targetFile) throws Exception {
        URL url;
        InputStream inputStream = null;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(targetFile));
        try {
            url = new URL(httpUrl);
            inputStream = url.openStream();
            dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
            byte[] bytes = new byte[4096];
            int bytesRead = dataInputStream.read(bytes);
            while (bytesRead > 0) {
                dataOutputStream.write(bytes, 0, bytesRead);
                bytesRead = dataInputStream.read(bytes);
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
            }
        }
    }

    /**
	 * 
	 * @param contents
	 * @return
	 */
    public static List<String> parseUrls(String html) {
        List<String> urls = new ArrayList<String>();
        if (Converter.isNotEmpty(html)) {
            int pos = html.indexOf("href=");
            while (pos > -1) {
                for (String quot : new String[] { "\"", "'" }) {
                    int actualStartPos = html.indexOf("href=" + quot, pos) + 6;
                    if (actualStartPos > pos) {
                        int actualEndPos = html.indexOf(quot, actualStartPos + 6);
                        if (actualEndPos > actualStartPos) {
                            String url = html.substring(actualStartPos, actualEndPos);
                            urls.add(url);
                        }
                    }
                }
                pos = html.indexOf("href=", pos + 1);
            }
        }
        return urls;
    }

    /**
	 * 
	 * @param response
	 * @param bytes
	 * @throws Exception
	 */
    public static void printResponse(HttpServletResponse response, String contentType, byte[] bytes) throws Exception {
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "no-cache");
        response.setContentLength((int) bytes.length);
        response.flushBuffer();
        ServletOutputStream out = response.getOutputStream();
        try {
            out.write(bytes, 0, bytes.length);
            out.flush();
        } catch (Exception ex) {
            Log.error(ex.getMessage());
        } finally {
            try {
                out.close();
            } catch (Exception ex2) {
                Log.error(ex2.getMessage());
            }
        }
    }

    /**
	 * 
	 * @param httpUrl
	 * @return
	 * @throws Exception
	 */
    public static JsonNode getJSONFromURL(String httpUrl) throws Exception {
        URL url;
        InputStream inputStream = null;
        DataInputStream dataInputStream;
        try {
            url = new URL(httpUrl);
            inputStream = url.openStream();
            dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
            return JsonUtil.getNode(dataInputStream);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
            }
        }
    }

    /**
	 * @return the defaultKeyStore
	 */
    public static File getDefaultKeyStore() {
        return defaultKeyStore;
    }

    /**
	 * @param defaultKeyStore the defaultKeyStore to set
	 */
    public static void setDefaultKeyStore(File defaultKeyStore) {
        HttpUtil.defaultKeyStore = defaultKeyStore;
    }
}

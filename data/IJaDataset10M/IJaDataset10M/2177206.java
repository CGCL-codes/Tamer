package cn.com.believer.songyuanframework.openapi.storage.box.impl.simple.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import cn.com.believer.songyuanframework.openapi.storage.box.constant.BoxConstant;

/**
 * @author Jimmy
 * 
 */
public final class BoxHTTPManager {

    /** log4j object. */
    protected static final Logger LOGGER = Logger.getLogger(BoxHTTPManager.class);

    /** singleton instance. */
    private static BoxHTTPManager instance;

    /** config properties. */
    private Properties config;

    /** only one instance in this application. */
    private HttpClient hc;

    /**
     * private constructor, singleton.
     */
    private BoxHTTPManager() {
        loadConfigProperties();
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        String maxConPerHost = config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_MAXCONNECTIONSPERHOST);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(Integer.parseInt(maxConPerHost));
        String maxTotalCons = config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_MAXTOTALCONNECTIONS);
        connectionManager.getParams().setMaxTotalConnections(Integer.parseInt(maxTotalCons));
        String connTimeout = config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_CONNECTIONTIMEOUT);
        connectionManager.getParams().setConnectionTimeout(Integer.parseInt(connTimeout));
        String soConnTimeout = config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_SOCONNECTIONTIMEOUT);
        connectionManager.getParams().setSoTimeout(Integer.parseInt(soConnTimeout));
        this.hc = new HttpClient(connectionManager);
    }

    /**
     * @return the config
     */
    public Properties getConfig() {
        return this.config;
    }

    /**
     * @param config
     *            the config to set
     */
    public void setConfig(Properties config) {
        this.config = config;
        HttpConnectionManager connectionManager = this.hc.getHttpConnectionManager();
        String maxConPerHost = config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_MAXCONNECTIONSPERHOST);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(Integer.parseInt(maxConPerHost));
        String maxTotalCons = config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_MAXTOTALCONNECTIONS);
        connectionManager.getParams().setMaxTotalConnections(Integer.parseInt(maxTotalCons));
        String connTimeout = config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_CONNECTIONTIMEOUT);
        connectionManager.getParams().setConnectionTimeout(Integer.parseInt(connTimeout));
        String soConnTimeout = config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_SOCONNECTIONTIMEOUT);
        connectionManager.getParams().setSoTimeout(Integer.parseInt(soConnTimeout));
    }

    /**
     * load config file to properties.
     */
    private void loadConfigProperties() {
        this.config = new Properties();
        try {
            String userDir = System.getProperty("user.dir");
            String propertyPath = userDir + File.separator + BoxConstant.CONFIG_FILE_NAME;
            InputStream in = new FileInputStream(new File(propertyPath));
            this.config.load(in);
        } catch (FileNotFoundException e) {
            LOGGER.warn("box4j-config.properties not found in classpath, use box4j-config-default.properties.");
            InputStream in = this.getClass().getResourceAsStream(BoxConstant.CONFIG_FILE_DEFAULT_NAME);
            try {
                this.config.load(in);
            } catch (IOException e1) {
                LOGGER.fatal("io exception happened when loading box4j-config-default.properties", e1);
            }
        } catch (IOException e) {
            LOGGER.fatal("io exception occured when read box4j-config.properties", e);
        }
    }

    /**
     * get the only one manager.
     * 
     * @return XDriveHTTPManager
     */
    public static BoxHTTPManager getBoxHTTPManager() {
        if (instance == null) {
            instance = new BoxHTTPManager();
        }
        return instance;
    }

    /**
     * post data to gateway.
     * 
     * @param url
     *            http URL
     * @param postData
     *            string of json
     * @return string response
     * @throws IOException
     *             IOException
     */
    public String doPost(String url, String postData) throws IOException {
        long t1 = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doPost-start #####, url=" + url + ", postData=\n" + postData);
        }
        String response = null;
        PostMethod pMethod = new PostMethod(url);
        if ("yes".equalsIgnoreCase(config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_IGNORECOOKIES))) {
            pMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        }
        try {
            NameValuePair type = new NameValuePair("data", postData);
            pMethod.setRequestBody(new NameValuePair[] { type });
            this.hc.executeMethod(pMethod);
            response = pMethod.getResponseBodyAsString();
        } finally {
            pMethod.releaseConnection();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doPost-end   #####, used time: " + (System.currentTimeMillis() - t1) + " ms,response=\n" + response + "\n");
        }
        return response;
    }

    /**
     * post XML content to the server.
     * 
     * @param url
     *            server URL
     * @param postData
     *            XML string
     * @return result from server
     * @throws IOException
     *             IO exception
     */
    public String doPostXML(String url, String postData) throws IOException {
        long t1 = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doPost-start #####, url=" + url + ", postData=\n" + postData);
        }
        String response = null;
        PostMethod pMethod = new PostMethod(url);
        if ("yes".equalsIgnoreCase(config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_IGNORECOOKIES))) {
            pMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        }
        try {
            StringRequestEntity xmlRequestEntity = new StringRequestEntity(postData);
            pMethod.setRequestEntity(xmlRequestEntity);
            this.hc.executeMethod(pMethod);
            response = pMethod.getResponseBodyAsString();
        } finally {
            pMethod.releaseConnection();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doPost-end   #####, used time: " + (System.currentTimeMillis() - t1) + " ms,response=\n" + response + "\n");
        }
        return response;
    }

    /**
     * http get method, get a XML result.
     * 
     * @param url
     *            http URL
     * @return XML result
     * @throws IOException
     *             io exception
     * @throws DocumentException
     *             document exception
     */
    public Document doGet(String url) throws IOException, DocumentException {
        Document result = null;
        long t1 = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doGet-start  #####, url=" + url);
        }
        GetMethod gMethod = new GetMethod(url);
        if ("yes".equalsIgnoreCase(config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_IGNORECOOKIES))) {
            gMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        }
        try {
            this.hc.executeMethod(gMethod);
            InputStream in = gMethod.getResponseBodyAsStream();
            SAXReader reader = new SAXReader();
            InputSource source = new InputSource(in);
            result = reader.read(source);
        } finally {
            gMethod.releaseConnection();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doGet-end    #####, used time: " + (System.currentTimeMillis() - t1) + " ms,response=\n" + result.asXML() + "\n");
        }
        return result;
    }

    /**
     * post to server and get byte array.
     * 
     * @param url
     *            server URL
     * @return byte array.
     * @throws IOException
     *             IO exception
     */
    public byte[] doGetByteArry(String url) throws IOException {
        byte[] result = null;
        long t1 = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doGet-start  #####, url=" + url);
        }
        GetMethod gMethod = new GetMethod(url);
        if ("yes".equalsIgnoreCase(config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_IGNORECOOKIES))) {
            gMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        }
        try {
            this.hc.executeMethod(gMethod);
            result = gMethod.getResponseBody();
        } finally {
            gMethod.releaseConnection();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doGet-end    #####, used time: " + (System.currentTimeMillis() - t1) + " ms,response is a file, size=" + result.length);
        }
        return result;
    }

    /**
     * download as a file object.
     * 
     * @param url
     *            server URL
     * @param inFile
     *            input file object
     * @return output file object
     * @throws IOException
     *             IO exception
     */
    public File doGetFile(String url, File inFile) throws IOException {
        long t1 = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doGet-start  #####, url=" + url);
        }
        InputStream responseBodyInputStream = null;
        GetMethod gMethod = new GetMethod(url);
        if ("yes".equalsIgnoreCase(config.getProperty(BoxConstant.CONFIG_HTTPCLIENT_IGNORECOOKIES))) {
            gMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        }
        try {
            this.hc.executeMethod(gMethod);
            responseBodyInputStream = gMethod.getResponseBodyAsStream();
            final int bufferSize = 2048;
            FileOutputStream fout = new FileOutputStream(inFile);
            byte[] buffer = new byte[bufferSize];
            int readCount = 0;
            while ((readCount = responseBodyInputStream.read(buffer)) != -1) {
                if (readCount < bufferSize) {
                    fout.write(buffer, 0, readCount);
                } else {
                    fout.write(buffer);
                }
            }
            fout.close();
        } finally {
            gMethod.releaseConnection();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doGet-end    #####, used time: " + (System.currentTimeMillis() - t1) + " ms,response=[InputStream]\n");
        }
        return inFile;
    }

    /**
     * upload multiple files.
     * 
     * @param url
     *            http URL
     * @param filesHashMap
     *            hashmap, key is string(file name), value is byte array.
     * @return response
     * @throws IOException
     *             exception
     */
    public String doMultipartPost(String url, Map filesHashMap) throws IOException {
        long t1 = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doPost, start, url=" + url);
        }
        PostMethod pMethod = new PostMethod(url);
        int fileCounts = filesHashMap.size();
        Part[] parts = new Part[fileCounts];
        Iterator it = filesHashMap.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            String key = (String) it.next();
            byte[] data = (byte[]) filesHashMap.get(key);
            ByteArrayPartSource byteArrayPartSource = new ByteArrayPartSource(key, data);
            FilePart filePart = new FilePart("Filedata" + i, byteArrayPartSource);
            parts[i] = filePart;
            i++;
        }
        MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, pMethod.getParams());
        pMethod.setRequestEntity(requestEntity);
        this.hc.executeMethod(pMethod);
        byte[] responseBody = pMethod.getResponseBody();
        String response = new String(responseBody);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doPost, end, used time: " + (System.currentTimeMillis() - t1));
            LOGGER.debug("doPost, end, response=\n" + response);
        }
        return response;
    }

    /**
     * upload multiple files.
     * 
     * @param url
     *            http URL
     * @param fileList
     *            file list(File list)
     * @return response
     * @throws IOException
     *             exception
     */
    public String doMultipartPost(String url, List fileList) throws IOException {
        long t1 = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doPost, start, url=" + url);
        }
        PostMethod pMethod = new PostMethod(url);
        int fileCounts = fileList.size();
        Part[] parts = new Part[fileCounts];
        for (int i = 0; i < fileList.size(); i++) {
            File f = (File) fileList.get(i);
            FilePart filePart = new FilePart("Filedata" + i, f);
            parts[i] = filePart;
        }
        MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, pMethod.getParams());
        pMethod.setRequestEntity(requestEntity);
        this.hc.executeMethod(pMethod);
        byte[] responseBody = pMethod.getResponseBody();
        String response = new String(responseBody);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doPost, end, used time: " + (System.currentTimeMillis() - t1));
            LOGGER.debug("doPost, end, response=\n" + response);
        }
        return response;
    }

    /**
     * get the only http client instance of system, config it as what you want.
     * 
     * @return the hc
     */
    public HttpClient getHttpClient() {
        return this.hc;
    }
}

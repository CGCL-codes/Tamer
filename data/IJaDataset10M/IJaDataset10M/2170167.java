package org.ludo.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.ludo.blocklist.BlocklistManager;
import org.ludo.blocklist.SimpleHtmlToPlainParser;
import org.ludo.config.ConfigConstants;
import org.ludo.config.ConfigEntry;
import org.ludo.net.HttpConstants;

/**
 * @author  <a href="mailto:masterludo@gmx.net">Ludovic Kim-Xuan Galibert</a>
 * @revision $Id: URLParser.java,v 1.2 2004/12/09 18:45:38 masterludo Exp $
 * @created Mar 16, 2004
 */
public class URLParser {

    /** Retrieve the data from the given URL.
   * @param anURL  the URL to retrieve the data from.
   * @param aConfigEntry  a ConfigEntry, must not be <code>null</code>.
   * @return  the content of the URL as string.
   * @throws IOException  if the URL could not be opened
   * @throws UnknownServiceException  if the given URL protocol is not supported
   */
    public static String retrieveDataFromURL(URL anURL, ConfigEntry aConfigEntry) throws IOException, UnknownServiceException {
        String data = null;
        if (null == anURL) {
            throw new IOException("The given URL is null");
        } else {
            if (anURL.getProtocol().equalsIgnoreCase("http")) {
                data = getUrlContent(anURL, aConfigEntry);
            } else if (anURL.getProtocol().equalsIgnoreCase("file")) {
                BufferedReader reader = new BufferedReader(new FileReader(anURL.getFile()));
                StringBuffer buffer = new StringBuffer(1024);
                String line = null;
                int lineNumber = 0;
                boolean isHtml = false;
                while (null != (line = reader.readLine())) {
                    if (0 == lineNumber) {
                        lineNumber++;
                        if (line.toLowerCase().startsWith("<!doctype") || line.toLowerCase().startsWith("<html>")) {
                            isHtml = true;
                        }
                    }
                    buffer.append(line.trim());
                    buffer.append("\n");
                }
                if (isHtml) {
                    data = SimpleHtmlToPlainParser.getInstance().parse(buffer.toString());
                } else {
                    data = buffer.toString();
                }
                reader.close();
            } else {
                throw new UnknownServiceException("The following protocol is not supported: " + anURL.getProtocol());
            }
            if (null == data) System.out.println("retrieveDataFromURL will return null");
        }
        return data;
    }

    /**
   * Gets the plain text content of the given URL.
   * @param anUrl  the URL to get the plain text from.
   * @param aConfigEntry  a ConfigEntry, must not be <code>null</code>.
   * @return  a plain text string.
   * @throws IOException  if the given URL could not be found
   * TODO Change the Exception thrown, something like PageNotFoundException or so... 
   */
    protected static String getUrlContent(URL anUrl, ConfigEntry aConfigEntry) throws IOException {
        String result = "";
        GetMethod method = new GetMethod(anUrl.toString());
        method.setFollowRedirects(true);
        String host = anUrl.getHost();
        method.addRequestHeader(HttpConstants.HEADER_HOST, host);
        HttpClient client = new HttpClient();
        HostConfiguration config = new HostConfiguration();
        BlocklistManager.handleProxySettings(aConfigEntry, config);
        config.setHost(host);
        client.setHostConfiguration(config);
        long timeout;
        String toval = aConfigEntry.getProperty(ConfigConstants.SOCKET_CONNECT_TIMEOUT);
        if (null != toval) {
            timeout = Long.parseLong(toval) * 1000;
        } else {
            timeout = 60000;
        }
        client.getParams().setConnectionManagerTimeout(timeout);
        try {
            client.executeMethod(method);
            writeToChunks(method.getResponseBodyAsStream(), anUrl.getPath().substring(anUrl.getPath().lastIndexOf("/")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        method.releaseConnection();
        return result;
    }

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static List writeToChunks(InputStream aStream, String aFileName) {
        StringBuffer buffer = new StringBuffer(1200000);
        List chunkList = new ArrayList(5);
        int currentSize = 0;
        try {
            File file = File.createTempFile("/sp/" + aFileName + "_", ".spchunk");
            FileOutputStream fos = new FileOutputStream(file);
            chunkList.add(file.getPath());
            Reader isReader = new InputStreamReader(aStream);
            BufferedReader reader = new BufferedReader(isReader);
            String line = null;
            while (null != (line = reader.readLine())) {
                currentSize += line.length();
                buffer.append(line);
                buffer.append(LINE_SEPARATOR);
                if (currentSize > 1000000) {
                    currentSize = 0;
                    System.out.println("making new chunk");
                    fos.write(buffer.toString().getBytes());
                    fos.flush();
                    fos.close();
                    file = File.createTempFile("/sp/" + aFileName + "_", ".spchunk");
                    chunkList.add(file);
                    fos = new FileOutputStream(file);
                    buffer = new StringBuffer(1200000);
                }
            }
            fos.write(buffer.toString().getBytes());
            fos.flush();
            fos.close();
            reader.close();
            isReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return chunkList;
    }

    public static String readInputStream(InputStream aStream) {
        String retVal = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            readInputStream(baos, aStream);
            retVal = baos.toString();
            baos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retVal;
    }

    public static void readInputStream(OutputStream anOS, InputStream anIS) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int read;
        while ((read = anIS.read(buffer)) >= 0) {
            anOS.write(buffer, 0, read);
        }
        anOS.flush();
    }
}

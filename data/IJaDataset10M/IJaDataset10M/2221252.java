package com.wpg.plugin.webServicesUser;

import com.wpg.Scripting.ScriptGUI;
import com.wpg.LoadGen.*;
import com.wpg.Util.Proxy;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.*;
import java.util.zip.*;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.httpclient.params.*;
import org.apache.commons.httpclient.protocol.*;
import org.apache.commons.httpclient.cookie.*;
import com.wpg.proxy.*;

public class WebServicesMessageListener implements HttpMessageHandler {

    public static PrintStream script = null;

    protected static Logger logger = Logger.getLogger(WebServicesMessageListener.class);

    public String packageName = "com.wpg.script.wpg";

    public String name = "OutputScript";

    private boolean scriptStarted = false;

    private Proxy proxy = ScriptGUI.config.proxy;

    public WebServicesMessageListener() {
    }

    public void failed(Exception expe) {
        logger.warn("Proxy Exception: " + expe);
    }

    public void failedRequest(HttpMessageRequest request, Exception expe) {
        logger.warn("Proxy Exception: " + expe);
    }

    public void failedResponse(HttpMessageResponse response, HttpMessageRequest request, Exception expe) {
        logger.warn("Proxy Exception: " + expe);
    }

    public void receivedRequest(HttpMessageRequest request) {
        if (proxy == null) return;
        logger.debug("Start Line: " + request.getStartLine());
        logger.debug("Trying converting tohost to proxy");
        String newURI = (request.getToPort() == 443 ? "https://" : "http://") + request.getToHost() + ":" + request.getToPort() + request.getUri();
        try {
            request.setUri(newURI);
        } catch (java.net.URISyntaxException e) {
            logger.warn("Error setting uri to: " + newURI + " Exception: " + e, e);
        }
        request.setToHost(proxy.host);
        request.setToPort(proxy.port);
    }

    public void receivedResponse(HttpMessageResponse response, HttpMessageRequest request) {
        if (request.getMethod().equals("POST") || request.getMethod().equals("GET")) addToScript(request, response);
    }

    private static byte[] decodeTable = new byte[256];

    static {
        for (int i = '0'; i <= '9'; i++) {
            decodeTable[i] = (byte) (i - '0');
        }
        for (int i = 'A'; i <= 'F'; i++) {
            decodeTable[i] = (byte) (i - 'A' + 10);
        }
    }

    private byte[] decode(byte[] data) {
        if (data == null || (data.length % 2) != 0) {
            logger.warn("Returning null because either input is null or input is odd length: " + data.length % 2);
            return null;
        }
        int dataSize = (int) data.length / 2;
        byte[] decoded = new byte[dataSize];
        for (int i = 0; i < dataSize; i++) {
            try {
                decoded[i] = (byte) ((decodeTable[data[i * 2]] << 4) | decodeTable[data[(i * 2) + 1]]);
            } catch (Exception e) {
                return decoded;
            }
        }
        return decoded;
    }

    private String getName(String in) {
        StringTokenizer st = new StringTokenizer(in, ":;=\" \n\r");
        while (st.hasMoreTokens()) if (st.nextToken().equals("name")) return st.nextToken();
        return "";
    }

    private String getFileName(String in) {
        Pattern pattern = Pattern.compile("filename=\"(.*)\"");
        Matcher m = pattern.matcher(in);
        if (m.find()) return m.group(1);
        return "";
    }

    private String readToBound(BufferedReader is, String bound) throws IOException {
        logger.trace("starting readToBound");
        StringBuffer sb = new StringBuffer();
        char inchar;
        while (is.ready()) {
            inchar = (char) is.read();
            sb.append(inchar);
            if (sb.toString().endsWith(bound)) break;
        }
        logger.trace("finished readToBound");
        logger.trace("string == " + sb.substring(0, (sb.length() - bound.length()) - 2));
        return sb.substring(0, (sb.length() - bound.length()) - 2);
    }

    private byte[] readFileToBound(BufferedReader is, String bound) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(10 * 1024 * 1024);
        StringBuffer sb = new StringBuffer();
        char[] in = new char[1];
        while (is.read(in, 0, 1) != -1) {
            bb.putChar(in[0]);
            sb.append(in[0]);
            if (sb.toString().endsWith(bound)) break;
        }
        byte[] buf = new byte[bb.position() - bound.length() + 1];
        bb.get(buf, 0, buf.length);
        return buf;
    }

    private String printHeaders(Map hMap) {
        Set hSet = hMap.keySet();
        Iterator hit = hSet.iterator();
        StringBuffer sb = new StringBuffer();
        while (hit.hasNext()) {
            String key = (String) hit.next();
            logger.trace("header: " + key + " Value: " + (List) hMap.get(key));
            sb.append(key + ": " + (List) hMap.get(key) + "\n");
        }
        return sb.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    private int numData = 0;

    private void addToScript(HttpMessageRequest request, HttpMessageResponse response) {
        if (this.name == null) setName(ScriptGUI.currentScript.name);
        try {
            if (destDir == null) {
                destDir = ScriptGUI.currentScript.path + System.getProperty("file.separator") + "RecordingLog" + System.getProperty("file.separator");
                new File(destDir).mkdirs();
                logger.info("Output Recording Log Dir: " + destDir);
            }
        } catch (Exception e) {
            logger.error("Exception creating recording log directory, Exception: " + e);
        }
        if (logger.isTraceEnabled()) {
            logger.trace("start line: " + request.getStartLine());
            logger.trace("protocol: " + request.getProtocol());
            logger.trace("to host: " + request.getToHost());
            logger.trace("to port: " + request.getToPort());
            logger.trace("version: " + request.getVersion());
            logger.trace("************* Request Header *****************");
            printHeaders(request.getHeaders());
            logger.trace("************* Request Header *****************");
            logger.trace("************* Response Header *****************");
            printHeaders(response.getHeaders());
            logger.trace("************* Response Header *****************");
        }
        List contentList = response.getHeaderValues(HttpMessage.HEADER_CONTENT_TYPE);
        String contentType = "";
        if (contentList != null) {
            contentType = (String) contentList.get(0);
        } else {
            return;
        }
        if (contentType.startsWith("text/css") || !contentType.startsWith("text/")) {
            File localFile = null;
            try {
                localFile = new File(destDir, request.getUri().toString());
                localFile.getParentFile().mkdirs();
                BufferedInputStream is = new BufferedInputStream(response.getBodyContentStream());
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(localFile));
                int i;
                while ((i = is.read()) != -1) os.write(i);
                is.close();
                os.close();
            } catch (Exception e) {
                logger.warn("error creating local resource: " + localFile.getPath() + " Exception: " + e);
            }
            return;
        }
        String referrer = "";
        List refList = request.getHeaderValues("referer");
        if (refList != null) referrer = (String) refList.get(0);
        String protocol = "http";
        if (request.getToPort() == 443) protocol = "https";
        ScriptGUI.currentScript.recordInd++;
        StringBuffer sb = new StringBuffer("\n");
        String url = "";
        try {
            url = request.getUri().toURL().toString();
        } catch (java.net.MalformedURLException e) {
            logger.error("MalformedURLException while building URL: " + e, e);
        }
        sb.append("fetchXML( \"" + url + "\",\n\t" + request.getMethod() + ",\n\t\"" + contentType + "\",\n\t\"" + referrer + "\"");
        if (request.getBodyContent() != null) {
            for (String body : new String(request.getBodyContent()).split("\n")) {
                sb.append(",\n\t\"").append(body).append("\"");
            }
        }
        if (refFormat == null) refFormat = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss.SSS-");
        String reference = refFormat.format(new Date()) + ScriptGUI.currentScript.recordInd;
        sb.append("); //recording reference: " + reference + "\n");
        logger.debug("Adding function call: " + sb.toString());
        logRecordingInfo(reference, request, response);
        ScriptGUI.appendScript(sb.toString());
    }

    private SimpleDateFormat refFormat = null;

    private String destDir = null;

    private void logRecordingInfo(String reference, HttpMessageRequest request, HttpMessageResponse response) {
        try {
            File temp = new File(destDir + reference + "-info.txt");
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(temp)));
            pw.println("Request: " + request.getStartLine());
            pw.println("Protocol: " + request.getProtocol());
            pw.println("To Host: " + request.getToHost());
            pw.println("To Port: " + request.getToPort());
            pw.println("Version: " + request.getVersion());
            pw.println("************* Request Header *****************");
            pw.println(request.getHeadersAsString());
            if (request.getBodyContent() != null) pw.println("\n\r" + new String(request.getBodyContent()));
            pw.println("************* Response Header *****************");
            pw.println(response.getHeadersAsString());
            if (response.getBodyContent() != null) pw.println("\n\r" + new String(response.getBodyContent()));
            pw.close();
            temp = new File(destDir + reference + "-body.html");
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(temp));
            os.write(response.getBodyContent(), 0, response.getBodyContent().length);
            os.close();
        } catch (Exception e) {
            logger.warn("Exception while creating recording log: " + e);
        }
    }
}

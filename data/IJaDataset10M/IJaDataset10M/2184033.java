package com.dcivision.form.core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dcivision.dms.client.HtmlFormFile;
import com.dcivision.dms.client.HtmlFormText;
import com.dcivision.form.FormErrorConstant;
import com.dcivision.framework.ApplicationException;

/**
 * FormHttpOperation.java
 * 
 * This class includes all functions of http operation for Form Builder.
 * 
 * @author Vera
 * @company DCIVision Limited
 * @creation date 27/08/2004
 * @version $Revision: 1.17.2.3 $
    */
public class FormHttpOperation {

    public static final String REVISION = "$Revision: 1.17.2.3 $";

    private static final Log log = LogFactory.getLog(FormHttpOperation.class);

    protected static final String starter = "-----------------------------";

    protected static final String returnChar = "\r\n";

    protected static final String lineEnd = "--";

    protected static final String HEADER_SETCOOKIE = "Set-Cookie";

    protected static final String HEADER_COOKIE = "Cookie";

    private List txtList = new ArrayList();

    private List fileList = new ArrayList();

    private String cookie = null;

    private String urlString = "";

    private String targetFile = null;

    private String actionStatus = null;

    private boolean needLogin = false;

    /**
 * constructor
 */
    public FormHttpOperation() {
    }

    public void addHtmlFormText(HtmlFormText txt) {
        txtList.add(txt);
    }

    public void addHtmlFormFile(HtmlFormFile file) {
        fileList.add(file);
    }

    public void setSubmissionURL(String urlString) {
        this.urlString = urlString;
    }

    public void setTargetFile(String targetFile) {
        this.targetFile = targetFile;
    }

    public String getTargetFile() {
        return this.targetFile;
    }

    public String getActionStatus() {
        return this.actionStatus;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return this.cookie;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public boolean getNeedLogin() {
        return this.needLogin;
    }

    /**
   * Transmit file
   * which will call transmit(String input,String filePath)
   * <pre>
   *   this.transmit(null,filePath);
   * </pre>
 * @param filePath
 * @return String that is the text of connection message
 * @throws Exception
 * 
 */
    public String transmit(String filePath) throws Exception {
        return this.transmit(null, filePath);
    }

    /**
   * Transmit file
 * @param input		File input 
 * @param filePath	Destination File Path
 * @return String that is the text of connection message
 * @throws Exception
 */
    public String transmit(String input, String filePath) throws Exception {
        if (cookie == null || "".equals(urlString)) {
            return null;
        }
        String txt = "";
        StringBuffer returnMessage = new StringBuffer();
        final String boundary = String.valueOf(System.currentTimeMillis());
        URL url = null;
        URLConnection conn = null;
        BufferedReader br = null;
        DataOutputStream dos = null;
        try {
            url = new URL(urlString);
            conn = url.openConnection();
            ((HttpURLConnection) conn).setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setAllowUserInteraction(true);
            conn.setUseCaches(false);
            conn.setRequestProperty(HEADER_COOKIE, cookie);
            if (input != null) {
                String auth = "Basic " + new sun.misc.BASE64Encoder().encode(input.getBytes());
                conn.setRequestProperty("Authorization", auth);
            }
            dos = new DataOutputStream(conn.getOutputStream());
            dos.write((starter + boundary + returnChar).getBytes());
            for (int i = 0; i < txtList.size(); i++) {
                HtmlFormText htmltext = (HtmlFormText) txtList.get(i);
                dos.write(htmltext.getTranslated());
                if (i + 1 < txtList.size()) {
                    dos.write((starter + boundary + returnChar).getBytes());
                } else if (fileList.size() > 0) {
                    dos.write((starter + boundary + returnChar).getBytes());
                }
            }
            for (int i = 0; i < fileList.size(); i++) {
                HtmlFormFile htmlfile = (HtmlFormFile) fileList.get(i);
                dos.write(htmlfile.getTranslated());
                if (i + 1 < fileList.size()) {
                    dos.write((starter + boundary + returnChar).getBytes());
                }
            }
            dos.write((starter + boundary + "--" + returnChar).getBytes());
            dos.flush();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            txt = transactFormStr(br);
            if (!"".equals(filePath) && !"null".equals(filePath)) {
                RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
                raf.seek(raf.length());
                raf.writeBytes(txt + "\n");
                raf.close();
            }
            txtList.clear();
            fileList.clear();
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            try {
                dos.close();
            } catch (Exception e) {
            }
            try {
                br.close();
            } catch (Exception e) {
            }
        }
        return txt;
    }

    /**
   * 
   * @param br
   * @return
   * @throws IOException
   */
    private String transactFormStr(BufferedReader br) throws IOException {
        String txt;
        StringBuffer bufferTxt = new StringBuffer();
        StringBuffer tempBufferTxt = new StringBuffer();
        String tempStr;
        String iframeStr = "";
        boolean isEndLoadingIframe = false;
        boolean isLoadingIframe = false;
        boolean isErrorIframe = false;
        while ((tempStr = br.readLine()) != null) {
            if ((!isErrorIframe) && (!isEndLoadingIframe) && (tempStr.toLowerCase().indexOf("<iframe".toLowerCase()) > -1)) {
                if (isLoadingIframe) {
                    isErrorIframe = true;
                } else if (tempStr.toLowerCase().indexOf("id=\"iframeLoading\"".toLowerCase()) > -1) {
                    isLoadingIframe = true;
                    if (tempStr.toLowerCase().indexOf("</iframe>".toLowerCase()) > -1) {
                        isEndLoadingIframe = true;
                        tempStr = tempStr.substring(0, tempStr.toLowerCase().indexOf("<iframe".toLowerCase())) + tempStr.substring(tempStr.toLowerCase().indexOf("</iframe>".toLowerCase()) + "</iframe>".length());
                    } else {
                        iframeStr = tempStr;
                        continue;
                    }
                }
            }
            if ((!isErrorIframe) && (isLoadingIframe) && (!isEndLoadingIframe) && (tempStr.toLowerCase().indexOf("</iframe>".toLowerCase()) > -1)) {
                isEndLoadingIframe = true;
                tempStr = tempStr.substring(tempStr.toLowerCase().indexOf("</iframe>".toLowerCase()) + "</iframe>".length());
            }
            if (!isLoadingIframe || (isLoadingIframe && isEndLoadingIframe)) {
                bufferTxt.append(tempStr);
                if ("<head>".equalsIgnoreCase(tempStr)) {
                    bufferTxt.append("<meta http-equiv='content-type' content='text/html; charset=UTF-8'>");
                }
                bufferTxt.append("\n");
            } else {
                tempBufferTxt.append(tempStr);
                if ("<head>".equalsIgnoreCase(tempStr)) {
                    tempBufferTxt.append("<meta http-equiv='content-type' content='text/html; charset=UTF-8'>");
                }
                tempBufferTxt.append("\n");
            }
        }
        if (isLoadingIframe && !isEndLoadingIframe) {
            iframeStr = iframeStr.replaceFirst("<iframe", "<div");
            bufferTxt.append(iframeStr);
            bufferTxt.append(tempBufferTxt);
            tempBufferTxt = null;
        }
        txt = bufferTxt.toString();
        bufferTxt = null;
        return txt;
    }

    /**
   * 
   * @param bufferTxt
   * @param tempStr
   */
    private void appendFormStr(StringBuffer bufferTxt, String tempStr) {
        bufferTxt.append(tempStr);
        if ("<head>".equalsIgnoreCase(tempStr)) {
            bufferTxt.append("<meta http-equiv='content-type' content='text/html; charset=UTF-8'>");
        }
        bufferTxt.append("\n");
    }

    /**
   * Check the Input Message type and then reformat the message
   * @param inputStr		Input String for the Message
   * @return String after formatted
   */
    private String formatLine(String inputStr) {
        String result = "";
        String messageType = "";
        if (inputStr.indexOf("errorMessage") > 0) {
            messageType = "error";
            actionStatus = "error";
            result = "Status: " + messageType;
        } else if (inputStr.indexOf("systemMessage") > 0) {
            messageType = "system";
            actionStatus = "system";
            result = "Status: " + messageType;
        } else if (inputStr.indexOf(">") > 0) {
            String tmpStr = "";
            tmpStr = inputStr.substring(inputStr.lastIndexOf(">") + 1, inputStr.length());
            if (!"".equals(tmpStr)) {
                result = "Message: " + tmpStr;
            }
        } else if (inputStr.indexOf("</UL>") > 0) {
        } else if (inputStr.indexOf("\n") > 0) {
        } else {
            if (!"".equals(inputStr)) {
                result = inputStr;
            }
        }
        return result;
    }

    /**
   * getCurrentTimestamp
   * @return  Timestamp object which representing the current time.
   */
    public static java.sql.Timestamp getCurrentTimestamp() {
        java.util.Calendar tmp = java.util.Calendar.getInstance();
        tmp.clear(java.util.Calendar.MILLISECOND);
        return (new java.sql.Timestamp(tmp.getTime().getTime()));
    }

    /**
   * login to System
   * @param strUrl			Login URL 
   * @param loginName		User Login name
   * @param loginPwd		User Login password
   * @return true if login successfully
   */
    public boolean login(String strUrl, String loginName, String loginPwd) throws ApplicationException {
        String starter = "-----------------------------";
        String returnChar = "\r\n";
        String lineEnd = "--";
        String urlString = strUrl;
        String input = null;
        List txtList = new ArrayList();
        List fileList = new ArrayList();
        String targetFile = null;
        String actionStatus = null;
        StringBuffer returnMessage = new StringBuffer();
        List head = new ArrayList();
        final String boundary = String.valueOf(System.currentTimeMillis());
        URL url = null;
        URLConnection conn = null;
        BufferedReader br = null;
        DataOutputStream dos = null;
        boolean isLogin = false;
        txtList.add(new HtmlFormText("loginName", loginName));
        txtList.add(new HtmlFormText("loginPwd", loginPwd));
        txtList.add(new HtmlFormText("navMode", "I"));
        txtList.add(new HtmlFormText("action", "login"));
        try {
            url = new URL(urlString);
            conn = url.openConnection();
            ((HttpURLConnection) conn).setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "multipart/form-data, boundary=" + "---------------------------" + boundary);
            if (input != null) {
                String auth = "Basic " + new sun.misc.BASE64Encoder().encode(input.getBytes());
                conn.setRequestProperty("Authorization", auth);
            }
            dos = new DataOutputStream(conn.getOutputStream());
            dos.write((starter + boundary + returnChar).getBytes());
            for (int i = 0; i < txtList.size(); i++) {
                HtmlFormText htmltext = (HtmlFormText) txtList.get(i);
                dos.write(htmltext.getTranslated());
                if (i + 1 < txtList.size()) {
                    dos.write((starter + boundary + returnChar).getBytes());
                } else if (fileList.size() > 0) {
                    dos.write((starter + boundary + returnChar).getBytes());
                }
            }
            dos.write((starter + boundary + "--" + returnChar).getBytes());
            dos.flush();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String cookieVal = conn.getHeaderField(HEADER_SETCOOKIE);
            if (cookieVal != null) {
                cookie = cookieVal.substring(0, cookieVal.indexOf(";"));
            }
            String tempstr;
            int line = 0;
            while (null != ((tempstr = br.readLine()))) {
                if (!tempstr.equals("")) {
                    if ("window.location.replace(\"/Home.do\");".indexOf(returnMessage.append(formatLine(tempstr)).toString()) != -1) {
                        isLogin = true;
                        break;
                    }
                    line++;
                }
            }
            txtList.clear();
            fileList.clear();
        } catch (Exception e) {
            log.error(e, e);
            throw new ApplicationException(FormErrorConstant.DB_APP_BASE_URL_ERROR);
        } finally {
            try {
                dos.close();
            } catch (Exception e) {
            }
            try {
                br.close();
            } catch (Exception e) {
            }
        }
        return isLogin;
    }

    /**
   * Get Object Input Stream from HttpServlet
   * @param strUrl		URL 
   * @return ObjectInputStream
   */
    public ObjectInputStream getObjectInputStreamFromServlet(String strUrl) throws Exception {
        if (cookie == null) {
            return null;
        }
        String starter = "-----------------------------";
        String returnChar = "\r\n";
        String lineEnd = "--";
        String urlString = strUrl;
        String input = null;
        List txtList = new ArrayList();
        List fileList = new ArrayList();
        String targetFile = null;
        String actionStatus = null;
        StringBuffer returnMessage = new StringBuffer();
        List head = new ArrayList();
        final String boundary = String.valueOf(System.currentTimeMillis());
        URL url = null;
        URLConnection conn = null;
        DataOutputStream dos = null;
        ObjectInputStream inputFromServlet = null;
        try {
            url = new URL(urlString);
            conn = url.openConnection();
            ((HttpURLConnection) conn).setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "multipart/form-data, boundary=" + "---------------------------" + boundary);
            conn.setRequestProperty(HEADER_COOKIE, cookie);
            if (input != null) {
                String auth = "Basic " + new sun.misc.BASE64Encoder().encode(input.getBytes());
                conn.setRequestProperty("Authorization", auth);
            }
            dos = new DataOutputStream(conn.getOutputStream());
            dos.flush();
            inputFromServlet = new ObjectInputStream(conn.getInputStream());
            txtList.clear();
            fileList.clear();
        } catch (Exception e) {
            log.error(e, e);
            return null;
        } finally {
            try {
                dos.close();
            } catch (Exception e) {
            }
        }
        return inputFromServlet;
    }

    public Object clone() {
        FormHttpOperation httpOperation = new FormHttpOperation();
        httpOperation.setCookie(this.getCookie());
        return httpOperation;
    }

    public void reset() {
        txtList = new ArrayList();
        fileList = new ArrayList();
        setSubmissionURL("");
    }
}

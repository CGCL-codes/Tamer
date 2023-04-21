package com.volumlab.plugin.AJAXUser;

import com.volumlab.LoadGen.*;
import com.volumlab.Scripting.TestScript;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.*;
import org.apache.commons.httpclient.cookie.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.httpclient.params.*;
import org.apache.commons.httpclient.protocol.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Script extends ScriptTemplate {

    protected Logger logger = Logger.getLogger(Script.class);

    protected static final int GET = 1;

    protected static final int POST = 3;

    protected HttpState httpState = new HttpState();

    protected GetMethod getMethod;

    protected PostMethod postMethod;

    protected int userTimeout = 120000;

    protected int socketTimeout = 180000;

    protected int connectTimeout = 30000;

    protected SimpleHttpConnectionManager httpConMan = null;

    protected HttpClient httpClient = null;

    protected boolean followRedirects = false;

    private String customUserAgent = null;

    public String getCustomUserAgent() {
        return customUserAgent;
    }

    public void setCustomUserAgent(String s) {
        customUserAgent = s;
    }

    public void initHTTPClient() {
        httpConMan = new SimpleHttpConnectionManager();
        httpClient = new HttpClient(httpConMan);
        if (proxy != null) {
            httpClient.getHostConfiguration().setProxy(proxy.host, proxy.port);
            if (!proxy.user.equals("")) httpClient.getState().setProxyCredentials(proxy.getAuthScope(), proxy.getUsernamePasswordCredentials());
        }
    }

    public Script() {
        super();
        Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", easyhttps);
    }

    protected void setFollowRedirects(boolean follow) {
        logMessage("Follow Page Redirects set to: " + follow);
        followRedirects = follow;
    }

    protected void setUserTimeout(int mil) {
        logMessage("Default User Timeout changed from " + (userTimeout / 1000) + " to " + (mil / 1000) + " seconds");
        userTimeout = mil;
    }

    protected void setTimeout(int mil) {
        logMessage("Default Socket Timeout changed from " + (socketTimeout / 1000) + " to " + (mil / 1000) + " seconds");
        socketTimeout = mil;
    }

    protected void setConnectionTimeout(int mil) {
        logMessage("Default Connection Timeout changed from " + (connectTimeout / 1000) + " to " + (mil / 1000) + " seconds");
        connectTimeout = mil;
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout);
    }

    public void run() {
        try {
            if (sleepBeforeStart > 0) {
                try {
                    logger.info("Sleep for " + sleepBeforeStart + " seconds, before running");
                    Thread.sleep(sleepBeforeStart * 1000);
                } catch (Exception e) {
                }
            }
            iteration = 0;
            testHarness.updateCurUsers(1);
            while (run) {
                initHTTPClient();
                iteration++;
                if (!test) {
                    logStr = new String("test=" + testid + " script=" + scriptName + " user=" + (id + 1) + " iter=" + iteration + ": ");
                }
                userIterationStatus = new ScriptStatus(scriptName, "_RunTime_", testid, id + 1, iteration);
                userIterationStatus.logs = new Vector();
                userIterationStatus.requests = new Vector();
                logMessage(userIterationStatus, "Start iteration");
                script();
                stopTransaction(userIterationStatus);
                if (test) {
                    run = false;
                }
            }
            testHarness.updateCurUsers(-1);
        } catch (Exception e) {
            errorMessage(userIterationStatus, "Exception: " + e, e);
            try {
                stopTransaction(userIterationStatus);
            } catch (Exception ex) {
            }
            run = false;
        }
    }

    public void script() throws InterruptedException {
        startTransaction("Default Place Holder Transaction");
        errorMessage("Please overload the script() class function in your script!; the prototype for this function is detailed at http://www.webperformancegroup.com/scriptingAPI/");
        stopTransaction();
    }

    /**return the cookie text*/
    public String getCookie(String name) {
        Cookie[] cookies = httpClient.getState().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) return cookie.getValue();
        }
        errorMessage("Cookie not found for: " + name);
        return "";
    }

    private class TimeoutTimer extends TimerTask {

        private HttpMethod method = null;

        private int timeout = 0;

        public void setTimeout(int i) {
            timeout = i;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setMethod(Object i) {
            this.method = (HttpMethod) i;
        }

        public HttpMethod getMethod() {
            return method;
        }

        public void run() {
            errorMessage("Fetch URL Timed Out Due to user timeout of " + (int) timeout / 1000 + " seconds being reached");
            method.abort();
            method = null;
        }
    }

    /**
	* function to handle raw or custom http get, without a body
	*/
    protected void fetchURL(String target, int method, String contentType, String referer) {
        fetchURL(target, method, contentType, referer, "");
    }

    /**
	* function to handle raw or custom html
	*/
    protected void fetchURL(String target, int method, String contentType, String referer, String... bodyArgs) {
        TimeoutTimer timeoutTimer = new TimeoutTimer();
        Timer timer = new Timer();
        StringBuffer bsb = new StringBuffer();
        for (String s : bodyArgs) {
            bsb.append(s).append("\n");
        }
        String body = bsb.toString();
        switch(method) {
            case GET:
                try {
                    logMessage("GET URL: " + target);
                    getMethod = new GetMethod(target);
                    getMethod.getParams().setSoTimeout(socketTimeout);
                    getMethod.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
                    getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
                    getMethod.setFollowRedirects(true);
                    getMethod.setRequestHeader("Referer", referer);
                    if (getCustomUserAgent() == null) {
                        getMethod.setRequestHeader("User-Agent", "WPG Load Generator user: " + id + 1 + " script: " + scriptName);
                    } else {
                        getMethod.setRequestHeader("User-Agent", getCustomUserAgent());
                    }
                    timeoutTimer.setMethod(getMethod);
                    timeoutTimer.setTimeout(userTimeout);
                    timer.schedule(timeoutTimer, userTimeout);
                    this._responseCode = httpClient.executeMethod(getMethod);
                    if (this._responseCode != HttpStatus.SC_OK) errorMessage("Get Failed: " + getMethod.getStatusLine());
                    _responseText = new StringBuffer(getMethod.getResponseBodyAsString());
                    logMessage("Response Code: " + this._responseCode + " " + HttpStatus.getStatusText(this._responseCode) + "\tSize: " + _responseText.length());
                    Request req = new Request(target, "GET", _responseCode, _responseText.toString());
                    req.queryString = getMethod.getQueryString();
                    req.respHeaders = getMethod.getResponseHeaders();
                    req.reqHeaders = getMethod.getRequestHeaders();
                    if (status != null) {
                        status.requests.addElement(req);
                    } else {
                        userIterationStatus.requests.addElement(req);
                    }
                    getMethod.releaseConnection();
                    timer.cancel();
                    if (test) writeRunLog(req);
                } catch (HttpException he) {
                    errorMessage("HTTP Exception in Get " + he, he);
                } catch (java.io.IOException ioe) {
                    errorMessage("IO Exception in Get " + ioe, ioe);
                } catch (java.lang.IllegalStateException e) {
                } catch (Exception e) {
                    errorMessage("Exception in Get " + e, e);
                } finally {
                    if (getMethod != null) {
                        getMethod.releaseConnection();
                        getMethod = null;
                    }
                    timer.cancel();
                }
                break;
            case POST:
                logMessage("POST URL: " + target);
                try {
                    postMethod = new PostMethod(target);
                    postMethod.getParams().setSoTimeout(socketTimeout);
                    postMethod.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
                    postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
                    postMethod.setRequestHeader("Referer", referer);
                    if (getCustomUserAgent() == null) {
                        postMethod.setRequestHeader("User-Agent", "WPG Load Generator user: " + id + 1 + " script: " + scriptName);
                    } else {
                        postMethod.setRequestHeader("User-Agent", getCustomUserAgent());
                    }
                    postMethod.setRequestHeader("Content-type", contentType);
                    postMethod.setRequestBody(body);
                    timeoutTimer.setMethod(postMethod);
                    timeoutTimer.setTimeout(userTimeout);
                    timer.schedule(timeoutTimer, userTimeout * 1000);
                    this._responseCode = httpClient.executeMethod(postMethod);
                    if (this._responseCode != HttpStatus.SC_OK) errorMessage("Post Failed: " + postMethod.getStatusLine());
                    _responseText = new StringBuffer(postMethod.getResponseBodyAsString());
                    logMessage("Response Code: " + this._responseCode + " " + HttpStatus.getStatusText(this._responseCode) + "\tSize: " + _responseText.length());
                    Request req = new Request(target, "POST", _responseCode, _responseText.toString());
                    req.queryString = postMethod.getQueryString();
                    req.respHeaders = postMethod.getResponseHeaders();
                    req.reqHeaders = postMethod.getRequestHeaders();
                    req.postParameters = postMethod.getParameters();
                    if (status != null) {
                        status.requests.addElement(req);
                    } else {
                        userIterationStatus.requests.addElement(req);
                    }
                    timer.cancel();
                    if (test) writeRunLog(req);
                } catch (HttpException he) {
                    errorMessage("HTTP Exception in Post " + he, he);
                } catch (java.io.IOException ioe) {
                    errorMessage("IO Exception in Post " + ioe, ioe);
                } catch (java.lang.IllegalStateException e) {
                } catch (Exception e) {
                    errorMessage("Exception in Post " + e, e);
                } finally {
                    if (postMethod != null) {
                        postMethod.releaseConnection();
                        postMethod = null;
                    }
                    timer.cancel();
                }
                break;
            default:
                errorMessage("HTTP Method Unknown");
                break;
        }
    }

    private int _tranCnt = 0;

    protected void writeRunLog(Request ro) {
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss.SSS-");
        _tranCnt++;
        String reference = dateFmt.format(new Date()) + _tranCnt;
        try {
            File temp = new File(logDir + reference + "-info.txt");
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(temp)));
            pw.println("Request: " + ro.url);
            pw.println("************* Request Header *****************");
            for (int i = 0; i < ro.reqHeaders.length; i++) pw.print(ro.reqHeaders[i].toString());
            pw.println("************* Response Header *****************");
            for (int i = 0; i < ro.respHeaders.length; i++) pw.print(ro.respHeaders[i].toString());
            pw.close();
            temp = new File(logDir + reference + "-body.html");
            pw = new PrintWriter(new BufferedWriter(new FileWriter(temp)));
            pw.print(ro.respText);
            pw.close();
        } catch (Exception e) {
            logger.error("Exception caught writing run log entry, Exception: " + e, e);
        }
    }
}

package com.taobao.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import com.taobao.api.model.TaobaoResponse;
import com.taobao.api.util.FetchUtil;

/**
 * JDK实现的UrlFetch
 * 
 * 在恶劣情况下，如果请求过于频繁（如每秒数百次）且单次请求耗时较长， 有可能导致客户端连接来不及释放，耗尽计算机端口资源。在这种环境 下，推荐使用
 * <code>com.taobao.api.HttpClientUrlFetch</code>
 * 
 * 
 * @author sulinchong.pt
 * @author lin.wangl
 */
public class JdkUrlFetch extends AbstractUrlFetch {

    private final String requestMethod = "POST";

    private int connectTimeOut = DEFAULT_CONNECT_TIMEOUT;

    private int readTimeOut = DEFAULT_READ_TIMEOUT;

    public static final String SIP_STATUS_OK = "9999";

    protected static final String CRLF = "\r\n";

    protected static final String PREF = "--";

    protected static final int UPLOAD_BUFFER_SIZE = 10240;

    public JdkUrlFetch() {
    }

    public void setConnectTimeout(int milliSecond) {
        this.connectTimeOut = milliSecond;
    }

    public void setReadTimeout(int milliSecond) {
        this.readTimeOut = milliSecond;
    }

    public void setKeepAlive(boolean keepAlive) {
        throw new UnsupportedOperationException();
    }

    public void setMaxConnectionsPerHost(int maxHostConnections) {
        throw new UnsupportedOperationException();
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        throw new UnsupportedOperationException();
    }

    public void setStaleCheckingEnabled(boolean value) {
        throw new UnsupportedOperationException();
    }

    /**
	 * 获得HttpUrlConnection链接
	 * 
	 * @return
	 * @throws IOException
	 */
    protected HttpURLConnection getHttpUrlConnection(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setReadTimeout(readTimeOut);
        con.setConnectTimeout(connectTimeOut);
        return con;
    }

    /**
	 * 释放HttpUrlConnection链接
	 * 
	 * @param connection
	 * @throws IOException
	 */
    protected void releaseUrlConnection(HttpURLConnection connection) throws IOException {
        connection.getInputStream().close();
        connection.disconnect();
        connection = null;
    }

    public TaobaoResponse fetch(URL url, Map<String, CharSequence> payload) throws TaobaoApiException {
        TaobaoResponse rsp = null;
        HttpURLConnection connection = null;
        try {
            connection = getHttpUrlConnection(url);
            rsp = _fetch(connection, payload);
        } catch (Exception e) {
            throw new TaobaoApiException(e);
        } finally {
            try {
                releaseUrlConnection(connection);
            } catch (IOException e) {
                throw new TaobaoApiException(e);
            }
        }
        return rsp;
    }

    public TaobaoResponse fetchWithFile(URL url, Map<String, CharSequence> payload, File file) throws TaobaoApiException {
        TaobaoResponse rsp = null;
        HttpURLConnection connection = null;
        try {
            connection = getHttpUrlConnection(url);
            rsp = _fetch(connection, payload, file);
        } catch (Exception e) {
            throw new TaobaoApiException(e);
        } finally {
            try {
                releaseUrlConnection(connection);
            } catch (IOException e) {
                throw new TaobaoApiException(e);
            }
        }
        return rsp;
    }

    protected TaobaoResponse _fetch(HttpURLConnection connection, Map<String, CharSequence> payload) throws IOException {
        String buffer = FetchUtil.paramsToBuffer(payload.entrySet(), "&", "=");
        connection.setRequestMethod(requestMethod);
        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.getOutputStream().write(buffer.getBytes());
        TaobaoResponse response = new TaobaoResponse();
        response.setRequestUrl(connection.getURL().toString());
        response.setRequestBody(buffer);
        response.setRequestParameters(buffer);
        String body = FetchUtil.inputStreamToString(connection.getInputStream());
        String status = connection.getHeaderField("sip_status");
        if (!SIP_STATUS_OK.equals(status)) {
            response.setErrorCode(status);
            response.setMsg(connection.getHeaderField("sip_error_message"));
            if ("1004".equals(status)) {
                response.setRedirectUrl(connection.getHeaderField("sip_isp_loginurl"));
            }
        }
        response.setBody(body);
        return response;
    }

    protected TaobaoResponse _fetch(HttpURLConnection connection, Map<String, CharSequence> payload, File file) throws IOException {
        String boundary = Long.toString(System.currentTimeMillis(), 16);
        byte[] data = null;
        byte[] endData = null;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, CharSequence> entry : payload.entrySet()) {
            sb.append(PREF + boundary + CRLF);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"");
            sb.append(CRLF);
            sb.append("Content-Type: text/plain; charset=utf-8");
            sb.append(CRLF);
            sb.append(CRLF);
            sb.append(entry.getValue().toString());
            sb.append(CRLF);
        }
        sb.append(PREF + boundary + CRLF);
        sb.append("Content-disposition: form-data; name =\"image\"; filename=\"" + file.getName() + "\"" + CRLF);
        sb.append("Content-Type: image/jpeg");
        sb.append(CRLF);
        sb.append("Content-Transfer-Encoding: binary");
        sb.append(CRLF);
        sb.append(CRLF);
        endData = (CRLF + PREF + boundary + PREF + CRLF).getBytes("utf-8");
        data = sb.toString().getBytes("utf-8");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Content-Length", String.valueOf(data.length + file.length() + endData.length));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte b[] = new byte[UPLOAD_BUFFER_SIZE];
        FileInputStream bufin = null;
        try {
            output = (ByteArrayOutputStream) connection.getOutputStream();
            output.write(data);
            bufin = new FileInputStream(file);
            for (int len = 0; (len = bufin.read(b)) > 0; ) {
                output.write(b, 0, len);
            }
            output.write(endData);
            output.flush();
        } finally {
            bufin.close();
            output.close();
        }
        TaobaoResponse response = new TaobaoResponse();
        response.setRequestUrl(connection.getURL().toString());
        response.setRequestBody(output.toString());
        response.setRequestParameters(FetchUtil.paramsToBuffer(payload.entrySet(), "&", "="));
        String body = FetchUtil.inputStreamToString(connection.getInputStream());
        String status = connection.getHeaderField("sip_status");
        if (!SIP_STATUS_OK.equals(status)) {
            response.setErrorCode(status);
            response.setMsg(connection.getHeaderField("sip_error_message"));
            if ("1004".equals(status)) {
                response.setRedirectUrl(connection.getHeaderField("sip_isp_loginurl"));
            }
        }
        response.setBody(body);
        return response;
    }
}

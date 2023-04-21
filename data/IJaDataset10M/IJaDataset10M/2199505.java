package websiteschema.crawler.fb.index;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import websiteschema.utils.StringUtil;

/**
 *
 * @author ray
 */
public class HttpIndexer {

    private HttpClient httpClient;

    private String command = "DREADDDATA";

    private String suffix = "";

    private String ENCODING = "UTF-8";

    private Logger logger = Logger.getLogger(HttpIndexer.class);

    public HttpIndexer() {
        this("search.nyapc.com", 80, "http");
    }

    public HttpIndexer(final String host, int port, final String protocol) {
        this(host, port, protocol, new SimpleHttpConnectionManager());
    }

    public HttpIndexer(final String host, int port, final String protocol, HttpConnectionManager httpConnectionManager) {
        this.httpClient = new HttpClient(httpConnectionManager);
        this.httpClient.getHostConfiguration().setHost(host, port, protocol);
    }

    public HttpIndexer(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String post(Map<String, String> params, String content) throws HttpException, IOException {
        PostMethod postMethod = new PostMethod(command);
        if (null != params && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                if (key == null) {
                    continue;
                }
                String value = entry.getValue();
                postMethod.addParameter(key, value);
            }
        }
        String requestBody = content;
        if (StringUtil.isNotEmpty(suffix)) {
            requestBody += "\n" + suffix;
        }
        postMethod.setRequestBody(requestBody);
        return executeQuery(postMethod);
    }

    private String executeQuery(HttpMethod method) throws HttpException, IOException {
        method.setRequestHeader("Content-Type", "text/plain; charset=" + ENCODING);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        method.setFollowRedirects(false);
        try {
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("获取内容失败: " + method.getStatusLine());
                return null;
            }
            logger.debug("读取内容");
            byte[] responseBody = method.getResponseBody();
            logger.debug("编码内容");
            String response = new String(responseBody, ENCODING);
            logger.debug(response);
            return response;
        } catch (HttpException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            method.releaseConnection();
        }
    }
}

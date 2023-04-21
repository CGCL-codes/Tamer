package edu.richmond.is.webservices.files2exist;

import java.net.URI;
import java.net.URISyntaxException;
import edu.richmond.is.webservices.util.Timer;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.File;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

public class ExistRest {

    private String hostname;

    private int port;

    private boolean ssl;

    private URI lastUri;

    private long lastResponseBodyLength = 0;

    private long lastEntityUploadLength = 0;

    private double lastDownloadSpeedCharPerSecond = 0.0;

    private double lastUploadSpeedCharPerSecond = 0.0;

    private long lastUploadEtMilliSeconds = 0;

    private long lastDownloadEtMilliSeconds = 0;

    private Logger transferLogger = Logger.getLogger("transferlog");

    public ExistRest(String hostname, int port, boolean ssl) {
        this.hostname = hostname;
        this.port = port;
        this.ssl = ssl;
    }

    private void clearLastProperties() {
        this.lastResponseBodyLength = 0;
        this.lastEntityUploadLength = 0;
        this.lastDownloadSpeedCharPerSecond = 0.0;
        this.lastUploadSpeedCharPerSecond = 0.0;
        this.lastUploadEtMilliSeconds = 0;
        this.lastDownloadEtMilliSeconds = 0;
    }

    public long getLastEntityUploadLength() {
        return this.lastEntityUploadLength;
    }

    public long getLastResponseBodyLength() {
        return this.lastResponseBodyLength;
    }

    public long getLastDownloadEtMilliSeconds() {
        return this.lastDownloadEtMilliSeconds;
    }

    public long getLastUploadEtMilliSeconds() {
        return this.lastUploadEtMilliSeconds;
    }

    private void setLastDownloadSpeedCharPerSecond() {
        double bytesPerSecond = this.lastResponseBodyLength / (this.lastDownloadEtMilliSeconds / 1000.0);
        this.lastDownloadSpeedCharPerSecond = bytesPerSecond;
    }

    public long getLastDownloadSpeedCharPerSecond() {
        return (long) (this.lastDownloadSpeedCharPerSecond);
    }

    public long getLastUploadSpeedCharPerSecond() {
        return (long) (this.lastUploadSpeedCharPerSecond);
    }

    public void setLastUploadSpeedCharPerSecond() {
        double bytesPerSecond = this.lastEntityUploadLength / (this.lastUploadEtMilliSeconds / 1000.0);
        this.lastUploadSpeedCharPerSecond = bytesPerSecond;
    }

    public URI getLastUri() {
        return this.lastUri;
    }

    public String restGet(String path, String username, String password) throws Exception {
        this.clearLastProperties();
        Timer sw = new Timer();
        sw.start();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String protocol = "http";
        if (this.ssl) {
            protocol = "https";
        }
        URI uri = URIUtils.createURI(protocol, this.hostname, this.port, path, null, null);
        this.lastUri = uri;
        HttpGet httpget = new HttpGet(uri);
        if (username == null || password == null) {
        } else {
            httpclient.getCredentialsProvider().setCredentials(new AuthScope(this.hostname, 8080, AuthScope.ANY_REALM, "basic"), new UsernamePasswordCredentials(username, password));
        }
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);
        httpclient.getConnectionManager().shutdown();
        sw.stop();
        this.lastResponseBodyLength = responseBody.length();
        this.lastDownloadEtMilliSeconds = sw.getET();
        this.setLastDownloadSpeedCharPerSecond();
        httpclient = null;
        uri = null;
        httpget = null;
        return responseBody;
    }

    public void restPut(File filename, String contentType, String path, String username, String password) throws IOException, URISyntaxException {
        if (contentType == null) {
            contentType = "text/xml";
        }
        this.clearLastProperties();
        Timer sw = new Timer();
        sw.start();
        HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {

            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authState.getAuthScheme() == null) {
                    AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
                    Credentials creds = credsProvider.getCredentials(authScope);
                    if (creds != null) {
                        authState.setAuthScheme(new BasicScheme());
                        authState.setCredentials(creds);
                    }
                }
            }
        };
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.addRequestInterceptor(preemptiveAuth, 0);
        String protocol = "http";
        if (this.ssl) {
            protocol = "https";
        }
        URI uri = new URI(protocol, null, this.hostname, this.port, path, null, null);
        this.lastUri = uri;
        HttpPut httpput = new HttpPut(uri);
        FileEntity myFile = new FileEntity(filename, contentType);
        httpput.setEntity(myFile);
        if (username == null || password == null) {
            transferLogger.warn("********NULL Credentials Detected: " + username + "/" + password + " **************");
        } else {
            transferLogger.debug("********Setting Credentials " + username + "/" + password + " **************");
            httpclient.getCredentialsProvider().setCredentials(new AuthScope(this.hostname, 8080, AuthScope.ANY_REALM, "basic"), new UsernamePasswordCredentials(username, password));
        }
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpput, responseHandler);
        httpclient.getConnectionManager().shutdown();
        sw.stop();
        long sizeOfResponseBody = responseBody.length();
        this.lastEntityUploadLength = filename.length();
        this.lastUploadEtMilliSeconds = sw.getET();
        this.setLastUploadSpeedCharPerSecond();
        httpclient = null;
        uri = null;
        httpput = null;
    }
}

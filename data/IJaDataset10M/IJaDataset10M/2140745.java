package flex.messaging.services.http.proxy;

import org.apache.commons.httpclient.HostConfiguration;
import java.net.URL;

/**
 * @exclude
 * Encapsulates information about a proxy target.
 *
 * @author Brian Deitte
 */
public class Target {

    private URL url;

    private boolean useCustomAuthentication = true;

    private boolean isHTTPS;

    private String encodedPath;

    private String remoteUsername;

    private String remotePassword;

    private HostConfiguration hostConfig;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public boolean isHTTPS() {
        return isHTTPS;
    }

    public void setHTTPS(boolean HTTPS) {
        isHTTPS = HTTPS;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public void setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
    }

    public HostConfiguration getHostConfig() {
        return hostConfig;
    }

    public void setHostConfig(HostConfiguration hostConfig) {
        this.hostConfig = hostConfig;
    }

    public String getRemoteUsername() {
        return remoteUsername;
    }

    public void setRemoteUsername(String name) {
        remoteUsername = name;
    }

    public String getRemotePassword() {
        return remotePassword;
    }

    public void setRemotePassword(String pass) {
        remotePassword = pass;
    }

    public boolean useCustomAuthentication() {
        return useCustomAuthentication;
    }

    public void setUseCustomAuthentication(boolean b) {
        useCustomAuthentication = b;
    }
}

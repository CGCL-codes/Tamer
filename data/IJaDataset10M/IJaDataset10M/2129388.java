package mindbright.net;

import java.io.IOException;

public interface ProxyAuthenticator {

    public String getProxyUsername(String type, String challenge) throws IOException;

    public String getProxyPassword(String type, String challenge) throws IOException;
}

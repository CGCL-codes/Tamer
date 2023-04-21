package issrg.pba.rbac;

import issrg.utils.repository.*;
import issrg.security.SecurityException;
import issrg.saml.SAMLSecurity;
import java.net.*;
import org.apache.log4j.*;

/**
 * This class handles WebDAV URL's, with SSL client authentication details.
 * 
 * @author Sean Antony
 * @version 13/03/2007
 */
public class WebDAVURLHandler extends URLHandler {

    private static Logger logger = Logger.getLogger(WebDAVURLHandler.class.getName());

    private String protocol = null;

    public WebDAVURLHandler(int defaultPort, String protocolIn) {
        super(protocolIn.toLowerCase(), defaultPort);
        this.protocol = protocolIn.toLowerCase();
    }

    public issrg.utils.repository.AttributeRepository getRepository(String url) throws BadURLException {
        if (getProtocolName(url).compareToIgnoreCase(HTTPS_PROTOCOL) == 0) {
            logger.debug("a SSL connection is required");
            try {
                SAMLSecurity security = new SAMLSecurity();
                security.login(3);
                URL up = new URL(url);
                String host = up.getHost();
                int port = up.getPort();
                if (port != -1) defaultPort = port;
                return new WebDAVRepository(host, defaultPort, security);
            } catch (MalformedURLException me) {
                throw new BadURLException("Wrong URL syntax: " + url + " is encountered");
            } catch (SecurityException se) {
                throw new BadURLException("security object error: " + se);
            } catch (Exception e) {
                throw new BadURLException("login error: " + e);
            }
        } else if (getProtocolName(url).compareToIgnoreCase(HTTP_PROTOCOL) == 0) {
            logger.debug("non-SSL connection is required");
            try {
                URL up = new URL(url);
                String host = up.getHost();
                int port = up.getPort();
                if (port != -1) defaultPort = port;
                return new WebDAVRepository(host, defaultPort);
            } catch (MalformedURLException me) {
                throw new BadURLException("Wrong URL syntax: " + url + " is encountered");
            }
        } else {
            throw new BadURLException("Wrong URL Handler: " + url);
        }
    }

    public String getProtocol() {
        return this.protocol;
    }
}

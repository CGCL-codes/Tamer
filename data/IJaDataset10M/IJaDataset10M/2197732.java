package org.opennms.netmgt.provision.detector.radius.client;

import java.io.IOException;
import java.net.InetAddress;
import net.sourceforge.jradiusclient.RadiusClient;
import net.sourceforge.jradiusclient.RadiusPacket;
import org.opennms.netmgt.provision.support.Client;

/**
 * @author Donald Desloge
 *
 */
public class RadiusDetectorClient implements Client<RadiusPacket, RadiusPacket> {

    /**
     * Default radius authentication port
     */
    public static final int DEFAULT_AUTH_PORT = 1812;

    /**
     * Default radius accounting port
     */
    public static final int DEFAULT_ACCT_PORT = 1813;

    /**
     * Default secret
     */
    public static final String DEFAULT_SECRET = "secret123";

    private RadiusClient m_radiusClient;

    private int m_authport = DEFAULT_AUTH_PORT;

    private int m_acctport = DEFAULT_ACCT_PORT;

    private String m_secret = DEFAULT_SECRET;

    public void close() {
    }

    public void connect(InetAddress address, int port, int timeout) throws IOException, Exception {
        m_radiusClient = new RadiusClient(address.getCanonicalHostName(), getAuthPort(), getAcctPort(), getSecret(), timeout);
    }

    public RadiusPacket receiveBanner() throws IOException {
        return null;
    }

    public RadiusPacket sendRequest(RadiusPacket request) throws Exception {
        return m_radiusClient.authenticate(request);
    }

    public void setAuthport(int authport) {
        m_authport = authport;
    }

    public int getAuthPort() {
        return m_authport;
    }

    public void setAcctPort(int acctport) {
        m_acctport = acctport;
    }

    public int getAcctPort() {
        return m_acctport;
    }

    public void setSecret(String secret) {
        m_secret = secret;
    }

    public String getSecret() {
        return m_secret;
    }
}

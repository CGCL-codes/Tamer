package org.opennms.netmgt.provision.detector;

import java.io.IOException;
import java.net.DatagramPacket;
import org.opennms.netmgt.provision.support.BasicDetector;
import org.opennms.netmgt.provision.support.Client;
import org.opennms.netmgt.provision.support.ClientConversation.ResponseValidator;
import org.opennms.netmgt.provision.support.dns.DNSAddressRequest;

/**
 * @author brozow
 *
 */
public class DnsDetector extends BasicDetector<DatagramPacket, DatagramPacket> {

    /**
     * </P>
     * The default port on which the host is checked to see if it supports DNS.
     * </P>
     */
    private static final int DEFAULT_PORT = 53;

    /**
     * Default number of retries for DNS requests
     */
    private static final int DEFAULT_RETRY = 3;

    /**
     * Default timeout (in milliseconds) for DNS requests.
     */
    private static final int DEFAULT_TIMEOUT = 3000;

    /**
     * Default DNS lookup
     */
    private static final String DEFAULT_LOOKUP = "localhost";

    private String m_lookup = DEFAULT_LOOKUP;

    /**
     * @param defaultPort
     * @param defaultTimeout
     * @param defaultRetries
     */
    public DnsDetector() {
        super(DEFAULT_PORT, DEFAULT_TIMEOUT, DEFAULT_RETRY);
    }

    public void onInit() {
        DNSAddressRequest req;
        send(encode(req = addrRequest(getLookup())), verifyResponse(req));
    }

    /**
     * @param request
     * @return
     */
    private ResponseValidator<DatagramPacket> verifyResponse(final DNSAddressRequest request) {
        return new ResponseValidator<DatagramPacket>() {

            public boolean validate(DatagramPacket response) {
                try {
                    System.out.println("\n Yo we got something back\n");
                    request.verifyResponse(response.getData(), response.getLength());
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        };
    }

    private DNSAddressRequest addrRequest(String host) {
        return new DNSAddressRequest(host);
    }

    private DatagramPacket encode(DNSAddressRequest dnsPacket) {
        byte[] data = buildRequest(dnsPacket);
        return new DatagramPacket(data, data.length);
    }

    /**
     * @param request
     * @return
     * @throws IOException
     */
    private byte[] buildRequest(DNSAddressRequest request) {
        try {
            return request.buildRequest();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to build dnsRequest!!! This can't happen!!");
        }
    }

    @Override
    protected Client<DatagramPacket, DatagramPacket> getClient() {
        return new DatagramClient();
    }

    /**
     * @param lookup the lookup to set
     */
    public void setLookup(String lookup) {
        m_lookup = lookup;
    }

    /**
     * @return the lookup
     */
    public String getLookup() {
        return m_lookup;
    }
}

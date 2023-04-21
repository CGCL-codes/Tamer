package org.opennms.netmgt.provision.detector.smb;

import org.opennms.netmgt.provision.detector.simple.request.LineOrientedRequest;
import org.opennms.netmgt.provision.detector.smb.client.SmbClient;
import org.opennms.netmgt.provision.detector.smb.response.NbtAddressResponse;
import org.opennms.netmgt.provision.support.BasicDetector;
import org.opennms.netmgt.provision.support.Client;
import org.opennms.netmgt.provision.support.ClientConversation.ResponseValidator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Donald Desloge
 *
 */
@Component
@Scope("prototype")
public class SmbDetector extends BasicDetector<LineOrientedRequest, NbtAddressResponse> {

    private static final String DEFAULT_SERVICE_NAME = "SMB";

    private static final int DEFAULT_RETRIES = 0;

    private static final int DEFAULT_TIMEOUT = 1000;

    private static final int DEFAULT_PORT = 0;

    /**
     * Default constructor
     */
    public SmbDetector() {
        super(DEFAULT_SERVICE_NAME, DEFAULT_PORT, DEFAULT_TIMEOUT, DEFAULT_RETRIES);
    }

    /**
     * Constructor for instantiating a non-default service name of this protocol
     * @param serviceName
     */
    public SmbDetector(String serviceName) {
        super(serviceName, DEFAULT_PORT, DEFAULT_TIMEOUT, DEFAULT_RETRIES);
    }

    /**
     * Constructor for overriding defaults
     * @param serviceName
     * @param timeout
     * @param retries
     */
    public SmbDetector(String serviceName, int timeout, int retries) {
        super(serviceName, DEFAULT_PORT, timeout, retries);
    }

    @Override
    protected void onInit() {
        expectBanner(validateAddressIsNotSame());
    }

    private ResponseValidator<NbtAddressResponse> validateAddressIsNotSame() {
        return new ResponseValidator<NbtAddressResponse>() {

            public boolean validate(NbtAddressResponse response) throws Exception {
                return response.validateAddressIsNotSame();
            }
        };
    }

    @Override
    protected Client<LineOrientedRequest, NbtAddressResponse> getClient() {
        return new SmbClient();
    }
}

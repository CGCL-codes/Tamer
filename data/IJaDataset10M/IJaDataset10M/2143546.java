package org.opennms.netmgt.provision.detector.dhcp;

import org.opennms.netmgt.provision.detector.dhcp.client.DhcpClient;
import org.opennms.netmgt.provision.detector.dhcp.request.DhcpRequest;
import org.opennms.netmgt.provision.detector.dhcp.response.DhcpResponse;
import org.opennms.netmgt.provision.support.BasicDetector;
import org.opennms.netmgt.provision.support.Client;
import org.opennms.netmgt.provision.support.ClientConversation.ResponseValidator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DhcpDetector extends BasicDetector<DhcpRequest, DhcpResponse> {

    private static final int DEFAULT_RETRY = 0;

    private static final int DEFAULT_TIMEOUT = 3000;

    public DhcpDetector() {
        super("DCHP", 0);
        setTimeout(DEFAULT_TIMEOUT);
        setRetries(DEFAULT_RETRY);
    }

    @Override
    protected void onInit() {
        expectBanner(responseTimeGreaterThan(-1));
    }

    private ResponseValidator<DhcpResponse> responseTimeGreaterThan(final long num) {
        return new ResponseValidator<DhcpResponse>() {

            public boolean validate(DhcpResponse response) throws Exception {
                return response.validate(num);
            }
        };
    }

    @Override
    protected Client<DhcpRequest, DhcpResponse> getClient() {
        DhcpClient client = new DhcpClient();
        client.setRetries(1);
        return client;
    }
}

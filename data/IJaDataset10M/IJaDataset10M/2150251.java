package org.opennms.netmgt.provision.detector.snmp;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Win32ServiceDetector extends SnmpDetector {

    private static final String SV_SVC_OPERATING_STATE_OID = ".1.3.6.1.4.1.77.1.2.3.1.3";

    private static final String DEFAULT_SERVICE_NAME = "Service";

    public Win32ServiceDetector() {
        setServiceName(DEFAULT_SERVICE_NAME);
        setOid(generateOid());
        setVbvalue("1");
    }

    private String generateOid() {
        int snLength = getServiceName().length();
        StringBuffer serviceOidBuf = new StringBuffer(SV_SVC_OPERATING_STATE_OID);
        serviceOidBuf.append(".").append(Integer.toString(snLength));
        for (byte thisByte : getServiceName().getBytes()) {
            serviceOidBuf.append(".").append(Byte.toString(thisByte));
        }
        return serviceOidBuf.toString();
    }
}

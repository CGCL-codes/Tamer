package test.unit.be.fedict.eid.dss;

import javax.security.auth.x500.X500Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class SignatureVerificationServiceBeanTest {

    private static final Log LOG = LogFactory.getLog(SignatureVerificationServiceBeanTest.class);

    @Test
    public void testExtractSerialNumberFromDN() throws Exception {
        String dn = "SERIALNUMBER=71715100070, GIVENNAME=Alice Geldigekaart2266, SURNAME=SPECIMEN, CN=Alice SPECIMEN (Authentication), C=BE";
        X500Principal principal = new X500Principal(dn);
        LOG.debug("principal: " + principal);
    }
}

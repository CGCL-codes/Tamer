package org.gridtrust.srb.demo;

import junit.framework.TestCase;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gridtrust.Constants;
import org.gridtrust.ppm.Policy;
import org.gridtrust.ppm.ServiceProvider;
import org.gridtrust.ppm.UserProfile;
import org.gridtrust.srb.SRBManagerPortType;
import org.gridtrust.srb.service.SRBManagerServiceAddressingLocator;
import org.gridtrust.util.XMLUtil;

public class AbstractSRBDemoTestCase extends TestCase {

    String noSecSRBURI = "http://127.0.1.1";

    String secSRBURI = "https://127.0.1.1";

    String defaultUnsecPort = "8080";

    String defaultSecPort = "8443";

    String srbPath = "/wsrf/services/gridtrust/srb/SRBManager";

    protected SRBManagerPortType srbManagerPort = null;

    private static final Log log = LogFactory.getLog(AbstractSRBDemoTestCase.class);

    @Override
    protected void setUp() throws Exception {
        log.info("Please make sure GridTrust server up and running");
        SRBManagerServiceAddressingLocator locator = new SRBManagerServiceAddressingLocator();
        EndpointReferenceType endpoint = new EndpointReferenceType();
        String secured = System.getProperty("gridtrust.security");
        String monitorPort = System.getProperty("gridtrust.monitor.port");
        boolean isSecured = false;
        String srbURI = noSecSRBURI;
        if (secured != null) {
            isSecured = Boolean.parseBoolean(secured);
            if (isSecured) {
                if (monitorPort != null) {
                    secSRBURI = secSRBURI + ":" + monitorPort + srbPath;
                } else {
                    secSRBURI = secSRBURI + ":" + defaultSecPort + srbPath;
                }
                srbURI = secSRBURI;
                String trustStore = System.getProperty("javax.net.ssl.trustStore");
                String keyStore = System.getProperty("javax.net.ssl.keyStore");
                String keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
                if (trustStore == null) {
                    log.error("Please set javax.net.ssl.trustStore property");
                    fail();
                }
                if (keyStore == null) {
                    log.error("Please set javax.net.ssl.keyStore property");
                    fail();
                }
                if (keyStorePassword == null) {
                    log.error("Please set javax.net.ssl.keyStorePassword property");
                    fail();
                }
            }
        } else {
            if (monitorPort != null) {
                noSecSRBURI = noSecSRBURI + ":" + monitorPort + srbPath;
            } else {
                noSecSRBURI = noSecSRBURI + ":" + defaultUnsecPort + srbPath;
            }
            srbURI = noSecSRBURI;
        }
        System.out.println("SRB URI " + srbURI);
        endpoint.setAddress(new Address(srbURI));
        srbManagerPort = locator.getSRBManagerPortTypePort(endpoint);
    }

    protected String loadWSDL(String wsdlName) {
        String wsdl = null;
        XMLUtil xmlUtil = new XMLUtil();
        wsdl = xmlUtil.loadXMLFileContent(wsdlName);
        return wsdl;
    }

    protected Policy getValidServicePolicy() {
        Policy servicePolicy = new Policy();
        servicePolicy.setPolicyInfoRef(Constants.XACML_2_0);
        XMLUtil xmlUtil = new XMLUtil();
        servicePolicy.setPolicyContent(xmlUtil.loadXMLFileContent("service_policy.xml"));
        return servicePolicy;
    }

    protected Policy getValidVOPolicy() {
        Policy voPolicy = new Policy();
        voPolicy.setPolicyInfoRef(Constants.XACML_2_0);
        XMLUtil xmlUtil = new XMLUtil();
        voPolicy.setPolicyContent(xmlUtil.loadXMLFileContent("vo_policy.xml"));
        return voPolicy;
    }

    protected String[] getUserIdList(UserProfile[] userProfileList) {
        String[] userIdList = new String[userProfileList.length];
        int counter = 0;
        for (UserProfile userProfile : userProfileList) {
            userIdList[counter] = userProfile.getUserId();
            counter++;
        }
        return userIdList;
    }

    protected String[] getSPIdList(ServiceProvider[] serviceProviderList) {
        String[] spIdList = new String[serviceProviderList.length];
        int counter = 0;
        for (ServiceProvider serviceProvider : serviceProviderList) {
            spIdList[counter] = serviceProvider.getSpId();
            counter++;
        }
        return spIdList;
    }
}

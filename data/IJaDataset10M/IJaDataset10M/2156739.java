package org.wijiscommons.ssaf.monitoring;

import java.util.HashMap;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.wijiscommons.ssaf.util.dom.DomUtils;

public class WebServicesMonitoringTest extends TestCase {

    private ApplicationContext ac;

    private WebServicesMonitoring webServicesMonitoringBean;

    protected void setUp() throws Exception {
        super.setUp();
        ac = new FileSystemXmlApplicationContext(new String[] { "/deployment-descriptors/monitoredWebServicesProperties.xml" });
        webServicesMonitoringBean = (WebServicesMonitoring) ac.getBean("webServicesMonitoringBean");
    }

    public void testGetWebServiceStatusXMLDocument() {
        String response = null;
        response = DomUtils.getStringFromDocument(webServicesMonitoringBean.getWebServiceStatusXMLDocument());
        System.out.println("Status XML created : " + response);
        assertNotNull(response);
    }

    public void testGetURLAttibutes() {
        String serviceURL = "https://lakshmi.doa.wistate.us:8444/SSAFDropOffWebService/DropOffSynchronousService";
        HashMap<String, String> urlAttributes = webServicesMonitoringBean.getURLAttibutes(serviceURL);
        assertNotNull(urlAttributes);
        System.out.println("Host name from web service URL" + urlAttributes.get("hostName"));
        System.out.println("Port number from web service URL : " + urlAttributes.get("portNumber"));
        System.out.println("Relative path from web service URL : " + urlAttributes.get("relativeURL"));
    }
}

package de.dgrid.bisgrid.services.proxy;

import junit.framework.TestCase;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.codehaus.xfire.transport.http.EasySSLProtocolSocketFactory;
import de.fzj.unicore.wsrflite.security.ISecurityProperties;

/**
 * 
 * TODO: FIX Test. Test did not work here and there should be no dependency to the workflow service (otherwise circle in the pom dependencies) 

 * @author Dirk Meister
 *
 */
public class UNICOREProxyServiceTest extends TestCase {

    public void testDummy() {
        assertTrue(true);
    }

    public void testServiceInstanceCreation() throws Exception {
        ISecurityProperties sec = new TestSecurityProperties();
        String workFlowServiceResourceID = "0";
        System.out.println("Post request to interrupt");
        Protocol.registerProtocol("https", new Protocol("https", new EasySSLProtocolSocketFactory(), 443));
        HttpClient client = new HttpClient();
        HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setProxy("localhost", 8091);
        PostMethod post = new PostMethod("https://localhost:9004/HotelService/");
        post.setRequestEntity(new StringRequestEntity("<?xml version='1.0' ?>" + "<env:Envelope" + "        xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\">" + "  <env:Header>" + "    <WWID>DefaultWorkflowService:" + workFlowServiceResourceID + "</WWID>" + "  </env:Header>" + "  <env:Body>" + "  </env:Body>" + "</env:Envelope>", "text/xml", "UTF-8"));
        client.executeMethod(hostConfig, post);
        System.out.println(post.getResponseBodyAsString());
        GetMethod get = new GetMethod("https://www.upb.de");
        client.executeMethod(hostConfig, get);
        System.out.println(get.getResponseBodyAsString());
    }
}

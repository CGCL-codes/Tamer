package org.mobicents.server.xdm.appusage.test;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.management.RuntimeMBeanException;
import javax.naming.NamingException;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;
import org.mobicents.xcap.client.XcapClient;
import org.mobicents.xcap.client.XcapResponse;
import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.impl.XcapClientImpl;
import org.mobicents.xcap.client.uri.DocumentSelectorBuilder;
import org.mobicents.xcap.client.uri.UriBuilder;
import org.openxdm.xcap.common.error.InternalServerErrorException;
import org.openxdm.xcap.common.error.NotUTF8ConflictException;
import org.openxdm.xcap.common.error.NotWellFormedConflictException;
import org.openxdm.xcap.common.xml.TextWriter;
import org.openxdm.xcap.common.xml.XMLValidator;
import org.openxdm.xcap.server.slee.appusage.rlsservices.RLSServicesAppUsage;

public class EncodedResourceListsXUITest extends AbstractT {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(EncodedResourceListsXUITest.class);
    }

    private String user = "sip:vasily@ericsson.com";

    private String documentName = "index";

    @Test
    public void test() throws IOException, JAXBException, InterruptedException, TransformerException, NotWellFormedConflictException, NotUTF8ConflictException, InternalServerErrorException, URISyntaxException, InstanceNotFoundException, MBeanException, ReflectionException, MalformedObjectNameException, NullPointerException, NamingException {
        initRmiAdaptor();
        try {
            createUser(user, "password");
        } catch (RuntimeMBeanException e) {
            if (!(e.getCause() instanceof IllegalStateException)) {
                e.printStackTrace();
            }
        }
        XcapClient client = new XcapClientImpl();
        Credentials credentials = client.getCredentialsFactory().getHttpDigestCredentials(user, "password");
        String documentSelector = DocumentSelectorBuilder.getUserDocumentSelectorBuilder(RLSServicesAppUsage.ID, user, documentName).toPercentEncodedString();
        UriBuilder uriBuilder = new UriBuilder().setSchemeAndAuthority("http://localhost:8080").setXcapRoot("/mobicents/").setDocumentSelector(documentSelector);
        URI documentURI = uriBuilder.toURI();
        InputStream is = this.getClass().getResourceAsStream("example_encodedResourceListsXUI.xml");
        String content = TextWriter.toString(XMLValidator.getWellFormedDocument(XMLValidator.getUTF8Reader(is)));
        is.close();
        XcapResponse response = client.put(documentURI, RLSServicesAppUsage.MIMETYPE, content, null, credentials);
        System.out.println("Response got:\n" + response);
        assertTrue("Put response must exists and status code must be 200 or 201", response != null && (response.getCode() == 200 || response.getCode() == 201));
        client.delete(documentURI, null, credentials);
        client.shutdown();
        removeUser(user);
    }
}

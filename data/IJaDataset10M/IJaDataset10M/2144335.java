package org.openxdm.xcap.client.test.error;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.xml.bind.JAXBException;
import junit.framework.JUnit4TestAdapter;
import org.apache.commons.httpclient.HttpException;
import org.junit.Test;
import org.openxdm.xcap.client.Response;
import org.openxdm.xcap.client.test.AbstractXDMJunitOldClientTest;
import org.openxdm.xcap.common.error.MethodNotAllowedException;
import org.openxdm.xcap.common.key.UserDocumentUriKey;
import org.openxdm.xcap.common.uri.ElementSelector;
import org.openxdm.xcap.common.uri.ElementSelectorStep;

public class MethodNotAllowedTest extends AbstractXDMJunitOldClientTest {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MethodNotAllowedTest.class);
    }

    @Test
    public void test() throws HttpException, IOException, JAXBException, InterruptedException {
        UserDocumentUriKey key = new UserDocumentUriKey(appUsage.getAUID(), user, documentName);
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<resource-lists xmlns=\"urn:ietf:params:xml:ns:resource-lists\">" + "<list/>" + "</resource-lists>";
        Response putResponse = client.put(key, appUsage.getMimetype(), content, null);
        assertTrue("Put response must exists", putResponse != null);
        assertTrue("Put response code should be 201", putResponse.getCode() == 201);
        LinkedList<ElementSelectorStep> elementSelectorSteps = new LinkedList<ElementSelectorStep>();
        ElementSelectorStep step1 = new ElementSelectorStep("pre:resource-lists");
        ElementSelectorStep step2 = new ElementSelectorStep("pre:list");
        elementSelectorSteps.add(step1);
        elementSelectorSteps.addLast(step2);
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("pre", appUsage.getDefaultDocumentNamespace());
        MethodNotAllowedTestFakeUserDocumentUriKey nsKey = new MethodNotAllowedTestFakeUserDocumentUriKey(appUsage.getAUID(), user, documentName, new ElementSelector(elementSelectorSteps), namespaces);
        Response nsPutResponse = client.put(nsKey, appUsage.getMimetype(), content, null);
        assertTrue("Put response must exists", nsPutResponse != null);
        assertTrue("Put response code should be " + MethodNotAllowedException.RESPONSE_STATUS, nsPutResponse.getCode() == MethodNotAllowedException.RESPONSE_STATUS);
        Response nsDeleteResponse = client.delete(nsKey, null);
        assertTrue("Put response must exists", nsDeleteResponse != null);
        assertTrue("Put response code should be " + MethodNotAllowedException.RESPONSE_STATUS, nsDeleteResponse.getCode() == MethodNotAllowedException.RESPONSE_STATUS);
        client.delete(key, null);
    }
}

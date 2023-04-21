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
import org.openxdm.xcap.common.error.BadRequestException;
import org.openxdm.xcap.common.key.UserAttributeUriKey;
import org.openxdm.xcap.common.key.UserDocumentUriKey;
import org.openxdm.xcap.common.resource.AttributeResource;
import org.openxdm.xcap.common.uri.AttributeSelector;
import org.openxdm.xcap.common.uri.ElementSelector;
import org.openxdm.xcap.common.uri.ElementSelectorStep;
import org.openxdm.xcap.common.uri.ElementSelectorStepByPos;

public class BadRequestTest extends AbstractXDMJunitOldClientTest {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BadRequestTest.class);
    }

    @Test
    public void test() throws HttpException, IOException, JAXBException, InterruptedException {
        UserDocumentUriKey key = new UserDocumentUriKey(appUsage.getAUID(), user, documentName);
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<resource-lists xmlns=\"urn:ietf:params:xml:ns:resource-lists\">" + "<list/>" + "<list name=\"enemies\"/>" + "</resource-lists>";
        Response response = client.put(key, appUsage.getMimetype(), content, null);
        assertTrue("Put response must exists", response != null);
        assertTrue("Put response code should be 201", response.getCode() == 201);
        LinkedList<ElementSelectorStep> elementSelectorSteps = new LinkedList<ElementSelectorStep>();
        ElementSelectorStep step1 = new ElementSelectorStep("pre1:resource-lists");
        ElementSelectorStep step2 = new ElementSelectorStepByPos("pre2:list", 1);
        elementSelectorSteps.add(step1);
        elementSelectorSteps.addLast(step2);
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("pre1", appUsage.getDefaultDocumentNamespace());
        UserAttributeUriKey attrKey = new UserAttributeUriKey(appUsage.getAUID(), user, documentName, new ElementSelector(elementSelectorSteps), new AttributeSelector("name"), namespaces);
        Response putResponse = client.put(attrKey, AttributeResource.MIMETYPE, "friends", null);
        assertTrue("Put response must exists", putResponse != null);
        assertTrue("Put response code should be " + BadRequestException.RESPONSE_STATUS, putResponse.getCode() == BadRequestException.RESPONSE_STATUS);
        elementSelectorSteps.removeLast();
        step2 = new ElementSelectorStepByPos("pre2:list", 2);
        elementSelectorSteps.addLast(step2);
        UserAttributeUriKey anotherAttrKey = new UserAttributeUriKey(appUsage.getAUID(), user, documentName, new ElementSelector(elementSelectorSteps), new AttributeSelector("name"), null);
        Response getResponse = client.get(anotherAttrKey, null);
        assertTrue("Delete response must exists", getResponse != null);
        assertTrue("Delete response code should be " + BadRequestException.RESPONSE_STATUS, getResponse.getCode() == BadRequestException.RESPONSE_STATUS);
        Response deleteResponse = client.delete(anotherAttrKey, null);
        assertTrue("Delete response must exists", deleteResponse != null);
        assertTrue("Delete response code should be " + BadRequestException.RESPONSE_STATUS, deleteResponse.getCode() == BadRequestException.RESPONSE_STATUS);
        client.delete(key, null);
    }
}

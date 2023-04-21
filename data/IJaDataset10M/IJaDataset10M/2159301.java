package com.meschbach.psi.util;

import com.meschbach.psi.PSIException;
import com.meschbach.psi.jetty6.LocalJetty6Builder;
import com.meschbach.psi.util.rest.PostRequest;
import com.meschbach.psi.util.rest.PutRequest;
import com.meschbach.psi.util.rest.RequestBuilder;
import com.meschbach.psi.util.rest.ResponseEntityAssertion;
import com.meschbach.psi.util.rest.StatusAssertionResponseHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author "Mark Eschbach" meschbach@gmail.com
 */
public class Issue11Test {

    WebAppHarness harness;

    final String testEntity = "This is an a test entity.  Hopefully this will work.";

    @BeforeMethod
    public void setupHarness() throws PSIException {
        harness = new WebAppHarness(new LocalJetty6Builder(), "echo-webapp", "/Echo");
        harness.start();
    }

    @AfterMethod
    public void takeDownHarness() throws PSIException {
        harness.shutdown();
    }

    private void doTest(RequestBuilder builder) throws PSIException {
        RESTClient client = new RESTClient(harness.getURL("/EchoEntity"), builder);
        client.addHandler(new StatusAssertionResponseHandler(HttpStatusCode.Ok));
        client.addHandler(new ResponseEntityAssertion(testEntity));
        client.doRequest();
    }

    @Test
    public void testPutEntity() throws PSIException {
        doTest(new PutRequest(testEntity));
    }

    @Test
    public void testPostEntity() throws PSIException {
        doTest(new PostRequest(testEntity));
    }
}

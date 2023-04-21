package test.com.aytul.janissary.element;

import junit.framework.TestCase;
import test.com.aytul.janissary.misc.ElementFactory;
import com.aytul.janissary.element.Request;

public class TestRequest extends TestCase {

    public TestRequest(String testName) {
        super(testName);
    }

    public Request createRequest() {
        Request request = new Request();
        request.setName(name);
        request.setUri(uri);
        request.setMethod(method);
        request.setAuthenticate(authenticate);
        request.setCount(count);
        request.setParameters(parameters);
        return request;
    }

    public void testRequest() {
        Request request = createRequest();
        ElementFactory rf = new ElementFactory();
        Request request2 = rf.buildRequest(name, uri, method, authenticate, count, parameters);
        assertEquals(request.getName(), request2.getName());
        assertEquals(request.getUri(), request2.getUri());
        assertEquals(request.getMethod(), request2.getMethod());
        assertEquals(request.isAuthenticate(), request2.isAuthenticate());
        assertEquals(request.getCount(), request2.getCount());
        assertEquals(request.getParameters(), request2.getParameters());
    }

    private Request request = new Request();

    private String name = "Test Request";

    private int count = 80;

    private boolean authenticate = true;

    private String parameters = "params";

    private String method = "method";

    private String uri = "uri";
}

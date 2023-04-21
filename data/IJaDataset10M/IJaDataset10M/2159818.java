package lu.ng.urlchecker.communication;

import static org.junit.Assert.assertEquals;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.junit.Test;

/**
 * @author georgosn
 * 
 */
public class HTTPMethodsTest {

    private HTTPMethods method;

    /**
     * Test method for
     * {@link lu.ng.urlchecker.communication.HTTPMethods#getMethod(java.lang.String)}
     * .
     */
    @Test
    public void testGetMethod() {
        method = HTTPMethods.HEAD;
        assertEquals(HeadMethod.class, method.getMethod("http://www.in.gr").getClass());
        method = HTTPMethods.PUT;
        assertEquals(PutMethod.class, method.getMethod("http://www.in.gr").getClass());
        method = HTTPMethods.DELETE;
        assertEquals(DeleteMethod.class, method.getMethod("http://www.in.gr").getClass());
        method = HTTPMethods.POST;
        assertEquals(PostMethod.class, method.getMethod("http://www.in.gr").getClass());
        method = HTTPMethods.GET;
        assertEquals(GetMethod.class, method.getMethod("http://www.in.gr").getClass());
    }
}

package fast4j.ejbbridge;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import fast4j.ejbbridge.ServiceResult;
import junit.framework.TestCase;

public class ServiceResultTest extends TestCase {

    public void testSerialization() throws Exception {
        final Object value = "hello world";
        final Map context = new HashMap();
        context.put("mykey", "key");
        final ServiceResult result = new ServiceResult(value, context);
        assertEquals(value, result.getValue());
        assertEquals(context, result.getContext());
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        final ObjectOutputStream output = new ObjectOutputStream(buffer);
        output.writeObject(result);
        output.close();
        final ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        final ServiceResult testResult = (ServiceResult) input.readObject();
        assertEquals(value, testResult.getValue());
        assertEquals(context, testResult.getContext());
        assertTrue(result.equals(testResult));
        assertTrue(testResult.equals(result));
        assertEquals(result.hashCode(), testResult.hashCode());
    }
}

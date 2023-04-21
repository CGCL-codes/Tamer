package org.apache.axis2.jaxws.rpclit.stringarray.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.axis2.jaxws.framework.AbstractTestCase;
import org.apache.axis2.jaxws.rpclit.stringarray.sei.Echo;
import org.apache.axis2.jaxws.rpclit.stringarray.sei.RPCLitStringArrayService;
import org.test.rpclit.stringarray.StringArray;
import javax.xml.ws.BindingProvider;
import java.util.Arrays;

public class RPCLitStringArrayTests extends AbstractTestCase {

    public static Test suite() {
        return getTestSetup(new TestSuite(RPCLitStringArrayTests.class));
    }

    public void testStringArrayType() throws Exception {
        System.out.println("------------------------------");
        System.out.println("Test : " + getName());
        RPCLitStringArrayService service = new RPCLitStringArrayService();
        Echo portType = service.getEchoPort();
        BindingProvider p = (BindingProvider) portType;
        p.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:6060/axis2/services/RPCLitStringArrayService.EchoImplPort");
        String[] strArray = { "str1", "str2", "str3", "str4 5" };
        StringArray array = new StringArray();
        array.getItem().addAll(Arrays.asList(strArray));
        StringArray result = portType.echoStringArray(array);
        assertEquals(array.getItem().size(), result.getItem().size());
        for (int i = 0; i < array.getItem().size(); i++) {
            assertEquals(array.getItem().get(i), result.getItem().get(i));
        }
        String[] strArray2 = { "str1", "str2", "str3", "str4 5" };
        array = new StringArray();
        array.getItem().addAll(Arrays.asList(strArray2));
        result = portType.echoStringArray(array);
        assertEquals(array.getItem().size(), result.getItem().size());
        for (int i = 0; i < array.getItem().size(); i++) {
            assertEquals(array.getItem().get(i), result.getItem().get(i));
        }
        System.out.print("---------------------------------");
    }

    public void testStringArrayTypeNoSEI() throws Exception {
        System.out.println("------------------------------");
        System.out.println("Test : " + getName());
        RPCLitStringArrayService service = new RPCLitStringArrayService();
        Echo portType = service.getEchoPort();
        BindingProvider p = (BindingProvider) portType;
        p.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:6060/axis2/services/RPCLitStringArrayService.EchoNoSEIPort");
        String[] strArray = { "str1", "str2", "str3", "str4 5" };
        StringArray array = new StringArray();
        array.getItem().addAll(Arrays.asList(strArray));
        StringArray result = portType.echoStringArray(array);
        assertEquals(array.getItem().size(), result.getItem().size());
        for (int i = 0; i < array.getItem().size(); i++) {
            assertEquals(array.getItem().get(i) + "return", result.getItem().get(i));
        }
        String[] strArray2 = { "str1", "str2", "str3", "str4 5" };
        array = new StringArray();
        array.getItem().addAll(Arrays.asList(strArray2));
        result = portType.echoStringArray(array);
        assertEquals(array.getItem().size(), result.getItem().size());
        for (int i = 0; i < array.getItem().size(); i++) {
            assertEquals(array.getItem().get(i) + "return", result.getItem().get(i));
        }
        System.out.print("---------------------------------");
    }
}

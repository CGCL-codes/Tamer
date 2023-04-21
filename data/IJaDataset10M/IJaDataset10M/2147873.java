package com.riversoforion.acheron.jaxb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.junit.Test;

public class JaxbUtilsTest {

    @Test
    public void testMarshal() throws Exception {
        JAXBContext ctx = new JAXBContextBuilder().bindClasses(JaxbTestObject.class).build();
        Marshaller marshaller = ctx.createMarshaller();
        final JaxbTestObject test = new JaxbTestObject();
        test.setName("Cookie Monster");
        test.setValue(2000000);
        String marshalled = JaxbUtils.marshal(test, marshaller);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><test-object><name>Cookie Monster</name><value>2000000</value></test-object>", marshalled);
    }

    @Test
    public void testUnmarshal_StringUnmarshaller() throws Exception {
        JAXBContext ctx = new JAXBContextBuilder().bindClasses(JaxbTestObject.class).build();
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        final String xml = "<test-object><name>Jones</name><value>25</value></test-object>";
        JaxbTestObject unmarshalled = JaxbUtils.unmarshal(xml, unmarshaller);
        assertNotNull(unmarshalled);
        assertEquals("Jones", unmarshalled.getName());
        assertEquals(25, unmarshalled.getValue());
    }

    @Test
    public void testUnmarshal_StringClassOfT() {
        final String xml = "<test-object><name>Jones</name><value>25</value></test-object>";
        JaxbTestObject unmarshalled = JaxbUtils.unmarshal(xml, JaxbTestObject.class);
        assertNotNull(unmarshalled);
        assertEquals("Jones", unmarshalled.getName());
        assertEquals(25, unmarshalled.getValue());
    }
}

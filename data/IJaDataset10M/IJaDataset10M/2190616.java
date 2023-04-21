package com.izforge.soapdtc.servlet;

import junit.framework.TestCase;
import java.io.IOException;
import java.io.StringWriter;

public class StaticResourceWsdlProviderTest extends TestCase {

    public void testWrite() throws IOException {
        StaticResourceWsdlProvider provider = new StaticResourceWsdlProvider("not-a-wsdl.txt");
        StringWriter stringWriter = new StringWriter();
        provider.writeWsdl(stringWriter);
        stringWriter.close();
        assertEquals("This is not a WSDL document.", stringWriter.toString());
    }
}

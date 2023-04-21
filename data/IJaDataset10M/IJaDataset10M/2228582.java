package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

import mock.javax.servlet.ServletContextMock;
import org.xml.sax.InputSource;
import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.volantis.xml.pipeline.sax.drivers.webservice.ServletContextResource;

public class ServletContextResourceTestCase extends WSDLResourceTestAbstract {

    public void testProvideInputSource() throws Exception {
        final ServletContextMock servletContextMock = new ServletContextMock("servletContextMock", expectations);
        contextMock.expects.getProperty(ServletContext.class).returns(servletContextMock).any();
        servletContextMock.expects.getResource("/fred.jsp").returns(ServletContextResourceTestCase.class.getResource("ServletContextResourceTestCaseContent.txt"));
        ServletContextResource resource = new ServletContextResource("/fred.jsp");
        InputSource source = resource.provideInputSource(contextMock);
        InputStream is = source.getByteStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String input = null;
        StringBuffer actualResult = new StringBuffer();
        while ((input = br.readLine()) != null) {
            actualResult.append(input);
        }
        assertEquals("Some text in a resource file", actualResult.toString());
    }
}

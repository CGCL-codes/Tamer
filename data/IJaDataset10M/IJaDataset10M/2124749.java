package org.t2framework.t2.navigation;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import junit.framework.TestCase;
import org.t2framework.commons.mock.MockHttpServletResponseImpl;
import org.t2framework.t2.mock.MockWebContext;

public class XmlTest extends TestCase {

    public void test1() throws Exception {
        Hoge hoge = new Hoge();
        hoge.name = "katayama";
        MockWebContext context = MockWebContext.createMock("/aaa");
        final StringWriter w = new StringWriter();
        context.setMockHttpServletResponse(new MockHttpServletResponseImpl(context.getMockHttpServletRequest()) {

            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter(w);
            }
        });
        Xml.toXml(hoge).execute(context);
        assertNotNull(w.toString());
        assertTrue(w.toString().contains("katayama"));
    }

    @XmlRootElement
    public static class Hoge {

        @XmlElement
        public String name;
    }
}

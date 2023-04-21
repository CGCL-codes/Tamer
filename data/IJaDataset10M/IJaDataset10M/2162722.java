package net.sf.katta.index.indexer;

import junit.framework.TestCase;

public class XPathServiceTest extends TestCase {

    public void testIndex() throws Exception {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<user>");
        stringBuilder.append("<name>");
        stringBuilder.append("<first>");
        stringBuilder.append("foo");
        stringBuilder.append("</first>");
        stringBuilder.append("<last>");
        stringBuilder.append("bar");
        stringBuilder.append("</last>");
        stringBuilder.append("</name>");
        stringBuilder.append("<login>");
        stringBuilder.append("foobar");
        stringBuilder.append("</login>");
        stringBuilder.append("<password>");
        stringBuilder.append("pwd");
        stringBuilder.append("</password>");
        stringBuilder.append("</user>");
        final String xml = stringBuilder.toString();
        final IXPathService service = new DefaultXPathService();
        String parse = service.parse("/user/name/first", xml);
        assertEquals("foo", parse);
        parse = service.parse("/user/name/last", xml);
        assertEquals("bar", parse);
    }
}

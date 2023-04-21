package net.sf.iqser.plugin.web.html.filters;

import junit.framework.TestCase;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class RegExTagNameFilterTest extends TestCase {

    public void test1() throws ParserException {
        String url;
        String tagName = "div";
        NodeFilter filter = new RegExTagNameFilter(tagName);
        url = "http://www.admin.ch/portal/index.html?lang=en";
        parse(url, filter);
        url = "http://www.eda.admin.ch/eda/en/home/dfa/policy.html";
        parse(url, filter);
        url = "http://www.edi.admin.ch/dokumentation/00334/index.html?lang=en";
        parse(url, filter);
    }

    protected void parse(String url, NodeFilter filter) throws ParserException {
        Parser parser = new Parser(url);
        parser.setEncoding(Page.DEFAULT_CHARSET);
        NodeList nodes = parser.parse(filter);
        Node item = nodes.elementAt(0);
        assertNotNull(" Test failed for url=" + url, item);
    }
}

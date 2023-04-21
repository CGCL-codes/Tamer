package net.sf.jsmap.test;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * @author Sebastian
 */
public class Test2 {

    static int j = 1;

    public static void main(String[] args) throws ParserException {
        int depth = 3;
        NodeFilter filter;
        String url = "http://www.student-zw.fh-kl.de/~sewi0001/testseite/index.html";
        filter = new NodeClassFilter(LinkTag.class);
        crawlPage(filter, url, depth);
    }

    /**
	 * crawls and parses the given webpage and extracts hyperlinks
	 * 
	 * @param filter specifies what kind of tags will be extracted
	 * @param url guess what! either the URL or the filename (autodetects)
	 * @param depth number of recursions
	 */
    public static void crawlPage(NodeFilter filter, String url, int depth) {
        try {
            Parser parser;
            NodeList list;
            if (j == depth) return; else {
                parser = new Parser(url);
                list = parser.extractAllNodesThatMatch(filter);
                System.out.println("Page:\n" + parser.getURL() + "\n\n");
                for (int i = 0; i < list.size(); i++) {
                    String newUrl = "http://www.student-zw.fh-kl.de/~sewi0001/testseite/" + list.elementAt(i).toPlainTextString() + ".html";
                    System.out.println("Link:\n" + list.elementAt(i).toHtml());
                    crawlPage(filter, newUrl, depth);
                    j++;
                }
            }
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }
}

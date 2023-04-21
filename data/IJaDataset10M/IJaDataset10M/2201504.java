package ca.ucalgary.cpsc.ebe.fitClipse.tests.junit.render;

import ca.ucalgary.cpsc.ebe.fitClipse.render.adapter.WikiPageFactory;
import ca.ucalgary.cpsc.ebe.fitClipse.render.html.HtmlPage;
import ca.ucalgary.cpsc.ebe.fitClipse.render.html.HtmlPageFactory;
import ca.ucalgary.cpsc.ebe.fitClipse.render.html.HtmlUtil;
import ca.ucalgary.cpsc.ebe.fitClipse.render.wiki.WikiPage;
import junit.framework.TestCase;

public class RenderTest extends TestCase {

    public void testMakeHtmlSimple() {
        String content = "%%(width: 100px;) This is some simple content%%";
        HtmlPageFactory factory = new HtmlPageFactory();
        WikiPageFactory pageFactory = new WikiPageFactory();
        WikiPage page = pageFactory.buildWikiPage("test", content);
        HtmlPage html = factory.newPage();
        try {
            html.main.use(HtmlUtil.addHeaderAndFooter(page, HtmlUtil.testableHtml(content)));
            System.out.println(html.html());
            String expected = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">" + "\r\n" + "<html>" + "\r\n" + "	<head>" + "\r\n" + "		<title>FitNesse</title>" + "\r\n" + "	</head>" + "\r\n" + "	<body>" + "\r\n" + "		<div class=\"main\"><span style=\"width: 100px;\">This is some simple content</span>" + "\r\n" + "</div>" + "\r\n" + "	</body>" + "\r\n" + "</html>" + "\r\n";
            assertEquals(expected, html.html());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testMakeTableSimple() {
        String content = "|This is a cell|with no vertical bar following it\n|this is a second row|with two cells\n";
        HtmlPageFactory factory = new HtmlPageFactory();
        WikiPageFactory pageFactory = new WikiPageFactory();
        WikiPage page = pageFactory.buildWikiPage("test", content);
        HtmlPage html = factory.newPage();
        try {
            html.main.use(HtmlUtil.addHeaderAndFooter(page, HtmlUtil.testableHtml(content)));
            System.out.println("---------------------------\n" + html.html());
            String expected = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">" + "\r\n" + "<html>" + "\r\n" + "	<head>" + "\r\n" + "		<title>FitNesse</title>" + "\r\n" + "	</head>" + "\r\n" + "	<body>" + "\r\n" + "		<div class=\"main\"><table border=\"1\">" + "\n" + "<tr><td>This is a cell</td>" + "\r\n" + "<td>with no vertical bar following it</td>" + "\r\n" + "</tr>" + "\n" + "<tr><td>this is a second row</td>" + "\r\n" + "<td>with two cells</td>" + "\r\n" + "</tr>" + "\n" + "</table>" + "\n" + "</div>" + "\r\n" + "	</body>" + "\r\n" + "</html>" + "\r\n";
            assertEquals(expected, html.html());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testMakeHeadingSimple() {
        String content = "----!!Heading\n";
        HtmlPageFactory factory = new HtmlPageFactory();
        WikiPageFactory pageFactory = new WikiPageFactory();
        WikiPage page = pageFactory.buildWikiPage("test", content);
        HtmlPage html = factory.newPage();
        try {
            html.main.use(HtmlUtil.addHeaderAndFooter(page, HtmlUtil.testableHtml(content)));
            String expected = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">" + "\r\n" + "<html>" + "\r\n" + "	<head>" + "\r\n" + "		<title>FitNesse</title>" + "\r\n" + "	</head>" + "\r\n" + "	<body>" + "\r\n" + "		<div class=\"main\"><hr><h2>Heading</h2></div>" + "\r\n" + "	</body>" + "\r\n" + "</html>" + "\r\n" + "";
            assertEquals(expected, html.html());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testMakeLinkSimple() {
        String content = "[a link]\\\\[a link|to somewhere]";
        HtmlPageFactory factory = new HtmlPageFactory();
        WikiPageFactory pageFactory = new WikiPageFactory();
        WikiPage page = pageFactory.buildWikiPage("test", content);
        HtmlPage html = factory.newPage();
        try {
            html.main.use(HtmlUtil.addHeaderAndFooter(page, HtmlUtil.testableHtml(content)));
            String expected = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">" + "\r\n" + "<html>" + "\r\n" + "	<head>" + "\r\n" + "		<title>FitNesse</title>" + "\r\n" + "	</head>" + "\r\n" + "	<body>" + "\r\n" + "		<div class=\"main\"><a href=\"#\">a link</a><br><a href=\"#\">a link</a></div>" + "\r\n" + "	</body>" + "\r\n" + "</html>" + "\r\n" + "";
            assertEquals(expected, html.html());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testMakeTextDifficult() {
        String content = "''%%(width: 100px;) a span%%''\n__%%(width: 100px;) a span%%__\n\n";
        HtmlPageFactory factory = new HtmlPageFactory();
        WikiPageFactory pageFactory = new WikiPageFactory();
        WikiPage page = pageFactory.buildWikiPage("test", content);
        HtmlPage html = factory.newPage();
        try {
            html.main.use(HtmlUtil.addHeaderAndFooter(page, HtmlUtil.testableHtml(content)));
            String expected = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">" + "\r\n" + "<html>" + "\r\n" + "	<head>" + "\r\n" + "		<title>FitNesse</title>" + "\r\n" + "	</head>" + "\r\n" + "	<body>" + "\r\n" + "		<div class=\"main\"><i><span style=\"width: 100px;\">a span</span>" + "\r\n" + "</i>" + "\r\n" + "<b><span style=\"width: 100px;\">a span</span>" + "\r\n" + "</b>" + "\r\n\n" + "</div>" + "\r\n" + "	</body>" + "\r\n" + "</html>" + "\r\n" + "";
            assertEquals(expected, html.html());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testMakeTextMonospaced() {
        String content = "{{Text}}";
        HtmlPageFactory factory = new HtmlPageFactory();
        WikiPageFactory pageFactory = new WikiPageFactory();
        WikiPage page = pageFactory.buildWikiPage("test", content);
        HtmlPage html = factory.newPage();
        try {
            html.main.use(HtmlUtil.addHeaderAndFooter(page, HtmlUtil.testableHtml(content)));
            System.out.println(html.html());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testMakeTermDef() {
        String content = ";Term:Definition\n";
        HtmlPageFactory factory = new HtmlPageFactory();
        WikiPageFactory pageFactory = new WikiPageFactory();
        WikiPage page = pageFactory.buildWikiPage("test", content);
        HtmlPage html = factory.newPage();
        try {
            html.main.use(HtmlUtil.addHeaderAndFooter(page, HtmlUtil.testableHtml(content)));
            System.out.println(html.html());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}

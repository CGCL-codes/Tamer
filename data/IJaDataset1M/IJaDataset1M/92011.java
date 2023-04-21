package net.sf.cplab.templater;

import static org.junit.Assert.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * @author jtse
 *
 */
public class TemplateUtilsTest {

    /**
	 * Test method for {@link net.sf.cplab.templater.TemplateUtils#createHtmlFilenameFromString(java.lang.String)}.
	 */
    @Test
    public final void testCreateHtmlFilenameFromString() {
        String testString = "aBcDeF   gHi";
        final String expected = "abcdef_ghij.html";
        String actual = null;
        actual = TemplateUtils.createHtmlFilenameFromString(testString).toString();
        assertTrue(!actual.equals(expected));
        actual = TemplateUtils.createHtmlFilenameFromString(testString + "J");
        assertEquals(expected, actual);
    }

    /**
	 * Test method for {@link net.sf.cplab.templater.TemplateUtils#createHtmlPagesMenuUsingPageList(java.util.List)}.
	 */
    @Test
    public final void testCreateHtmlPagesMenuUsingPageList() {
        List<Page> pages = ContentXMLHandlerTest.getExpectedSections().get(1).getPages();
        final String expected = "<table class=\"tmpl_pages_menu\"><tr><td class=\"tmpl_pages_menu_item\"><a href=\"page_2a.html\">Page 2A</a></td></tr><tr><td class=\"tmpl_pages_menu_item\"><a href=\"page_2b.html\">Page 2B</a></td></tr></table>";
        String actual = TemplateUtils.createHtmlPagesMenuUsingPageList(pages).toString();
        assertEquals(expected, actual);
    }

    /**
	 * Test method for {@link net.sf.cplab.templater.TemplateUtils#createHtmlSectionsMenuUsingSectionList(java.util.List)}.
	 */
    @Test
    public final void testCreateHtmlSectionsMenuUsingSectionList() {
        List<Section> sections = ContentXMLHandlerTest.getExpectedSections();
        final String expected = "<table class=\"tmpl_sections_menu\"><tr><td class=\"tmpl_sections_menu_item\"><a href=\"index.html\">Section 1</a></td><td class=\"tmpl_sections_menu_item\"><a href=\"page_2a.html\">Section 2</a></td></tr></table>";
        String actual = TemplateUtils.createHtmlSectionsMenuUsingSectionList(sections).toString();
        assertEquals(expected, actual);
    }

    /**
	 * Test method for {@link net.sf.cplab.templater.TemplateUtils#createHtmlPageContentsUsingPageAndTemplate(net.sf.cplab.templater.Page, java.lang.String)}.
	 */
    @Test
    public final void testCreateHtmlPageContentsUsingPage() {
        String line1 = "Testing 1 2 3 4";
        String line2 = "Testing 5 6 7 8";
        Page page = new Page();
        List<String> contents = new ArrayList<String>();
        contents.add(line1);
        contents.add(line2);
        page.setContents(contents);
        final String expected = line1 + line2;
        String actual = TemplateUtils.createHtmlPageContentsUsingPage(page).toString();
        assertEquals(expected, actual);
    }

    @Test
    public final void testCreateHtmlPageImagesUsingPage() {
        final String line1 = "image/image1.png";
        final String line2 = "image/image2.png";
        final String line3 = "image/image3.png";
        Page page = new Page();
        List<String> images = new ArrayList<String>();
        images.add(line1);
        images.add(line2);
        images.add(line3);
        page.setImages(images);
        String expected = "<table class=\"tmpl_page_images\">";
        expected += "<tr><td><img src=\"" + line1 + "\" /></td></tr>";
        expected += "<tr><td><img src=\"" + line2 + "\" /></td></tr>";
        expected += "<tr><td><img src=\"" + line3 + "\" /></td></tr>";
        expected += "</table>";
        String actual = TemplateUtils.createHtmlPageImagesUsingPage(page).toString();
        assertEquals(expected, actual);
    }

    /**
	 * Test method for {@link net.sf.cplab.templater.TemplateUtils#createStringBufferUsingInputStream(java.io.InputStream)}.
	 */
    @Test
    public final void testCreateStringBufferUsingInputStream() {
        InputStream in = TemplateUtilsTest.class.getResourceAsStream(TemplateUtilsTest.class.getSimpleName() + ".txt");
        final String expected = "OneTwo\nThreeFour\nFive\nSix\nSevenEight";
        String actual = TemplateUtils.createStringBufferUsingInputStream(in).toString();
        assertEquals(expected, actual);
    }
}

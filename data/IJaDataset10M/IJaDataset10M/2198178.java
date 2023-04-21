package fitnesse.html;

import junit.framework.TestCase;

public class HtmlTagTest extends TestCase {

    public static String endl = HtmlElement.endl;

    private HtmlTag tag;

    public void setUp() throws Exception {
        tag = new HtmlTag("sillytag");
    }

    public void tearDown() throws Exception {
    }

    public void testEmpty() throws Exception {
        assertEquals("<sillytag/>" + endl, tag.html());
    }

    public void testWithText() throws Exception {
        tag.add("some text");
        assertEquals("<sillytag>some text</sillytag>" + endl, tag.html());
    }

    public void testEmbeddedTag() throws Exception {
        tag.add(new HtmlTag("innertag"));
        String expected = "<sillytag>" + "<innertag/>" + endl + "</sillytag>" + endl;
        assertEquals(expected, tag.html());
    }

    public void testAttribute() throws Exception {
        tag.addAttribute("key", "value");
        assertEquals("<sillytag key=\"value\"/>" + endl, tag.html());
    }

    public void testCombination() throws Exception {
        tag.addAttribute("mykey", "myValue");
        HtmlTag inner = new HtmlTag("inner");
        inner.add(new HtmlTag("beforetext"));
        inner.add("inner text");
        inner.add(new HtmlTag("aftertext"));
        tag.add(inner);
        String expected = "<sillytag mykey=\"myValue\">" + "<inner>" + "<beforetext/>" + endl + "inner text" + "<aftertext/>" + endl + "</inner>" + endl + "</sillytag>" + endl;
        assertEquals(expected, tag.html());
    }

    public void testNoEndTabWithoutChildrenTags() throws Exception {
        HtmlTag subtag = new HtmlTag("subtag");
        subtag.add("content");
        tag.add(subtag);
        String expected = "<sillytag>" + "<subtag>content</subtag>" + endl + "</sillytag>" + endl;
        assertEquals(expected, tag.html());
    }

    public void testTwoChildren() throws Exception {
        tag.add(new HtmlTag("tag1"));
        tag.add(new HtmlTag("tag2"));
        String expected = "<sillytag>" + "<tag1/>" + endl + "<tag2/>" + endl + "</sillytag>" + endl;
        assertEquals(expected, tag.html());
    }

    public void testUse() throws Exception {
        tag.add("original");
        tag.use("new");
        assertEquals("<sillytag>new</sillytag>" + endl, tag.html());
    }
}

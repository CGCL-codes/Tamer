package net.sourceforge.pebble.util;

import junit.framework.TestCase;

/**
 * Tests for the utilities in the StringUtils class.
 *
 * @author    Simon Brown
 */
public class StringUtilsTest extends TestCase {

    public void testTransformHTML() {
        assertNull(StringUtils.transformHTML(null));
        assertEquals("Here is some text", StringUtils.transformHTML("Here is some text"));
        assertEquals("Here is a &lt; symbol", StringUtils.transformHTML("Here is a < symbol"));
        assertEquals("Here is a &gt; symbol", StringUtils.transformHTML("Here is a > symbol"));
        assertEquals("Here is a &amp; symbol", StringUtils.transformHTML("Here is a & symbol"));
        assertEquals("&lt;a href=&quot;http://www.google.com&quot;&gt;Google&lt;/a&gt;", StringUtils.transformHTML("<a href=\"http://www.google.com\">Google</a>"));
    }

    public void testTransformToHTMLSubset() {
        assertNull(StringUtils.transformToHTMLSubset(null));
        assertEquals("Here is some text", StringUtils.transformToHTMLSubset("Here is some text"));
        assertEquals("Here is a &lt; symbol", StringUtils.transformToHTMLSubset("Here is a &amp;lt; symbol"));
        assertEquals("Here is a &lt; symbol", StringUtils.transformToHTMLSubset("Here is a &lt; symbol"));
        assertEquals("Here is a &gt; symbol", StringUtils.transformToHTMLSubset("Here is a &amp;gt; symbol"));
        assertEquals("Here is a &gt; symbol", StringUtils.transformToHTMLSubset("Here is a &gt; symbol"));
        assertEquals("Here is a <b> tag", StringUtils.transformToHTMLSubset("Here is a &lt;b&gt; tag"));
        assertEquals("Here is a <strong> tag", StringUtils.transformToHTMLSubset("Here is a &lt;strong&gt; tag"));
        assertEquals("Here is a <i> tag", StringUtils.transformToHTMLSubset("Here is a &lt;i&gt; tag"));
        assertEquals("Here is a <em> tag", StringUtils.transformToHTMLSubset("Here is a &lt;em&gt; tag"));
        assertEquals("Here is a <p> tag", StringUtils.transformToHTMLSubset("Here is a &lt;p&gt; tag"));
        assertEquals("Here is a </p> tag", StringUtils.transformToHTMLSubset("Here is a &lt;/p&gt; tag"));
        assertEquals("Here is a <br /> tag", StringUtils.transformToHTMLSubset("Here is a &lt;br&gt; tag"));
        assertEquals("Here is a <br /> tag", StringUtils.transformToHTMLSubset("Here is a &lt;br/&gt; tag"));
        assertEquals("Here is a <br /> tag", StringUtils.transformToHTMLSubset("Here is a &lt;br /&gt; tag"));
        assertEquals("Here is a <pre> tag", StringUtils.transformToHTMLSubset("Here is a &lt;pre&gt; tag"));
        assertEquals("Here is a </pre> tag", StringUtils.transformToHTMLSubset("Here is a &lt;/pre&gt; tag"));
        assertEquals("Here is a <a href=\"http://www.google.com\">link</a> to Google", StringUtils.transformToHTMLSubset("Here is a &lt;a href=\"http://www.google.com\"&gt;link&lt;/a&gt; to Google"));
        assertEquals("Here is a <a href=\"http://www.google.com\">link</a> to Google and another <a href=\"http://www.google.com\">link</a>", StringUtils.transformToHTMLSubset("Here is a &lt;a href=\"http://www.google.com\"&gt;link&lt;/a&gt; to Google and another &lt;a href=\"http://www.google.com\"&gt;link&lt;/a&gt;"));
        assertEquals("Here is a <a href='http://www.google.com'>link</a> to Google", StringUtils.transformToHTMLSubset("Here is a &lt;a href='http://www.google.com'&gt;link&lt;/a&gt; to Google"));
        assertEquals("Here is a <a href='http://www.google.com'>link</a> to Google and another <a href='http://www.google.com'>link</a>", StringUtils.transformToHTMLSubset("Here is a &lt;a href='http://www.google.com'&gt;link&lt;/a&gt; to Google and another &lt;a href='http://www.google.com'&gt;link&lt;/a&gt;"));
        assertEquals("Here is a <a href=\"http://www.google.com\">link</a> to Google", StringUtils.transformToHTMLSubset("Here is a &lt;a href=\"http://www.google.com\" target=\"_blank\"&gt;link&lt;/a&gt; to Google"));
        assertEquals("Here is a &lt;script&gt; tag", StringUtils.transformToHTMLSubset("Here is a &lt;script&gt; tag"));
        assertEquals("Here is a <blockquote> tag", StringUtils.transformToHTMLSubset("Here is a &lt;blockquote&gt; tag"));
        assertEquals("Here is a </blockquote> tag", StringUtils.transformToHTMLSubset("Here is a &lt;/blockquote&gt; tag"));
        assertEquals("Here is a <a href=\"mailto:somebody@somedomain.com\">mail link</a>", StringUtils.transformToHTMLSubset("Here is a &lt;a href=\"mailto:somebody@somedomain.com\"&gt;mail link&lt;/a&gt;"));
        assertEquals("Here is a &#8217; character", StringUtils.transformToHTMLSubset("Here is a &amp;#8217; character"));
        assertEquals("Here is a &quot; symbol", StringUtils.transformToHTMLSubset("Here is a &amp;quot; symbol"));
        assertEquals("&uacute;", StringUtils.transformToHTMLSubset("&amp;uacute;"));
        assertEquals("see contracts as &quot;fly-by-night&quot; sorts", StringUtils.transformToHTMLSubset("see contracts as &amp;quot;fly-by-night&amp;quot; sorts"));
        assertEquals("Here is a <sup> tag", StringUtils.transformToHTMLSubset("Here is a &lt;sup&gt; tag"));
        assertEquals("Here is a <sub> tag", StringUtils.transformToHTMLSubset("Here is a &lt;sub&gt; tag"));
    }

    public void testFilterNewLines() {
        assertNull(StringUtils.filterNewlines(null));
        assertEquals("Here is some text", StringUtils.filterNewlines("Here is some text"));
        assertEquals("Line 1\n", StringUtils.filterNewlines("Line 1\r\n"));
        assertEquals("Line 1\nLine2", StringUtils.filterNewlines("Line 1\r\nLine2"));
    }

    public void testFilterHTML() {
        assertEquals("Here is some text.", StringUtils.filterHTML("Here is <!-- <rdf>...</rdf> -->some text."));
        assertEquals("Here is some text.", StringUtils.filterHTML("Here is <!-- <rdf/> -->some text."));
        assertEquals("Here is some text.", StringUtils.filterHTML("<b>Here</b> is <i>some</i> text."));
        assertEquals("Here is a link.", StringUtils.filterHTML("Here is <a href=\"http://www.google.com\">a link</a>."));
        assertEquals("Here is a link.", StringUtils.filterHTML("Here is <a \nhref=\"http://www.google.com\">a link</a>."));
        assertEquals("Here is some text", StringUtils.filterHTML("Here is &lt;some&gt; text"));
    }

    public void testStripScriptTags() {
        assertEquals("some text", StringUtils.stripScriptTags("some <script>alert(1)</script>text"));
        assertEquals("some text", StringUtils.stripScriptTags("some <script >alert(1)</script>text"));
        assertEquals("some text", StringUtils.stripScriptTags("some <script >alert(1)</script >text"));
        assertEquals("some text", StringUtils.stripScriptTags("some <script language=\"JavaScript\">alert(1)</script >text"));
        assertEquals("some text", StringUtils.stripScriptTags("some <script src=\"something.js\"/>text"));
    }
}

package com.petebevin.markdown.test;

import com.petebevin.markdown.HTMLDecoder;
import com.petebevin.markdown.MarkdownProcessor;
import junit.framework.TestCase;

public class EscapeSpecialCharsWithinTagAttributes extends TestCase {

    MarkdownProcessor markdownProcessor;

    public void setUp() throws Exception {
        markdownProcessor = new MarkdownProcessor();
    }

    public void testImages() {
        String url = "![an *image*](/images/an_image_with_underscores.jpg \"An_image_title\")";
        String processed = markdownProcessor.markdown(url);
        String output = "<p><img src=\"/images/an_image_with_underscores.jpg\" alt=\"an *image*\" title=\"An_image_title\" /></p>\n";
        assertEquals(output, processed);
    }

    public void testAutoLinks() {
        String url = "[a _link_](http://url.com/a_tale_of_two_cities?var1=a_query_&var2=string \"A_link_title\")";
        String processed = markdownProcessor.markdown(url);
        String output = "<p><a href=\"http://url.com/a_tale_of_two_cities?var1=a_query_&amp;var2=string\" title=\"A_link_title\">a <em>link</em></a></p>\n";
        assertEquals(output, processed);
    }
}

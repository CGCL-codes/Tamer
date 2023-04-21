package org.jamwiki.parser;

/**
 *
 */
public class ParseHtmlCommentTest extends TestParser {

    /**
	 *
	 */
    public ParseHtmlCommentTest(String name) {
        super(name);
    }

    /**
	 *
	 */
    public void testBasicHtmlComments() throws Exception {
        executeParserTest("HtmlCommentTest1");
    }
}

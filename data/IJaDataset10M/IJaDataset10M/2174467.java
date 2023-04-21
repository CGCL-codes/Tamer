package org.jamwiki.test.parser.jflex;

/**
 *
 */
public class WikiHeadingTagTest extends JFlexParserTest {

    /**
	 *
	 */
    public WikiHeadingTagTest(String name) {
        super(name);
    }

    /**
	 *
	 */
    public void testHeading() {
        String input = "";
        String output = "";
        input = "==heading==";
        output = "<div style=\"font-size:90%;float:right;margin-left:5px;\">[<a href=\"/wiki/en/Special:Edit?topic=DUMMY&amp;section=1\">Edit</a>]</div><a name=\"heading\"></a><h2>heading</h2>\n";
        assertEquals(output, this.parse(input));
        input = "=='''heading'''==";
        output = "<div style=\"font-size:90%;float:right;margin-left:5px;\">[<a href=\"/wiki/en/Special:Edit?topic=DUMMY&amp;section=1\">Edit</a>]</div><a name=\"heading\"></a><h2><b>heading</b></h2>\n";
        assertEquals(output, this.parse(input));
        input = "== three word heading ==";
        output = "<div style=\"font-size:90%;float:right;margin-left:5px;\">[<a href=\"/wiki/en/Special:Edit?topic=DUMMY&amp;section=1\">Edit</a>]</div><a name=\"three_word_heading\"></a><h2>three word heading</h2>\n";
        assertEquals(output, this.parse(input));
        input = "=== unmatched heading ==";
        output = "<div style=\"font-size:90%;float:right;margin-left:5px;\">[<a href=\"/wiki/en/Special:Edit?topic=DUMMY&amp;section=1\">Edit</a>]</div><a name=\"%3D_unmatched_heading\"></a><h2>= unmatched heading</h2>\n";
        assertEquals(output, this.parse(input));
    }
}

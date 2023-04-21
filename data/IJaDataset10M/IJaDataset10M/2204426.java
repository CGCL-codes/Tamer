package org.ashkelon;

import junit.framework.TestCase;

public class AuthorTest extends TestCase {

    public void testAuthorParseText1() {
        Author author = new Author("  Eitan Suez  ");
        assertEquals("Eitan Suez", author.getName());
        assertEquals("", author.getEmail());
    }

    public void testAuthorParseText2() {
        new Author(" this is complete gibberish ... ");
    }

    public void testAuthorParseText3() {
        Author author = new Author("<a href=\"mailto:eitan@u2d.com\">Eitan Suez</a>");
        assertEquals("Eitan Suez", author.getName());
        assertEquals("eitan@u2d.com", author.getEmail());
    }
}

package org.columba.mail.parser;

import java.util.List;
import java.util.Vector;
import junit.framework.TestCase;

/**
 * @author fdietz
 * 
 */
public class ListParserTest extends TestCase {

    public void testCreateListFromStringNull() {
        try {
            ListParser.createListFromString(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testCreateListFromStringEmpty() {
        String s = "";
        List<String> l = ListParser.createListFromString(s);
        assertEquals("list size 0", 0, l.size());
    }

    public void testCreateListFromString() {
        String s = "test@test.de";
        List<String> l = ListParser.createListFromString(s);
        assertEquals("list size 1", 1, l.size());
        assertEquals("test@test.de", l.get(0));
    }

    public void testCreateListFromString2() {
        String s = "test@test.de, test2@test2.de";
        List<String> l = ListParser.createListFromString(s);
        assertEquals("list size 2", 2, l.size());
        assertEquals("test@test.de", l.get(0));
        assertEquals("test2@test2.de", l.get(1));
    }

    /**
	 * test if leading or trailing whitespaces are trimmed correctly
	 * 
	 */
    public void testCreateListFromString3() {
        String s = "test@test.de,test2@test2.de, MyGroup,  My Test Group";
        List<String> l = ListParser.createListFromString(s);
        assertEquals("list size 4", 4, l.size());
        assertEquals("test@test.de", l.get(0));
        assertEquals("test2@test2.de", l.get(1));
        assertEquals("MyGroup", l.get(2));
        assertEquals("My Test Group", l.get(3));
    }

    /**
	 * test if a comma doesn't disturb our parser if enclosed in double-quotes
	 * 
	 */
    public void testCreateListFromString4() {
        String s = "test@test.de, Firstname Lastname, \"Lastname, Firstname\"";
        List<String> l = ListParser.createListFromString(s);
        assertEquals("list size 3", 3, l.size());
        assertEquals("test@test.de", l.get(0));
        assertEquals("Firstname Lastname", l.get(1));
        assertEquals("Lastname, Firstname", l.get(2));
    }

    /**
	 * test if \" characters are removed in the list, we only need this in the
	 * String representation
	 */
    public void testCreateListFromString5() {
        String s = "test@test.de, \"Firstname Lastname\", \"Lastname, Firstname\"";
        List<String> l = ListParser.createListFromString(s);
        assertEquals("list size 3", 3, l.size());
        assertEquals("test@test.de", l.get(0));
        assertEquals("Firstname Lastname", l.get(1));
        assertEquals("Lastname, Firstname", l.get(2));
    }

    /**
	 * Test displayname and address with and without comma
	 */
    public void testCreateListFromString6() {
        String s = "test@test.de, \"Firstname Lastname\" <mail@mail.org>, \"Lastname, Firstname\" <mail@mail.org>";
        List<String> l = ListParser.createListFromString(s);
        assertEquals("list size 3", 3, l.size());
        assertEquals("test@test.de", l.get(0));
        assertEquals("Firstname Lastname <mail@mail.org>", l.get(1));
        assertEquals("Lastname, Firstname <mail@mail.org>", l.get(2));
    }

    public void testCreateStringFromListNull() {
        try {
            ListParser.createStringFromList(new Vector<String>(), null);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testCreateStringFromListNull2() {
        try {
            ListParser.createStringFromList(null, ";");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testCreateStringFromListEmpty() {
        List<String> list = new Vector<String>();
        String result = ListParser.createStringFromList(list, ";");
        assertEquals("", result);
    }

    public void testCreateStringFromList() {
        List<String> list = new Vector<String>();
        list.add("test@test.de");
        list.add("test2@test2.de");
        String result = ListParser.createStringFromList(list, ",");
        assertEquals("test@test.de, test2@test2.de, ", result);
    }

    /**
	 * Test if \" character disturbs our parser
	 */
    public void testCreateStringFromList2() {
        List<String> list = new Vector<String>();
        list.add("test@test.de");
        list.add("\"My yours and he's list\"");
        String result = ListParser.createStringFromList(list, ",");
        assertEquals("test@test.de, My yours and he's list, ", result);
    }

    /**
	 * Test if "," inside a contact item is escaped correctly
	 *
	 */
    public void testCreateStringFromList4() {
        List<String> list = new Vector<String>();
        list.add("test@test.de");
        list.add("Firstname Lastname");
        list.add("\"Lastname, Firstname\"");
        String result = ListParser.createStringFromList(list, ",");
        assertEquals("test@test.de, Firstname Lastname, \"Lastname, Firstname\", ", result);
    }

    /**
	 * Test what parser does if contact item already contains an escaped 
	 * representation
	 */
    public void testCreateStringFromList5() {
        List<String> list = new Vector<String>();
        list.add("test@test.de");
        list.add("\"Firstname Lastname\" <mail@mail.org>");
        list.add("\"Lastname, Firstname\" <mail@mail.org>");
        String result = ListParser.createStringFromList(list, ",");
        assertEquals("test@test.de, Firstname Lastname <mail@mail.org>, \"Lastname, Firstname\" <mail@mail.org>, ", result);
    }
}

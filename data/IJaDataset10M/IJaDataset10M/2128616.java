package kdaf.test.util;

import junit.framework.*;

import kdaf.util.*;

public class XMLDaysTest extends TestCase {

  public XMLDaysTest(String name) {
    super(name);
  }

    private XMLDays _xmlDays;
    private XMLDays _xmlDaysFromString;
    private static final String _xmlSpec = "<Sunday>TRUE</Sunday><Monday>TRUE</Monday><Tuesday>TRUE</Tuesday><Wednesday>TRUE</Wednesday><Thursday>TRUE</Thursday><Friday>TRUE</Friday><Saturday>TRUE</Saturday>";
    private static final String _xmlSpecOut = "<Sunday>FALSE</Sunday>\r\n<Monday>FALSE</Monday>\r\n<Tuesday>FALSE</Tuesday>\r\n<Wednesday>FALSE</Wednesday>\r\n<Thursday>FALSE</Thursday>\r\n<Friday>FALSE</Friday>\r\n<Saturday>FALSE</Saturday>\r\n";

  public void setUp() {
      _xmlDays = new XMLDays();
      _xmlDaysFromString = new XMLDays(_xmlSpec);
  }

    private void doTest(int day) {
	assert("String" + day, _xmlDaysFromString.getDay(day));
	assert("Default" + day, !_xmlDays.getDay(day));
    }

    public void testDays() {
	doTest(XMLDays.SUNDAY);
	doTest(XMLDays.MONDAY);
	doTest(XMLDays.TUESDAY);
	doTest(XMLDays.WEDNESDAY);
	doTest(XMLDays.THURSDAY);
	doTest(XMLDays.FRIDAY);
	doTest(XMLDays.SATURDAY);
    }

    public void testTag() {
	assertEquals("Tag", _xmlDays.getDayTag(XMLDays.MONDAY), "<Monday>FALSE</Monday>\r\n");
	assertEquals("TagString", _xmlDaysFromString.getDayTag(XMLDays.MONDAY), "<Monday>TRUE</Monday>\r\n");
    }

    public void testStringRepresent() {
	assertEquals("String", _xmlDays.toString(), _xmlSpecOut);
    }

    public void testAlterString() {
	XMLDays myDays = new XMLDays(_xmlSpec);
	for(int i=0; i< 7; i++) {
	    myDays.setDay(i, false); // Alter from TRUE
	}
	// now should equal _xmlSpecOut (same as for the create default)
	assertEquals("AlterString", myDays.toString(), _xmlSpecOut);
    }
}







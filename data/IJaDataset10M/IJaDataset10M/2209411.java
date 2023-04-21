package cz.langteacher.util;

public interface CharacterCheckerIface {

    /**
	 * Test if given string starts with char according to Java start char and
	 * 	rest of chars are according to Java part
	 * @param string - tested string
	 * @return error message if any error occurred
	 */
    public String testJavaLetterWithStartIdentifier(String string);
}

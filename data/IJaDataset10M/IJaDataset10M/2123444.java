package org.ccnx.ccn.apps.examples.ccnb;

/**
 * This class defines the protocol tags and strings for XML used in the example.
 */
public class ExampleDTags {

    /**
	 * If you add to this list, also add to the reverse map below.
	 */
    public static final int EXAMPLE_OFFSET = 5678001;

    public static final int Example2Integers = 5678001;

    public static final int Integer1 = 5678002;

    public static final int Integer2 = 5678003;

    public static final int ExampleComplicated = 5678004;

    public static final int StringBinaryVector = 5678005;

    public static final int String = 5678006;

    public static final int Binary = 5678007;

    public static final int Enumeration = 5678008;

    protected static final String[] _tagToStringMap = new String[] { "Example2Integers", "Integer1", "Integer2", "ExampleComplicated", "StringBinaryVector", "String", "Binary", "Enumeration" };

    protected static final int TAG_MAP_LENGTH = _tagToStringMap.length;

    /**
	 * This is the slow search -- find a tag based on an index. Only 
	 * used in cases where we need to print based on a binary tag value; 
	 * this is only used in text encoding of usually binary objects... For
	 * now, as it's rare, do a scan, rather than taking the up front hit
	 * to build a hash table.
	 * @param tagVal
	 * @return
	 */
    public static String tagToString(long tagVal) {
        if ((tagVal >= EXAMPLE_OFFSET) && tagVal < (TAG_MAP_LENGTH + EXAMPLE_OFFSET)) {
            return _tagToStringMap[(int) tagVal - EXAMPLE_OFFSET];
        }
        return null;
    }

    /**
	 * This is the slow search, and does the reverse of tagToString().
	 * @param tagName
	 * @return
	 */
    public static Long stringToTag(String tagName) {
        for (int i = 0; i < _tagToStringMap.length; ++i) {
            if ((null != _tagToStringMap[i]) && (_tagToStringMap[i].equals(tagName))) {
                return (long) i + EXAMPLE_OFFSET;
            }
        }
        return null;
    }
}

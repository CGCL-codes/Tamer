package huf.misc;

/**
 * Interface for classes converting strings.
 */
public interface StringConverter {

    /**
	 * Convert input string according to implementing class rules.
	 *
	 * @param str source string
	 * @return converted string
	 */
    String convert(String str);
}

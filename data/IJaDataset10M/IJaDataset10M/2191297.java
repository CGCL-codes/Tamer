package pl.bigk.utils.lexengines;

/**
 * Interface for all classes able to convert numbers to human-readable strings.
 * 
 * @author lukasz.kalek
 */
public interface LexEngine {

    /**
	 * The method returns human-readable string for given number.
	 * 
	 * @param number
	 *            a long-type number to convert
	 * @return textual representation of given number
	 */
    String getTextForNumber(long number);
}

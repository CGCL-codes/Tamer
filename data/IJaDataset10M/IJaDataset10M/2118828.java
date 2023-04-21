package wrm.saferJava.oval.localization.value;

/**
 * @author Sebastian Thomschke
 */
public interface MessageValueFormatter {

    /**
	 * @param value
	 * @return a string representation of the given value object
	 */
    String format(Object value);
}

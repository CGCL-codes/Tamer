package nl.utwente.ewi.tpl.runtime.converter;

/**
 * The {@code IntegerConverter} converts an integer value into a string and vice
 * versa. It recognises any integer value between -2<sup>31</sup> and 
 * 2<sup>31</sup>-1 (the range of an integer).
 *
 * @author Emond Papegaaij
 */
public class IntegerConverter implements Converter<Integer> {

    /**
	 * Converts the given value into an integer. It recognises any integer value
	 * between -2<sup>31</sup> and 2<sup>31</sup>-1 (the range of an integer). The
	 * conversion is performed using {@code Integer.parseInt}.
	 * @param value The value to convert.
	 * @return The resulting integer value.
	 */
    public Integer convert(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new TypeConversionException(e);
        }
    }

    /**
	 * Converts the given integer value into a string.
	 * @param value The integer value to convert into a string.
	 * @return The string representation.
	 */
    public String convertBack(Integer value) {
        return value.toString();
    }
}

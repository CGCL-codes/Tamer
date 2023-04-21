package nl.utwente.ewi.tpl.runtime.converter;

/**
 * The {@code DoubleConverter} converts a double value into a string and vice
 * versa. It recognises any valid string representation of a double. The syntax
 * is discussed in the API documentation of {@code Double.valueOf}.
 *
 * @author Emond Papegaaij
 */
public class DoubleConverter implements Converter<Double> {

    /**
	 * Converts the given value into a double. It recognises any valid string
	 * representation of a double. The syntax is discussed in the API
	 * documentation of {@code Double.valueOf}. The conversion is performed using
	 * {@code Double.parseDouble}.
	 * @param value The value to convert.
	 * @return The resulting double value.
	 */
    public Double convert(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new TypeConversionException(e);
        }
    }

    /**
	 * Converts the given double value into a string.
	 * @param value The double value to convert into a string.
	 * @return The string representation.
	 */
    public String convertBack(Double value) {
        return value.toString();
    }
}

package org.gbif.ipt.struts2.converter;

import org.gbif.ipt.utils.CoordinateUtils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Map;
import com.opensymphony.xwork2.conversion.TypeConversionException;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * This class provides the method to validate the latitude and longitude coordinates as decimal numbers.
 */
public abstract class CoordinateFormatConverter extends StrutsTypeConverter {

    private static final String ANGLE = "coordinate.angle";

    private static final char ALTERNATIVE_DECIMAL_SEPARATOR = ',';

    private static final String DECIMAL_PATTERN = "###.##";

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values[0].length() == 0) {
            return null;
        }
        String coordinate = context.get(ANGLE).toString();
        if (coordinate == null) {
            throw new TypeConversionException("Invalid decimal number: " + values[0]);
        } else {
            DoubleRange range;
            if (coordinate.equals(CoordinateUtils.LATITUDE)) {
                range = new DoubleRange(CoordinateUtils.MIN_LATITUDE, CoordinateUtils.MAX_LATITUDE);
            } else {
                range = new DoubleRange(CoordinateUtils.MIN_LONGITUDE, CoordinateUtils.MAX_LONGITUDE);
            }
            Double number;
            try {
                number = Double.parseDouble(values[0]);
                if (range.containsDouble(number)) {
                    return number;
                } else {
                    throw new TypeConversionException("Invalid decimal number: " + values[0]);
                }
            } catch (NumberFormatException e) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator(ALTERNATIVE_DECIMAL_SEPARATOR);
                DecimalFormat decimal = new DecimalFormat(DECIMAL_PATTERN, symbols);
                try {
                    number = decimal.parse(values[0]).doubleValue();
                    if (range.containsDouble(number)) {
                        return number;
                    } else {
                        throw new TypeConversionException("Invalid decimal number: " + values[0]);
                    }
                } catch (ParseException e1) {
                    throw new TypeConversionException("Invalid decimal number: " + values[0]);
                }
            }
        }
    }

    @Override
    public String convertToString(Map context, Object o) {
        if (o instanceof Double) {
            return o.toString();
        } else {
            throw new TypeConversionException("Invalid decimal number: " + o.toString());
        }
    }
}

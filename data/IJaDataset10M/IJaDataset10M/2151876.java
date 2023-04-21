package wheel.util;

import wheel.WheelException;
import java.text.ParseException;

public class DateConversionHandler extends AbstractDateConversionHandler {

    public Object convertFrom(Object object) {
        try {
            return getDateFormat().parse((String) object);
        } catch (ParseException e) {
            throw new WheelException("Could not parse '" + object.toString() + "' to Date.", e, null);
        }
    }

    public boolean canConvertFrom(Class aClass) {
        if (aClass.getName().equals("java.lang.String")) return true;
        return false;
    }
}

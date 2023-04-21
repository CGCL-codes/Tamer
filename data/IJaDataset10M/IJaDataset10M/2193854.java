package wheel.util;

import org.mvel.ConversionHandler;
import java.text.SimpleDateFormat;

public abstract class AbstractDateConversionHandler implements ConversionHandler {

    private static ThreadLocal dateFormat = new ThreadLocal() {

        @Override
        protected Object initialValue() {
            return new SimpleDateFormat();
        }
    };

    public static SimpleDateFormat getDateFormat() {
        return (SimpleDateFormat) dateFormat.get();
    }

    public static void setDateFormat(SimpleDateFormat simpleDateFormat) {
        dateFormat.set(simpleDateFormat);
    }
}

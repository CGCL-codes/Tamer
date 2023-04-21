package vi.parsers;

import java.util.Calendar;

/**
 Date constant.

 @author <a href="mailto:ivaradi@freemail.c3.hu">Istv�n V�radi</a>
 */
public class DateExpr extends ConstExpr {

    /**
         The value of the constant.
         */
    public final Calendar value;

    /**
         Construct an expression object.

         @param value   the value of the constant.
         */
    public DateExpr(Calendar value) {
        this.value = (Calendar) value.clone();
        this.value.get(Calendar.DAY_OF_YEAR);
        this.value.set(Calendar.HOUR, 0);
        this.value.set(Calendar.MINUTE, 0);
        this.value.set(Calendar.SECOND, 0);
        this.value.get(Calendar.DAY_OF_YEAR);
    }

    /**
         Convert the expression into a string.

         @return the string representation
         */
    public String toString() {
        return value.get(Calendar.YEAR) + "." + (value.get(Calendar.MONTH) + 1) + "." + value.get(Calendar.DAY_OF_MONTH);
    }
}

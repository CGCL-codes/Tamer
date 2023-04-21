package jse;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author WangShuai
 */
public class DateFormatTest {

    public static void main(String[] args) {
        System.out.println(DateFormat.getDateInstance().format(new Date()));
        String strDate = "AAAJul 6, 2011";
        ParsePosition pp = new ParsePosition(3);
        System.out.println(DateFormat.getDateInstance().parse(strDate, pp));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd", Locale.CHINA);
        System.out.println(sdf.format(new Date()));
        SimpleDateFormat sdf2 = new SimpleDateFormat("h a (zzzz)");
        FieldPosition fp = new FieldPosition(DateFormat.TIMEZONE_FIELD);
        System.out.println(fp.getBeginIndex());
        System.out.println(sdf2.format(new Date(), new StringBuffer(), fp));
        System.out.println(fp.getBeginIndex());
    }
}

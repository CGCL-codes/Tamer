package com.fh.auge.consors;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import com.domainlanguage.money.Money;
import com.fh.auge.core.quote.Quote;

public class Parser {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM. | HH:mm");

    private static final NumberFormat NUMBER_INSTANCE = NumberFormat.getNumberInstance(Locale.GERMAN);

    public Quote getQuote(String content) throws ParseException {
        int index1 = content.indexOf("<span class=\"price\">");
        String spanPlus = "<span class=\"plus\">";
        String spanMinus = "<span class=\"minus\">";
        String span = spanPlus;
        int index2 = content.indexOf(spanPlus, index1);
        if (index2 == -1) {
            span = spanMinus;
            index2 = content.indexOf(spanMinus, index1);
        }
        int index3 = content.indexOf("</span>", index2);
        String ss = content.substring(index2 + span.length(), index3).trim();
        int index4 = ss.indexOf(" ");
        double v = NUMBER_INSTANCE.parse(ss.substring(0, index4).trim()).doubleValue();
        String curString = ss.substring(index4).trim();
        Money m = Money.valueOf(v, Currency.getInstance(curString));
        String dp1 = "<td class=\"snapshot_global\">Kurs vom";
        int i1 = content.indexOf(dp1);
        String ds = content.substring(i1 + dp1.length(), i1 + dp1.length() + 15).trim();
        Date date = DATE_FORMAT.parse(ds);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        date = c.getTime();
        return new Quote(m, null, date);
    }
}

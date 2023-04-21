package it.aco.mandragora.common;

import fr.improve.struts.taglib.layout.formatter.DispatchFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.jsp.PageContext;

public class SimpleDateFormatter extends DispatchFormatter {

    /**
	 * If in_value is not null,
	 * cast in_value in a java.util.Date object and format it
	 * according to the s pattern "dd/MM/yyyy".
	 */
    public String date(Object in_value, PageContext in_pageContext) {
        Date lc_date = (Date) in_value;
        if (lc_date == null) {
            return null;
        } else {
            DateFormat lc_format = new SimpleDateFormat("dd/MM/yyyy");
            return lc_format.format(lc_date);
        }
    }

    public String dateTime(Object in_value, PageContext in_pageContext) {
        Date lc_date = (Date) in_value;
        if (lc_date == null) {
            return null;
        } else {
            DateFormat lc_format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return lc_format.format(lc_date);
        }
    }
}

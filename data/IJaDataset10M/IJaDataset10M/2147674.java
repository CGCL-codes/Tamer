package com.sun.j2ee.blueprints.taglibs.smart;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * HTML 'input' tag. Use with NameTag and ValueTag.
 */
public class DateSelectorTag extends SimpleTagSupport {

    private String locale = "en_US";

    private String prefix = "date";

    private Calendar calendar;

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setDate(Date d) {
        if (locale == null) {
            calendar = Calendar.getInstance();
        } else {
            calendar = Calendar.getInstance(new Locale(locale));
        }
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void doTag() throws JspTagException, IOException {
        JspWriter out = getJspContext().getOut();
        if (calendar == null) {
            Date date = new Date();
            setDate(date);
        }
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        if ((locale != null) && locale.toLowerCase().equals("en_us") || locale.toLowerCase().equals("en-us")) {
            out.print("<label for=\"month_id\">Month:</label>");
            createSelect(prefix + "_month", 1, 12, month, "month_id", out);
            out.print("<label for=\"day_id\">Day:</label>");
            createSelect(prefix + "_day", 1, 31, day, "day_id", out);
            out.print("<label for=\"year_id\">Year:</label>");
            createSelect(prefix + "_year", year, year + 4, year, "year_id", out);
        } else if ((locale != null) && locale.toLowerCase().equals("ja_jp") || locale.toLowerCase().equals("ja-jp")) {
            out.print("<label for=\"year_id\">年:</label>");
            createSelect(prefix + "_year", 2003, 4, year, "year_id", out);
            out.print("<label for=\"month_id\">月:</label>");
            createSelect(prefix + "_month", 1, 12, month, "month_id", out);
            out.print("<label for=\"day_id\">日:</label>");
            createSelect(prefix + "_day", 1, 31, day, "day_id", out);
        }
        reset();
    }

    private void createSelect(String name, int start, int count, int selectedIndex, String id, JspWriter out) throws IOException {
        out.write("<select name=\"" + name + "\" id=\"" + id + "\">");
        for (int loop = start; loop < count + 1; loop++) {
            if (loop == selectedIndex) {
                out.write("<option selected>" + loop + "</option>");
            } else {
                out.write("<option>" + loop + "</option>");
            }
        }
        out.write("</select>");
    }

    private void reset() {
        locale = "en_US";
        prefix = "date";
        calendar = null;
    }
}

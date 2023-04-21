package com.dcivision.framework.taglib.newCalendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

public class SetTitleClass extends BodyTagSupport {

    public static final String REVISION = "$Revision: 1.1 $";

    public SetTitleClass() {
        day = -1;
    }

    static Class _mthclass$(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    public int doAfterBody() throws JspException {
        CalendarTag calendartag = (CalendarTag) TagSupport.findAncestorWithClass(this, CalendarTag.class);
        if (calendartag == null) {
            throw new JspException("Could not find ancestor calendarTag");
        }
        BodyContent bodycontent = getBodyContent();
        String s;
        if (bodycontent == null) {
            s = "";
        } else {
            s = bodycontent.getString();
        }
        s = s.trim();
        if (s.length() > 0) {
            if (day == -1) {
                for (int i = 0; i < 7; i++) {
                    calendartag.setTitleClass(i, s);
                }
            } else {
                if (day <= 0 || day > 7) {
                    throw new JspException("Invalid day:" + day);
                }
                calendartag.setTitleClass(day - 1, s);
            }
        }
        return 0;
    }

    public int getDay() {
        return day;
    }

    public void release() {
        day = -1;
    }

    public void setDay(int i) {
        day = i;
    }

    private int day;
}

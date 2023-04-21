package com.dcivision.framework.taglib.newCalendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

public class SetNextMonth extends BodyTagSupport {

    public static final String REVISION = "$Revision: 1.1 $";

    public SetNextMonth() {
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
            calendartag.setNextMonth(s);
        }
        return 0;
    }

    public void release() {
    }
}

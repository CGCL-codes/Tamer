package presentation.com.sampleprj.common.ui.calendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class setLink extends BodyTagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 6128843675085072129L;

    public setLink() {
        day = -1;
    }

    public void setDay(int i) {
        day = i;
    }

    public void setDay(Integer integer) {
        day = integer.intValue();
    }

    public void setDay(String s) {
        day = Integer.parseInt(s);
    }

    public int getDay() {
        return day;
    }

    public int doAfterBody() throws JspException {
        calendarTag calendartag = (calendarTag) findAncestorWithClass(this, presentation.com.sampleprj.common.ui.calendar.calendarTag.class);
        if (calendartag == null) throw new JspException("Could not find ancestor calendarTag");
        BodyContent bodycontent = getBodyContent();
        String s;
        if (bodycontent == null) s = ""; else s = bodycontent.getString();
        s = s.trim();
        if (s.length() > 0) if (day == -1) {
            for (int i = 0; i < 31; i++) calendartag.setDateLink(i, s);
        } else {
            if (day <= 0 || day > 31) throw new JspException("Invalid day:" + day);
            calendartag.setDateLink(day - 1, s);
        }
        if (bodycontent != null) bodycontent.clearBody();
        return 0;
    }

    public int doEndTag() throws JspException {
        dropData();
        return 6;
    }

    public void release() {
        dropData();
    }

    private void dropData() {
        day = -1;
    }

    private int day;
}

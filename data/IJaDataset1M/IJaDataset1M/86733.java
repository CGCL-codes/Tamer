package com.dcivision.framework.taglib.channel;

import java.util.List;
import javax.servlet.jsp.PageContext;
import com.dcivision.framework.ApplicationException;

public class AjaxMeetingFormatter_1 extends AjaxStandardMeetingFormatter {

    public AjaxMeetingFormatter_1(PageContext pageContext, int pageSize, List channelList) {
        super(pageContext, pageSize, channelList);
    }

    protected String getIcon() throws ApplicationException {
        StringBuffer bstr = new StringBuffer();
        bstr.append("<td>");
        this.getIcon(bstr);
        bstr.append("</td>");
        return bstr.toString();
    }

    protected String getPriority() throws ApplicationException {
        StringBuffer bstr = new StringBuffer();
        bstr.append("<td>");
        this.getPriority(bstr);
        bstr.append("</td>");
        return bstr.toString();
    }

    protected String getTitle() throws ApplicationException {
        StringBuffer bstr = new StringBuffer();
        bstr.append("<td>");
        this.getTitle(bstr);
        bstr.append("</td>");
        return bstr.toString();
    }

    protected String getStartDate() throws ApplicationException {
        StringBuffer bstr = new StringBuffer();
        bstr.append("<td>&nbsp;");
        this.getStartDate(bstr);
        bstr.append("</td>");
        return bstr.toString();
    }
}

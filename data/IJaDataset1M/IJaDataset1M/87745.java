package com.dcivision.framework.taglib.channel;

import java.util.List;
import javax.servlet.jsp.PageContext;
import com.dcivision.framework.ApplicationException;

public class AjaxRecentDocFormatter_1 extends AjaxStandardRecentDocFormatter {

    public AjaxRecentDocFormatter_1(PageContext pageContext, int pageSize, List channelList) {
        super(pageContext, pageSize, channelList);
    }

    protected String getName() throws ApplicationException {
        StringBuffer bstr = new StringBuffer();
        bstr.append("<td>");
        this.getName(bstr);
        bstr.append("</td>");
        return bstr.toString();
    }

    protected String getLocation() throws ApplicationException {
        StringBuffer bstr = new StringBuffer();
        bstr.append("<td>");
        this.getLocation(bstr);
        bstr.append("</td>");
        return bstr.toString();
    }

    protected String getStatus() throws ApplicationException {
        StringBuffer bstr = new StringBuffer();
        bstr.append("<td>");
        this.getStatus(bstr);
        bstr.append("</td>");
        return bstr.toString();
    }

    protected String getUpdateDate() throws ApplicationException {
        StringBuffer bstr = new StringBuffer();
        bstr.append("<td>");
        this.getUpdateDate(bstr);
        bstr.append("</td>");
        return bstr.toString();
    }
}

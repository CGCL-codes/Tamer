package com.definity.toolkit.web.taglib;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

public class OptionsTag extends BaseTag {

    private static final long serialVersionUID = 3628326498321754282L;

    private String list;

    private String itemLabel;

    private String itemValue;

    private Object value;

    public OptionsTag() {
    }

    public void setList(String list) {
        this.list = list;
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public int doEndTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuilder html = new StringBuilder();
        List<?> domainList = (List<?>) request.getAttribute(list);
        for (Object domain : domainList) {
            Object optionValue = getValue(domain, itemValue);
            html.append("<option value='");
            html.append(optionValue);
            html.append("'");
            if (value != null && value.equals(optionValue)) {
                html.append(" selected='selected'");
            }
            html.append(">");
            html.append(getValue(domain, itemLabel));
            html.append("</option>");
        }
        write(html);
        return super.doEndTag();
    }
}

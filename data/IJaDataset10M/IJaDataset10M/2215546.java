package org.spice.servlet.tags;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;

public class TextAreaTag extends BaseTag implements DynamicAttributes {

    private static final long serialVersionUID = 1L;

    private String rows;

    private String cols;

    protected String ref;

    protected Boolean isValidation;

    protected String name;

    public String getRows() {
        return rows;
    }

    public String getCols() {
        return cols;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsValidation() {
        return isValidation;
    }

    public void setIsValidation(Boolean isValidation) {
        this.isValidation = isValidation;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            this.pageContext.getOut().append("</textarea>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doEndTag();
    }

    @Override
    public int doAfterBody() throws JspException {
        return super.doAfterBody();
    }

    @Override
    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
    }

    @SuppressWarnings("unchecked")
    @Override
    public int writeTag(PageContext pageContext) {
        try {
            Boolean flag = Boolean.FALSE;
            String reference = getRef();
            if (reference.contains(",")) {
                reference = reference.replace(" ", "");
                Enumeration<String> attributes = this.pageContext.getRequest().getAttributeNames();
                List<String> enumList = Collections.list(attributes);
                for (String singleEntity : enumList) {
                    if (singleEntity.trim().equalsIgnoreCase(reference.trim())) {
                        this.pageContext.getOut().append("<textarea id=\"" + getId() + "\" name= \"" + getRef() + "\" rows= \"" + getRows() + "\" cols= \"" + getCols() + "\">");
                        flag = Boolean.TRUE;
                    }
                }
                if (!flag) {
                    this.pageContext.getOut().append("<textarea id=\"" + getId() + "\" name= \"" + getRef() + "\" rows= \"" + getRows() + "\" cols= \"" + getCols() + "\">");
                }
            } else {
                this.pageContext.getOut().append("<textarea id=\"" + getId() + "\" name= \"" + getRef() + "\" rows= \"" + getRows() + "\" cols= \"" + getCols() + "\">");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;
    }
}

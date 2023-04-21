package org.strutsgears.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.taglib.TagUtils;

public class TogglerTag extends TagSupport {

    private String[] suffixArray;

    private int indexSuffix = -1;

    private String prefix = "";

    private String suffix = "";

    private String styleClass = "";

    private boolean writeHTML = true;

    private boolean generatesClassTag = true;

    private String var;

    public TogglerTag() {
        var = "";
    }

    public boolean getWriteHTML() {
        return writeHTML;
    }

    public void setWriteHTML(boolean writeHTML) {
        this.writeHTML = writeHTML;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public int doStartTag() throws JspException {
        StringBuffer out = new StringBuffer();
        if (generatesClassTag) {
            out.append("class=");
            out.append("\"");
        }
        if (!this.styleClass.equals("")) {
            out.append(this.styleClass);
        } else {
            out.append(this.prefix);
            if (!this.suffix.equals("")) {
                if (indexSuffix == -1) {
                    suffixArray = suffix.split("/");
                }
                if (suffixArray.length == 0) {
                    suffix = "";
                } else {
                    String tmpIndex = (String) pageContext.getAttribute(var + "_togglerIndex");
                    indexSuffix = tmpIndex == null ? -1 : Integer.valueOf(tmpIndex);
                    indexSuffix = indexSuffix != suffixArray.length - 1 ? indexSuffix + 1 : 0;
                    out.append(suffixArray[indexSuffix]);
                    pageContext.setAttribute(var + "_togglerIndex", Integer.toString(indexSuffix));
                }
            }
        }
        if (generatesClassTag) {
            out.append("\"");
        }
        pageContext.setAttribute(var, out.toString());
        if (this.writeHTML) {
            TagUtils.getInstance().write(pageContext, out.toString());
        }
        return TagSupport.EVAL_BODY_INCLUDE;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    public void setGeneratesClassTag(boolean generatesClassTag) {
        this.generatesClassTag = generatesClassTag;
    }

    public boolean isGeneratesClassTag() {
        return generatesClassTag;
    }
}

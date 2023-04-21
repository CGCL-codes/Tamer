package com.faceye.core.util.taglib.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.apache.commons.lang.StringUtils;

public class ToolContainer extends SimpleTagSupport {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void doTag() throws JspException, IOException {
        String innerId;
        try {
            if (StringUtils.isEmpty(this.getId())) {
                innerId = "ToolContainer";
            } else {
                innerId = this.getId();
            }
            JspWriter out = getJspContext().getOut();
            out.print("<div id=\"" + innerId + "\">");
            getJspBody().invoke(null);
            out.print("</div>");
        } catch (JspException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }
    }
}

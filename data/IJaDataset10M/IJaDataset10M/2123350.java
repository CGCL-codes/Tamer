package com.hk.frame.web.taglib;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import com.hk.frame.util.DataUtil;

public class HtmlValueTag extends BaseBodyTag {

    private static final long serialVersionUID = 2444660484543529830L;

    private String value;

    private boolean onerow;

    private boolean textarea;

    public void setTextarea(boolean textarea) {
        this.textarea = textarea;
    }

    public void setOnerow(boolean onerow) {
        this.onerow = onerow;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    protected void adapter(JspWriter writer) throws IOException {
        if (DataUtil.isEmpty(this.value)) {
            return;
        }
        String tmp = null;
        if (onerow) {
            tmp = this.value.replaceAll("\n", "").replaceAll("\r", "");
        } else {
            tmp = this.value;
        }
        if (this.textarea) {
            tmp = DataUtil.toHtmlSimple(tmp).replaceAll("<br/>", "\n");
            writer.append(tmp);
            return;
        }
        writer.append(DataUtil.toHtmlSimple(tmp));
    }
}

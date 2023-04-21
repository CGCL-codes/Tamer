package com.hk.frame.web.taglib.wap;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;

public class ImportCssTag extends BaseWapTag {

    private static final long serialVersionUID = 756365438370223524L;

    private String href;

    @Override
    protected void adapter(JspWriter writer) throws IOException {
        this.getRequest().setAttribute(CSS_IMPORT_ATTR, href);
    }

    public void setHref(String href) {
        this.href = href;
    }
}

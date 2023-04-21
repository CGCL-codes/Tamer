package com.erclab.internal.xpresso.webapp.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import com.erclab.internal.xpresso.conf.Settings;
import com.erclab.internal.xpresso.lang.LanguageException;

public class PrintLabel extends SimpleTagSupport {

    protected String label;

    protected String lang;

    public void setLang(String language) {
        this.lang = language;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void doTag() throws JspException, IOException {
        if (this.lang == null) {
            getJspContext().getOut().print(label);
            return;
        }
        try {
            getJspContext().getOut().print(Settings.getLanguageHandler().getLabel(lang, label));
        } catch (LanguageException e) {
            throw new JspException(e);
        }
    }
}

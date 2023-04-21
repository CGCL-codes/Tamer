package org.blueoxygen.komodo.topic_front;

import com.opensymphony.xwork2.ActionContext;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Enumeration;

public class EditLanguage extends FormCategory {

    private String lang;

    public String execute() {
        ResourceBundle bundle = null;
        Locale locale = null;
        if (lang.equals("indonesia")) {
            locale = Locale.FRANCE;
        } else {
            locale = Locale.US;
        }
        ActionContext.getContext().getSession().put("myLocale", locale);
        bundle = ResourceBundle.getBundle("Message", locale);
        for (Enumeration e = bundle.getKeys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            String s = bundle.getString(key);
            ActionContext.getContext().getSession().put(key, s);
        }
        return SUCCESS;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}

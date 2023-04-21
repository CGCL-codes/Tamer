package com.xtreme.cis.util;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

public class MessageFactory {

    ResourceBundle bundle;

    Locale locale;

    public MessageFactory() {
        locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        bundle = ResourceBundle.getBundle("com.xtreme.cis.util.messages", locale);
    }

    public String getMessage(String key) {
        return bundle.getString(key);
    }
}

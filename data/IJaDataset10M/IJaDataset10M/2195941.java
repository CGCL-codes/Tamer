package com;

import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class Localizer {

    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void localize() {
        String localeParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lang");
        locale = new Locale(localeParam);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
}

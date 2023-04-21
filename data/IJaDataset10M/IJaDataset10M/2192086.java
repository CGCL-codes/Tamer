package com.makeabyte.jhosting.client.session;

import java.util.List;
import java.util.Locale;

public interface Theme {

    public String getLanguage();

    public void setLanguage(String language);

    public void prepareLocaleList();

    public List<String> getLocaleList();

    public Locale getLocale();

    public void setLocale();

    public void setTemplate();

    public void setTemplate(String template);

    public void prepareTemplateList();

    public String getTemplate();

    public List<String> getTemplateList();

    public void setIcon();

    public void setIcon(String icon);

    public void prepareIconList();

    public String getIcon();

    public List<String> getIconList();

    public void setStyle();

    public void setStyle(String style);

    public void prepareStyleList();

    public String getStyle();

    public List<String> getStyleList();

    public void setIceStyle();

    public void setIceStyle(String style);

    public void prepareIceStyleList();

    public String getIceStyle();

    public List<String> getIceStyleList();
}

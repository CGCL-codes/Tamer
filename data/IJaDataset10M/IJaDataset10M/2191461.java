package org.apache.myfaces.config.impl.digester.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller</a>
 */
public class LocaleConfig {

    private String defaultLocale;

    private List<String> supportedLocales = new ArrayList<String>();

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public void addSupportedLocale(String locale) {
        supportedLocales.add(locale);
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public List<String> getSupportedLocales() {
        return supportedLocales;
    }
}

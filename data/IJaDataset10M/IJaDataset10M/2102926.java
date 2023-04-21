package org.zeroexchange.web.i18n;

/**
 * The interface for services aware concerning the current locale.
 * 
 * @author black
 */
public interface LocaleAware {

    /**
     * Sets the current locale.
     */
    void setLocale(String locale);
}

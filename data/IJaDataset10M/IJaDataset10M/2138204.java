package de.hu.gralog.resources;

/**If the locale is changing, the Translator is firing events
 * to all registered LocaleChangeListeners.
 */
public interface LocaleChangeListener {

    /** Method was called if the locale changes
   */
    public void localeChanged(LocaleChangeEvent e);
}

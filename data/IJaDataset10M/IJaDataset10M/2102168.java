package net.dadajax.languagemanager;

import java.io.File;
import java.util.List;

/**
 * @author dadajax
 *
 */
public interface LanguageManager {

    /**
	 * This method localize given string and return localized string.
	 * @param string string which will be localized
	 * @return localized string depending on selected language
	 */
    public String localize(String string);

    /**
	 * It is exactly the same method as localize(). Only different is
	 * only the name of method, which is here shorter :)
	 * @param string string which will be localized
	 * @return localized string depending on selected language
	 */
    public String loc(String string);

    /**
	 * Select current language, which will be used by localize() and loc().
	 * @param languageName name of language
	 * @return <b>true</b> if given language exist or <b>false</b> when language with
	 * 		given string doesn't exist.
	 */
    public boolean setLanguage(String languageName);

    /**
	 * Load languages from given file. LanguageManager should try to load
	 * languages from some default location. You can change this resource by
	 * calling this method.
	 * @param fileName file with stored languages
	 * @return <b>true</> if file exist or <b>false</b> if file doesn't exist
	 */
    public boolean loadLanguages(File file);

    /**
	 * @return a list of available languages.
	 */
    public List<String> getAvailableLanguagesNames();

    /**
	 * Return name of active language as a String.
	 * @return name of active language as a String.
	 */
    public String getActiveLanguage();
}

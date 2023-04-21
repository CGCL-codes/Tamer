package javax.i18n4java;

/**
 * This interface is used to implement a listener for changed translations. This
 * can be used to update GUIs.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public interface LanguageChangeListener {

    /**
	 * This method is called in cases of changed translations
	 * 
	 * @param translator
	 *            is the translator with changed translation.
	 */
    public void translationChanged(Translator translator);
}

package wilos.resources;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import wilos.presentation.web.utils.WebSessionService;
import com.icesoft.faces.context.effects.JavascriptContext;

public class LocaleBean {

    private static final String BASENAME = "wilos/resources/messages";

    private static ResourceBundle bundle = ResourceBundle.getBundle(LocaleBean.BASENAME, Locale.getDefault());

    public LocaleBean() {
    }

    /**
	 * procedure setCurrentLocale
	 * 
	 * @param locale
	 */
    public static void setCurrentLocale(Locale locale) {
        if (locale != null) {
            try {
                Locale.setDefault(locale);
                bundle = ResourceBundle.getBundle(LocaleBean.BASENAME, locale);
            } catch (Exception e) {
                Locale.setDefault(Locale.ENGLISH);
                bundle = ResourceBundle.getBundle(LocaleBean.BASENAME, Locale.ENGLISH);
            }
        }
    }

    /**
	 * function getText
	 * 
	 * @param key
	 * @return String : the key in the properties file
	 */
    public static String getText(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
	 * function getChar
	 * 
	 * @param key
	 * @return char: the mnemonic characters
	 */
    public static char getChar(String key) {
        return LocaleBean.getText(key).charAt(0);
    }
}

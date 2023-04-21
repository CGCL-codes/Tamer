package vh.web;

import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

/**
 * <p>
 * Standard {@link LocaleResolver} of the VH application. The locale to use is read from
 * the HTTP accept-language header. If the requested language is supported, the resolved
 * locale will use the specified language. Otherwise, the default locale is used as
 * fallback.
 * </p>
 * 
 * <p>
 * The {@link Locale} that is returned by the {@link #resolveLocale(HttpServletRequest)}
 * method always uses the country of the default locale. Only the language is changed.
 * Example: If the default locale is 'de-CH' and the client that connects uses 'fr-FR',
 * the resolved locale will be 'fr-CH' (if French is an accepted language, see
 * {@link #setAcceptedLanguages(Set)} for details).
 * </p>
 * 
 * @version $Id: VHLocaleResolver.java 40 2006-10-08 15:36:13Z janjanke $
 * @author jjanke
 */
public class VHLocaleResolver extends AbstractLocaleResolver {

    private Set<String> d_setAcceptedLanguages;

    /**
   * Sets the accepted languages. This LocaleResolver only accepts locales that refer to
   * an accepted language. If a request refers to a non-accepted language, the default
   * language will automatically be used.
   * 
   * @param setLanguages a set containing the officile ISO two letter abbreviations for
   *        all languages that are supported by the application
   */
    public void setAcceptedLanguages(Set<String> setAcceptedLanguages) {
        d_setAcceptedLanguages = setAcceptedLanguages;
    }

    public Locale resolveLocale(HttpServletRequest rq) {
        String strRequestedLanguage = rq.getLocale().getLanguage();
        if (d_setAcceptedLanguages.contains(strRequestedLanguage)) return new Locale(strRequestedLanguage, getDefaultLocale().getCountry()); else return getDefaultLocale();
    }

    public void setLocale(HttpServletRequest rq, HttpServletResponse rp, Locale locale) {
        throw new UnsupportedOperationException("Changing the locale dynamically is not yet supported.");
    }
}

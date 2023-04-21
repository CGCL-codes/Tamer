package be.oniryx.lean.session;

import java.util.List;
import java.util.Locale;

/**
 * User: cedric
 * Date: Mar 26, 2010
 */
public interface LocaleSupport {

    String getName(Locale l);

    void select(Locale l);

    List<Locale> getSupportedLocales();

    Locale getLocale(String language);
}

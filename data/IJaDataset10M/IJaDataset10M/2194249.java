package org.broadleafcommerce.common.locale.service;

import org.broadleafcommerce.common.locale.domain.Locale;
import java.util.List;

/**
 * Created by bpolster.
 */
public interface LocaleService {

    /**
     * @return The locale for the passed in code
     */
    public Locale findLocaleByCode(String localeCode);

    /**
     * Returns the page template with the passed in id.
     *
     * @return The default locale
     */
    public Locale findDefaultLocale();

    /**
     * Returns the page template with the passed in id.
     *
     * @return The default locale
     */
    public List<Locale> findAllLocales();
}

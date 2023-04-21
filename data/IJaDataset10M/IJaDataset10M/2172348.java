package org.openwms.core.service;

import java.util.List;
import org.openwms.core.domain.system.I18n;

/**
 * An I18nService is responsible to find and save i18n translations.
 * 
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version $Revision: 1555 $
 * @since 0.1
 */
public interface I18nService {

    /**
     * Find and return a list of all translations.
     * 
     * @return all translations
     */
    List<I18n> findAllTranslations();

    /**
     * Save an arbitrary collection of translations. No matter, whether entities
     * in the collection already exist or not.
     * 
     * @param translations
     *            An vararg of translations to save
     */
    void saveTranslations(I18n... translations);
}

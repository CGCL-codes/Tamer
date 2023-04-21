package org.nakedobjects.runtime.system.internal;

import java.util.Locale;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.system.SystemConstants;

public class NakedObjectsLocaleInitializer {

    public static final Logger LOG = Logger.getLogger(NakedObjectsLocaleInitializer.class);

    public void initLocale(NakedObjectConfiguration configuration) {
        final String localeSpec = configuration.getString(SystemConstants.LOCALE_KEY);
        if (localeSpec != null) {
            final int pos = localeSpec.indexOf('_');
            Locale locale;
            if (pos == -1) {
                locale = new Locale(localeSpec, "");
            } else {
                final String language = localeSpec.substring(0, pos);
                final String country = localeSpec.substring(pos + 1);
                locale = new Locale(language, country);
            }
            Locale.setDefault(locale);
            LOG.info("locale set to " + locale);
        }
        LOG.debug("locale is " + Locale.getDefault());
    }
}

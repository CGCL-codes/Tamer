package bizcal.util;

import java.util.Locale;

/**
 * Interface that provides the current locale.
 * 
 * @author Fredrik Bertilsson
 */
public interface LocaleCallback {

    public Locale getLocale() throws Exception;

    public static class DefaultImpl implements LocaleCallback {

        private Locale _locale;

        public DefaultImpl() {
            _locale = Locale.getDefault();
        }

        public DefaultImpl(Locale locale) {
            _locale = locale;
        }

        @Override
        public Locale getLocale() throws Exception {
            return _locale;
        }
    }
}

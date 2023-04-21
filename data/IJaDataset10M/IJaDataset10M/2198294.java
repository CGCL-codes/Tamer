package br.com.sysmap.crux.core.i18n;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Thiago da Rosa de Bustamante
 * @author Gesse S. F. Dafe
 */
public interface LocaleResolver {

    void initializeUserLocale(HttpServletRequest request);

    Locale getUserLocale() throws LocaleResolverException;
}

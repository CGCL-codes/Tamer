package org.osmius.webapp.filter;

import org.osmius.Constants;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.*;

/**
 * Filter to wrap request with a request including user preferred locale.
 */
public class LocaleFilter extends OncePerRequestFilter {

    private final transient Log log = LogFactory.getLog(LocaleRequestWrapper.class);

    private static final List<Locale> SUPPORTED_LOCALES = new ArrayList<Locale>();

    private static final Locale DEFAULT_LOCALE = new Locale("en", "US");

    private static final List<String> SUPPORTED_THEMES = new ArrayList<String>();

    static {
        SUPPORTED_LOCALES.add(DEFAULT_LOCALE);
        SUPPORTED_LOCALES.add(new Locale("es", "ES"));
        SUPPORTED_THEMES.add("osmius");
    }

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Locale preferredLocale = getLocaleParameter(request.getParameter("locale"));
        HttpSession session = request.getSession();
        if (preferredLocale == null) {
            preferredLocale = (Locale) session.getAttribute(Constants.PREFERRED_LOCALE_KEY);
            if (preferredLocale == null) {
                Enumeration<Locale> userLocales = request.getLocales();
                if (userLocales != null) {
                    while (userLocales.hasMoreElements() && preferredLocale == null) preferredLocale = getLocaleParameter(userLocales.nextElement().getLanguage());
                    if (preferredLocale != null && log.isDebugEnabled()) log.debug("Se aplica el locale '" + preferredLocale + "' preferido en el explorador");
                }
                if (preferredLocale == null) {
                    preferredLocale = new Locale(DEFAULT_LOCALE.getLanguage(), DEFAULT_LOCALE.getCountry());
                    if (log.isDebugEnabled()) log.debug("Se aplica el locale '" + DEFAULT_LOCALE + "' por defecto");
                }
                session.setAttribute(Constants.PREFERRED_LOCALE_KEY, preferredLocale);
                Config.set(session, Config.FMT_LOCALE, preferredLocale);
            }
        } else {
            session.setAttribute(Constants.PREFERRED_LOCALE_KEY, preferredLocale);
            Config.set(session, Config.FMT_LOCALE, preferredLocale);
        }
        if (!(request instanceof LocaleRequestWrapper)) {
            request = new LocaleRequestWrapper(request, preferredLocale);
            LocaleContextHolder.setLocale(preferredLocale);
        }
        String theme = request.getParameter("theme");
        if (isThemeValid(theme) && request.isUserInRole(Constants.ADMIN_ROLE)) {
            Map config = (Map) getServletContext().getAttribute(Constants.CONFIG);
            config.put(Constants.CSS_THEME, theme);
        }
        chain.doFilter(request, response);
        LocaleContextHolder.setLocaleContext(null);
    }

    private Locale getLocaleParameter(String locale) {
        if (locale != null) {
            for (Locale localeList : SUPPORTED_LOCALES) {
                if (localeList.getLanguage().equals(locale)) return new Locale(localeList.getLanguage(), localeList.getCountry());
            }
        }
        return null;
    }

    private boolean isThemeValid(String theme) {
        if (theme != null) {
            for (String themeList : SUPPORTED_THEMES) {
                if (themeList.equals(theme)) return true;
            }
        }
        return false;
    }
}

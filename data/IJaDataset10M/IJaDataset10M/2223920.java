package org.corrib.s3b.mbb.i18n;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.corrib.s3b.mbb.beans.ConfigKeeper;

/**
 *
 * @author  Piotr Piotrowski
 * @version
 */
public class LocaleFilter implements Filter {

    public static final String LANG = "lang";

    public static final String LOCALE = "lang.locale";

    private FilterConfig filterConfig = null;

    public LocaleFilter() {
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param result The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        setLocale((HttpServletRequest) request);
        chain.doFilter(request, response);
    }

    /**
     * Do not use this method. It is public for one reason and one reason only.
     * It is used in login.jsp as a hack - no filters are called before it.
     */
    @SuppressWarnings("unchecked")
    public static void setLocale(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        String lang = request.getParameter(LANG);
        if (lang != null && !lang.equals("")) {
            session.setAttribute(LANG, lang);
            session.setAttribute(LOCALE, getLocale(lang));
        } else if (session.getAttribute(LANG) == null && request.getHeader("accept-language") != null) {
            Locale pref = getPrefferedLocale(request.getLocales());
            if (pref != null) {
                session.setAttribute(LANG, pref.toString());
                session.setAttribute(LOCALE, pref);
            } else {
                session.setAttribute(LANG, request.getLocale().toString());
                session.setAttribute(LOCALE, request.getLocale());
            }
        } else if (session.getAttribute(LANG) == null) {
            session.setAttribute(LANG, getDefaultLocale());
            session.setAttribute(LOCALE, getLocale(getDefaultLocale()));
        }
    }

    /**
     * Iterates through the given Enumeration of Locale objects.
     * Returns the first Locale objects which lang part is known to the system.
     * If no such Locale is found null is returned.
     */
    private static Locale getPrefferedLocale(Enumeration<Locale> locales) {
        while (locales.hasMoreElements()) {
            Locale locale = locales.nextElement();
            if (Langs.isLangKnown(locale.getLanguage())) {
                return locale;
            }
        }
        return null;
    }

    /**
     * Returns a Locale object based on the given locale name.
     * The locale name is in the format returned by Locale.toString().
     */
    private static Locale getLocale(String lang) {
        String[] tmp = lang.split("_", 3);
        if (tmp.length == 3) {
            return new Locale(tmp[0], tmp[1], tmp[2]);
        } else if (tmp.length == 2) {
            return new Locale(tmp[0], tmp[1]);
        } else {
            return new Locale(tmp[0]);
        }
    }

    private static synchronized String getDefaultLocale() {
        return ConfigKeeper.getProperty("core.i18n.defaultLanguage");
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param _filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig _filterConfig) {
        this.filterConfig = _filterConfig;
    }

    /**
     * Destroy method for this filter
     *
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     */
    public void init(FilterConfig _filterConfig) {
        this.filterConfig = _filterConfig;
    }

    /**
     * Return a String representation of this object.
     */
    public String toString() {
        if (filterConfig == null) return ("LocaleFilter()");
        StringBuffer sb = new StringBuffer("LocaleFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
}

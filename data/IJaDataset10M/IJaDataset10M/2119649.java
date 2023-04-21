package com.potix.zk.fn;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;
import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DecimalFormatSymbols;
import java.io.Writer;
import java.io.IOException;
import javax.servlet.ServletRequest;
import com.potix.lang.Strings;
import com.potix.lang.Objects;
import com.potix.util.CacheMap;
import com.potix.util.Locales;
import com.potix.util.logging.Log;
import com.potix.web.fn.ServletFns;
import com.potix.web.servlet.JavaScript;
import com.potix.web.servlet.StyleSheet;
import com.potix.zk.ui.Component;
import com.potix.zk.ui.Page;
import com.potix.zk.ui.Execution;
import com.potix.zk.ui.Executions;
import com.potix.zk.ui.UiException;
import com.potix.zk.ui.sys.ExecutionsCtrl;
import com.potix.zk.ui.metainfo.LanguageDefinition;
import com.potix.zk.au.AuResponse;

/**
 * Utilities for using EL.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public class ZkFns {

    private static final Log log = Log.lookup(ZkFns.class);

    /** Denotes whether style sheets are generated for this request. */
    private static final String ATTR_LANG_CSS_GENED = "javax.potix.zk.lang.css.generated";

    /** Denotes whether JavaScripts are generated for this request. */
    private static final String ATTR_LANG_JS_GENED = "javax.potix.zk.lang.js.generated";

    /** Redraw the specified component into the specified out.
	 *
	 * @param comp the component. If null, nothing happens
	 * @param out the output. If null, the current output
	 * will be used.
	 */
    public static final void redraw(Component comp, Writer out) throws IOException {
        if (comp == null) return;
        if (out == null) out = ServletFns.getCurrentOut();
        try {
            comp.redraw(out);
        } catch (Throwable ex) {
            log.realCauseBriefly("Failed to redraw " + comp, ex);
            if (ex instanceof IOException) throw (IOException) ex;
            throw UiException.Aide.wrap(ex);
        }
    }

    /** Returns JavaScript for handling the specified response.
	 */
    public static final String outResponseJavaScripts(Collection responses) {
        if (responses == null || responses.isEmpty()) return "";
        final StringBuffer sb = new StringBuffer(256).append("zk.addInit(function(){\n");
        for (Iterator it = responses.iterator(); it.hasNext(); ) {
            final AuResponse response = (AuResponse) it.next();
            sb.append("zk.process('").append(response.getCommand()).append("',");
            final String[] data = response.getData();
            final int datanum = data != null ? data.length : 0;
            sb.append(datanum);
            for (int j = 0; j < datanum; ++j) {
                sb.append(",\"");
                if (data[j] != null) sb.append(Strings.escape(data[j], "\"\\\n\r"));
                sb.append('"');
            }
            sb.append(");\n");
        }
        return sb.append("});").toString();
    }

    /** Returns HTML tags to include all JavaScript files and codes that are
	 * required when loading a ZUML page.
	 *
	 * <p>FUTURE CONSIDERATION: we might generate the inclusion on demand
	 * instead of all at once.
	 */
    public static final String outLangJavaScripts(Page page, String action) {
        final ServletRequest request = ServletFns.getCurrentRequest();
        if (request.getAttribute(ATTR_LANG_JS_GENED) != null) return "";
        request.setAttribute(ATTR_LANG_JS_GENED, Boolean.TRUE);
        if (action == null) throw new IllegalArgumentException("null");
        final StringBuffer sb = new StringBuffer(512);
        sb.append("<script type=\"text/javascript\">\n").append("zk_action=\"").append(action).append("\";\nzk_desktopId=\"").append(page.getDesktop().getId()).append("\";\n</script>\n");
        final Set jses = new LinkedHashSet(37);
        for (Iterator it = LanguageDefinition.getAll().iterator(); it.hasNext(); ) jses.addAll(((LanguageDefinition) it.next()).getJavaScripts());
        for (Iterator it = jses.iterator(); it.hasNext(); ) append(sb, (JavaScript) it.next());
        for (Iterator it = LanguageDefinition.getAll().iterator(); it.hasNext(); ) {
            final LanguageDefinition langdef = (LanguageDefinition) it.next();
            final Set mods = langdef.getJavaScriptModules().entrySet();
            if (!mods.isEmpty()) {
                sb.append("\n<script type=\"text/javascript\">");
                for (Iterator e = mods.iterator(); e.hasNext(); ) {
                    final Map.Entry me = (Map.Entry) e.next();
                    sb.append("\nzk.mods[\"").append(me.getKey()).append("\"]=\"").append(me.getValue()).append("\";");
                }
                sb.append("\n</script>");
            }
        }
        return sb.toString();
    }

    private static void append(StringBuffer sb, JavaScript js) {
        sb.append("\n<script type=\"text/javascript\"");
        if (js.getSrc() != null) {
            String url;
            try {
                url = ServletFns.encodeURL(js.getSrc());
            } catch (javax.servlet.ServletException ex) {
                throw new UiException(ex);
            }
            int j = url.lastIndexOf(';');
            if (j > 0 && url.indexOf('.', j + 1) < 0 && url.indexOf('/', j + 1) < 0) url = url.substring(0, j);
            sb.append(" src=\"").append(url).append('"');
            final String charset = js.getCharset();
            if (charset != null) sb.append(" charset=\"").append(charset).append('"');
            sb.append('>');
        } else {
            sb.append(">\n").append(js.getContent());
        }
        sb.append("\n</script>");
    }

    /** Returns HTML tags to include all style sheets that are
	 * required when loading a ZUML page.
	 *
	 * <p>In addition to style sheets defined in lang.xml and lang-addon.xml,
	 * it also include:
	 * <ol>
	 * <li>The style sheet specified in the theme-uri parameter.</li>
	 * <li>All style sheets, if any, of pages belonging to the
	 * current desktop.</li>
	 * </ol>
	 *
	 * <p>FUTURE CONSIDERATION: we might generate the inclusion on demand
	 * instead of all at once.
	 */
    public static final String outLangStyleSheets() {
        final ServletRequest request = ServletFns.getCurrentRequest();
        if (request.getAttribute(ATTR_LANG_CSS_GENED) != null) return "";
        request.setAttribute(ATTR_LANG_CSS_GENED, Boolean.TRUE);
        final Execution exec = Executions.getCurrent();
        final StringBuffer sb = new StringBuffer(512);
        for (Iterator it = LanguageDefinition.getAll().iterator(); it.hasNext(); ) for (Iterator e = ((LanguageDefinition) it.next()).getStyleSheets().iterator(); e.hasNext(); ) append(sb, (StyleSheet) e.next(), exec, null);
        final String href = exec != null ? exec.getDesktop().getWebApp().getConfiguration().getThemeURI() : null;
        if (href != null && href.length() > 0) append(sb, new StyleSheet(href, "text/css"), exec, null);
        if (exec != null) {
            for (Iterator it = exec.getDesktop().getPages().iterator(); it.hasNext(); ) {
                final Page page = (Page) it.next();
                for (Iterator e = page.getStyleSheets().iterator(); e.hasNext(); ) append(sb, (StyleSheet) e.next(), exec, page);
            }
        }
        return sb.toString();
    }

    private static void append(StringBuffer sb, StyleSheet ss, Execution exec, Page page) {
        String href = ss.getHref();
        if (href != null) {
            try {
                if (exec != null) href = (String) exec.evaluate(page, href, String.class);
                if (href != null && href.length() > 0) sb.append("\n<link rel=\"stylesheet\" type=\"").append(ss.getType()).append("\" href=\"").append(ServletFns.encodeURL(href)).append("\"/>");
            } catch (javax.servlet.ServletException ex) {
                throw new UiException(ex);
            }
        } else {
            sb.append("\n<style");
            if (ss.getType() != null) sb.append(" type=\"").append(ss.getType()).append('"');
            sb.append(">\n").append(ss.getContent()).append("\n</type>");
        }
    }

    /** Converts the specified URI to absolute if necessary.
	 * Refer to {@link Execution#toAbsoluteURI}.
	 */
    public static String toAbsoluteURI(String uri) {
        return Executions.getCurrent().toAbsoluteURI(uri);
    }

    /** Generates Locale-dependent strings in JavaScript syntax.
	 */
    public static final String outLocaleJavaScript() {
        final Locale locale = Locales.getCurrent();
        return outNumberJavaScript(locale) + outDateJavaScript(locale);
    }

    /** Output number relevant texts.
	 */
    private static final String outNumberJavaScript(Locale locale) {
        final DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        final StringBuffer sb = new StringBuffer(128);
        appendAssignJavaScript(sb, "zk.GROUPING", symbols.getGroupingSeparator());
        appendAssignJavaScript(sb, "zk.DECIMAL", symbols.getDecimalSeparator());
        appendAssignJavaScript(sb, "zk.PERCENT", symbols.getPercent());
        appendAssignJavaScript(sb, "zk.MINUS", symbols.getMinusSign());
        return sb.toString();
    }

    private static final void appendAssignJavaScript(StringBuffer sb, String nm, char val) {
        sb.append(nm).append("='").append(val).append("';\n");
    }

    /** Output date/calendar relevant labels.
	 */
    private static final String outDateJavaScript(Locale locale) {
        synchronized (_datejs) {
            final String djs = (String) _datejs.get(locale);
            if (djs != null) return djs;
        }
        String djs = getDateJavaScript(locale);
        synchronized (_datejs) {
            for (Iterator it = _datejs.values().iterator(); it.hasNext(); ) {
                final String val = (String) it.next();
                if (val.equals(djs)) djs = val;
            }
            _datejs.put(locale, djs);
        }
        return djs;
    }

    private static final String getDateJavaScript(Locale locale) {
        final StringBuffer sb = new StringBuffer(512);
        final Calendar cal = Calendar.getInstance(locale);
        cal.clear();
        final int firstDayOfWeek = cal.getFirstDayOfWeek();
        sb.append("zk.DOW_1ST=").append(firstDayOfWeek - Calendar.SUNDAY).append(";\n");
        final boolean zhlang = locale.getLanguage().equals("zh");
        SimpleDateFormat df = new SimpleDateFormat("E", locale);
        final String[] sdow = new String[7], s2dow = new String[7];
        for (int j = firstDayOfWeek, k = 0; k < 7; ++k) {
            cal.set(Calendar.DAY_OF_WEEK, j);
            sdow[k] = df.format(cal.getTime());
            if (++j > Calendar.SATURDAY) j = Calendar.SUNDAY;
            if (zhlang) {
                s2dow[k] = sdow[k].length() >= 3 ? sdow[k].substring(2) : sdow[k];
            } else {
                final int len = sdow[k].length();
                final char cc = sdow[k].charAt(len - 1);
                s2dow[k] = cc == '.' || cc == ',' ? sdow[k].substring(0, len - 1) : sdow[k];
            }
        }
        df = new SimpleDateFormat("EEEE", locale);
        final String[] fdow = new String[7];
        for (int j = firstDayOfWeek, k = 0; k < 7; ++k) {
            cal.set(Calendar.DAY_OF_WEEK, j);
            fdow[k] = df.format(cal.getTime());
            if (++j > Calendar.SATURDAY) j = Calendar.SUNDAY;
        }
        df = new SimpleDateFormat("MMM", locale);
        final String[] smon = new String[12], s2mon = new String[12];
        for (int j = 0; j < 12; ++j) {
            cal.set(Calendar.MONTH, j);
            smon[j] = df.format(cal.getTime());
            if (zhlang) {
                s2mon[j] = smon[0].length() >= 2 ? smon[j].substring(0, smon[j].length() - 1) : smon[j];
            } else {
                final int len = smon[j].length();
                final char cc = smon[j].charAt(len - 1);
                s2mon[j] = cc == '.' || cc == ',' ? smon[j].substring(0, len - 1) : smon[j];
            }
        }
        df = new SimpleDateFormat("MMMM", locale);
        final String[] fmon = new String[12];
        for (int j = 0; j < 12; ++j) {
            cal.set(Calendar.MONTH, j);
            fmon[j] = df.format(cal.getTime());
        }
        appendDateJavaScript(sb, "SDOW", sdow);
        if (Objects.equals(s2dow, sdow)) sb.append("zk.S2DOW=zk.SDOW;\n"); else appendDateJavaScript(sb, "S2DOW", s2dow);
        if (Objects.equals(fdow, sdow)) sb.append("zk.FDOW=zk.SDOW;\n"); else appendDateJavaScript(sb, "FDOW", fdow);
        appendDateJavaScript(sb, "SMON", smon);
        if (Objects.equals(s2mon, smon)) sb.append("zk.S2MON=zk.SMON;\n"); else appendDateJavaScript(sb, "S2MON", s2mon);
        if (Objects.equals(fmon, smon)) sb.append("zk.FMON=zk.SMON;\n"); else appendDateJavaScript(sb, "FMON", fmon);
        return sb.toString();
    }

    private static final void appendDateJavaScript(StringBuffer sb, String varnm, String[] vals) {
        sb.append("zk.").append(varnm).append("=new Array(");
        for (int j = 0; ; ) {
            sb.append('"').append(Strings.escape(vals[j], "\\\"")).append('"');
            if (++j >= vals.length) break; else sb.append(',');
        }
        sb.append(");\n");
    }

    private static final CacheMap _datejs = new CacheMap().setLifetime(24 * 60 * 60 * 1000);
}

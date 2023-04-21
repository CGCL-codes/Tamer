package org.jaxson.util.locale;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;

/**
 * Utilities associated with loading and managing resource bundles.
 * 
 * @author Joe Maisel
 */
public class LocaleUtil {

    private static Map<String, ResourceBundle> bundles = new HashMap<String, ResourceBundle>();

    private static Map<String, Long> timestamps = new HashMap<String, Long>();

    private static ServletContext ctx;

    public static ResourceBundle getBundle(String bundlePath, Locale locale) throws IOException {
        if (ctx == null) throw new IllegalStateException("ServletContext not set.");
        locale = locale == null ? Locale.US : locale;
        String path = ctx.getRealPath(bundlePath + "_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties");
        File f = new File(path);
        if (!f.exists()) {
            throw new FileNotFoundException(path + " cannot be found");
        }
        if (!timestamps.containsKey(path) || !timestamps.get(path).equals(f.lastModified())) {
            bundles.put(path, new NotNullPropertyResourceBundle(new FileInputStream(f)));
            timestamps.put(path, f.lastModified());
        }
        return bundles.get(path);
    }

    /**
	 * @param ctx
	 *            the ServletContext to set
	 */
    public static void setServletContext(ServletContext ctx) {
        LocaleUtil.ctx = ctx;
    }
}

package cookxml.common.helper;

import java.util.Locale;
import cookxml.core.interfaces.Helper;

/**
 * Helper class for constructing a Locale object.
 *
 * @cxhelper java.util.Locale
 *
 * @author Heng Yuan
 * @version $Id: LocaleHelper.java 215 2007-06-06 03:59:41Z coconut $
 * @since CookXml 1.0
 */
public class LocaleHelper implements Helper {

    /** The name of the locale. */
    public Locale value;

    public Object getFinalObject() {
        return value;
    }
}

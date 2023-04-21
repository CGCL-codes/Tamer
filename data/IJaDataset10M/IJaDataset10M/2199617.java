package cookxml.cookswing.helper;

import java.awt.*;
import java.util.Locale;
import javax.swing.*;
import cookxml.core.interfaces.Helper;

/**
 * Helper class for constructing a Font object.
 *
 * @cxhelper java.awt.Font
 *
 * @author Heng Yuan
 * @version $Id: FontHelper.java 215 2007-06-06 03:59:41Z coconut $
 * @since CookSwing 1.0
 */
public class FontHelper implements Helper {

    /** The font name,type,size. */
    public Font value;

    /** the ui key used to look up the Font in UIManager */
    public String ui;

    /** Local of the ui key.  If not specified, the default is used. */
    public Locale locale;

    public Object getFinalObject() {
        if (value != null) return value;
        if (ui != null) {
            if (locale != null) return UIManager.getFont(ui, locale);
            return UIManager.getFont(ui);
        }
        return null;
    }
}

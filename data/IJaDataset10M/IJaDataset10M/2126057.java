package edu.xtec.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.SwingUtilities;

/**
 *
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public abstract class LFUtil {

    /** Key for Options
     */
    public static final String LOOK_AND_FEEL = "lookAndFeel";

    /** Default look & feel name
     */
    public static final String DEFAULT = "default";

    /** System look & feel name
     */
    public static final String SYSTEM = "system";

    /** Metal look & feel name
     */
    public static final String METAL = "metal";

    /** Motif look & feel name
     */
    public static final String MOTIF = "motif";

    /** Windows look & feel name
     */
    public static final String WINDOWS = "windows";

    public static final String[] VALUES = { DEFAULT, SYSTEM, METAL, MOTIF };

    /** Sets the app look & feel
     * @param friendlyName Look & feel name. If null, empty or not recognized this function does nohing.
     */
    public static void setLookAndFeel(String friendlyName, Component rootComponent) {
        if (friendlyName != null) {
            try {
                setLookAndFeel(null, "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel", rootComponent);
            } catch (Exception e) {
                System.err.println("AQUI unable to set lookAndFeel to: \"" + friendlyName + "\"\n" + e);
            }
        }
    }

    private static void setLookAndFeel(String prefix, String className, Component rootComponent) throws Exception {
        javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    }

    public static Color getSysColor(String key, Color defaultValue) {
        Color result = javax.swing.UIManager.getColor(key);
        return result == null ? defaultValue : result;
    }

    public static Color getColor(String key, Color defaultValue) {
        Color result = defaultValue;
        Object o = javax.swing.UIManager.get(key);
        if (o != null && o instanceof Color) result = (Color) o;
        return result;
    }

    public static Font getFont(String key, Font defaultValue) {
        Font result = defaultValue;
        Object o = javax.swing.UIManager.get(key);
        if (o != null && o instanceof Font) result = (Font) o;
        return result;
    }
}

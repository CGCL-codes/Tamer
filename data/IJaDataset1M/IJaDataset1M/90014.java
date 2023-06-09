package sun.font;

import java.awt.Font;
import java.util.Locale;
import java.util.TreeMap;

/**
 * This is an extension of the {@link FontManager} interface which has to
 * be implemented on systems that want to use SunGraphicsEnvironment. It
 * adds a couple of methods that are only required by SGE. Graphics
 * implementations that use their own GraphicsEnvironment are not required
 * to implement this and can use plain FontManager instead.
 */
public interface FontManagerForSGE extends FontManager {

    /**
     * Return an array of created Fonts, or null, if no fonts were created yet.
     */
    public Font[] getCreatedFonts();

    /**
     * Similar to getCreatedFonts, but returns a TreeMap of fonts by family name.
     */
    public TreeMap<String, String> getCreatedFontFamilyNames();

    /**
     * Returns all fonts installed in this environment.
     */
    public Font[] getAllInstalledFonts();

    public String[] getInstalledFontFamilyNames(Locale requestedLocale);
}

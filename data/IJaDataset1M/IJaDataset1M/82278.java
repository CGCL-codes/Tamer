package edu.xtec.util;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.io.File;
import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.awt.geom.AffineTransform;
import java.awt.font.TextLayout;
import java.lang.reflect.Method;

/**
 *
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class FontCheck {

    public static final String DEFAULT_FONT_NAME = "default";

    public static final Font DEFAULT_FONT = new Font(DEFAULT_FONT_NAME, Font.PLAIN, 17);

    public static final String TMP_FONT_PREFIX = "tmp_font_";

    private static final HashMap systemFonts = new HashMap();

    private static String[] fontList;

    public static final String[] fontSizes = new String[] { "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72" };

    private FontCheck() {
    }

    public static String[] getFontList(boolean reload) {
        if (fontList == null || reload) fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        return fontList;
    }

    public static boolean checkFont(Font font) {
        boolean result = false;
        if (font != null) {
            FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
            TextLayout layout = new TextLayout("AB", font, frc);
            result = layout.getBounds().getWidth() > 1;
        }
        return result;
    }

    public static boolean checkFontFamilyName(Object family) {
        return family != null && family instanceof String && checkFont(new Font((String) family, Font.PLAIN, 17));
    }

    public static Font getValidFont(String family, int style, int size) {
        Font f = new Font(family, style, size);
        if (!checkFont(f)) {
            Font fontBase = (Font) systemFonts.get(family.toLowerCase());
            if (fontBase == null) {
                fontBase = DEFAULT_FONT;
            }
            f = fontBase.deriveFont(style, size);
        }
        return f;
    }

    public static String getValidFontFamilyName(Object family) {
        Font f = (Font) systemFonts.get(family instanceof String ? ((String) family).toLowerCase() : family);
        if (f != null) return f.getFamily();
        return checkFontFamilyName(family) ? (String) family : DEFAULT_FONT_NAME;
    }

    public static Font checkSystemFont(String fontName, String fontFileName) {
        String fnLower = fontName.toLowerCase();
        Font f = (Font) systemFonts.get(fnLower);
        if (f == null) {
            f = new Font(fontName, Font.PLAIN, 17);
            if (!checkFont(f) || !fontName.toLowerCase().equals(f.getFamily().toLowerCase())) {
                try {
                    f = buildNewFont(fontFileName, ResourceManager.STREAM_PROVIDER, "fonts/" + fontFileName);
                    if (checkFont(f)) systemFonts.put(fnLower, f); else f = DEFAULT_FONT;
                } catch (Exception ex) {
                    System.err.println("Unable to build font " + fontName + "\n:" + ex);
                }
            }
        }
        return f;
    }

    public static Font buildNewFont(String fileName, StreamIO.InputStreamProvider isp, String resourceName) throws Exception {
        String tmpDir = System.getProperty("java.io.tmpdir");
        if (tmpDir == null) throw new Exception("Unable to create fonts: No temp dir!");
        File fontFile = new File(tmpDir + File.separator + TMP_FONT_PREFIX + fileName);
        if (!fontFile.exists()) StreamIO.writeStreamTo(isp.getInputStream(resourceName), new FileOutputStream(fontFile));
        String createName = null;
        try {
            if (FontCheck.class.getResource("/sun/java2d/SunGraphicsEnvironment.class") != null) {
                Class cl = Class.forName("sun.java2d.SunGraphicsEnvironment");
                if (cl != null) {
                    Method m = cl.getMethod("createFont", new Class[] { File.class });
                    if (m != null) {
                        createName = (String) m.invoke(null, new Object[] { fontFile });
                    }
                }
            }
        } catch (Exception ex) {
        }
        if (createName == null) throw new Exception("Unable to create font - bad font data");
        return getValidFont(createName, Font.PLAIN, 1);
    }
}

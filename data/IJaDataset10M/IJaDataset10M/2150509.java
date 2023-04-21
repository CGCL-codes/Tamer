package org.apache.myfaces.trinidadinternal.style.util;

import java.awt.Color;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidad.util.ArrayMap;
import org.apache.myfaces.trinidadinternal.style.CSSStyle;
import org.apache.myfaces.trinidadinternal.style.PropertyParseException;
import org.apache.myfaces.trinidadinternal.style.Style;
import org.apache.myfaces.trinidadinternal.util.LRUCache;

/**
 * CSS-related utilities. I think as we move away from xss, most of this code will
 * be removed.
 *
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/style/util/CSSUtils.java#0 $) $Date: 10-nov-2005.18:58:49 $
 */
public class CSSUtils {

    /**
   * Resolve the propertyValue. For example, if the propertyValue is an url, then we run
   * it through the encodeResourceURL method.
   * @param styleSheetName - The Skin's stylesheet name.
   * @param baseURI - An absolute base URI pointing to the directory which contains the 
   * skin style sheet. You can compute this value by calling getBaseSkinStyleSheetURI and
   * passing in the styleSheetName.
   * @param propertyName - The css property name, like background-image
   * @param propertyValue -The css property value, like url("skins/purple/error.gif");
   * @return the propertyValue, possibly changed to a 'resolved' propertyValue. For instance,
   * if it is an url, the return value will be the encoded url.
   */
    public static String resolvePropertyValue(String styleSheetName, String baseURI, String propertyName, String propertyValue) {
        if (_containsURL(propertyValue)) {
            String resolvedUrl = _resolveURL(styleSheetName, baseURI, propertyValue);
            return resolvedUrl;
        } else {
            if (_URI_PROPERTIES.contains(propertyName)) {
                if (!_SPECIAL_URI_VALUES.contains(propertyValue)) {
                    _LOG.warning("URL_VALUE_EXPECTED_FOR_PROPERTY_IN_STYLE_SHEET", new Object[] { propertyName, styleSheetName, propertyValue });
                }
            }
            return propertyValue;
        }
    }

    /**
   * Given a Skin's stylesheet name that is being parsed,
   * return an absolute base URI pointing to the
   * directory which contains the skin style sheet.  We will use this
   * base URI to ensure that URLs specified in the style sheet
   * (eg. background-image URLs) are resolved appropriately
   * (ie. not relative to the generated style sheet).
   * 
   * @param styleSheetName - The Skin's stylesheet name. E.g., "/skins/purple/purpleSkin.css"
   * @return an absolute base URI pointing to the
  // directory which contains the skin style sheet. e.g., "/trinidad-context/skins/purple"
   */
    public static String getBaseSkinStyleSheetURI(String styleSheetName) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        assert (facesContext != null);
        ExternalContext externalContext = facesContext.getExternalContext();
        String contextPath = externalContext.getRequestContextPath();
        assert (contextPath != null);
        int contextPathLength = contextPath.length();
        assert (contextPathLength > 0);
        assert (contextPath.charAt(0) == '/');
        assert (contextPath.charAt(contextPathLength - 1) != '/');
        assert (styleSheetName.length() > 0);
        assert (styleSheetName.charAt(0) != '/');
        if (styleSheetName.startsWith("META-INF/")) styleSheetName = styleSheetName.substring(9);
        int lastSepIndex = styleSheetName.lastIndexOf('/');
        if (lastSepIndex == -1) return contextPath; else {
            StringBuilder buffer = new StringBuilder(contextPathLength + lastSepIndex + 1);
            buffer.append(contextPath);
            buffer.append("/");
            buffer.append(styleSheetName.substring(0, lastSepIndex));
            return buffer.toString();
        }
    }

    /**
   * Convert a relative URI values to an absolute URI value.
   * For example, if the baseURI is "/trinidad-context/skins/purple" and 
   * the uri is "../../skins/purple/xyz.gif", we return 
   * @param styleSheetName - the name of the Skin's stylesheet. We use this in any warning 
   * messages.
   * @param baseURI - absolute base URI pointing to the directory 
   * which contains the skin style sheet. This is used to figure out the absolute uri of the uri
   * parameter.
   * @param uri - a uri. If this is an uri that begins with "../", then
   * we convert it to be an absolute url that has no "../" at the start.

   * @return An uri that does not begin with one or more "../" strings.
   */
    public static String getAbsoluteURIValue(String styleSheetName, String baseURI, String uri) {
        String strippedURI = uri;
        String strippedBaseURI = baseURI;
        while (strippedURI.startsWith("../")) {
            int lastSepIndex = strippedBaseURI.lastIndexOf('/');
            if (lastSepIndex < 0) {
                _LOG.warning("INVALID_IMAGE_URI_IN_STYLE_SHEET", new Object[] { uri, styleSheetName });
                break;
            }
            strippedURI = strippedURI.substring(3);
            strippedBaseURI = strippedBaseURI.substring(0, lastSepIndex);
        }
        StringBuilder builder = new StringBuilder(strippedBaseURI.length() + strippedURI.length() + 2);
        builder.append(strippedBaseURI);
        builder.append("/");
        builder.append(strippedURI);
        return builder.toString();
    }

    /**
   * Parse an inline style into a CSSStyle object.
   */
    public static CSSStyle parseStyle(String text) {
        if ((text == null) || "".equals(text)) return null;
        if (text.startsWith("{")) text = text.substring(1);
        if (text.endsWith("}")) text = text.substring(0, text.length() - 1);
        CSSStyle style = new CSSStyle();
        StringTokenizer tokens = new StringTokenizer(text, ";");
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            int colonPos = token.indexOf(':');
            if (colonPos >= 0) {
                String key = token.substring(0, colonPos).trim();
                String value = token.substring(colonPos + 1).trim();
                style.setProperty(key, value);
            }
        }
        return style;
    }

    /**
   * Parses the CSS color value.
   *
   * @param value A CSS color value.
   * @return A Color object which corresponds to the provided value
   * @throws PropertyParseException Thrown if the specified color
   *   value can not be parsed.
   */
    public static Color parseColor(String value) throws PropertyParseException {
        value = _stripWhitespace(value);
        if ((value == null) || (value.length() == 0)) return null;
        value = value.toLowerCase();
        Color color = (Color) ArrayMap.get(_NAMED_COLORS, value);
        if (color != null) return color;
        if (value.charAt(0) == '#') {
            int length = value.length();
            if (length == 7) {
                int rgb = 0;
                try {
                    rgb = Integer.parseInt(value.substring(1), 16);
                } catch (NumberFormatException e) {
                    throw new PropertyParseException(_INVALID_COLOR + value);
                }
                return _getSharedColor(rgb);
            } else if (length == 4) {
                int r = 0;
                int g = 0;
                int b = 0;
                try {
                    r = Integer.parseInt(value.substring(1, 2), 16);
                    g = Integer.parseInt(value.substring(2, 3), 16);
                    b = Integer.parseInt(value.substring(3, 4), 16);
                } catch (NumberFormatException e) {
                    throw new PropertyParseException(_INVALID_COLOR + value);
                }
                int rgb = (((r << 20) & 0xf00000) | ((r << 16) & 0x0f0000) | ((g << 12) & 0x00f000) | ((g << 8) & 0x000f00) | ((b << 4) & 0x0000f0) | (b & 0x00000f));
                return _getSharedColor(rgb);
            } else {
                throw new PropertyParseException(_INVALID_COLOR + value);
            }
        }
        if (value.startsWith("rgb")) {
            int startIndex = value.indexOf('(');
            int endIndex = value.indexOf(')');
            if ((startIndex == -1) || (endIndex == -1) || (endIndex <= startIndex)) {
                throw new PropertyParseException(_INVALID_COLOR + value);
            }
            StringTokenizer tokens = new StringTokenizer(value.substring(startIndex + 1, endIndex), " \t,");
            String redToken = null;
            String blueToken = null;
            String greenToken = null;
            try {
                redToken = tokens.nextToken();
                greenToken = tokens.nextToken();
                blueToken = tokens.nextToken();
            } catch (NoSuchElementException e) {
                throw new PropertyParseException(_INVALID_COLOR + value);
            }
            int red = _parseColorComponent(value, redToken);
            int green = _parseColorComponent(value, greenToken);
            int blue = _parseColorComponent(value, blueToken);
            return _getSharedColor(red, green, blue);
        }
        if (ArrayMap.get(_SYSTEM_COLORS, value) != null) return null;
        throw new PropertyParseException(_INVALID_COLOR + value);
    }

    /**
   * Parses a CSS font family value into a list of font family names.
   *
   * @param value A CSS font family value.
   * @return The list of font family names present in the font family property
   * @throws PropertyParseException Thrown if the specified font family
   *   value can not be parsed.
   */
    public static String[] parseFontFamilies(String value) throws PropertyParseException {
        if ((value == null) || (value.length() == 0)) return null;
        Vector<String> v = new Vector<String>();
        StringTokenizer tokens = new StringTokenizer(value, ",\"");
        while (tokens.hasMoreTokens()) {
            String family = _stripWhitespace(tokens.nextToken());
            if ((family != null) && (family.length() > 0)) v.addElement(family);
        }
        String[] families = new String[v.size()];
        v.copyInto(families);
        return families;
    }

    /**
   * Parses a CSS font size.
   *
   * @param value A CSS font size value.  At the moment, all font
   *   sizes must be specified in points (eg. "12pt").
   * @return An Integer font size suitable for use as an AWT Font size
   * @throws PropertyParseException Thrown if the specified font size
   *   value can not be parsed.
   */
    public static Integer parseFontSize(String value) throws PropertyParseException {
        value = _stripWhitespace(value);
        if ((value == null) || (value.length() == 0)) return null;
        value = value.toLowerCase();
        Integer fontSize = (Integer) ArrayMap.get(_NAMED_FONTS_SIZES, value);
        if (fontSize != null) return fontSize;
        if (_isLength(value)) return parseLength(value);
        if (_isPercentage(value)) return _parsePercentage(value);
        throw new PropertyParseException(_INVALID_FONT_SIZE + value);
    }

    /**
   * Parses a CSS font style
   *
   * @param value A CSS font style value.
   * @return An integer font style suitable for use as an AWT Font style
   * @throws PropertyParseException Thrown if the specified font style
   *   value can not be parsed.
   */
    public static Object parseFontStyle(String value) throws PropertyParseException {
        value = _stripWhitespace(value);
        if ((value == null) || (value.length() == 0)) return null;
        value = value.toLowerCase();
        if (_NORMAL_STYLE.equals(value)) return Style.PLAIN_FONT_STYLE;
        if (_ITALIC_STYLE.equals(value) || _OBLIQUE_STYLE.equals(value)) return Style.ITALIC_FONT_STYLE;
        throw new PropertyParseException(_INVALID_FONT_STYLE + value);
    }

    /**
   * Parses a CS font weight
   *
   * @param value A CSS font weight value.  At the moment, all font
   *   weights must be specified as either "bold" or "normal".
   * @return An integer font weight suitable for use as the weight
   *   component of an AWT Font style
   * @throws PropertyParseException Thrown if the specified font weight
   *   value can not be parsed.
   */
    public static Object parseFontWeight(String value) throws PropertyParseException {
        value = _stripWhitespace(value);
        if ((value == null) || (value.length() == 0)) return null;
        value = value.toLowerCase();
        if (_NORMAL_WEIGHT.equals(value) || _LIGHTER_WEIGHT.equals(value)) return Style.PLAIN_FONT_WEIGHT;
        if (_BOLD_WEIGHT.equals(value) || _BOLDER_WEIGHT.equals(value)) return Style.BOLD_FONT_WEIGHT;
        try {
            int weight = Integer.parseInt(value);
            if ((weight >= 100) && (weight <= 900) && ((weight % 100) == 0)) {
                if (weight >= 600) return Style.BOLD_FONT_WEIGHT;
                return Style.PLAIN_FONT_WEIGHT;
            }
        } catch (NumberFormatException e) {
            ;
        }
        throw new PropertyParseException(_INVALID_FONT_WEIGHT + value);
    }

    /**
   * Parses a CSS length value.
   *
   * @param value A CSS length value.
   * @return An Integer font size representing the length
   * @throws PropertyParseException Thrown if the specified
   *   value can not be parsed.
   */
    public static Integer parseLength(String value) throws PropertyParseException {
        value = _stripWhitespace(value);
        if (_isLength(value)) return _parseLength(value);
        throw new PropertyParseException(_INVALID_LENGTH + value);
    }

    /**
   * Converts the specified Color to a valid CSS color value.
   */
    public static String getColorValue(Color color) {
        StringBuffer buffer = new StringBuffer(7);
        buffer.append('#');
        buffer.append(_getHexColorComponent(color.getRed()));
        buffer.append(_getHexColorComponent(color.getGreen()));
        buffer.append(_getHexColorComponent(color.getBlue()));
        return buffer.toString();
    }

    private static String _getHexColorComponent(int colorComponent) {
        String hex = Integer.toString(colorComponent, 16);
        if (hex.length() == 1) hex = "0" + hex;
        return hex;
    }

    private static int _parseColorComponent(String value, String comp) throws PropertyParseException {
        if ((comp == null) || (comp.length() == 0)) return 0;
        int col = 0;
        if (comp.endsWith("%")) {
            double percent = 0;
            try {
                percent = Double.parseDouble(comp.substring(0, comp.length() - 1));
            } catch (NumberFormatException e) {
                throw new PropertyParseException(_INVALID_COLOR + value);
            }
            col = (int) ((percent / 100.0) * 255);
        } else {
            try {
                col = Integer.parseInt(comp);
            } catch (NumberFormatException e) {
                throw new PropertyParseException(_INVALID_COLOR + value);
            }
        }
        if (col < 0) return 0;
        return (col > 255) ? 255 : col;
    }

    private static boolean _isLength(String value) {
        assert (value != null);
        return (value.endsWith("in") || value.endsWith("cm") || value.endsWith("mm") || value.endsWith("pt") || value.endsWith("pc") || value.endsWith("em") || value.endsWith("ex") || value.endsWith("px"));
    }

    private static boolean _isPercentage(String value) {
        assert (value != null);
        return value.endsWith("%");
    }

    private static Integer _parseLength(String value) throws PropertyParseException {
        assert (_isLength(value));
        double size = 0;
        try {
            size = Double.parseDouble(value.substring(0, value.length() - 2));
        } catch (NumberFormatException e) {
            throw new PropertyParseException(_INVALID_LENGTH + value);
        }
        int points = 0;
        if (value.endsWith("in")) {
            points = (int) (72 * size);
        } else if (value.endsWith("cm")) {
            points = (int) ((72 * size) / 2.54);
        } else if (value.endsWith("mm")) {
            points = (int) ((72 * size) / 25.4);
        } else if (value.endsWith("pt")) {
            points = (int) size;
        } else if (value.endsWith("pc")) {
            points = (int) (12 * size);
        } else if (value.endsWith("em")) {
            points = (int) (12 * size);
        } else if (value.endsWith("ex")) {
            points = (int) (6 * size);
        } else if (value.endsWith("px")) {
            points = (int) size;
        } else {
            throw new PropertyParseException(_INVALID_LENGTH + value);
        }
        return points;
    }

    private static Integer _parsePercentage(String value) throws PropertyParseException {
        assert (_isPercentage(value));
        double percent = 0;
        try {
            percent = Double.parseDouble(value.substring(0, value.length() - 1));
        } catch (NumberFormatException e) {
            throw new PropertyParseException(_INVALID_PERCENTAGE + value);
        }
        return (int) ((percent / 100.0) * 12);
    }

    private static Color _getSharedColor(int rgb) {
        Color sharedColor = _sColorCache.get(Integer.valueOf(rgb));
        if (sharedColor == null) {
            sharedColor = new Color(rgb);
            _sColorCache.put(Integer.valueOf(rgb), sharedColor);
        }
        return sharedColor;
    }

    private static Color _getSharedColor(int r, int g, int b) {
        return _getSharedColor(((r << 16) & 0xff0000) | ((g << 8) & 0x00ff00) | (b & 0x0000ff));
    }

    private static String _stripWhitespace(String str) {
        if (str == null) return null;
        int length = str.length();
        int startIndex = 0;
        while (startIndex < length) {
            if (Character.isWhitespace(str.charAt(startIndex))) startIndex++; else break;
        }
        int endIndex = length;
        while (endIndex > 0) {
            if (Character.isWhitespace(str.charAt(endIndex - 1))) endIndex--; else break;
        }
        if ((startIndex == 0) && (endIndex == length)) return str;
        if (endIndex <= startIndex) return null;
        return str.substring(startIndex, endIndex);
    }

    /**
   * Resolve the uri that will be output to the generated CSS file
   * @param styleSheetName - the name of the Skin's stylesheet. We use this in any warning 
   * messages.
   * @param baseURI - absolute base URI pointing to the directory 
   * which contains the skin style sheet.
   * @param url - the url is the CSS property value that contains url(). For example, 
   * "url('/skins/purple/abc.png')"
   * @return The resolved uri. It will be run through the externalContext.encodeResourceURL
   * method.
   */
    private static String _resolveURL(String styleSheetName, String baseURI, String url) {
        int endIndex = -1;
        int index = url.indexOf("url(");
        StringBuilder builder = new StringBuilder();
        while (index >= 0) {
            builder.append(url, endIndex + 1, index);
            endIndex = url.indexOf(')', index + 3);
            String uri = url.substring(index + 4, endIndex);
            int uriLength = uri.length();
            if (uriLength > 0) {
                if ((uri.charAt(0) == '\'' && uri.charAt(uriLength - 1) == '\'') || (uri.charAt(0) == '"' && uri.charAt(uriLength - 1) == '"')) {
                    uri = uri.substring(1, uriLength - 1);
                    uriLength = uriLength - 2;
                }
            }
            if (uriLength == 0) {
                _LOG.warning("EMPTY_URL_IN_STYLE_SHEET", styleSheetName);
            }
            builder.append("url(");
            String resolvedURI = _resolveCSSURI(styleSheetName, baseURI, uri);
            builder.append(resolvedURI);
            builder.append(')');
            index = url.indexOf("url(", endIndex);
        }
        builder.append(url, endIndex + 1, url.length());
        return builder.toString();
    }

    /**
   * Resolve the uri that will be output to the generated CSS file
   * @param styleSheetName - the name of the Skin's stylesheet. We use this in any warning 
   * messages.
   * @param baseURI - absolute base URI pointing to the directory 
   * which contains the skin style sheet.
   * @param uri - a uri.
   * @return The resolved uri. It will be run through the externalContext.encodeResourceURL
   * method.
   */
    private static String _resolveCSSURI(String styleSheetName, String baseURI, String uri) {
        String resolvedURI = uri;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        assert (facesContext != null);
        ExternalContext externalContext = facesContext.getExternalContext();
        if (uri.charAt(0) == '/') {
            int uriLength = uri.length();
            if (uriLength > 1 && uri.charAt(1) == '/') {
                resolvedURI = uri.substring(1, uriLength);
            } else {
                String contextPath = externalContext.getRequestContextPath();
                assert contextPath.charAt(0) == '/';
                assert contextPath.charAt(contextPath.length() - 1) != '/';
                StringBuilder builder = new StringBuilder(contextPath.length() + uri.length());
                builder.append(contextPath);
                builder.append(uri);
                resolvedURI = builder.toString();
            }
        } else if (_isRelativeURI(uri)) {
            resolvedURI = getAbsoluteURIValue(styleSheetName, baseURI, uri);
        }
        return externalContext.encodeResourceURL(resolvedURI);
    }

    /**
   * Tests whether the specified uri is relative
   * @param uri - a uri
   * @return true if the uri is a relative uri. That is, it doesn't start with '/' and
   * it doesn't have a ':' in the string.
   */
    private static boolean _isRelativeURI(String uri) {
        return ((uri.charAt(0) != '/') && (uri.indexOf(':') < 0));
    }

    /**
   * Determines if the specified value contains a CSS url. The URLs are
   * detected by finding usage of url() function.
   * 
   * @param value
   * 
   * @return <code>true</code> if the specified value contains an URL, 
   *         <code>false</code> otherwise.
   */
    private static boolean _containsURL(String value) {
        if (value == null) {
            return false;
        }
        return value.indexOf("url(") >= 0;
    }

    private static final String _NORMAL_STYLE = "normal";

    private static final String _ITALIC_STYLE = "italic";

    private static final String _OBLIQUE_STYLE = "oblique";

    private static final String _NORMAL_WEIGHT = "normal";

    private static final String _BOLD_WEIGHT = "bold";

    private static final String _BOLDER_WEIGHT = "bolder";

    private static final String _LIGHTER_WEIGHT = "lighter";

    private static final String _INVALID_COLOR = "Invalid color: ";

    private static final String _INVALID_FONT_SIZE = "Invalid font size: ";

    private static final String _INVALID_FONT_STYLE = "Invalid font style: ";

    private static final String _INVALID_FONT_WEIGHT = "Invalid font weight: ";

    private static final String _INVALID_LENGTH = "Invalid length: ";

    private static final String _INVALID_PERCENTAGE = "Invalid percentage: ";

    private static final Map<Integer, Color> _sColorCache = Collections.synchronizedMap(new LRUCache<Integer, Color>(50));

    private static final Object[] _NAMED_COLORS = new Object[] { "black", _getSharedColor(0x000000), "white", _getSharedColor(0xffffff), "gray", _getSharedColor(0x808080), "red", _getSharedColor(0xff0000), "green", _getSharedColor(0x008000), "blue", _getSharedColor(0x0000ff), "yellow", _getSharedColor(0xffff00), "aqua", _getSharedColor(0x00ffff), "fuchsia", _getSharedColor(0xff00ff), "lime", _getSharedColor(0x00ff00), "maroon", _getSharedColor(0x800000), "navy", _getSharedColor(0x000080), "olive", _getSharedColor(0x808000), "purple", _getSharedColor(0x800080), "silver", _getSharedColor(0xc0c0c0), "teal", _getSharedColor(0x008080) };

    private static final Object[] _SYSTEM_COLORS = new Object[] { "activeborder", Boolean.TRUE, "activecaption", Boolean.TRUE, "appworkspace", Boolean.TRUE, "background", Boolean.TRUE, "buttonface", Boolean.TRUE, "buttonhighlight", Boolean.TRUE, "buttonshadow", Boolean.TRUE, "buttontext", Boolean.TRUE, "captiontext", Boolean.TRUE, "graytext", Boolean.TRUE, "highlight", Boolean.TRUE, "highlighttext", Boolean.TRUE, "inactiveborder", Boolean.TRUE, "inactivecaption", Boolean.TRUE, "inactivecaptiontext", Boolean.TRUE, "infobackground", Boolean.TRUE, "infotext", Boolean.TRUE, "menu", Boolean.TRUE, "menutext", Boolean.TRUE, "scrollbar", Boolean.TRUE, "threeddarkshadow", Boolean.TRUE, "threedface", Boolean.TRUE, "threedhighlight", Boolean.TRUE, "threedlightshadow", Boolean.TRUE, "threedshadow", Boolean.TRUE, "window", Boolean.TRUE, "windowframe", Boolean.TRUE, "windowtext", Boolean.TRUE };

    private static final Object[] _NAMED_FONTS_SIZES = new Object[] { "xx-small", 8, "x-small", 9, "small", 10, "medium", 12, "large", 14, "x-large", 16, "xx-large", 18, "smaller", 10, "larger", 14 };

    private static final Set<String> _URI_PROPERTIES = new HashSet<String>();

    static {
        _URI_PROPERTIES.add("background-image");
        _URI_PROPERTIES.add("cue-after");
        _URI_PROPERTIES.add("cue-before");
        _URI_PROPERTIES.add("list-style-image");
    }

    private static final Set<String> _SPECIAL_URI_VALUES = new HashSet<String>();

    static {
        _SPECIAL_URI_VALUES.add("none");
        _SPECIAL_URI_VALUES.add("inherit");
    }

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(CSSUtils.class);
}

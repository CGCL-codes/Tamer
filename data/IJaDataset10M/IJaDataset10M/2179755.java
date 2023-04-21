package org.apache.myfaces.trinidadinternal.style.xml.parse;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import org.apache.myfaces.trinidadinternal.agent.TrinidadAgent;
import org.apache.myfaces.trinidadinternal.share.xml.BaseNodeParser;
import org.apache.myfaces.trinidadinternal.share.xml.NodeParser;
import org.apache.myfaces.trinidadinternal.share.xml.ParseContext;
import org.apache.myfaces.trinidadinternal.share.xml.XMLUtils;
import org.apache.myfaces.trinidadinternal.style.StyleConstants;
import org.apache.myfaces.trinidadinternal.style.util.NameUtils;
import org.apache.myfaces.trinidadinternal.style.xml.XMLConstants;
import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;

/**
 * NodeParser for style sheet nodes
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/style/xml/parse/StyleSheetNodeParser.java#0 $) $Date: 10-nov-2005.18:58:47 $
 */
public class StyleSheetNodeParser extends BaseNodeParser implements XMLConstants, StyleConstants {

    /**
   * Implementation of NodeParser.startElement()
   */
    @Override
    public void startElement(ParseContext context, String namespaceURI, String localName, Attributes attrs) throws SAXParseException {
        _initLocales(attrs.getValue(LOCALES_ATTR));
        _direction = NameUtils.getDirection(attrs.getValue(DIRECTION_ATTR));
        _mode = NameUtils.getMode(attrs.getValue(MODE_ATTR));
        _initBrowsers(attrs.getValue(BROWSERS_ATTR));
        _initVersions(attrs.getValue(VERSIONS_ATTR));
        _initPlatforms(attrs.getValue(PLATFORMS_ATTR));
    }

    /**
   * Implementation of NodeParser.endElement()
   */
    @Override
    public Object endElement(ParseContext context, String namespaceURI, String localName) {
        StyleNode[] styles = null;
        if (_styles != null) {
            styles = new StyleNode[_styles.size()];
            _styles.copyInto(styles);
        }
        return new StyleSheetNode(styles, _locales, _direction, _browsers, _versions, _platforms, _mode);
    }

    /**
   * Implementation of NodeParser.startChildElement()
   */
    @Override
    public NodeParser startChildElement(ParseContext context, String namespaceURI, String localName, Attributes attrs) {
        if (localName.equals(STYLE_NAME)) return context.getParser(StyleNode.class, namespaceURI, localName);
        return null;
    }

    /**
   * Implementation of NodeParser.addCompletedChild().
   */
    @Override
    public void addCompletedChild(ParseContext context, String namespaceURI, String localName, Object child) {
        if (child instanceof StyleNode) {
            if (_styles == null) _styles = new Vector<StyleNode>();
            _styles.addElement((StyleNode) child);
        }
    }

    private Locale _getLocale(String str) {
        int length = str.length();
        if (length == 2) return new Locale(str, "");
        if ((length == 5) && (str.charAt(2) == '_')) return new Locale(str.substring(0, 2), str.substring(3, 5));
        return null;
    }

    private void _initLocales(String localeAttr) {
        if (localeAttr == null) return;
        Vector<Locale> locales = new Vector<Locale>();
        Iterator<String> tokens = _getTokens(localeAttr);
        while (tokens.hasNext()) {
            Locale locale = _getLocale(tokens.next());
            if (locale != null) locales.addElement(locale);
        }
        if (locales != null) {
            _locales = new Locale[locales.size()];
            locales.copyInto(_locales);
        }
    }

    private void _initBrowsers(String browserAttr) {
        Iterator<String> browsers = _getTokens(browserAttr);
        if (browsers == null) return;
        Vector<Integer> v = new Vector<Integer>();
        while (browsers.hasNext()) {
            int browser = NameUtils.getBrowser(browsers.next());
            if (browser != TrinidadAgent.APPLICATION_UNKNOWN) v.addElement(browser);
        }
        _browsers = _getIntegers(v);
    }

    private void _initVersions(String versionAttr) {
        Iterator<String> versions = _getTokens(versionAttr);
        if (versions == null) return;
        Vector<Integer> v = new Vector<Integer>();
        while (versions.hasNext()) {
            int version = 0;
            try {
                version = Integer.parseInt(versions.next());
            } catch (NumberFormatException e) {
                ;
            }
            if (version != 0) v.addElement(version);
        }
        _versions = _getIntegers(v);
    }

    private void _initPlatforms(String platformAttr) {
        Iterator<String> platforms = _getTokens(platformAttr);
        if (platforms == null) return;
        Vector<Integer> v = new Vector<Integer>();
        while (platforms.hasNext()) {
            String platformName = platforms.next();
            int platform = NameUtils.getPlatform(platformName);
            if ((platform == TrinidadAgent.OS_UNKNOWN) && "unix".equals(platformName)) platform = StyleSheetNode.__OS_UNIX;
            if (platform != TrinidadAgent.OS_UNKNOWN) v.addElement(platform);
        }
        _platforms = _getIntegers(v);
    }

    private int[] _getIntegers(Vector<Integer> v) {
        int count = v.size();
        if (count == 0) return null;
        int[] array = new int[count];
        for (int i = 0; i < count; i++) array[i] = v.elementAt(i).intValue();
        return array;
    }

    private Iterator<String> _getTokens(String attr) {
        if (attr == null) return null;
        return (Arrays.asList(XMLUtils.parseNameTokens(attr))).iterator();
    }

    private Vector<StyleNode> _styles;

    private Locale[] _locales;

    private int _direction;

    private int _mode;

    private int[] _browsers;

    private int[] _versions;

    private int[] _platforms;
}

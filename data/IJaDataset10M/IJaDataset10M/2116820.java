package hambo.xpres;

import hambo.app.util.DOMUtil;
import org.w3c.dom.*;

/**
 * ExternalNewsLink a link with a caption text. The link is set with an absolute url.
 */
public class ExternalNewsLink extends NewsLink {

    static final int ALL = 0;

    static final int HTML = 1;

    static final int WML = 2;

    static final int DEFAULT = ALL;

    String exturl;

    int supports;

    /**
     * Creates an ExternalNewsLink with element.
     */
    public ExternalNewsLink(Element elem) {
        super(elem);
        String supportsStr = elem.getAttribute("supports");
        if (supportsStr == null || supportsStr.trim().equals("")) {
            supports = DEFAULT;
        } else if (supportsStr.trim().toLowerCase().equals("all")) {
            supports = ALL;
        } else if (supportsStr.trim().toLowerCase().equals("html")) {
            supports = HTML;
        } else if (supportsStr.trim().toLowerCase().equals("wml")) {
            supports = WML;
        }
        NodeList nl = elem.getElementsByTagName("url");
        exturl = null;
        if (nl.getLength() > 0) exturl = DOMUtil.findFirstTextData(nl.item(0)); else System.err.println("ExternalNewsLink.ExternalNewsLink(): Unable to find url tag");
    }

    /**
     * Returns the link Url for HTML devices. 
     */
    public String getUrlHTML() {
        if (!supportsHTML()) return null;
        return exturl;
    }

    /**
     * Returns the link Url for WML devices. 
     */
    public String getUrlWML() {
        if (!supportsWML()) return null;
        return exturl;
    }

    /**
     * Returns the external url.
     */
    public String getUrl() {
        return exturl;
    }

    /**
     * Does this link suport Html devices.
     */
    public boolean supportsHtml() {
        return ((supports == HTML) || (supports == ALL));
    }

    /**
     * Does this link suport wml devices.
     */
    public boolean supportsWml() {
        return ((supports == WML) || (supports == ALL));
    }

    /**
     * Returns the default style for a ExternalNewsLink.
     */
    public String getDefaultStyle(String key) {
        return (String) PropertyHandler.getDefaultExternalNLStyleHM().get(key);
    }
}

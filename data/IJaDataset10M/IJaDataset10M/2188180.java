package net.wotonomy.web;

import java.util.Enumeration;
import net.wotonomy.foundation.NSDictionary;
import net.wotonomy.foundation.NSMutableDictionary;

/**
 * WOActiveImage renders a dynamically generated IMG tag, enclosed in a hyperlink.
 * Internally, it uses a WOImage and a WOHyperlink to do the actual work.
 *
 * The bindings are those of WOImage and WOHyperlink combined.
 *
 * @author ezamudio@nasoft.com
 * @author $Author: cgruber $
 * @version $Revision: 905 $
 */
public class WOActiveImage extends WODynamicElement {

    protected WOActiveImage() {
        super();
    }

    public WOActiveImage(String aName, NSDictionary aMap, WOElement template) {
        super(aName, aMap, template);
    }

    public void appendToResponse(WOResponse r, WOContext c) {
        NSMutableDictionary atribs = new NSMutableDictionary(5);
        if (associations.objectForKey("mimeType") != null) atribs.setObjectForKey(associations.objectForKey("mimeType"), "mimeType");
        if (associations.objectForKey("data") != null) atribs.setObjectForKey(associations.objectForKey("data"), "data");
        if (associations.objectForKey("src") != null) atribs.setObjectForKey(associations.objectForKey("src"), "src");
        if (associations.objectForKey("framework") != null) atribs.setObjectForKey(associations.objectForKey("framework"), "framework");
        if (associations.objectForKey("filename") != null) atribs.setObjectForKey(associations.objectForKey("filename"), "filename");
        if (associations.objectForKey("alt") != null) atribs.setObjectForKey(associations.objectForKey("alt"), "alt");
        if (associations.objectForKey("border") != null) atribs.setObjectForKey(associations.objectForKey("border"), "border");
        if (associations.objectForKey("width") != null) atribs.setObjectForKey(associations.objectForKey("width"), "width");
        if (associations.objectForKey("height") != null) atribs.setObjectForKey(associations.objectForKey("height"), "height");
        WODynamicElement img = new WOImage("WOImage_" + name, atribs, null);
        NSMutableDictionary uf = new NSMutableDictionary();
        Enumeration enumeration = associations.keyEnumerator();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            if (key.startsWith("?")) uf.setObjectForKey(associations.objectForKey(key), key);
        }
        createLink(img).appendToResponse(r, c);
    }

    public WOActionResults invokeAction(WORequest r, WOContext c) {
        return createLink(null).invokeAction(r, c);
    }

    protected WOHyperlink createLink(WOElement e) {
        NSMutableDictionary atribs = new NSMutableDictionary(5);
        if (associations.objectForKey("href") != null) atribs.setObjectForKey(associations.objectForKey("href"), "href");
        if (associations.objectForKey("pageName") != null) atribs.setObjectForKey(associations.objectForKey("pageName"), "pageName");
        if (associations.objectForKey("action") != null) atribs.setObjectForKey(associations.objectForKey("action"), "action");
        if (associations.objectForKey("directActionName") != null) atribs.setObjectForKey(associations.objectForKey("directActionName"), "directActionName");
        if (associations.objectForKey("actionClass") != null) atribs.setObjectForKey(associations.objectForKey("actionClass"), "actionClass");
        if (associations.objectForKey("target") != null) atribs.setObjectForKey(associations.objectForKey("target"), "target");
        return new WOHyperlink(name, atribs, e);
    }
}

package org.jfree.report.modules.factories.report.flow;

import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.ResourceException;
import org.jfree.util.Log;
import org.jfree.xmlns.parser.StringReadHandler;
import org.jfree.xmlns.parser.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Creation-Date: 12.04.2006, 14:53:29
 *
 * @author Thomas Morgner
 */
public class StyleSheetReadHandler extends StringReadHandler {

    private StyleSheet styleSheet;

    public StyleSheetReadHandler() {
    }

    /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws SAXException if there is a parsing error.
   */
    protected void startParsing(final Attributes attrs) throws SAXException {
        super.startParsing(attrs);
        String href = attrs.getValue(getUri(), "href");
        if (href != null) {
            final ResourceKey key = getRootHandler().getSource();
            final ResourceManager manager = getRootHandler().getResourceManager();
            try {
                final ResourceKey derivedKey = manager.deriveKey(key, href);
                final Resource resource = manager.create(derivedKey, null, StyleSheet.class);
                getRootHandler().getDependencyCollector().add(resource);
                styleSheet = (StyleSheet) resource.getResource();
            } catch (ResourceKeyCreationException e) {
                throw new ParseException("Unable to derive key for " + key + " and " + href, getLocator());
            } catch (ResourceCreationException e) {
                Log.warn("Unable to parse resource for " + key + " and " + href);
            } catch (ResourceLoadingException e) {
                Log.warn("Unable to load resource data for " + key + " and " + href);
            } catch (ResourceException e) {
                Log.warn("Unable to load resource for " + key + " and " + href);
            }
        }
    }

    /**
   * Done parsing.
   *
   * @throws SAXException       if there is a parsing error.
   */
    protected void doneParsing() throws SAXException {
        super.doneParsing();
        if (this.styleSheet != null) {
            return;
        }
        final String styleText = getResult();
        if (styleText.trim().length() == 0) {
            return;
        }
        try {
            final byte[] bytes = styleText.getBytes("UTF-8");
            final ResourceKey baseKey = getRootHandler().getSource();
            final ResourceManager resourceManager = getRootHandler().getResourceManager();
            final ResourceKey rawKey = resourceManager.createKey(bytes);
            final Resource resource = resourceManager.create(rawKey, baseKey, StyleSheet.class);
            this.styleSheet = (StyleSheet) resource.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Returns the object for this element.
   *
   * @return the object.
   */
    public Object getObject() {
        return styleSheet;
    }

    public StyleSheet getStyleSheet() {
        return styleSheet;
    }
}

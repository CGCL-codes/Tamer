package org.dbe.composer.wfengine.sdl;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import org.dbe.composer.wfengine.SdlException;
import org.dbe.composer.wfengine.util.SdlUtil;
import org.dbe.composer.wfengine.xml.XMLParserBase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * This provider allows you to load sdl files, which may be cached locally.
 */
public class SDLFactory implements ISDLFactory {

    /** Tag in SDL catalog which is the SDL entry item */
    private static String SDL_ENTRY_TAG = "sdlEntry";

    /** Tag in SDL catalog which is the URL for the SDL entry */
    private static String SDL_URL_TAG = "url";

    /** Tag in SDL catalog which is the classpath for the SDL entry */
    private static String SDL_CLASSPATH_TAG = "classpath";

    /** Tag in SDL catalog which is the namespace for the SDL entry */
    private static String SDL_NAMESPACE_TAG = "namespace";

    /** Hash map of url to classpath locations for sdl  */
    private static HashMap sUrl2Classpath = new HashMap();

    /** Hash map of url to classpath locations for sdl  */
    private static HashMap sNamespace2Url = new HashMap();

    /**
     * Creates a SDL provider, given the stream for the catalog. The catalog
     * contains a mapping of URI to resource location.
     * @param aCatalog input stream for catalog
     */
    public SDLFactory(InputStream aCatalog) {
        loadSDLCatalog(aCatalog);
    }

    public InputSource getSDLSource(String aSdlUrl) throws SdlException {
        InputStream stream = null;
        try {
            String classpath = (String) sUrl2Classpath.get(aSdlUrl.toLowerCase());
            if (classpath != null) {
                stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpath);
            } else {
                stream = new URL(aSdlUrl).openStream();
            }
        } catch (Exception ex) {
            throw new SdlException("Error loading sdl file: " + aSdlUrl, ex);
        }
        return new InputSource(stream);
    }

    /**
     * Returns the sdl source for a passed namespace or null if none.
     */
    public InputSource getSDLForNamespace(String aNamespace) throws SdlException {
        Object obj = sNamespace2Url.get(aNamespace);
        if (obj != null) {
            return getSDLSource(obj.toString());
        }
        return null;
    }

    public String getSDLLocationForNamespace(String aNamespace) throws SdlException {
        return (String) sNamespace2Url.get(aNamespace);
    }

    /**
     * Load the sdl local catalog file into our internal mapping.
     * @param aCatalog input stream for catalog of SDL mappings
     */
    private void loadSDLCatalog(InputStream aCatalog) {
        if (aCatalog == null) {
            return;
        }
        XMLParserBase parser = new XMLParserBase();
        parser.setValidating(false);
        parser.setNamespaceAware(true);
        try {
            Document doc = parser.loadSdlDocument(aCatalog, null);
            if (doc.getDocumentElement() != null) {
                for (Node node = doc.getDocumentElement().getFirstChild(); node != null; node = node.getNextSibling()) {
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        if (SDL_ENTRY_TAG.equals(node.getLocalName())) {
                            Element elem = (Element) node;
                            String namespace = elem.getAttribute(SDL_NAMESPACE_TAG);
                            String url = elem.getAttribute(SDL_URL_TAG).toLowerCase();
                            String classpath = elem.getAttribute(SDL_CLASSPATH_TAG);
                            sUrl2Classpath.put(url, classpath);
                            if (!SdlUtil.isNullOrEmpty(namespace)) {
                                sNamespace2Url.put(namespace, url);
                            }
                        }
                    }
                }
            }
        } catch (SdlException ex) {
            SdlException.logError(ex, "Error loading sdl catalog");
        }
    }
}

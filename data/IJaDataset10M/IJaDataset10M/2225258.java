package net.i2geo.constructions.xwikiapi;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import java.util.*;
import java.io.StringReader;
import java.net.URL;
import net.i2geo.api.server.ConstructionFactory;
import net.i2geo.constructions.xwikiapi.img.ImgConstructionFactory;

/** Singleton that loads and configures the {@link
 * @author Paul Libbrecht, DFKI GmbH, under the intergeo project, http://inter2geo.eu/
 */
public class VignetteManager {

    public static synchronized VignetteManager getInstance() {
        if (soleInstance == null) {
            soleInstance = new VignetteManager();
            soleInstance.init();
        }
        return soleInstance;
    }

    private static VignetteManager soleInstance = null;

    public void init() {
        try {
            Document doc = new SAXBuilder().build(VignetteManager.class.getResource("/constructions-config.xml"));
            Namespace ns = Namespace.getNamespace("cf", "http://i2geo.net/namespaces/construction-factories-config");
            baseURL = new URL("http://i2geo.net/xwiki/bin/view/Space/Name");
            if (doc.getRootElement().getAttribute("base-url") != null) baseURL = new URL(doc.getRootElement().getAttributeValue("base-url"));
            this.cache = new Cache(doc.getRootElement().getChild("cache", ns));
            pngConstructionFactory = new ImgConstructionFactory();
            Element pngConfig = new SAXBuilder().build(new StringReader("    <construction-factory class=\"net.i2geo.constructions.xwikiapi.img.ImgConstructionFactory\"\n" + "            icon=\"/static/types/png-type.png\"\n" + "            alt=\"$msg.get(&quot;resource-types.png.name&quot;)\">\n" + "        <properties>\n" + "        </properties>\n" + "    </construction-factory>")).getRootElement();
            VignetteHandler h = new VignetteHandler(pngConstructionFactory, this, "image/png", pngConfig);
            for (Element elt : (List<Element>) doc.getRootElement().getChildren("construction-factory", ns)) {
                try {
                    String className = elt.getAttributeValue("class");
                    Properties props = new Properties();
                    if (elt.getChildren("properties", ns) != null) {
                        props.load(new StringReader(elt.getChild("properties", ns).getText()));
                    }
                    ConstructionFactory factory = (ConstructionFactory) Class.forName(className).newInstance();
                    factory.configure(props);
                    Collection<String> mediaTypes = (Collection<String>) factory.getSupportedMimeTypes();
                    if (mediaTypes == null && "de.cinderella.api.i2geo.ServerAPI".equals(className)) mediaTypes = Arrays.asList("application/vnd.cinderella");
                    if (mediaTypes == null) {
                        System.err.println("Class \"" + className + "\" supports no mime-type, dropping!");
                        continue;
                    }
                    for (String mediaType : mediaTypes) {
                        VignetteHandler handler = new VignetteHandler(factory, this, mediaType, elt);
                        mediaTypesMap.put(mediaType, handler);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Failed instantiating construction-factory " + doc.getRootElement().getChildren("construction-factory", ns).indexOf(elt) + " of class \"" + elt.getAttributeValue("class") + "\".");
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("VignetteManager can't be configured.", e);
        }
    }

    private Map<String, VignetteHandler> mediaTypesMap = new HashMap<String, VignetteHandler>();

    private URL baseURL;

    private Cache cache;

    private ConstructionFactory pngConstructionFactory;

    public ConstructionFactory getPngConstructionFactory() {
        return pngConstructionFactory;
    }

    public VignetteHandler getHandlerFor(String mediaType) {
        return mediaTypesMap.get(mediaType);
    }

    URL getBaseURL() {
        return baseURL;
    }

    Cache getCache() {
        return cache;
    }
}

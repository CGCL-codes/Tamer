package info.port4.bbsp2.rendering.minml;

import uk.co.wilson.xml.MinML;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;
import java.util.Hashtable;
import java.io.Reader;
import java.io.IOException;
import info.port4.bbsp2.rendering.Renderers;

/**
 * 
 * @author <a href="mailto:harumanx@geocities.co.jp">MIYABE Tatsuhiko</a>
 * @version $Id: MinMLRenderers.java,v 1.2 2002/05/11 14:21:35 harumanx Exp $
 */
public class MinMLRenderers extends Renderers {

    private Hashtable srcLayers() {
        return srcLayers;
    }

    private MinML handler = new MinML() {

        private Hashtable roles;

        public void startElement(String name, AttributeList attr) {
            if (name.equals("for")) {
                this.roles = new Hashtable();
                MinMLRenderers.this.srcLayers().put(attr.getValue("layer"), this.roles);
            } else if (name.equals("renderer")) {
                String role = attr.getValue("role");
                String destLayer = attr.getValue("destLayer");
                if (destLayer == null) {
                    destLayer = "any";
                }
                String clazz = attr.getValue("class");
                Hashtable destLayers = (Hashtable) this.roles.get(role);
                if (destLayers == null) {
                    destLayers = new Hashtable();
                    this.roles.put(role, destLayers);
                }
                destLayers.put(destLayer, clazz);
            }
        }
    };

    public static void configure(Reader in) throws SAXException, IOException {
        if (instance == null) {
            instance = new MinMLRenderers();
            ((MinMLRenderers) instance).handler.parse(in);
        }
    }
}

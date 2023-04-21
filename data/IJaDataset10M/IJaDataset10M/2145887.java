package net.ontopia.topicmaps.xml;

import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.EntityResolver;
import net.ontopia.xml.*;

/**
 * INTERNAL: SAX entity resolver that only follows doctype
 * declarations referencing the ISO 13250 and XTM 1.0 DTDs using
 * public ids if the references actually point to something that can
 * be accessed. If not an empty input source is returned instead.</p>
 *
 * This behaviour is activated for references to system ids ending in
 * '.dtd' and to the following public ids:</p>
 *
 * <pre>
 *   "-//TopicMaps.Org//DTD XML Topic Map (XTM) 1.0//EN"
 *   "+//IDN ontopia.net//DTD Topic Map Interchange Format (Strict 1.0)//EN"
 * </pre>
 *
 * @since 2.0.3
 */
public class TopicMapDTDEntityResolver implements EntityResolver {

    public InputSource resolveEntity(String public_id, String system_id) {
        if (referencesDTD(public_id, system_id)) return attemptResolution(public_id, system_id);
        InputSource src = new InputSource(system_id);
        src.setPublicId(public_id);
        return src;
    }

    private boolean referencesDTD(String public_id, String system_id) {
        return ((public_id != null && (public_id.equals("-//TopicMaps.Org//DTD XML Topic Map (XTM) 1.0//EN") || public_id.equals("+//IDN ontopia.net//DTD Topic Map Interchange Format (Strict 1.0)//EN"))) || (system_id != null && system_id.toLowerCase().endsWith(".dtd")));
    }

    private InputSource attemptResolution(String public_id, String system_id) {
        if (system_id != null && !system_id.endsWith("/.")) {
            try {
                URL url = new URL(system_id);
                URLConnection conn = url.openConnection();
                conn.connect();
                InputStream stream = conn.getInputStream();
                InputSource src = new InputSource(system_id);
                src.setPublicId(public_id);
                src.setByteStream(stream);
                return src;
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } catch (java.security.AccessControlException e) {
            }
        }
        return new InputSource(new java.io.StringReader(""));
    }
}

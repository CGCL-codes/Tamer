package org.allcolor.ywt.pipe;

import org.allcolor.xml.parser.CShaniDomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;

/**
 * 
DOCUMENT ME!
 *
 * @author Quentin Anciaux
 * @version 0.1.0
 */
public class CPipeTransformerFactory {

    /** DOCUMENT ME! */
    private static CPipeTransformerFactory handle = null;

    /** DOCUMENT ME! */
    private static final Object LOCK = new Object();

    /** DOCUMENT ME! */
    private final Map<String, IPipeTransformer> transformerMap = new HashMap<String, IPipeTransformer>();

    /** DOCUMENT ME! */
    private final ServletContext context;

    /** DOCUMENT ME! */
    private long lastModified = -1;

    /**
   * Creates a new CPipeTransformerFactory object.
   * 
   * @param context
   *            DOCUMENT ME!
   */
    CPipeTransformerFactory(final ServletContext context) {
        this.context = context;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param name DOCUMENT ME!
	 * @param context DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public static IPipeTransformer getTransformer(final String name, final ServletContext context) {
        synchronized (CPipeTransformerFactory.LOCK) {
            if (CPipeTransformerFactory.handle == null) {
                CPipeTransformerFactory.handle = new CPipeTransformerFactory(context);
            }
            CPipeTransformerFactory.handle.loadConfigFile();
            return CPipeTransformerFactory.handle.transformerMap.get(name);
        }
    }

    /**
	 * DOCUMENT ME!
	 */
    private void loadConfigFile() {
        try {
            final URL url = this.context.getResource("/WEB-INF/config/transform.conf.xml");
            final URLConnection uc = url.openConnection();
            final InputStream in = uc.getInputStream();
            final long lastModified = uc.getLastModified();
            in.close();
            if (lastModified != this.lastModified) {
                this.lastModified = lastModified;
                final CShaniDomParser parser = new CShaniDomParser();
                final Document doc = parser.parse(url);
                final NodeList nl = doc.getElementsByTagNameNS("http://www.allcolor.org/xmlns/transform", "transformer");
                synchronized (this.transformerMap) {
                    this.transformerMap.clear();
                }
                for (int i = 0; i < nl.getLength(); i++) {
                    final Element eTransformer = (Element) nl.item(i);
                    try {
                        final IPipeTransformer transformer = (IPipeTransformer) this.getClass().getClassLoader().loadClass(eTransformer.getAttribute("class")).newInstance();
                        final String name = eTransformer.getAttribute("name");
                        synchronized (this.transformerMap) {
                            this.transformerMap.put(name, transformer);
                        }
                    } catch (final Exception ignore) {
                        ;
                    }
                }
            }
        } catch (final Exception ignore) {
            ;
        }
    }
}

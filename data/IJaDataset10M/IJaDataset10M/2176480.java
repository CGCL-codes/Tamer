package org.java.plugin.registry.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.util.IoUtil;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @version $Id: ManifestParser.java,v 1.4 2007/03/03 17:16:26 ddimon Exp $
 */
final class ManifestParser {

    static Log log = LogFactory.getLog(ManifestParser.class);

    static final String PLUGIN_DTD_1_0 = loadPluginDtd("1_0");

    private static String loadPluginDtd(final String version) {
        try {
            Reader in = new InputStreamReader(PluginRegistryImpl.class.getResourceAsStream("plugin_" + version + ".dtd"), "UTF-8");
            try {
                StringBuilder sBuf = new StringBuilder();
                char[] cBuf = new char[64];
                int read;
                while ((read = in.read(cBuf)) != -1) {
                    sBuf.append(cBuf, 0, read);
                }
                return sBuf.toString();
            } finally {
                in.close();
            }
        } catch (IOException ioe) {
            log.error("can't read plug-in DTD file of version " + version, ioe);
        }
        return null;
    }

    private static EntityResolver getDtdEntityResolver() {
        return new EntityResolver() {

            public InputSource resolveEntity(final String publicId, final String systemId) {
                if (publicId == null) {
                    log.debug("can't resolve entity, public ID is NULL, systemId=" + systemId);
                    return null;
                }
                if (PLUGIN_DTD_1_0 == null) {
                    return null;
                }
                if (publicId.equals("-//JPF//Java Plug-in Manifest 1.0") || publicId.equals("-//JPF//Java Plug-in Manifest 0.7") || publicId.equals("-//JPF//Java Plug-in Manifest 0.6") || publicId.equals("-//JPF//Java Plug-in Manifest 0.5") || publicId.equals("-//JPF//Java Plug-in Manifest 0.4") || publicId.equals("-//JPF//Java Plug-in Manifest 0.3") || publicId.equals("-//JPF//Java Plug-in Manifest 0.2")) {
                    if (log.isDebugEnabled()) {
                        log.debug("entity resolved to plug-in manifest DTD, publicId=" + publicId + ", systemId=" + systemId);
                    }
                    return new InputSource(new StringReader(PLUGIN_DTD_1_0));
                }
                if (log.isDebugEnabled()) {
                    log.debug("entity not resolved, publicId=" + publicId + ", systemId=" + systemId);
                }
                return null;
            }
        };
    }

    private final SAXParserFactory parserFactory;

    private final EntityResolver entityResolver;

    ManifestParser(final boolean isValidating) {
        parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(isValidating);
        entityResolver = isValidating ? getDtdEntityResolver() : null;
        log.info("got SAX parser factory - " + parserFactory);
    }

    ModelPluginManifest parseManifest(final URL url) throws ParserConfigurationException, SAXException, IOException {
        ManifestHandler handler = new ManifestHandler(entityResolver);
        InputStream strm = IoUtil.getResourceInputStream(url);
        try {
            parserFactory.newSAXParser().parse(strm, handler);
        } finally {
            strm.close();
        }
        ModelPluginManifest result = handler.getResult();
        result.setLocation(url);
        return result;
    }

    ModelManifestInfo parseManifestInfo(final URL url) throws ParserConfigurationException, SAXException, IOException {
        ManifestInfoHandler handler = new ManifestInfoHandler(entityResolver);
        InputStream strm = IoUtil.getResourceInputStream(url);
        try {
            parserFactory.newSAXParser().parse(strm, handler);
        } finally {
            strm.close();
        }
        return handler.getResult();
    }
}

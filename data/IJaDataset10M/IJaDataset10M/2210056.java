package net.sf.xsdutils.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import net.sf.xsdutils.AbstractXSDTool;

/**
 * XML namespace normalizer.
 * 
 * @author Rustam Abdullaev
 */
public class NSNormalizer extends AbstractXSDTool {

    private static final String COMMON_PREFIXES_PROPERTIES = "common-prefixes.properties";

    private final Properties commonNSToPrefixMap;

    private final Properties commonPrefixToNSMap;

    public NSNormalizer() {
        commonNSToPrefixMap = new Properties();
        commonPrefixToNSMap = new Properties();
        try {
            InputStream in = getClass().getResourceAsStream(COMMON_PREFIXES_PROPERTIES);
            if (in == null) {
                throw new IOException("File not found: " + COMMON_PREFIXES_PROPERTIES);
            }
            try {
                commonPrefixToNSMap.load(in);
            } finally {
                in.close();
            }
            for (Map.Entry<?, ?> entry : commonPrefixToNSMap.entrySet()) {
                String ns = (String) entry.getValue();
                String prefix = (String) entry.getKey();
                commonNSToPrefixMap.setProperty(ns, prefix);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("unable to read " + COMMON_PREFIXES_PROPERTIES, e);
        }
    }

    public void normalizeNS(URL sourceXML, File targetXML) {
    }
}

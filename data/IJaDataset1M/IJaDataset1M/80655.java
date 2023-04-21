package net.sf.yaxdiff.output;

import org.apache.commons.lang.StringUtils;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author Ramon Nogueira (ramon döt nogueira at g maíl döt cöm)
 *
 */
public class XMLUtil {

    public static final String DIFF_URI = "http://yaxdiff.sf.net/ns/differences";

    public static final String DIFF_PREFIX = "diff";

    public static final Attributes NO_ATTS = new AttributesImpl();

    public static final String DEFAULT_URI = StringUtils.EMPTY;

    public static final String CDATA_STR = "CDATA";

    public static void startDiffElement(ContentHandler handler, String name, Attributes atts) throws SAXException {
        String qname = DIFF_PREFIX.length() == 0 ? name : DIFF_PREFIX + ":" + name;
        handler.startElement(DIFF_URI, name, qname, atts);
    }

    public static void endDiffElement(ContentHandler handler, String name) throws SAXException {
        String qname = DIFF_PREFIX.length() == 0 ? name : DIFF_PREFIX + ":" + name;
        handler.endElement(DIFF_URI, name, qname);
    }

    public static Attributes attributesFromMap(Map<String, String> map, String uri) {
        AttributesImpl atts = new AttributesImpl();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            atts.addAttribute(uri, entry.getKey(), entry.getKey(), CDATA_STR, entry.getValue());
        }
        return atts;
    }
}

package org.obe.event;

import org.obe.client.api.model.MIMETypes;
import org.obe.spi.service.ServiceManager;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles MIME <tag>text/*</tag> content.
 *
 * @author Adrian Price
 */
public class MIMETextHandler extends AbstractContentHandler {

    private final Map _patterns = new HashMap();

    public MIMETextHandler() {
    }

    public void init(ServiceManager svcMgr, Properties props) {
        init(svcMgr);
        for (Iterator iter = props.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            String contentType = (String) entry.getKey();
            String pattern = (String) entry.getValue();
            _patterns.put(Pattern.compile(pattern), contentType);
        }
    }

    public String getContentType(Object data) {
        String s = (String) data;
        for (Iterator iter = _patterns.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            Pattern p = (Pattern) entry.getKey();
            Matcher matcher = p.matcher(s);
            if (matcher.matches()) return (String) entry.getValue();
        }
        return MIMETypes.TEXT_PLAIN;
    }

    protected String getSchema(Object data, String contentType) {
        String schema = "";
        if (contentType.equals(MIMETypes.XML)) {
            Document document = (Document) data;
            DocumentType docType = document.getDoctype();
            Element docElem = document.getDocumentElement();
            String nodeName = docElem.getLocalName();
            String namespace;
            if (docType != null) {
                namespace = docType.getPublicId();
                if (namespace == null) namespace = docType.getSystemId();
            } else {
                namespace = docElem.getNamespaceURI();
            }
            schema = namespace != null ? '{' + namespace + '}' + nodeName : nodeName;
        }
        return schema;
    }
}

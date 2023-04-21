package ORG.oclc.os.SRW;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author levan
 */
public class ElementParser implements Enumeration {

    static Log log = LogFactory.getLog(ElementParser.class);

    char[] frag;

    int offset = 0;

    String attributes = null;

    /** Creates a new instance of ElementParser */
    public ElementParser(String xmlFragment) {
        if (log.isDebugEnabled()) log.debug("parsing: " + xmlFragment);
        frag = xmlFragment.toCharArray();
        while (offset < frag.length && frag[offset] != '<') offset++;
    }

    private int find(String elementName, int offset) {
        int i;
        if (log.isDebugEnabled()) log.debug("looking for \"" + elementName + "\" in \"" + new String(frag, offset, frag.length - offset) + "\"");
        for (; offset < frag.length; offset++) if (frag[offset] == '<' && frag[offset + 1] == '/') break;
        for (i = 0; i < elementName.length() && frag[i + offset + 2] == elementName.charAt(i); i++) ;
        if (i < elementName.length() || frag[i + offset + 2] != '>') return (find(elementName, offset + 2));
        return offset;
    }

    public String getAttributes() {
        return attributes;
    }

    public boolean hasMoreElements() {
        if (offset < frag.length && frag[offset] == '<') {
            if (log.isDebugEnabled()) log.debug("\"" + new String(frag, offset, frag.length - offset) + "\"");
            return true;
        }
        return false;
    }

    public Object nextElement() {
        if (!hasMoreElements()) throw new NoSuchElementException();
        int end, start = offset + 1;
        for (end = start + 1; end < frag.length && frag[end] != ' ' && frag[end] != '/' && frag[end] != '>'; end++) ;
        String elementName = new String(frag, start, end - start);
        if (log.isDebugEnabled()) log.debug("elementName=" + elementName);
        for (start = end; start < frag.length && frag[start] != '>'; start++) ;
        if (start != end) {
            attributes = new String(frag, end + 1, start - end - 1);
            if (attributes.endsWith("/")) if (attributes.length() == 1) attributes = null; else attributes = attributes.substring(0, attributes.length() - 1);
        } else attributes = null;
        if (log.isDebugEnabled()) log.debug("attributes=" + attributes);
        if (frag[start] == '/') start++;
        start++;
        String value = "";
        if (start < frag.length && frag[start] != '<') {
            end = find(elementName, start);
            for (offset = end + 1; offset < frag.length && frag[offset] != '<'; offset++) ;
            value = new String(frag, start, end - start);
            if (log.isDebugEnabled()) log.debug("value=" + value);
        } else offset = start;
        return new NameValuePair(elementName, value);
    }
}

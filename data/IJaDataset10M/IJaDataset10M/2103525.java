package net.sf.lavabeans.read;

import java.util.Stack;
import org.apache.commons.lang.StringUtils;

public class DocumentPathImpl implements DocumentPath {

    private final Stack elements = new Stack();

    private final Stack locations = new Stack();

    private String attributeName;

    private String pathStr = null;

    public Object getLocation() {
        return locations.peek();
    }

    public int getElementCount() {
        return elements.size();
    }

    public Element peekElement() {
        return (Element) elements.get(elements.size() - 1);
    }

    public Element getElement(int depth) {
        return (Element) elements.get(depth);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
        clearCache();
    }

    public void clearAttributeName() {
        this.attributeName = null;
        clearCache();
    }

    public void pushElement(Element element, Object location) {
        elements.push(element);
        locations.push(location);
        clearCache();
    }

    public Element popElement() {
        locations.pop();
        clearCache();
        return (Element) elements.pop();
    }

    public boolean endsWith(String pathStr) {
        return pathStr().endsWith(pathStr);
    }

    public boolean contains(String pathStr) {
        return pathStr().indexOf(pathStr) != -1;
    }

    private synchronized void clearCache() {
        pathStr = null;
    }

    private synchronized String pathStr() {
        if (pathStr == null) {
            pathStr = ("/" + StringUtils.join(elements.iterator(), "/"));
        }
        return pathStr;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtils.join(elements.iterator(), "/"));
        if (attributeName != null) {
            sb.append("@").append(attributeName);
        }
        if (!locations.isEmpty()) {
            sb.append(" ").append(getLocation());
        }
        return sb.toString();
    }
}

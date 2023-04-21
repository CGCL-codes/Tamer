package annone.util.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import annone.util.AnnoneException;
import annone.util.Checks;
import annone.util.Text;

public class XmlParent extends XmlElement {

    @SuppressWarnings("rawtypes")
    private final ArrayList children;

    XmlParent() {
        this.children = new ArrayList<XmlElement>(0);
    }

    @SuppressWarnings("unchecked")
    public List<XmlElement> getChildren() {
        return Collections.unmodifiableList((ArrayList<XmlElement>) children);
    }

    @SuppressWarnings("unchecked")
    public List<XmlNode> getNodes() {
        for (int i = 0; i < children.size(); i++) if (!(children.get(i) instanceof XmlNode)) {
            List<XmlNode> nodes = new ArrayList<XmlNode>(i);
            for (int j = 0; j < children.size(); j++) {
                XmlElement element = (XmlElement) children.get(j);
                if (element instanceof XmlNode) nodes.add((XmlNode) element);
            }
            return Collections.unmodifiableList(nodes);
        }
        return Collections.unmodifiableList((ArrayList<XmlNode>) children);
    }

    @SuppressWarnings("unchecked")
    public List<XmlText> getTexts() {
        for (int i = 0; i < children.size(); i++) if (!(children.get(i) instanceof XmlText)) {
            List<XmlText> texts = new ArrayList<XmlText>(i);
            for (Object element : children) if (element instanceof XmlNode) texts.add((XmlText) element);
            return Collections.unmodifiableList(texts);
        }
        return Collections.unmodifiableList((ArrayList<XmlText>) children);
    }

    public String getText() {
        if (children.size() == 0) return ""; else if (children.size() == 1) {
            XmlElement first = (XmlElement) children.get(0);
            if (first instanceof XmlText) return ((XmlText) first).getText(); else return "";
        } else {
            StringBuilder b = new StringBuilder();
            for (Object element : children) if (element instanceof XmlText) b.append(((XmlText) element).getText());
            return b.toString();
        }
    }

    @SuppressWarnings("unchecked")
    public void addChild(XmlElement element) {
        Checks.notNull("element", element);
        children.add(element);
    }

    public void removeChild(XmlElement element) {
        Checks.notNull("element", element);
        children.remove(element);
    }

    public void clearChildren() {
        children.clear();
    }

    public List<XmlNode> getNodes(String path) {
        Checks.notNull("path", path);
        int i = path.indexOf('/');
        XmlNode node;
        if (i >= 0) {
            node = getNode(path.substring(0, i));
            if (node == null) return Collections.emptyList(); else return node.getNodes(path.substring(i + 1));
        } else return findNodes(path);
    }

    public XmlNode getNode(String path) {
        List<XmlNode> nodes = getNodes(path);
        if (nodes.isEmpty()) return null; else if (nodes.size() == 1) return nodes.get(0); else throw new AnnoneException(Text.get("Multiple nodes at ''{0}''.", path));
    }

    private List<XmlNode> findNodes(String name) {
        List<XmlNode> nodes = null;
        for (Object child : children) if ((child instanceof XmlNode) && ((XmlNode) child).getName().equals(name)) {
            if (nodes == null) nodes = new ArrayList<XmlNode>(1);
            nodes.add((XmlNode) child);
        }
        if (nodes != null) return Collections.unmodifiableList(nodes); else return Collections.emptyList();
    }

    @Override
    protected String toString(int level) {
        if (children.isEmpty()) return ""; else {
            StringBuilder b = new StringBuilder();
            for (Object child : children) b.append(((XmlElement) child).toString(level + 1));
            return b.toString();
        }
    }

    @Override
    public String toXml() {
        if (children.isEmpty()) return ""; else {
            StringBuilder b = new StringBuilder();
            for (Object child : children) b.append(((XmlElement) child).toXml());
            return b.toString();
        }
    }
}

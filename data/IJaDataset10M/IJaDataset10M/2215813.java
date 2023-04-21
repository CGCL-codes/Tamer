package ms.utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XEOList<T extends XmlElementObject> {

    private XmlFile source;

    private Element collectionRoot;

    private String elementTagName;

    private Class<? extends XmlElementObject> elementClass;

    private Constructor ctor;

    public XEOList(XmlFile source, Element collectionRoot, String elementTagName, Class<? extends XmlElementObject> cls) throws SecurityException, NoSuchMethodException {
        this.source = source;
        this.collectionRoot = collectionRoot;
        this.elementTagName = elementTagName;
        this.elementClass = cls;
        ctor = elementClass.getConstructor(Element.class);
    }

    public int size() {
        return collectionRoot.getElementsByTagName(elementTagName).getLength();
    }

    @SuppressWarnings("unchecked")
    public T add() {
        Element node = (Element) collectionRoot.appendChild(source.Root.getOwnerDocument().createElement(elementTagName));
        try {
            return (T) ctor.newInstance(node);
        } catch (Exception e) {
            return null;
        }
    }

    public T add(String id) {
        T e = add();
        e.setAttribute("id", id);
        return e;
    }

    public boolean remove(String id) {
        try {
            String arg = elementTagName + "[@id='" + id + "']";
            Element node = source.selectSingleNode(arg, collectionRoot);
            if (node != null) collectionRoot.removeChild(node);
            return node != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean remove(T object) {
        try {
            if (object != null && object.Node != null) collectionRoot.removeChild(object.Node);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> ToList() throws Exception {
        ArrayList<T> result = new ArrayList<T>();
        NodeList nodes = collectionRoot.getElementsByTagName(elementTagName);
        for (int i = 0; i < nodes.getLength(); i++) result.add((T) ctor.newInstance(nodes.item(i)));
        return result;
    }

    @SuppressWarnings("unchecked")
    public T find(String id) {
        try {
            Element node = source.selectSingleNode(String.format("%s[@id='%s']", elementTagName, id), collectionRoot);
            if (node != null) return (T) ctor.newInstance(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

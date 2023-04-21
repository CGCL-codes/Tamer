package org.qsari.effectopedia.base;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jdom.Element;
import org.jdom.Namespace;
import org.qsari.effectopedia.base.ids.ReferenceIDs;
import org.qsari.effectopedia.data.XMLFileDS;

public abstract class EffectopediaObjects<E extends EffectopediaObject> extends HashMap<Long, E> implements XMLImportable, XMLExportable, Comparable<EffectopediaObjects<?>> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
		 * Helper (not efficient) method for infrequent calls that provides sorted
		 * iterator for the map elements.
		 * 
		 * @return sorted iterator for the HasMap represented by this class.
		 */
    public Iterator<Map.Entry<Long, E>> getSortedIterator() {
        List<Map.Entry<Long, E>> list = new ArrayList<Map.Entry<Long, E>>();
        Iterator<Map.Entry<Long, E>> it = entrySet().iterator();
        while (it.hasNext()) list.add(it.next());
        class IDComparator implements Comparator<Map.Entry<Long, E>> {

            public int compare(Map.Entry<Long, E> o1, Map.Entry<Long, E> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        }
        Collections.sort(list, new IDComparator());
        return list.iterator();
    }

    public EffectopediaObjects() {
        super();
    }

    public abstract boolean addNew(EffectopediaObject parent, Class<E> objectClass);

    public void updateParenthood() {
        Iterator<Map.Entry<Long, E>> it = entrySet().iterator();
        while (it.hasNext()) it.next().getValue().updateParenthood();
    }

    public void updateExternalIDs() {
        Iterator<Map.Entry<Long, E>> it = getSortedIterator();
        while (it.hasNext()) it.next().getValue().updateExternalID();
    }

    public void putDefault(HashMap<Long, E> index) {
        Iterator<Map.Entry<Long, E>> it = index.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, E> e = it.next();
            EffectopediaObject eo = e.getValue();
            if ((eo != null) && (eo.isDefaultID())) put(e.getKey(), e.getValue());
        }
    }

    public void updateExternalIDToIDMap(HashMap<Long, Long> idMap) {
        Iterator<Map.Entry<Long, E>> it = entrySet().iterator();
        while (it.hasNext()) {
            E e = it.next().getValue();
            idMap.put(e.getExternalID(), e.getID());
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends EffectopediaObject> T[] getObjectArrayByIDs(ReferenceIDs<T> ids) {
        int count = ids.size();
        if (count == 0) return null; else {
            T[] objectsArray = (T[]) Array.newInstance(ids.getObjectClass(), count);
            for (int i = 0; i < count; i++) objectsArray[i] = (T) get(i);
            return objectsArray;
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromXMLElement(Element element, Namespace namespace) {
        if (element == null) return;
        Element e = element.getChild("items", namespace);
        if (e != null) {
            int count = Integer.parseInt(e.getAttributeValue("count", namespace));
            List children = e.getChildren();
            if ((count != 0) && (children != null) && (children.size() == count)) {
                Iterator<Element> iterator = children.iterator();
                while (iterator.hasNext()) {
                    Element child = (Element) iterator.next();
                    try {
                        Class cl = Class.forName(child.getName());
                        XMLFileDS.load(cl, null, child, namespace);
                    } catch (ClassNotFoundException cnfe) {
                        cnfe.printStackTrace();
                    }
                }
            }
        }
    }

    public Element storeToXMLElement(Element element, Namespace namespace, boolean visualAttributes) {
        Element e = new Element("items", namespace);
        e.setAttribute("count", Integer.toString(entrySet().size()), namespace);
        Iterator<Map.Entry<Long, E>> iterator = getSortedIterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, E> entry = (Map.Entry<Long, E>) iterator.next();
            E o = (E) entry.getValue();
            Element obj = new Element(o.getClass().getName(), namespace);
            o.storeToXMLElement(obj, namespace, false);
            e.addContent(obj);
        }
        element.addContent(e);
        return element;
    }

    public Element storeToXMLElement(HashMap<Long, EffectopediaObject> excludeIDs, Element element, Namespace namespace, boolean visualAttributes) {
        Element e = new Element("items", namespace);
        int cnt = 0;
        Iterator<Map.Entry<Long, E>> iterator = getSortedIterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, E> entry = (Map.Entry<Long, E>) iterator.next();
            if (excludeIDs.get(entry.getKey()) == null) {
                E o = (E) entry.getValue();
                Element obj = new Element(o.getClass().getName(), namespace);
                o.storeToXMLElement(obj, namespace, visualAttributes);
                o.getContainedIDs(excludeIDs);
                e.addContent(obj);
                cnt++;
            }
        }
        e.setAttribute("count", Integer.toString(cnt), namespace);
        element.addContent(e);
        return (cnt == 0) ? null : element;
    }

    public int hashCode() {
        return ID;
    }

    public int compareTo(EffectopediaObjects<?> o) {
        return ID - o.ID;
    }

    public Object[] get() {
        return values().toArray();
    }

    @Override
    public E put(Long key, E value) {
        return super.put(key, value);
    }

    public final int ID = effectopediaObjectsIDs++;

    public static int effectopediaObjectsIDs = 0;
}

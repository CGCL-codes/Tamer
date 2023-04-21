package org.base.apps.beans.converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.base.apps.beans.util.Kind;
import org.base.apps.xml.elem.Element;
import org.base.apps.xml.elem.ElementContext;
import org.base.apps.xml.elem.ElementFactory;
import org.base.apps.xml.elem.vis.ToXmlVisitor;

/**
 *
 * @author Kevan Simpson
 */
public class CollectionConverter extends AbstractItemConverter {

    public enum CollectionTag implements CharSequence {

        collection, item;

        /** @see java.lang.CharSequence#charAt(int) */
        public char charAt(int index) {
            return name().charAt(index);
        }

        /** @see java.lang.CharSequence#length() */
        public int length() {
            return name().length();
        }

        /** @see java.lang.CharSequence#subSequence(int, int) */
        public CharSequence subSequence(int start, int end) {
            return name().subSequence(start, end);
        }
    }

    /**
     */
    public CollectionConverter() {
        super(Collection.class);
    }

    /**
     * @param defaultValue
     */
    public CollectionConverter(Object defaultValue) {
        super(defaultValue);
    }

    /** @see org.apache.commons.beanutils.converters.AbstractConverter#convertToType(java.lang.Class, java.lang.Object) */
    @SuppressWarnings("rawtypes")
    @Override
    protected Object convertToType(Class type, Object value) throws Throwable {
        if (Collection.class.isAssignableFrom(type)) {
            String xml = ObjectUtils.toString(value);
            if (StringUtils.isNotBlank(xml)) {
                return parseCollection(xml);
            }
        }
        return null;
    }

    /** @see org.apache.commons.beanutils.converters.AbstractConverter#convertToString(java.lang.Object) */
    @SuppressWarnings("rawtypes")
    @Override
    protected String convertToString(Object value) throws Throwable {
        ToXmlVisitor xvis = new ToXmlVisitor();
        ElementFactory fac = ElementFactory.getInstance();
        if (value instanceof Collection) {
            Collection coll = (Collection) value;
            Element root = createTypedElement(Kind.collection, coll.getClass());
            ElementContext rctx = root.getContext();
            String uri = root.getTag().getNamespaceURI();
            for (Object obj : coll) {
                Element item = fac.createElement(CollectionTag.item);
                root.addChild(item);
                item.getTag().setNamespaceURI(uri);
                setText(item, obj, rctx);
            }
            return xvis.toXml(root);
        } else return super.convertToString(value);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Object parseCollection(String xml) throws Exception {
        if (StringUtils.isNotBlank(xml)) {
            Element collElem = parse(xml);
            if (collElem != null) {
                String type = StringUtils.difference(CollectionConverter.CONVERTER_BASE_NS_URI, collElem.getTag().getNamespaceURI());
                Class<Collection> impl = (Class<Collection>) ClassUtils.getClass(type);
                Collection coll = impl.newInstance();
                Map<String, Class<?>> nodeTypes = new HashMap<String, Class<?>>();
                ToXmlVisitor xvis = new ToXmlVisitor();
                for (Iterator<Element> iter = collElem.getElements(); iter.hasNext(); ) {
                    Element child = iter.next();
                    Object item = parseItem(child, xvis, nodeTypes);
                    if (item != null) {
                        coll.add(item);
                    }
                }
                return coll;
            }
        }
        return null;
    }

    protected Object parseItem(Element elem, ToXmlVisitor xvis, Map<String, Class<?>> nodeTypes) throws Exception {
        String prefix = elem.getTag().getPrefix();
        Class<?> nodeImpl = nodeTypes.get(prefix);
        if (nodeImpl == null) {
            nodeImpl = ClassUtils.getClass(elem.getContext().getNamespaceURI(prefix));
            nodeTypes.put(prefix, nodeImpl);
        }
        String itemXml = xvis.toXml(elem.getChild(0));
        Object item = delegateTypeConversion(actualType(elem, prefix, nodeImpl), itemXml);
        return item;
    }

    protected Class<?> actualType(Element node, String prefix, Class<?> defaultType) throws Exception {
        return (StringUtils.equals(prefix, node.getTag().getPrefix())) ? defaultType : ClassUtils.getClass(node.getTag().getNamespaceURI());
    }
}

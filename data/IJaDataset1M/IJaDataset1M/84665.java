package org.base.apps.beans.converter;

import java.io.StringReader;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.base.apps.xml.Parser;
import org.base.apps.xml.elem.Element;
import org.base.apps.xml.elem.ElementContext;
import org.base.apps.xml.elem.ElementFactory;
import org.base.apps.xml.elem.Tag;
import org.base.apps.xml.elem.vis.ToXmlVisitor;
import org.base.apps.xml.elem.vis.VisitableNode;
import org.base.apps.xml.stax.StAXElementParser;

/**
 * Base implentation of complex bean {@link Converter}.
 * 
 * @author Kevan Simpson
 */
public abstract class AbstractItemConverter extends BaseConverter {

    public static final String CONVERTER_BASE_NS_URI = Parser.BASE_NS_URI + "converter/";

    /**
     * @param type
     */
    public AbstractItemConverter(Class<?> type) {
        super(type);
    }

    /**
     * @param defaultValue
     */
    public AbstractItemConverter(Object defaultValue) {
        super(defaultValue);
    }

    protected Element createTypedElement(CharSequence tag, Class<?> type) throws Exception {
        String uri = CONVERTER_BASE_NS_URI + type.getName();
        Element elem = getElementFactory().createElement(new Tag(tag, uri));
        if (elem != null) {
            elem.getContext().addNamespaceURI(XMLConstants.DEFAULT_NS_PREFIX, uri);
        }
        return elem;
    }

    protected ElementFactory getElementFactory() {
        return ElementFactory.getInstance();
    }

    protected Element parse(String xml) throws Exception {
        StAXElementParser step = new StAXElementParser();
        return step.parse(new StreamSource(new StringReader(xml)));
    }

    protected Object parseItem(Element elem, Map<String, Class<?>> nodeTypes) throws Exception {
        String prefix = elem.getTag().getPrefix();
        Class<?> nodeImpl = nodeTypes.get(prefix);
        if (nodeImpl == null) {
            nodeImpl = ClassUtils.getClass(elem.getContext().getNamespaceURI(prefix));
            nodeTypes.put(prefix, nodeImpl);
        }
        String itemXml = toXml(elem.getChild(0));
        Object item = delegateTypeConversion(actualType(elem, prefix, nodeImpl), itemXml);
        return item;
    }

    protected Class<?> actualType(Element node, String prefix, Class<?> defaultType) throws Exception {
        return (StringUtils.equals(prefix, node.getTag().getPrefix())) ? defaultType : ClassUtils.getClass(node.getTag().getNamespaceURI());
    }

    protected void setText(Element elem, Object value, ElementContext rootCtx) {
        Tag tag = elem.getTag();
        ElementContext elemCtx = elem.getContext();
        String uri = value.getClass().getName(), prefix = elemCtx.getPrefix(uri);
        if (StringUtils.isBlank(prefix)) {
            prefix = String.valueOf(tag.getLocalName().charAt(0));
            if (StringUtils.isNotBlank(elemCtx.getNamespaceURI(prefix))) {
                prefix += "-alt";
            }
        }
        tag.setPrefix(prefix);
        tag.setNamespaceURI(uri);
        if (StringUtils.isBlank(rootCtx.getNamespaceURI(prefix))) {
            if (StringUtils.endsWith(prefix, "-alt")) {
                elemCtx.addNamespaceURI(prefix, uri);
            } else {
                rootCtx.addNamespaceURI(prefix, uri);
            }
        } else if (StringUtils.isBlank(elemCtx.getNamespaceURI(prefix))) {
            elemCtx.addNamespaceURI(prefix, uri);
        }
        elem.appendText(delegateStringConversion(value));
    }

    protected String toXml(VisitableNode node) {
        return (new ToXmlVisitor()).toXml(node);
    }
}

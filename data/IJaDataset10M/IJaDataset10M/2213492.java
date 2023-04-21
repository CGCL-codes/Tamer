package xades4j.xml.marshalling;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import xades4j.properties.QualifyingProperty;
import xades4j.properties.data.GenericDOMData;
import xades4j.properties.data.PropertyDataObject;
import xades4j.properties.data.SigAndDataObjsPropertiesData;
import xades4j.xml.bind.xades.ObjectFactory;
import xades4j.utils.CollectionUtils;
import xades4j.utils.DOMHelper;

/**
 *
 * @author Luís
 */
abstract class BaseJAXBMarshaller<TXml> {

    private final Map<Class, QualifyingPropertyDataToXmlConverter<TXml>> converters;

    private final String propsElemName;

    protected BaseJAXBMarshaller(int convertersInitialSize, String propsElemName) {
        this.converters = new HashMap<Class, QualifyingPropertyDataToXmlConverter<TXml>>(convertersInitialSize);
        this.propsElemName = propsElemName;
    }

    protected void putConverter(Class<? extends PropertyDataObject> propClass, QualifyingPropertyDataToXmlConverter<TXml> propConverter) {
        this.converters.put(propClass, propConverter);
    }

    protected void doMarshal(SigAndDataObjsPropertiesData properties, Node qualifyingPropsNode, TXml xmlProps) throws MarshalException {
        if (properties.isEmpty()) return;
        Document doc = qualifyingPropsNode.getOwnerDocument();
        Collection<PropertyDataObject> unknownSigProps = null;
        if (!properties.getSigProps().isEmpty()) {
            prepareSigProps(xmlProps);
            unknownSigProps = convert(properties.getSigProps(), xmlProps, doc);
        }
        Collection<PropertyDataObject> unknownDataObjProps = null;
        if (!properties.getDataObjProps().isEmpty()) {
            prepareDataObjsProps(xmlProps);
            unknownDataObjProps = convert(properties.getDataObjProps(), xmlProps, doc);
        }
        if (propsNotAlreadyPresent(qualifyingPropsNode)) doJAXBMarshalling(qualifyingPropsNode, xmlProps); else {
            Node tempNode = DOMHelper.createElement(qualifyingPropsNode.getOwnerDocument(), "temp", null, QualifyingProperty.XADES_XMLNS);
            qualifyingPropsNode.appendChild(tempNode);
            doJAXBMarshalling(tempNode, xmlProps);
            qualifyingPropsNode.removeChild(tempNode);
            transferProperties(qualifyingPropsNode, tempNode);
        }
        Element topMostPropsElem = DOMHelper.getFirstDescendant((Element) qualifyingPropsNode, QualifyingProperty.XADES_XMLNS, propsElemName);
        if (!CollectionUtils.nullOrEmpty(unknownSigProps)) marshallUnknownProps(unknownSigProps, DOMHelper.getFirstChildElement(topMostPropsElem));
        if (!CollectionUtils.nullOrEmpty(unknownDataObjProps)) marshallUnknownProps(unknownDataObjProps, DOMHelper.getLastChildElement(topMostPropsElem));
    }

    private Collection<PropertyDataObject> convert(Collection<PropertyDataObject> props, TXml xmlProps, Document doc) throws MarshalException {
        Collection<PropertyDataObject> unknownProps = null;
        QualifyingPropertyDataToXmlConverter<TXml> conv;
        for (PropertyDataObject p : props) {
            conv = this.converters.get(p.getClass());
            if (null == conv) {
                unknownProps = CollectionUtils.newIfNull(unknownProps, 1);
                unknownProps.add(p);
            } else conv.convertIntoObjectTree(p, xmlProps, doc);
        }
        return unknownProps;
    }

    private boolean propsNotAlreadyPresent(Node qualifyingPropsNode) {
        return null == DOMHelper.getFirstDescendant((Element) qualifyingPropsNode, QualifyingProperty.XADES_XMLNS, propsElemName);
    }

    private void doJAXBMarshalling(Node qualifyingPropsNode, TXml xmlProps) throws MarshalException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(xmlProps.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            Object propsElem = createPropsXmlElem(new ObjectFactory(), xmlProps);
            marshaller.marshal(propsElem, qualifyingPropsNode);
        } catch (JAXBException ex) {
            throw new MarshalException("Error on JAXB marshalling", ex);
        }
    }

    private void transferProperties(Node qualifPropsNode, Node tempNode) {
        Element existingProps = DOMHelper.getFirstDescendant((Element) qualifPropsNode, QualifyingProperty.XADES_XMLNS, propsElemName);
        Element newProps = DOMHelper.getFirstChildElement(tempNode);
        Element newSpecificProps = DOMHelper.getFirstChildElement(newProps);
        do {
            Element existingSpecificProps = DOMHelper.getFirstDescendant(existingProps, newSpecificProps.getNamespaceURI(), newSpecificProps.getLocalName());
            if (null == existingSpecificProps) existingProps.appendChild(newSpecificProps); else transferChildren(newSpecificProps, existingSpecificProps);
            newSpecificProps = DOMHelper.getNextSiblingElement(newSpecificProps);
        } while (newSpecificProps != null);
    }

    private void transferChildren(Element from, Element to) {
        Node child = from.getFirstChild();
        Node childSib;
        while (child != null) {
            childSib = child.getNextSibling();
            to.appendChild(child);
            child = childSib;
        }
    }

    private void marshallUnknownProps(Collection<PropertyDataObject> unknownProps, Element parent) throws MarshalException {
        for (PropertyDataObject pData : unknownProps) {
            if (!(pData instanceof GenericDOMData)) throw new UnsupportedDataObjectException(pData);
            Node propElem = ((GenericDOMData) pData).getPropertyElement();
            if (propElem.getOwnerDocument() != parent.getOwnerDocument()) propElem = parent.getOwnerDocument().importNode(propElem, true);
            parent.appendChild(propElem);
        }
    }

    protected abstract void prepareSigProps(TXml xmlProps);

    protected abstract void prepareDataObjsProps(TXml xmlProps);

    protected abstract Object createPropsXmlElem(ObjectFactory objFact, TXml xmlProps);
}

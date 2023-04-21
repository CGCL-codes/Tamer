package org.gvsig.remoteClient.gml.schemas;

import java.io.IOException;
import java.util.Vector;
import org.gvsig.remoteClient.gml.factories.XMLTypesFactory;
import org.gvsig.remoteClient.gml.types.IXMLType;
import org.gvsig.remoteClient.gml.types.XMLComplexType;
import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * A simple XSD element that represent an object
 * with a type of data.
 * 
 * @author Jorge Piera Llodr� (piera_jor@gva.es)
 * @author Carlos S�nchez Peri��n (sanchez_carper@gva.es)
 * 
 */
public class XMLElement {

    private String name = null;

    private String reference = null;

    private IXMLType type = null;

    private String typeUnknown = null;

    private int minOccurs = -1;

    private int maxOccurs = -1;

    private int totalDigits = 0;

    private int fractionDigits = 0;

    private XMLElement parentElement = null;

    public XMLElement(XMLSchemaParser parser) throws XmlPullParserException, IOException {
        super();
        parse(parser);
    }

    public XMLElement(String name) {
        super();
        this.name = name;
    }

    /**
	 * @return Returns the maxOccurs.
	 */
    public int getMaxOccurs() {
        return maxOccurs;
    }

    /**
	 * @param maxOccurs The maxOccurs to set.
	 */
    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    /**
	 * @return Returns the minOccurs.
	 */
    public int getMinOccurs() {
        return minOccurs;
    }

    /**
	 * @param minOccurs The minOccurs to set.
	 */
    public void setMinOccurs(int minOccurs) {
        this.minOccurs = minOccurs;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the type.
	 */
    public IXMLType getEntityType() {
        if (type != null) {
            return type;
        }
        if (typeUnknown != null) {
            this.type = XMLTypesFactory.getType(typeUnknown);
        }
        if ((type == null) && (typeUnknown != null)) {
            if (typeUnknown.split(":").length > 1) {
                this.type = XMLTypesFactory.getType(null + ":" + typeUnknown.split(":")[1]);
                if (type == null) {
                    this.type = XMLTypesFactory.getType(typeUnknown.split(":")[1]);
                }
            }
        }
        return type;
    }

    /**
	 * @param type The type to set.
	 */
    public void setEntityType(String type) {
        IXMLType xmlType = XMLTypesFactory.getType(type);
        if (xmlType == null) {
            String[] types = type.split(":");
            if (types.length == 1) {
                xmlType = XMLTypesFactory.getType("XS:" + type);
            }
            if (xmlType == null) {
                xmlType = XMLTypesFactory.getType("GML:" + type);
            }
            if (xmlType == null) {
                typeUnknown = type;
            }
        }
        this.type = xmlType;
    }

    public String toString() {
        return name;
    }

    /**
     * Parses the contents of the parser  to extract the information
     * about a XSD element
	 * @throws IOException 
	 * @throws XmlPullParserException 
     * 
     */
    private void parse(XMLSchemaParser parser) throws XmlPullParserException, IOException {
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            if (parser.getAttributeName(i).compareTo(CapabilitiesTags.ELEMENT_NAME) == 0) {
                setName(parser.getAttributeValue(i));
            } else if (parser.getAttributeName(i).compareTo(CapabilitiesTags.ELEMENT_TYPE) == 0) {
                setEntityType(parser.getAttributeValue(i));
            } else if (parser.getAttributeName(i).compareTo(CapabilitiesTags.ELEMENT_MAXOCCURS) == 0) {
                try {
                    setMaxOccurs(Integer.parseInt(parser.getAttributeValue(i)));
                } catch (NumberFormatException e) {
                    setMaxOccurs(-1);
                }
            } else if (parser.getAttributeName(i).compareTo(CapabilitiesTags.ELEMENT_MINOCCURS) == 0) {
                try {
                    setMinOccurs(Integer.parseInt(parser.getAttributeValue(i)));
                } catch (NumberFormatException e) {
                    setMinOccurs(-1);
                }
            } else if (parser.getAttributeName(i).compareTo(CapabilitiesTags.ELEMENT_REF) == 0) {
                setReference(parser.getAttributeValue(i));
            }
        }
    }

    public void parseSimpleType(XMLSchemaParser parser) throws IOException, XmlPullParserException {
        int currentTag;
        boolean end = false;
        currentTag = parser.getEventType();
        while (!end) {
            switch(currentTag) {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.RESTRICTION) == 0) {
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).compareTo(CapabilitiesTags.BASE) == 0) {
                                setEntityType(parser.getAttributeValue(i));
                            }
                        }
                        parseRestriction(parser);
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.SIMPLETYPE) == 0) end = true;
                    break;
                case KXmlParser.TEXT:
                    break;
            }
            if (!end) {
                currentTag = parser.next();
            }
        }
    }

    private void parseRestriction(XMLSchemaParser parser) throws IOException, XmlPullParserException {
        int currentTag;
        boolean end = false;
        parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.RESTRICTION);
        currentTag = parser.next();
        while (!end) {
            switch(currentTag) {
                case KXmlParser.START_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.TOTAL_DIGITS) == 0) {
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).compareTo(CapabilitiesTags.VALUE) == 0) {
                                setTotalDigits(Integer.parseInt(parser.getAttributeValue(i)));
                            }
                        }
                    } else if (parser.getName().compareTo(CapabilitiesTags.FRACTION_DIGITS) == 0) {
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).compareTo(CapabilitiesTags.VALUE) == 0) {
                                setFractionDigits(Integer.parseInt(parser.getAttributeValue(i)));
                            }
                        }
                    }
                    break;
                case KXmlParser.END_TAG:
                    if (parser.getName().compareTo(CapabilitiesTags.RESTRICTION) == 0) end = true;
                    break;
                case KXmlParser.TEXT:
                    break;
            }
            if (!end) {
                currentTag = parser.next();
            }
        }
    }

    /**
	 * @return Returns the reference.
	 */
    public String getReference() {
        return reference;
    }

    /**
	 * @param reference The reference to set.
	 */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
	 * @return Returns the fractionDigits.
	 */
    public int getFractionDigits() {
        return fractionDigits;
    }

    /**
	 * @param fractionDigits The fractionDigits to set.
	 */
    public void setFractionDigits(int fractionDigits) {
        this.fractionDigits = fractionDigits;
    }

    /**
	 * @return Returns the totalDigits.
	 */
    public int getTotalDigits() {
        return totalDigits;
    }

    /**
	 * @param totalDigits The totalDigits to set.
	 */
    public void setTotalDigits(int totalDigits) {
        this.totalDigits = totalDigits;
    }

    /**
	 * @return Returns the parentType.
	 */
    public XMLElement getParentElement() {
        return parentElement;
    }

    /**
	 * @param parent The parentTypeparentType to set.
	 */
    public void setParentElement(XMLElement parentElement) {
        this.parentElement = parentElement;
    }

    /**
	 * This method returns the full element name. The full name
	 * is composed by all the parent names. E.g: ParentName/ChildName 
	 * @return
	 */
    public String getFullName() {
        String path = getName();
        XMLElement parent = this.getParentElement();
        while (parent != null) {
            if (parent.getParentElement() != null) {
                path = parent.getName() + "/" + path;
            }
            parent = parent.getParentElement();
        }
        return path;
    }

    /**
	 * Search a attribute by name. If the attribute is found, the
	 * XMLElement is returned
	 * @param attName
	 * Attribute to search
	 * @return xMLElement
	 */
    public XMLElement searchAttribute(String attName) {
        return searchAttribute(attName, getName());
    }

    private XMLElement searchAttribute(String attName, String fullName) {
        if (fullName.equals(attName)) {
            return this;
        }
        if (getEntityType().getType() == IXMLType.COMPLEX) {
            Vector attributes = ((XMLComplexType) getEntityType()).getAttributes();
            XMLElement child = null;
            for (int i = 0; i < attributes.size(); i++) {
                child = (XMLElement) attributes.get(i);
                XMLElement element = child.searchAttribute(attName, fullName + "/" + child.getName());
                if (element != null) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
	 * Return the element chidren (if the element is a complex type)
	 * @return
	 */
    public Vector getChildren() {
        if ((getEntityType() != null) && (getEntityType().getType() == IXMLType.COMPLEX)) {
            return ((XMLComplexType) getEntityType()).getAttributes();
        }
        return new Vector();
    }

    /**
	 * Returns if the attribute is multiple or not
	 * @return
	 */
    public boolean isMultiple() {
        if (maxOccurs == 1) {
            return false;
        }
        return true;
    }
}

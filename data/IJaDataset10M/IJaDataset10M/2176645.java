package com.abiquo.framework.xml.events;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import org.codehaus.stax2.XMLStreamWriter2;
import com.abiquo.framework.domain.AbstractName;
import com.abiquo.framework.exception.EventCastException;
import com.abiquo.framework.xml.EventConstants;

/**
 * This is the binding class for XML complex type:
 * @verbatim
   <xs:complexType name="AbstractName"> 
     <xs:attribute name="LocalId" type="xs:string"/> 
   </xs:complexType>
   @endverbatim
 */
public class AbstractNameXMLEvent extends AbstractXMLEvent {

    /** The start element. */
    private StartElement startElement;

    /** The end element. */
    private EndElement endElement;

    /** The abstractName. */
    private AbstractName abstractName;

    /** The st elem name. */
    private String elementName = "AbstractName";

    /**
	 * Instantiates a new abstract name XML event.
	 * 
	 * @param name
	 *            the name
	 */
    public AbstractNameXMLEvent(String name) {
        this(new AbstractName(name));
    }

    /**
	 * Instantiates a new abstract name XML event.
	 * 
	 * @param abstractName
	 *            the abstract name
	 */
    public AbstractNameXMLEvent(AbstractName abstractName) {
        this.abstractName = abstractName;
        createTagElements();
    }

    /**
	 * Instantiates a new abstract name XML event.
	 * 
	 * @param attributes
	 *            attributes iterator
	 * 
	 * @throws EventCastException
	 *             the event cast exception
	 */
    public AbstractNameXMLEvent(Iterator<Attribute> attributes) throws EventCastException {
        this.abstractName = new AbstractName("NameNotFound");
        Attribute att;
        String attName;
        while (attributes.hasNext()) {
            att = attributes.next();
            attName = att.getName().getLocalPart();
            if (attName.equalsIgnoreCase("LocalId")) {
                abstractName.setName(att.getValue());
            } else {
                throw new EventCastException("unknown attribute for abstractName AbsName:" + "[attName]:" + att.getName() + " [attValue]:" + att.getValue());
            }
        }
        createTagElements();
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#asCharacters()
	 */
    public Characters asCharacters() {
        throw new EventCastException("it is a  AbsName");
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#asEndElement()
	 */
    public EndElement asEndElement() {
        throw new EventCastException("it is a  AbsName");
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#asStartElement()
	 */
    public StartElement asStartElement() {
        throw new EventCastException("it is a  AbstractName");
    }

    /**
	 * Fill the start elements attributes. 
	 */
    private void createTagElements() {
        List<Attribute> lstAtt = new ArrayList<Attribute>();
        lstAtt.add(eventFactory.createAttribute("LocalId", this.abstractName.getName()));
        startElement = eventFactory.createStartElement(EventConstants.QN_ABSTRACT_NAME, lstAtt.iterator(), null);
        endElement = eventFactory.createEndElement(EventConstants.QN_ABSTRACT_NAME, null);
    }

    /**
	 * Fill the start elements attributes.
	 * 
	 * @param itAttr 
	 *          attribute iterator from message event allocator parser
	 */
    @SuppressWarnings("unused")
    private void createTagElements(Iterator<Attribute> itAttr) {
        startElement = eventFactory.createStartElement(EventConstants.QN_ABSTRACT_NAME, itAttr, null);
        endElement = eventFactory.createEndElement(EventConstants.QN_ABSTRACT_NAME, null);
    }

    /**
	 * Gets the abstract name.
	 * 
	 * @return the abstract name attribute
	 */
    public AbstractName getAbstractName() {
        return abstractName;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#getEventType()
	 */
    public int getEventType() {
        return EventConstants.ET_ABSTRACT_NAME;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#getLocation()
	 */
    public Location getLocation() {
        return endElement.getLocation();
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#getSchemaType()
	 */
    public QName getSchemaType() {
        return null;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#isAttribute()
	 */
    public boolean isAttribute() {
        return false;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#isCharacters()
	 */
    public boolean isCharacters() {
        return false;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#isEndDocument()
	 */
    public boolean isEndDocument() {
        return false;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#isEndElement()
	 */
    public boolean isEndElement() {
        return false;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#isEntityReference()
	 */
    public boolean isEntityReference() {
        return false;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#isNamespace()
	 */
    public boolean isNamespace() {
        return false;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#isProcessingInstruction()
	 */
    public boolean isProcessingInstruction() {
        return false;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#isStartDocument()
	 */
    public boolean isStartDocument() {
        return false;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#isStartElement()
	 */
    public boolean isStartElement() {
        return false;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#writeAsEncodedUnicode(java.io.Writer)
	 */
    public void writeAsEncodedUnicode(Writer writer) throws XMLStreamException {
        startElement.writeAsEncodedUnicode(writer);
        endElement.writeAsEncodedUnicode(writer);
    }

    /**
	 * StAX 2 full stream writer model.
	 * 
	 * @param w
	 *            the stream writer
	 * 
	 * @throws XMLStreamException
	 *             the XML stream exception
	 */
    public void writeUsing(XMLStreamWriter2 w) throws XMLStreamException {
        w.writeStartElement(elementName);
        w.writeAttribute("LocalId", abstractName.getName());
        w.writeEndElement();
    }
}

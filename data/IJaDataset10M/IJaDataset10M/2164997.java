package com.abiquo.framework.xml.events;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import org.codehaus.stax2.XMLStreamWriter2;
import com.abiquo.framework.domain.IService;
import com.abiquo.framework.exception.EventCastException;
import com.abiquo.framework.xml.EventConstants;
import com.abiquo.framework.xml.events.messages.Data;
import com.abiquo.framework.xml.events.messages.IXMLMessage;
import com.abiquo.util.compiler.ServiceCompiler;

/**
 * This is the binding class for XML complex type:
 * 
 * @verbatim 
  <!-- API defined resource, or the only idea of service / it is deployed as a new resource -->
	<xs:complexType name="Service">
		<xs:sequence>
			<xs:element name="DataList" type="DataListType" minOccurs="1" maxOccurs="1"/>
		</xs:sequence>
		<xs:attribute name="LocalId" type="xs:string" use="required"/>
	</xs:complexType>
	@endverbatim
 */
public class ServiceXMLEvent implements IXMLMessage {

    /** The start element. */
    private StartElement startElement;

    /** The end element. */
    private EndElement endElement;

    /** The service (domain object this class are binding) . */
    private IService rj;

    /**
	 * Instantiates a new service XML event.
	 * 
	 * @param itAttr
	 *            the attributes iterator
	 * @param javaFiles
	 *            the java files content
	 * 
	 * @throws EventCastException
	 *             the event cast exception. Or the framework-api.jar required to instantiate Service is not set at classpath
	 */
    public ServiceXMLEvent(Iterator<javax.xml.stream.events.Attribute> itAttr, List<Data> javaFiles) throws EventCastException {
        try {
            this.rj = ServiceCompiler.instantiateEmptyService();
        } catch (InstantiationException e) {
            throw new EventCastException("Can create the event since cant create the Service domain object" + "check if framework-api.jar is set at your classpath");
        }
        this.rj.setJavaFiles(javaFiles);
        this.rj.setName("nameNOTFOUND");
        javax.xml.stream.events.Attribute att;
        String attName;
        while (itAttr.hasNext()) {
            att = itAttr.next();
            attName = att.getName().getLocalPart();
            if (attName.equals("LocalId")) {
                this.rj.setName(att.getValue());
            } else {
                throw new EventCastException("unknown attribute for a service: " + "[attName]:" + att.getName() + " [attValue]:" + att.getValue());
            }
        }
        createTagElements();
    }

    /**
	 * Instantiates a new service XML event.
	 * 
	 * @param service
	 *            the service bean object containing the information
	 */
    public ServiceXMLEvent(IService service) {
        this.rj = service;
        createTagElements();
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#asCharacters()
	 */
    public Characters asCharacters() {
        throw new EventCastException("it is  a service");
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#asEndElement()
	 */
    public EndElement asEndElement() {
        throw new EventCastException("it is a service");
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#asStartElement()
	 */
    public StartElement asStartElement() {
        throw new EventCastException("it is  a service");
    }

    /**
	 * Fill the start elements attributes.
	 */
    private void createTagElements() {
        List<javax.xml.stream.events.Attribute> lstAtt = new ArrayList<javax.xml.stream.events.Attribute>();
        lstAtt.add(EventConstants.evntFact.createAttribute("LocalId", this.rj.getName()));
        startElement = EventConstants.evntFact.createStartElement(EventConstants.QN_SERVICE, lstAtt.iterator(), null);
        endElement = EventConstants.evntFact.createEndElement(EventConstants.QN_SERVICE, null);
    }

    /**
	 * Fill the start elements attributes.
	 * 
	 * @param itAttr
	 *            attribute iterator from message event allocator parser
	 */
    @SuppressWarnings("unused")
    private void createTagElements(Iterator<javax.xml.stream.events.Attribute> itAttr) {
        startElement = EventConstants.evntFact.createStartElement(EventConstants.QN_SERVICE, itAttr, null);
        endElement = EventConstants.evntFact.createEndElement(EventConstants.QN_SERVICE, null);
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#getEventType()
	 */
    public int getEventType() {
        return EventConstants.ET_SERVICE;
    }

    /**
	 * @see javax.xml.stream.events.XMLEvent#getLocation()
	 */
    public Location getLocation() {
        return endElement.getLocation();
    }

    /**
	 * Gets the service (domain object)
	 * 
	 * @return the service
	 */
    public IService getService() {
        return this.rj;
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
        if (this.rj.getJavaFiles() != null) {
            EventConstants.evntFact.createStartElement(new QName("DataList"), null, null).writeAsEncodedUnicode(writer);
            for (Data jf : this.rj.getJavaFiles()) {
                jf.writeAsEncodedUnicode(writer);
            }
            EventConstants.evntFact.createEndElement(new QName("DataList"), null);
        }
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
        w.writeStartElement("Service");
        w.writeAttribute("LocalId", this.rj.getName());
        if (this.rj.getJavaFiles() != null) {
            w.writeStartElement("DataList");
            for (Data jf : this.rj.getJavaFiles()) {
                jf.writeUsing(w);
            }
            w.writeEndElement();
        }
        w.writeEndElement();
    }
}

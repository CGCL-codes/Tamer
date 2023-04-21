package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.ServiceType;

/**
 * Class to encode a serviceType to xml
 * 
 * @author Michael
 */
public class ServiceTypeEncoder implements MessageEncoder {

    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
        ServiceType serviceType = (ServiceType) message;
        if (serviceType == null) {
            System.out.println("WARNING: Object serviceType is null and cannot be encoded");
            return;
        }
        writer.writeStartElement(ServiceType.ID);
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(serviceType.getId()));
        writer.writeEndElement();
        if (serviceType.getServiceName() != null) {
            writer.writeStartElement("serviceName");
            writer.writeCharacters(serviceType.getServiceName());
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
}

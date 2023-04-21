package org.slasoi.monitoring.fbk.impl;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slasoi.common.eventschema.EventInstance;
import org.slasoi.common.eventschema.ObjectFactory;

/**
 * Get an EventInstance containing the result event and deserialize it.
 *
 * @since 0.1
 */
public class MonitoringResutEventDeserializer {

    /** The event instance to deserialize. */
    private EventInstance eventInstance = null;

    /**
     * Construct passing the eventInstance.
     *
     * @param eventInstanceIn the event instance
     */
    public MonitoringResutEventDeserializer(final EventInstance eventInstanceIn) {
        super();
        this.eventInstance = eventInstanceIn;
    }

    /**
     * Return in the string the xml that represent the eventInstance object.
     *
     * @return String the deserialized string with the message
     */
    public final String deserialize() {
        StringWriter sw = new StringWriter();
        ObjectFactory of = new ObjectFactory();
        JAXBElement<EventInstance> event = of.createEvent(eventInstance);
        JAXBContext context = null;
        try {
            String packageName = "org.slasoi.common.eventschema";
            context = JAXBContext.newInstance(packageName);
        } catch (JAXBException e1) {
            e1.printStackTrace();
        }
        Marshaller marshaller = null;
        try {
            marshaller = context.createMarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        try {
            marshaller.marshal(event, sw);
            return sw.getBuffer().toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}

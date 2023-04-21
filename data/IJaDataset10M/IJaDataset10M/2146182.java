package com.patientis.business.eprescribing.util;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.patientos.surescripts.Message;
import org.xml.sax.InputSource;

public class JAXBUtils {

    /**
	 *  How shall we handle multiple threads?  Maybe lets  
	 * 
	 */
    private static JAXBContext context;

    private static final String contextName = "org.patientos.surescripts";

    static {
        try {
            context = JAXBContext.newInstance(contextName);
        } catch (JAXBException e) {
            throw new RuntimeException();
        }
    }

    public static Message unmarshall(String xml) throws JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        InputSource source = new InputSource(new StringReader(xml));
        return (Message) unmarshaller.unmarshal(source);
    }

    public static String marshall(Message message) throws JAXBException {
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(message, writer);
        return writer.toString();
    }
}

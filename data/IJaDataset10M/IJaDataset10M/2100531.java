package org.nexopenframework.jaxws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import junit.framework.TestCase;
import org.nexopenframework.example.protocol.Request;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Easy test for just check some beahviours of JAXB 2.0. Notice that
 *    all the classes are annotated with JAXB 2.0 annotations</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class JAXBTest extends TestCase {

    /**
	 * @throws JAXBException
	 * @throws DatatypeConfigurationException
	 * @throws XMLStreamException
	 */
    public void testJAXB() throws JAXBException, DatatypeConfigurationException, XMLStreamException {
        Request request = new Request();
        request.setId("ID-" + System.currentTimeMillis());
        request.setOperation("urn:nexopen:invoke");
        GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
        gc.setTime(new Date());
        request.setCreationDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
        request.setSource("Hello from this example");
        XMLOutputFactory xmlOutputFactoyr = XMLOutputFactory.newInstance();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLStreamWriter writer = xmlOutputFactoyr.createXMLStreamWriter(baos);
        JAXBContext context = JAXBContext.newInstance("org.nexopenframework.example.protocol");
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(request, writer);
        writer.flush();
        byte[] array = baos.toByteArray();
        System.out.println("Request :: " + new String(array));
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(array));
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(reader);
        assertNotNull(obj);
        assertTrue(obj instanceof Request);
        Request _request = (Request) obj;
        assertEquals("urn:nexopen:invoke", _request.getOperation());
    }
}

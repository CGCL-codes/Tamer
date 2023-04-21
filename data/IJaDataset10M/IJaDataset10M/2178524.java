package cat.inkubator.plugin4j.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author mariano
 */
public class Xmlizer {

    protected JAXBContext jaxbContext;

    protected Marshaller marshaller;

    protected Unmarshaller unmarshaller;

    protected Class klass;

    public Xmlizer(Class klass) throws JAXBException {
        this.klass = klass;
        jaxbContext = JAXBContext.newInstance(klass);
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        unmarshaller = jaxbContext.createUnmarshaller();
    }

    public String toXml(Object object) throws JAXBException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(object, out);
        return out.toString();
    }

    public void xmlToFile(Object object, File file) throws JAXBException {
        marshaller.marshal(object, file);
    }

    public void toXmlOut(Object object, OutputStream out) throws JAXBException {
        marshaller.marshal(object, out);
    }

    public Object fromXml(String xml) throws JAXBException {
        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
        return unmarshaller.unmarshal(in);
    }

    public Object fromFileXml(File file) throws JAXBException {
        return unmarshaller.unmarshal(file);
    }
}

package jse;

import java.io.InputStream;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author WangShuai
 */
public class STAXTest {

    public static void main(String[] args) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = STAXTest.class.getResourceAsStream("/user.xml");
        XMLEventReader reader = inputFactory.createXMLEventReader(in);
        while (reader.hasNext()) {
            XMLEvent e = reader.nextEvent();
            if (e.isStartElement()) {
                StartElement se = e.asStartElement();
                if (se.getName().getLocalPart().equalsIgnoreCase("user")) {
                    String attrIdValue = se.getAttributeByName(QName.valueOf("id")).getValue();
                    System.out.println("userId:" + attrIdValue);
                }
            }
        }
    }
}

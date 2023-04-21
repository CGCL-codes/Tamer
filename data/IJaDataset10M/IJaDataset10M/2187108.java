package net.sf.xml2cb.core.io;

import javax.xml.stream.XMLStreamException;
import net.sf.xml2cb.test.util.XmlUtils;
import org.w3c.dom.Document;

public class XmlReaderDOMTest extends AbstractXmlReaderTest {

    protected XmlReader createXmlReader(byte[] xml) throws XMLStreamException {
        Document document = XmlUtils.createDocument(new FastByteArrayInputStream(xml));
        return new XmlReaderDOM(document);
    }
}

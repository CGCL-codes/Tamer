package hrc.tool.xml;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;

/**
 * @author hrc
 * @see XmlTeller
 */
public class NewFileXmlTeller extends XmlTeller {

    NewFileXmlTeller(String fileName, String root) throws XmlException {
        super.fileName = fileName;
        this.createNewXML(root);
    }

    private void createNewXML(String root) throws XmlException {
        try {
            File file = new File(fileName);
            file.createNewFile();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            m_doc = builder.newDocument();
            m_doc.setXmlVersion("1.0");
            Element element = m_doc.createElement(root);
            m_doc.appendChild(element);
        } catch (Exception e) {
            throw new XmlException("fail to create new xml file");
        }
    }

    @Override
    public void save() throws XmlException {
        super.saveXml();
    }
}

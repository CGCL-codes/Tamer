package openminer.util;

import java.io.ByteArrayInputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class XMLFileLoader {

    public static Element loadXMLDOM(String fileName) throws Exception {
        String xml = TextFileLoader.loadFile(fileName);
        SAXBuilder builder = new SAXBuilder();
        ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Document doc = builder.build(is);
        return doc.getRootElement();
    }
}

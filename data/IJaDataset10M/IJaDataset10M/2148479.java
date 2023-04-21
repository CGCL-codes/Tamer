package djbeans.xml;

import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class XmlParser {

    public static FolderXmlBean parseXml(String xml) throws Exception {
        StringReader reader = new StringReader(xml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(reader);
        return doParseXml(doc);
    }

    public static FolderXmlBean parseXml(InputStream is) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(is);
        return doParseXml(doc);
    }

    private static FolderXmlBean doParseXml(Document doc) {
        Element root = doc.getRootElement();
        FolderXmlBean vbean = new FolderXmlBean(root.getAttributeValue("version"), Boolean.parseBoolean(root.getAttributeValue("skip")), root.getAttributeValue("description"));
        List list = root.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Element e = (Element) list.get(i);
            if (e.getName().equalsIgnoreCase("folder")) {
                FolderXmlBean gbean = new FolderXmlBean(e);
                vbean.addFolder(gbean);
                addFolderChilds(gbean, e);
            }
        }
        return vbean;
    }

    private static void addFolderChilds(FolderXmlBean parentbean, Element root) {
        List list = root.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Element e = (Element) list.get(i);
            if (e.getName().equalsIgnoreCase("folder")) {
                FolderXmlBean gbean = new FolderXmlBean(e);
                parentbean.addFolder(gbean);
                addFolderChilds(gbean, e);
            } else if (e.getName().equalsIgnoreCase("property")) {
                PropertyXmlBean pbean = new PropertyXmlBean(e);
                parentbean.addProperty(pbean);
            }
        }
    }

    public static void main(String[] args) {
        InputStream is = null;
        try {
            ClassLoader classLoader = XmlParser.class.getClassLoader();
            is = classLoader.getResourceAsStream("djb.xml");
            FolderXmlBean o = parseXml(is);
            o.print(System.out, "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (Exception e) {
            }
        }
    }
}

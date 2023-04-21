package de.harizen.mapcreator.graphics.play;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class OpenXMLFile {

    private static String filename = "C:\\java\\workspaces\\jboss_svn\\shunnyMapCreator\\Speicherung_Mapables.xml";

    public URL getUrl(String filename) throws Exception {
        File f = new File(filename);
        return f.toURL();
    }

    public Document parse(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        return document;
    }

    public static void main(String[] args) {
        try {
            OpenXMLFile xml = new OpenXMLFile();
            Document document = xml.parse(xml.getUrl(filename));
            Element root = document.getRootElement();
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                System.out.println(element.getName());
                System.out.println("\t" + element.attributeValue("name"));
                System.out.println("\t" + element.attributeValue("description"));
                for (Iterator j = element.elementIterator(); j.hasNext(); ) {
                    Element foo = (Element) j.next();
                    System.out.println("\t" + foo.getName());
                    System.out.println(foo.getText().trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

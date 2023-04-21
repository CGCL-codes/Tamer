package com.anlugifa.editor.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.anlugifa.editor.base.EditorLog;

public class XmlManager {

    private File xmlFile;

    private Document document;

    public XmlManager(File file) throws IOException {
        setXmlFile(file);
        read();
    }

    private void read() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFile);
        } catch (ParserConfigurationException e) {
            EditorLog.thrown(getClass(), "read", e);
        } catch (SAXException e) {
            EditorLog.thrown(getClass(), "read", e);
        } catch (IOException e) {
            EditorLog.thrown(getClass(), "read", e);
        }
    }

    public List<Element> parseElements(String key) {
        List<Element> list = new ArrayList<Element>();
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName(key);
        for (int i = 0; i < nodeList.getLength(); i++) list.add((Element) nodeList.item(i));
        return list;
    }

    private void setXmlFile(File file) throws IOException {
        if ((file == null) || (!file.exists()) || (!file.canRead())) throw new IOException("The file is not accessable!");
        this.xmlFile = file;
    }
}

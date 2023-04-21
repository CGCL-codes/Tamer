package org.ericliu.xml.xslt;

import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ResearchTrans {

    public static void main(String args[]) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("E://java/xalan-j_2_7_0/samples/SimpleTransform/birds.xml"));
            String str = transNodeToString(doc);
            System.out.println(str);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String transNodeToString(Node node) {
        String errorDesc = "��xml�ڵ�ת��Ϊ�ַ�:";
        String string = "";
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(sw));
            string = sw.toString();
            string = string.substring(string.indexOf('\n') + 1);
        } catch (TransformerConfigurationException tcex) {
            throw new RuntimeException(errorDesc + tcex.getMessage());
        } catch (TransformerException tex) {
            throw new RuntimeException(errorDesc + tex.getMessage());
        } catch (TransformerFactoryConfigurationError tfcerr) {
            throw new RuntimeException(errorDesc + tfcerr.getMessage());
        }
        return (string);
    }
}

package com.calipso.reportgenerator.reportmanager;

import org.apache.commons.vfs.FileObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import java.io.*;
import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import com.calipso.reportgenerator.common.exception.InfoException;
import com.calipso.reportgenerator.common.LanguageTraslator;

/**
 *
 * User: jbassino
 * Date: 30/09/2004
 * Time: 15:02:33
 *
 */
public class ReportSourceDefinitionVersion {

    public static void validateVersion(FileObject fileObject) throws InfoException {
        try {
            File file = new File(fileObject.getName().getPath());
            if (isOldVersion(file)) {
                transformToNewVersion(file);
            }
        } catch (Exception e) {
            throw new InfoException(LanguageTraslator.traslate(""), e);
        }
    }

    public static void transformToNewVersion(File file) throws Exception {
        Node docRoot = getDocRoot(file);
        Collection collection = getDimensionElements(docRoot);
        eliminateAllElements(collection, "QUERYCONVERTTOSTRINGPATTERN");
        writeFile(file, docRoot);
    }

    private static void writeFile(File file, Node docRoot) throws InfoException {
        Document document = docRoot.getOwnerDocument();
        String encoding = document.getDocumentElement().getAttribute("encoding");
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            Result result = new StreamResult(new FileOutputStream(file));
            transformer.transform(source, result);
            System.out.println("ResportSourceDefinition upgraded");
        } catch (Exception e) {
            throw new InfoException(LanguageTraslator.traslate("376"), e);
        }
    }

    private static void eliminateAllElements(Collection collection, String elementToEliminate) {
        for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
            Node node = (Node) iterator.next();
            eliminateElement(node, elementToEliminate);
        }
    }

    private static void eliminateElement(Node node, String elementToEliminate) {
        if (node.hasAttributes()) {
            NamedNodeMap attrs = node.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                if (attrs.item(i).getNodeName().equalsIgnoreCase(elementToEliminate)) {
                    attrs.removeNamedItem(attrs.item(i).getNodeName());
                }
            }
        }
    }

    private static Collection getDimensionElements(Node docRoot) throws InfoException {
        Collection result = new Vector();
        NodeList nodes = docRoot.getChildNodes();
        Node dimensions = null;
        for (int i = 0; i < nodes.getLength(); i++) {
            dimensions = nodes.item(i);
            if (dimensions.getNodeName().equalsIgnoreCase("DimensionSourceDefinitions")) {
                break;
            }
        }
        if (dimensions != null) {
            nodes = dimensions.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node dimension = nodes.item(i);
                result.add(dimension);
            }
        } else {
            throw new InfoException(LanguageTraslator.traslate("375"));
        }
        return result;
    }

    public static Node getDocRoot(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        return document.getDocumentElement();
    }

    public static boolean isOldVersion(File file) throws IOException {
        StringBuffer buffer = readFile(file);
        String s = buffer.toString();
        s = s.toUpperCase();
        return (s.indexOf("QUERYCONVERTTOSTRINGPATTERN") > 0);
    }

    /**
   * Cargar el archivo en un StringBuffer, leyendolo linea a linea
   * @param file archivo a leer
   * @return StringBuffer el texto del archivo
   * @throws IOException
   */
    public static StringBuffer readFile(File file) throws IOException {
        StringBuffer sb = new StringBuffer();
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader in = new BufferedReader(isr);
        String s;
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        return sb;
    }
}

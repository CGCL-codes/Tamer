package cz.zweistein.autoupdater.definition;

import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import cz.zweistein.autoupdater.definition.vo.Directory;
import cz.zweistein.autoupdater.definition.vo.VersionedFile;

public class XMLProducer {

    public static String createXML(Directory directory) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("autoUpdater");
        doc.appendChild(root);
        root.appendChild(parseDir(directory, doc));
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "3");
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        String xmlString = sw.toString();
        return xmlString;
    }

    private static Node parseDir(Directory directory, Document doc) {
        Element directoryElement = doc.createElement("directory");
        directoryElement.setAttribute("name", directory.getName());
        for (Directory subDirectory : directory.getDirectories()) {
            directoryElement.appendChild(parseDir(subDirectory, doc));
        }
        for (VersionedFile versionedFile : directory.getFiles()) {
            directoryElement.appendChild(parseFile(versionedFile, doc));
        }
        return directoryElement;
    }

    private static Node parseFile(VersionedFile file, Document doc) {
        Element fileElement = doc.createElement("file");
        fileElement.setAttribute("name", file.getName());
        fileElement.setAttribute("sha1", file.getSha1());
        fileElement.setAttribute("size", Long.toString(file.getSize()));
        return fileElement;
    }
}

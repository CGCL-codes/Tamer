package parser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.BitSet;
import java.util.HashSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class Report {

    private OutputStream m_out;

    private Document m_doc;

    public Report(OutputStream out) {
        m_out = out;
        DocumentBuilder db;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            m_doc = db.newDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void build(BitSet seqMark, HashSet<String> services) {
        Element htmlNode = m_doc.createElement("html");
        m_doc.appendChild(htmlNode);
        Element headNode = m_doc.createElement("head");
        Element bodyNode = m_doc.createElement("body");
        htmlNode.appendChild(headNode);
        htmlNode.appendChild(bodyNode);
        Element styleNode = m_doc.createElement("style");
        Text styleText = m_doc.createTextNode("\n.container {\n width: 700px;\n color: black;\n}\n" + "\n.blockContainer {\n width: 400px;\n color: black;\n float: left;\n}\n" + ".block {\n position: static;\n height: 10px;\n width: 10px;\n" + " border: 1px solid;\n background: green;\n float: left;\n}\n" + ".missedblock {\n position: static;\n height: 10px;\n width: 10px;\n" + " border: 1px solid;\n background: #BBBB55;\n float: left;\n}\n" + ".info {\n width: 200px;\n color: black;\n float: left;\n}\n" + ".info p {\n font-size: 10px;\n font-family: verdana;\n color: blue;\n}\n");
        headNode.appendChild(styleNode);
        styleNode.appendChild(styleText);
        Element containerNode = m_doc.createElement("div");
        containerNode.setAttribute("class", "container");
        bodyNode.appendChild(containerNode);
        Element blockNode = m_doc.createElement("div");
        blockNode.setAttribute("class", "blockContainer");
        Element infoNode = m_doc.createElement("div");
        infoNode.setAttribute("class", "info");
        containerNode.appendChild(blockNode);
        containerNode.appendChild(infoNode);
        Element infoRecord = m_doc.createElement("p");
        Text titleText = m_doc.createTextNode("Starting sequence: " + seqMark.nextSetBit(0));
        infoRecord.appendChild(titleText);
        infoNode.appendChild(infoRecord);
        for (String service : services) {
            infoRecord = m_doc.createElement("p");
            titleText = m_doc.createTextNode("Services: " + service);
            infoRecord.appendChild(titleText);
            infoNode.appendChild(infoRecord);
        }
        int initialIndx = seqMark.nextSetBit(0);
        for (int i = initialIndx; i < seqMark.length(); i++) {
            Element divNode = m_doc.createElement("div");
            String classType = "";
            if (seqMark.get(i)) {
                classType = "block";
            } else {
                classType = "missedblock";
            }
            divNode.setAttribute("class", classType);
            blockNode.appendChild(divNode);
        }
    }

    public void write() {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource ds = new DOMSource(m_doc);
            StreamResult outputTarget = new StreamResult(m_out);
            tr.transform(ds, outputTarget);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OutputStream out;
        out = System.out;
        try {
            out = new FileOutputStream("/home/randres/Escritorio/report.htm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Report r = new Report(out);
        BitSet seqMark = new BitSet();
        seqMark.set(4);
        seqMark.set(12);
        seqMark.set(122);
        seqMark.set(1212);
        HashSet<String> services = new HashSet<String>();
        services.add("21, 3");
        r.build(seqMark, services);
        r.write();
    }
}

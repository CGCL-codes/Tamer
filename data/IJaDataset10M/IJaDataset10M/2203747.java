package eu.sarunas.projects.atf.utils;

import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import eu.sarunas.projects.atf.tests.TestCase;
import eu.sarunas.projects.atf.tests.TestInput;
import eu.sarunas.projects.atf.tests.TestInputParameter;
import eu.sarunas.projects.atf.tests.TestSuite;

public class Serializer {

    public String toString(TestSuite testSuite) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.appendChild(toXml(testSuite, doc));
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();
            return xmlString;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return "err";
    }

    ;

    public Node toXml(TestSuite testSuite, Document document) {
        Node root = document.createElement("testSuite");
        for (TestCase testCase : testSuite.getTestCases()) {
            root.appendChild(toXml(testCase, document));
        }
        return root;
    }

    ;

    private Node toXml(TestCase testCase, Document document) {
        Element root = document.createElement("testCase");
        root.setAttribute("class", testCase.getClassName());
        root.setAttribute("method", testCase.getMethod());
        for (TestInput input : testCase.getInputs()) {
            root.appendChild(toXml(input, document));
        }
        return root;
    }

    ;

    private Node toXml(TestInput testInput, Document document) {
        Element root = document.createElement("testInput");
        for (TestInputParameter parameter : testInput.getInputParameters()) {
            root.appendChild(toXml(parameter, document));
        }
        return root;
    }

    ;

    private Node toXml(TestInputParameter parameter, Document document) {
        Element root = document.createElement("testInputParameter");
        root.setAttribute("name", parameter.getName());
        root.setAttribute("value", parameter.getValue());
        return root;
    }

    ;
}

;

package net.sourceforge.javacavemapstest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class to manage the data of a suvey using an XML file.
 * 
 * created on 15/12/2008 by arkalain@gmail.com
 */
public class SurveyXMLFile {

    private Hashtable<String, String> attributes = new Hashtable<String, String>();

    private Document document;

    private Element docRoot;

    public SurveyXMLFile() {
        attributes.put("to-station-name", "");
        attributes.put("from-station-name", "");
        attributes.put("azmith", "");
        attributes.put("distance", "");
        attributes.put("vertical-angle", "");
    }

    public SurveyXMLFile(String toStationName, String fromStationName, String azmith, String distance, String verticalAngle) {
        attributes.put("to-station-name", toStationName);
        attributes.put("from-station-name", fromStationName);
        attributes.put("azmith", azmith);
        attributes.put("distance", distance);
        attributes.put("vertical-angle", verticalAngle);
    }

    /**
	 * Puts the properties of the survey into an XML document
	 * 
	 * @throws ParserConfigurationException
	 */
    public Document parseToXML() throws ParserConfigurationException {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Element surveyline, attribute;
        document = docBuilder.newDocument();
        docRoot = document.createElement("survey");
        document.appendChild(docRoot);
        surveyline = document.createElement("survey-line");
        attribute = document.createElement("to-station-name");
        surveyline.setAttribute("to-station-name", "value to station");
        surveyline.setAttribute("from-station-name", "value from station");
        surveyline.setAttribute("azmith", "value azmith");
        surveyline.setAttribute("distance", "value distance");
        surveyline.setAttribute("vertical-angle", "value vertical angle");
        docRoot.appendChild(surveyline);
        return document;
    }

    /**
	 * Gets the data for the SurveyXMLFile object from an xml file.
	 * The document will get stored and returned when this method is called.
	 * 
	 * @throws Exception 
	 * @return Document - the document matching the content of the file.
	 */
    public Document loadXMLFromFile(String filepath) throws Exception {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new Exception("Specified file does not exist or you do not have access to it.");
        }
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        document = builder.parse(file);
        return document;
    }

    /**
	 * This will store the xml created in parseToXML() to a physical file in the
	 * hard disk. Call parseToXML() BEFORE using this.
	 * 
	 * @throws IOException
	 * @throws DOMException
	 */
    public void saveXmlToPhysicalFile(Document doc) throws TransformerException, FileNotFoundException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        Source src = new DOMSource(doc);
        Result dest = new StreamResult(new FileOutputStream("C:/SurveyXml.xml"));
        transformer.transform(src, dest);
    }

    public String getToStationName() {
        return attributes.get("to-station-name").toString();
    }

    public void setToStationName(String toStationName) {
        attributes.put("to-station-name", toStationName);
    }

    public String getFromStationName() {
        return attributes.get("from-station-name").toString();
    }

    public void setFromStationName(String fromStationName) {
        attributes.put("from-station-name", fromStationName);
    }

    public String getAzmith() {
        return attributes.get("azmith").toString();
    }

    public void setAzmith(String azmith) {
        attributes.put("azmith", azmith);
    }

    public String getDistance() {
        return attributes.get("distance").toString();
    }

    public void setDistance(String distance) {
        attributes.put("distance", distance);
    }

    public String getVerticalAngle() {
        return attributes.get("vertical-angle").toString();
    }

    public void setVerticalAngle(String verticalAngle) {
        attributes.put("vertical-angle", verticalAngle);
    }

    public static void main(String[] args) throws Exception {
        SurveyXMLFile surveyXML = new SurveyXMLFile();
        Document doc = surveyXML.loadXMLFromFile("C:/SurveyXml.xml");
        Hashtable values = surveyXML.getAttributes();
        Set valueset = values.keySet();
        Object key;
        for (Iterator i = valueset.iterator(); i.hasNext(); ) {
            key = i.next();
            System.out.print(key + ": ");
            System.out.println(values.get(key));
        }
    }

    public Hashtable<String, String> getAttributes() {
        return attributes;
    }
}

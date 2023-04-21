package managedBean;

import Domein.Cache;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Kristof
 */
@WebService(name = "Studenten.xml doorgeven")
public class OfferStudentServiceToASP {

    private String SERVERADRESS = "http://localhost:8084/pas_sylvana_java/";

    public OfferStudentServiceToASP() {
    }

    @WebMethod(action = "getStudenten")
    public List<Utils.JavaStudentInfo> getStudenten() {
        List<Utils.JavaStudentInfo> studenten;
        try {
            studenten = (List<Utils.JavaStudentInfo>) Cache.getInstance().getFromCache("list_read_xml");
        } catch (NullPointerException ex) {
            studenten = new ArrayList<Utils.JavaStudentInfo>();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = null;
            DocumentBuilderFactory builder = null;
            try {
                readXmlDocument(studenten, schemaFactory);
            } catch (ParserConfigurationException ex1) {
                ex1.printStackTrace();
            } catch (MalformedURLException ex1) {
                ex1.printStackTrace();
            } catch (SAXException ex1) {
                ex1.printStackTrace();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
            Cache.getInstance().putInCache("list__read_xml", studenten);
        }
        return studenten;
    }

    private void readXmlDocument(final List<Utils.JavaStudentInfo> studenten, final SchemaFactory schemaFactory) throws IOException, SAXException, MalformedURLException, ParserConfigurationException {
        DocumentBuilderFactory builder;
        Schema schema;
        String xls = SERVERADRESS + "Files/studentenlijst.xsd";
        schema = schemaFactory.newSchema(new URL(xls));
        builder = DocumentBuilderFactory.newInstance();
        builder.setValidating(false);
        builder.setIgnoringComments(true);
        builder.setNamespaceAware(true);
        builder.setValidating(true);
        builder.setSchema(schema);
        DocumentBuilder docBuild = null;
        docBuild = builder.newDocumentBuilder();
        String xml = SERVERADRESS + "Files/studentenlijst.xml";
        Document doc = docBuild.parse(xml);
        NodeList nl = doc.getElementsByTagName("student");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                Element el = (Element) nl.item(i);
                Utils.JavaStudentInfo e = getStudentInfo(el);
                studenten.add(e);
            }
        }
    }

    private Utils.JavaStudentInfo getStudentInfo(Element el) {
        Utils.JavaStudentInfo student = new Utils.JavaStudentInfo();
        student.setStudentnr(getIntValue(el, "studentnummer"));
        student.setNaam(getTextValue(el, "naam"));
        student.setVoornaam(getTextValue(el, "voornaam"));
        student.setEmail(getTextValue(el, "email"));
        student.setStudiejaar(getTextValue(el, "studiejaar"));
        student.setPaswoord(getTextValue(el, "paswoord"));
        NodeList nl = el.getElementsByTagName("project");
        if (nl != null && nl.getLength() > 0) {
            student.setGeindividualiseerdTraject(true);
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < nl.getLength(); i++) {
                Element projID = (Element) (nl.item(i));
                list.add(Integer.parseInt(projID.getAttribute("id")));
            }
            student.setProject(list);
        } else {
            student.setGeindividualiseerdTraject(false);
        }
        return student;
    }

    private int getIntValue(Element ele, String tagName) {
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }
        return textVal;
    }
}

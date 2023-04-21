package fhj.itm05.seminarswe.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import fhj.itm05.seminarswe.database.CategoryDAOHsqldb;
import fhj.itm05.seminarswe.domain.Category;

/**
 * Exports categories from logged in user to a xml file
 * @author Pucher A. (with help from Gert and Harald)
 * @version 1.0
 * 
 */
public class CategoryExportController implements Controller {

    ByteArrayOutputStream os = new ByteArrayOutputStream();

    public Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws FileNotFoundException {
        String xmlSkeleton = "<?xml version='1.0' encoding='UTF-8'?>";
        xmlSkeleton += "<xsd:list xmlns:xsd=\"urn:nonstandard:test\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:nonstandard:test CategoriesSchema.xsd\">";
        Document document;
        List<Category> categoryList;
        UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
        String user = userSession.getUserData().getUserName();
        categoryList = CategoryDAOHsqldb.getInstance().listCategoryBy(user, "1");
        for (int i = 0; i < categoryList.size(); i++) {
            xmlSkeleton += "<xsd:category><xsd:cat_id></xsd:cat_id><xsd:cat_name></xsd:cat_name>" + "<xsd:cat_description></xsd:cat_description><xsd:userdata_username></xsd:userdata_username></xsd:category>";
        }
        xmlSkeleton += "</xsd:list>";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new ByteArrayInputStream(xmlSkeleton.getBytes()));
            BuildXML(document, categoryList);
            WriteXML(document);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        Map<String, Object> categoriesxml = new HashMap<String, Object>();
        categoriesxml.put("categoriesxmlkey", os.toString());
        System.out.println("OutputStream os - categoriesxmlkey: " + os.toString());
        return categoriesxml;
    }

    private void BuildXML(Document document, List<Category> categoryList) {
        NodeList categoryNodeList;
        NodeList childNodeList;
        categoryNodeList = document.getElementsByTagName("xsd:category");
        for (int i = 0; i < categoryNodeList.getLength(); i++) {
            childNodeList = categoryNodeList.item(i).getChildNodes();
            childNodeList.item(0).setTextContent(Long.toString(categoryList.get(i).getId()));
            childNodeList.item(1).setTextContent(categoryList.get(i).getName());
            childNodeList.item(2).setTextContent(categoryList.get(i).getDescription());
            childNodeList.item(3).setTextContent(categoryList.get(i).getUsername());
        }
    }

    private void WriteXML(Document document) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(os);
            transformer.transform(source, result);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}

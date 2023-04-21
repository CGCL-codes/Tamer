package Kiddies;

import com.sun.org.apache.xpath.internal.functions.FuncBoolean;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.w3c.dom.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
@WebService()
public class KiddiesWeb {

    public String result = null;

    public String xmlOutWriter = null;

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addKid")
    public void addKid(@WebParam(name = "firstName") String firstName, @WebParam(name = "lastName") String lastName, @WebParam(name = "age") String age) {
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "test")
    public String test(String inputText) {
        return "KiddiesWeb says: " + inputText;
    }

    @WebMethod(operationName = "addContact")
    public void addContact(@WebParam(name = "lastname") String lastname, @WebParam(name = "initials") String initials, @WebParam(name = "email") String email, @WebParam(name = "phone") String phone, @WebParam(name = "mobile") String mobile, @WebParam(name = "address") String address, @WebParam(name = "housenumber") String housenumber, @WebParam(name = "zipcode") String zipcode, @WebParam(name = "city") String city) {
        MysqlDataSource msds = new MysqlDataSource();
        msds.setURL("jdbc:mysql://localhost:3306/kiddies");
        msds.setUser("root");
        msds.setPassword("im5772");
        try {
            lastname = "Leider";
            initials = "D.A.";
            email = "leider@gmail.com";
            phone = "1234567890";
            mobile = "1234567890";
            address = "accstraat";
            housenumber = "11";
            zipcode = "1212AA";
            city = "merel";
            Statement st = null;
            ResultSet rsSelect = null;
            Connection conn = null;
            conn = (Connection) msds.getConnection();
            st = (Statement) conn.createStatement();
            st.executeUpdate(String.format("INSERT INTO contact values ('%s','%s','%s','%s','%s','%s','%s','%s','%s')", lastname, initials, email, phone, mobile, address, housenumber, zipcode, city));
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    @WebMethod(operationName = "selectContact")
    public String selectContact() {
        MysqlDataSource msds = new MysqlDataSource();
        msds.setURL("jdbc:mysql://localhost:3306/kiddies");
        msds.setUser("root");
        msds.setPassword("im5772");
        try {
            Statement st = null;
            ResultSet rsSelect = null;
            Connection conn = null;
            conn = (Connection) msds.getConnection();
            st = (Statement) conn.createStatement();
            rsSelect = st.executeQuery("SELECT * FROM contact;");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document doc = parser.newDocument();
            Element root = doc.createElement("root");
            doc.appendChild(root);
            Element childElement = doc.createElement("contacts");
            root.appendChild(childElement);
            while (rsSelect.next()) {
                childElement = doc.createElement("contact");
                childElement.setAttribute("lastname", rsSelect.getString("LASTNAME"));
                childElement.setAttribute("initials", rsSelect.getString("INITIALS"));
                root.appendChild(childElement);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            Source source = new DOMSource(doc);
            StringWriter xmlOut = new StringWriter();
            transformer.transform(source, new StreamResult(xmlOut));
            xmlOutWriter = xmlOut.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        return xmlOutWriter.toString();
    }
}

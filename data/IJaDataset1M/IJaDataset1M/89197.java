package reports.utility.datamodel.administration;

import org.hibernate.*;
import java.sql.*;

/**
 *
 * @author Administrator
 */
public class PATRON_CATEGORY_MANAGER {

    /** Creates a new instance of PATRON_CATEGORY_MANAGER */
    public PATRON_CATEGORY_MANAGER() {
    }

    public PATRON_CATEGORY load(org.hibernate.Session session, PATRON_CATEGORY_KEY pkey) {
        return (PATRON_CATEGORY) session.load(PATRON_CATEGORY.class, pkey);
    }

    public Integer getMaxPatId(Connection con) {
        Integer result = null;
        try {
            Statement stmt = con.createStatement();
            String query = "select max(patron_category_id) from patron_category where library_id=" + reports.utility.StaticValues.getInstance().getLoginLibraryId();
            ResultSet rs = stmt.executeQuery(query);
            int pid = 1;
            while (rs.next()) {
                pid = rs.getInt(1) + 1;
            }
            rs.close();
            stmt.close();
            result = new Integer(pid);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return result;
    }

    public Integer getMaxPatId() {
        org.jdom.Element root = new org.jdom.Element("OperationId");
        org.jdom.Element table = new org.jdom.Element("Table");
        org.jdom.Element libraryid = new org.jdom.Element("LibraryId");
        table.setText("PATRON_CATEGORY");
        libraryid.setText("" + reports.utility.StaticValues.getInstance().getLoginLibraryId());
        root.addContent(table);
        root.addContent(libraryid);
        root.setAttribute("no", "29");
        org.jdom.Document doc = new org.jdom.Document(root);
        org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
        java.io.StringWriter sw = new java.io.StringWriter();
        try {
            out.output(doc, sw);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        String xmlStr = sw.toString();
        System.out.println("xml slsb xmlStr : " + xmlStr);
        String xmlStr1 = tools.ServletConnector.getInstance().sendRequest("ReportMainServlet", xmlStr);
        System.out.println(xmlStr1);
        org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
        org.jdom.Document document = null;
        try {
            document = sb.build(new java.io.StringReader(xmlStr1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int maxPatCatId = Integer.parseInt(document.getRootElement().getChild("Id").getText());
        return new Integer(maxPatCatId);
    }
}

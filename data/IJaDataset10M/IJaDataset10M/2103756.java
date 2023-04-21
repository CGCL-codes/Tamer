package reports.utility.datamodel.administration;

import java.util.List;
import org.hibernate.Session;
import java.sql.*;

/**
 *
 * @author Administrator
 */
public class COURSE_MANAGER {

    /** Creates a new instance of COURSE_MANAGER */
    public COURSE_MANAGER() {
    }

    public COURSE getCourse(Session session, COURSE_KEY primaryKey) {
        COURSE course = (COURSE) session.load(COURSE.class, primaryKey);
        return course;
    }

    public List getCourses(Session session, String course) {
        List list = session.createQuery("from COURSE where course_name='" + course + "'").list();
        return list;
    }

    public Integer getMaxCourseId(Connection con) {
        Integer result = null;
        try {
            Statement stmt = con.createStatement();
            String query = "select max(course_id) from course where library_id=" + reports.utility.StaticValues.getInstance().getLoginLibraryId();
            ResultSet rs = stmt.executeQuery(query);
            int cid = 1;
            while (rs.next()) {
                cid = rs.getInt(1) + 1;
            }
            rs.close();
            stmt.close();
            result = new Integer(cid);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return result;
    }

    public Integer getMaxCourseId() {
        org.jdom.Element root = new org.jdom.Element("OperationId");
        org.jdom.Element table = new org.jdom.Element("Table");
        org.jdom.Element libraryid = new org.jdom.Element("LibraryId");
        table.setText("COURSE");
        libraryid.setText("" + reports.utility.StaticValues.getInstance().getLoginLibraryId());
        root.addContent(table);
        root.addContent(libraryid);
        root.setAttribute("no", "28");
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
        org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
        org.jdom.Document document = null;
        try {
            document = sb.build(new java.io.StringReader(xmlStr1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int maxCourseId = Integer.parseInt(document.getRootElement().getChild("Id").getText());
        return new Integer(maxCourseId);
    }
}

package educate.sis.registration;

import java.sql.Statement;
import javax.servlet.http.HttpSession;
import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.util.CDate;
import lebah.util.DateTool;
import org.apache.velocity.Template;

public class DeleteStudentModule extends lebah.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        String submit = getParam("command");
        String template_name = prepareTemplate(submit);
        Template template = engine.getTemplate(template_name);
        return template;
    }

    String prepareTemplate(String submit) throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/sis/register/delete_student.vm";
        String studentId = getParam("student_id");
        context.put("student_id", studentId);
        context.put("deleted", Boolean.FALSE);
        if ("deleteStudent".equals(submit)) {
            deleteStudent(session, studentId);
            context.put("deleted", Boolean.TRUE);
        }
        return template_name;
    }

    void deleteStudent(HttpSession session, String studentId) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            {
                String remoteAddr = request.getRemoteAddr();
                String username = (String) session.getAttribute("_portal_login");
                String logDescription = "DELETE STUDENT " + studentId;
                r.clear();
                r.add("log_date", DateTool.getCurrentDate());
                r.add("ip_address", remoteAddr);
                r.add("username", username);
                r.add("description", logDescription);
                sql = r.getSQLInsert("sis_log");
                stmt.executeUpdate(sql);
            }
            {
                r.clear();
                r.add("id", studentId);
                sql = r.getSQLDelete("student");
                stmt.executeUpdate(sql);
            }
            {
                r.clear();
                r.add("student_id", studentId);
                sql = r.getSQLDelete("student_course");
                stmt.executeUpdate(sql);
            }
            {
                r.clear();
                r.add("student_id", studentId);
                sql = r.getSQLDelete("student_billing");
                stmt.executeUpdate(sql);
            }
            {
                r.clear();
                r.add("student_id", studentId);
                sql = r.getSQLDelete("student_billing_detail");
                stmt.executeUpdate(sql);
            }
            {
                r.clear();
                r.add("student_id", studentId);
                sql = r.getSQLDelete("student_receipt");
                stmt.executeUpdate(sql);
            }
            {
                r.clear();
                r.add("student_id", studentId);
                sql = r.getSQLDelete("student_receipt_detail");
                stmt.executeUpdate(sql);
            }
        } finally {
            if (db != null) db.close();
        }
    }
}

package educate.sis.admission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import lebah.db.Db;
import lebah.db.DbException;
import lebah.db.SQLRenderer;
import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class SetupQualifierModule extends lebah.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/sis/setup_qualifier.vm";
        String submit = getParam("command");
        if ("add".equals(submit)) {
            add();
        } else if ("delete".equals(submit)) {
            delete();
        }
        Vector v = list();
        context.put("qualifierVector", v);
        Vector subjectList = SubjectDb.getList();
        context.put("subjectList", subjectList);
        Vector examInfo = SubjectDb.getExamInfo();
        session.setAttribute("examInfo", examInfo);
        context.put("examInfo", examInfo);
        Hashtable displayGrade = SubjectDb.getDisplayGrade();
        context.put("displayGrade", displayGrade);
        Template template = engine.getTemplate(template_name);
        return template;
    }

    private Vector list() throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("f.qualifier_id");
            r.add("f.adm_subject_id");
            r.add("s.adm_subject_name");
            r.add("f.qualifier_subject_grade");
            r.add("f.qualifier_operator");
            r.add("g.adm_grade_display");
            r.add("f.adm_subject_id", r.unquote("s.adm_subject_id"));
            r.add("s.adm_grade_display_id", r.unquote("g.adm_grade_display_id"));
            r.add("f.qualifier_subject_grade", r.unquote("g.adm_grade_value"));
            sql = r.getSQLSelect("adm_qualifier_factor f, adm_exam_subject s, adm_display_grade g", "qualifier_id");
            ResultSet rs = stmt.executeQuery(sql);
            Vector v = new Vector();
            while (rs.next()) {
                Hashtable h = new Hashtable();
                h.put("qualifier_id", rs.getString("qualifier_id"));
                h.put("subject_id", rs.getString("adm_subject_id"));
                h.put("subject_name", rs.getString("adm_subject_name"));
                h.put("subject_grade", rs.getString("qualifier_subject_grade"));
                String operator = rs.getString("qualifier_operator");
                h.put("operator", operator);
                h.put("grade_display", rs.getString("adm_grade_display"));
                v.addElement(h);
            }
            return v;
        } catch (DbException dbex) {
            throw dbex;
        } catch (SQLException sqlex) {
            throw sqlex;
        } finally {
            if (db != null) db.close();
        }
    }

    private void add() throws Exception {
        String qualifier_id = getParam("qualifier_id");
        String subject_id = getParam("subject_id");
        String subject_grade = getParam("subject_grade");
        String qualifier_operator = getParam("qualifier_operator");
        if ("".equals(qualifier_id)) throw new Exception("Can not have empty fields!");
        if ("".equals(subject_id)) throw new Exception("Can not have empty fields!");
        if ("".equals(subject_grade)) throw new Exception("Can not have empty fields!");
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            r.add("qualifier_id");
            r.add("qualifier_id", qualifier_id);
            sql = r.getSQLSelect("adm_qualifier_factor");
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) found = true;
            if (found) throw new Exception("Qualifier Id already exists!");
            r.clear();
            r.add("qualifier_id", qualifier_id.trim().toUpperCase());
            r.add("adm_subject_id", subject_id);
            r.add("qualifier_subject_grade", subject_grade);
            r.add("qualifier_operator", qualifier_operator);
            sql = r.getSQLInsert("adm_qualifier_factor");
            stmt.executeUpdate(sql);
        } catch (DbException dbex) {
            throw dbex;
        } catch (SQLException sqlex) {
            throw sqlex;
        } finally {
            if (db != null) db.close();
        }
    }

    private void delete() throws Exception {
        String qualifier_id = getParam("qualifier_id");
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            sql = "DELETE FROM adm_qualifier_factor WHERE qualifier_id = '" + qualifier_id + "'";
            stmt.executeUpdate(sql);
        } finally {
            if (db != null) db.close();
        }
    }
}

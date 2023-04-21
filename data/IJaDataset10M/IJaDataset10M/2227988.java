package mecca.sis.admission;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import mecca.db.Db;
import mecca.db.SQLRenderer;
import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class SetupGradeDisplayModule extends mecca.portal.velocity.VTemplate {

    private int current_group = 0;

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/sis/setup_grade_display.vm";
        String submit = getParam("command");
        String grade_display_id = getParam("grade_display_id");
        String grade_display_name = getParam("grade_display_name");
        context.put("grade_display_id", grade_display_id);
        context.put("grade_display_name", grade_display_name);
        if ("add".equals(submit)) {
            add();
        } else if ("delete".equals(submit)) {
            delete();
        } else if ("createnew".equals(submit)) {
            createnew();
        } else if ("updatename".equals(submit)) {
            updatename();
            context.put("grade_display_name", grade_display_name);
        }
        Vector list = getListMain();
        context.put("list", list);
        Vector gradeList = new Vector();
        Hashtable gradeDetail = new Hashtable();
        prepareGradeList(gradeList, gradeDetail);
        context.put("gradeList", gradeList);
        context.put("gradeDetail", gradeDetail);
        Template template = engine.getTemplate(template_name);
        return template;
    }

    private Vector getListMain() throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("adm_grade_display_id");
            r.add("adm_grade_display_name");
            sql = r.getSQLSelect("adm_display_grade_main");
            ResultSet rs = stmt.executeQuery(sql);
            Vector v = new Vector();
            while (rs.next()) {
                Hashtable h = new Hashtable();
                h.put("id", rs.getString("adm_grade_display_id"));
                h.put("name", rs.getString("adm_grade_display_name"));
                v.addElement(h);
            }
            return v;
        } finally {
            if (db != null) db.close();
        }
    }

    private void createnew() throws Exception {
        String grade_display_id = getParam("grade_display_id");
        String grade_display_name = getParam("grade_display_name");
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("adm_grade_display_id");
            r.add("adm_grade_display_id", grade_display_id);
            sql = r.getSQLSelect("adm_display_grade_main");
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) throw new Exception("Grade Id already exists!");
            r.clear();
            r.add("adm_grade_display_id", grade_display_id);
            r.add("adm_grade_display_name", grade_display_name);
            sql = r.getSQLInsert("adm_display_grade_main");
            stmt.executeUpdate(sql);
        } finally {
            if (db != null) db.close();
        }
    }

    private void updatename() throws Exception {
        String grade_display_id = getParam("grade_display_id");
        String grade_display_name = getParam("grade_display_name");
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("adm_grade_display_name", grade_display_name);
            r.update("adm_grade_display_id", grade_display_id);
            sql = r.getSQLUpdate("adm_display_grade_main");
            stmt.executeUpdate(sql);
        } finally {
            if (db != null) db.close();
        }
    }

    private void prepareGradeList(Vector gradeList, Hashtable gradeDetail) throws Exception {
        String grade_display_id = getParam("grade_display_id");
        if ("".equals(grade_display_id)) return;
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            {
                r.add("adm_grade_display_name");
                r.add("adm_grade_display_id", grade_display_id);
                sql = r.getSQLSelect("adm_display_grade_main");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    gradeDetail.put("id", grade_display_id);
                    gradeDetail.put("name", rs.getString("adm_grade_display_name"));
                }
            }
            r.clear();
            r.add("adm_grade_value");
            r.add("adm_grade_display");
            r.add("adm_grade_display_id", grade_display_id);
            sql = r.getSQLSelect("adm_display_grade", "adm_grade_value");
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Hashtable h = new Hashtable();
                h.put("value", rs.getString("adm_grade_value"));
                h.put("display", rs.getString("adm_grade_display"));
                gradeList.addElement(h);
            }
        } finally {
            if (db != null) db.close();
        }
    }

    private void add() throws Exception {
        String grade_display_id = getParam("grade_display_id");
        String grade_value = getParam("grade_value");
        String grade_display = getParam("grade_display");
        if ("".equals(grade_display)) throw new Exception("Can not have empty fields!");
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            {
                r.clear();
                r.add("adm_grade_value");
                r.add("adm_grade_value", Integer.parseInt(grade_value));
                r.add("adm_grade_display_id", grade_display_id);
                sql = r.getSQLSelect("adm_display_grade");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true;
            }
            if (found) {
                r.clear();
                r.add("adm_grade_display", grade_display);
                r.update("adm_grade_display_id", grade_display_id);
                r.update("adm_grade_value", Integer.parseInt(grade_value));
                sql = r.getSQLUpdate("adm_display_grade");
                stmt.executeUpdate(sql);
            } else {
                r.clear();
                r.add("adm_grade_display_id", grade_display_id);
                r.add("adm_grade_value", Integer.parseInt(grade_value));
                r.add("adm_grade_display", grade_display);
                sql = r.getSQLInsert("adm_display_grade");
                stmt.executeUpdate(sql);
            }
        } finally {
            if (db != null) db.close();
        }
    }

    private void delete() throws Exception {
        String grade_display_id = getParam("grade_display_id");
        String qualifier_id = getParam("qualifier_id");
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
        } finally {
            if (db != null) db.close();
        }
    }
}

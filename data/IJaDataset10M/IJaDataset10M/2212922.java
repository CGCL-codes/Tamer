package mecca.sis.admission;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import mecca.db.Db;
import mecca.db.DbException;
import mecca.db.SQLRenderer;
import mecca.general.CountryData;
import mecca.sis.struct.ProgramData;
import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ListApplicantModule extends mecca.portal.velocity.VTemplate {

    protected boolean isLastPage = false;

    protected int LIST_ROWS = 10;

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/sis/add_applicant.vm";
        context.put("unique_id", "");
        String submit = getParam("command");
        if ("add".equals(submit)) {
            Hashtable applicantInfo = new Hashtable();
            add(applicantInfo);
            context.put("applicantInfo", applicantInfo);
            template_name = "vtl/sis/applicant_data.vm";
        } else if ("edit".equals(submit)) {
            String applicant_id = getParam("applicant_id");
            Hashtable applicantInfo = new Hashtable();
            applicantInfo = getApplicant(applicant_id);
            context.put("applicantInfo", applicantInfo);
        } else if ("getdata".equals(submit)) {
            String applicant_id = getParam("applicant_id");
            Hashtable applicantInfo = new Hashtable();
            applicantInfo = getApplicant(applicant_id);
            context.put("applicantInfo", applicantInfo);
        } else if ("academic".equals(submit)) {
            template_name = "vtl/sis/applicant_exam_data.vm";
            context.put("applicantData", new Hashtable());
            context.put("examDetail", new Hashtable());
            Vector examInfo = SubjectDb.getExamInfo();
            session.setAttribute("examInfo", examInfo);
            context.put("examInfo", examInfo);
            String applicant_id = getParam("applicant_id");
            if (!"".equals(applicant_id)) {
                Hashtable applicantDetail = new Hashtable();
                Hashtable examDetail = new Hashtable();
                ExamResultData.list(applicant_id, applicantDetail, examDetail);
                context.put("applicantData", applicantDetail);
                context.put("examDetail", examDetail);
            }
        } else if ("academic_save".equals(submit)) {
            template_name = "vtl/sis/applicant_exam_data.vm";
            academic_save(session);
            String applicant_id = getParam("applicant_id");
            if (!"".equals(applicant_id)) {
                Hashtable applicantDetail = new Hashtable();
                Hashtable examDetail = new Hashtable();
                ExamResultData.list(applicant_id, applicantDetail, examDetail);
                context.put("applicantData", applicantDetail);
                context.put("examDetail", examDetail);
            }
        } else if ("course".equals(submit)) {
            template_name = "vtl/sis/applicant_select_course.vm";
            String applicant_id = getParam("applicant_id");
            Hashtable applicantInfo = new Hashtable();
            applicantInfo = getApplicant(applicant_id);
            context.put("applicantInfo", applicantInfo);
            Vector programList = ProgramData.getProgramList();
            context.put("programList", programList);
            Hashtable programSelected = getProgramSelected();
            context.put("programSelected", programSelected);
            context.put("savechoice", "false");
        } else if ("savechoice".equals(submit)) {
            template_name = "vtl/sis/applicant_select_course.vm";
            Hashtable programSelected = new Hashtable();
            saveChoiceProgram(programSelected);
            context.put("programSelected", programSelected);
            context.put("savechoice", "true");
        } else if ("delete".equals(submit)) {
            delete();
            prepareList(session);
            int page = Integer.parseInt(getParam("pagenum"));
            getRows(session, page);
            session.setAttribute("page_number", Integer.toString(page));
            template_name = "vtl/sis/list_applicant.vm";
        } else if ("list".equals(submit)) {
            prepareList(session);
            template_name = "vtl/sis/list_applicant.vm";
        } else if ("goPage".equals(submit)) {
            int page = Integer.parseInt(getParam("pagenum"));
            template_name = "vtl/sis/list_applicant.vm";
            getRows(session, page);
            session.setAttribute("page_number", Integer.toString(page));
        } else {
            Hashtable applicantInfo = new Hashtable();
            applicantInfo.put("id", "");
            context.put("applicantInfo", applicantInfo);
            String unique_id = mecca.sis.tools.UniqueStringId.get();
            context.put("unique_id", unique_id);
            prepareList(session);
            template_name = "vtl/sis/list_applicant.vm";
        }
        Vector countryList = CountryData.getList();
        context.put("countryList", countryList);
        Template template = engine.getTemplate(template_name);
        return template;
    }

    void prepareList(HttpSession session) throws Exception {
        Vector list = list();
        int pages = list.size() / LIST_ROWS;
        double leftover = ((double) list.size() % (double) LIST_ROWS);
        if (leftover > 0.0) ++pages;
        context.put("pages", new Integer(pages));
        session.setAttribute("pages", new Integer(pages));
        session.setAttribute("applicantList", list);
        getRows(session, 1);
        session.setAttribute("page_number", "1");
        context.put("page_number", new Integer(1));
        context.put("applicantList", list);
    }

    void getRows(HttpSession session, int page) throws Exception {
        Vector list = (Vector) session.getAttribute("applicantList");
        Vector items = getPage(page, LIST_ROWS, list);
        context.put("items", items);
        context.put("page_number", new Integer(page));
        context.put("pages", (Integer) session.getAttribute("pages"));
    }

    Vector getPage(int page, int size, Vector list) throws Exception {
        int elementstart = (page - 1) * size;
        int elementlast = 0;
        if (page * size < list.size()) {
            elementlast = (page * size) - 1;
            isLastPage = false;
            context.put("eol", new Boolean(false));
        } else {
            elementlast = list.size() - 1;
            isLastPage = true;
            context.put("eol", new Boolean(true));
        }
        if (page == 1) context.put("bol", new Boolean(true)); else context.put("bol", new Boolean(false));
        Vector v = new Vector();
        for (int i = elementstart; i < elementlast + 1; i++) {
            v.addElement(list.elementAt(i));
        }
        return v;
    }

    private String fmt(String s) {
        s = s.trim();
        if (s.length() == 1) return "0".concat(s); else return s;
    }

    private void add(Hashtable applicantInfo) throws Exception {
        String mode = "".equals(getParam("applicant_id_gen").trim()) ? "update" : "insert";
        String applicant_id = getParam("applicant_id");
        String password = getParam("password");
        if ("".equals(applicant_id)) applicant_id = getParam("applicant_id_gen");
        if ("".equals(getParam("applicant_name"))) throw new Exception("Can not have empty fields!");
        applicantInfo.put("id", applicant_id);
        applicantInfo.put("password", password);
        applicantInfo.put("name", getParam("applicant_name"));
        applicantInfo.put("address1", getParam("address1"));
        applicantInfo.put("address2", getParam("address2"));
        applicantInfo.put("address3", getParam("address3"));
        applicantInfo.put("city", getParam("city"));
        applicantInfo.put("state", getParam("state"));
        applicantInfo.put("poscode", getParam("poscode"));
        applicantInfo.put("country_code", getParam("country_list"));
        applicantInfo.put("email", getParam("email"));
        applicantInfo.put("phone", getParam("phone"));
        String birth_year = getParam("birth_year");
        String birth_month = getParam("birth_month");
        String birth_day = getParam("birth_day");
        applicantInfo.put("birth_year", birth_year);
        applicantInfo.put("birth_month", birth_month);
        applicantInfo.put("birth_day", birth_day);
        applicantInfo.put("gender", getParam("gender"));
        String birth_date = birth_year + "-" + fmt(birth_month) + "-" + fmt(birth_day);
        applicantInfo.put("birth_date", birth_date);
        Db db = null;
        String sql = "";
        Connection conn = null;
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            {
                r.add("applicant_id");
                r.add("applicant_id", (String) applicantInfo.get("id"));
                sql = r.getSQLSelect("adm_applicant");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true; else found = false;
            }
            if (found && !"update".equals(mode)) throw new Exception("Applicant Id was invalid!");
            {
                r.clear();
                r.add("password", (String) applicantInfo.get("password"));
                r.add("applicant_name", (String) applicantInfo.get("name"));
                r.add("address1", (String) applicantInfo.get("address1"));
                r.add("address2", (String) applicantInfo.get("address2"));
                r.add("address3", (String) applicantInfo.get("address3"));
                r.add("city", (String) applicantInfo.get("city"));
                r.add("state", (String) applicantInfo.get("state"));
                r.add("poscode", (String) applicantInfo.get("poscode"));
                r.add("country_code", (String) applicantInfo.get("country_code"));
                r.add("phone", (String) applicantInfo.get("phone"));
                r.add("birth_date", (String) applicantInfo.get("birth_date"));
                r.add("gender", (String) applicantInfo.get("gender"));
            }
            if (!found) {
                r.add("applicant_id", (String) applicantInfo.get("id"));
                sql = r.getSQLInsert("adm_applicant");
                stmt.executeUpdate(sql);
            } else {
                r.update("applicant_id", (String) applicantInfo.get("id"));
                sql = r.getSQLUpdate("adm_applicant");
                stmt.executeUpdate(sql);
            }
            conn.commit();
        } catch (DbException dbex) {
            throw dbex;
        } catch (SQLException sqlex) {
            try {
                conn.rollback();
            } catch (SQLException rollex) {
            }
            throw sqlex;
        } finally {
            if (db != null) db.close();
        }
    }

    private void prepareRenderer(SQLRenderer r, String id) {
        r.add("applicant_id");
        r.add("password");
        r.add("applicant_name");
        r.add("address1");
        r.add("address2");
        r.add("address3");
        r.add("city");
        r.add("state");
        r.add("poscode");
        r.add("country_code");
        r.add("email");
        r.add("phone");
        r.add("gender");
        r.add("birth_date");
        r.add("apply_date");
        r.add("ip_address");
        if (!"".equals(id)) r.add("applicant_id", id);
    }

    private Hashtable getApplicant(String id) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            prepareRenderer(r, id);
            sql = r.getSQLSelect("adm_applicant");
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return getApplicantData(rs);
            } else {
                return null;
            }
        } catch (DbException dbex) {
            throw dbex;
        } catch (SQLException sqlex) {
            throw sqlex;
        } finally {
            if (db != null) db.close();
        }
    }

    private Vector list() throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            prepareRenderer(r, "");
            sql = r.getSQLSelect("adm_applicant", "apply_date");
            ResultSet rs = stmt.executeQuery(sql);
            Vector v = new Vector();
            while (rs.next()) {
                v.addElement(getApplicantData(rs));
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

    private Hashtable getApplicantData(ResultSet rs) throws Exception {
        Hashtable applicantInfo = new Hashtable();
        applicantInfo.put("id", getString(rs, "applicant_id"));
        applicantInfo.put("password", getString(rs, "password"));
        applicantInfo.put("name", getString(rs, "applicant_name"));
        applicantInfo.put("address1", getString(rs, "address1"));
        applicantInfo.put("address2", getString(rs, "address2"));
        applicantInfo.put("address3", getString(rs, "address3"));
        applicantInfo.put("city", getString(rs, "city"));
        applicantInfo.put("state", getString(rs, "state"));
        applicantInfo.put("poscode", getString(rs, "poscode"));
        applicantInfo.put("country_code", getString(rs, "country_code"));
        applicantInfo.put("email", getString(rs, "email"));
        applicantInfo.put("phone", getString(rs, "phone"));
        applicantInfo.put("gender", getString(rs, "gender"));
        try {
            applicantInfo.put("birth_date", getString(rs, "birth_date"));
            java.util.Date birthDate = rs.getDate("birth_date");
            if (birthDate != null) {
                Calendar c = new java.util.GregorianCalendar();
                c.setTime(birthDate);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                applicantInfo.put("birth_year", new Integer(year));
                applicantInfo.put("birth_month", new Integer(month));
                applicantInfo.put("birth_day", new Integer(day));
            }
            java.util.Date apply_date = rs.getDate("apply_date");
            applicantInfo.put("ip_address", getString(rs, "ip_address"));
            if (apply_date != null) {
                applicantInfo.put("apply_date", mecca.sis.tools.DateTool.getDateFormatted(apply_date));
            } else {
                applicantInfo.put("apply_date", "");
            }
        } catch (Exception e2) {
        }
        return applicantInfo;
    }

    private String getString(ResultSet rs, String name) throws Exception {
        String s = rs.getString(name);
        if (s != null) return s; else return "";
    }

    private Integer getInteger(ResultSet rs, String name) throws Exception {
        String s = rs.getString(name);
        if (s != null) return new Integer(s); else return new Integer(0);
    }

    private void delete() throws Exception {
        String applicant_id = getParam("applicant_id");
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            {
            }
            if (!found) {
                sql = "DELETE FROM adm_applicant WHERE applicant_id = '" + applicant_id + "'";
                stmt.executeUpdate(sql);
            }
        } finally {
            if (db != null) db.close();
        }
    }

    private void academic_save(HttpSession session) throws Exception {
        Hashtable info = new Hashtable();
        info.put("applicant_id", getParam("applicant_id"));
        Vector examInfo = (Vector) session.getAttribute("examInfo");
        for (int i = 0; i < examInfo.size(); i++) {
            Hashtable exam = (Hashtable) examInfo.elementAt(i);
            String exam_id = (String) exam.get("id");
            Vector subjects = (Vector) exam.get("subjects");
            for (int k = 0; k < subjects.size(); k++) {
                Hashtable subject = (Hashtable) subjects.elementAt(k);
                String subject_id = (String) subject.get("id");
                info.put(subject_id, getParam(subject_id));
            }
        }
        ExamResultData.save(examInfo, info);
    }

    private Vector getCourseList() throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            {
                r.clear();
                r.add("course_id");
                r.add("course_code");
                r.add("course_name");
                sql = r.getSQLSelect("study_course");
                ResultSet rs = stmt.executeQuery(sql);
                Vector list = new Vector();
                while (rs.next()) {
                    Hashtable h = new Hashtable();
                    h.put("id", rs.getString("course_id"));
                    h.put("code", rs.getString("course_code"));
                    h.put("name", rs.getString("course_name"));
                    list.addElement(h);
                }
                return list;
            }
        } finally {
            if (db != null) db.close();
        }
    }

    private void savechoice(Hashtable h) throws Exception {
        String applicant_id = getParam("applicant_id");
        String choice1 = getParam("choice1");
        String choice2 = getParam("choice2");
        String choice3 = getParam("choice3");
        h.put("choice1", choice1);
        h.put("choice2", choice2);
        h.put("choice3", choice3);
        if ("".equals(applicant_id)) return;
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            {
                r.add("applicant_id");
                r.add("applicant_id", applicant_id);
                sql = r.getSQLSelect("adm_applicant_choice");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true;
            }
            {
                r.clear();
                r.add("choice1", choice1);
                r.add("choice2", choice2);
                r.add("choice3", choice3);
                if (!found) {
                    r.add("applicant_id", applicant_id);
                    sql = r.getSQLInsert("adm_applicant_choice");
                } else {
                    r.update("applicant_id", applicant_id);
                    sql = r.getSQLUpdate("adm_applicant_choice");
                }
                stmt.executeUpdate(sql);
            }
        } finally {
            if (db != null) db.close();
        }
    }

    private Hashtable getCourseSelected() throws Exception {
        String applicant_id = getParam("applicant_id");
        if ("".equals(applicant_id)) throw new Exception("Missing applicant_id..");
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("choice1");
            r.add("choice2");
            r.add("choice3");
            r.add("applicant_id", applicant_id);
            sql = r.getSQLSelect("adm_applicant_choice");
            ResultSet rs = stmt.executeQuery(sql);
            Hashtable h = new Hashtable();
            if (rs.next()) {
                h.put("choice1", rs.getString("choice1"));
                h.put("choice2", rs.getString("choice2"));
                h.put("choice3", rs.getString("choice3"));
            }
            return h;
        } finally {
            if (db != null) db.close();
        }
    }

    private Hashtable getProgramSelected() throws Exception {
        String applicant_id = getParam("applicant_id");
        if ("".equals(applicant_id)) throw new Exception("Missing applicant_id..");
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("choice1");
            r.add("choice2");
            r.add("choice3");
            r.add("applicant_id", applicant_id);
            sql = r.getSQLSelect("adm_applicant_choice");
            ResultSet rs = stmt.executeQuery(sql);
            Hashtable h = new Hashtable();
            if (rs.next()) {
                h.put("choice1", rs.getString("choice1"));
                h.put("choice2", rs.getString("choice2"));
                h.put("choice3", rs.getString("choice3"));
            }
            return h;
        } finally {
            if (db != null) db.close();
        }
    }

    private void saveChoiceProgram(Hashtable h) throws Exception {
        String applicant_id = getParam("applicant_id");
        String choice1 = getParam("choice1");
        String choice2 = getParam("choice2");
        String choice3 = getParam("choice3");
        h.put("choice1", choice1);
        h.put("choice2", choice2);
        h.put("choice3", choice3);
        if ("".equals(applicant_id)) return;
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            {
                r.add("applicant_id");
                r.add("applicant_id", applicant_id);
                sql = r.getSQLSelect("adm_applicant_choice");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true;
            }
            {
                r.clear();
                r.add("choice1", choice1);
                r.add("choice2", choice2);
                r.add("choice3", choice3);
                if (!found) {
                    r.add("applicant_id", applicant_id);
                    sql = r.getSQLInsert("adm_applicant_choice");
                } else {
                    r.update("applicant_id", applicant_id);
                    sql = r.getSQLUpdate("adm_applicant_choice");
                }
                stmt.executeUpdate(sql);
            }
        } finally {
            if (db != null) db.close();
        }
    }
}

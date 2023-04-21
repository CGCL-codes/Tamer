package educate.sis.admission;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import lebah.db.Db;
import lebah.db.DbException;
import lebah.db.SQLRenderer;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ApplicantData {

    private static String fmt(String s) {
        s = s.trim();
        if (s.length() == 1) return "0".concat(s); else return s;
    }

    public static void add(Hashtable applicantInfo, String mode) throws Exception {
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
                r.add("email", (String) applicantInfo.get("email"));
                r.add("phone", (String) applicantInfo.get("phone"));
                r.add("birth_date", (String) applicantInfo.get("birth_date"));
                r.add("status", "0");
                r.add("status2", "NEW");
            }
            if (!found) {
                r.add("apply_date", r.unquote("now()"));
                r.add("ip_address", (String) applicantInfo.get("ip_address"));
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

    private static void prepareRenderer(SQLRenderer r, String id) {
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
        if (!"".equals(id)) r.add("applicant_id", id);
    }

    public static Hashtable getApplicant(String id) throws Exception {
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

    public static Vector list() throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            prepareRenderer(r, "");
            sql = r.getSQLSelect("adm_applicant", "applicant_name");
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

    private static Hashtable getApplicantData(ResultSet rs) throws Exception {
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
        applicantInfo.put("birth_date", getString(rs, "birth_date"));
        java.util.Date birthDate = rs.getDate("birth_date");
        Calendar c = new java.util.GregorianCalendar();
        c.setTime(birthDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        applicantInfo.put("birth_year", new Integer(year));
        applicantInfo.put("birth_month", new Integer(month));
        applicantInfo.put("birth_day", new Integer(day));
        applicantInfo.put("birth_year", new Integer(year));
        applicantInfo.put("birth_month", new Integer(month));
        applicantInfo.put("birth_day", new Integer(day));
        return applicantInfo;
    }

    private static String getString(ResultSet rs, String name) throws Exception {
        String s = rs.getString(name);
        if (s != null) return s; else return "";
    }

    private Integer getInteger(ResultSet rs, String name) throws Exception {
        String s = rs.getString(name);
        if (s != null) return new Integer(s); else return new Integer(0);
    }

    public static void delete(String applicant_id) throws Exception {
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

    public static void academic_save(String applicant_id, Vector examInfo, Hashtable grade) throws Exception {
        Hashtable info = new Hashtable();
        info.put("applicant_id", applicant_id);
        for (int i = 0; i < examInfo.size(); i++) {
            Hashtable exam = (Hashtable) examInfo.elementAt(i);
            String exam_id = (String) exam.get("id");
            Vector subjects = (Vector) exam.get("subjects");
            for (int k = 0; k < subjects.size(); k++) {
                Hashtable subject = (Hashtable) subjects.elementAt(k);
                String subject_id = (String) subject.get("id");
                info.put(subject_id, grade.get(subject_id));
            }
        }
        ExamResultData.save(examInfo, info);
    }

    public static Vector getCourseList() throws Exception {
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

    public static void savechoice(Hashtable h) throws Exception {
        String applicant_id = (String) h.get("applicant_id");
        String choice1 = (String) h.get("choice1");
        String choice2 = (String) h.get("choice2");
        String choice3 = (String) h.get("choice3");
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

    public static Hashtable getCourseSelected(String applicant_id) throws Exception {
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
}

package educate.sis.exam;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import lebah.db.Db;
import lebah.db.SQLRenderer;
import org.apache.velocity.Template;
import educate.sis.registration.SessionData;
import educate.sis.struct.ProgramData;
import educate.sis.struct.Subject;
import educate.sis.struct.SubjectData;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MarkBySubjectModule extends lebah.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/sis/exam/get_by_subject.vm";
        String submit = getParam("command");
        context.put("new", new Boolean(false));
        Hashtable ses = SessionData.getCurrentSession();
        context.put("ses", ses);
        context.put("programSelected", new Boolean(false));
        boolean display = false;
        if ("getSubject".equals(submit)) {
            context.put("programSelected", new Boolean(true));
            displayData(session, ses);
            display = true;
        } else if ("getProgram".equals(submit)) {
            context.put("programSelected", new Boolean(true));
            String program_code = getParam("program_list_select");
            context.put("program_code", program_code);
            getSubjectList(session, program_code);
            display = false;
        } else if ("saveData".equals(submit)) {
            context.put("programSelected", new Boolean(true));
            saveData(session);
            displayData(session, ses);
            String program_code = getParam("program_list_select");
            context.put("program_code", program_code);
            getSubjectList(session, program_code);
            display = true;
        } else {
            context.put("new", new Boolean(true));
        }
        context.put("display", new Boolean(display));
        Vector sessionList = SessionData.getList();
        context.put("sessionList", sessionList);
        Vector programList = ProgramData.getList();
        context.put("programList", programList);
        Template template = engine.getTemplate(template_name);
        return template;
    }

    void displayData(HttpSession session, Hashtable ses) throws Exception {
        String session_id = getParam("session_list");
        ses = SessionData.getSession(session_id);
        context.put("ses", ses);
        String subject_id = getParam("subject_list");
        context.put("subject_id", subject_id);
        Subject subject = SubjectData.getSubject(subject_id);
        context.put("subjectDetail", subject);
        String program_code = getParam("program_list_select");
        context.put("program_code", program_code);
        Vector parts = StudentMarkData.getSchemeInfo(subject_id, session_id);
        context.put("parts", parts);
        Vector studentList = StudentMarkData.getStudentList(subject_id, session_id, program_code);
        context.put("studentList", studentList);
        String mark_type = getMarkType(subject_id, session_id);
        context.put("mark_type", mark_type);
    }

    void getSubjectList(HttpSession session, String program) throws Exception {
        Vector subjectList = null;
        if ("".equals(program)) subjectList = SubjectData.getList(); else subjectList = SubjectData.getList(program);
        context.put("subjectList", subjectList);
    }

    static String getMarkType(String subject, String session_id) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.clear();
            r.add("m.mark_type");
            r.add("i.session_id", session_id);
            r.add("i.subject_id", subject);
            r.add("i.scheme_id", r.unquote("m.scheme_id"));
            sql = r.getSQLSelect("marking_scheme m, marking_scheme_impl i");
            ResultSet rs = stmt.executeQuery(sql);
            String type = "actual";
            if (rs.next()) {
                type = rs.getString("mark_type");
            }
            return type != null ? type : "actual";
        } finally {
            if (db != null) db.close();
        }
    }

    void saveData(HttpSession session) throws Exception {
        String subject_id = getParam("subject_id");
        String session_id = getParam("session_id");
        String[] students = request.getParameterValues("student_list");
        String[] periods = request.getParameterValues("period_list");
        String[] programs = request.getParameterValues("program_list");
        if (students != null) {
            Db db = null;
            try {
                db = new Db();
                Statement stmt = db.getStatement();
                Vector parts = StudentMarkData.getPartIdList(stmt, session_id, subject_id);
                for (int i = 0; i < students.length; i++) {
                    Mark mark = new Mark();
                    for (int k = 0; k < parts.size(); k++) {
                        String part_id = (String) parts.elementAt(k);
                        String mark_value = getParam("value_" + students[i] + "_" + part_id);
                        String percent_check = getParam("check_" + students[i] + "_" + part_id);
                        String mark_type = getParam("type_" + students[i]);
                        mark.setValue(part_id, Double.parseDouble(mark_value));
                        double percent_mark = Double.parseDouble(percent_check);
                        mark.setPercent(part_id, percent_mark);
                        mark.setType(mark_type);
                    }
                    Hashtable gradingScheme = GradingSchemeData.getSchemeInfo(session_id, programs[i]);
                    Vector grades = (Vector) gradingScheme.get("grades");
                    StudentMarkData.addMark(stmt, students[i], subject_id, periods[i], mark, grades);
                }
            } finally {
                if (db != null) db.close();
            }
        }
    }

    void getProgramCode(String student_id) throws Exception {
    }
}

package educate.sis.exam;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import educate.sis.admission2.StudentStatistic;
import educate.sis.struct.Subject;
import lebah.db.Db;
import lebah.db.SQLRenderer;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class StudentMarkData {

    public static Hashtable getMarkInfo2(String student, String session_id, String period, String program, String course_id, boolean showExempted, boolean showIncomplete) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            Hashtable<String, Integer> seqMap = new Hashtable<String, Integer>();
            {
                sql = r.reset().add("subject_id").add("sequence_no").add("program_code", program).add("course_id", course_id).add("period_id", period).getSQLSelect("course_structure");
                ;
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    seqMap.put(rs.getString(1), rs.getInt(2));
                }
            }
            Hashtable gradingScheme = GradingSchemeData.getSchemeInfo(session_id, program);
            Vector grades = (Vector) gradingScheme.get("grades");
            Hashtable markInfo = new Hashtable();
            markInfo.put("grades", grades);
            Vector<Hashtable> subjectList = new Vector<Hashtable>();
            {
                r.reset().add("s.student_id", student).add("s.period_id", period).add("s.program_code", program).add("s.course_id", course_id).add("s.subject_id", r.unquote("fs.subject_id")).add("s.register_id", r.unquote("sr.status_id")).add("s.subject_id").add("fs.subject_code").add("fs.subject_name").add("fs.credit_hrs").add("fs.exclude_gpa").add("sr.status_category").add("sr.status_name");
                if (showExempted && showIncomplete) {
                    r.add("sr.status_category", 4, "<");
                    r.add("sr.status_category", 2, "<>");
                } else if (showExempted && !showIncomplete) {
                    r.add("sr.status_category", 2, "<");
                } else if (!showExempted && showIncomplete) {
                    r.add("sr.status_category", 4, "<");
                    r.add("sr.status_category", 1, "<>");
                } else {
                    r.add("sr.status_category", 0);
                }
                sql = r.getSQLSelect("student_subject s, subject_reg_status sr, faculty_subject fs");
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String subject_id = rs.getString("subject_id");
                    String subject_code = rs.getString("subject_code");
                    String subject_name = rs.getString("subject_name");
                    int credit_hrs = rs.getInt("credit_hrs");
                    int status = rs.getInt("status_category");
                    String status_name = rs.getString(("status_name"));
                    Hashtable<String, Object> h = new Hashtable<String, Object>();
                    h.put("subject_id", subject_id);
                    h.put("subject_code", subject_code);
                    h.put("subject_name", subject_name);
                    h.put("credit_hrs", credit_hrs);
                    int exclude_gpa = rs.getInt("exclude_gpa");
                    h.put("calculate_gpa", exclude_gpa == 0 ? "yes" : "no");
                    h.put("sequence_no", seqMap.get(subject_id) != null ? seqMap.get(subject_id) : 99);
                    h.put("status", status);
                    h.put("status_name", status_name);
                    subjectList.addElement(h);
                }
                Collections.sort(subjectList, new StudentMarkData.SubjectSequenceComparator());
                markInfo.put("subjectList", subjectList);
            }
            Hashtable info = new Hashtable();
            int totalCalculatedCreditHrs = 0;
            double totalGradePoint = 0.0;
            int totalCreditHrs = 0;
            double totalCalculatedGradePoint = 0.0;
            for (int i = 0; i < subjectList.size(); i++) {
                Hashtable h = (Hashtable) subjectList.elementAt(i);
                String subject = (String) h.get("subject_id");
                String calculateGpa = (String) h.get("calculate_gpa");
                int status = (Integer) h.get("status");
                String status_name = (String) h.get("status_name");
                if (status == 0 || status == 1 || status == 3) {
                    double total = 0.0;
                    Mark studentMark = getMark2(db.getStatement(), student, subject, period);
                    Mark mark = new Mark();
                    mark.setGradeScheme(grades);
                    Vector parts = getSchemeInfo2(db.getStatement(), subject, session_id);
                    if (parts != null) {
                        for (int k = 0; k < parts.size(); k++) {
                            MarkingPart part = (MarkingPart) parts.elementAt(k);
                            mark.addMarkingPart(part);
                            double mark_value = studentMark.getValue(part.getId());
                            mark.setValue(part.getId(), mark_value);
                        }
                    } else {
                    }
                    double point = 0.0;
                    String grade = "";
                    {
                        r.reset().add("mark_total").add("mark_grade").add("mark_point").add("student_id", student).add("period_id", period).add("subject_id", subject);
                        sql = r.getSQLSelect("student_marks_total");
                        ResultSet rs = stmt.executeQuery(sql);
                        if (rs.next()) {
                            total = rs.getDouble("mark_total");
                            grade = rs.getString("mark_grade");
                            if (grade == null) grade = "";
                            point = rs.getDouble("mark_point");
                        }
                    }
                    mark.setTotal(total);
                    mark.setPoint(point);
                    mark.setGrade(grade);
                    {
                        r.reset().add("m.mark_type").add("i.session_id", session_id).add("i.subject_id", subject).add("i.scheme_id", r.unquote("m.scheme_id"));
                        sql = r.getSQLSelect("marking_scheme m, marking_scheme_impl i");
                        ResultSet rs = stmt.executeQuery(sql);
                        String type = "actual";
                        if (rs.next()) {
                            type = rs.getString("mark_type");
                        }
                        mark.setType(type != null ? type : "actual");
                    }
                    int credit_hrs = h.get("credit_hrs") != null ? ((Integer) h.get("credit_hrs")).intValue() : 0;
                    double gradePoint = point * credit_hrs;
                    totalCreditHrs += credit_hrs;
                    totalGradePoint += gradePoint;
                    if (status == 0 && "yes".equals(calculateGpa)) {
                        totalCalculatedCreditHrs += credit_hrs;
                        totalCalculatedGradePoint += gradePoint;
                    }
                    mark.setGradePoint(gradePoint);
                    info.put(subject, mark);
                    mark.setDisplayResult(true);
                    if (status == 1) {
                        mark.setTotal(-1);
                        mark.setPoint(-1);
                        mark.setGradePoint(-1);
                        mark.setGrade(status_name);
                        mark.setDisplayResult(false);
                        info.put(subject, mark);
                    }
                    if (status == 3) {
                        mark.setTotal(-1);
                        mark.setPoint(-1);
                        mark.setGradePoint(-1);
                        mark.setGrade(status_name);
                        mark.setDisplayResult(false);
                        info.put(subject, mark);
                    }
                }
            }
            if (totalCalculatedCreditHrs == 0) {
                return null;
            }
            BigDecimal gpaBd = new BigDecimal(totalCalculatedGradePoint);
            double gpa = gpaBd.doubleValue() / totalCalculatedCreditHrs;
            BigDecimal gpaB = new BigDecimal(gpa);
            gpaB = gpaB.setScale(2, BigDecimal.ROUND_UP);
            String GPA = new java.text.DecimalFormat("##.00").format(gpaB.doubleValue());
            markInfo.put("totalCreditHrs", new Integer(totalCreditHrs));
            markInfo.put("totalCalculatedCreditHrs", new Integer(totalCalculatedCreditHrs));
            markInfo.put("totalGradePoint", new Double(totalGradePoint));
            String totalGradePointDisplay = Mark.getDecimalFormatted(totalGradePoint);
            markInfo.put("totalGradePointDisplay", totalGradePointDisplay);
            markInfo.put("totalCalculatedGradePoint", new Double(totalCalculatedGradePoint));
            String totalCalculatedGradePointDisplay = Mark.getDecimalFormatted(totalCalculatedGradePoint);
            markInfo.put("totalCalculatedGradePointDisplay", totalCalculatedGradePointDisplay);
            markInfo.put("gpa", GPA);
            markInfo.put("markInfo", info);
            return markInfo;
        } finally {
            if (db != null) db.close();
        }
    }

    public static void addMark(String student, String subject, String period, Mark mark, Vector grades) throws Exception {
        Db db = null;
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            addMark(stmt, student, subject, period, mark, grades);
        } finally {
            if (db != null) db.close();
        }
    }

    public static void addMark(Statement stmt, String student, String subject, String period, Mark mark, Vector grades) throws Exception {
        String sql = "";
        SQLRenderer r = new SQLRenderer();
        double total = 0.0;
        String mark_type = mark.getType();
        for (Enumeration e = mark.getKeys(); e.hasMoreElements(); ) {
            String part_id = (String) e.nextElement();
            double mark_value = mark.getValue(part_id);
            double percent_value = mark.getPercent(part_id);
            if ("percentage".equals(mark_type)) {
                double real_value = mark_value * (percent_value / 100);
                total += real_value;
            } else if ("point".equals(mark_type)) {
                double top = 4.0;
                double factor = 100.0 / top;
                double ptop = percent_value / factor;
                double real_value = mark_value * (ptop / top);
                total += real_value;
            } else {
                total += mark_value;
            }
            boolean found = false;
            {
                r.clear();
                r.add("student_id", student);
                r.add("period_id", period);
                r.add("subject_id", subject);
                r.add("part_id", part_id);
                r.add("mark_value");
                sql = r.getSQLSelect("student_marks");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true;
            }
            if (!found) {
                r.clear();
                r.add("student_id", student);
                r.add("period_id", period);
                r.add("subject_id", subject);
                r.add("part_id", part_id);
                r.add("mark_value", mark_value);
                r.add("mark_type", mark_type);
                sql = r.getSQLInsert("student_marks");
                stmt.executeUpdate(sql);
            } else {
                r.clear();
                r.update("student_id", student);
                r.update("period_id", period);
                r.update("subject_id", subject);
                r.update("part_id", part_id);
                r.add("mark_value", mark_value);
                r.add("mark_type", mark_type);
                sql = r.getSQLUpdate("student_marks");
                stmt.executeUpdate(sql);
            }
        }
        if ("point".equals(mark_type)) {
            saveTotalAsPoint(stmt, student, period, subject, mark, grades, total);
        } else {
            saveTotal(stmt, student, period, subject, mark, grades, total);
        }
    }

    private static void saveTotal(Statement stmt, String student, String period, String subject, Mark mark, Vector grades, double total) throws Exception {
        String sql = "";
        String grade = "";
        Hashtable g = getGrading(grades, total);
        mark.setPoint(g.get("point") != null ? ((Double) g.get("point")).doubleValue() : 0.0);
        mark.setGrade(g.get("display") != null ? (String) g.get("display") : "");
        SQLRenderer r = new SQLRenderer();
        {
            r.clear();
            r.add("student_id", student);
            r.add("period_id", period);
            r.add("subject_id", subject);
            sql = r.getSQLDelete("student_marks_total");
            stmt.executeUpdate(sql);
        }
        {
            r.clear();
            r.add("student_id", student);
            r.add("period_id", period);
            r.add("subject_id", subject);
            r.add("mark_total", total);
            r.add("mark_grade", mark.getGrade());
            r.add("mark_point", mark.getPoint());
            sql = r.getSQLInsert("student_marks_total");
            stmt.executeUpdate(sql);
        }
    }

    private static void saveTotalAsPoint(Statement stmt, String student, String period, String subject, Mark mark, Vector grades, double total) throws Exception {
        String sql = "";
        String grade = "";
        Hashtable g = getGradingByPoint(grades, total);
        mark.setPoint(g.get("point") != null ? ((Double) g.get("point")).doubleValue() : 0.0);
        mark.setGrade(g.get("display") != null ? (String) g.get("display") : "");
        SQLRenderer r = new SQLRenderer();
        {
            r.clear();
            r.add("student_id", student);
            r.add("period_id", period);
            r.add("subject_id", subject);
            sql = r.getSQLDelete("student_marks_total");
            stmt.executeUpdate(sql);
        }
        {
            r.clear();
            r.add("student_id", student);
            r.add("period_id", period);
            r.add("subject_id", subject);
            r.add("mark_total", total);
            r.add("mark_grade", mark.getGrade());
            r.add("mark_point", mark.getPoint());
            sql = r.getSQLInsert("student_marks_total");
            stmt.executeUpdate(sql);
        }
    }

    public static Mark getMark(String student, String subject, String period) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            Mark mark = new Mark();
            {
                r.clear();
                r.add("student_id", student);
                r.add("period_id", period);
                r.add("subject_id", subject);
                r.add("part_id");
                r.add("mark_value");
                sql = r.getSQLSelect("student_marks");
                ResultSet rs = stmt.executeQuery(sql);
                double total = 0.0;
                while (rs.next()) {
                    String part_id = rs.getString("part_id");
                    double mark_value = rs.getDouble("mark_value");
                    mark.setValue(part_id, mark_value);
                    total += mark_value;
                }
                mark.setTotal(total);
            }
            return mark;
        } finally {
            if (db != null) db.close();
        }
    }

    public static Mark getMark2(Statement stmt, String student, String subject, String period) throws Exception {
        String sql = "";
        SQLRenderer r = new SQLRenderer();
        Mark mark = new Mark();
        {
            r.clear();
            r.add("student_id", student);
            r.add("period_id", period);
            r.add("subject_id", subject);
            r.add("part_id");
            r.add("mark_value");
            sql = r.getSQLSelect("student_marks");
            ResultSet rs = stmt.executeQuery(sql);
            double total = 0.0;
            while (rs.next()) {
                String part_id = rs.getString("part_id");
                double mark_value = rs.getDouble("mark_value");
                mark.setValue(part_id, mark_value);
                total += mark_value;
            }
            mark.setTotal(total);
        }
        return mark;
    }

    static class SubjectSequenceComparator implements Comparator {

        public int compare(Object obj1, Object obj2) {
            Hashtable s1 = (Hashtable) obj1;
            Hashtable s2 = (Hashtable) obj2;
            return (Integer) s1.get("sequence_no") - (Integer) s2.get("sequence_no");
        }
    }

    public static Hashtable getMarkingInfo(String student, String session_id, String period, String program, String course_id) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            Hashtable gradingScheme = GradingSchemeData.getSchemeInfo(session_id, program);
            Vector grades = (Vector) gradingScheme.get("grades");
            Hashtable markInfo = new Hashtable();
            markInfo.put("grades", grades);
            Vector subjectList = new Vector();
            {
                r.clear();
                r.add("s.student_id", student);
                r.add("s.period_id", period);
                r.add("s.program_code", program);
                r.add("s.course_id", course_id);
                r.add("sr.status_category", 0);
                r.add("fs.exclude_gpa", 0);
                r.add("s.subject_id", r.unquote("fs.subject_id"));
                r.add("s.register_id", r.unquote("sr.status_id"));
                r.add("s.subject_id");
                r.add("fs.subject_code");
                r.add("fs.subject_name");
                r.add("fs.credit_hrs");
                sql = r.getSQLSelect("student_subject s, subject_reg_status sr, faculty_subject fs");
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String subject_id = rs.getString("subject_id");
                    String subject_code = rs.getString("subject_code");
                    String subject_name = rs.getString("subject_name");
                    int credit_hrs = rs.getInt("credit_hrs");
                    Subject subject = new Subject();
                    subject.setId(subject_id);
                    subject.setCode(subject_code);
                    subject.setName(subject_name);
                    subject.setCreditHours(credit_hrs);
                    subjectList.addElement(subject);
                }
                markInfo.put("subjectList", subjectList);
            }
            Hashtable info = new Hashtable();
            int totalCreditHrs = 0;
            float totalGradePoint = 0.0f;
            for (int i = 0; i < subjectList.size(); i++) {
                Subject subject = (Subject) subjectList.elementAt(i);
                String subject_id = subject.getId();
                double total = 0.0;
                Mark studentMark = getMark(student, subject_id, period);
                Mark mark = new Mark();
                mark.setGradeScheme(grades);
                Vector parts = getSchemeInfo(subject_id, session_id);
                if (parts != null) {
                    for (int k = 0; k < parts.size(); k++) {
                        MarkingPart part = (MarkingPart) parts.elementAt(k);
                        mark.addMarkingPart(part);
                        double mark_value = studentMark.getValue(part.getId());
                        mark.setValue(part.getId(), mark_value);
                    }
                } else {
                }
                float point = 0.0f;
                String grade = "";
                {
                    sql = "select mark_total, mark_grade, mark_point from student_marks_total " + "where student_id = '" + student + "' " + "and period_id = '" + period + "' " + "and subject_id = '" + subject_id + "' ";
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        total = rs.getDouble("mark_total");
                        grade = rs.getString("mark_grade");
                        if (grade == null) grade = "";
                        point = rs.getFloat("mark_point");
                    }
                }
                mark.setTotal(total);
                mark.setPoint(point);
                mark.setGrade(grade);
                {
                    r.clear();
                    r.add("m.mark_type");
                    r.add("i.session_id", session_id);
                    r.add("i.subject_id", subject_id);
                    r.add("i.scheme_id", r.unquote("m.scheme_id"));
                    sql = r.getSQLSelect("marking_scheme m, marking_scheme_impl i");
                    ResultSet rs = stmt.executeQuery(sql);
                    String type = "actual";
                    if (rs.next()) {
                        type = rs.getString("mark_type");
                    }
                    mark.setType(type != null ? type : "actual");
                }
                int credit_hrs = subject.getCreditHours();
                float gradePoint = point * credit_hrs;
                totalCreditHrs += credit_hrs;
                totalGradePoint += gradePoint;
                mark.setGradePoint(gradePoint);
                info.put(subject_id, mark);
            }
            float gpa = totalGradePoint / totalCreditHrs;
            String GPA = new java.text.DecimalFormat("##.00").format(gpa);
            markInfo.put("totalCreditHrs", new Integer(totalCreditHrs));
            markInfo.put("totalGradePoint", new Float(totalGradePoint));
            markInfo.put("totalGradePointDisplay", Mark.getDecimalFormatted(totalGradePoint));
            markInfo.put("gpa", GPA);
            markInfo.put("markInfo", info);
            markInfo.put("period_id", period);
            return markInfo;
        } finally {
            if (db != null) db.close();
        }
    }

    public static Mark getMarkInfo(String student, String session_id, String period, String program, String subject) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            Hashtable gradingScheme = GradingSchemeData.getSchemeInfo(session_id, program);
            Vector grades = (Vector) gradingScheme.get("grades");
            double total = 0.0;
            Mark studentMark = getMark(student, subject, period);
            Mark mark = new Mark();
            mark.setGradeScheme(grades);
            Vector parts = getSchemeInfo(subject, session_id);
            for (int k = 0; k < parts.size(); k++) {
                MarkingPart part = (MarkingPart) parts.elementAt(k);
                mark.addMarkingPart(part);
                double mark_value = studentMark.getValue(part.getId());
                mark.setValue(part.getId(), mark_value);
            }
            double point = 0.0;
            String grade = "";
            {
                sql = "select mark_total, mark_grade, mark_point from student_marks_total " + "where student_id = '" + student + "' " + "and period_id = '" + period + "' " + "and subject_id = '" + subject + "' ";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    total = rs.getDouble("mark_total");
                    grade = rs.getString("mark_grade");
                    if (grade == null) grade = "";
                    point = rs.getFloat("mark_point");
                }
            }
            mark.setTotal(total);
            mark.setPoint(point);
            mark.setGrade(grade);
            {
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
                mark.setType(type != null ? type : "actual");
            }
            return mark;
        } finally {
            if (db != null) db.close();
        }
    }

    static Hashtable getGrading(Vector grades, double total) {
        Hashtable h = new Hashtable();
        if (grades == null) {
            h.put("point", 0.0);
            h.put("display", "Nil");
        } else {
            for (int i = 0; i < grades.size(); i++) {
                Grade grade = (Grade) grades.elementAt(i);
                double high = grade.getHigh();
                double low = grade.getLow();
                if (total < high && total >= low) {
                    h.put("point", grade.getPoint());
                    h.put("display", grade.getDisplay());
                    break;
                }
                h.put("point", 0.0);
                h.put("display", "Nil");
            }
        }
        return h;
    }

    static Hashtable getGradingByPoint(Vector grades, double total) {
        Hashtable h = new Hashtable();
        if (grades == null) {
            h.put("point", 0.0);
            h.put("display", "Nil");
        } else {
            for (int i = 0; i < grades.size(); i++) {
                Grade grade = (Grade) grades.elementAt(i);
                if (grade.getPoint() <= total) {
                    selectGrade(h, grades, total, i);
                    break;
                }
                h.put("point", new Float(0.0f));
                h.put("display", "Nil");
            }
        }
        return h;
    }

    private static void selectGrade(Hashtable h, Vector grades, double total, int i) {
        int k = i;
        if (i > 1) {
            Grade grade = (Grade) grades.elementAt(i);
            double p1 = grade.getPoint();
            Grade grade2 = (Grade) grades.elementAt(i - 1);
            double p2 = grade2.getPoint();
            double diff1 = total - p1;
            double diff2 = p2 - total;
            if (diff1 < diff2) k = i; else k = i - 1;
        }
        Grade g = (Grade) grades.elementAt(k);
        h.put("point", new Double(g.getPoint()));
        h.put("display", g.getDisplay());
    }

    public static Vector getSchemeInfo(String subject, String session) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("subject_id", subject);
            r.add("session_id", session);
            r.add("part_id");
            r.add("part_name");
            r.add("part_percentage");
            r.add("p.scheme_id", r.unquote("m.scheme_id"));
            sql = r.getSQLSelect("marking_part p, marking_scheme_impl m", "part_sequence");
            ResultSet rs = stmt.executeQuery(sql);
            Vector partList = new Vector();
            while (rs.next()) {
                String part_id = rs.getString("part_id");
                String part_name = rs.getString("part_name");
                int pct = rs.getInt("part_percentage");
                MarkingPart part = new MarkingPart();
                part.setId(part_id);
                part.setName(part_name);
                part.setPercentage(pct);
                partList.addElement(part);
            }
            return partList;
        } finally {
            if (db != null) db.close();
        }
    }

    public static Vector getSchemeInfo2(Statement stmt, String subject, String session) throws Exception {
        String sql = "";
        SQLRenderer r = new SQLRenderer();
        r.add("subject_id", subject);
        r.add("session_id", session);
        r.add("part_id");
        r.add("part_name");
        r.add("part_percentage");
        r.add("p.scheme_id", r.unquote("m.scheme_id"));
        sql = r.getSQLSelect("marking_part p, marking_scheme_impl m", "part_sequence");
        ResultSet rs = stmt.executeQuery(sql);
        Vector partList = new Vector();
        while (rs.next()) {
            String part_id = rs.getString("part_id");
            String part_name = rs.getString("part_name");
            int pct = rs.getInt("part_percentage");
            MarkingPart part = new MarkingPart();
            part.setId(part_id);
            part.setName(part_name);
            part.setPercentage(pct);
            partList.addElement(part);
        }
        return partList;
    }

    public static void deleteMark(String student, String subject, String period) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
        } finally {
            if (db != null) db.close();
        }
    }

    public static Vector getPartIdList(String session_id, String subject) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            return getPartIdList(db.getStatement(), session_id, subject);
        } finally {
            if (db != null) db.close();
        }
    }

    public static Vector getPartIdList(Statement stmt, String session_id, String subject) throws Exception {
        String sql = "";
        SQLRenderer r = new SQLRenderer();
        r.add("i.session_id", session_id);
        r.add("i.subject_id", subject);
        r.add("i.scheme_id", r.unquote("p.scheme_id"));
        r.add("part_id");
        sql = r.getSQLSelect("marking_scheme_impl i, marking_part p");
        ResultSet rs = stmt.executeQuery(sql);
        Vector<String> v = new Vector<String>();
        while (rs.next()) {
            v.addElement(rs.getString("part_id"));
        }
        return v;
    }

    public static Vector<Hashtable> getStudentList(String subject_id, String session_id) throws Exception {
        return getStudentList(subject_id, session_id, "");
    }

    public static Vector<Hashtable> getStudentList(String subject_id, String session_id, String program_code) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("s.student_id");
            r.add("st.name");
            r.add("s.period_id");
            r.add("s.program_code");
            r.add("s.subject_id", subject_id);
            r.add("i.session_id", session_id);
            r.add("s.student_id", r.unquote("c.student_id"));
            r.add("s.program_code", r.unquote("c.program_code"));
            r.add("s.period_id", r.unquote("i.period_id"));
            r.add("i.period_root_id", r.unquote("c.period_root_id"));
            r.add("i.intake_session", r.unquote("c.intake_session"));
            r.add("s.student_id", r.unquote("st.id"));
            r.add("s.register_id", r.unquote("sr.status_id"));
            r.add("sr.status_category", 0);
            if (!"".equals(program_code)) {
                r.add("s.program_code", program_code);
            }
            sql = r.getSQLSelectDistinct("student_subject s, subject_reg_status sr, intake_batch i, student_course c, student st", "st.name");
            ResultSet rs = stmt.executeQuery(sql);
            Vector<Hashtable> v = new Vector<Hashtable>();
            while (rs.next()) {
                Hashtable h = new Hashtable();
                String student_id = rs.getString("student_id");
                String student_name = rs.getString("name");
                String period = rs.getString("period_id");
                String program = rs.getString("program_code");
                h.put("student_id", student_id);
                h.put("student_name", student_name);
                h.put("period_id", period);
                h.put("program_code", program);
                Mark mark = getMarkInfo(student_id, session_id, period, program, subject_id);
                h.put("markInfo", mark);
                v.addElement(h);
            }
            return v;
        } finally {
            if (db != null) db.close();
        }
    }

    public static void saveTranscriptInfo(Vector transcriptRecords, Vector cumSections, String student_id, String periodToSave) throws Exception {
        saveTranscriptInfo(transcriptRecords, cumSections, student_id, periodToSave, false);
    }

    public static void saveTranscriptInfo(Vector transcriptRecords, Vector cumSections, String student_id, String periodToSave, boolean onePeriod) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            sql = "delete from transcript_display where student_id = '" + student_id + "'";
            stmt.executeUpdate(sql);
            int cumCnt = 0;
            String temp = "";
            int cum_credit_hrs = 0;
            double cum_grade_point = 0.0;
            String cgpa = "";
            for (int i = 0; i < transcriptRecords.size(); i++) {
                Hashtable t = (Hashtable) transcriptRecords.elementAt(i);
                String period_id = (String) t.get("period_id");
                if (!temp.equals(period_id)) {
                    Hashtable cum = (Hashtable) cumSections.elementAt(cumCnt++);
                    cum_credit_hrs = ((Integer) cum.get("cumCreditHrs")).intValue();
                    cum_grade_point = ((Double) cum.get("cumPoint")).doubleValue();
                    cgpa = (String) cum.get("cgpa");
                    temp = period_id;
                }
                if (onePeriod) {
                    if (periodToSave.equals(period_id)) {
                        r.clear();
                        saveData(r, stmt, t, student_id, cum_credit_hrs, cum_grade_point, cgpa);
                    }
                } else {
                    r.clear();
                    saveData(r, stmt, t, student_id, cum_credit_hrs, cum_grade_point, cgpa);
                }
            }
        } finally {
            if (db != null) db.close();
        }
    }

    static void saveData(SQLRenderer r, Statement stmt, Hashtable t, String student_id, int cum_credit_hrs, double cum_grade_point, String cgpa) throws Exception {
        String program_code = (String) t.get("program_code");
        String period_id = (String) t.get("period_id");
        String period_name = (String) t.get("period_name");
        java.util.Date startDate = (java.util.Date) t.get("startDate");
        String session_name = (String) t.get("session_name");
        String subject_code = (String) t.get("subject_code");
        String subject_name = (String) t.get("subject_name");
        int credit_hrs = t.get("credit_hrs") != null ? ((Integer) t.get("credit_hrs")).intValue() : 0;
        double total = ((Double) t.get("total_mark")).doubleValue();
        double point = ((Double) t.get("point")).doubleValue();
        double grade_point = ((Double) t.get("grade_point")).doubleValue();
        String grade = (String) t.get("grade");
        int tot_credit_hrs = ((Integer) t.get("tot_credit_hrs")).intValue();
        int calculated_credit_hrs = ((Integer) t.get("calculated_credit_hrs")).intValue();
        double tot_grade_point = ((Double) t.get("tot_grade_point")).doubleValue();
        String gpa = (String) t.get("gpa");
        Format f = new DecimalFormat("#,###,###.00");
        String start_date = educate.sis.tools.DateTool.getDateString(startDate);
        {
            String sql = r.reset().add("student_id", student_id).add("program_code", program_code).add("period_id", period_id).add("period_name", period_name).add("startDate", start_date).add("session_name", session_name).add("subject_code", subject_code).add("subject_name", subject_name).add("credit_hrs", credit_hrs).add("total", total > -1 ? f.format(total) : "").add("point", point > -1 ? f.format(point) : "").add("grade_point", grade_point > -1 ? f.format(grade_point) : "").add("grade", grade).add("tot_credit_hrs", tot_credit_hrs).add("calculated_credit_hrs", calculated_credit_hrs).add("tot_grade_point", tot_grade_point > -1 ? f.format(tot_grade_point) : "").add("gpa", gpa).add("cum_credit_hrs", cum_credit_hrs).add("cum_grade_point", f.format(cum_grade_point)).add("cgpa", cgpa).getSQLInsert("transcript_display");
            stmt.executeUpdate(sql);
        }
    }
}

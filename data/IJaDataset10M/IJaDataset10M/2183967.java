package org.eledge;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*******************************************************************************
 * @deprecated
 * 
 */
@Deprecated
public class Gradebook extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -6481676519470703332L;

    RBStore res = EledgeResources.getGradebookBundle();

    @Override
    public String getServletInfo() {
        return "This Eledge servlet module displays a summary of student grades for the Instructor.";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Student student = (Student) session.getAttribute(Course.name + "Student");
        if (student == null) {
            student = new Student();
        }
        if (!student.isAuthenticated()) {
            response.sendRedirect(Course.secureLoginURL + "Gradebook");
            return;
        }
        if (student.getIsTA()) {
            TA ta = TAS.getTA(student.getIDNumber());
            StringBuffer err = new StringBuffer();
            if (!ta.hasPermission("Gradebook", request, student, err)) {
                out.println(Page.create(err.toString()));
                return;
            }
        }
        if (!student.getIsInstructor()) {
            out.println(Page.create(res.getString("str_instructor_only_page")));
            return;
        }
        String userRequest = request.getParameter("UserRequest");
        if (userRequest != null) {
            if (userRequest.equals("HelpTextFile")) {
                out.println(Page.create(helpTextFile()));
            }
            return;
        }
        out.println(Page.create(classScores(student)));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Student student = (Student) session.getAttribute(Course.name + "Student");
        TA ta = null;
        if (student == null) {
            student = new Student();
        }
        if (!student.isAuthenticated()) {
            response.sendRedirect(Course.secureLoginURL + "Gradebook");
            return;
        }
        if (student.getIsTA()) {
            ta = TAS.getTA(student.getIDNumber());
            StringBuffer err = new StringBuffer();
            if (!ta.hasPermission("Gradebook", request, student, err)) {
                out.println(Page.create(err.toString()));
                return;
            }
        }
        if (!student.getIsInstructor()) {
            out.println(Page.create(res.getString("str_instructor_only_page")));
            return;
        }
        String userRequest = request.getParameter("UserRequest");
        if (userRequest == null) {
            out.println(Page.create(classScores(student)));
            return;
        }
        if (userRequest.equals("CreateTextFile")) {
            out.println(createTextFile(student));
            return;
        }
        String id = request.getParameter("StudentIDNumber");
        if (id == null) {
            out.println(Page.create(res.getString("str_must_select_student")));
            return;
        }
        if (userRequest.equals("DeleteStudent")) {
            if (student.idNumberLooksValid(id)) {
                deleteStudent(id);
            }
            out.println(Page.create(classScores(student)));
            return;
        }
        if (userRequest.equals("UpdateStudent")) {
            if (student.idNumberLooksValid(id)) {
                updateStudent(id, request, ta);
            }
            out.println(Page.create(classScores(student)));
            return;
        }
        if (userRequest.equals("ScoresDetail")) {
            if (student.idNumberLooksValid(id)) {
                out.println(Page.create(scoresDetail(id)));
            }
            return;
        }
        if (userRequest.equals("DeleteScore")) {
            String scoreID = request.getParameter("ScoreID");
            if (scoreID != null) {
                deleteScore(scoreID);
                out.println(Page.create(scoresDetail(id)));
                return;
            }
            out.println(Page.create(res.getString("str_must_select_score")));
            return;
        }
        if (userRequest.equals("AddScore")) {
            String assignment = request.getParameter("Assignment");
            String ipNumber = request.getRemoteAddr();
            int score = 0;
            try {
                score = Integer.parseInt(request.getParameter("Score"));
            } catch (NumberFormatException e) {
                out.println(Page.create(res.getString("str_must_enter_int_score")));
                return;
            }
            if (assignment != null) {
                addScore(id, assignment, score, ipNumber);
                out.println(Page.create(scoresDetail(id)));
            } else {
                out.println(Page.create(res.getString("str_must_enter_assign")));
            }
            return;
        }
        if (userRequest.equals("ShowAnswers")) {
            String scoreID = request.getParameter("ScoreID");
            if (scoreID != null) {
                out.println(Page.create(showAnswers(id, scoreID)));
                return;
            }
            out.println(Page.create(res.getString("str_must_select_score")));
            return;
        }
        if (userRequest.equals("GradeEssays")) {
            if (student.idNumberLooksValid(id)) {
                out.println(Page.create(gradeEssayForm(id)));
            }
        }
        if (userRequest.equals("UpdateEssay")) {
            int score = 0;
            int possibleScore = 0;
            try {
                score = Integer.parseInt(request.getParameter("Score"));
                possibleScore = Integer.parseInt(request.getParameter("PossibleScore"));
            } catch (NumberFormatException e) {
                out.println(Page.create(res.getString("str_must_enter_int_score")));
                return;
            }
            String assignment = request.getParameter("Assignment");
            String questionID = request.getParameter("QuestionID");
            String testType = request.getParameter("TestType");
            String gradedAnswer = request.getParameter("Answer");
            String code = request.getParameter("Code");
            boolean email = (request.getParameter("Email") == null ? false : true);
            if (questionID != null && testType != null && gradedAnswer != null && assignment != null && code != null) {
                if ((updateEssay(student, id, questionID, testType, gradedAnswer, score, assignment, email, possibleScore, code))) {
                    out.println(Page.create(gradeEssayForm(id)));
                } else {
                    out.println(Page.create(res.getString("str_error_answer_not_updated")));
                }
            } else {
                out.println(Page.create(res.getString("str_error_essay_page")));
            }
        }
        if (userRequest.equals("DeleteEssay")) {
            String questionID = request.getParameter("QuestionID");
            String testType = request.getParameter("TestType");
            String code = request.getParameter("Code");
            if (questionID != null && testType != null && code != null) {
                if (deleteEssay(id, questionID, testType, code)) {
                    out.println(Page.create(gradeEssayForm(id)));
                } else {
                    out.println(Page.create(res.getString("str_error_answer_not_deleted")));
                }
            } else {
                out.println(Page.create(res.getString("str_error_essay_page")));
            }
        }
        if (userRequest.equals("ShowGradedEssays")) {
            if (student.idNumberLooksValid(id)) {
                out.println(Page.create(gradedEssays(id)));
            }
        }
        if (userRequest.equals("ResetPassword")) {
            if (student.idNumberLooksValid(id)) {
                out.println(Page.create(resetStudentPasswordForm(id)));
            }
        }
        if (userRequest.equals("ResetStudentPassword")) {
            if (!(student.getPassword().equals(request.getParameter("TeacherPass")))) {
                out.println(res.getString("str_teacher_passwd_invalid"));
                return;
            }
            String studentPass = request.getParameter("StudentPass");
            out.println(Page.create(student.resetPassword(studentPass, id) + classScores(student)));
            return;
        }
    }

    String classScores(Student student) {
        int numberOfSections = 1;
        StringBuffer buf = new StringBuffer();
        MessageFormat mf = new MessageFormat(res.getString("str_td_title"));
        Date now = new Date();
        buf.append("<style>\n");
        buf.append("<!--\n");
        buf.append("td.score {\n");
        buf.append("    text-align: center;\n");
        buf.append("    position: relative;\n");
        buf.append("}\n");
        buf.append("\n");
        buf.append("td a.score {\n");
        buf.append("  display: block;\n");
        buf.append("  text-decoration: none;\n");
        buf.append("  color: black;\n");
        buf.append("}\n");
        buf.append("\n");
        buf.append(".tooltip {\n");
        buf.append("  position: absolute;\n");
        buf.append("  z-index: 7;\n");
        buf.append("  border: 1px #000 solid;\n");
        buf.append("  background-color: blue;\n");
        buf.append("  color: white;\n");
        buf.append("  font-size: large;\n");
        buf.append("  font-weight: bold;\n");
        buf.append("  display: none;\n");
        buf.append("}\n");
        buf.append("-->\n");
        buf.append("</style>\n");
        buf.append("<script>\n");
        buf.append("<!--\n");
        buf.append("function showTip(id) {\n");
        buf.append("  var el = document.getElementById(id);\n");
        buf.append("  if (el == null) {\n");
        buf.append("    return false;\n");
        buf.append("  }\n");
        buf.append("   el.style.display = \"block\";\n");
        buf.append("}\n");
        buf.append("\n");
        buf.append("function hideTip(id) {\n");
        buf.append("  var el = document.getElementById(id);\n");
        buf.append("  if (el == null) {\n");
        buf.append("    return false;\n");
        buf.append("  }\n");
        buf.append("  el.style.display = \"none\";\n");
        buf.append("}\n");
        buf.append("\n");
        buf.append("function showTip2(event, id) {\n");
        buf.append("  //alert('I got called');\n");
        buf.append("  var el = document.getElementById(id);\n");
        buf.append("  if (el == null) {\n");
        buf.append("    alert(id + 'not found');\n");
        buf.append("    return false;\n");
        buf.append("  }\n");
        buf.append("  //alert(id + ' was found');\n");
        buf.append("  //set the position...\n");
        buf.append("  if (!event) event = window.event;\n");
        buf.append("  //if (event == null) {\n");
        buf.append("  //alert('event was null. Hrm.');\n");
        buf.append("  //}\n");
        buf.append("  var x, y;\n");
        buf.append("  x = (event.pageX?event.pageX:(event.clientX?event.clientX:0));\n");
        buf.append("  y = (event.pageY?event.pageY:(event.clientY?event.clientY:0));\n");
        buf.append("  el.style.top = x;\n");
        buf.append("  el.style.left=y;\n");
        buf.append("  el.style.display = \"block\";\n");
        buf.append("}\n");
        buf.append("\n");
        buf.append("function hideTip2(event, id) {\n");
        buf.append("  //alert('I just called, to say, I love you');\n");
        buf.append("  var el = document.getElementById(id);\n");
        buf.append("  if (el == null) {\n");
        buf.append("    return false;\n");
        buf.append("  }\n");
        buf.append("  el.style.display = \"none\";\n");
        buf.append("}\n");
        buf.append("-->\n");
        buf.append("</script>\n");
        buf.append("<h3>" + res.getString("str_scores_page_title") + "</h3>" + now + "<p>" + res.getString("str_scores_explain1") + res.getString("str_scores_explain2"));
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Value FROM CourseParameters WHERE Name='NumberOfSections'");
            if (rs.next()) {
                numberOfSections = rs.getInt("Value");
            }
            String[] sectionNames = new String[numberOfSections + 1];
            ResultSet rsSectionNames = stmt.executeQuery("SELECT SectionName FROM CourseSections");
            for (int j = 1; j <= numberOfSections; j++) {
                if (rsSectionNames.next()) {
                    sectionNames[j] = rsSectionNames.getString("SectionName");
                }
            }
            String sqlStr2 = "SELECT Assignment FROM Scores GROUP BY Assignment ORDER BY Assignment";
            ResultSet rs2 = stmt.executeQuery(sqlStr2);
            Vector<String> names = new Vector<String>();
            while (rs2.next()) {
                names.addElement(rs2.getString("Assignment"));
            }
            String[] assignmentName = new String[names.size()];
            for (int i = 0; i < names.size(); i++) {
                assignmentName[i] = names.get(i);
            }
            String sqlStr1 = "SELECT CONCAT(Students.FirstName,'&nbsp;',Students.LastName) AS Name," + "Students.StudentIDNumber AS ID,Students.SectionID AS SectionID,Scores.Assignment AS Assignment, " + "MAX(Scores.Score) AS Score,Students.Status AS Status,Students.Email AS Email " + "FROM Students LEFT OUTER JOIN Scores USING (StudentIDNumber) " + "GROUP BY Assignment,ID ORDER BY Status,SectionID,Students.LastName," + "Students.FirstName,Assignment";
            if (student.getIsTA()) {
                TA ta = TAS.getTA(student.getIDNumber());
                TAPermission tap = ta.getPermission("Gradebook");
                if (tap.getPermissionLevel().equals(TAPermission.PERM_NONE) || tap.getPermissionLevel().equals(TAPermission.PERM_STUDENT)) {
                    return Permissions.getPermission("Gradebook").getDenyMsg();
                }
                if (tap.getPermissionLevel().equals(TAPermission.PERM_CONDITIONAL)) {
                    sqlStr1 = "SELECT CONCAT(Students.Firstname,'&nbsp;',Students.LastName) " + "AS Name, Students.StudentIDNumber AS ID, Students.SectionID AS " + "SectionID, Scores.Assignment AS Assignment, MAX(Scores.Score) AS " + "Score, Students.Status AS Status, Students.Email AS Email, " + "TAAssignments.StudentIDNumber AS TAID, TAAssignments.Type, " + "TAAssignments.Value FROM Students LEFT OUTER JOIN Scores USING " + "(StudentIDNumber) LEFT OUTER JOIN TAAssignments ON (TAAssignments.StudentIDNumber='" + student.getIDNumber() + "' AND TAAssignments.Type='Student' AND TAAssignments.Value=Students.StudentIDNumber) " + "WHERE TAAssignments.Value IS NOT NULL GROUP BY Assignment,ID " + "ORDER BY Status, SectionID, Students.LastName, Students.FirstName, Assignment";
                }
            }
            ResultSet rs1 = stmt.executeQuery(sqlStr1);
            buf.append("<FORM NAME=UpdateStudent METHOD=POST>");
            buf.append("\n<input type=hidden name=UserRequest>");
            buf.append("<input type=SUBMIT value='" + res.getString("str_reset_passwd") + "' onClick=this.form.elements.UserRequest.value='ResetPassword';><BR>");
            buf.append("<table border=1 cellspacing=0>" + "<tr><td></td><td><b>" + res.getString("str_field_name") + "</b></td><td><b>" + res.getString("str_field_id") + "</b></td>");
            if (numberOfSections > 1) {
                buf.append("<td><b>" + res.getString("str_field_section") + "</b></td>");
            }
            buf.append("<td><b>" + res.getString("str_field_status") + "</b></td>");
            for (String element : assignmentName) {
                buf.append("<td><b>" + element + "</b></td>");
            }
            buf.append("</tr>");
            boolean more = rs1.next();
            while (more) {
                String id = rs1.getString("ID");
                String name = fixName(rs1.getString("Name"));
                buf.append("\n<tr>" + "<td><input type=radio name=StudentIDNumber value='" + rs1.getString("ID") + "'></td>" + "<td><a href=mailto:" + rs1.getString("Students.Email") + ">" + rs1.getString("Name") + "</a></td>" + "<td>" + rs1.getString("ID") + "</td>");
                if (numberOfSections > 1) {
                    buf.append("<td>" + sectionSelectBox(rs1.getString("ID"), rs1.getInt("SectionID"), numberOfSections, sectionNames) + "</td>");
                }
                buf.append("<td>" + statusSelectionBox(id, rs1.getString("Status")) + "</td>");
                if (rs1.getString("Assignment") == null) {
                    for (String element : assignmentName) {
                        Object[] args = { element, name };
                        String tipName = rs1.getString("ID") + "_" + element;
                        buf.append("<td class='score'><a class='score' href='' onmouseover=\"showTip('").append(tipName).append("')\" onmouseout=\"hideTip('").append(tipName).append("')\">&nbsp;</a><div id='");
                        buf.append(tipName).append("' class='tooltip'>");
                        buf.append(mf.format(args)).append("</div></td>");
                    }
                    more = rs1.next();
                } else {
                    for (String element : assignmentName) {
                        Object[] args = { element, name };
                        String tipName = rs1.getString("ID") + "_" + element;
                        if (!more) {
                            buf.append("<td class='score'><a class='score' href='' onmouseover=\"showTip('").append(tipName).append("')\" onmouseout=\"hideTip('").append(tipName).append("')\">&nbsp;</a><div id='");
                            buf.append(tipName).append("' class='tooltip'>");
                            buf.append(mf.format(args)).append("</div></td>");
                        } else if (rs1.getString("Assignment") == null) {
                            buf.append("<td class='score'><a class='score' href='' onmouseover=\"showTip('").append(tipName).append("')\" onmouseout=\"hideTip('").append(tipName).append("')\">&nbsp;</a><div id='");
                            buf.append(tipName).append("' class='tooltip'>");
                            buf.append(mf.format(args)).append("</div></td>");
                        } else {
                            if (rs1.getString("ID").equals(id) & rs1.getString("Assignment").toUpperCase().equals(element.toUpperCase())) {
                                buf.append("<td class='score' ALIGN=CENTER>");
                                buf.append("<a class='score' href='' onmouseover=\"showTip('").append(tipName).append("')\" onmouseout=\"hideTip('").append(tipName).append("')\">").append((rs1.getInt("Score") < 0 ? res.getString("str_not_graded") : rs1.getString("Score"))).append("</a><div id='");
                                buf.append(tipName).append("' class='tooltip'>");
                                buf.append(mf.format(args)).append("</div></td>");
                                more = rs1.next();
                            } else {
                                buf.append("<td").append(" class='score'><a href='' class='score' onmouseover=\"showTip('").append(tipName).append("')\" onmouseout=\"hideTip('").append(tipName).append("')\">&nbsp;</a><div id='");
                                buf.append(tipName).append("' class='tooltip'>");
                                buf.append(mf.format(args)).append("</div></td>");
                            }
                        }
                    }
                }
                buf.append("</tr>");
            }
            buf.append("\n</table>");
            buf.append("\n<input type=SUBMIT onClick=this.form.elements.UserRequest.value='UpdateStudent';" + " VALUE='" + res.getString("str_update_student") + "'>&nbsp;");
            buf.append("\n<input type=BUTTON " + "onClick=\"if (confirm('" + res.getString("str_warning_delete_student") + "')) { " + "UserRequest.value='DeleteStudent'; UpdateStudent.submit();}\" VALUE='" + res.getString("str_delete_student") + "'>&nbsp;");
            buf.append("\n<input type=SUBMIT onClick=this.form.elements.UserRequest.value='ScoresDetail';" + " VALUE='" + res.getString("str_scores_detail") + "'>");
            if (hasUngradedQuestions()) {
                buf.append("\n<input type=SUBMIT onClick=this.form.elements.UserRequest.value='GradeEssays'; VALUE='" + res.getString("str_grade_essays") + "'>");
            }
            buf.append("\n</FORM>");
            buf.append("<FORM METHOD=POST><INPUT TYPE=HIDDEN NAME=UserRequest VALUE=CreateTextFile>" + "<INPUT TYPE=SUBMIT VALUE='" + res.getString("str_export_scores") + "'> " + "<a href=" + Course.name + ".Gradebook?UserRequest=HelpTextFile>" + res.getString("str_explain_export") + "</a></FORM>");
            if (!student.getIsTA()) {
                buf.append("<BR><hr width=40%><FORM METHOD=GET ACTION=\"" + Course.name + ".ManageTAPermissions\"><INPUT TYPE=SUBMIT VALUE='" + res.getString("str_manage_ta") + "'></FORM>");
            }
        } catch (Exception e) {
            return e.getMessage() + buf.toString();
        }
        return buf.toString();
    }

    String fixName(String oldName) {
        StringBuffer on = new StringBuffer(oldName);
        on.replace(oldName.indexOf('&'), (oldName.indexOf(';') + 1), " ");
        return on.toString();
    }

    String createTextFile(Student student) {
        int numberOfSections = 1;
        StringBuffer buf = new StringBuffer();
        Date now = new Date();
        buf.append(now + "\n");
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Value FROM CourseParameters WHERE Name='NumberOfSections'");
            if (rs.next()) {
                numberOfSections = rs.getInt("Value");
            }
            String[] sectionNames = new String[numberOfSections + 1];
            ResultSet rsSectionNames = stmt.executeQuery("SELECT SectionName FROM CourseSections");
            for (int j = 1; j <= numberOfSections; j++) {
                if (rsSectionNames.next()) {
                    sectionNames[j] = rsSectionNames.getString("SectionName");
                }
            }
            String sqlStr2 = "SELECT Assignment FROM Scores GROUP BY Assignment ORDER BY Assignment";
            ResultSet rs2 = stmt.executeQuery(sqlStr2);
            Vector<String> names = new Vector<String>();
            while (rs2.next()) {
                names.addElement(rs2.getString("Assignment"));
            }
            String[] assignmentName = new String[names.size()];
            for (int i = 0; i < names.size(); i++) {
                assignmentName[i] = names.get(i);
            }
            String sqlStr1 = "SELECT CONCAT(Students.LastName,', ',Students.FirstName) AS Name," + "Students.StudentIDNumber AS ID,Students.SectionID AS SectionID,Scores.Assignment AS Assignment, " + "MAX(Scores.Score) AS Score,Students.Status AS Status,Students.Email AS Email " + "FROM Students LEFT OUTER JOIN Scores USING (StudentIDNumber) " + "GROUP BY Assignment,ID ORDER BY Status,SectionID,Students.LastName," + "Students.FirstName,Assignment";
            if (student.getIsTA()) {
                TA ta = TAS.getTA(student.getIDNumber());
                TAPermission tap = ta.getPermission("Gradebook");
                if (tap.getPermissionLevel().equals(TAPermission.PERM_NONE) || tap.getPermissionLevel().equals(TAPermission.PERM_STUDENT)) {
                    return Permissions.getPermission("Gradebook").getDenyMsg();
                }
                if (tap.getPermissionLevel().equals(TAPermission.PERM_CONDITIONAL)) {
                    sqlStr1 = "SELECT CONCAT(Students.Firstname,'&nbsp;',Students.LastName) " + "AS Name, Students.StudentIDNumber AS ID, Students.SectionID AS " + "SectionID, Scores.Assignment AS Assignment, MAX(Scores.Score) AS " + "Score, Students.Status AS Status, Students.Email AS Email, " + "TAAssignments.StudentIDNumber AS TAID, TAAssignments.Type, " + "TAAssignments.Value FROM Students LEFT OUTER JOIN Scores USING " + "(StudentIDNumber) LEFT OUTER JOIN TAAssignments ON (TAAssignments.StudentIDNumber='" + student.getIDNumber() + "' AND TAAssignments.Type='Student' AND TAAssignments.Value=Students.StudentIDNumber) " + "WHERE TAAssignments.Value IS NOT NULL GROUP BY Assignment,ID " + "ORDER BY Status, SectionID, Students.LastName, Students.FirstName, Assignment";
                }
            }
            ResultSet rs1 = stmt.executeQuery(sqlStr1);
            buf.append(res.getString("str_field_name") + "\t" + res.getString("str_field_id") + "\t");
            if (numberOfSections > 1) {
                buf.append(res.getString("str_field_section") + "\t");
            }
            buf.append(res.getString("str_field_status") + "\t");
            for (String element : assignmentName) {
                buf.append(element + "\t");
            }
            buf.append("\n");
            boolean more = rs1.next();
            while (more) {
                String id = rs1.getString("ID");
                buf.append(rs1.getString("Name") + "\t\"" + rs1.getString("ID") + "\"\t");
                if (numberOfSections > 1) {
                    buf.append(rs1.getInt("SectionID") + "\t");
                }
                buf.append(rs1.getString("Status") + "\t");
                if (rs1.getString("Assignment") == null) {
                    for (String element : assignmentName) {
                        buf.append("\t");
                    }
                    more = rs1.next();
                } else {
                    for (String element : assignmentName) {
                        if (!more) {
                            buf.append("\t");
                        } else if (rs1.getString("Assignment") == null) {
                            buf.append("\t");
                        } else {
                            if (rs1.getString("ID").equals(id) & rs1.getString("Assignment").toUpperCase().equals(element.toUpperCase())) {
                                buf.append(rs1.getString("Score") + "\t");
                                more = rs1.next();
                            } else {
                                buf.append("\t");
                            }
                        }
                    }
                }
                buf.append("\n");
            }
        } catch (Exception e) {
            return e.getMessage() + buf.toString();
        }
        return buf.toString();
    }

    String helpTextFile() {
        StringBuffer buf = new StringBuffer();
        buf.append("<h3>" + res.getString("str_export_title") + "</h3>");
        buf.append(res.getString("str_explain_export2"));
        return buf.toString();
    }

    String sectionSelectBox(String studentIDNumber, int sectionID, int numberOfSections, String[] sectionNames) {
        StringBuffer buf = new StringBuffer("\n<SELECT NAME=Section" + studentIDNumber + ">");
        for (int j = 1; j <= numberOfSections; j++) {
            buf.append("<OPTION VALUE=" + j + (j == sectionID ? " SELECTED>" : ">") + sectionNames[j] + "</OPTION>");
        }
        buf.append("</SELECT>");
        return buf.toString();
    }

    String statusSelectionBox(String studentIDNumber, String status) {
        StringBuffer buf = new StringBuffer("\n<SELECT NAME=Status" + studentIDNumber + ">");
        buf.append("<OPTION VALUE=Current" + (status.equals("Current") ? " SELECTED>" : ">") + res.getString("str_status_current") + "</OPTION>");
        buf.append("<OPTION VALUE=Visitor" + (status.equals("Visitor") ? " SELECTED>" : ">") + res.getString("str_status_visitor") + "</OPTION>");
        buf.append("<OPTION VALUE=Frozen" + (status.equals("Frozen") ? " SELECTED>" : ">") + res.getString("str_status_frozen") + "</OPTION>");
        buf.append("<OPTION VALUE=Instructor" + (status.equals("Instructor") ? " SELECTED>" : ">") + res.getString("str_status_instructor") + "</OPTION>");
        buf.append("<OPTION VALUE=TA" + (status.equals("TA") ? " SELECTED>" : ">") + res.getString("str_status_ta") + "</OPTION>");
        buf.append("</SELECT>");
        return buf.toString();
    }

    void deleteScore(String scoreID) {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            ResultSet rsCodeType = stmt.executeQuery("SELECT Code, TestType FROM Scores WHERE " + "ScoreID='" + scoreID + "'");
            String sqlUpdateString = "DELETE FROM Scores WHERE ScoreID='" + scoreID + "'";
            if (stmt.executeUpdate(sqlUpdateString) != 1) {
                return;
            }
            if (!rsCodeType.next()) {
                return;
            }
            int code = rsCodeType.getInt("Code");
            String testType = rsCodeType.getString("TestType");
            if (code == -1 || testType.equals("void")) {
                return;
            }
            String mysqlString = "DELETE FROM " + testType + "AssignedQuestions WHERE " + "Code='" + code + "'";
            if (stmt.executeUpdate(mysqlString) != 1) {
                return;
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
        }
        return;
    }

    void addScore(String studentIDNumber, String assignment, int score, String ipNumber) {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            String sqlUpdateString = "INSERT INTO Scores " + "(StudentIDNumber,Assignment,Score,IPAddress,Code,TestType) " + "VALUES ('" + studentIDNumber + "','" + converter(assignment, 0) + "'," + score + ",'" + ipNumber + "','-1','void')";
            if (stmt.executeUpdate(sqlUpdateString) != 1) {
                return;
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
        }
        return;
    }

    String converter(String oldString, int fromIndex) {
        int i = oldString.indexOf('\'', fromIndex);
        return i < 0 ? oldString : converter(new StringBuffer(oldString).insert(i, '\\').toString(), i + 2);
    }

    void deleteStudent(String id) {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM Students WHERE StudentIDNumber='" + id + "'");
            stmt.executeUpdate("DELETE FROM Scores WHERE StudentIDNumber='" + id + "'");
            stmt.close();
            conn.close();
        } catch (Exception e) {
        }
        return;
    }

    void updateStudent(String id, HttpServletRequest request, TA ta) {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            String newStatus = request.getParameter("Status" + id);
            String newSection = request.getParameter("Section" + id);
            String sqlUpdateString = "UPDATE Students " + "SET SectionID='" + newSection + "'," + " Status='" + newStatus + "' " + "WHERE StudentIDNumber='" + id + "'";
            if (ta != null) {
                if (ta.getPermission("Gradebook_UpdateStudent").getPermissionLevel().equals(TAPermission.PERM_CONDITIONAL)) {
                    Student stud = (Student) (ta.getAssignment(TA.ASSIGNMENT_STUDENT, id));
                    if (stud == null || stud.getIsTA()) {
                        return;
                    }
                    sqlUpdateString = "UPDATE Students SET ";
                    if (ta.isAssigned(TA.ASSIGNMENT_SECTION, newSection)) {
                        sqlUpdateString += "SectionID='" + newSection + "', ";
                    } else {
                        sqlUpdateString += "SectionID=SectionID, ";
                    }
                    if (newStatus.equals("TA")) {
                        sqlUpdateString += "Status=Status";
                    } else {
                        sqlUpdateString += "Status='" + newStatus + "'";
                    }
                    sqlUpdateString += " WHERE StudentIDNumber='" + id + "'";
                }
            }
            if (newStatus.equals("TA")) {
                if (ta == null || ta.getPermission("Gradebook_UpdateStudent").getPermissionLevel().equals(TAPermission.PERM_ALL)) {
                    TA newTa = new TA(id, false, true);
                    newTa.setupNewTA();
                }
            }
            if (stmt.executeUpdate(sqlUpdateString) != 1) {
                throw new Exception("Unknown database error.");
            }
        } catch (Exception e) {
            return;
        }
        return;
    }

    String showAnswers(String studentID, String scoreID) {
        StringBuffer buf = new StringBuffer();
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            ResultSet rsCodeType = stmt.executeQuery("SELECT Code, Assignment, TestType FROM Scores WHERE" + " ScoreID='" + scoreID + "'");
            if (!rsCodeType.next() || rsCodeType.getString("Code") == null || rsCodeType.getString("TestType") == null) {
                return res.getString("str_invalid_score");
            }
            if (rsCodeType.getString("TestType").equals("Review")) {
                return res.getString("str_is_review");
            }
            int code = rsCodeType.getInt("Code");
            String testType = rsCodeType.getString("TestType");
            String assignment = rsCodeType.getString("Assignment");
            rsCodeType.close();
            if (code == -1 || testType.equals("void")) {
                return res.getString("str_score_by_teacher");
            }
            try {
                ResultSet rsTrackAnswers = stmt.executeQuery("SELECT TrackAnswers FROM " + testType + "Parameters");
                if (rsTrackAnswers.next() && !rsTrackAnswers.getBoolean("TrackAnswers")) {
                    buf.append(res.getString("str_tracking_disabled"));
                    if (testType.equals("Quiz")) {
                        buf.append(res.getString("str_quizzes"));
                    } else if (testType.equals("Homework")) {
                        buf.append(res.getString("str_homework"));
                    } else {
                        buf.append(res.getString("str_exams"));
                    }
                    return buf.toString();
                }
            } catch (Exception e) {
                if (addParam(testType)) {
                    return (res.getString("str_param_track_answers_added1") + testType + res.getString("str_param_track_answers_added2"));
                }
            }
            ResultSet rsStudentName = stmt.executeQuery("SELECT CONCAT(FirstName,' ',LastName)" + " AS Name " + "FROM Students WHERE StudentIDNumber LIKE '" + studentID + "'");
            String mySqlQuery1 = "SELECT * FROM " + testType + "AssignedQuestions WHERE " + "Code='" + code + "'";
            buf.append("<h3>".concat(assignment).concat("</h3>"));
            buf.append("<div align=center><h2>");
            if (rsStudentName.next()) {
                buf.append(rsStudentName.getString("Name"));
            } else {
                return res.getString("str_invalid_student");
            }
            rsStudentName.close();
            buf.append(res.getString("str_answers") + " </h2></div>\n<FORM METHOD=POST>\n" + "<INPUT type=hidden name=StudentIDNumber value='" + studentID + "'>\n<ol>");
            ResultSet rs1 = stmt.executeQuery(mySqlQuery1);
            if (rs1.next()) {
                do {
                    String mySqlQuery2 = "SELECT * FROM " + testType + "Questions WHERE QuestionID='" + rs1.getString("QuestionID") + "'";
                    ResultSet rs2 = stmt2.executeQuery(mySqlQuery2);
                    if (!rs2.next()) {
                        buf.append("<li>" + res.getString("str_invalid_question"));
                        continue;
                    }
                    IQuestion question = QuestionFactory.createQuestion(rs2);
                    question.setAssignmentType(testType);
                    question.setCode(code);
                    if (rs1.getBoolean("Graded")) {
                        buf.append("<li>" + question.printCorrection(rs1.getString("StudentAnswer")));
                    } else {
                        buf.append("<li>" + question.print());
                    }
                    rs2.close();
                } while (rs1.next());
            } else {
                buf.append(res.getString("str_not_avail_answers"));
            }
            buf.append("</ol><INPUT type=hidden name=UserRequest value='ScoresDetail'>" + "<INPUT type=SUBMIT value='" + res.getString("str_return_to_score") + "'>\n</FORM>");
            stmt.close();
            stmt2.close();
            conn.close();
        } catch (Exception e) {
            if (createAQTable()) {
                return res.getString("str_tables_created");
            }
            return (e.getMessage());
        }
        return buf.toString();
    }

    String scoresDetail(String id) {
        StringBuffer buf = new StringBuffer();
        SimpleDateFormat dfDateTime = new SimpleDateFormat("MM-dd-yyyy h:mm a");
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            String sqlStr1 = "SELECT CONCAT(FirstName,' ',LastName) AS Name " + "FROM Students WHERE StudentIDNumber LIKE '" + id + "'";
            ResultSet rs1 = stmt.executeQuery(sqlStr1);
            if (!rs1.next()) {
                return res.getString("str_student_not_found");
            }
            buf.append("<h4>" + res.getString("str_scdetail_for") + rs1.getString("Name") + "</h4>");
            String sqlStr2 = "SELECT ScoreID,Assignment,Score,Timestamp,IPAddress FROM Scores " + "WHERE StudentIDNumber LIKE '" + id + "' ORDER BY ScoreID DESC";
            ResultSet rs2 = stmt.executeQuery(sqlStr2);
            buf.append("<FORM><INPUT TYPE=SUBMIT VALUE='" + res.getString("str_return_gradebook") + "'></FORM><HR>");
            buf.append("<FORM METHOD=POST>");
            buf.append("<table border=1 cellspacing=0>" + "<tr><td><b>" + res.getString("str_assignment") + "</b></td><td><b>" + res.getString("str_score_input") + "</b></td></tr>" + "<tr><td><input name=Assignment></td><td align=center><input size=3 name=Score></td></tr>" + "</table>" + "<input type=hidden name=StudentIDNumber value='" + id + "'>" + "<input type=hidden name=UserRequest value=AddScore>" + "<input type=submit value='" + res.getString("str_add_score") + "'>" + "</FORM>");
            buf.append("\n<HR><FORM METHOD=POST>");
            buf.append("\n<input type=submit onClick=this.form.elements.UserRequest.value='DeleteScore'; value='" + res.getString("str_delete_score") + "'>");
            buf.append("<input type=submit onClick=this.form.elements.UserRequest.value='ShowAnswers'; value='" + res.getString("str_show_answers") + "'>");
            buf.append("\n<table cellspacing=0 border=1>" + "\n<tr><td></td>" + "<td><b>" + res.getString("str_field_assignment") + "</b></td><td><b>" + res.getString("str_field_score") + "</b></td>" + "<td><b>" + res.getString("str_field_timestamp") + "</b></td><td><b>" + res.getString("str_field_ip") + "</b></td></tr>");
            while (rs2.next()) {
                buf.append("\n<tr><td><input type=radio name=ScoreID value='" + rs2.getString("ScoreID") + "'></td>" + "<td>" + rs2.getString("Assignment") + "</td>" + "<td ALIGN=CENTER>" + (rs2.getInt("Score") < 0 ? res.getString("str_not_graded") : rs2.getString("Score")) + "</td>" + "<td>" + dfDateTime.format(rs2.getTimestamp("Timestamp")) + "</td>" + "<td>" + rs2.getString("IPAddress") + "</td></tr>");
            }
            buf.append("\n</table>");
            buf.append("\n<input type=hidden name=StudentIDNumber value='" + id + "'>");
            buf.append("\n<input type=hidden name=UserRequest>");
            buf.append("\n<input type=submit onClick=this.form.elements.UserRequest.value='DeleteScore'; value='" + res.getString("str_delete_score") + "'>");
            buf.append("<input type=submit onClick=this.form.elements.UserRequest.value='ShowAnswers'; value='" + res.getString("str_show_answers") + "'>");
            if (hasGradedEssays(id)) {
                buf.append("<br><input type=submit onClick=this.form.elements.UserRequest.value='ShowGradedEssays' value='" + res.getString("str_review_essays") + "'>");
            }
            buf.append("\n</FORM>");
            buf.append("<hr><FORM><INPUT TYPE=SUBMIT VALUE='" + res.getString("str_return_gradebook") + "'></FORM>");
        } catch (Exception e) {
            if (addCodeType()) {
                return res.getString("str_fields_created");
            }
            return (e.getMessage());
        }
        return buf.toString();
    }

    boolean addCodeType() {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("ALTER TABLE Scores ADD COLUMN Code INT");
            stmt.executeUpdate("ALTER TABLE Scores ADD COLUMN TestType VARCHAR(8)");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean addParam(String testType) {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("ALTER TABLE " + testType + "Parameters ADD TrackAnswers VARCHAR(5)");
            stmt.executeUpdate("UPDATE " + testType + "Parameters SET TrackAnswers='false'");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean createAQTable() {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE QuizAssignedQuestions (Code INT, QuestionID INT, Graded VARCHAR(5), StudentAnswer TEXT)");
            stmt.executeUpdate("CREATE TABLE ExamAssignedQuestions (Code INT, QuestionID INT, Graded VARCHAR(5), StudentAnswer TEXT)");
            stmt.executeUpdate("CREATE TABLE HomeworkAssignedQuestions (Code INT, QuestionID INT, Graded VARCHAR(5), StudentAnswer TEXT)");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean hasUngradedQuestions() {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            String sqlQueryString = "Select * from Essays WHERE Graded='false'";
            ResultSet rsGraded = stmt.executeQuery(sqlQueryString);
            if (rsGraded.next()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    boolean hasGradedEssays(String id) {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            String sqlQueryString = "Select * from Essays WHERE Graded='true' AND StudentIDNumber='" + id + "'";
            ResultSet rsGraded = stmt.executeQuery(sqlQueryString);
            if (rsGraded.next()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    String gradeEssayForm(String studentIDNumber) {
        StringBuffer buf = new StringBuffer();
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            String sqlQueryString = "Select * from Essays WHERE StudentIDNumber='";
            sqlQueryString += studentIDNumber + "' AND Graded='false'";
            ResultSet rsEssays = stmt.executeQuery(sqlQueryString);
            if (!rsEssays.next()) {
                return res.getString("str_no_ungraded_essays") + "<form><input type=submit value='" + res.getString("str_return_gradebook") + ".'></form>";
            }
            buf.append("<h3>" + res.getString("str_ungrade_essays") + studentIDNumber + "</h3>");
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("urPar", "UserRequest");
            params.put("assignPar", "Assignment");
            params.put("ansPar", "Answer");
            params.put("scPar", "Score");
            params.put("pscPar", "PossibleScore");
            params.put("updatePar", "UpdateEssay");
            params.put("deletePar", "DeleteEssay");
            params.put("emPar", "Email");
            params.put("qidPar", "QuestionID");
            params.put("sidPar", "StudentIDNumber");
            params.put("ttPar", "TestType");
            params.put("gradePar", "GradeEssays");
            do {
                ResultSet rsQuestion = stmt2.executeQuery("Select * from " + rsEssays.getString("TestType") + "Questions WHERE QuestionID='" + rsEssays.getString("QuestionID") + "'");
                if (!rsQuestion.next()) {
                    buf.append(rsEssays.getString("TestType")).append("-").append(rsEssays.getString("QuestionID")).append("-").append(res.getString("str_error_question"));
                    buf.append("<form method='post'><input type='hidden' name='UserRequest' value='DeleteEssay'><input type='hidden' name='Code' value='").append(rsEssays.getString("Code")).append("'><input type='hidden' name='QuestionID' value='").append(rsEssays.getString("QuestionID")).append("'><input type='hidden' name='TestType' value='").append(rsEssays.getString("TestType")).append("'><input type='Submit' value='").append(res.getString("str_delete_answer")).append("'><input type='hidden' name='StudentIDNumber' value='").append(studentIDNumber).append("'></form>");
                    continue;
                }
                IQuestion q = QuestionFactory.createQuestion(rsQuestion);
                if (!(q instanceof ITeacherGradedQuestion)) {
                    continue;
                }
                q.setAssignmentType(rsEssays.getString("TestType"));
                params.put("codePar", "Code");
                params.put("Code", rsEssays.getString("Code"));
                ((ITeacherGradedQuestion) q).printForTeacherGrading(buf, rsEssays.getString("Answer"), params, studentIDNumber);
            } while (rsEssays.next());
            buf.append("\n<FORM><INPUT TYPE=SUBMIT VALUE='" + res.getString("str_return_gradebook") + "'></FORM>");
        } catch (Exception e) {
            return e.getMessage();
        }
        return buf.toString();
    }

    boolean updateEssay(Student student, String studentIDNumber, String questionID, String testType, String gradedAnswer, int score, String assignment, boolean email, int possibleScore, String code) {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            String sqlUpdateString = "Update Essays SET Graded='true', Answer='" + CharHider.quot2literal(gradedAnswer) + "', Score='" + score + "' WHERE Graded='false' AND StudentIDNumber='" + studentIDNumber + "' AND QuestionID='" + questionID + "' AND TestType='" + testType + "' AND Code='" + code + "'";
            stmt.executeUpdate(sqlUpdateString);
            sqlUpdateString = "DELETE From Scores WHERE StudentIDNumber='" + studentIDNumber + "' AND TestType='" + testType + "' AND Score='-" + questionID + "' AND Code='" + code + "'";
            stmt.executeUpdate(sqlUpdateString);
            sqlUpdateString = "Update Scores Set Score=Score+" + score + " WHERE StudentIDNumber='" + studentIDNumber + "' AND Assignment='" + assignment + "' AND Code='" + code + "'";
            ResultSet rsAssignment = stmt.executeQuery("select AssignmentNumber from " + testType + "Questions where QuestionID='" + questionID + "'");
            int assignmentNum;
            if (rsAssignment.next()) {
                assignmentNum = rsAssignment.getInt("AssignmentNumber");
            } else {
                assignmentNum = -1;
            }
            stmt.executeUpdate(sqlUpdateString);
            sqlUpdateString = "Update " + testType + "Transactions SET PossibleScore=PossibleScore + " + possibleScore + ", Date=Date WHERE StudentIDNumber='" + studentIDNumber + "' AND AssignmentNumber='" + assignmentNum + "' AND Code='" + code + "'";
            stmt.executeUpdate(sqlUpdateString);
            if (email) {
                sendMessage(student, studentIDNumber, Message.RecipientType.TO, (testType + ": Question " + questionID + " Feedback --\n" + gradedAnswer + "\n\rFinal Score: " + score));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean deleteEssay(String studentIDNumber, String questionID, String testType, String code) {
        boolean ret;
        try {
            IQuestion q = QuestionFactory.createQuestion(questionID, testType);
            if (q instanceof ITeacherGradedQuestion) {
                ret = ((ITeacherGradedQuestion) q).deleteResponse(studentIDNumber, code);
            } else {
                ret = false;
            }
        } catch (Exception e) {
            ret = false;
        }
        return ret;
    }

    String gradedEssays(String id) {
        StringBuffer buf = new StringBuffer();
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            String sqlQueryString = "Select * from Essays WHERE StudentIDNumber='";
            sqlQueryString += id + "' AND Graded='true'";
            ResultSet rsEssays = stmt.executeQuery(sqlQueryString);
            if (!rsEssays.next()) {
                return res.getString("str_no_graded_essays");
            }
            buf.append("<h3>").append(res.getString("str_graded_essays")).append(id).append("</h3><table>");
            do {
                ResultSet rsQuestion = stmt2.executeQuery("Select * from " + rsEssays.getString("TestType") + "Questions WHERE QuestionID='" + rsEssays.getString("QuestionID") + "'");
                if (rsQuestion.next()) {
                    IQuestion q = QuestionFactory.createQuestion(rsQuestion);
                    if (!(q instanceof ITeacherGradedQuestion)) {
                        continue;
                    }
                    ((ITeacherGradedQuestion) q).printGradedQuestion(buf, rsEssays.getString("Answer"), rsEssays.getInt("Score"));
                } else {
                    buf.append("\n<tr><td><b>" + res.getString("str_field_score") + ":</b></td><td width=70%> " + rsEssays.getString("Score") + "</td></tr>");
                    buf.append("<tr><td valign=top><b>" + res.getString("str_field_graded_answer") + ":</b></td><td width=70%>" + rsEssays.getString("Answer") + "</td></tr>\n");
                    buf.append("<tr><td height=15 colspan=2><hr></td></tr>");
                }
            } while (rsEssays.next());
            buf.append("\n</table>");
            buf.append("<FORM METHOD=POST><INPUT TYPE=hidden name='StudentIDNumber' value='" + id + "'><INPUT TYPE=hidden name='UserRequest' VALUE='ScoresDetail'><INPUT TYPE=SUBMIT VALUE='" + res.getString("str_return_to_score") + "'></FORM>");
        } catch (Exception e) {
            return e.getMessage();
        }
        return buf.toString();
    }

    String sendMessage(Student student, String id, Message.RecipientType type, String text) {
        StringBuffer err = new StringBuffer();
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            ResultSet rsRecipient = stmt.executeQuery("SELECT Email FROM Students WHERE StudentIDNumber='" + id + "'");
            if (!rsRecipient.next()) {
                return res.getString("str_student_not_found");
            }
            Properties props = System.getProperties();
            props.put("mail.smtp.host", Course.outgoingMailServer);
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(student.email, student.getFullName()));
            message.addRecipients(type, rsRecipient.getString("Email"));
            message.setSubject(res.getString("str_mail_subject"));
            message.setText(text);
            Transport.send(message);
            return res.getString("str_mail_sent");
        } catch (Exception e) {
            return "sendMessage error: " + err + e.getMessage();
        }
    }

    String resetStudentPasswordForm(String studentID) {
        StringBuffer buf = new StringBuffer();
        buf.append("<h3>" + res.getString("str_change_passwd") + studentID + "</h3>");
        buf.append("<SCRIPT><!--");
        buf.append("\nfunction verifypass(){" + "\n if(document.passform.StudentPass.value.length < 6 || document.passform.StudentPass.value.length > 12){" + "\n  alert('" + res.getString("str_invalid_passwd") + "');\n  return false;\n }\n return true;\n}" + "\n --> </SCRIPT>");
        buf.append("<FORM METHOD=POST NAME=passform><TABLE BORDER=0><TR><TD>" + res.getString("str_new_passwd") + ":</TD>");
        buf.append("<TD><INPUT TYPE=PASSWORD NAME='StudentPass'></TD></TR>");
        buf.append("<TR><TD>" + res.getString("str_teach_passwd") + ":</TD>");
        buf.append("<TD><INPUT TYPE=PASSWORD NAME='TeacherPass'></TD></TR></TABLE>");
        buf.append("<INPUT TYPE=HIDDEN NAME=UserRequest Value='ResetStudentPassword'>" + "<INPUT TYPE=HIDDEN NAME=StudentIDNumber VALUE='" + studentID + "'>" + "<INPUT TYPE=BUTTON VALUE='" + res.getString("str_set_passwd") + "' onClick=\"if (verifypass()){document.passform.submit()}\"></FORM>");
        return buf.toString();
    }
}

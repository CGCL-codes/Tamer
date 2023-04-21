package org.eledge;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*******************************************************************************
 * @deprecated
 * 
 */
@Deprecated
public class Homework extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -3144377564910431012L;

    private boolean canSaveImages = false;

    int nSubjectAreas = 1;

    int nQuestionsPerSubjectArea = 100;

    int timeLimit = 0;

    int waitForNewDownload = 0;

    boolean enforceDeadlines = true;

    boolean allowMultipleTries = true;

    boolean scrambleQuestions = true;

    boolean allowWorkAhead = false;

    boolean showMissedQuestions = true;

    boolean useSectionDeadlines = false;

    int numberOfSections = 1;

    boolean trackAnswers = false;

    private RBStore res = EledgeResources.getHomeworkBundle();

    private Logger log = new Logger();

    @Override
    public String getServletInfo() {
        return res.getString("str_servlet_info");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Student student = (Student) session.getAttribute(Course.name + "Student");
        if (student == null) {
            student = new Student();
        }
        if (!student.isAuthenticated()) {
            response.sendRedirect(Course.secureLoginURL + "Homework");
        }
        if (student.getIsFrozen()) {
            out.println(Page.create(res.getString("str_act_frozen")));
            return;
        }
        if (student.getIsTA()) {
            TA ta = TAS.getTA(student.getIDNumber());
            StringBuffer err = new StringBuffer();
            if (!ta.hasPermission("Homework", request, student, err)) {
                out.println(Page.create(err.toString()));
                return;
            }
        }
        getHomeworkParameters();
        if (timeLimit > 0 && session.getMaxInactiveInterval() < (timeLimit * 60 + 300)) {
            session.setMaxInactiveInterval(timeLimit * 60 + 300);
        }
        out.println(Page.create(homeworkSelectForm(student)));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Student student = (Student) session.getAttribute(Course.name + "Student");
        if (student == null) {
            student = new Student();
        }
        if (!student.isAuthenticated()) {
            response.sendRedirect(Course.secureLoginURL + "Homework");
        }
        if (student.getIsFrozen()) {
            out.println(Page.create(res.getString("str_act_frozen")));
            return;
        }
        if (student.getIsTA()) {
            TA ta = TAS.getTA(student.getIDNumber());
            StringBuffer err = new StringBuffer();
            if (!ta.hasPermission("Homework", request, student, err)) {
                out.println(Page.create(err.toString()));
                return;
            }
        }
        getHomeworkParameters();
        String userRequest = request.getParameter("UserRequest");
        if (userRequest == null) {
            out.println(Page.create(homeworkSelectForm(student)));
            return;
        }
        int assignmentNumber = -1;
        try {
            assignmentNumber = Integer.parseInt(request.getParameter("AssignmentNumber"));
        } catch (Exception e) {
            out.println(Page.create(res.getString("str_must_select_valid")));
            return;
        }
        if (userRequest.equals("NewHomework")) {
            if (okTimeForNewHomework(student, assignmentNumber, out)) {
                out.println(Page.create(printHomework(student, assignmentNumber, request.getRemoteAddr())));
            }
            return;
        }
        if (userRequest.equals("GradeHomework")) {
            int code = Integer.parseInt(request.getParameter("Code"));
            int possibleScore = Integer.parseInt(request.getParameter("PossibleScore"));
            out.println(Page.create(gradeHomework(student, assignmentNumber, possibleScore, code, request)));
            return;
        }
        out.println(Page.create(res.getString("str_no_understand")));
    }

    String homeworkSelectForm(Student student) {
        StringBuffer buf = new StringBuffer();
        int sectionID = student.sectionID;
        String sectionName;
        buf.append("<b>" + student.getFullName() + "</b>");
        Date now = new Date();
        buf.append("<br>" + now);
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            ResultSet rsParams = stmt.executeQuery("SELECT Value FROM CourseParameters WHERE Name='NumberOfSections'");
            if (rsParams.next()) {
                numberOfSections = rsParams.getInt("Value");
            }
            ResultSet rsSections = stmt.executeQuery("SELECT * FROM CourseSections WHERE SectionID='" + sectionID + "'");
            if (rsSections.next()) {
                sectionName = rsSections.getString("SectionName");
            } else {
                sectionID = 1;
                sectionName = "";
            }
            if (useSectionDeadlines) {
                buf.append("<br>Section " + sectionName);
            } else {
                sectionID = 1;
            }
            Timestamp deadline;
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy h:mm a");
            buf.append("<form method=POST");
            if (!allowMultipleTries) {
                buf.append(" onSubmit=\"return confirm('" + res.getString("str_confirm_submit") + "')\"");
            }
            buf.append(">");
            buf.append(res.getString("str_select_hw") + "<br>");
            buf.append("<table cellspacing=0 border=1>");
            buf.append("<tr><td></td><td><b>" + res.getString("str_field_hw") + "</b></td><td><b>" + res.getString("str_field_title") + "</b></td><td><b>" + res.getString("str_field_deadline") + "</b></td></tr>");
            int assignmentNumber;
            boolean previousHomeworkExpired = true;
            ResultSet rs;
            try {
                rs = stmt.executeQuery("SELECT AssignmentNumber,Title,Deadline" + sectionID + " AS Deadline FROM HomeworkInfo ORDER BY DEADLINE");
            } catch (Exception e) {
                rs = stmt.executeQuery("SELECT AssignmentNumber,Title,Deadline1 AS Deadline FROM HomeworkInfo ORDER BY DEADLINE");
            }
            while (rs.next()) {
                assignmentNumber = rs.getInt("AssignmentNumber");
                deadline = rs.getTimestamp("Deadline");
                if (deadline.before(now)) {
                    buf.append("<tr><td><input type=radio name='AssignmentNumber' value=" + assignmentNumber + "></td>");
                } else if (previousHomeworkExpired) {
                    buf.append("<tr BGCOLOR=FFFF00><td><input type=radio name='AssignmentNumber' value=" + assignmentNumber + " CHECKED></td>");
                    previousHomeworkExpired = false;
                } else if (allowWorkAhead || student.getIsInstructor()) {
                    buf.append("<tr><td><input type=radio name='AssignmentNumber' value=" + assignmentNumber + "></td>");
                } else {
                    buf.append("<tr><td align=center><font color=FF0000 size=-2>n/a</font></td>");
                }
                buf.append("<td ALIGN=CENTER>" + assignmentNumber + "</td>");
                buf.append("<td>" + rs.getString("Title") + "</td>");
                buf.append("<td>" + df.format(deadline) + "</td>");
                buf.append("</tr>");
            }
        } catch (Exception e) {
            return createHomeworkTables(student);
        }
        buf.append("</table>");
        buf.append("<input type=hidden name='UserRequest' value='NewHomework'>");
        buf.append("<input type=submit value='" + res.getString("str_btn_display_hw") + "'>");
        buf.append("</form>");
        if (student.getIsInstructor()) {
            buf.append("<FORM ACTION=" + Course.name + ".ManageHomework>" + "<b>" + res.getString("str_teach_only") + "</b><input type=submit value='" + res.getString("str_btn_manage_hw") + "'></FORM>");
        }
        buf.append(homeworkRules());
        return buf.toString();
    }

    String homeworkRules() {
        StringBuffer buf = new StringBuffer();
        MessageFormat mf = new MessageFormat("str_rule_deadline");
        Object[] args = { new Integer(timeLimit) };
        buf.append("<h2>" + res.getString("str_title_rules") + "</h2>");
        buf.append("<ul>");
        if (enforceDeadlines) {
            buf.append("<li>" + res.getString("str_rule_deadline") + "</li>");
        }
        if (allowMultipleTries) {
            buf.append("<li>" + res.getString("str_rule_allow_multiple") + "</li>");
        } else {
            buf.append("<li>" + res.getString("str_rule_deny_multiple1") + "<i>" + res.getString("str_rule_deny_multiple2") + "</i></li>");
        }
        if (timeLimit != 0) {
            buf.append("<li>" + mf.format(args) + "</li>");
        }
        mf.applyPattern(res.getString("str_rule_dlrate"));
        args[0] = new Integer(waitForNewDownload);
        if (waitForNewDownload > 0) {
            buf.append("<li>" + mf.format(args) + "</li>");
        }
        buf.append("</ul>");
        return buf.toString();
    }

    boolean okTimeForNewHomework(Student student, int assignmentNumber, PrintWriter out) {
        if (waitForNewDownload == 0) {
            return true;
        }
        if (student.getIsInstructor()) {
            return true;
        }
        ResultSet rsTimestamp;
        Statement stmt;
        boolean returnValue;
        MessageFormat mf = new MessageFormat(res.getString("str_must_wait"));
        Object[] args = { new Integer(waitForNewDownload) };
        Calendar now = Calendar.getInstance();
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            stmt = conn.createStatement();
            rsTimestamp = stmt.executeQuery("SELECT MAX(Date-ElapsedMinutes*100) AS StartTime FROM HomeworkTransactions " + "WHERE StudentIDNumber='" + student.getIDNumber() + "' AND AssignmentNumber='" + assignmentNumber + "' GROUP BY StudentIDNumber");
            if (rsTimestamp.next()) {
                Calendar then = Calendar.getInstance();
                then.setTime((Date) rsTimestamp.getTimestamp("StartTime"));
                then.add(Calendar.MINUTE, waitForNewDownload);
                if (!now.after(then)) {
                    String ret = mf.format(args) + "<br>";
                    mf.applyPattern(res.getString("str_curr_time"));
                    args[0] = now.getTime();
                    ret += mf.format(args) + "<br>";
                    mf.applyPattern(res.getString("str_next_avail"));
                    args[0] = then.getTime();
                    ret += mf.format(args) + "<br>" + res.getString("str_try_reloading");
                    out.println(Page.create(ret));
                }
                returnValue = now.after(then);
            } else {
                returnValue = true;
            }
            rsTimestamp.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            returnValue = false;
        }
        return returnValue;
    }

    String printHomework(Student student, int assignmentNumber, String ipNumber) {
        Score score = new Score();
        int code;
        String sqlInsert;
        String title = "";
        StringBuffer buf = new StringBuffer();
        StringBuffer functionsBuf = new StringBuffer("\n  function runCommands() {\n");
        StringBuffer scriptsBuf = new StringBuffer("\n<script language=\"javascript\">\n<!--\n");
        MessageFormat mf = new MessageFormat(res.getString("str_title_assignment"));
        Object[] args = new Object[2];
        buf.append("<b>" + student.firstName + " " + student.lastName + "</b>");
        Date now = new Date();
        buf.append("<br>" + now);
        Random rand = new Random();
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            ResultSet rsInfo = stmt.executeQuery("SELECT * FROM HomeworkInfo WHERE AssignmentNumber='" + assignmentNumber + "'");
            if (rsInfo.next()) {
                nSubjectAreas = rsInfo.getInt("NSubjectAreas");
                nQuestionsPerSubjectArea = rsInfo.getInt("NQuestionsPerSubjectArea");
                title = rsInfo.getString("Title");
            }
            args[0] = new Integer(assignmentNumber);
            args[1] = title;
            buf.append("<h3>" + mf.format(args) + "</h3>");
            buf.append("<form METHOD=POST onSubmit=\"runCommands(); return confirm('" + res.getString("str_confirm_grade") + "?');\">");
            buf.append("<OL>");
            rsInfo.close();
            ResultSet rsCode = stmt.executeQuery("SELECT Code FROM HomeworkTransactions WHERE " + "StudentIDNumber='" + student.getIDNumber() + "' AND AssignmentNumber='" + assignmentNumber + "' AND StudentScore IS NULL ORDER BY Date");
            if (rsCode.last()) {
                code = rsCode.getInt("Code");
            } else {
                do {
                    code = rand.nextInt(99999999);
                    sqlInsert = "INSERT INTO HomeworkTransactions" + " (StudentIDNumber,LastName,FirstName,AssignmentNumber,Code,IPNumber)" + " VALUES ('" + student.getIDNumber() + "','" + converter(student.lastName) + "','" + converter(student.firstName) + "','" + assignmentNumber + "','" + code + "','" + ipNumber + "')";
                } while (dbSQLRequest(sqlInsert) != 1);
            }
            rand.setSeed((long) code);
            for (int area = 0; area < nSubjectAreas; area++) {
                Vector<IQuestion> questions = new Vector<IQuestion>();
                String sqlQueryString = "SELECT * FROM HomeworkQuestions WHERE AssignmentNumber='" + assignmentNumber + "' AND SubjectArea='" + area + "' AND (Section='All' OR Section='" + student.sectionID + "') ORDER BY QuestionID";
                ResultSet rsQuestions = stmt.executeQuery(sqlQueryString);
                while (rsQuestions.next()) {
                    IQuestion question = QuestionFactory.createQuestion(rsQuestions);
                    questions.addElement(question);
                }
                rsQuestions.close();
                int nQuestions = nQuestionsPerSubjectArea < questions.size() ? nQuestionsPerSubjectArea : questions.size();
                PrintAssessmentWrapper wrapper = new PrintAssessmentWrapper(buf, functionsBuf, scriptsBuf, score, Integer.toString(code));
                wrapper.setCanSaveImages(canSaveImages);
                wrapper.setStudent(student);
                for (; wrapper.getQuestionCount() < nQuestions; wrapper.incrementQuestionCount()) {
                    int q = scrambleQuestions ? rand.nextInt(questions.size()) : 0;
                    IQuestion selected = questions.remove(q);
                    selected.setAssignmentType("Homework");
                    selected.printForAssessment(wrapper);
                    if (wrapper.getContinueOnReturn()) {
                        continue;
                    }
                    if (trackAnswers) {
                        String mySqlInsertString = "INSERT INTO HomeworkAssignedQuestions VALUES('" + code + "','" + selected.getID() + "','" + selected.getQuestionGraded() + "','null')";
                        stmt.executeUpdate(mySqlInsertString);
                    }
                }
            }
            buf.append("</ol>");
            stmt.close();
            conn.close();
        } catch (Exception e) {
            if (addSectionField()) {
                return res.getString("str_tbl_updated");
            }
            if (addAQTable()) {
                return res.getString("str_tbl_updated");
            } else if (addEssayTable()) {
                return res.getString("str_essaytbl_added");
            }
            return e.getMessage();
        }
        buf.append("<input type=hidden name='Code' value=" + code + ">");
        buf.append("<input type=hidden name='PossibleScore' value=" + score.getPossibleScore() + ">");
        buf.append("<input type=hidden name='AssignmentNumber' value=" + assignmentNumber + ">");
        buf.append("<input type=hidden name='UserRequest' value='GradeHomework'>");
        buf.append("<input type=submit value='" + res.getString("str_btn_compute") + "'>");
        buf.append("</form>");
        functionsBuf.append("\n}\n");
        scriptsBuf.append(functionsBuf);
        scriptsBuf.append("\n-->\n</script>");
        scriptsBuf.append(buf);
        return scriptsBuf.toString();
    }

    String converter(String oldString) {
        int i = oldString.indexOf('\'', 0);
        return i < 0 ? oldString : converter(new StringBuffer(oldString).insert(i, '\\').toString(), i + 2);
    }

    String converter(String oldString, int fromIndex) {
        int i = oldString.indexOf('\'', fromIndex);
        return i < 0 ? oldString : converter(new StringBuffer(oldString).insert(i, '\\').toString(), i + 2);
    }

    String gradeHomework(Student student, int assignmentNumber, int possibleScore, int code, HttpServletRequest request) {
        StringBuffer buf = new StringBuffer();
        MessageFormat mf = new MessageFormat(res.getString("str_grade_title"));
        Object[] args = { new Integer(assignmentNumber), null };
        buf.append("<b>" + student.firstName + " " + student.lastName + "</b>");
        Date now = new Date();
        buf.append("<br>" + now);
        buf.append("<h3>" + mf.format(args) + "</h3>");
        Score score = new Score();
        StringBuffer infoBuf = new StringBuffer();
        StringBuffer feedbackBuf = new StringBuffer();
        boolean eligibleForGrading = eligibleForGrading(student, assignmentNumber, code, infoBuf);
        boolean hasScore = false;
        try {
            FeedbackDetail fd = new FeedbackDetail();
            if (showMissedQuestions) {
                fd.setLevel(FeedbackDetail.DETAIL_QUESTION);
                fd.setLevel(FeedbackDetail.DETAIL_ID);
            } else {
                fd.clear();
            }
            hasScore = hasScore(code);
            AssessmentWrapper ew = new AssessmentWrapper(score, student, fd, feedbackBuf, infoBuf, code, eligibleForGrading, request.getRemoteAddr(), hasScore);
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            String sqlQueryString = "SELECT * FROM HomeworkQuestions WHERE AssignmentNumber='" + assignmentNumber + "' ORDER BY SubjectArea,QuestionID";
            ResultSet rsQuestions = stmt.executeQuery(sqlQueryString);
            while (rsQuestions.next()) {
                IQuestion question = QuestionFactory.createQuestion(rsQuestions);
                question.setAssignmentType("Homework");
                String studentAnswer[] = request.getParameterValues(question.getID());
                if (studentAnswer != null) {
                    for (int i = 1; i < studentAnswer.length; i++) {
                        studentAnswer[0] += studentAnswer[i];
                    }
                    question.gradeQuestion(studentAnswer[0], ew);
                    if (trackAnswers && !hasScore(code)) {
                        stmt.executeUpdate("UPDATE HomeworkAssignedQuestions SET Graded='" + question.getQuestionGraded() + "', StudentAnswer='" + converter(studentAnswer[0]) + "' WHERE Code='" + code + "' AND QuestionID='" + question.getID() + "'");
                    }
                }
            }
            rsQuestions.close();
            stmt.close();
            conn.close();
            if (score.getHasMissedQuestions()) {
                buf.append("<FONT COLOR=#FF0000><h4>").append(res.getString("str_title_incorrect")).append("</h4>").append(res.getString("str_title_left_blank")).append("</FONT><UL>");
                buf.append(feedbackBuf);
                buf.append("</UL><hr>");
            }
            mf.applyPattern(res.getString("str_hw_score"));
            args[0] = new Integer(score.getEarnedScore());
            args[1] = new Integer(possibleScore);
            buf.append(mf.format(args));
        } catch (Exception e) {
            buf.append(res.getString("str_err") + e.getMessage());
        }
        if (eligibleForGrading) {
            if (possibleScore > 0 && score.getEarnedScore() == possibleScore) {
                buf.append("<h2>" + res.getString("str_perfect_score") + "</h2>");
            }
            buf.append(recordScore(code, assignmentNumber, score.getEarnedScore(), possibleScore, student, request.getRemoteAddr()));
        } else {
            mf.applyPattern(res.getString("str_score_not_recorded"));
            args[0] = "<b>";
            args[1] = "</b>";
            buf.append("<BR>" + mf.format(args));
            if (trackAnswers && !hasScore(code)) {
                dbSQLRequest("DELETE FROM HomeworkAssignedQuestions WHERE Code='" + code + "'");
            }
        }
        return buf.toString();
    }

    boolean hasScore(int code) {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            ResultSet rsResult = stmt.executeQuery("SELECT * FROM Scores WHERE Code='" + code + "' AND TestType='Homework'");
            if (rsResult.next()) {
                return true;
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    String recordScore(int code, int assignmentNumber, int studentScore, int possibleScore, Student student, String ipAddress) {
        String sqlUpdateString = "UPDATE HomeworkTransactions SET StudentScore='" + studentScore + "',PossibleScore='" + possibleScore + "',ElapsedMinutes='" + elapsedMinutes(code) + "'" + " WHERE ((Code=" + code + ") AND (StudentScore IS NULL))";
        dbSQLRequest(sqlUpdateString);
        String sqlInsertString = "INSERT INTO Scores (StudentIDNumber,Assignment,Score,IPAddress, Code, TestType) Values ('" + student.getIDNumber() + "','HW" + (assignmentNumber < 10 ? "0" : "") + assignmentNumber + "','" + studentScore + "','" + ipAddress + "','" + code + "','Homework')";
        if (dbSQLRequest(sqlInsertString) == 1) {
            return "<BR>" + res.getString("str_score_recorded");
        }
        if (addCodeType()) {
            if (dbSQLRequest(sqlInsertString) == 1) {
                return "<BR>" + res.getString("str_score_recorded");
            }
        }
        return assignmentNumber + " " + studentScore + res.getString("str_score_record_err");
    }

    int dbSQLRequest(String sqlString) {
        int result = 0;
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            result = stmt.executeUpdate(sqlString);
            stmt.close();
            conn.close();
        } catch (Exception e) {
        }
        return result;
    }

    boolean eligibleForGrading(Student student, int assignmentNumber, int code, StringBuffer buf) {
        boolean eligible = true;
        MessageFormat mf = new MessageFormat(res.getString("str_too_long"));
        Object[] args = { null, new Integer(timeLimit) };
        if (!allowMultipleTries) {
            if (nTries(student, assignmentNumber) > 1) {
                eligible = false;
                buf.append("<br>" + res.getString("str_oneattempt"));
            }
        }
        if (timeLimit != 0) {
            int minutes = elapsedMinutes(code);
            args[0] = new Integer(minutes);
            if (minutes > timeLimit) {
                eligible = false;
                buf.append("<br>" + mf.format(args));
            }
        }
        if (enforceDeadlines) {
            if (deadlinePassed(student, assignmentNumber)) {
                eligible = false;
                buf.append("<br>" + res.getString("str_passed_deadline"));
            }
        }
        return eligible;
    }

    int nTries(Student student, int assignmentNumber) {
        int n = 0;
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            String sqlQueryString = "SELECT * FROM HomeworkTransactions WHERE (AssignmentNumber='" + assignmentNumber + "' AND StudentIDNumber='" + student.getIDNumber() + "')";
            ResultSet rs = stmt.executeQuery(sqlQueryString);
            while (rs.next()) {
                n++;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
        }
        return n;
    }

    int elapsedMinutes(int code) {
        log.paranoid("Begin Method.", "Homework:elapsedMinutes");
        Timestamp dateTaken = null;
        log.paranoid("dateTaken (should be null): " + dateTaken, "Homework:elapsedMinutes");
        int ret;
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            String sqlQueryString = "SELECT * FROM HomeworkTransactions WHERE Code=" + code;
            log.paranoid("executing: " + sqlQueryString, "Homework:elapsedMinutes");
            ResultSet rs = stmt.executeQuery(sqlQueryString);
            if (rs.next()) {
                dateTaken = rs.getTimestamp("Date");
            } else {
                return 99999;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            return 99999;
        }
        log.paranoid("dateTaken: " + dateTaken, "Homework:elapsedMinutes");
        log.paranoid("dT in millis: " + dateTaken.getTime(), "Homework:elapsedMinutes");
        Date now = new Date();
        log.paranoid("now: " + now, "Homework:elapsedMinutes");
        log.paranoid("now in millis: " + now.getTime(), "Homework:elapsedMinutes");
        long elapsedMilliseconds = now.getTime() - dateTaken.getTime();
        log.paranoid("elapsedMillis: " + elapsedMilliseconds, "Homework:elapsedMinutes");
        ret = (int) (elapsedMilliseconds / 60000);
        log.paranoid("returning: " + ret, "Homework:elapsedMinutes");
        return ret;
    }

    boolean deadlinePassed(Student student, int assignmentNumber) {
        Timestamp deadline = null;
        int sectionID = student.sectionID;
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            ResultSet rsParams = stmt.executeQuery("SELECT Value FROM CourseParameters WHERE Name='NumberOfSections'");
            if (useSectionDeadlines) {
                if (rsParams.next()) {
                    numberOfSections = rsParams.getInt("Value");
                }
                ResultSet rsSections = stmt.executeQuery("SELECT * FROM CourseSections WHERE SectionID='" + sectionID + "'");
                if (!rsSections.next()) {
                    sectionID = 1;
                }
            } else {
                sectionID = 1;
            }
            ResultSet rsDeadline;
            try {
                rsDeadline = stmt.executeQuery("SELECT Deadline" + sectionID + " AS Deadline FROM HomeworkInfo WHERE AssignmentNumber='" + assignmentNumber + "'");
            } catch (Exception e) {
                rsDeadline = stmt.executeQuery("SELECT Deadline1 AS Deadline FROM HomeworkInfo WHERE AssignmentNumber='" + assignmentNumber + "'");
            }
            if (rsDeadline.next()) {
                deadline = rsDeadline.getTimestamp("Deadline");
            } else {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        Date now = new Date();
        return deadline.before(now);
    }

    void getHomeworkParameters() {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            ResultSet rsParams = stmt.executeQuery("SELECT * FROM HomeworkParameters");
            rsParams.first();
            timeLimit = rsParams.getInt("TimeLimit");
            waitForNewDownload = rsParams.getInt("WaitForNewDownload");
            enforceDeadlines = rsParams.getBoolean("EnforceDeadlines");
            allowMultipleTries = rsParams.getBoolean("AllowMultipleTries");
            scrambleQuestions = rsParams.getBoolean("ScrambleQuestions");
            allowWorkAhead = rsParams.getBoolean("AllowWorkAhead");
            showMissedQuestions = rsParams.getBoolean("ShowMissedQuestions");
            useSectionDeadlines = rsParams.getBoolean("UseSectionDeadlines");
            trackAnswers = rsParams.getBoolean("TrackAnswers");
        } catch (Exception e) {
            addParam();
        }
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

    boolean addParam() {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("ALTER TABLE HomeworkParameters ADD TrackAnswers VARCHAR(5)");
            stmt.executeUpdate("UPDATE HomeworkParameters SET TrackAnswers='false'");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean addSectionField() {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("ALTER TABLE HomeworkQuestions ADD (Section VARCHAR(3) DEFAULT 'All')");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean addAQTable() {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE HomeworkAssignedQuestions (Code INT, QuestionID INT, Graded VARCHAR(5), StudentAnswer TEXT)");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean addEssayTable() {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE Essays (Code INT, QuestionID INT, Graded VARCHAR(5), Answer TEXT, StudentIDNumber VARCHAR(50), TestType VARCHAR(8), Score INT)");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    String createHomeworkTables(Student student) {
        try {
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE HomeworkInfo (AssignmentNumber INT PRIMARY KEY AUTO_INCREMENT," + "Title TEXT,NSubjectAreas INT,NQuestionsPerSubjectArea INT,Deadline1 DATETIME)");
            stmt.executeUpdate("CREATE TABLE HomeworkQuestions (QuestionID INT PRIMARY KEY AUTO_INCREMENT," + "AssignmentNumber INT,SubjectArea INT,QuestionText TEXT,QuestionType TEXT,NumberOfChoices INT," + "ChoiceAText TEXT,ChoiceBText TEXT,ChoiceCText TEXT,ChoiceDText TEXT,ChoiceEText TEXT," + "RequiredPrecision DOUBLE,CorrectAnswer1 TEXT,CorrectAnswer2 TEXT,QuestionTag TEXT,PointValue INT,Section VARCHAR(3) DEFAULT 'All')");
            stmt.executeUpdate("CREATE TABLE HomeworkTransactions (StudentIDNumber VARCHAR(50),LastName TEXT," + "FirstName TEXT,AssignmentNumber INT,Date TIMESTAMP,Code INT,StudentScore INT,PossibleScore INT," + "ElapsedMinutes INT,IPNumber VARCHAR(15))");
            stmt.executeUpdate("CREATE TABLE HomeworkParameters (TimeLimit INT,WaitForNewDownload INT," + "EnforceDeadlines VARCHAR(5),AllowMultipleTries VARCHAR(5),ScrambleQuestions VARCHAR(5)," + "AllowWorkAhead VARCHAR(5),ShowMissedQuestions VARCHAR(5),UseSectionDeadlines VARCHAR(5), TrackAnswers VARCHAR(5))");
            stmt.executeUpdate("CREATE TABLE HomeworkAssignedQuestions (Code INT, QuestionID INT, Graded VARCHAR(5), StudentAnswer TEXT)");
            try {
                stmt.executeUpdate("CREATE TABLE Essays (Code INT, QuestionID INT, Graded VARCHAR(5), Answer TEXT, StudentIDNumber VARCHAR(50), TestType VARCHAR(8), Score INT)");
            } catch (Exception e) {
            }
            stmt.executeUpdate("INSERT INTO HomeworkParameters VALUES (" + timeLimit + "," + waitForNewDownload + ",'" + enforceDeadlines + "','" + allowMultipleTries + "','" + scrambleQuestions + "','" + allowWorkAhead + "','" + showMissedQuestions + "','" + useSectionDeadlines + "','" + trackAnswers + "')");
            return homeworkSelectForm(student);
        } catch (Exception e2) {
            return e2.getMessage();
        }
    }

    protected static synchronized String getUnsubmittedAssignments(String studentIDNumber) {
        StringBuffer buf = new StringBuffer("");
        boolean hasUnsubmitted = false;
        RBStore r2 = EledgeResources.getHomeworkBundle();
        try {
            buf.append("<table border=1 cellspacing=0><thead><b>" + r2.getString("str_field_hw") + "</b></thead>");
            buf.append("<TR><TH>" + r2.getString("str_field_assignment") + "</TH><TH>" + r2.getString("str_field_status") + "</TH></TR>");
            Class.forName(Course.jdbcDriver).newInstance();
            Connection conn = DriverManager.getConnection(Course.dbName, Course.mySQLUser, Course.mySQLPass);
            Statement stmt = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            ResultSet rsInfo = stmt.executeQuery("SELECT * FROM HomeworkInfo");
            if (!rsInfo.isBeforeFirst()) {
                return "";
            }
            while (rsInfo.next()) {
                String assignmentName = "HW" + ((rsInfo.getInt("AssignmentNumber") < 10) ? "0" : "") + rsInfo.getString("AssignmentNumber");
                String sqlQueryString = "SELECT * FROM Scores WHERE StudentIDNumber='" + studentIDNumber + "' AND Assignment='" + assignmentName + "'";
                ResultSet rsAssignment = stmt2.executeQuery(sqlQueryString);
                if (!rsAssignment.next()) {
                    if (!hasUnsubmitted) {
                        hasUnsubmitted = true;
                    }
                    buf.append("\n<TR><TD>" + assignmentName + "</TD><TD>" + r2.getString("str_status_unsubmitted") + "</TD></TR>");
                }
            }
        } catch (Exception e) {
            return "";
        }
        if (!hasUnsubmitted) {
            buf.append("</tr><td colspan=2>" + r2.getString("str_hw_done") + "</td></tr>");
        }
        buf.append("</TABLE>");
        return buf.toString();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}

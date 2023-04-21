package com.galileoschoice.admin.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.galileoschoice.admin.bean.ConnectionUtil;
import com.galileoschoice.admin.internal.HtmlUtils;

/**
 * Servlet implementation class for Servlet: AddNewServ
 *
 */
public class TestServ extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    public TestServ() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");
        String passwd = (String) session.getAttribute("password");
        Connection conn = null;
        ResultSet rs = null;
        try {
            if (userName != null && passwd != null) conn = ConnectionUtil.getConnection(userName, passwd);
        } catch (Exception e) {
        }
        if (conn == null) {
            out.println("<HTML><HEAD><TITLE>Searching Result</TITLE>" + "<link href=css/style.css rel=stylesheet type=text/css />" + "<script type=\"text/javascript\"> " + "top.location.replace('login.jsp');" + "</script>" + "</HEAD>");
        } else {
            out.println("<HTML><HEAD><TITLE>Login Validation</TITLE>" + "<link href=css/style.css rel=stylesheet type=text/css />" + "</HEAD><BODY class=\"sub\">");
            Statement statement = null;
            String sql;
            HtmlUtils htmlUtils = new HtmlUtils();
            try {
                statement = conn.createStatement();
                sql = "use userdb";
                statement.execute(sql);
                String tableName = userName + "_test";
                sql = "create table if not exists `" + tableName + "` (" + "`id` int(10) unsigned NOT NULL auto_increment," + "`questionId` int(10) NOT NULL," + "`answerId` int(10) default NULL," + "`isCorrect` tinyint(1) default '0'," + "`choiceSeq` smallint default '0'," + "PRIMARY KEY (`id`)" + ") ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1";
                statement.execute(sql);
                sql = "TRUNCATE TABLE `" + tableName + "`";
                statement.execute(sql);
                String subject = (String) request.getParameter("subject");
                String level = (String) request.getParameter("level");
                String time = (String) request.getParameter("time");
                String questionId = (String) request.getParameter("id");
                String name = (String) request.getParameter("name");
                String difficulty = (String) request.getParameter("difficulty");
                String contents = "";
                boolean isValid = false;
                boolean hasRecord = false;
                int numberOfQuestion = 5;
                if (time != null) try {
                    numberOfQuestion = (int) (10 * Double.valueOf(time));
                } catch (Exception e) {
                }
                sql = "insert into userdb." + tableName + " (questionId) select DISTINCT id " + "from questionlib.questions where isValid=1";
                if (subject != null && subject.trim().length() > 0) sql += " and subject='" + subject + "'";
                if (level != null && level.trim().length() > 0) sql += " and level='" + level + "'";
                sql += " order by rand() limit " + numberOfQuestion;
                statement.execute(sql);
                sql = "select a.id,a.name,a.subject,a.level,a.difficulty," + "substring(a.contents,1,100),isValid from questionlib.questions a, " + "userdb." + tableName + " b where a.id=b.questionId";
                if (statement.execute(sql)) {
                    rs = statement.getResultSet();
                    if (rs.next()) {
                        hasRecord = true;
                    }
                }
                if (!hasRecord) {
                    throw new SQLException("Can not find such records, subject=" + subject + ", level=" + level);
                }
                out.println("<H3>Found the following questions. Click the ID to edit a question.</H3>");
                out.println("<form>\n");
                out.println(htmlUtils.getTableHead("center", "100%", 1));
                out.println(htmlUtils.getTR("center") + htmlUtils.getTD("right", "5%", "<b>ID</b>") + htmlUtils.getTD("right", "10%", "<b>Name</b>") + htmlUtils.getTD("right", "5%", "<b>Subject</b>") + htmlUtils.getTD("right", "5%", "<b>Level</b>") + htmlUtils.getTD("right", "5%", "<b>Difficulty</b>") + htmlUtils.getTD("right", "5%", "<b>Valid</b>") + htmlUtils.getTD("left", "55%", "<b>Contents</b>") + htmlUtils.getTD("left", "10%", "<b>Action</b>") + htmlUtils.getClosedTR());
                boolean isFirst = true;
                while (isFirst || rs.next()) {
                    isFirst = false;
                    questionId = rs.getString(1);
                    name = rs.getString(2);
                    subject = rs.getString(3);
                    level = rs.getString(4);
                    difficulty = String.valueOf(rs.getInt(5));
                    contents = rs.getString(6) + "... \n... ...";
                    int isValidValue = rs.getInt(7);
                    isValid = isValidValue == 1 ? true : false;
                    out.println(htmlUtils.getTR("center") + htmlUtils.getTD("right", "5%", "<a href='editquestion.jsp?id=" + questionId + "'>" + questionId + "</a>") + htmlUtils.getTD("right", "10%", "<a href='editquestion.jsp?id=" + questionId + "'>" + name + "</a>") + htmlUtils.getTD("right", "5%", subject) + htmlUtils.getTD("right", "5%", level) + htmlUtils.getTD("right", "5%", difficulty) + htmlUtils.getTD("right", "5%", isValid ? "yes" : "no") + htmlUtils.getTD("left", "55%", contents) + htmlUtils.getTD("left", "55%", "<a href='PreviewServ?id=" + questionId + "'>Preview</a> <a href='editquestion.jsp?id=" + questionId + "'>Edit</a> ") + htmlUtils.getClosedTR());
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                out.println(htmlUtils.getClosedTable());
                out.println("</form>\n");
                out.println("<p align=\"right\">Are you ready? <a href=\"TestPageServ\">" + "Please click here to start the test.</a>&nbsp;&nbsp;</p>");
            } catch (Exception e) {
                htmlUtils.handleException(e, out);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                    if (statement != null) statement.close();
                    statement = null;
                    if (conn != null) conn.close();
                    conn = null;
                } catch (Exception e) {
                }
            }
            out.println("</BODY></HTML>");
        }
    }
}

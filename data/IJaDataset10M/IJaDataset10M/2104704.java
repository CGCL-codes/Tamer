package control;

import control.dao.*;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Mentee;
import model.MenteeAnswer;
import model.QueryMenteeAnswer;
import model.QueryMenteeAnswerByDate;
import model.QueryMenteeAnswerByName;
import model.QueryMenteeAnswerByQuestion;
import model.Question;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

/**
 *
 * @author Nile
 */
public class MenteeAnswerActionServlet extends HttpServlet {

    private static final int DEFAULT_BUFFER_SIZE = 10240;

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sess = request.getSession();
        String role = (String) sess.getAttribute("uel_role");
        if (role != null && !role.isEmpty()) {
            String action = request.getParameter("action");
            sess.removeAttribute("info");
            if (action.equalsIgnoreCase("nextquestion")) {
                if (role.equalsIgnoreCase("Mentor")) {
                    loadNextQuestion(sess, request, response);
                    return;
                } else {
                    response.sendRedirect(response.encodeRedirectURL("main.jsp"));
                    return;
                }
            }
            if (action.equalsIgnoreCase("viewanswers")) {
                if (!role.equalsIgnoreCase("Mentor")) {
                    String criteria = request.getParameter("criteria");
                    if (criteria != null && criteria.equalsIgnoreCase("all")) {
                        showAllAnswers(sess, request, response, role);
                    } else if (criteria != null && criteria.equalsIgnoreCase("byquestion")) {
                        showAllAnswersForQuestion(sess, request, response, role);
                    } else if (criteria != null && criteria.equalsIgnoreCase("bydate")) {
                        showAllAnswersForDate(sess, request, response, role);
                    } else if (criteria != null && criteria.equalsIgnoreCase("bymentee")) {
                        showAllAnswersForMentee(sess, request, response, role);
                    }
                    return;
                } else {
                    response.sendRedirect(response.encodeRedirectURL("main.jsp"));
                    return;
                }
            }
            if (action.equalsIgnoreCase("saveanswers")) {
                if (!role.equalsIgnoreCase("Mentor")) {
                    String criteria = request.getParameter("criteria");
                    if (criteria != null && criteria.equalsIgnoreCase("all")) {
                        saveAllAnswers(sess, request, response, role);
                    } else if (criteria != null && criteria.equalsIgnoreCase("byquestion")) {
                        saveAllAnswersForQuestion(sess, request, response, role);
                    } else if (criteria != null && criteria.equalsIgnoreCase("bydate")) {
                        saveAllAnswersForDate(sess, request, response, role);
                    } else if (criteria != null && criteria.equalsIgnoreCase("bymentee")) {
                        saveAllAnswersForMentee(sess, request, response, role);
                    }
                    return;
                } else {
                    response.sendRedirect(response.encodeRedirectURL("main.jsp"));
                    return;
                }
            }
        } else {
            response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            return;
        }
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sess = request.getSession();
        String role = (String) sess.getAttribute("uel_role");
        if (role != null && !role.isEmpty()) {
            String action = request.getParameter("action");
            if (action == null || action.isEmpty()) {
                response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            }
            sess.removeAttribute("info");
            if (action.equalsIgnoreCase("show")) {
                if (role.equalsIgnoreCase("Mentor")) {
                    String viewType = request.getParameter("view");
                    if (viewType != null && viewType.equalsIgnoreCase("single")) {
                        loadFirstQuestion(sess, request, response);
                    } else {
                        loadQuestions(sess, request, response);
                    }
                    return;
                } else {
                    response.sendRedirect(response.encodeRedirectURL("main.jsp"));
                    return;
                }
            }
            if (action.equalsIgnoreCase("save")) {
                if (role.equalsIgnoreCase("Mentor")) {
                    String viewType = request.getParameter("type");
                    if (viewType != null && viewType.equalsIgnoreCase("single")) {
                        loadFirstQuestion(sess, request, response);
                    } else {
                        saveAnswers(sess, request, response);
                    }
                    return;
                } else {
                    response.sendRedirect(response.encodeRedirectURL("main.jsp"));
                    return;
                }
            }
        } else {
            response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            return;
        }
    }

    protected void showAllAnswers(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        RequestDispatcher rd;
        MenteeAnswerActions maa = new MenteeAnswerActions();
        List<QueryMenteeAnswer> qma = maa.getDetailedInfo();
        sess.setAttribute("MenteeAnswers", qma);
        if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser")) {
            rd = request.getRequestDispatcher("/WEB-INF/Researcher/AllMenteeAnswers.jsp");
            rd.forward(request, response);
            return;
        }
        response.sendRedirect(response.encodeRedirectURL("main.jsp"));
        return;
    }

    protected void showAllAnswersForQuestion(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        RequestDispatcher rd;
        String qid;
        qid = request.getParameter("questionID");
        if (qid != null) {
            Question q = new QuestionActions().getQuestionByID(Integer.parseInt(qid));
            MenteeAnswerActions maa = new MenteeAnswerActions();
            List<QueryMenteeAnswerByQuestion> qma = maa.getDetailedInfoForQuestion(Integer.parseInt(qid));
            sess.setAttribute("MenteeAnswers", qma);
            sess.setAttribute("Question", q);
            if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser")) {
                rd = request.getRequestDispatcher("/WEB-INF/Researcher/MenteeAnswerForQuestion.jsp");
                rd.forward(request, response);
                return;
            }
        }
        response.sendRedirect(response.encodeRedirectURL("main.jsp"));
        return;
    }

    protected void showAllAnswersForMentee(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        RequestDispatcher rd;
        String meid;
        meid = request.getParameter("menteeID");
        if (meid != null) {
            MenteeAnswerActions maa = new MenteeAnswerActions();
            List<QueryMenteeAnswerByName> qma = maa.getDetailedInfoForMentee(Integer.parseInt(meid));
            Mentee m = new MenteeActions().getMenteeByID(Integer.parseInt(meid));
            sess.setAttribute("MenteeAnswers", qma);
            sess.setAttribute("Mentee", m);
            if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser")) {
                rd = request.getRequestDispatcher("/WEB-INF/Researcher/MenteeAnswerSpecific.jsp");
                rd.forward(request, response);
                return;
            }
        }
        response.sendRedirect(response.encodeRedirectURL("main.jsp"));
        return;
    }

    protected void showAllAnswersForDate(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        RequestDispatcher rd;
        String date;
        date = request.getParameter("answerDate");
        if (date != null) {
            try {
                long l = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
                java.sql.Date adate = new java.sql.Date(l);
                MenteeAnswerActions maa = new MenteeAnswerActions();
                List<QueryMenteeAnswerByDate> qma = maa.getDetailedInfoForDate(adate);
                sess.setAttribute("MenteeAnswers", qma);
                sess.setAttribute("AnswerDate", new SimpleDateFormat("dd/MM/yyyy").format(adate));
                if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser")) {
                    rd = request.getRequestDispatcher("/WEB-INF/Researcher/MenteeAnswerDate.jsp");
                    rd.forward(request, response);
                    return;
                }
            } catch (ParseException pe) {
            }
        }
        response.sendRedirect(response.encodeRedirectURL("main.jsp"));
        return;
    }

    protected void saveAllAnswers(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        MenteeAnswerActions maa = new MenteeAnswerActions();
        List<QueryMenteeAnswer> qma = maa.getDetailedInfo();
        if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser")) {
            File f = File.createTempFile("feedback-all_", ".xls");
            FileOutputStream out = new FileOutputStream(f);
            Workbook wb = new HSSFWorkbook();
            CreationHelper createHelper = wb.getCreationHelper();
            Sheet s = wb.createSheet();
            Row r = null;
            Cell c = null;
            wb.setSheetName(0, "Mentee Feedback");
            r = s.createRow(0);
            CellStyle dateCellStyle = wb.createCellStyle();
            CellStyle headerCellStyle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            headerCellStyle.setFont(font);
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
            for (int i = 0; i < QueryMenteeAnswer.getLabels().length; i++) {
                c = r.createCell(i);
                c.setCellValue(QueryMenteeAnswer.getLabels()[i]);
                c.setCellType(Cell.CELL_TYPE_STRING);
                c.setCellStyle(headerCellStyle);
            }
            int rownum;
            for (rownum = (short) 1; rownum <= qma.size(); rownum++) {
                r = s.createRow(rownum);
                Object[] obj = qma.get(rownum - 1).getObjectData();
                for (short cellnum = (short) 0; cellnum < obj.length; cellnum++) {
                    c = r.createCell(cellnum);
                    Object sel = obj[cellnum];
                    if (sel instanceof Integer) {
                        c.setCellValue((Integer) sel);
                    } else if (sel instanceof String) {
                        c.setCellValue((String) sel);
                    } else if (sel instanceof Date) {
                        c.setCellValue((Date) sel);
                        c.setCellStyle(dateCellStyle);
                    } else {
                        c.setCellValue(sel.toString());
                    }
                }
            }
            for (int i = 0; i < QueryMenteeAnswer.getLabels().length; i++) {
                s.autoSizeColumn(i);
            }
            wb.write(out);
            out.flush();
            out.close();
            String contentType = getServletContext().getMimeType(f.getName());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            response.reset();
            response.setBufferSize(DEFAULT_BUFFER_SIZE);
            response.setContentType(contentType);
            response.setHeader("Content-Length", String.valueOf(f.length()));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
            BufferedInputStream input = null;
            BufferedOutputStream output = null;
            try {
                input = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);
                output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            } finally {
                close(output);
                close(input);
            }
            if (!f.delete()) {
                System.err.println("Oh no!");
            }
            return;
        }
        response.sendRedirect(response.encodeRedirectURL("main.jsp"));
        return;
    }

    protected void saveAllAnswersForQuestion(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        String qid;
        qid = request.getParameter("questionID");
        if (qid != null) {
            Question q = new QuestionActions().getQuestionByID(Integer.parseInt(qid));
            MenteeAnswerActions maa = new MenteeAnswerActions();
            List<QueryMenteeAnswerByQuestion> qma = maa.getDetailedInfoForQuestion(Integer.parseInt(qid));
            sess.setAttribute("MenteeAnswers", qma);
            if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser")) {
                File f = File.createTempFile("feedback_question_" + q.getQid() + "-", ".xls");
                FileOutputStream out = new FileOutputStream(f);
                Workbook wb = new HSSFWorkbook();
                CreationHelper createHelper = wb.getCreationHelper();
                Sheet s = wb.createSheet();
                Row r = null;
                Cell c = null;
                wb.setSheetName(0, "Mentee Feedback, by Question");
                r = s.createRow(0);
                CellStyle dateCellStyle = wb.createCellStyle();
                CellStyle headerCellStyle = wb.createCellStyle();
                Font font = wb.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                headerCellStyle.setFont(font);
                dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                for (int i = 0; i < QueryMenteeAnswerByQuestion.getLabels().length; i++) {
                    c = r.createCell(i);
                    c.setCellValue(QueryMenteeAnswerByQuestion.getLabels()[i]);
                    c.setCellType(Cell.CELL_TYPE_STRING);
                    c.setCellStyle(headerCellStyle);
                }
                int rownum;
                for (rownum = (short) 1; rownum <= qma.size(); rownum++) {
                    r = s.createRow(rownum);
                    Object[] obj = qma.get(rownum - 1).getObjectData();
                    for (short cellnum = (short) 0; cellnum < obj.length; cellnum++) {
                        c = r.createCell(cellnum);
                        Object sel = obj[cellnum];
                        if (sel instanceof Integer) {
                            c.setCellValue((Integer) sel);
                        } else if (sel instanceof String) {
                            c.setCellValue((String) sel);
                        } else if (sel instanceof Date) {
                            c.setCellValue((Date) sel);
                            c.setCellStyle(dateCellStyle);
                        } else {
                            c.setCellValue(sel.toString());
                        }
                    }
                }
                for (int i = 0; i < QueryMenteeAnswerByQuestion.getLabels().length; i++) {
                    s.autoSizeColumn(i);
                }
                wb.write(out);
                out.flush();
                out.close();
                String contentType = getServletContext().getMimeType(f.getName());
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                response.reset();
                response.setBufferSize(DEFAULT_BUFFER_SIZE);
                response.setContentType(contentType);
                response.setHeader("Content-Length", String.valueOf(f.length()));
                response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
                BufferedInputStream input = null;
                BufferedOutputStream output = null;
                try {
                    input = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);
                    output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
                    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                    int length;
                    while ((length = input.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                } finally {
                    close(output);
                    close(input);
                }
                if (!f.delete()) {
                    System.err.println("Oh no!");
                }
                return;
            }
        }
        response.sendRedirect(response.encodeRedirectURL("main.jsp"));
        return;
    }

    protected void saveAllAnswersForMentee(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        String meid;
        meid = request.getParameter("menteeID");
        if (meid != null) {
            MenteeAnswerActions maa = new MenteeAnswerActions();
            List<QueryMenteeAnswerByName> qma = maa.getDetailedInfoForMentee(Integer.parseInt(meid));
            Mentee m = new MenteeActions().getMenteeByID(Integer.parseInt(meid));
            sess.setAttribute("MenteeAnswers", qma);
            if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser")) {
                File f = File.createTempFile("feedback_mentee_" + m.getMeid() + "-", ".xls");
                FileOutputStream out = new FileOutputStream(f);
                Workbook wb = new HSSFWorkbook();
                CreationHelper createHelper = wb.getCreationHelper();
                Sheet s = wb.createSheet();
                Row r = null;
                Cell c = null;
                wb.setSheetName(0, "Mentee Feedback");
                r = s.createRow(0);
                CellStyle dateCellStyle = wb.createCellStyle();
                CellStyle headerCellStyle = wb.createCellStyle();
                Font font = wb.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                headerCellStyle.setFont(font);
                dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                for (int i = 0; i < QueryMenteeAnswerByName.getLabels().length; i++) {
                    c = r.createCell(i);
                    c.setCellValue(QueryMenteeAnswerByName.getLabels()[i]);
                    c.setCellType(Cell.CELL_TYPE_STRING);
                    c.setCellStyle(headerCellStyle);
                }
                int rownum;
                for (rownum = (short) 1; rownum <= qma.size(); rownum++) {
                    r = s.createRow(rownum);
                    Object[] obj = qma.get(rownum - 1).getObjectData();
                    for (short cellnum = (short) 0; cellnum < obj.length; cellnum++) {
                        c = r.createCell(cellnum);
                        Object sel = obj[cellnum];
                        if (sel instanceof Integer) {
                            c.setCellValue((Integer) sel);
                        } else if (sel instanceof String) {
                            c.setCellValue((String) sel);
                        } else if (sel instanceof Date) {
                            c.setCellValue((Date) sel);
                            c.setCellStyle(dateCellStyle);
                        } else {
                            c.setCellValue(sel.toString());
                        }
                    }
                }
                for (int i = 0; i < QueryMenteeAnswerByName.getLabels().length; i++) {
                    s.autoSizeColumn(i);
                }
                wb.write(out);
                out.flush();
                out.close();
                String contentType = getServletContext().getMimeType(f.getName());
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                response.reset();
                response.setBufferSize(DEFAULT_BUFFER_SIZE);
                response.setContentType(contentType);
                response.setHeader("Content-Length", String.valueOf(f.length()));
                response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
                BufferedInputStream input = null;
                BufferedOutputStream output = null;
                try {
                    input = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);
                    output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
                    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                    int length;
                    while ((length = input.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                } finally {
                    close(output);
                    close(input);
                }
                if (!f.delete()) {
                    System.err.println("Oh no!");
                }
                return;
            }
        }
        response.sendRedirect(response.encodeRedirectURL("main.jsp"));
        return;
    }

    protected void saveAllAnswersForDate(HttpSession sess, HttpServletRequest request, HttpServletResponse response, String role) throws ServletException, IOException {
        String date;
        date = request.getParameter("answerDate");
        if (date != null) {
            try {
                long l = new SimpleDateFormat("dd/MM/yyy").parse(date).getTime();
                java.sql.Date adate = new java.sql.Date(l);
                MenteeAnswerActions maa = new MenteeAnswerActions();
                List<QueryMenteeAnswerByDate> qma = maa.getDetailedInfoForDate(adate);
                sess.setAttribute("MenteeAnswers", qma);
                sess.setAttribute("AnswerDate", new SimpleDateFormat("dd/MM/yyyy").format(adate));
                if (role.equalsIgnoreCase("Researcher") || role.equalsIgnoreCase("Superuser")) {
                    File f = File.createTempFile("feedback_date-" + new SimpleDateFormat("dd-MM-yyyy").format(adate) + "_", ".xls");
                    FileOutputStream out = new FileOutputStream(f);
                    Workbook wb = new HSSFWorkbook();
                    CreationHelper createHelper = wb.getCreationHelper();
                    Sheet s = wb.createSheet();
                    Row r = null;
                    Cell c = null;
                    wb.setSheetName(0, "Mentee Feedback");
                    r = s.createRow(0);
                    CellStyle dateCellStyle = wb.createCellStyle();
                    CellStyle headerCellStyle = wb.createCellStyle();
                    Font font = wb.createFont();
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                    headerCellStyle.setFont(font);
                    dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                    for (int i = 0; i < QueryMenteeAnswerByDate.getLabels().length; i++) {
                        c = r.createCell(i);
                        c.setCellValue(QueryMenteeAnswerByDate.getLabels()[i]);
                        c.setCellType(Cell.CELL_TYPE_STRING);
                        c.setCellStyle(headerCellStyle);
                    }
                    int rownum;
                    for (rownum = (short) 1; rownum <= qma.size(); rownum++) {
                        r = s.createRow(rownum);
                        Object[] obj = qma.get(rownum - 1).getObjectData();
                        for (short cellnum = (short) 0; cellnum < obj.length; cellnum++) {
                            c = r.createCell(cellnum);
                            Object sel = obj[cellnum];
                            if (sel instanceof Integer) {
                                c.setCellValue((Integer) sel);
                            } else if (sel instanceof String) {
                                c.setCellValue((String) sel);
                            } else if (sel instanceof Date) {
                                c.setCellValue((Date) sel);
                                c.setCellStyle(dateCellStyle);
                            } else {
                                c.setCellValue(sel.toString());
                            }
                        }
                    }
                    for (int i = 0; i < QueryMenteeAnswerByDate.getLabels().length; i++) {
                        s.autoSizeColumn(i);
                    }
                    wb.write(out);
                    out.flush();
                    out.close();
                    String contentType = getServletContext().getMimeType(f.getName());
                    if (contentType == null) {
                        contentType = "application/octet-stream";
                    }
                    response.reset();
                    response.setBufferSize(DEFAULT_BUFFER_SIZE);
                    response.setContentType(contentType);
                    response.setHeader("Content-Length", String.valueOf(f.length()));
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
                    BufferedInputStream input = null;
                    BufferedOutputStream output = null;
                    try {
                        input = new BufferedInputStream(new FileInputStream(f), DEFAULT_BUFFER_SIZE);
                        output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
                        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                        int length;
                        while ((length = input.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }
                    } finally {
                        close(output);
                        close(input);
                    }
                    if (!f.delete()) {
                        System.err.println("Oh no!");
                    }
                    return;
                }
            } catch (ParseException pe) {
            }
        }
        response.sendRedirect(response.encodeRedirectURL("main.jsp"));
        return;
    }

    protected void loadQuestions(HttpSession sess, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("Date") != null) {
            sess.setAttribute("fdate", request.getParameter("Date"));
        }
        sess.removeAttribute("error");
        Integer mid = (Integer) sess.getAttribute("mentorID");
        if (Functions.getRecentAssignmentID(mid) == null) {
            sess.removeAttribute("message");
            sess.setAttribute("error", "You have not yet been assigned a Mentee.");
            response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            return;
        }
        QuestionActions qa = new QuestionActions();
        List<Question> questions = qa.getAllQuestions();
        sess.setAttribute("questions", questions);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/Mentor/QuestionAnswerAll.jsp");
        rd.forward(request, response);
        return;
    }

    protected void saveAnswers(HttpSession sess, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("Date") != null) {
            sess.setAttribute("fdate", request.getParameter("Date"));
        }
        sess.removeAttribute("error");
        Integer mid = (Integer) sess.getAttribute("mentorID");
        if (Functions.getRecentAssignmentID(mid) == null) {
            sess.removeAttribute("message");
            sess.setAttribute("error", "You have not yet been assigned a Mentee.");
            response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            return;
        }
        String fdate = (String) sess.getAttribute("fdate");
        Integer meid = Functions.getRecentAssignmentID(mid);
        MenteeAnswerActions maa = new MenteeAnswerActions();
        List<Question> q = (List<Question>) sess.getAttribute("questions");
        try {
            for (Question question : q) {
                Integer qid = question.getQid();
                String answer = request.getParameter("" + qid);
                if (!(answer == null || answer.isEmpty())) {
                    MenteeAnswer ma = new MenteeAnswer();
                    ma.setMeid(meid);
                    ma.setAval(Integer.parseInt(answer));
                    ma.setQid(qid);
                    java.sql.Date date = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(fdate).getTime());
                    ma.setAdate(date);
                    MenteeAnswer ma2 = maa.getMenteeAnswerByID(meid, qid, date);
                    if (ma2 != null) {
                        System.out.println("Updating since we already have feedback for this date.");
                        maa.updateMenteeAnswer(ma);
                    } else {
                        maa.addNewMenteeAnswer(ma);
                    }
                }
            }
            sess.setAttribute("info", "Feedback has been saved for your Mentee.");
            response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            return;
        } catch (ParseException pe) {
            throw new ServletException("Error parsing Date", pe);
        } catch (SQLException sql) {
            throw new ServletException("Error saving Answer", sql);
        } catch (RuntimeException re) {
            throw new ServletException("Could not save answer.", re);
        }
    }

    protected void loadFirstQuestion(HttpSession sess, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("Date") != null) {
            sess.setAttribute("fdate", request.getParameter("Date"));
        }
        sess.removeAttribute("error");
        Integer mid = (Integer) sess.getAttribute("mentorID");
        if (Functions.getRecentAssignmentID(mid) == null) {
            sess.removeAttribute("message");
            sess.setAttribute("error", "You have not yet been assigned a Mentee.");
            response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            return;
        }
        List<Question> ques = (List<Question>) sess.getAttribute("Questions");
        Integer currQues = (Integer) sess.getAttribute("cQnum");
        System.out.println("Current Question: " + currQues);
        sess.setAttribute("cQues", ques.get(currQues - 1));
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/Mentor/QuestionAnswer.jsp");
        rd.forward(request, response);
        return;
    }

    protected void loadNextQuestion(HttpSession sess, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/Mentor/QuestionAnswer.jsp");
        rd.forward(request, response);
        return;
    }

    protected void saveAnswer(HttpSession sess, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("Date") != null) {
            sess.setAttribute("fdate", request.getParameter("Date"));
        }
        List<Question> ques = (List<Question>) sess.getAttribute("Questions");
        Integer currQues = (Integer) sess.getAttribute("cQnum");
        Integer mid = (Integer) sess.getAttribute("mentorID");
        Integer qid = Integer.parseInt(request.getParameter("qid"));
        String fdate = (String) sess.getAttribute("fdate");
        System.out.println("Current Question: " + currQues);
        System.out.println("Answer: " + request.getParameter("Answer") + " by " + mid);
        System.out.println("Date: " + fdate);
        MenteeAnswerActions maa = new MenteeAnswerActions();
        MenteeAnswer ma = new MenteeAnswer();
        Integer meid = Functions.getRecentAssignmentID(mid);
        ma.setMeid(meid);
        ma.setAval(Integer.parseInt(request.getParameter("Answer")));
        ma.setQid(qid);
        try {
            java.sql.Date date = new Date(new SimpleDateFormat("dd/MM/yyyy").parse(fdate).getTime());
            ma.setAdate(date);
            MenteeAnswer ma2 = maa.getMenteeAnswerByID(meid, qid, date);
            System.out.println(ma2);
            if (ma2 != null) {
                maa.updateMenteeAnswer(ma);
            } else {
                maa.addNewMenteeAnswer(ma);
            }
        } catch (ParseException pe) {
            throw new ServletException("Error parsing Date", pe);
        } catch (SQLException sql) {
            throw new ServletException("Error saving Answer", sql);
        } catch (RuntimeException re) {
            throw new ServletException("Could not save answer.", re);
        }
        currQues++;
        sess.setAttribute("cQnum", currQues);
        Integer tQues = (Integer) sess.getAttribute("tQnum");
        if (currQues > tQues) {
            sess.setAttribute("message", "Feedback has been saved for your Mentee.");
            sess.setAttribute("cQnum", 1);
            response.sendRedirect(response.encodeRedirectURL("main.jsp"));
            return;
        }
        sess.setAttribute("cQues", ques.get(currQues - 1));
        response.sendRedirect(response.encodeRedirectURL("menteeanswers?action=nextquestion"));
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This Servlet controls actions pertaining to information held on Mentors";
    }

    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

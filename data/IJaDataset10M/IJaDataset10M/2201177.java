package de.ifgi.simcat.CIMOnlineTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import de.ifgi.simcat.cimQuestionnaire.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class for Servlet: Test
 * 
 */
public class Test1 extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    private BufferedWriter bw;

    public Test1() throws IOException {
        super();
        bw = new BufferedWriter(new FileWriter("outputFile.txt"));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false) == null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/introduction.jsp");
            dispatcher.forward(request, response);
        } else {
            HttpSession session = request.getSession();
            String diffVal = request.getParameter("diffVal");
            int current = (Integer) session.getAttribute("lastCompleted") + 1;
            int next = current + 1;
            if (isDiffValValid(diffVal)) {
                session.setAttribute("lastCompleted", current);
                session.setAttribute("diffValue" + current, diffVal);
                long usedTime = System.currentTimeMillis() - session.getLastAccessedTime();
                session.setAttribute("usedTime" + current, usedTime);
            }
            Test test = (Test) session.getAttribute("test" + next);
            RequestDispatcher dispatcher;
            if (test == null) {
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/completed.jsp");
                writeLogFile(session);
            } else if (test.getVistype().equals(RankingVisualizer.TAGCLOUD)) dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/tagcloud.jsp"); else if (test.getVistype().equals(RankingVisualizer.SPACEDLIST)) dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/spacedlist.jsp"); else dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/list.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String p_age = request.getParameter("txtAge");
        String p_gender = request.getParameter("rdGender");
        String p_profession = request.getParameter("txtProfession");
        if (isAgeValid(p_age) && isGenderValid(p_gender) && isProfessionValid(p_profession)) {
            if (request.getSession(false) == null) {
                HttpSession session = request.getSession();
                session.setAttribute("age", p_age);
                session.setAttribute("gender", p_gender);
                session.setAttribute("profession", p_profession);
                session.setAttribute("lastCompleted", 0);
                ArrayList<Test> tests = TestGenerator.getTests();
                session.setAttribute("totalNumberOfTests", tests.size());
                int counter = 1;
                for (Iterator<Test> iter = tests.iterator(); iter.hasNext(); ) {
                    session.setAttribute("test" + counter, iter.next());
                    session.setAttribute("diffValue" + counter, null);
                    counter++;
                }
                RequestDispatcher dispatcher;
                if (tests.get(0).getVistype().equals(RankingVisualizer.TAGCLOUD)) dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/tagcloud.jsp"); else if (tests.get(0).getVistype().equals(RankingVisualizer.SPACEDLIST)) dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/spacedlist.jsp"); else dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/list.jsp");
                dispatcher.forward(request, response);
            }
        }
    }

    private boolean isAgeValid(String age) {
        try {
            int age1 = Integer.parseInt(age);
            if (age1 > 10 && age1 < 100) return true; else return false;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean isGenderValid(String gender) {
        if (gender.equals("m") || gender.equals("f")) return true; else return false;
    }

    private boolean isProfessionValid(String profession) {
        profession = profession.trim();
        if (profession.length() > 0) return true; else return false;
    }

    private boolean isDiffValValid(String diffVal) {
        try {
            int diffValue = Integer.parseInt(diffVal);
            if (diffValue >= 0 && diffValue <= 100) return true; else return false;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void writeLogFile(HttpSession session) throws IOException {
        try {
            String age = (String) session.getAttribute("age");
            String gender = (String) session.getAttribute("gender");
            String profession = (String) session.getAttribute("profession");
            bw.write(age + "\t");
            bw.write(gender + "\t");
            bw.write(profession + "\t");
            int totalNumberOfTest = (Integer) session.getAttribute("totalNumberOfTests");
            for (int i = 1; i <= totalNumberOfTest; i++) {
                long usedTime = (Long) session.getAttribute("usedTime" + i);
                int diffVal = (Integer) session.getAttribute("diffValue" + i);
                bw.write(diffVal + "\t");
                if (i == totalNumberOfTest) bw.write(usedTime + "\n"); else bw.write(usedTime + "\t");
            }
            bw.close();
        } catch (IOException ex) {
            throw ex;
        }
    }
}

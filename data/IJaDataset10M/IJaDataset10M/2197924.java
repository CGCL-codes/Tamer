package com.esa.servlet.choice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.xml.sax.SAXException;
import com.esa.dbi.impl.ChoiceDBImpl;
import com.esa.dbi.impl.StudentDBImpl;
import com.esa.javabean.Choice;
import com.esa.tois.TransChoice;

public class DoMyChoiceServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        this.doPost(request, response);
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String acname = (String) request.getSession().getAttribute("acname");
        StudentDBImpl sdbi = new StudentDBImpl();
        String sno = sdbi.getByAcname(acname).getSno();
        String cno = request.getParameter("cno");
        ChoiceDBImpl cdbi = new ChoiceDBImpl();
        Choice ch = new Choice();
        ch.setCno(cno);
        ch.setSno(sno);
        ch.setScore("0");
        List<Choice> list = new ArrayList<Choice>();
        TransChoice tdc = new TransChoice();
        if (cno.charAt(0) != '1') {
            list.add(ch);
            try {
                tdc.trans(list);
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } else {
        }
        if (cdbi.getChoice(cno, sno) == null) {
            cdbi.addChoice(ch);
        } else {
        }
        response.sendRedirect("ShowMyChoicesServlet");
        out.flush();
        out.close();
    }
}

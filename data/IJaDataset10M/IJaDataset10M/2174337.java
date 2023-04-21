package com.esa.servlet.choice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.esa.dbi.impl.ChoiceDBImpl;
import com.esa.dbi.impl.CourseDBImpl;
import com.esa.javabean.ChosenNum;
import com.esa.javabean.Course;

public class ShowAllChoicesServlet extends HttpServlet {

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
        ChoiceDBImpl cdbi = new ChoiceDBImpl();
        CourseDBImpl ci = new CourseDBImpl();
        ChosenNum cn;
        Course course;
        List<Course> allcourses = new ArrayList<Course>();
        allcourses = ci.getAllCourses();
        List<ChosenNum> list = new ArrayList<ChosenNum>();
        int sum = 0;
        String cno = null;
        for (int i = 0; i < allcourses.size(); i++) {
            course = allcourses.get(i);
            cno = course.getCno();
            sum = cdbi.getStudentsOfaCource(cno).size();
            cn = new ChosenNum();
            cn.setCno(course.getCno());
            cn.setCname(course.getCname());
            cn.setCredit(course.getCredit());
            cn.setCteacher(course.getCteacher());
            cn.setCplace(course.getCplace());
            cn.setCshare(course.getCshare());
            cn.setSum(sum);
            list.add(cn);
        }
        String acname = (String) request.getSession().getAttribute("acname");
        request.setAttribute("acname", acname);
        request.setAttribute("list", list);
        request.getRequestDispatcher("showAllchoices.jsp").forward(request, response);
        out.flush();
        out.close();
    }
}

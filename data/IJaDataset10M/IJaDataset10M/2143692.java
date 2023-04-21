package org.freipert.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.freipert.beans.TaskDetailBean;
import org.freipert.delegates.TaskInfoBD;
import org.freipert.utils.Error;

/**
 * 
 * @author thompsco in browser, http://localhost:9080/freipert/taskinfo start
 *         Tomcat not with plugin icon, but by right-clicking server in SERvers,
 *         pick "Start"
 */
public class TaskInfoServlet extends javax.servlet.http.HttpServlet {

    static final long serialVersionUID = 22;

    static final Logger logger = Logger.getLogger(TaskInfoServlet.class);

    public void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        performTask(request, response);
    }

    public void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        performTask(request, response);
    }

    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        try {
            String taskID = request.getParameter("task");
            String projID = request.getParameter("projID");
            if (taskID == null || projID == null) {
                printWarningToUser(response);
            } else {
                TaskInfoBD infoBD = new TaskInfoBD();
                TaskDetailBean detail = new TaskDetailBean();
                Map mp = infoBD.getInfoForOneTask(projID, taskID);
                String labelField = (String) mp.get("labelField");
                detail.setLabelField(labelField);
                String project = (String) mp.get("project");
                detail.setParent_project(project);
                boolean isFirstTask = ((Boolean) mp.get("isFirstTask")).booleanValue();
                detail.setFirstTask(isFirstTask);
                boolean isLastTask = ((Boolean) mp.get("isLastTask")).booleanValue();
                detail.setLastTask(isLastTask);
                Calendar taskStart = (Calendar) mp.get("taskStart");
                logger.debug("from TaskinfoServlet, taskStart = " + taskStart);
                detail.setTaskStart(taskStart);
                Calendar taskEnd = (Calendar) mp.get("taskEnd");
                detail.setTaskEnd(taskEnd);
                String narrative = (String) mp.get("narrative");
                detail.setNarrative(narrative);
                String resolution = (String) mp.get("resolution");
                detail.setResolution(resolution);
                Integer days_duration = (Integer) mp.get("days_duration");
                detail.setDays_duration(days_duration);
                HttpSession session = request.getSession();
                session.setAttribute("TaskDetailBean", detail);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/taskinfo.jsp");
                if (dispatcher != null) dispatcher.forward(request, response);
            }
        } catch (Throwable theException) {
            Error.showProblem(theException, request, response);
        }
    }

    private void printWarningToUser(javax.servlet.http.HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("You must specify a value for the task and projID parameters in the URL...");
        out.println("<p>");
    }
}

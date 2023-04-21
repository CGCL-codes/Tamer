package org.managersheet.report.workHours.tagLib;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.managersheet.accessControl.api.User;
import org.managersheet.report.workHours.api.IWorkHours;
import org.managersheet.report.workHours.api.WorkHours;
import org.managersheet.report.workHours.impl.WorkHoursControl;
import org.managersheet.userProfile.api.Project;

/**
 *
 * @author Fabio
 */
public class WorkHoursMonthTagLib extends BodyTagSupport {

    private User user;

    private Project project;

    private String month;

    private String year;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter jspWriter = this.pageContext.getOut();
        IWorkHours iWorkHours = WorkHoursControl.getInstance();
        String startDate = "01/" + month + "/" + year;
        String endDate = lastDate();
        iWorkHours.calculeteWorkHoursWeek(project, user, startDate, endDate);
        Iterator<WorkHours> iterator = iWorkHours.getListWorkHours().iterator();
        try {
            jspWriter.println("<table BORDER=4 >");
            jspWriter.println("<tr><th>Atividades</th><th>Total</th></tr>");
            long totalHoursProject = 0;
            while (iterator.hasNext()) {
                WorkHours workHours = iterator.next();
                jspWriter.println("<tr>");
                jspWriter.println("<td> " + workHours.getActivity().getName() + "</td>");
                jspWriter.println("<td>" + workHours.getCountHours() + "</td>");
                jspWriter.println("</tr>");
                totalHoursProject += workHours.getCountHours();
            }
            jspWriter.println("</table>");
            jspWriter.println("<br/>");
            jspWriter.println("Total do Projeto: " + totalHoursProject);
        } catch (IOException ex) {
            Logger.getLogger(WorkHoursMonthTagLib.class.getName()).log(Level.SEVERE, null, ex);
        }
        return EVAL_PAGE;
    }

    private String lastDate() {
        String date = "31";
        String[][] arrayDate = new String[12][2];
        for (int indice = 0; indice < 12; indice++) {
            arrayDate[indice][0] = String.valueOf(indice);
            if (indice == 0 || indice == 2 || indice == 4 || indice == 5 || indice == 6 || indice == 8 || indice == 9) {
                arrayDate[indice][1] = "31";
            } else if (indice == 1) {
                arrayDate[indice][1] = "28";
            } else {
                arrayDate[indice][1] = "30";
            }
        }
        if ((Integer.parseInt(year.trim()) % 4) == 0) {
            if (Integer.parseInt(month.trim()) == 2) {
                date = "29";
            }
        } else {
            for (int indice = 0; indice < 12; indice++) {
                if ((Integer.parseInt(month.trim()) - 1) == indice) {
                    date = arrayDate[indice][1];
                    break;
                }
            }
        }
        return date + "/" + month + "/" + year;
    }
}

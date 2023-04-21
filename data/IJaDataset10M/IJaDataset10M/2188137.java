package Commands;

import domain.Controller;
import domain.Student;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CmdSaveFinalSubjects extends Command {

    protected String nextJspUrl = "userStory6.jsp";

    CommandFactory comFac;

    Controller controller;

    public void init(CommandFactory comFac, Controller c) throws ServletException {
        this.comFac = comFac;
        this.controller = c;
    }

    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        HttpSession session = req.getSession();
        controller.clearFinalSubjects();
        List<Student> students = controller.getStudents();
        for (int i = 0; i < students.size(); i++) {
            String[] subjects = new String[2];
            subjects[0] = req.getParameter("" + students.get(i).getId() + "_1");
            subjects[1] = req.getParameter("" + students.get(i).getId() + "_2");
            if (subjects != null) {
                for (int j = 0; j < subjects.length; j++) {
                    students.get(i).addFinalSubject(controller.getSubject(Integer.parseInt(subjects[j])));
                }
            }
        }
        controller.saveFinalSubjects();
    }

    public String getNextJspUrl() {
        return nextJspUrl;
    }
}

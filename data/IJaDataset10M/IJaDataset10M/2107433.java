package Commands;

import domain.Controller;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CmdDefault extends Command {

    protected String nextJspUrl = "menu.jsp";

    CommandFactory comFac;

    Controller controller;

    public void init(CommandFactory comFac, Controller c) throws ServletException {
        this.comFac = comFac;
        this.controller = c;
    }

    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        Command command;
        HttpSession session = req.getSession();
        controller.getPuljer();
        session.setAttribute("pulje1", controller.getPulje1());
        session.setAttribute("pulje2", controller.getPulje2());
        req.setAttribute("command", "CmdGendan");
        command = comFac.getCommand(req);
        command.init(comFac, controller);
        command.execute(req, resp);
    }

    public String getNextJspUrl() {
        return nextJspUrl;
    }
}

package net.sf.drawbridge.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.drawbridge.vo.Status;
import net.sf.drawbridge.vo.StatusMessage;
import net.sf.drawbridge.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FakeLoginController extends AbstractDrawbridgeController {

    @RequestMapping(value = "/FakeLogin.do")
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("mainMenu");
        User user = null;
        List<StatusMessage> messages = new ArrayList<StatusMessage>();
        try {
            user = this.drawbridgeService.getUser(new Integer(request.getParameter(LOGGED_IN_USER)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        HttpSession session = request.getSession();
        session.setAttribute(LOGGED_IN_USER, user);
        messages.add(new StatusMessage(Status.INFO, "Logged in as: " + user, null));
        session.setAttribute(MESSAGE_LIST, messages);
        mav.addObject("databaseList", drawbridgeService.listDatabases());
        return mav;
    }
}

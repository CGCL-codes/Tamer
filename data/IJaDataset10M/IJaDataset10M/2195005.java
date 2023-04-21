package pl.edu.agh.pp.jspclient.web;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.edu.agh.pp.jspclient.admin.Connector;
import pl.edu.agh.pp.forum.Message;

public class MessageCreateFormController extends SimpleFormController {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    private Connector connector;

    public ModelAndView onSubmit(Object command) throws ServletException {
        String username = ((Message) command).getUsername();
        String text = ((Message) command).getText();
        int id = ((Message) command).getThreadId();
        if (id == -1) return new ModelAndView(new RedirectView(getSuccessView()));
        logger.info("Creating message %.");
        connector.createMessage(id, username, text);
        logger.info("returning from MessageCreateForm view to " + getSuccessView());
        return new ModelAndView(new RedirectView(getSuccessView()));
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        Message messageCreate = new Message();
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            id = -1;
        }
        if (connector.getThread(id) == null) id = -1;
        messageCreate.setThreadId(id);
        return messageCreate;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Connector getConnector() {
        return connector;
    }
}

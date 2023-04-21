package whiteboard.poll;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import whiteboard.HomePage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;

/**
 * This action class readies the poll instance for usage in the poll page.
 * 
 * @author Gooble
 *
 */
public class CreatePollPage extends ActionSupport implements ServletContextAware, SessionAware {

    private ServletContext servletContext;

    private Poll poll;

    private Map<String, Object> session;

    private String pollid;

    private List<String> options;

    private String yourChoice;

    public String execute() {
        poll = getHomePage().getSelectedCourse().extractPoll(Integer.parseInt(getPollid()));
        getHomePage().getSelectedCourse().setSelectedPoll(poll);
        options = new ArrayList<String>(poll.getPollOptions().keySet());
        return SUCCESS;
    }

    private HomePage getHomePage() {
        Map<String, Object> attibutes = ActionContext.getContext().getSession();
        return ((HomePage) attibutes.get("homePage"));
    }

    public void setServletContext(ServletContext arg0) {
        this.servletContext = arg0;
    }

    @Override
    public void setSession(Map<String, Object> arg0) {
        this.session = arg0;
    }

    public void setPollid(String pollid) {
        this.pollid = pollid;
    }

    public String getPollid() {
        return pollid;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setYourChoice(String yourChoice) {
        this.yourChoice = yourChoice;
    }

    public String getYourChoice() {
        return yourChoice;
    }
}

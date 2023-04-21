package struts2.sample07.action;

import java.util.Map;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import com.opensymphony.xwork2.ActionContext;

@Results({ @Result(name = "login", value = "login.jsp"), @Result(name = "menu", value = "menu.jsp") })
public class IndexAction {

    public String execute() {
        Map session = ActionContext.getContext().getSession();
        if (session.containsKey("login.user") == false) {
            return "login";
        }
        return "menu";
    }
}

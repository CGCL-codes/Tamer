package tags.app;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import beans.app.User;
import beans.app.Users;

public class HintAvailableTag extends TagSupport implements beans.app.Constants {

    public int doStartTag() throws JspException {
        Users users = (Users) pageContext.getAttribute(USERS_KEY, PageContext.APPLICATION_SCOPE);
        if (users != null) {
            HttpSession session = pageContext.getSession();
            String userName = (String) session.getAttribute(USERNAME_KEY);
            if (userName != null) {
                User user = users.getUser(userName);
                if (user != null) {
                    if (user.getPwdHint() != null) return EVAL_BODY_INCLUDE;
                }
            }
        } else {
            throw new JspException("Can't find users");
        }
        return SKIP_BODY;
    }
}

package demo;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 * Custom tag for retrieving a message.
 */
public class MessageTag extends TagSupport {

    private String folder;

    private String session;

    private int num = 1;

    /**
     * folder attribute setter method.
     */
    public String getFolder() {
        return folder;
    }

    /**
     * num attribute getter method.
     */
    public String getNum() {
        return Integer.toString(num);
    }

    /**
     * session attribute getter method.
     */
    public String getSession() {
        return session;
    }

    /**
     * folder setter method.
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }

    /**
     * num attribute setter method.
     */
    public void setNum(String num) {
        this.num = Integer.parseInt(num);
    }

    /**
     * session attribute setter method.
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * Method for processing the start of the tag.
     */
    public int doStartTag() throws JspException {
        MessageInfo messageinfo = new MessageInfo();
        try {
            Folder f = (Folder) pageContext.getAttribute(getFolder(), PageContext.SESSION_SCOPE);
            Message message = f.getMessage(num);
            messageinfo.setMessage(message);
            pageContext.setAttribute(getId(), messageinfo);
        } catch (Exception ex) {
            throw new JspException(ex.getMessage());
        }
        return SKIP_BODY;
    }
}

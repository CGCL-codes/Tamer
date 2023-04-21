package ro.gateway.aida.tags;

import java.io.IOException;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import ro.gateway.aida.servlet.EditActivityServlet;
import ro.xblue.translator.ShowEditButtonAction;
import ro.xblue.translator.ShowPageMessageTag;

/**
 * <p>Title: Romanian AIDA</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (comparator) 2003</p>
 * <p>Company: ro-gateway</p>
 * @author Mihai Popoaei, mihai_popoaei@yahoo.com, smike@intellisource.ro
 * @version 1.0-* @version $Id: PrjEdShowErrorTag.java,v 1.1 2005/07/20 12:08:58 mihaipostelnicu Exp $
 */
public class PrjEdShowErrorTag extends TagSupport {

    String label = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
       *
       */
    public int doStartTag() throws JspException {
        return (SKIP_BODY);
    }

    /**
       *
       */
    public int doEndTag() throws JspException {
        if (label == null) return EVAL_PAGE;
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        HttpSession session = req.getSession(true);
        Hashtable errors = (Hashtable) pageContext.getAttribute(EditActivityServlet.PNAME_HERRORS);
        if (errors == null) return EVAL_PAGE;
        String message_label = (String) errors.get(label);
        if (message_label == null) return EVAL_PAGE;
        boolean show_links_flag = session.getAttribute(ShowEditButtonAction.FLAG) != null;
        String message = "";
        if (show_links_flag) {
            message = ShowPageMessageTag.get_linked_message(pageContext, message_label);
        } else {
            message = ShowPageMessageTag.get_message(pageContext, message_label);
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<p style=\"color:red;\" class=\"error\">").append(message).append("</p>");
        JspWriter writer = pageContext.getOut();
        try {
            writer.print(sb.toString());
        } catch (IOException ioEx) {
            System.err.println(ioEx);
        }
        this.label = null;
        return EVAL_PAGE;
    }
}

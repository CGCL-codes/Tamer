package es.cea;

import java.io.IOException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class MenuTag extends TagSupport {

    @Override
    public int doStartTag() throws JspException {
        JspWriter writer = pageContext.getOut();
        HttpSession session = this.pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        ServletResponse response = pageContext.getResponse();
        try {
            if (session.getAttribute("usuario") != null) {
                writer.println("<h3>Menu de opciones para usuarios registrados</h3>");
                writer.println("<a href='#'>Opcion 1</a> - <a href='#'>Opcion 2</a> - <a href='#'>Opcion 3</a> - <a href='#'>Opcion 4</a>");
            } else {
                writer.println("<a href='#'>Opcion 1</a> - <a href='#'>Opcion 2</a>");
            }
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}

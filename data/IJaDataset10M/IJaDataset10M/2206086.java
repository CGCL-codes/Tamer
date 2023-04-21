package org.gnomekr.potron.web.tag;

import static org.gnomekr.potron.util.PotronConstants.RESOURCE_NAME;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MessageBoxTag.java
 * @author Xavier Cho
 * @version $Revision 1.1 $ $Date: 2005/07/07 15:20:15 $
 * @jsp:tag 
 *      name="msgbox"
 *      body-content="JSP"
 */
public class MessageBoxTag extends TagSupport {

    private static final long serialVersionUID = 3258407314078839091L;

    public static final String PARAM_NAME = "msg";

    private String parameter;

    /**
     * @jsp:attribute
     *      required="false"
     *      rtexprvalue="true"
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * @param parameter
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        if (parameter == null) {
            parameter = PARAM_NAME;
        }
        String key = pageContext.getRequest().getParameter(parameter);
        String message = null;
        if (key != null) {
            try {
                ResourceBundle resources = ResourceBundle.getBundle(RESOURCE_NAME);
                message = resources.getString(key);
            } catch (MissingResourceException e) {
                Log log = LogFactory.getLog(getClass());
                if (log.isWarnEnabled()) {
                    String msg = "Unable to find the specified message : " + key;
                    log.warn(msg, e);
                }
            }
        }
        if (message != null) {
            try {
                JspWriter out = pageContext.getOut();
                out.print("<script type=\"text/javascript\" ");
                out.println("language=\"javascript\">");
                out.print("     alert(\"");
                out.print(message);
                out.println("\");");
                out.println("</script>");
            } catch (IOException e) {
                String msg = "Unable to write tag content : " + e.getMessage();
                Log log = LogFactory.getLog(getClass());
                if (log.isErrorEnabled()) {
                    log.error(msg, e);
                }
                throw new JspException(msg);
            } finally {
                release();
            }
        } else {
            release();
        }
        return SKIP_BODY;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release() {
        this.parameter = null;
    }
}

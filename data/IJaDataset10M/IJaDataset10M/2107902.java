package wow.play.cricket.tags;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import wow.play.cricket.common.LCConstants;

/**
 * An error display tag, which will analyze the run time errors generated due to application / system
 * exceptions. The {@link AppExceptionHandler} will put the generated exceptions in a request object
 * with name {@link LCConstants.ERROR_BUNDLE}.
 * This tag will render HTML/Javascript code to display the pop-up error message box to user. Also depending
 * on no. of errors (single/multilple) it will show appropriate message boxes.
 *
 * Last changed by $Author: nurulsiddik $ on $Date: 2007-03-27 05:50:51 -0400 (Tue, 27 Mar 2007) $ as $Revision: 2 $
 *
 * @author Jatan Porecha
 * @version $Revision: 2 $
 */
public class ErrorTag extends TagSupport {

    /** Creates a new instance of ErrorTag */
    public ErrorTag() {
    }

    /**
     * This method renders the HTML/Javascript code to pop the error/status message box in case of any
     * application / system exception.
     * @return SKIP_BODY
     * @throws javax.servlet.jsp.JspException In case of any system exception.
     */
    public int doStartTag() throws JspException {
        System.out.println("Start Error tag");
        try {
            return wrapper();
        } catch (JspException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private int wrapper() throws JspException {
        Object[] error_data = (Object[]) pageContext.getRequest().getAttribute(LCConstants.ERROR_BUNDLE);
        String message_code = (String) pageContext.getRequest().getAttribute(LCConstants.STATUS_MESSAGE_CODE);
        JspWriter out = pageContext.getOut();
        if (error_data == null || error_data.length == 0) {
            if (message_code == null) {
                return SKIP_BODY;
            }
            try {
                out.println("<script>");
                out.println("document.status_message_return=fnShowMessage('" + message_code + "');");
                out.println("document.status_message_code='" + message_code + "';");
                out.println("</script>");
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new JspException(ex);
            }
            return SKIP_BODY;
        }
        String[] error_msgs = (String[]) error_data[0];
        String error_code = (String) error_data[1];
        String error_type = (String) error_data[2];
        try {
            out.println("<script>");
            if (error_msgs.length == 1) {
                renderSingleError(error_msgs[0], error_code, error_type);
            } else {
                renderErrorList(error_msgs);
            }
            out.println("</script>");
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new JspException(ex);
        }
        return SKIP_BODY;
    }

    private void renderSingleError(String error_msg, String error_code, String error_type) throws IOException {
        JspWriter out = pageContext.getOut();
        out.println("document.error_message_return=fnShowTextMessage('" + error_msg + "','" + error_type + "');");
        out.println("document.error_message_code='" + error_code + "';");
        out.println("document.error_message_type='" + error_type + "';");
    }

    private void renderErrorList(String[] error_msgs) throws IOException {
        JspWriter out = pageContext.getOut();
        out.println("var errmsgs = new Array();");
        for (String error_msg : error_msgs) {
            out.println("errmsgs[errmsgs.length] = {type:'E',text:'" + error_msg + "'};");
        }
        out.println("document.error_message_code=showModalDialog('/mmt/common/jsp/messagelist.html',errmsgs,'dialogWidth:500px;dialogHeight:400px;dialogTop:200px;status: no; titlebar: no; ');");
    }
}

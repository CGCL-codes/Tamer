package vqwiki.taglib;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import vqwiki.utils.Utilities;

/**
 * Tag which assigns to var the current username.
 *
 * @author garethc
 * @since Jan 7, 2003
 */
public class CurrentUserTag extends AbstractVarTag {

    private static final long serialVersionUID = -6659614843613570344L;

    private static final Logger logger = Logger.getLogger(CurrentUserTag.class.getName());

    /**
     *
     */
    public int doEndTag() throws JspException {
        try {
            String user = Utilities.getUserFromRequest((HttpServletRequest) this.pageContext.getRequest());
            if (user != null) {
                pageContext.setAttribute(this.var, user);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "", e);
        }
        return SKIP_BODY;
    }
}

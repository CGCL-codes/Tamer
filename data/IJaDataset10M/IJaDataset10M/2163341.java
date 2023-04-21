package vqwiki.taglib;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspException;
import vqwiki.WikiBase;
import vqwiki.svc.Notify;

/**
 * Returns true as var if user is subscribed to topic.
 *
 * @author garethc
 * @since Jan 7, 2003
 */
public class NotificationTag extends AbstractVarTag {

    private static final long serialVersionUID = 5614410807600191829L;

    private static final Logger logger = Logger.getLogger(NotificationTag.class.getName());

    private String userVar;

    private String topicVar;

    /**
     *
     */
    public int doEndTag() throws JspException {
        String virtualWiki = (String) this.pageContext.findAttribute(VIRTUAL_WIKI_KEY);
        String topic = (String) this.pageContext.findAttribute(topicVar);
        String user = (String) this.pageContext.findAttribute(userVar);
        try {
            WikiBase wikiBase = (WikiBase) this.pageContext.getServletContext().getAttribute(WIKI_BASE_KEY);
            Notify notifier = wikiBase.createNotify(virtualWiki, topic);
            pageContext.setAttribute(var, Boolean.valueOf(notifier.isMember(user)));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    /**
     *
     */
    public String getUserVar() {
        return userVar;
    }

    /**
     *
     */
    public void setUserVar(String userVar) {
        this.userVar = userVar;
    }

    /**
     *
     */
    public String getTopicVar() {
        return topicVar;
    }

    /**
     *
     */
    public void setTopicVar(String topicVar) {
        this.topicVar = topicVar;
    }
}

package org.netbeans.server.uihandler;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/** A JSP tag to initialize data from a statistic. Locates given statistic
 * and associates its values to the page context.
 *
 * @author Jaroslav Tulach
 */
public final class StatisticsTag extends SimpleTagSupport {

    private String statistic;

    /** Name of the statistic to initialize
     * @param statistic name of the statistic
     */
    public void setName(String statistic) {
        this.statistic = statistic;
    }

    /**
    * Registers the beans.
    */
    @Override
    public void doTag() throws JspException {
        try {
            LogsManager logsManager = LogsManager.getDefault();
            PageContext context = (PageContext) getJspContext();
            if (context.getRequest().getParameter("submit_netbeans_version") != null) {
                context.getSession().setAttribute("netbeans_version", context.getRequest().getParameter("netbeans_version"));
            }
            context.getRequest().setAttribute("stat_versions", logsManager.getPossibleVersions());
            logsManager.preparePageContext(context, Collections.singleton(statistic));
        } catch (InterruptedException ex) {
            throw new JspException(ex);
        } catch (ExecutionException ex) {
            throw new JspException(ex);
        }
    }
}

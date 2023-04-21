package org.apache.jetspeed.services.jsp.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.turbine.services.jsp.JspService;
import org.apache.ecs.ConcreteElement;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.jetspeed.services.resources.JetspeedResources;
import org.apache.jetspeed.services.rundata.JetspeedRunData;
import org.apache.jetspeed.util.template.JetspeedTool;

/**
 * Supporting class for the pane tag.
 * Builds the output of a PSML config file and insert it within the 
 * current JSP page
 *
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @author <a href="mailto:morciuch@apache.org">Mark Orciuch</a> 
 * @version $Id: JetspeedPaneTag.java,v 1.6 2004/02/23 03:59:40 jford Exp $
 */
public class JetspeedPaneTag extends TagSupport {

    /**
     * Static initialization of the logger for this class
     */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(JetspeedPaneTag.class.getName());

    private String name = null;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Method called when the tag is encountered to send attributes to the
     * output stream
     *
     * @return SKIP_BODY, as it is intended to be a single tag.
     */
    public int doStartTag() throws JspException {
        JetspeedRunData data = (JetspeedRunData) pageContext.getAttribute(JspService.RUNDATA, PageContext.REQUEST_SCOPE);
        if (this.name == null) {
            this.name = JetspeedResources.getString("screen.homepage");
        }
        try {
            pageContext.getOut().flush();
            ConcreteElement result = new ConcreteElement();
            if (data != null && data.getUser() != null) {
                JetspeedTool jt = new JetspeedTool(data);
                String jspeid = (String) data.getUser().getTemp("js_peid");
                if (jspeid != null) {
                    data.setMode(JetspeedRunData.MAXIMIZE);
                    result = jt.getPortletById(jspeid);
                } else {
                    result = jt.getPane(this.name);
                }
            }
            if (result != null) {
                result.setCodeSet(data.getResponse().getCharacterEncoding());
                result.output(data.getResponse().getWriter());
            }
        } catch (Exception e) {
            String message = "Error processing name '" + name + "'.";
            logger.error(message, e);
            try {
                data.getOut().print("Error processing ecs screen '" + name + "'. See log for more information.");
            } catch (java.io.IOException ioe) {
            }
        }
        return SKIP_BODY;
    }
}

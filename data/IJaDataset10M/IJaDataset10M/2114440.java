package org.eaasyst.eaa.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.util.MessageResources;
import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.utils.StringUtils;

/**
 * <p>Tag support class for the <code>contentContainer</code> tag, which inserts
 * the container that is wrapped around the application-specific view component.</p>
 *
 * @version 2.9.1
 * @author Jeff Chilton
 */
public class ContentContainerTag extends TagSupport {

    private static final long serialVersionUID = 1;

    private static final MessageResources localStrings = MessageResources.getMessageResources("org.eaasyst.eaa.syst.LocalStrings");

    private String key = null;

    /**
	 * <p>This method inserts the specified item onto the rendered page.</p>
	 * 
	 * @throws JspException if an error occurs while processing this tag
	 * @since Eaasy Street 2.3
	 */
    public int doStartTag() throws JspException {
        StringBuffer buffer = new StringBuffer();
        String contentRowStartTemplate = "contentRowNoHeader_start";
        buffer.append(EaasyStreet.getThemeProperty("contentContainer_start"));
        if (!StringUtils.nullOrBlank(key)) {
            MessageResources appResources = EaasyStreet.getApplicationResources();
            String label = appResources.getMessage(pageContext.getRequest().getLocale(), key);
            String template = EaasyStreet.getThemeProperty("headerRow");
            buffer.append(Constants.LF);
            buffer.append(StringUtils.replace(template, "{0}", label));
            contentRowStartTemplate = "contentRow_start";
        }
        buffer.append(Constants.LF);
        buffer.append(EaasyStreet.getThemeProperty(contentRowStartTemplate));
        try {
            pageContext.getOut().print(buffer.toString());
        } catch (Exception e) {
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    /**
	 * <p>Process the end of this tag.</p>
	 *
	 * @throws JspException if a JSP exception has occurred
	 * @since Eaasy Street 2.3
	 */
    public int doEndTag() throws JspException {
        StringBuffer buffer = new StringBuffer();
        buffer.append(EaasyStreet.getThemeProperty("contentRow_end"));
        buffer.append(Constants.LF);
        String template = EaasyStreet.getThemeProperty("controlRow");
        buffer.append(StringUtils.replace(template, "{0}", getControls()));
        buffer.append(Constants.LF);
        buffer.append(EaasyStreet.getThemeProperty("contentContainer_end"));
        try {
            pageContext.getOut().print(buffer.toString());
        } catch (Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    /**
	 * <p>This method obtains the system and application controls from the
	 * request object and formats them.</p>
	 * 
	 * @return a string containing the application controls
	 * @since Eaasy Street 2.3
	 */
    private String getControls() {
        String[] systActions = (String[]) pageContext.getRequest().getAttribute(Constants.RAK_SYST_ACTIONS);
        String[] applActions = (String[]) pageContext.getRequest().getAttribute(Constants.RAK_APPL_ACTIONS);
        StringBuffer buffer = new StringBuffer();
        if (applActions != null) {
            for (int i = 0; i < applActions.length; i++) {
                if (!buffer.toString().equals(Constants.EMPTY_STRING)) {
                    buffer.append("&nbsp; ");
                }
                buffer.append(localStrings.getMessage("template.application.control", Constants.RPK_APPLICATION_ACTION, applActions[i]) + Constants.LF);
            }
        }
        if (systActions != null) {
            for (int i = 0; i < systActions.length; i++) {
                if (i > 0) {
                    buffer.append("&nbsp; ");
                }
                if (systActions[i].equalsIgnoreCase("cancel") || systActions[i].equalsIgnoreCase("exit")) {
                    buffer.append(localStrings.getMessage("template.application.cancel.control", Constants.RPK_SYSTEM_ACTION, systActions[i]) + Constants.LF);
                } else {
                    buffer.append(localStrings.getMessage("template.application.control", Constants.RPK_SYSTEM_ACTION, systActions[i]) + Constants.LF);
                }
            }
        }
        return buffer.toString();
    }

    /**
	 * @return
	 */
    public String getKey() {
        return key;
    }

    /**
	 * @param string
	 */
    public void setKey(String string) {
        key = string;
    }
}

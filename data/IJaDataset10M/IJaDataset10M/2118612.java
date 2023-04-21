package org.opencms.workplace.administration;

import org.opencms.workplace.CmsWorkplace;

/**
 * Menu item implementation that works as an context help text container.<p>
 * 
 * @author Michael Moossen  
 * 
 * @version $Revision: 1.7 $ 
 * 
 * @since 6.0.0 
 */
public class CmsAdminContextHelpMenuItem extends CmsAdminMenuItem {

    /**
     * Default Constructor.<p>
     */
    public CmsAdminContextHelpMenuItem() {
        super("conhelp", "Context Help", "", "", "", true, null);
    }

    /**
     * @see org.opencms.workplace.administration.CmsAdminMenuItem#itemHtml(CmsWorkplace)
     */
    public String itemHtml(CmsWorkplace wp) {
        StringBuffer html = new StringBuffer(512);
        html.append("<table border='0' cellspacing='0' cellpadding='0' width='100%' id='conhelp' class='node'>\n");
        html.append("\t<tr>\n");
        html.append("\t\t<td width='100%'><div id='contexthelp'><div>\n");
        html.append("\t\t\t<span id='contexthelp_text' class='hint'></span>\n");
        html.append("\t\t</div></div></td>\n");
        html.append("\t</tr>\n");
        html.append("</table>\n");
        return html.toString();
    }
}

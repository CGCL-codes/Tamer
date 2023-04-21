package org.opencms.workplace.list;

import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.CmsWorkplace;
import org.opencms.workplace.tools.A_CmsHtmlIconButton;
import org.opencms.workplace.tools.CmsHtmlIconButtonStyleEnum;

/**
 * Default implementation of a list multi action.<p>
 * 
 * @author Michael Moossen  
 * 
 * @version $Revision: 1.20 $ 
 * 
 * @since 6.0.0 
 */
public class CmsListMultiAction extends A_CmsListAction {

    /**
     * Default Constructor.<p>
     * 
     * @param id the unique id
     */
    public CmsListMultiAction(String id) {
        super(id);
    }

    /**
     * @see org.opencms.workplace.tools.I_CmsHtmlIconButton#buttonHtml(CmsWorkplace)
     */
    public String buttonHtml(CmsWorkplace wp) {
        if (!isVisible()) {
            return "";
        }
        if (isEnabled()) {
            String onClic = "listMAction('" + getListId() + "','" + getId() + "', '" + CmsStringUtil.escapeJavaScript(wp.resolveMacros(getConfirmationMessage().key(wp.getLocale()))) + "', " + CmsHtmlList.NO_SELECTION_HELP_VAR + ");";
            return A_CmsHtmlIconButton.defaultButtonHtml(CmsHtmlIconButtonStyleEnum.SMALL_ICON_TEXT, getId(), getName().key(wp.getLocale()), getHelpText().key(wp.getLocale()), isEnabled(), getIconPath(), null, onClic);
        }
        return "";
    }
}

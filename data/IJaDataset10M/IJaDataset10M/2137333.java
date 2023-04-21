package org.opencms.widgets;

import org.opencms.file.CmsObject;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.CmsWorkplace;

/**
 * Provides an OpenCms Principal selection widget, for use on a widget dialog.<p>
 *
 * @author Michael Moossen
 * 
 * @version $Revision: 1.4 $ 
 * 
 * @since 6.5.6 
 */
public class CmsPrincipalWidget extends A_CmsWidget {

    /** Configuration parameter to set the flags of the principals to display, optional. */
    public static final String CONFIGURATION_FLAGS = "flags";

    /** The the flags used in the popup window. */
    private Integer m_flags;

    /**
     * Creates a new principals selection widget.<p>
     */
    public CmsPrincipalWidget() {
        this("");
    }

    /**
     * Creates a new principals selection widget with the parameters to configure the popup window behaviour.<p>
     * 
     * @param flags the group flags to restrict the group selection, can be <code>null</code>
     */
    public CmsPrincipalWidget(Integer flags) {
        m_flags = flags;
    }

    /**
     * Creates a new principals selection widget with the given configuration.<p>
     * 
     * @param configuration the configuration to use
     */
    public CmsPrincipalWidget(String configuration) {
        super(configuration);
    }

    /**
     * @see org.opencms.widgets.A_CmsWidget#getConfiguration()
     */
    public String getConfiguration() {
        StringBuffer result = new StringBuffer(8);
        if (m_flags != null) {
            if (result.length() > 0) {
                result.append("|");
            }
            result.append(CONFIGURATION_FLAGS);
            result.append("=");
            result.append(m_flags);
        }
        return result.toString();
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogIncludes(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog)
     */
    public String getDialogIncludes(CmsObject cms, I_CmsWidgetDialog widgetDialog) {
        StringBuffer result = new StringBuffer(16);
        result.append(getJSIncludeFile(CmsWorkplace.getSkinUri() + "components/widgets/principalselector.js"));
        return result.toString();
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogWidget(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog, org.opencms.widgets.I_CmsWidgetParameter)
     */
    public String getDialogWidget(CmsObject cms, I_CmsWidgetDialog widgetDialog, I_CmsWidgetParameter param) {
        String id = param.getId();
        StringBuffer result = new StringBuffer(128);
        result.append("<td class=\"xmlTd\">");
        result.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"maxwidth\"><tr><td style=\"width: 100%;\">");
        result.append("<input style=\"width: 99%;\" class=\"xmlInput");
        if (param.hasError()) {
            result.append(" xmlInputError");
        }
        result.append("\" value=\"");
        result.append(param.getStringValue(cms));
        result.append("\" name=\"");
        result.append(id);
        result.append("\" id=\"");
        result.append(id);
        result.append("\"></td>");
        result.append(widgetDialog.dialogHorizontalSpacer(10));
        result.append("<td><table class=\"editorbuttonbackground\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
        result.append(widgetDialog.button(getButtonJs(id, "EDITOR"), null, "principal", org.opencms.workplace.Messages.GUI_DIALOG_BUTTON_SEARCH_0, widgetDialog.getButtonStyle()));
        result.append("</tr></table>");
        result.append("</td></tr></table>");
        result.append("</td>");
        return result.toString();
    }

    /**
     * Returns the needed java script for the search button.<p>
     * 
     * @param id the id of the widget to generate the search button for
     * @param form the id of the form where to which the widget belongs
     * 
     * @return javascript code
     */
    public String getButtonJs(String id, String form) {
        StringBuffer buttonJs = new StringBuffer(8);
        buttonJs.append("javascript:openPrincipalWin('");
        buttonJs.append(OpenCms.getSystemInfo().getOpenCmsContext());
        buttonJs.append("/system/workplace/commons/principal_selection.jsp");
        buttonJs.append("','" + form + "',  '");
        buttonJs.append(id);
        buttonJs.append("', document, '");
        if (m_flags != null) {
            buttonJs.append(m_flags);
        } else {
            buttonJs.append("null");
        }
        buttonJs.append("'");
        buttonJs.append(");");
        return buttonJs.toString();
    }

    /**
     * Returns the flags, or <code>null</code> if all.<p>
     *
     * @return the flags, or <code>null</code> if all
     */
    public Integer getFlags() {
        return m_flags;
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#newInstance()
     */
    public I_CmsWidget newInstance() {
        return new CmsPrincipalWidget(getConfiguration());
    }

    /**
     * @see org.opencms.widgets.A_CmsWidget#setConfiguration(java.lang.String)
     */
    public void setConfiguration(String configuration) {
        m_flags = null;
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(configuration)) {
            int flagsIndex = configuration.indexOf(CONFIGURATION_FLAGS);
            if (flagsIndex != -1) {
                String flags = configuration.substring(CONFIGURATION_FLAGS.length() + 1);
                if (flags.indexOf('|') != -1) {
                    flags = flags.substring(0, flags.indexOf('|'));
                }
                try {
                    m_flags = Integer.valueOf(flags);
                } catch (Throwable t) {
                }
            }
        }
        super.setConfiguration(configuration);
    }
}

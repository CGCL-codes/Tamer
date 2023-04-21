package org.opencms.widgets;

import org.opencms.file.CmsObject;
import org.opencms.i18n.CmsEncoder;

/**
 * Provides a standard HTML form input widget, for use on a widget dialog.<p>
 *
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.12 $ 
 * 
 * @since 6.0.0 
 */
public class CmsInputWidget extends A_CmsWidget {

    /**
     * Creates a new input widget.<p>
     */
    public CmsInputWidget() {
        this("");
    }

    /**
     * Creates a new input widget with the given configuration.<p>
     * 
     * @param configuration the configuration to use
     */
    public CmsInputWidget(String configuration) {
        super(configuration);
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogWidget(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog, org.opencms.widgets.I_CmsWidgetParameter)
     */
    public String getDialogWidget(CmsObject cms, I_CmsWidgetDialog widgetDialog, I_CmsWidgetParameter param) {
        String id = param.getId();
        StringBuffer result = new StringBuffer(16);
        result.append("<td class=\"xmlTd\">");
        result.append("<input class=\"xmlInput textInput");
        if (param.hasError()) {
            result.append(" xmlInputError");
        }
        result.append("\"");
        result.append(" name=\"");
        result.append(id);
        result.append("\" id=\"");
        result.append(id);
        result.append("\" value=\"");
        result.append(CmsEncoder.escapeXml(param.getStringValue(cms)));
        result.append("\">");
        result.append("</td>");
        return result.toString();
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#newInstance()
     */
    public I_CmsWidget newInstance() {
        return new CmsInputWidget(getConfiguration());
    }
}

package org.opencms.widgets;

import org.opencms.file.CmsObject;
import org.opencms.util.CmsMacroResolver;
import java.util.Iterator;
import java.util.List;

/**
 * Provides a widget for a standard HTML form select box.<p>
 * 
 * Please see the documentation of <code>{@link org.opencms.widgets.CmsSelectWidgetOption}</code> for a description 
 * about the configuration String syntax for the select options.<p>
 *
 * The select widget does use the following select options:<ul>
 * <li><code>{@link org.opencms.widgets.CmsSelectWidgetOption#getValue()}</code> for the <code>value</code> of the HTML select box
 * <li><code>{@link org.opencms.widgets.CmsSelectWidgetOption#isDefault()}</code> for pre-selecting a specific value 
 * <li><code>{@link org.opencms.widgets.CmsSelectWidgetOption#getOption()}</code> for the <code>option</code> of the HTML select box
 * </ul>
 * <p>
 *
 * @author Andreas Zahner 
 * 
 * @version $Revision: 1.15 $ 
 * 
 * @since 6.0.0 
 */
public class CmsSelectWidget extends A_CmsSelectWidget {

    /**
     * Creates a new select widget.<p>
     */
    public CmsSelectWidget() {
        super();
    }

    /**
     * Creates a select widget with the select options specified in the given configuration List.<p>
     * 
     * The list elements must be of type <code>{@link CmsSelectWidgetOption}</code>.<p>
     * 
     * @param configuration the configuration (possible options) for the select widget
     * 
     * @see CmsSelectWidgetOption
     */
    public CmsSelectWidget(List configuration) {
        super(configuration);
    }

    /**
     * Creates a select widget with the specified select options.<p>
     * 
     * @param configuration the configuration (possible options) for the select box
     */
    public CmsSelectWidget(String configuration) {
        super(configuration);
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogWidget(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog, org.opencms.widgets.I_CmsWidgetParameter)
     */
    public String getDialogWidget(CmsObject cms, I_CmsWidgetDialog widgetDialog, I_CmsWidgetParameter param) {
        String id = param.getId();
        StringBuffer result = new StringBuffer(16);
        result.append("<td class=\"xmlTd\" style=\"height: 25px;\"><select class=\"xmlInput");
        if (param.hasError()) {
            result.append(" xmlInputError");
        }
        result.append("\" name=\"");
        result.append(id);
        result.append("\" id=\"");
        result.append(id);
        result.append("\">");
        List options = parseSelectOptions(cms, widgetDialog, param);
        String selected = getSelectedValue(cms, param);
        Iterator i = options.iterator();
        while (i.hasNext()) {
            CmsSelectWidgetOption option = (CmsSelectWidgetOption) i.next();
            result.append("<option value=\"");
            result.append(option.getValue());
            result.append("\"");
            if ((selected != null) && selected.equals(option.getValue())) {
                result.append(" selected=\"selected\"");
            }
            result.append(">");
            result.append(option.getOption());
            result.append("</option>");
        }
        result.append("</select>");
        result.append("</td>");
        return result.toString();
    }

    /**
     * @see org.opencms.widgets.A_CmsWidget#getWidgetStringValue(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog, org.opencms.widgets.I_CmsWidgetParameter)
     */
    public String getWidgetStringValue(CmsObject cms, I_CmsWidgetDialog widgetDialog, I_CmsWidgetParameter param) {
        String result = super.getWidgetStringValue(cms, widgetDialog, param);
        String configuration = CmsMacroResolver.resolveMacros(getConfiguration(), cms, widgetDialog.getMessages());
        if (configuration == null) {
            configuration = param.getDefault(cms);
        }
        List options = CmsSelectWidgetOption.parseOptions(configuration);
        for (int m = 0; m < options.size(); m++) {
            CmsSelectWidgetOption option = (CmsSelectWidgetOption) options.get(m);
            if (result.equals(option.getValue())) {
                result = option.getOption();
                break;
            }
        }
        return result;
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#newInstance()
     */
    public I_CmsWidget newInstance() {
        return new CmsSelectWidget(getConfiguration());
    }
}

package org.opencms.widgets;

import org.opencms.file.CmsObject;
import org.opencms.i18n.CmsEncoder;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsStringUtil;
import java.util.Map;
import org.apache.commons.logging.Log;

/**
 * Provides a widget that creates a rich input field using the matching component, for use on a widget dialog.<p>
 * 
 * The matching component is determined by checking the installed editors for the best matching component to use.<p>
 *
 * @author Andreas Zahner
 * 
 * @version $Revision: 1.4 $ 
 * 
 * @since 6.0.1 
 */
public class CmsHtmlWidget extends A_CmsHtmlWidget {

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsHtmlWidget.class);

    /** The editor widget to use depending on the current users settings, current browser and installed editors. */
    private I_CmsWidget m_editorWidget;

    /**
     * Creates a new html editing widget.<p>
     */
    public CmsHtmlWidget() {
        this("");
    }

    /**
     * Creates a new html editing widget with the given configuration.<p>
     * 
     * @param configuration the configuration to use
     */
    public CmsHtmlWidget(CmsHtmlWidgetOption configuration) {
        super(configuration);
    }

    /**
     * Creates a new html editing widget with the given configuration.<p>
     * 
     * @param configuration the configuration to use
     */
    public CmsHtmlWidget(String configuration) {
        super(configuration);
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogIncludes(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog)
     */
    public String getDialogIncludes(CmsObject cms, I_CmsWidgetDialog widgetDialog) {
        return getEditorWidget(cms, widgetDialog).getDialogIncludes(cms, widgetDialog);
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogInitCall(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog)
     */
    public String getDialogInitCall(CmsObject cms, I_CmsWidgetDialog widgetDialog) {
        return getEditorWidget(cms, widgetDialog).getDialogInitCall(cms, widgetDialog);
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogInitMethod(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog)
     */
    public String getDialogInitMethod(CmsObject cms, I_CmsWidgetDialog widgetDialog) {
        return getEditorWidget(cms, widgetDialog).getDialogInitMethod(cms, widgetDialog);
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogWidget(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog, org.opencms.widgets.I_CmsWidgetParameter)
     */
    public String getDialogWidget(CmsObject cms, I_CmsWidgetDialog widgetDialog, I_CmsWidgetParameter param) {
        return getEditorWidget(cms, widgetDialog).getDialogWidget(cms, widgetDialog, param);
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#newInstance()
     */
    public I_CmsWidget newInstance() {
        return new CmsHtmlWidget(getConfiguration());
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#setEditorValue(org.opencms.file.CmsObject, java.util.Map, org.opencms.widgets.I_CmsWidgetDialog, org.opencms.widgets.I_CmsWidgetParameter)
     */
    public void setEditorValue(CmsObject cms, Map formParameters, I_CmsWidgetDialog widgetDialog, I_CmsWidgetParameter param) {
        String[] values = (String[]) formParameters.get(param.getId());
        if ((values != null) && (values.length > 0)) {
            String val = CmsEncoder.decode(values[0], CmsEncoder.ENCODING_UTF_8);
            param.setStringValue(cms, val);
        }
    }

    /**
     * Returns the editor widget to use depending on the current users settings, current browser and installed editors.<p>
     * 
     * @param cms the current CmsObject
     * @param widgetDialog the dialog where the widget is used on
     * @return the editor widget to use depending on the current users settings, current browser and installed editors
     */
    private I_CmsWidget getEditorWidget(CmsObject cms, I_CmsWidgetDialog widgetDialog) {
        if (m_editorWidget == null) {
            String widgetClassName = OpenCms.getWorkplaceManager().getWorkplaceEditorManager().getWidgetEditor(cms.getRequestContext(), widgetDialog.getUserAgent());
            boolean foundWidget = true;
            if (CmsStringUtil.isEmpty(widgetClassName)) {
                widgetClassName = CmsTextareaWidget.class.getName();
                foundWidget = false;
            }
            try {
                if (foundWidget) {
                    Class widgetClass = Class.forName(widgetClassName);
                    A_CmsHtmlWidget editorWidget = (A_CmsHtmlWidget) widgetClass.newInstance();
                    editorWidget.setHtmlWidgetOption(getHtmlWidgetOption());
                    m_editorWidget = editorWidget;
                } else {
                    Class widgetClass = Class.forName(widgetClassName);
                    I_CmsWidget editorWidget = (I_CmsWidget) widgetClass.newInstance();
                    editorWidget.setConfiguration("15");
                    m_editorWidget = editorWidget;
                }
            } catch (Exception e) {
                LOG.error(Messages.get().container(Messages.LOG_CREATE_HTMLWIDGET_INSTANCE_FAILED_1, widgetClassName).key());
            }
        }
        return m_editorWidget;
    }
}

package ui2swt.swt;

import ui2swt.ClassDefinition;
import ui2swt.UIDefinition;
import ui2swt.Widget;

public class QTextBrowserTranslator extends AbstractWidgetTranslator {

    public QTextBrowserTranslator() {
        super();
    }

    protected void setProperties(UIDefinition iUIDefinition, Widget iWidget) {
        SWTWidgetHelper.setAttributeProperties(iUIDefinition, iWidget);
        SWTWidgetHelper.setExpandProperties(iUIDefinition, iWidget);
        SWTWidgetHelper.setMinimumSizeProperties(iUIDefinition, iWidget);
        this.setPackageProperty(iUIDefinition, iWidget);
        this.setClassProperty(iUIDefinition, iWidget);
        this.setDefaultExpandProperties(iUIDefinition, iWidget);
        this.setStyleProperty(iUIDefinition, iWidget);
    }

    protected void generateConstructCode(UIDefinition iUIDefinition, Widget iWidget, ClassDefinition iClassDefinition) {
        SWTWidgetHelper.generateConstructCode(iUIDefinition, iWidget, iClassDefinition);
    }

    protected void generateSetFieldsCode(UIDefinition iUIDefinition, Widget iWidget, ClassDefinition iClassDefinition) {
        SWTWidgetHelper.generateSetEnabledCode(iUIDefinition, iWidget, iClassDefinition);
        SWTWidgetHelper.generateSetSizeCode(iUIDefinition, iWidget, iClassDefinition);
        SWTWidgetHelper.generateSetToolTipCode(iUIDefinition, iWidget, iClassDefinition);
        SWTTextHelper.generateSetTextCode(iUIDefinition, iWidget, iClassDefinition);
    }

    private void setPackageProperty(UIDefinition iUIDefinition, Widget iWidget) {
        iWidget.setProperty(SWTWidgetHelper.SWT_PACKAGE, "org.eclipse.swt.browser");
    }

    private void setClassProperty(UIDefinition iUIDefinition, Widget iWidget) {
        iWidget.setProperty(SWTWidgetHelper.SWT_CLASS, "Browser");
    }

    private void setDefaultExpandProperties(UIDefinition iUIDefinition, Widget iWidget) {
        iWidget.setIntProperty(SWTWidgetHelper.SWT_DEFAULT_EXPAND_HORIZONTAL, SWTWidgetHelper.SWT_EXPAND_YES);
        iWidget.setIntProperty(SWTWidgetHelper.SWT_DEFAULT_EXPAND_VERTICAL, SWTWidgetHelper.SWT_EXPAND_YES);
    }

    private void setStyleProperty(UIDefinition iUIDefinition, Widget iWidget) {
        if (SWTFrameHelper.hasFrame(iUIDefinition, iWidget)) {
            iWidget.setProperty(SWTWidgetHelper.SWT_STYLE, "SWT.BORDER");
        }
    }
}

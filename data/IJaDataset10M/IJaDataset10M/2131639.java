package org.plazmaforge.studio.reportdesigner.dialogs.subreport;

import org.eclipse.swt.widgets.Shell;
import org.plazmaforge.studio.reportdesigner.ReportDesignerResources;
import org.plazmaforge.studio.reportdesigner.dialogs.AbstractElementParameterEditDialog;
import org.plazmaforge.studio.reportdesigner.model.data.ElementParameter;
import org.plazmaforge.studio.reportdesigner.model.subreport.Subreport;
import org.plazmaforge.studio.reportdesigner.model.subreport.SubreportParameter;

/**
 * 
 * @author ohapon
 *
 */
public class SubreportParameterEditDialog extends AbstractElementParameterEditDialog {

    private Subreport subreport;

    public SubreportParameterEditDialog(Shell parentShell, int mode, Subreport subreport, SubreportParameter subreportParameter) {
        super(parentShell, mode, subreportParameter);
        this.subreport = subreport;
    }

    protected String getNameLabel() {
        return ReportDesignerResources.Subreport_Parameter_Name;
    }

    protected String getExpressionLabel() {
        return ReportDesignerResources.Default_Value_Expression;
    }

    protected void addParameter(ElementParameter parameter) {
        subreport.addParameter((SubreportParameter) parameter);
    }

    protected ElementParameter getParameter(String name) {
        return subreport.getParameter(name);
    }
}

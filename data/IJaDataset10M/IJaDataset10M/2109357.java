package org.plazmaforge.studio.reportdesigner.dialogs.chart;

import org.eclipse.swt.widgets.Shell;
import org.plazmaforge.studio.reportdesigner.ReportDesignerResources;
import org.plazmaforge.studio.reportdesigner.dialogs.AbstractElementParameterEditDialog;
import org.plazmaforge.studio.reportdesigner.model.Hyperlink;
import org.plazmaforge.studio.reportdesigner.model.HyperlinkParameter;
import org.plazmaforge.studio.reportdesigner.model.data.ElementParameter;

/** 
 * @author Oleh Hapon
 * $Id: HyperlinkParameterEditDialog.java,v 1.6 2010/04/28 06:43:09 ohapon Exp $
 */
public class HyperlinkParameterEditDialog extends AbstractElementParameterEditDialog {

    private Hyperlink hyperlink;

    public HyperlinkParameterEditDialog(Shell parentShell, int command, Hyperlink hyperlink, HyperlinkParameter hyperlinkParameter) {
        super(parentShell, command, hyperlinkParameter);
        this.hyperlink = hyperlink;
    }

    protected String getNameLabel() {
        return ReportDesignerResources.Link_Parameter_Name;
    }

    protected String getExpressionLabel() {
        return ReportDesignerResources.Value_Expression;
    }

    protected void addParameter(ElementParameter parameter) {
        hyperlink.addHyperlinkParameter((HyperlinkParameter) parameter);
    }

    protected ElementParameter getParameter(String name) {
        return hyperlink.getHyperlinkParameter(name);
    }
}

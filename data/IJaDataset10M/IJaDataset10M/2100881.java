package org.plazmaforge.studio.reportdesigner.actions;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.plazmaforge.studio.reportdesigner.ReportDesignerResources;
import org.plazmaforge.studio.reportdesigner.dialogs.DatasetListDialog;
import org.plazmaforge.studio.reportdesigner.model.Report;

public class DatasetListAction extends AbstractReportAction {

    public static final String ID = DatasetListAction.class.getName();

    public static final String NAME = ReportDesignerResources.Subdatasets;

    public DatasetListAction(IWorkbenchPart part, int style) {
        super(part, style);
        initialize();
    }

    public DatasetListAction(IWorkbenchPart part) {
        super(part);
        initialize();
    }

    private void initialize() {
        setText(NAME);
        setId(ID);
    }

    protected boolean calculateEnabled() {
        return true;
    }

    protected void runBusy() {
        Report report = getReport();
        if (report == null) {
            return;
        }
        Shell shell = getShell();
        DatasetListDialog dialog = new DatasetListDialog(shell, report);
        dialog.setReportEditor(getReportEditor());
        dialog.open();
    }
}

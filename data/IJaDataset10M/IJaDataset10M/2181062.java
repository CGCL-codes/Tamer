package org.plazmaforge.studio.reportdesigner.dialogs;

import org.eclipse.swt.widgets.Shell;
import org.plazmaforge.studio.reportdesigner.editor.ReportEditor;
import org.plazmaforge.studio.reportdesigner.model.Band;
import org.plazmaforge.studio.reportdesigner.model.IDesignElement;
import org.plazmaforge.studio.reportdesigner.model.Report;
import org.plazmaforge.studio.reportdesigner.model.DesignElement;

public class EditDialogHelper {

    public static void openEditDialog(Shell shell, ReportEditor reportEditor, IDesignElement element) {
        Report report = reportEditor.getReport();
        if (element instanceof Report) {
            ReportEditDialog dialog = new ReportEditDialog(shell, report);
            dialog.setReportEditor(reportEditor);
            dialog.open();
        } else if (element instanceof Band) {
            BandEditDialog dialog = new BandEditDialog(shell, report.getBandInfo((Band) element));
            dialog.setReportEditor(reportEditor);
            dialog.open();
        } else {
            ElementEditDialog dialog = new ElementEditDialog(shell);
            dialog.setForceChange(true);
            dialog.setReportEditor(reportEditor);
            dialog.setDesignElement((DesignElement) element);
            dialog.open();
        }
    }
}

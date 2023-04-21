package org.plazmaforge.studio.reportdesigner.dialogs.providers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.plazmaforge.studio.reportdesigner.ReportDesignerResources;
import org.plazmaforge.studio.reportdesigner.dialogs.ImportEditDialog;

public class ImportProvider extends AbstractReportElementProvider {

    public ImportProvider(IObjectSelector parentSelector) {
        super(parentSelector);
        setName(ReportDesignerResources.Report_imports);
    }

    public ImportProvider(Object parent, IContentProvider contentProvider, ILabelProvider labelProvider) {
        super(parent, contentProvider, labelProvider);
        setTitle(ReportDesignerResources.Report_imports);
        setName(ReportDesignerResources.Report_imports);
        setColumnNames(new String[] { ReportDesignerResources.Report_import });
        setColumnSizes(new int[] { 300 });
        setColumnStyles(new int[] { SWT.NONE });
    }

    @Override
    public Object createElement() {
        return null;
    }

    @Override
    public boolean deleteElement(Object element) {
        int index = (element == null) ? -1 : (Integer) element;
        getReport().removeImport(index);
        return true;
    }

    @Override
    public Dialog createDialog(Shell shell, int mode, Object element) {
        int index = (element == null) ? -1 : (Integer) element;
        ImportEditDialog dialog = new ImportEditDialog(getShell(), mode, getReport(), index);
        dialog.setReportEditor(getReportEditor());
        return dialog;
    }

    protected void doEditAction() {
        int index = getSelectionElementIndex();
        if (index < 0) {
            return;
        }
        Dialog dialog = createDialog(getShell(), EDIT, index);
        if (dialog == null) {
            return;
        }
        dialog.open();
        getTableViewer().refresh();
    }

    protected void doAddAction() {
        Dialog dialog = createDialog(getShell(), CREATE, -1);
        if (dialog == null) {
            return;
        }
        dialog.open();
        getTableViewer().refresh();
    }

    protected void doDeleteAction() {
        int index = getSelectionElementIndex();
        if (index < 0) {
            return;
        }
        Table table = getTableViewer().getTable();
        if (!deleteElement(index)) {
            return;
        }
        updateModel();
        index--;
        if (index < 0) {
            index = 0;
        }
        if (index < table.getItemCount()) {
            table.select(index);
        }
        getTableViewer().refresh();
        updateButtons();
    }
}

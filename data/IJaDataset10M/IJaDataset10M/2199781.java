package com.ivis.xprocess.ui.tables.util;

import java.io.File;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Tree;
import com.ivis.xprocess.ui.tables.columns.XProcessColumnManager;
import com.ivis.xprocess.ui.tables.viewmanagers.ViewManager;
import com.ivis.xprocess.ui.util.ArtifactManager;
import com.ivis.xprocess.ui.util.DialogUtil;
import com.ivis.xprocess.ui.util.ViewUtil;

public class ExportRowManager {

    protected ViewManager viewManager;

    protected XProcessColumnManager columnManager;

    protected String separator = ",";

    protected String exportFilename;

    protected boolean includeHiddenChildren = false;

    public ExportRowManager(ViewManager viewManager, XProcessColumnManager columnManager, String separator) {
        this.viewManager = viewManager;
        this.columnManager = columnManager;
        this.separator = separator;
    }

    public void export() {
        boolean mayHaveChildren = false;
        if (viewManager.getControl() instanceof Tree) {
            mayHaveChildren = true;
        }
        ExportRowsToFileDialog exportRowsToFileDialog = new ExportRowsToFileDialog(ViewUtil.getCurrentShell(), mayHaveChildren);
        if (exportRowsToFileDialog.open() == IDialogConstants.OK_ID) {
            exportFilename = exportRowsToFileDialog.getFilePath();
            includeHiddenChildren = exportRowsToFileDialog.includeHiddenChildren();
            File file = new File(exportFilename);
            if (file.exists()) {
                if (!DialogUtil.openQuestionDialog("Export Rows", "The export file already exists. Do you want to overwrite it?")) {
                    return;
                }
            }
            separator = exportRowsToFileDialog.getSeparator();
            IExportRows exportRows = null;
            if (!includeHiddenChildren) {
                if (viewManager.getControl() instanceof Tree) {
                    exportRows = new ExportRows(viewManager, columnManager, separator);
                } else {
                    exportRows = new ExportTableRows(viewManager, columnManager, separator);
                }
            } else {
                exportRows = new ExportWrappers(viewManager, columnManager, separator);
            }
            exportRows.export(exportFilename);
            if (exportRowsToFileDialog.openFile()) {
                ArtifactManager.open(file.getAbsolutePath());
            }
        }
    }
}

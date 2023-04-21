package com.ivis.xprocess.ui.dialogs;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import com.ivis.xprocess.ui.dialogs.providers.ColumnLabelProvider;
import com.ivis.xprocess.ui.dialogs.providers.SelectReorderContentProvider;

/**
 * Extends the SelectReorderDialog with the ability to select Tree or Table mode.
 *
 */
public class ColumnSelectionWithViewsDialog extends SelectReorderDialog {

    private int selectedView;

    public ColumnSelectionWithViewsDialog(Shell parent, Control parentControl, String title, String unchosenTitle, String chosenTitle, int view) {
        super(parent, parentControl, title, unchosenTitle, chosenTitle);
        this.selectedView = view;
        allowNoColumns = false;
        unchosenContentProvider = new SelectReorderContentProvider();
        chosenContentProvider = new SelectReorderContentProvider();
        unchosenLabelProvider = new ColumnLabelProvider();
        chosenLabelProvider = new ColumnLabelProvider();
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        Point currentSize = newShell.getSize();
        newShell.setSize(currentSize);
    }

    @Override
    protected void createPreComposite(Composite c) {
    }

    @Override
    protected void setButtonStates() {
        super.setButtonStates();
        if (allowNoColumns) {
            okButton.setEnabled(true);
        } else {
            okButton.setEnabled(((Table) chosenViewer.getControl()).getItemCount() > 0);
        }
    }

    /**
     * @return ViewManager.TABLE_VIEW or ViewManager.TREE_VIEW
     */
    public int getView() {
        return selectedView;
    }

    @Override
    protected void preOkDispose() {
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Control control = super.createDialogArea(parent);
        return control;
    }
}

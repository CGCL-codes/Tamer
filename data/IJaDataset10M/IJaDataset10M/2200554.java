package net.sf.signs.views.netlist;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class SisDialog extends ElementListSelectionDialog {

    public SisDialog(Shell parent_) {
        super(parent_, new LabelProvider() {
        });
        setElements(new String[] { "Optimization", "Technology Mapping", "Print Stats", "Print Map Stats", "Print Delay" });
        setMultipleSelection(true);
    }
}

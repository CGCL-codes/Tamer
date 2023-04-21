package org.liris.schemerger.ui.actions.chronicle;

import java.util.Iterator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.liris.schemerger.chronicle.ChrTimeConstraint;
import org.liris.schemerger.core.event.EDate;
import org.liris.schemerger.core.pattern.ITypeDec;
import org.liris.schemerger.ui.SchEmergerPlugin;
import org.liris.schemerger.ui.dialogs.EditConstraintDialog;
import org.liris.schemerger.ui.editors.ChronicleGraphNode;
import org.liris.schemerger.ui.observablemodel.ObservableChronicle;
import org.liris.schemerger.utils.IndexEntry;

public class AddConstraintAction extends ChronicleAction {

    public AddConstraintAction(ObservableChronicle<?> chronicle) {
        super(chronicle);
        setText("Add Constraint");
        setToolTipText("Add a new time constraint to the chronicle");
        setImageDescriptor(SchEmergerPlugin.getImageDescriptor("icons/add_constraint.gif"));
    }

    private int index1;

    private int index2;

    @Override
    public void run() {
        ITypeDec type1;
        ITypeDec type2;
        if (this.index1 < this.index2) {
            type1 = chronicle.getEpisode().get(this.index1);
            type2 = chronicle.getEpisode().get(this.index2);
        } else {
            type2 = chronicle.getEpisode().get(this.index1);
            type1 = chronicle.getEpisode().get(this.index2);
        }
        EditConstraintDialog dialog = new EditConstraintDialog(SchEmergerPlugin.getDefault().getWorkbench().getDisplay().getActiveShell(), type1, type2);
        int returnCode = dialog.open();
        if (returnCode == Dialog.OK) {
            ChrTimeConstraint ctc = new ChrTimeConstraint(index1, index2, EDate.n(dialog.getLowerBound()), EDate.n(dialog.getUpperBound()));
            chronicle.addConstraint(ctc);
        }
    }

    @Override
    protected boolean shouldEnabled(IStructuredSelection selection) {
        boolean activate = true;
        activate &= (selection.size() == 2);
        if (activate) {
            Iterator<?> it = selection.iterator();
            Object next1 = it.next();
            Object next2 = it.next();
            if (!(next1 instanceof ChronicleGraphNode)) return false;
            if (!(next2 instanceof ChronicleGraphNode)) return false;
            ChronicleGraphNode node1 = (ChronicleGraphNode) next1;
            ChronicleGraphNode node2 = (ChronicleGraphNode) next2;
            this.index1 = node1.getIndex();
            this.index2 = node2.getIndex();
            if (this.index1 < this.index2) {
                IndexEntry ie = new IndexEntry(this.index1, this.index2);
                return !chronicle.getCstIndexEntries().contains(ie);
            } else {
                IndexEntry ie = new IndexEntry(this.index2, this.index1);
                return !chronicle.getCstIndexEntries().contains(ie);
            }
        } else {
            return false;
        }
    }
}

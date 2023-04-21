package org.pubcurator.core.dialogs;

import java.util.List;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.widgets.Shell;
import org.pubcurator.core.ui.ExtendedSelectionDialog;
import org.pubcurator.model.result.PubTermRelation;

/**
 * @author Kai Schlamp (schlamp@gmx.de)
 *
 */
public class RemoveTermRelationsDialog extends ExtendedSelectionDialog {

    public RemoveTermRelationsDialog(Shell parentShell, List<PubTermRelation> termRelations) {
        super(parentShell);
        super.setTitle("Select Term Relations To Remove");
        super.setContentProvider(new ArrayContentProvider());
        super.setLabelProvider(new CellLabelProvider() {

            @Override
            public void update(ViewerCell cell) {
                PubTermRelation termRelation = (PubTermRelation) cell.getElement();
                String categoryName = termRelation.getCategoryName();
                cell.setText(categoryName);
            }

            @Override
            public String getToolTipText(Object element) {
                PubTermRelation termRelation = (PubTermRelation) element;
                String tooltip = "";
                tooltip += "Source Term: " + termRelation.getSource().getCoveredText() + "\n";
                tooltip += "Destination Term: " + termRelation.getDestination().getCoveredText() + "\n";
                tooltip += "Covered Text: " + termRelation.getCoveredText() + "\n";
                tooltip += "Begin: " + termRelation.getBegin() + "\n";
                tooltip += "End: " + termRelation.getEnd();
                return tooltip;
            }
        });
        super.setComparator(new ViewerComparator() {

            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                PubTermRelation r1 = (PubTermRelation) e1;
                PubTermRelation r2 = (PubTermRelation) e2;
                return r1.getCategoryName().compareTo(r2.getCategoryName());
            }
        });
        super.setMultiSelection(true);
        super.setHeight(5);
        super.setInput(termRelations);
    }
}

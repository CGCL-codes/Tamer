package eu.redseeds.sc.merging.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import eu.redseeds.engine.navigator.providers.AdapterFactory;
import eu.redseeds.engine.navigator.providers.IProvider;
import eu.redseeds.sc.merging.MergeConstants;
import eu.redseeds.sc.merging.conflicts.MergeConflictObject;
import eu.redseeds.sc.merging.dialogs.MergeConflictsDialog;

/**
 * Used by {@link MergeConflictsDialog} 
 *
 */
public class MergeConflictViewLabelProvider extends LabelProvider implements ITableLabelProvider {

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        if (element instanceof MergeConflictObject) {
            MergeConflictObject conflict = (MergeConflictObject) element;
            if (columnIndex == 0) {
                IProvider provider = AdapterFactory.adapt(conflict.getCurrentObject(), null);
                return provider.getImage(conflict.getCurrentObject());
            } else if (columnIndex == 1) {
                IProvider provider = AdapterFactory.adapt(conflict.getPastObject(), null);
                return provider.getImage(conflict.getPastObject());
            }
        }
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof MergeConflictObject) {
            MergeConflictObject conflict = (MergeConflictObject) element;
            if (columnIndex == 0) {
                IProvider provider = AdapterFactory.adapt(conflict.getCurrentObject(), null);
                return provider.getText(conflict.getCurrentObject());
            } else if (columnIndex == 1) {
                IProvider provider = AdapterFactory.adapt(conflict.getPastObject(), null);
                return provider.getText(conflict.getPastObject());
            } else if (columnIndex == 2) {
                return conflict.getDescription();
            } else if (columnIndex == 3) {
                return MergeConstants.MERGE_ACTION_LABELS[conflict.getConflictAction() - MergeConstants.MERGE_ACTION_BASE_ID - 1];
            }
        }
        return element.toString();
    }
}

package de.objectcode.time4u.client.ui.adapter;

import java.util.List;
import org.eclipse.core.runtime.IAdapterFactory;
import de.objectcode.time4u.client.ui.util.CompoundSelection;
import de.objectcode.time4u.client.ui.util.CompoundSelectionEntityType;
import de.objectcode.time4u.server.api.data.WorkItem;

public class CompoundWorkItemAdapterFactory implements IAdapterFactory {

    @SuppressWarnings("unchecked")
    public Object getAdapter(final Object adaptableObject, final Class adapterType) {
        if (!(adaptableObject instanceof CompoundSelection)) {
            return null;
        }
        final CompoundSelection selection = (CompoundSelection) adaptableObject;
        final List<Object> selections = (List<Object>) selection.getSelection(CompoundSelectionEntityType.WORKITEM);
        if (selections != null && !selections.isEmpty()) {
            if (WorkItem.class.isAssignableFrom(adapterType)) {
                final Object sel = selections.get(0);
                if (sel instanceof WorkItem) {
                    return sel;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Class[] getAdapterList() {
        return new Class[] { WorkItem.class };
    }
}

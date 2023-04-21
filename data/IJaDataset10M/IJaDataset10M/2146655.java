package gov.sns.xal.tools.widgets;

import gov.sns.xal.smf.NodeChannelRef;
import java.util.List;

/** interface for receiving PVPickerView events */
public interface PVPickerListener {

    /**
	 * Handle the event indicating that items have been selected.
	 * @param view the PVPickerView view where the selection was made
	 * @param items the selected items
	 */
    public void itemsSelected(final PVPickerView view, final List<?> selectedItems);

    /**
	 * Handle the event indicating that channel references have been selected form the specified PVPickerView.
	 * @param view the PVPickerView view where the selection was made
	 * @param channelRefs the node-channel references
	 */
    public void channelReferencesSelected(final PVPickerView view, final List<NodeChannelRef> channelRefs);
}

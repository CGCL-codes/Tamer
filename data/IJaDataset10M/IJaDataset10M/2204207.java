package net.sourceforge.nattable.filterrow.event;

import static net.sourceforge.nattable.util.ObjectUtils.isNotNull;
import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.grid.GridRegion;
import net.sourceforge.nattable.layer.LabelStack;
import net.sourceforge.nattable.ui.NatEventData;
import net.sourceforge.nattable.ui.matcher.IMouseEventMatcher;
import org.eclipse.swt.events.MouseEvent;

public class FilterRowMouseEventMatcher implements IMouseEventMatcher {

    public boolean matches(NatTable natTable, MouseEvent event, LabelStack regionLabels) {
        NatEventData eventData = NatEventData.createInstanceFromEvent(event);
        LabelStack labels = eventData.getRegionLabels();
        if (isNotNull(labels)) {
            return labels.getLabels().contains(GridRegion.FILTER_ROW);
        }
        return false;
    }
}

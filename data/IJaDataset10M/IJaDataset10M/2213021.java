package net.sourceforge.nattable.filterrow.action;

import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.filterrow.command.ClearFilterCommand;
import net.sourceforge.nattable.ui.NatEventData;
import net.sourceforge.nattable.ui.action.IMouseAction;
import org.eclipse.swt.events.MouseEvent;

public class ClearFilterAction implements IMouseAction {

    public void run(NatTable natTable, MouseEvent event) {
        NatEventData natEventData = (NatEventData) event.data;
        natTable.doCommand(new ClearFilterCommand(natTable, natEventData.getColumnPosition()));
    }
}

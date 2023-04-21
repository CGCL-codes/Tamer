package net.sourceforge.nattable.hideshow.command;

import net.sourceforge.nattable.command.AbstractMultiRowCommand;
import net.sourceforge.nattable.layer.ILayer;

public class MultiRowHideCommand extends AbstractMultiRowCommand {

    public MultiRowHideCommand(ILayer layer, int rowPosition) {
        this(layer, new int[] { rowPosition });
    }

    public MultiRowHideCommand(ILayer layer, int[] rowPositions) {
        super(layer, rowPositions);
    }

    protected MultiRowHideCommand(MultiRowHideCommand command) {
        super(command);
    }

    public MultiRowHideCommand cloneCommand() {
        return new MultiRowHideCommand(this);
    }
}

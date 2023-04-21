package net.sourceforge.nattable.group.command;

import net.sourceforge.nattable.command.AbstractRowCommand;
import net.sourceforge.nattable.layer.ILayer;

public class RowGroupExpandCollapseCommand extends AbstractRowCommand {

    public RowGroupExpandCollapseCommand(ILayer layer, int rowPosition) {
        super(layer, rowPosition);
    }

    protected RowGroupExpandCollapseCommand(RowGroupExpandCollapseCommand command) {
        super(command);
    }

    public RowGroupExpandCollapseCommand cloneCommand() {
        return new RowGroupExpandCollapseCommand(this);
    }
}

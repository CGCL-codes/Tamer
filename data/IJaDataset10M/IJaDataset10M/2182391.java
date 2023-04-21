package net.sourceforge.nattable.viewport.command;

import net.sourceforge.nattable.command.AbstractPositionCommand;
import net.sourceforge.nattable.layer.ILayer;

public class ShowCellCommand extends AbstractPositionCommand {

    public ShowCellCommand(ILayer layer, int columnPosition, int rowPosition) {
        super(layer, columnPosition, rowPosition);
    }

    protected ShowCellCommand(ShowCellCommand command) {
        super(command);
    }

    public ShowCellCommand cloneCommand() {
        return new ShowCellCommand(this);
    }
}

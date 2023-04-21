package ch.fusun.baron.basic.client.ui;

import ch.fusun.baron.client.ui.messages.Messages;

/**
 * Moves a unit up
 */
public class MoveUnitUpTileAction extends MoveUnitTileAction {

    @Override
    protected int getVerticalDiff() {
        return 1;
    }

    @Override
    protected int getHorizontalDiff() {
        return 0;
    }

    @Override
    public String getText() {
        return Messages.MoveUnitUpTileAction_Description;
    }
}

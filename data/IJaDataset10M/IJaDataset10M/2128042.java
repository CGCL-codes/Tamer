package mrusanov.fantasyruler.player.message;

import mrusanov.fantasyruler.player.Player;

/**
 * Invisible message should be executed right after sending and then removed
 * from message box.
 * 
 */
@Deprecated
public abstract class InvisibleMessage extends Message {

    public InvisibleMessage(Player sender, Player receiver, MessageAction afterReceiveAction) {
        super(sender, receiver, new MessageAction[] {}, afterReceiveAction);
    }

    @Override
    public boolean canRepeat() {
        return false;
    }
}

package mrusanov.fantasyruler.player.message.friendship;

import mrusanov.fantasyruler.player.Player;
import mrusanov.fantasyruler.player.message.Message;
import mrusanov.fantasyruler.player.message.action.AbstractMessageAction;
import mrusanov.fantasyruler.player.message.info.FriendshipTreatyAcceptedMessage;

public class FriendshipTreatyAcceptedMessageAction extends AbstractMessageAction {

    @Override
    public void executeMessageAction(Message message, int currentTurn) {
        message.getReceiver().getDiplomacy().acceptFriendshipTreaty(message.getSender(), currentTurn);
        Player.sendMessage(new FriendshipTreatyAcceptedMessage(message.getReceiver(), message.getSender()), currentTurn);
    }
}

package mrusanov.fantasyruler.player.message.alliance;

import mrusanov.fantasyruler.player.Player;
import mrusanov.fantasyruler.player.message.Message;
import mrusanov.fantasyruler.player.message.MessageAction;

/**
 * This message is used when player asks alliance leader to enter alliance
 * 
 */
public class AskLeaderToEnterAllianceMessage extends Message {

    public static final int ANSWER_YES = 1;

    public static final int ANSWER_NO = 0;

    public AskLeaderToEnterAllianceMessage(Player sender, Player receiver) {
        super(sender, receiver, new MessageAction[] { new FailAskLeaderToEnterAllianceMessageAction(), new SuccAskLeaderToEnterAllianceMessageAction() });
    }

    @Override
    public boolean isValid() {
        if (!getReceiver().getDiplomacy().isAllianceLeader() || !getSender().getDiplomacy().isAllianceLeader()) {
            return false;
        }
        return getSender().getDiplomacy().hasPossibilityToJoinAlliance(getReceiver());
    }

    @Override
    public boolean canRepeat() {
        return false;
    }
}

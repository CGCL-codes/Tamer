package mrusanov.fantasyruler.player.message.alliance;

import mrusanov.fantasyruler.player.Player;
import mrusanov.fantasyruler.player.message.Message;
import mrusanov.fantasyruler.player.message.action.AbstractMessageAction;
import mrusanov.fantasyruler.player.message.info.RemovedFromAllianceMessage;

public class ThrowOutFromAllianceMessageAction extends AbstractMessageAction {

    @Override
    public void executeMessageAction(Message message, int currentTurn) {
        message.getReceiver().getDiplomacy().removeIndependentPlayerFromAlliance(currentTurn, AllianceLeavingReason.THROWEN_AWAY);
        for (Player removed : message.getReceiver().getDiplomacy().getAllianceGroup()) {
            for (Player inAlliance : message.getSender().getDiplomacy().getAllianceGroup()) {
                Player.sendMessage(new RemovedFromAllianceMessage(removed, inAlliance), currentTurn);
            }
        }
    }
}

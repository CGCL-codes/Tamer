package mrusanov.fantasyruler.player.message.peace;

import mrusanov.fantasyruler.player.Player;
import mrusanov.fantasyruler.player.diplomacy.Diplomacy;
import mrusanov.fantasyruler.player.message.MessageAction;
import mrusanov.fantasyruler.player.message.MessageTransferObject;
import com.google.common.collect.Lists;

public class ProposePeaceMessage extends AbstractProposePeaceMessage {

    public static final int ANSWER_YES = 1;

    public static final int ANSWER_NO = 0;

    public ProposePeaceMessage(Player sender, Player receiver) {
        this(sender, receiver, new PeaceProposal(Lists.newArrayList(sender), Lists.newArrayList(receiver)));
    }

    public ProposePeaceMessage(Player sender, Player receiver, PeaceProposal peaceProposal) {
        super(sender, receiver, new MessageAction[] { new PeaceTreatyRejectedMessageAction(), new PeaceTreatyAcceptedMessageAction(peaceProposal) });
    }

    @Override
    public boolean isValid() {
        boolean superValue = super.isValid();
        if (!superValue) {
            return false;
        }
        return getReceiver().getTurnsWithoutControllingAnyCity() < Diplomacy.TURNS_WITHOUT_CITY_CONTROL_TO_SEND_ULTIMATIVE_MESSAGES;
    }

    @Override
    public MessageTransferObject getMessageTransferObject() {
        return new ProposePeaceMessageTransferObject(this);
    }
}

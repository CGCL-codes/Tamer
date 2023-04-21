package mrusanov.fantasyruler.player.message.peace;

import mrusanov.fantasyruler.game.GameContainer;
import mrusanov.fantasyruler.player.message.Message;

public class UltimativeProposePeaceMessageTransferObject extends AbstractProposePeaceMessageTransferObject {

    private static final long serialVersionUID = -2434574191361022188L;

    public UltimativeProposePeaceMessageTransferObject(UltimativeProposePeaceMessage message) {
        super(message);
    }

    @Override
    public Message getMessage(GameContainer container) {
        return new UltimativeProposePeaceMessage(container.getPlayerByName(sender), container.getPlayerByName(receiver), getPeaceProposal().buildProposal(container));
    }
}

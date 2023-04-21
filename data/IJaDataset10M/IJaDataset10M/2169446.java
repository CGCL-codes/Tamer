package mrusanov.fantasyruler.events.dto;

import com.google.common.base.Preconditions;
import mrusanov.fantasyruler.events.AbstractGameEvent;
import mrusanov.fantasyruler.events.ExecuteMessageGameEvent;
import mrusanov.fantasyruler.game.GameContainer;
import mrusanov.fantasyruler.player.Player;
import mrusanov.fantasyruler.player.message.Message;

public class ExecuteMessageTransferObject extends GameEventTransferObject {

    private static final long serialVersionUID = -1017993327077350884L;

    private final String playerName;

    private final int messageNumber;

    private final int answerNumber;

    public ExecuteMessageTransferObject(Player player, Message message, int answerNumber) {
        super();
        playerName = player.getName();
        messageNumber = player.getMessageBox().getMessageNumber(message);
        Preconditions.checkArgument(messageNumber != -1, "Message not found!");
        this.answerNumber = answerNumber;
    }

    @Override
    public AbstractGameEvent buildGameEvent(GameContainer gameContainer) {
        Player player = gameContainer.getPlayerByName(playerName);
        Message message = player.getMessageBox().get(messageNumber);
        return new ExecuteMessageGameEvent(false, message, answerNumber);
    }
}

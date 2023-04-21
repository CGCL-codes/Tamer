package net.sf.doolin.app.sc.common.turn;

import net.sf.doolin.app.sc.common.model.Player;
import net.sf.doolin.app.sc.common.model.PlayerType;
import net.sf.doolin.app.sc.common.model.SCGame;
import net.sf.doolin.app.sc.turn.Turn;

public class StartPlayerTurnExecutor extends PerPlayerTurnExecutor {

    @Override
    protected void playTurn(Turn turn, Player player, SCGame instance) {
        if (player.getType() == PlayerType.HUMAN) {
            int year = instance.getYear();
            GameHistoryItem item = new GameHistoryItem(player.getLocale(), year, GameHistoryType.TURN, year);
            player.getHistory().add(item);
        }
    }
}

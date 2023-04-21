package org.mathig.poker.action;

import org.mathig.poker.PlayerListener;
import org.mathig.poker.PlayerWrapper;
import org.mathig.poker.table.TableHand;

public class BetAction extends PlayerAction {

    private int amount;

    public BetAction(PlayerWrapper player, TableHand tableHand, int amount) {
        super(player, tableHand);
        this.amount = amount;
    }

    public void execute() {
        amount = BetHelper.checkBet(tableHand, player, amount);
        BetHelper.addToBet(tableHand, player, amount);
        logger.info("{} bet {}", player, tableHand.getBet(player));
        for (PlayerListener listener : tableHand.getPlayerListeners()) {
            if (listener != player) listener.playerBet(player.getPlayer(), tableHand);
        }
    }
}

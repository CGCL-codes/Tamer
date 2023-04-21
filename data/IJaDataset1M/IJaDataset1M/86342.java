package edu.bsu.monopoly.trade;

import edu.bsu.monopoly.game.actions.Command;

public interface TradeType extends Command {

    public abstract boolean equals(TradeType t);

    public void setTradeId(int tradeId);
}

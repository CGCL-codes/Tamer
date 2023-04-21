package jelb.trade;

import jelb.TradeBot;
import jelb.common.Actor;
import jelb.common.IItem;
import jelb.common.UninitializedItemException;
import jelb.netio.Message;
import jelb.struct.Account;
import jelb.trade.State.States;

public class GetEvent extends Event {

    /**
	 * @param tradePartner
	 */
    public GetEvent(TradeBot bot, Actor tradePartner) {
        super(bot, tradePartner);
    }

    @Override
    public State balanceTrade() {
        if (this.hasUninitializedItems(this.mineTradeContainer) || this.hasUninitializedItems(this.yoursTradeContainer)) {
            return new State(States.Balancing);
        }
        Account a = this.bot.data.getAccounts().get(this.tradePartner.getName());
        try {
            if (a != null && a.contains(this.memory) && this.memoryEquals(this.mineTradeContainer)) {
                return new State(States.Balanced);
            } else {
                return new State(States.CriticalException);
            }
        } catch (UninitializedItemException uie) {
            return new State(States.Balancing);
        }
    }

    @Override
    public Message commit() {
        Account a = this.bot.data.getAccounts().get(this.tradePartner.getName());
        if (a == null) return null;
        for (IItem item : this.mineTradeContainer) {
            a.get(item);
        }
        for (IItem item : this.yoursTradeContainer) {
            this.bot.data.getGuild().doPayBackOrPut(item, a);
        }
        this.bot.data.save(this.bot.data.getAccounts());
        return null;
    }
}

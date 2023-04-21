package jelb.trade;

import java.util.ArrayList;
import jelb.TradeBot;
import jelb.common.Actor;
import jelb.common.ILogger;
import jelb.common.Item;
import jelb.common.UninitializedItemException;
import jelb.netio.Message;
import jelb.struct.Account;
import jelb.struct.BuyOrdersListItem;
import jelb.struct.GoldCoins;
import jelb.struct.OrdersListItem;
import jelb.struct.PriceListItem;
import jelb.trade.State.States;

public class BuyEvent extends Event {

    public BuyEvent(TradeBot bot, Actor partner) {
        super(bot, partner);
    }

    @Override
    public State balanceTrade() {
        if (this.isNotReadyForCalcualtion()) {
            return State.Balancing;
        }
        long yourCont = 0;
        try {
            yourCont = this.getYourTradeContainerValue();
        } catch (UninitializedItemException uie) {
            return State.Balancing;
        }
        long alreadyPut = this.getGc(this.mineTradeContainer);
        long diff = alreadyPut - yourCont;
        for (Item item : this.yoursTradeContainer) {
            if (!item.isInitialized()) {
                return State.Balancing;
            }
            if (GoldCoins.isGoldCoins(item)) {
                return new State(States.CriticalException, jelb.Locale.MSG_BUYING_ONLY_ITEMS);
            } else if (this.yoursTradeContainer.getQuantity(item.getName()) > this.bot.data.getOrders().leftToBuy(item.getName())) {
                int leftToBuy = this.bot.getOrders().leftToBuy(item.getName());
                BuyOrdersListItem boItem = this.bot.data.getHistory().getLastBuyOrder(item.getName());
                if (boItem != null) {
                    String msg = String.format(jelb.Locale.MSG_NOT_BUYING_SO_MANY_BUT_SOMEONE_BUY, item.getName(), boItem.player);
                    return new State(States.CriticalException, msg);
                }
                if (leftToBuy > 0) return new State(States.CriticalException, String.format(jelb.Locale.MSG_NOT_BUYING_SUCH_AMOUNT, leftToBuy, item.getName())); else return new State(States.CriticalException, String.format(jelb.Locale.MSG_NOT_BUYING, item.getName()));
            }
        }
        if (diff == 0) {
            if (!this.mineTradeContainer.isEmpty() && !this.yoursTradeContainer.isEmpty()) return new State(States.Balanced, jelb.Locale.MSG_TRADE_CALC_DONE);
            return State.Balancing;
        }
        if (diff > 0) {
            if (this.isNotReadyForCalcualtion() || alreadyPut == 0) return new State(States.Balancing);
            if (diff > alreadyPut) diff = alreadyPut;
            this.removeFromTrade(new GoldCoins(diff));
            return new State(States.Balancing);
        } else {
            if (this.isNotReadyForCalcualtion()) return new State(States.Balancing);
            Item gci = this.bot.getInventory().get(GoldCoins.Sname);
            if (gci == null || gci.getQuantity() < -diff) {
                return new State(States.CriticalException, jelb.Locale.MSG_NO_MORE_MONEY);
            }
            this.putOnTrade(new GoldCoins(-diff));
            return new State(States.Balancing);
        }
    }

    public long getYourTradeContainerValue() throws UninitializedItemException {
        return this.getValueOf(this.yoursTradeContainer, PriceListItem.BUY_PRICE);
    }

    public Message commit() {
        String info = "BGHT ";
        for (Item itm : this.yoursTradeContainer) {
            ArrayList<OrdersListItem> changes = this.bot.data.getOrders().buy(itm.getName(), itm.getQuantity());
            for (OrdersListItem item : changes) {
                Account a = this.bot.data.getAccounts().get(item.player);
                if (a != null) {
                    this.bot.data.getGuild().doPayBackOrPut(item, a);
                    jelb.struct.Message msg = new jelb.struct.Message();
                    msg.to = a.owner.name;
                    msg.from = this.bot.data.getConfig().getPlayerName();
                    msg.body = String.format(jelb.Locale.MSG_SOMEONE_SOLD_ME, tradePartner.getName(), item.getQuantity(), item.getName());
                    this.bot.data.getMessages().syncAdd(msg);
                } else {
                    this.bot.log(ILogger.LogLevel.Error, "BuyEvent.commit() - account '" + item.player + "' missing");
                }
            }
            if (!GoldCoins.isGoldCoins(itm)) {
                info += itm.getQuantityUint32().toStringValue() + " " + itm.getName() + ", ";
            }
        }
        info = info.substring(0, info.length() - 2);
        this.bot.data.save(this.bot.data.getOrders());
        this.bot.data.save(this.bot.data.getAccounts());
        this.bot.log(ILogger.LogLevel.Out, info);
        return this.bot.preaparePm(this.tradePartner.getName(), jelb.Locale.MSG_THANKS_FOR_TRADE);
    }
}

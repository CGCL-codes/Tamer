package net.sourceforge.jasa.agent;

import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.event.AgentArrivalEvent;
import net.sourceforge.jasa.event.MarketEvent;
import net.sourceforge.jasa.market.Market;

/**
 * <p>
 * Agents of this type have a fixed volume, and they trade units equal to their
 * volume in each round of the market.
 * </p>
 * 
 * @author Steve Phelps
 * @version $Revision: 1.10 $
 */
public class SimpleTradingAgent extends AbstractTradingAgent {

    public SimpleTradingAgent(int stock, double funds, double privateValue, EventScheduler scheduler) {
        super(stock, funds, privateValue, scheduler);
    }

    public SimpleTradingAgent(int stock, double funds, double privateValue, TradingStrategy strategy, EventScheduler scheduler) {
        super(stock, funds, privateValue, strategy, scheduler);
    }

    public SimpleTradingAgent(int stock, double funds, EventScheduler scheduler) {
        super(stock, funds, scheduler);
    }

    public SimpleTradingAgent(double privateValue, boolean isSeller, TradingStrategy strategy, EventScheduler scheduler) {
        super(0, 0, privateValue, strategy, scheduler);
    }

    public SimpleTradingAgent(double privateValue, EventScheduler scheduler) {
        super(0, 0, privateValue, scheduler);
    }

    public SimpleTradingAgent(EventScheduler scheduler) {
        this(0, scheduler);
    }

    public SimpleTradingAgent() {
        this(null);
    }

    public void onAgentArrival(Market auction, AgentArrivalEvent event) {
        super.onAgentArrival(auction, event);
        lastPayoff = 0;
    }

    public boolean acceptDeal(Market auction, double price, int quantity) {
        return price >= valuer.determineValue(auction);
    }

    public double getLastPayoff() {
        return lastPayoff;
    }

    public boolean active() {
        return true;
    }

    public void onEndOfDay(MarketEvent event) {
    }

    public String toString() {
        return "(" + getClass() + " id:" + hashCode() + " valuer:" + valuer + +totalPayoff + " lastProfit:" + lastPayoff + " strategy:" + strategy + ")";
    }

    @Override
    public double calculateProfit(Market auction, int quantity, double price) {
        if (currentOrder == null) {
            return 0;
        }
        return super.calculateProfit(auction, quantity, price);
    }
}

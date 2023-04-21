package net.sourceforge.jasa.agent.strategy;

import java.io.Serializable;
import net.sourceforge.jabm.util.Prototypeable;
import net.sourceforge.jasa.agent.AbstractTradingAgent;
import net.sourceforge.jasa.market.Market;
import net.sourceforge.jasa.market.Order;

/**
 * <p>
 * A trading strategy in which we bid a constant mark-up on the agent's private
 * value.
 * </p>
 * 
 * <b>Parameters</b><br>
 * 
 * <table>
 * 
 * <tr>
 * <td valign=top><i>base.</i><tt>delta</tt><br>
 * <font size=-1>double</font></td>
 * <td valign=top>(the markup over our private valuation to bid for)</td>
 * </tr>
 * 
 * </table>
 * 
 * @author Steve Phelps
 * @version $Revision: 1.6 $
 */
public class PureSimpleStrategy extends FixedDirectionStrategy implements Serializable, Prototypeable {

    protected double margin;

    static final String P_DELTA = "delta";

    static final double DEFAULT_DELTA = 0;

    public PureSimpleStrategy(AbstractTradingAgent agent, double margin, int quantity) {
        super(agent);
        this.margin = margin;
        this.quantity = quantity;
    }

    public PureSimpleStrategy() {
        super(null);
        margin = DEFAULT_DELTA;
    }

    public Object protoClone() {
        Object clonedStrategy;
        try {
            clonedStrategy = this.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
        return clonedStrategy;
    }

    public boolean modifyShout(Order shout) {
        double delta;
        if (isSell()) {
            delta = margin;
        } else {
            delta = -margin;
        }
        shout.setPrice(getAgent().getValuation(auction) + delta);
        shout.setQuantity(quantity);
        if (shout.getPrice() < 0) {
            shout.setPrice(0);
        }
        return super.modifyShout(shout);
    }

    public void onRoundClosed(Market auction) {
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }
}

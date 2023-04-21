package dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree;

import dk.mirasola.systemtraining.bridgewidgets.shared.model.Distribution;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.Hand;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.Seat;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.Suit;
import java.util.HashSet;
import java.util.Set;

public final class HcpSuitDistributionFilter extends DistributionFilterGroupItem {

    private HcpInterval hcpInterval = new HcpInterval();

    private Seat seat = Seat.NORTH;

    private Set<Suit> suits = new HashSet<Suit>();

    /**
     * Gets seat
     *
     * @return
     */
    public Seat getSeat() {
        return seat;
    }

    /**
     * Sets seat
     *
     * @param seat
     */
    public void setSeat(Seat seat) {
        if (seat == null) {
            throw new NullPointerException();
        }
        this.seat = seat;
    }

    /**
     * Gets set of suits
     */
    public Set<Suit> getSuits() {
        return suits;
    }

    /**
     * Sets set of suits
     */
    public void setSuits(Set<Suit> suits) {
        if (suits == null) {
            throw new NullPointerException();
        }
        this.suits = suits;
    }

    public HcpInterval getHcpInterval() {
        return hcpInterval;
    }

    public void setHcpInterval(HcpInterval hcpInterval) {
        if (hcpInterval == null) {
            throw new NullPointerException();
        }
        this.hcpInterval = hcpInterval;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("hcp(").append(seat).append(",");
        boolean first = true;
        for (Suit suit : getSuits()) {
            if (first) {
                first = false;
            } else {
                builder.append(":");
            }
            builder.append(suit);
        }
        builder.append(")");
        if (hcpInterval.getMinHcp() != hcpInterval.getMaxHcp()) {
            builder.append(" between ").append(hcpInterval.getMinHcp()).append(" and ").append(hcpInterval.getMaxHcp());
        } else {
            builder.append("=").append(hcpInterval.getMinHcp());
        }
        return builder.toString();
    }

    protected boolean matchesInternal(Distribution distribution) {
        Hand hand = distribution.getHand(seat);
        int hcp = 0;
        for (Suit suit : suits) {
            hcp += hand.getHcp(suit);
        }
        return hcpInterval.contains(hcp);
    }

    @Override
    public boolean isCopy(GroupItem groupItem) {
        if (!super.isCopy(groupItem)) return false;
        if (!(groupItem instanceof HcpSuitDistributionFilter)) return false;
        HcpSuitDistributionFilter that = (HcpSuitDistributionFilter) groupItem;
        return seat == that.seat && hcpInterval.equals(that.hcpInterval) && suits.equals(that.suits);
    }

    @Override
    public HcpSuitDistributionFilter copy() {
        HcpSuitDistributionFilter copy = new HcpSuitDistributionFilter();
        copy.setInverted(this.getInverted());
        copy.suits.addAll(this.suits);
        copy.seat = this.seat;
        copy.hcpInterval = this.hcpInterval.copy();
        return copy;
    }
}

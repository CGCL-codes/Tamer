package tribler.overlay.peer.peerSelection.rankingComparators;

import java.util.Comparator;
import protopeer.Finger;

/**
 * Compares two ForwardingRanks to each other according to Give-to-Get metrics
 * (first direct forwarding, then overall forwarding)
 */
public class ForwardingRankComparator implements Comparator<Finger> {

    private G2GRater g2gRater;

    public ForwardingRankComparator(G2GRater g2grater) {
        this.g2gRater = g2grater;
    }

    @Override
    public int compare(Finger o1, Finger o2) {
        ForwardingRank b1 = g2gRater.getRank(o1);
        ForwardingRank b2 = g2gRater.getRank(o2);
        if (b1.getDirectForwarding() > b2.getDirectForwarding()) {
            return 1;
        } else if (b1.getDirectForwarding() < b2.getDirectForwarding()) {
            return -1;
        } else {
            if (b1.getOverallForwarding() > b2.getOverallForwarding()) {
                return 1;
            } else if (b1.getOverallForwarding() < b2.getOverallForwarding()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}

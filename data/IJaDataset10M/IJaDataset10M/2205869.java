package ao.bucket.index.detail;

import ao.bucket.index.canon.flop.Flop;
import ao.bucket.index.canon.hole.CanonHole;
import ao.bucket.index.canon.turn.Turn;
import ao.bucket.index.detail.flop.FlopDetailFlyweight.CanonFlopDetail;
import ao.bucket.index.detail.turn.TurnDetailFlyweight.CanonTurnDetail;
import org.apache.log4j.Logger;

/**
 * Date: Jan 21, 2009
 * Time: 11:54:48 AM
 */
public class TestCanonDetails {

    private TestCanonDetails() {
    }

    private static final Logger LOG = Logger.getLogger(TestCanonDetails.class);

    public static void main(String[] args) {
        testTurnDetails();
    }

    public static void testHoleDetails() {
        for (char canonHole = 0; canonHole < CanonHole.CANONS; canonHole++) {
            LOG.info(DetailLookup.lookupHole(canonHole));
        }
    }

    public static void testFlopDetails() {
        long turnCountTotal = 0;
        long turnCountMax = Long.MIN_VALUE;
        long turnCountMin = Long.MAX_VALUE;
        for (int canonFlop = 0; canonFlop < Flop.CANONS; canonFlop++) {
            CanonFlopDetail details = DetailLookup.lookupFlop(canonFlop);
            turnCountTotal += details.canonTurnCount();
            turnCountMax = Math.max(turnCountMax, details.canonTurnCount());
            turnCountMin = Math.min(turnCountMin, details.canonTurnCount());
        }
        LOG.info("turnCountTotal = " + turnCountTotal);
        LOG.info("turnCountMax   = " + turnCountMax);
        LOG.info("turnCountMin   = " + turnCountMin);
    }

    public static void testTurnDetails() {
        double totalStrength = 0;
        for (int canonTurn = 0; canonTurn < Turn.CANONS; canonTurn++) {
            CanonTurnDetail details = DetailLookup.lookupTurn(canonTurn);
            totalStrength += details.strength();
        }
        LOG.info("totalStrength/n = " + (totalStrength / Turn.CANONS));
    }
}

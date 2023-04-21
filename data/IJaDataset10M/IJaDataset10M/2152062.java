package bouttime.boutmaker.bracket16;

import bouttime.boutmaker.bracket4.Bracket4BoutMaker;
import bouttime.model.Bout;
import bouttime.model.Group;
import bouttime.model.Wrestler;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * A utility class to make bouts for a 16-man bracket.
 */
public class Bracket16BoutMaker {

    static Logger logger = Logger.getLogger(Bracket16BoutMaker.class);

    protected Bout r1b1;

    protected Bout r1b2;

    protected Bout r1b3;

    protected Bout r1b4;

    protected Bout r1b5;

    protected Bout r1b6;

    protected Bout r1b7;

    protected Bout r1b8;

    protected Bout r2b1;

    protected Bout r2b2;

    protected Bout r2b3;

    protected Bout r2b4;

    protected Bout r2b5;

    protected Bout r2b6;

    protected Bout r2b7;

    protected Bout r2b8;

    protected Bout r3b1;

    protected Bout r3b2;

    protected Bout r3b3;

    protected Bout r3b4;

    protected Bout r4b1;

    protected Bout r4b2;

    protected Bout r4b3;

    protected Bout r4b4;

    protected Bout r5b1;

    protected Bout r5b2;

    protected Bout r6b1;

    protected Bout r6b2;

    protected Bout r6b3;

    protected Bout r7b1;

    protected Wrestler w1;

    protected Wrestler w2;

    protected Wrestler w3;

    protected Wrestler w4;

    protected Wrestler w5;

    protected Wrestler w6;

    protected Wrestler w7;

    protected Wrestler w8;

    protected Wrestler w9;

    protected Wrestler w10;

    protected Wrestler w11;

    protected Wrestler w12;

    protected Wrestler w13;

    protected Wrestler w14;

    protected Wrestler w15;

    protected Wrestler w16;

    public void makeBouts(Group g, Boolean fifthPlaceEnabled, Boolean secondPlaceChallengeEnabled, Wrestler dummy) {
        logger.trace("Making bouts for bracket group of size " + g.getNumWrestlers());
        getWrestlerSeedValues(g);
        ArrayList<Bout> bList = new ArrayList<Bout>();
        int rounds = 6;
        bList.addAll(makeRound1Bouts(g, dummy));
        bList.addAll(makeRound2Bouts(g, dummy));
        bList.addAll(makeRound3Bouts(g, dummy));
        bList.addAll(makeRound4Bouts(g, dummy));
        bList.addAll(makeRound5Bouts(g, dummy));
        bList.addAll(makeRound6Bouts(g, dummy, fifthPlaceEnabled));
        if (secondPlaceChallengeEnabled && (g.getWrestlers().size() > 2)) {
            bList.addAll(makeRound7Bouts(g, dummy));
            rounds = 7;
        } else {
            this.r7b1 = null;
        }
        linkBouts();
        g.setBouts(bList);
        g.setNumRounds(rounds);
        g.setBracketType(Group.BRACKET_TYPE_16MAN_DOUBLE);
        logger.trace("Total bouts is [" + g.getNumBouts() + "]");
        return;
    }

    protected void getWrestlerSeedValues(Group g) {
        this.w1 = g.getWrestlerAtSeed(1);
        this.w2 = g.getWrestlerAtSeed(2);
        this.w3 = g.getWrestlerAtSeed(3);
        this.w4 = g.getWrestlerAtSeed(4);
        this.w5 = g.getWrestlerAtSeed(5);
        this.w6 = g.getWrestlerAtSeed(6);
        this.w7 = g.getWrestlerAtSeed(7);
        this.w8 = g.getWrestlerAtSeed(8);
        this.w9 = g.getWrestlerAtSeed(9);
        this.w10 = g.getWrestlerAtSeed(10);
        this.w11 = g.getWrestlerAtSeed(11);
        this.w12 = g.getWrestlerAtSeed(12);
        this.w13 = g.getWrestlerAtSeed(13);
        this.w14 = g.getWrestlerAtSeed(14);
        this.w15 = g.getWrestlerAtSeed(15);
        this.w16 = g.getWrestlerAtSeed(16);
    }

    /**
     * Create bouts for round 1 for the given group.
     * @param g Group to make bouts for.
     * @param d A dummy wrestler to use when creating the bouts.
     * @return A list of bouts for this round.
     */
    protected List<Bout> makeRound1Bouts(Group g, Wrestler d) {
        int byeCount = 0;
        boolean isBye;
        List<Bout> bList = new ArrayList<Bout>();
        isBye = ((this.w1 == null) || (this.w16 == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r1b1 = new Bout(this.w1, this.w16, g, Bout.ROUND_1, 1, "", isBye, false);
        bList.add(this.r1b1);
        isBye = ((this.w9 == null) || (this.w8 == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r1b2 = new Bout(this.w9, this.w8, g, Bout.ROUND_1, 2, "", isBye, false);
        bList.add(this.r1b2);
        isBye = ((this.w5 == null) || (this.w12 == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r1b3 = new Bout(this.w5, this.w12, g, Bout.ROUND_1, 3, "", isBye, false);
        bList.add(this.r1b3);
        isBye = ((this.w13 == null) || (this.w4 == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r1b4 = new Bout(this.w13, this.w4, g, Bout.ROUND_1, 4, "", isBye, false);
        bList.add(this.r1b4);
        isBye = ((this.w3 == null) || (this.w14 == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r1b5 = new Bout(this.w3, this.w14, g, Bout.ROUND_1, 5, "", isBye, false);
        bList.add(this.r1b5);
        isBye = ((this.w11 == null) || (this.w6 == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r1b6 = new Bout(this.w11, this.w6, g, Bout.ROUND_1, 6, "", isBye, false);
        bList.add(this.r1b6);
        isBye = ((this.w7 == null) || (this.w10 == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r1b7 = new Bout(this.w7, this.w10, g, Bout.ROUND_1, 7, "", isBye, false);
        bList.add(this.r1b7);
        isBye = ((this.w15 == null) || (this.w2 == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r1b8 = new Bout(this.w15, this.w2, g, Bout.ROUND_1, 8, "", isBye, false);
        bList.add(this.r1b8);
        logger.debug("Round 1 byes = " + byeCount);
        return bList;
    }

    /**
     * Create bouts for round 2 for the given group.
     * @param g Group to make bouts for.
     * @param d A dummy wrestler to use when creating the bouts.
     * @return A list of bouts for this round.
     */
    protected List<Bout> makeRound2Bouts(Group g, Wrestler d) {
        int byeCount = 0;
        List<Bout> bList = new ArrayList<Bout>();
        Wrestler red;
        Wrestler green;
        boolean isBye;
        red = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r1b1, d);
        green = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r1b2, d);
        isBye = ((red == null) || (green == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r2b1 = new Bout(red, green, g, Bout.ROUND_2, 1, "D", isBye, false);
        bList.add(this.r2b1);
        red = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r1b3, d);
        green = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r1b4, d);
        isBye = ((red == null) || (green == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r2b2 = new Bout(red, green, g, Bout.ROUND_2, 2, "E", isBye, false);
        bList.add(this.r2b2);
        red = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r1b5, d);
        green = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r1b6, d);
        isBye = ((red == null) || (green == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r2b3 = new Bout(red, green, g, Bout.ROUND_2, 3, "F", isBye, false);
        bList.add(this.r2b3);
        red = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r1b7, d);
        green = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r1b8, d);
        isBye = ((red == null) || (green == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r2b4 = new Bout(red, green, g, Bout.ROUND_2, 4, "G", isBye, false);
        bList.add(this.r2b4);
        isBye = (this.r1b1.isBye() || this.r1b2.isBye()) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r2b5 = new Bout(d, d, g, Bout.ROUND_2, 5, "", isBye, true);
        bList.add(this.r2b5);
        isBye = (this.r1b3.isBye() || this.r1b4.isBye()) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r2b6 = new Bout(d, d, g, Bout.ROUND_2, 6, "", isBye, true);
        bList.add(this.r2b6);
        isBye = (this.r1b5.isBye() || this.r1b6.isBye()) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r2b7 = new Bout(d, d, g, Bout.ROUND_2, 7, "", isBye, true);
        bList.add(this.r2b7);
        isBye = (this.r1b7.isBye() || this.r1b8.isBye()) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r2b8 = new Bout(d, d, g, Bout.ROUND_2, 8, "", isBye, true);
        bList.add(this.r2b8);
        logger.debug("Round 2 byes = " + byeCount);
        return bList;
    }

    /**
     * Create bouts for round 3 for the given group.
     * @param g Group to make bouts for.
     * @param d A dummy wrestler to use when creating the bouts.
     * @return A list of bouts for this round.
     */
    protected List<Bout> makeRound3Bouts(Group g, Wrestler d) {
        int byeCount = 0;
        List<Bout> bList = new ArrayList<Bout>();
        boolean isBye;
        isBye = ((r1b1.isBye() && r1b2.isBye()) || r2b4.isBye()) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r3b1 = new Bout(d, d, g, Bout.ROUND_3, 1, "", isBye, true);
        bList.add(this.r3b1);
        isBye = ((r1b3.isBye() && r1b4.isBye()) || r2b3.isBye()) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r3b2 = new Bout(d, d, g, Bout.ROUND_3, 2, "", isBye, true);
        bList.add(this.r3b2);
        isBye = ((r1b5.isBye() && r1b6.isBye()) || r2b2.isBye()) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r3b3 = new Bout(d, d, g, Bout.ROUND_3, 3, "", isBye, true);
        bList.add(this.r3b3);
        isBye = ((r1b7.isBye() && r1b8.isBye()) || r2b1.isBye()) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r3b4 = new Bout(d, d, g, Bout.ROUND_3, 4, "", isBye, true);
        bList.add(this.r3b4);
        logger.debug("Round 3 byes = " + byeCount);
        return bList;
    }

    /**
     * Create bouts for round 4 for the given group.
     * @param g Group to make bouts for.
     * @param d A dummy wrestler to use when creating the bouts.
     * @return A list of bouts for this round.
     */
    protected List<Bout> makeRound4Bouts(Group g, Wrestler d) {
        int byeCount = 0;
        List<Bout> bList = new ArrayList<Bout>();
        Wrestler red;
        Wrestler green;
        boolean isBye;
        red = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r2b1, d);
        green = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r2b2, d);
        isBye = ((red == null) || (green == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r4b1 = new Bout(red, green, g, Bout.ROUND_4, 1, "B", isBye, false);
        bList.add(this.r4b1);
        red = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r2b3, d);
        green = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r2b4, d);
        isBye = ((red == null) || (green == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r4b2 = new Bout(red, green, g, Bout.ROUND_4, 2, "C", isBye, false);
        bList.add(this.r4b2);
        isBye = ((r1b1.isBye() && r1b2.isBye() && r2b4.isBye()) || (r1b3.isBye() && r1b4.isBye() && r2b3.isBye())) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r4b3 = new Bout(d, d, g, Bout.ROUND_4, 3, "", isBye, true);
        bList.add(this.r4b3);
        isBye = ((r1b5.isBye() && r1b6.isBye() && r2b2.isBye()) || (r1b7.isBye() && r1b8.isBye() && r2b1.isBye())) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r4b4 = new Bout(d, d, g, Bout.ROUND_4, 4, "", isBye, true);
        bList.add(this.r4b4);
        logger.debug("Round 4 byes = " + byeCount);
        return bList;
    }

    /**
     * Create bouts for round 5 for the given group.
     * @param g Group to make bouts for.
     * @param d A dummy wrestler to use when creating the bouts.
     * @return A list of bouts for this round.
     */
    protected List<Bout> makeRound5Bouts(Group g, Wrestler d) {
        int byeCount = 0;
        List<Bout> bList = new ArrayList<Bout>();
        boolean isBye;
        isBye = (r4b1.isBye() || (r1b1.isBye() && r1b2.isBye() && r1b3.isBye() && r1b4.isBye() && r2b3.isBye() && r2b4.isBye())) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r5b1 = new Bout(d, d, g, Bout.ROUND_5, 1, "X", isBye, true);
        bList.add(this.r5b1);
        isBye = (r4b2.isBye() || (r1b5.isBye() && r1b6.isBye() && r1b7.isBye() && r1b8.isBye() && r2b1.isBye() && r2b2.isBye())) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r5b2 = new Bout(d, d, g, Bout.ROUND_5, 2, "Y", isBye, true);
        bList.add(this.r5b2);
        logger.debug("Round 5 byes = " + byeCount);
        return bList;
    }

    /**
     * Create bouts for round 6 for the given group.
     * @param g Group to make bouts for.
     * @param d A dummy wrestler to use when creating the bouts.
     * @return A list of bouts for this round.
     */
    protected List<Bout> makeRound6Bouts(Group g, Wrestler d, boolean doFifthPlace) {
        int byeCount = 0;
        List<Bout> bList = new ArrayList<Bout>();
        Wrestler red;
        Wrestler green;
        boolean isBye;
        int numWrestlers = g.getNumWrestlers();
        red = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r4b1, d);
        green = Bracket4BoutMaker.getAutoAdvancingWrestler(this.r4b2, d);
        isBye = ((red == null) || (green == null)) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r6b1 = new Bout(red, green, g, Bout.ROUND_6, 1, "A", isBye, false);
        bList.add(this.r6b1);
        isBye = (numWrestlers < 4) ? true : false;
        byeCount += (isBye) ? 1 : 0;
        this.r6b2 = new Bout(d, d, g, Bout.ROUND_6, 2, "Z", isBye, true);
        bList.add(this.r6b2);
        if (doFifthPlace && (numWrestlers > 5)) {
            isBye = (this.r5b1.isBye() || this.r5b2.isBye()) ? true : false;
            byeCount += (isBye) ? 1 : 0;
            this.r6b3 = new Bout(red, green, g, Bout.ROUND_6, 3, "", isBye, true);
            bList.add(this.r6b3);
        } else {
            this.r6b3 = null;
        }
        logger.debug("Round 6 byes = " + byeCount);
        return bList;
    }

    /**
     * Create bouts for round 7 for the given group.
     * @param g Group to make bouts for.
     * @param d A dummy wrestler to use when creating the bouts.
     * @return A list of bouts for this round.
     */
    protected List<Bout> makeRound7Bouts(Group g, Wrestler d) {
        List<Bout> bList = new ArrayList<Bout>();
        this.r7b1 = new Bout(d, d, g, Bout.ROUND_7, 1, "", false, true);
        bList.add(this.r7b1);
        return bList;
    }

    /**
     * Link the bouts together so the winners and losers will advance.
     */
    protected void linkBouts() {
        this.r1b1.setWinnerNextBout(this.r2b1);
        this.r1b1.setLoserNextBout(this.r2b5);
        this.r1b2.setWinnerNextBout(this.r2b1);
        this.r1b2.setLoserNextBout(this.r2b5);
        this.r1b3.setWinnerNextBout(this.r2b2);
        this.r1b3.setLoserNextBout(this.r2b6);
        this.r1b4.setWinnerNextBout(this.r2b2);
        this.r1b4.setLoserNextBout(this.r2b6);
        this.r1b5.setWinnerNextBout(this.r2b3);
        this.r1b5.setLoserNextBout(this.r2b7);
        this.r1b6.setWinnerNextBout(this.r2b3);
        this.r1b6.setLoserNextBout(this.r2b7);
        this.r1b7.setWinnerNextBout(this.r2b4);
        this.r1b7.setLoserNextBout(this.r2b8);
        this.r1b8.setWinnerNextBout(this.r2b4);
        this.r1b8.setLoserNextBout(this.r2b8);
        this.r2b1.setWinnerNextBout(this.r4b1);
        this.r2b1.setLoserNextBout(this.r3b4);
        this.r2b2.setWinnerNextBout(this.r4b1);
        this.r2b2.setLoserNextBout(this.r3b3);
        this.r2b3.setWinnerNextBout(this.r4b2);
        this.r2b3.setLoserNextBout(this.r3b2);
        this.r2b4.setWinnerNextBout(this.r4b2);
        this.r2b4.setLoserNextBout(this.r3b1);
        this.r2b5.setWinnerNextBout(this.r3b1);
        this.r2b5.setLoserNextBout(null);
        this.r2b6.setWinnerNextBout(this.r3b2);
        this.r2b6.setLoserNextBout(null);
        this.r2b7.setWinnerNextBout(this.r3b3);
        this.r2b7.setLoserNextBout(null);
        this.r2b8.setWinnerNextBout(this.r3b4);
        this.r2b8.setLoserNextBout(null);
        this.r3b1.setWinnerNextBout(this.r4b3);
        this.r3b1.setLoserNextBout(null);
        this.r3b2.setWinnerNextBout(this.r4b3);
        this.r3b2.setLoserNextBout(null);
        this.r3b3.setWinnerNextBout(this.r4b4);
        this.r3b3.setLoserNextBout(null);
        this.r3b4.setWinnerNextBout(this.r4b4);
        this.r3b4.setLoserNextBout(null);
        this.r4b1.setWinnerNextBout(this.r6b1);
        this.r4b1.setLoserNextBout(this.r5b1);
        this.r4b2.setWinnerNextBout(this.r6b1);
        this.r4b2.setLoserNextBout(this.r5b2);
        this.r4b3.setWinnerNextBout(this.r5b1);
        this.r4b3.setLoserNextBout(null);
        this.r4b4.setWinnerNextBout(this.r5b2);
        this.r4b4.setLoserNextBout(null);
        this.r5b1.setWinnerNextBout(this.r6b2);
        this.r5b1.setLoserNextBout(this.r6b3);
        this.r5b2.setWinnerNextBout(this.r6b2);
        this.r5b2.setLoserNextBout(this.r6b3);
        this.r6b1.setWinnerNextBout(null);
        this.r6b1.setLoserNextBout(this.r7b1);
        this.r6b2.setWinnerNextBout(this.r7b1);
        this.r6b2.setLoserNextBout(null);
    }
}

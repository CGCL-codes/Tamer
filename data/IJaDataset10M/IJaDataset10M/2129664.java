package net.sf.nebulacards.main;

/**
 * Some routines that might be useful to those programming computer players.
 * All methods are static, so you do not have to instantiate this class.
 *
 * @author James Ranson
 */
public final class Think {

    /**
     * Determine which cards in the given hand can be played.
     *
     * @param h The hand to be checked.
     * @param t The cards that are on the table so far.
     * @param bp All the cards that have been played during this hand.
     * @param rules A rule set whose validPlay method is to be used.
     * @return The cards that are legal plays.
     */
    public static PileOfCards validPlays(PileOfCards h, PileOfCards t, PileOfCards bp, Rules rules) {
        if (t == null) t = new PileOfCards();
        PileOfCards vp = new PileOfCards();
        for (int i = 0; i < h.howManyCards(); i++) if (t.search(h.getCard(i)) == h.NOT_FOUND && rules.validPlay(bp, t, h, h.getCard(i))) vp.addToTop(h.getCard(i));
        return vp;
    }

    /**
     * Determine who currently has the highest card on the table.
     *
	 * @deprecated Use Rules.whoWinsTrick(Tableau)
     * @param t The cards on the table.
     * @return Table position of the person who has the highest card
     */
    public static int aheadNow(Tableau t, Rules rules) {
        return rules.whoWinsTrick(t);
    }

    /**
     * Determine what card(s) can be played to take the lead in this trick.
     *
     * @param vp Possible plays (These <b>must</b> be valid otherwise the result could be erroneous.
     * @param t The trick as it stands now.
     * @param rules A rule set.
     * @return All cards in vp that would be the highest in the trick.
     */
    public static PileOfCards takeLead(PileOfCards vp, PileOfCards t, Rules rules) {
        PileOfCards results = new PileOfCards();
        for (int i = 0; i < vp.howManyCards(); i++) {
            Tableau tableau = new Tableau();
            tableau.addToTop(t);
            tableau.addToTop(vp.getCard(i));
            if (aheadNow(tableau, rules) == (tableau.howManyCards() - 1)) results.addToTop(vp.getCard(i));
        }
        return results;
    }

    /**
     * Find the card with the highest pip count.
     *
     * @return The highest card.
     * @param h The cards to examine.
     * @param aceHigh True if aces are higher than kings.
     */
    public static PlayingCard highCard(PileOfCards h, boolean aceHigh) {
        PlayingCard high = h.getCard(0);
        for (int i = 1; i < h.howManyCards(); i++) if (h.getCard(i).higherThan(high, aceHigh)) high = h.getCard(i);
        return high;
    }

    /**
     * Find the card with the lowest pip count.
     *
     * @return The lowest card.
     * @param h The cards to examine.
     * @param aceHigh True if aces are higher than kings.
     */
    public static PlayingCard lowCard(PileOfCards h, boolean aceHigh) {
        PlayingCard low = h.getCard(0);
        for (int i = 1; i < h.howManyCards(); i++) if (low.higherThan(h.getCard(i), aceHigh)) low = h.getCard(i);
        return low;
    }

    /**
     * Calculate the probability that someone can take the lead in a trick.
     *
     * @param tableau The cards that are in the trick so far.
     * @param h The cards that might be in the player's hand.
     * @param bp All the cards that have been played so far
     * @param rules A rule set.
     * @return A probability from 0 (impossible) to 1 (certain).
     */
    public static float canBeat(PileOfCards h, PileOfCards tableau, PileOfCards bp, Rules rules) {
        if (tableau == null || tableau.howManyCards() == 0) return (float) 1.0;
        if (h == null || h.howManyCards() == 0) return (float) 0.0;
        PileOfCards tl = takeLead(h, tableau, rules);
        if (tl.howManyCards() == 0) return 0;
        PileOfCards vp = validPlays(h, tableau, bp, rules);
        float f = 0;
        for (int i = 0; i < tl.howManyCards(); i++) {
            PlayingCard c = tl.getCard(i);
            if (vp.search(c) == vp.NOT_FOUND) f += 2.0 / vp.howManyCards(); else f += 1.0;
        }
        float p = f / (float) vp.howManyCards();
        p = (float) (p > 1.0 ? 1.0 : p);
        return p;
    }
}

package jskat.player.AIPlayerMJL;

import org.apache.log4j.Logger;
import jskat.share.CardVector;
import jskat.share.Card;
import jskat.share.SkatConstants;

/**
 * @author Markus J. Luzius <markus@luzius.de>
 *
 */
public class SkatProcessor {

    private static final Logger log = Logger.getLogger(SkatProcessor.class);

    /**
     * Processes the skat after the player has won the bidding.
     * @param cards the player's hand
     * @param skat the skat
     * @return suit to be played (0-3), grand (4), null (-1)
     */
    public static int processSkat(CardVector cards, CardVector skat) {
        log.debug("My cards:" + cards + ", Skat=" + skat);
        int cDiamonds = cards.getSuitColorCount(SkatConstants.DIAMONDS);
        int cHearts = cards.getSuitColorCount(SkatConstants.HEARTS);
        int cSpades = cards.getSuitColorCount(SkatConstants.SPADES);
        int cClubs = cards.getSuitColorCount(SkatConstants.CLUBS);
        cDiamonds += skat.getSuitColorCount(SkatConstants.DIAMONDS);
        cHearts += skat.getSuitColorCount(SkatConstants.HEARTS);
        cSpades += skat.getSuitColorCount(SkatConstants.SPADES);
        cClubs += skat.getSuitColorCount(SkatConstants.CLUBS);
        log.debug("C=" + cClubs + ", S=" + cSpades + ", H=" + cHearts + ", D=" + cDiamonds);
        int diamonds = Helper.suitCardsToBinaryWithSkat(cards, skat, SkatConstants.DIAMONDS);
        int hearts = Helper.suitCardsToBinaryWithSkat(cards, skat, SkatConstants.HEARTS);
        int spades = Helper.suitCardsToBinaryWithSkat(cards, skat, SkatConstants.SPADES);
        int clubs = Helper.suitCardsToBinaryWithSkat(cards, skat, SkatConstants.CLUBS);
        log.debug("C=" + clubs + ", S=" + spades + ", H=" + hearts + ", D=" + diamonds);
        int possibleSkatSuits = 0;
        if (cDiamonds > 0 && cDiamonds < 3 && (diamonds & 64) == 0) possibleSkatSuits += 1;
        if (cHearts > 0 && cHearts < 3 && (hearts & 64) == 0) possibleSkatSuits += 2;
        if (cSpades > 0 && cSpades < 3 && (spades & 64) == 0) possibleSkatSuits += 4;
        if (cClubs > 0 && cClubs < 3 && (clubs & 64) == 0) possibleSkatSuits += 8;
        Card skatOne = skat.remove(0);
        Card skatTwo = skat.remove(0);
        if (possibleSkatSuits > 0) {
            int skatSuit = Helper.binaryToSuit(possibleSkatSuits);
            if (skatSuit < 0) {
                skat.add(skatOne);
                skat.add(skatTwo);
            } else {
                log.debug("Color for skat:" + skatSuit);
                if (skatOne.getSuit() == skatSuit || cards.getSuitColorCount(skatSuit) < 1) {
                    skat.add(skatOne);
                } else {
                    skat.add(cards.remove(cards.getFirstIndexOfSuit(skatSuit)));
                    cards.add(skatOne);
                }
                if (skatTwo.getSuit() == skatSuit || cards.getSuitColorCount(skatSuit) < 1) {
                    skat.add(skatTwo);
                } else {
                    skat.add(cards.remove(cards.getFirstIndexOfSuit(skatSuit)));
                    cards.add(skatTwo);
                }
            }
        } else {
            skat.add(skatOne);
            skat.add(skatTwo);
        }
        log.debug("Done - my cards:" + cards + ", Skat=" + skat);
        return cards.getMostFrequentSuitColor();
    }

    /**
     * Decides, whether the AI player should look at the skat or rather play hand
     * @param cards the player's hand
     * @return true, if the player should look at the skat
     */
    public static boolean lookAtSkat(CardVector cards) {
        return true;
    }
}

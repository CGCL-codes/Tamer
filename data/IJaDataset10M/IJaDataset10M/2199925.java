package net.sf.nebulacards.rules;

import net.sf.nebulacards.main.BiddingRules;
import net.sf.nebulacards.main.GameResult;
import net.sf.nebulacards.main.PileOfCards;
import net.sf.nebulacards.main.Player;
import net.sf.nebulacards.main.PlayingCard;
import net.sf.nebulacards.main.Rules;
import net.sf.nebulacards.main.Tableau;

public class Spades extends Rules implements BiddingRules {

    private int bidCount;

    private int handCount;

    public Spades() {
        super("Spades", PlayingCard.SPADES, "Spades");
        bidCount = 0;
        handCount = 0;
    }

    public void setTrump(PlayingCard c) {
    }

    public boolean shouldBid(int pos) {
        if (bidCount == 0 && pos != handCount % 4) return false;
        return (bidCount < 4);
    }

    public void doneBidding() {
        bidCount = 0;
    }

    public void reportBid(int pos, int bid) {
        bidCount++;
    }

    public boolean validPlay(PileOfCards beenPlayed, Tableau tableau, PileOfCards hand, PlayingCard c) {
        if (hand.allThisSuit(c.getSuit())) return true;
        boolean weLead = (tableau.howManyCardsNotNull() == 0);
        int suitLed = -1;
        if (!weLead) suitLed = tableau.get(tableau.getLead()).getSuit();
        if (!weLead && c.getSuit() != suitLed && hand.anyOfThisSuit(suitLed)) return false;
        if (weLead && (c.getSuit() == PlayingCard.SPADES) && !beenPlayed.anyOfThisSuit(PlayingCard.SPADES)) return false;
        return true;
    }

    public void score(PileOfCards[] hands, Player[] p) {
        int i;
        int[] tricks = new int[4], scoreDiffs = { 0, 0 };
        for (i = 0; i < 4; i++) tricks[i] = hands[i].howManyCardsNotNull() / 4;
        for (i = 0; i < 4; i++) {
            if (p[i].getBid() == 0) {
                if (tricks[i] == 0) scoreDiffs[i % 2] += 100; else scoreDiffs[i % 2] -= 100;
            }
        }
        for (i = 0; i < 2; i++) {
            int bidDiff = tricks[i] + tricks[i + 2] - p[i].getBid() - p[i + 2].getBid();
            if (bidDiff < 0) scoreDiffs[i] -= (p[i].getBid() + p[i + 2].getBid()) * 10; else {
                scoreDiffs[i] += (p[i].getBid() + p[i + 2].getBid()) * 10;
                scoreDiffs[i] += bidDiff;
                p[i].setBags(p[i].getBags() + bidDiff);
                p[i + 2].setBags(p[i + 2].getBags() + bidDiff);
            }
            while (p[i].getBags() >= 10) {
                scoreDiffs[i] -= 100;
                p[i].setBags(p[i].getBags() - 10);
                p[i + 2].setBags(p[i + 2].getBags() - 10);
            }
            p[i].setScore(p[i].getScore() + scoreDiffs[i]);
            p[i + 2].setScore(p[i + 2].getScore() + scoreDiffs[i]);
        }
    }

    public GameResult done(Player[] p) {
        handCount++;
        GameResult gr = new GameResult();
        if (p[0].getScore() >= 500 || p[1].getScore() >= 500) gr.done = true; else gr.done = false;
        if (p[0].getScore() == p[1].getScore()) gr.done = false;
        gr.winners = new int[2];
        if (p[0].getScore() > p[1].getScore()) {
            gr.winners[0] = 0;
            gr.winners[1] = 2;
        } else {
            gr.winners[0] = 1;
            gr.winners[1] = 3;
        }
        return gr;
    }
}

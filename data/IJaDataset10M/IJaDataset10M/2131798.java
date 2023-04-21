package Poker;

import java.util.Random;

public class FiveCardDraw extends SimplePoker {

    public void playHand(Random rand) {
        beforeHand();
        dealCards(rand);
        bidding();
        if (numPlayersIn > 1) {
            drawCards();
            bidding();
        }
        resolveWinner();
        removeBankrupt();
    }

    void drawCards() {
        for (int p = 0; p < data.size(); p++) {
            PlayerData pd = data.get((p + firstBidder) % data.size());
            if (pd.folded) continue;
            Card[] cards = pd.player.draw(getPlayerStats());
            if (cards != null) {
                int n = 0;
                for (Card c : cards) {
                    if (c == null) continue;
                    if (!pd.replaceCard(c, deck.dealOne())) throw new IllegalArgumentException(pd + " tried to discard a card that isn't in their hand!");
                    n++;
                }
                System.out.println(pd + " draws " + n);
            }
            Card[] copy = pd.copyHand(5);
            pd.player.deal(copy);
        }
    }
}

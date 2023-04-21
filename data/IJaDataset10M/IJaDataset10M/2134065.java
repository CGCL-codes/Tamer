package baspackage;

import java.util.Vector;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Vector cards = new Vector();
        GameCard card1 = new GameCard(12);
        GameCard card2 = new GameCard(25);
        GameCard card3 = new GameCard(38);
        GameCard card4 = new GameCard(1);
        GameCard card5 = new GameCard(13);
        GameCard card6 = new GameCard(26);
        GameCard card7 = new GameCard(39);
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);
        cards.add(card6);
        cards.add(card7);
        int bassie[];
        bassie = RealFlushCheck.FlushCheck(cards);
        if (bassie[0] == 0) {
            bassie = RealCheckStraight.CheckStraight(cards);
            if (bassie[0] == 0) {
                bassie = Paircheck.paircheck(cards);
            }
        }
        for (int i = 0; i < bassie.length; i++) {
            System.out.print(bassie[i] + "\n");
        }
    }
}

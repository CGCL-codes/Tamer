package ua.in.say.cgfw.base;

import ua.in.say.cgfw.contract.Card;
import ua.in.say.cgfw.contract.CardComparsion;
import ua.in.say.cgfw.contract.RankComparsion;
import ua.in.say.cgfw.contract.SuitComparsion;

/**
 * @author <a href="mailto:Yuriy.Lazarev@gmail.com">Yuriy.Lazarev@gmail.com</a>
 */
public abstract class CardComparsionBase implements CardComparsion {

    protected RankComparsion rankComparsion;

    protected SuitComparsion suitComparsion;

    public CardComparsionBase(RankComparsion rankComparsion, SuitComparsion suitComparsion) {
        this.rankComparsion = rankComparsion;
        this.suitComparsion = suitComparsion;
    }

    public byte compare(Card card1, Card card2) {
        if (isRankMatters()) {
            if (isSuitMatters()) {
                return compareRankAndSuit(card1, card2);
            } else {
                return rankComparsion.compare(card1.rank(), card2.rank());
            }
        } else {
            if (isSuitMatters()) {
                return suitComparsion.compare(card1.suit(), card2.suit());
            } else {
                throw new IllegalStateException("Either Suit or Rank must matters!");
            }
        }
    }

    protected byte compareRankAndSuit(Card card1, Card card2) {
        throw new IllegalStateException("Should never rich here [card1=" + card1 + ";card2=" + card2 + "]");
    }

    public boolean gt(Card card1, Card card2) {
        return compare(card1, card2) > 0;
    }

    public boolean lt(Card card1, Card card2) {
        return compare(card1, card2) < 0;
    }

    public boolean eq(Card card1, Card card2) {
        return compare(card1, card2) == 0;
    }

    public boolean gte(Card card1, Card card2) {
        return gt(card1, card2) || eq(card1, card2);
    }

    public boolean lte(Card card1, Card card2) {
        return lt(card1, card2) || eq(card1, card2);
    }

    /**
     * Is rank matters?
     *
     * @return true if rank has value
     */
    public boolean isRankMatters() {
        return true;
    }

    /**
     * Is suit matters?
     *
     * @return true if suit has value
     */
    public boolean isSuitMatters() {
        return true;
    }
}

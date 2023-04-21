package com.shudes.eval;

import java.util.*;
import com.shudes.game.*;
import com.shudes.util.*;

public class HandEvaluator {

    protected static final Map<Rank, Set<Rank>> STRAIGHT_MAP = initStraightMap();

    protected Map<Suit, Integer> suitCount;

    protected Map<Rank, Integer> rankCount;

    protected Hand hand;

    protected HandType handType;

    protected Integer strength;

    public HandEvaluator(Hand hand) {
        this.hand = hand;
        this.calcSuiteAndRankCount();
        this.handType = this.inferHandType();
        this.strength = this.calcStrength();
    }

    public Hand getHand() {
        return this.hand;
    }

    public HandType getHandType() {
        return this.handType;
    }

    public Integer getStrength() {
        return this.strength;
    }

    public HandStrength getHandStrength() {
        return new HandStrength(getHand(), getHandType(), getStrength());
    }

    protected void calcSuiteAndRankCount() {
        suitCount = new HashMap<Suit, Integer>();
        rankCount = new HashMap<Rank, Integer>();
        Integer count;
        for (Card c : hand.getCards()) {
            count = suitCount.get(c.getSuit());
            if (count == null) count = new Integer(0);
            suitCount.put(c.getSuit(), ++count);
            count = rankCount.get(c.getRank());
            if (count == null) count = new Integer(0);
            rankCount.put(c.getRank(), ++count);
        }
    }

    private static Map<Rank, Set<Rank>> initStraightMap() {
        Map<Rank, Set<Rank>> map;
        map = new HashMap<Rank, Set<Rank>>();
        map.put(Rank.ACE, quickSet(Rank.KING, Rank.QUEEN, Rank.JACK, Rank.TEN));
        map.put(Rank.KING, quickSet(Rank.QUEEN, Rank.JACK, Rank.TEN, Rank.NINE));
        map.put(Rank.QUEEN, quickSet(Rank.JACK, Rank.TEN, Rank.NINE, Rank.EIGHT));
        map.put(Rank.JACK, quickSet(Rank.TEN, Rank.NINE, Rank.EIGHT, Rank.SEVEN));
        map.put(Rank.TEN, quickSet(Rank.NINE, Rank.EIGHT, Rank.SEVEN, Rank.SIX));
        map.put(Rank.NINE, quickSet(Rank.EIGHT, Rank.SEVEN, Rank.SIX, Rank.FIVE));
        map.put(Rank.EIGHT, quickSet(Rank.SEVEN, Rank.SIX, Rank.FIVE, Rank.FOUR));
        map.put(Rank.SEVEN, quickSet(Rank.SIX, Rank.FIVE, Rank.FOUR, Rank.THREE));
        map.put(Rank.SIX, quickSet(Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO));
        map.put(Rank.FIVE, quickSet(Rank.FOUR, Rank.THREE, Rank.TWO, Rank.ACE));
        return map;
    }

    private static Set<Rank> quickSet(Rank r1, Rank r2, Rank r3, Rank r4) {
        HashSet<Rank> s;
        s = new HashSet<Rank>();
        s.add(r1);
        s.add(r2);
        s.add(r3);
        s.add(r4);
        return s;
    }

    protected boolean isStraight(Collection<Card> cards) {
        return calcStraightStrength(cards) != null;
    }

    @SuppressWarnings("unchecked")
    protected Integer calcStraightStrength(Collection<Card> cards) {
        Set<Rank> mapSet;
        Set<Rank> uniqueRanks;
        ArrayList<Rank> ordered;
        uniqueRanks = new HashSet<Rank>();
        for (Card c : cards) {
            uniqueRanks.add(c.getRank());
        }
        if (uniqueRanks.size() < 5) return null;
        ordered = new ArrayList<Rank>(uniqueRanks);
        Collections.sort(ordered);
        Collections.reverse(ordered);
        for (Rank r : ordered) {
            mapSet = STRAIGHT_MAP.get(r);
            if (mapSet != null && uniqueRanks.containsAll(mapSet)) {
                return r.getId().intValue();
            }
        }
        return null;
    }

    protected HandType inferHandType() {
        int numPairs = 0;
        boolean flush = false;
        boolean trips = false;
        boolean quads = false;
        boolean straight = false;
        straight = this.isStraight(hand.getCards());
        for (Suit s : suitCount.keySet()) {
            Integer i = suitCount.get(s);
            if (i != null && i >= 5) {
                flush = true;
            }
        }
        for (Rank r : rankCount.keySet()) {
            Integer i = rankCount.get(r);
            if (i == null) continue;
            if (i == 4) quads = true;
            if (i == 3) trips = true;
            if (i == 2) numPairs++;
        }
        if (flush && straight && isStraightFlush()) return HandType.STRAIGHT_FLUSH;
        if (quads) return HandType.QUADS;
        if (trips && numPairs > 0) return HandType.FULL_HOUSE;
        if (flush) return HandType.FLUSH;
        if (straight) return HandType.STRAIGHT;
        if (trips) return HandType.THREE_OF_A_KIND;
        if (numPairs > 1) return HandType.TWO_PAIR;
        if (numPairs == 1) return HandType.PAIR;
        return HandType.HIGH_CARD;
    }

    protected boolean isStraightFlush() {
        ArrayList<Card> cards;
        Suit suit = null;
        cards = new ArrayList<Card>();
        for (Suit s : suitCount.keySet()) {
            if (suitCount.get(s) != null && suitCount.get(s) >= 5) suit = s;
        }
        if (suit != null) {
            for (Card c : hand.getCards()) {
                if (suit.equals(c.getSuit())) {
                    cards.add(c);
                }
            }
            return isStraight(cards);
        }
        return false;
    }

    protected Integer calcHighCardStrength() {
        ArrayList rankList;
        rankList = new ArrayList<Rank>(new TreeSet<Rank>(rankCount.keySet()));
        trimToFive(rankList);
        Collections.reverse(rankList);
        return calcOrderedStrength(rankList);
    }

    @SuppressWarnings("unchecked")
    protected Integer calcPairStrength() {
        ArrayList<Card> highCards;
        ArrayList<Card> pairCards;
        highCards = new ArrayList<Card>(2);
        pairCards = new ArrayList<Card>(5);
        for (Card c : hand.getCards()) {
            if (rankCount.get(c.getRank()) == 2) pairCards.add(c); else highCards.add(c);
        }
        Collections.sort(highCards);
        highCards.addAll(pairCards);
        Collections.reverse(highCards);
        return calcOrderedStrength(highCards.subList(0, 5));
    }

    @SuppressWarnings("unchecked")
    protected Integer calcTwoPairStrength() {
        List<Card> pairs;
        List<Card> high;
        high = new ArrayList<Card>(hand.getCards());
        pairs = new ArrayList<Card>();
        for (Card c : hand.getCards()) {
            if (rankCount.get(c.getRank()) == 2) {
                pairs.add(c);
            }
        }
        Collections.sort(pairs);
        Collections.reverse(pairs);
        pairs = pairs.subList(0, 4);
        high.removeAll(pairs);
        Collections.sort(high);
        Collections.reverse(high);
        pairs.add(high.get(0));
        return calcOrderedStrength(pairs);
    }

    @SuppressWarnings("unchecked")
    protected Integer calcTripsStrength() {
        List<Card> trips;
        ArrayList<Card> high;
        trips = new ArrayList<Card>();
        high = new ArrayList<Card>(hand.getCards());
        for (Card c : hand.getCards()) {
            if (rankCount.get(c.getRank()) == 3) {
                trips.add(c);
            }
        }
        Collections.sort(trips);
        trips = trips.subList(0, 3);
        high.removeAll(trips);
        Collections.sort(high);
        Collections.reverse(high);
        trips.addAll(high.subList(0, 2));
        return calcOrderedStrength(trips);
    }

    @SuppressWarnings("unchecked")
    protected Integer calcFlushStrength() {
        Suit suit = null;
        List<Card> flush;
        for (Suit s : suitCount.keySet()) {
            if (suitCount.get(s) >= 5) {
                suit = s;
            }
        }
        flush = new ArrayList<Card>();
        for (Card c : hand.getCards()) {
            if (suit.equals(c.getSuit())) {
                flush.add(c);
            }
        }
        Collections.sort(flush);
        Collections.reverse(flush);
        return calcOrderedStrength(flush.subList(0, 5));
    }

    protected Integer calcFullHouseStrength() {
        List<Card> trips;
        Card pair = null;
        trips = new ArrayList<Card>();
        for (Card c : hand.getCards()) {
            if (rankCount.get(c.getRank()) == 3) {
                trips.add(c);
            }
            if (rankCount.get(c.getRank()) == 2 && ((pair == null) || c.getRank().compareTo(pair.getRank()) > 0)) {
                pair = c;
            }
        }
        trips.add(pair);
        trips.add(pair);
        return calcOrderedStrength(trips);
    }

    protected Integer calcQuadsStrength() {
        List<Card> quads;
        Card high = null;
        quads = new ArrayList<Card>();
        for (Card c : hand.getCards()) {
            if (rankCount.get(c.getRank()) == 4) {
                quads.add(c);
            }
            if (rankCount.get(c.getRank()) == 1 && ((high == null) || c.getRank().compareTo(high.getRank()) > 0)) {
                high = c;
            }
        }
        quads.add(high);
        return calcOrderedStrength(quads);
    }

    protected Integer calcStraightFlushStrength() {
        List<Card> cards;
        cards = new ArrayList<Card>();
        for (Card c : hand.getCards()) {
            if (suitCount.get(c.getSuit()) >= 5) cards.add(c);
        }
        return calcStraightStrength(cards);
    }

    protected void trimToFive(ArrayList list) {
        int size = list.size() - 5;
        for (int i = 0; i < size; i++) {
            list.remove(0);
        }
    }

    protected Integer calcStrength() {
        if (HandType.HIGH_CARD.equals(handType)) {
            return calcHighCardStrength();
        } else if (HandType.PAIR.equals(handType)) {
            return calcPairStrength();
        } else if (HandType.TWO_PAIR.equals(handType)) {
            return calcTwoPairStrength();
        } else if (HandType.THREE_OF_A_KIND.equals(handType)) {
            return calcTripsStrength();
        } else if (HandType.STRAIGHT.equals(handType)) {
            return calcStraightStrength(hand.getCards());
        } else if (HandType.FLUSH.equals(handType)) {
            return calcFlushStrength();
        } else if (HandType.FULL_HOUSE.equals(handType)) {
            return calcFullHouseStrength();
        } else if (HandType.QUADS.equals(handType)) {
            return calcQuadsStrength();
        } else if (HandType.STRAIGHT_FLUSH.equals(handType)) {
            return calcStraightFlushStrength();
        }
        return null;
    }

    protected Integer calcOrderedStrength(Collection cards) {
        Rank r;
        int size;
        int count;
        int pow;
        int strength;
        size = cards.size();
        count = 0;
        strength = 0;
        for (Object o : cards) {
            if (o instanceof Card) {
                r = ((Card) o).getRank();
            } else {
                r = (Rank) o;
            }
            ++count;
            pow = size - count;
            strength += (r.getId() << (pow * 4));
        }
        return strength;
    }

    @Override
    public String toString() {
        return "handEvaluator={hand=" + hand + ",type=" + handType + ",strength=" + strength + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof HandEvaluator)) return false;
        return ((HandEvaluator) o).getHand().equals(this.getHand());
    }
}

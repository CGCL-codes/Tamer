package dk.mirasola.systemtraining.bridgewidgets.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Distribution implements IsSerializable {

    public static final Seat[] SEATS = new Seat[] { Seat.NORTH, Seat.EAST, Seat.SOUTH, Seat.WEST };

    private static class ShuffleCard implements Comparable<ShuffleCard> {

        double random = Math.random();

        Card card;

        private ShuffleCard(Card card) {
            this.card = card;
        }

        @Override
        public int compareTo(ShuffleCard shuffleCard) {
            if (random >= shuffleCard.random) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public static Distribution shuffledDistribution() {
        ShuffleCard[] sc = new ShuffleCard[52];
        for (int i = 0; i < Card.values().length; i++) {
            sc[i] = new ShuffleCard(Card.values()[i]);
        }
        Arrays.sort(sc);
        Set<Card> north = new HashSet<Card>(13);
        for (int i = 0; i < 13; i++) {
            north.add(sc[i].card);
        }
        Set<Card> east = new HashSet<Card>(13);
        for (int i = 13; i < 26; i++) {
            east.add(sc[i].card);
        }
        Set<Card> south = new HashSet<Card>(13);
        for (int i = 26; i < 39; i++) {
            south.add(sc[i].card);
        }
        Set<Card> west = new HashSet<Card>(13);
        for (int i = 39; i < 52; i++) {
            west.add(sc[i].card);
        }
        Distribution distribution = new Distribution();
        distribution.seatHand.put(Seat.NORTH, Hand.create(north, false));
        distribution.seatHand.put(Seat.EAST, Hand.create(east, false));
        distribution.seatHand.put(Seat.SOUTH, Hand.create(south, false));
        distribution.seatHand.put(Seat.WEST, Hand.create(west, false));
        return distribution;
    }

    private Map<Seat, Hand> seatHand = new HashMap<Seat, Hand>();

    public Distribution() {
    }

    public Hand getHand(Seat seat) {
        return seatHand.get(seat);
    }

    public void setHand(Seat seat, Hand hand) {
        if (hand == null) {
            seatHand.remove(seat);
        } else {
            seatHand.put(seat, hand);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Distribution)) return false;
        Distribution that = (Distribution) o;
        if (!seatHand.equals(that.seatHand)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return seatHand.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Seat seat : SEATS) {
            sb.append(seat.name().substring(0, 1));
            sb.append(":");
            Hand hand = seatHand.get(seat);
            if (hand != null) {
                sb.append(hand);
            }
            if (seat != Seat.WEST) {
                sb.append(" | ");
            }
        }
        return sb.toString();
    }
}

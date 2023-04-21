package ua.in.say.cgfw.impl;

import ua.in.say.cgfw.contract.comparator.RankComparator;
import ua.in.say.cgfw.enums.Rank;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RankComparatorImpl implements RankComparator {

    private static List<Rank> weight = new ArrayList<Rank>();

    static {
        weight.add(Rank.DEUCE);
        weight.add(Rank.THREE);
        weight.add(Rank.FOUR);
        weight.add(Rank.FIVE);
        weight.add(Rank.SIX);
        weight.add(Rank.SEVEN);
        weight.add(Rank.EIGHT);
        weight.add(Rank.NINE);
        weight.add(Rank.TEN);
        weight.add(Rank.JACK);
        weight.add(Rank.QUEEN);
        weight.add(Rank.KING);
        weight.add(Rank.ACE);
    }

    public int compare(Rank rank1, Rank rank2) {
        if (rank1 == null) {
            throw new IllegalArgumentException("rank1 is null");
        }
        if (rank2 == null) {
            throw new IllegalArgumentException("rank2 is null");
        }
        if (rank1.equals(rank2)) {
            return 0;
        } else {
            int weight1 = weight.indexOf(rank1);
            int weight2 = weight.indexOf(rank2);
            if (weight1 > weight2) {
                return +1;
            } else {
                return -1;
            }
        }
    }

    public boolean gt(Rank rank1, Rank rank2) {
        return compare(rank1, rank2) == 1;
    }

    public boolean gte(Rank rank1, Rank rank2) {
        return gt(rank1, rank2) || eq(rank1, rank2);
    }

    public boolean lt(Rank rank1, Rank rank2) {
        return compare(rank1, rank2) == -1;
    }

    public boolean lte(Rank rank1, Rank rank2) {
        return lt(rank1, rank2) || eq(rank1, rank2);
    }

    public boolean eq(Rank rank1, Rank rank2) {
        return compare(rank1, rank2) == 0;
    }

    /**
     * Returns an iterator over a set of Ranks
     *
     * @return an Iterator.
     */
    public Iterator<Rank> iterator() {
        return weight.iterator();
    }
}

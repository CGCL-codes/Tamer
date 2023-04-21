package poker;

public class Scorer {

    public static long ScoreLowHand(final Card[] cards, final Combination combo) {
        Card a = cards[combo.current()[0]];
        Card b = cards[combo.current()[1]];
        long score = a.value | b.value;
        if (a.value == b.value) score |= (1 << 13);
        return score;
    }

    public static long ScoreHighHand(final Card[] cards, final Combination combo) {
        int[] suits = { 0, 0, 0, 0 };
        for (int i = 0; i < 7; ++i) {
            if (i != combo.current()[0] && i != combo.current()[1]) suits[cards[i].suit] |= cards[i].value;
        }
        int groups = suits[0] | suits[1] | suits[2] | suits[3];
        int numGroups = Integer.bitCount(groups);
        switch(numGroups) {
            case 5:
                {
                    boolean is_flush = (Integer.bitCount(suits[0]) == 5) || (Integer.bitCount(suits[1]) == 5) || (Integer.bitCount(suits[2]) == 5) || (Integer.bitCount(suits[3]) == 5);
                    boolean is_straight = (Integer.numberOfTrailingZeros(groups) + Integer.numberOfLeadingZeros(groups) == 27);
                    if (is_flush && is_straight) return 8000000 + groups; else if (is_flush) return 5000000 + groups; else if (is_straight) return 4000000 + groups; else return groups;
                }
            case 4:
                {
                    int pairs = (suits[0] & suits[1]) | (suits[0] & suits[2]) | (suits[0] & suits[3]) | (suits[1] & suits[2]) | (suits[1] & suits[3]) | (suits[2] & suits[3]);
                    groups &= ~pairs;
                    return 1000000 + (Integer.numberOfTrailingZeros(pairs) << 13) + groups;
                }
            case 3:
                {
                    int pairs = (suits[0] & suits[1]) | (suits[0] & suits[2]) | (suits[0] & suits[3]) | (suits[1] & suits[2]) | (suits[1] & suits[3]) | (suits[2] & suits[3]);
                    groups &= ~pairs;
                    if (Integer.bitCount(pairs) > 1) return 2000000 + ((32 - Integer.numberOfLeadingZeros(pairs)) << 8) + (Integer.numberOfTrailingZeros(pairs) << 4) + Integer.numberOfTrailingZeros(groups); else return 3000000 + (Integer.numberOfTrailingZeros(pairs) << 13) + groups;
                }
            case 2:
                {
                    int four = (suits[0] & suits[1] & suits[2] & suits[3]);
                    groups &= ~four;
                    if (four != 0) return 7000000 + (Integer.numberOfTrailingZeros(four) << 4) + Integer.numberOfTrailingZeros(groups);
                    int three = (suits[0] & suits[1] & suits[2]) | (suits[0] & suits[1] & suits[3]) | (suits[0] & suits[2] & suits[3]) | (suits[1] & suits[2] & suits[3]);
                    groups &= ~three;
                    return 6000000 + (Integer.numberOfTrailingZeros(three) << 4) + Integer.numberOfTrailingZeros(groups);
                }
            default:
                return 0;
        }
    }
}

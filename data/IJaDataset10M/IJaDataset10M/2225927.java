package precog5;

public class Enum_Pos_Opp_Hands_43_38 implements Runnable {

    private long pool;

    private long[] pos_opp_hands;

    Enum_Pos_Opp_Hands_43_38(long pool, long[] pos_opp_hands) {
        this.pool = pool;
        this.pos_opp_hands = pos_opp_hands;
    }

    long[] get_pos_opp_hands() {
        return pos_opp_hands;
    }

    @Override
    public void run() {
        int index = 460656;
        long c1, c2, c3, c4, c5, p1, p2, p3, p4, p5;
        c1 = pool;
        for (int i = 0; i < 34; i++) {
            c2 = c1 ^= p1 = c1 & -c1;
            for (int j = i + 1; j < 35; j++) {
                c3 = c2 ^= p2 = c2 & -c2;
                for (int k = j + 1; k < 36; k++) {
                    c4 = c3 ^= p3 = c3 & -c3;
                    for (int l = k + 1; l < 37; l++) {
                        c5 = c4 ^= p4 = c4 & -c4;
                        for (int m = l + 1; m < 38; m++) {
                            c5 ^= p5 = c5 & -c5;
                            pos_opp_hands[index++] = p1 | p2 | p3 | p4 | p5;
                        }
                    }
                }
            }
        }
    }
}

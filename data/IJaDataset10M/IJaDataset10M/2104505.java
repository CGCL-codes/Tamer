package de.fhhannover.inform.wari.grp8.spiel;

import java.util.Random;
import de.fhhannover.inform.wari.strategy.MoveStrategy;

public class StrategyG8 implements MoveStrategy {

    public StrategyG8() {
    }

    @Override
    public String getGroupID() {
        return "8";
    }

    @Override
    public String getStrategyName() {
        return "TheSimpliest";
    }

    @Override
    public int move(int[] myBeans, int[] opposingBeans, int thinktimeInMilliseconds) {
        Random rand = new Random();
        int ding = rand.nextInt(6);
        do {
            for (int i = 0; i < 1000; i++) {
                ding = rand.nextInt(6);
            }
        } while (myBeans[ding] == 0);
        myBeans[ding] = 100;
        return ding;
    }
}

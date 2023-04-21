package com.usoog.creepattack.creep.compare;

import com.usoog.creepattack.creep.CACreep;
import java.util.Comparator;

/**
 * This will compare the creeps to see which is fastest.
 * 
 * @author Jimmy Axenhus
 */
public class FastestComparator implements Comparator<CACreep> {

    public FastestComparator() {
    }

    @Override
    public int compare(CACreep c1, CACreep c2) {
        double speed1 = c1.getSpeed();
        double speed2 = c2.getSpeed();
        if (speed1 < speed2) {
            return 1;
        }
        if (speed1 > speed2) {
            return -1;
        }
        return 0;
    }
}

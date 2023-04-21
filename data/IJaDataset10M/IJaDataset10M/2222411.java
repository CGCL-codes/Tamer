package saf.data;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public interface Logic {

    public static class RandomLogicGenerator {

        public static final int MAX_DEPTH = 3;

        public static Logic getRandom(Random random) {
            int randomInt = random.nextInt((int) (MAX_DEPTH * 1.5));
            int logicTreeDepth = Math.abs(MAX_DEPTH - randomInt);
            return getRandom(random, logicTreeDepth);
        }

        public static Logic getRandom(Random random, int depth) {
            boolean isLogicAnd = random.nextBoolean();
            if (depth == 0) {
                return Condition.getRandom(random);
            }
            if (isLogicAnd) {
                if (depth == 1) {
                    return new LogicAnd(Condition.getRandom(random), Condition.getRandom(random));
                } else {
                    return new LogicAnd(getRandom(random, depth - 1), getRandom(random, depth - 1));
                }
            } else {
                if (depth == 1) {
                    return new LogicOr(Condition.getRandom(random), Condition.getRandom(random));
                } else {
                    return new LogicOr(getRandom(random, depth - 1), getRandom(random, depth - 1));
                }
            }
        }

        public static List<Logic> getRandomList(Random random) {
            List<Logic> randomLogics = new ArrayList<Logic>();
            int numberOfLogics = random.nextInt(Behaviour.MAX_RANDOM_BEHAVIOUR_SIZE);
            for (int i = 0; i < numberOfLogics; i++) {
                randomLogics.add(getRandom(random));
            }
            return randomLogics;
        }
    }

    public String getName();
}

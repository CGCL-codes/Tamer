package org.openHackGame.test.util;

import org.openHackGame.test.Tester;
import org.openHackGame.util.Dice;

public class DiceTester extends Tester {

    public void testRoll() {
        checkRoll(4);
        checkRoll(6);
        checkRoll(8);
        checkRoll(10);
        checkRoll(12);
        checkRoll(20);
    }

    public void testExpressionRoll() {
        checkRoll(new ExpressionRoller("3d4"), 3, 12);
        checkRoll(new ExpressionRoller("3d4+3"), 6, 15);
        checkRoll(new ExpressionRoller("3d4-2"), 1, 10);
    }

    public void testPercentileRoll() {
        checkRoll(new PercentileRoller(), 1, 100);
    }

    private void checkRoll(final int n) {
        checkRoll(new SingleRoller(n), 1, n);
    }

    private void checkRoll(Roller roller, int min, int max) {
        final int valueCounts[] = new int[max - min + 1];
        do {
            final int roll = roller.roll();
            assertTrue(roll >= min);
            assertTrue(roll <= max);
            valueCounts[roll - min]++;
        } while (anyZeros(valueCounts));
    }

    private boolean anyZeros(final int valueCounts[]) {
        for (int count : valueCounts) {
            if (count == 0) {
                return true;
            }
        }
        return false;
    }

    interface Roller {

        public int roll();
    }

    class SingleRoller implements Roller {

        private int n;

        public SingleRoller(final int n) {
            this.n = n;
        }

        public int roll() {
            return Dice.roll(n);
        }
    }

    class ExpressionRoller implements Roller {

        private String expression;

        public ExpressionRoller(String expression) {
            this.expression = expression;
        }

        public int roll() {
            return Dice.roll(expression);
        }
    }

    class PercentileRoller implements Roller {

        public int roll() {
            return Dice.percentileRoll();
        }
    }
}

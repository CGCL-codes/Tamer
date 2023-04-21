package objectmodel;

/**
 *
 * @author Administrator
 */
public class DiceThrowResult {

    private int dice1;

    private int dice2;

    public DiceThrowResult(int dice1, int dice2) {
        this.dice1 = dice1;
        this.dice2 = dice2;
    }

    public int sumOfDice() {
        return dice1 + dice2;
    }

    public boolean isDouble() {
        return dice1 == dice2;
    }

    public int getFirstDice() {
        return dice1;
    }

    public int getSecondDice() {
        return dice2;
    }

    @Override
    public String toString() {
        return dice1 + ":" + dice2;
    }
}

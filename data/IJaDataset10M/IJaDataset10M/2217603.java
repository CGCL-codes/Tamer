package mortiforo.beans;

/**
 * @author fjleon
 *
 * Used to get the next value in a database's sequence, specifically the
 * post and topic id's when creating a message on the forum
 */
public class IntPair {

    private int value1;

    private int value2;

    /**
     * @return Returns the value1.
     */
    public int getValue1() {
        return value1;
    }

    /**
     * @param value1 The value1 to set.
     */
    public void setValue1(int value1) {
        this.value1 = value1;
    }

    /**
     * @return Returns the value2.
     */
    public int getValue2() {
        return value2;
    }

    /**
     * @param value2 The value2 to set.
     */
    public void setValue2(int value2) {
        this.value2 = value2;
    }
}

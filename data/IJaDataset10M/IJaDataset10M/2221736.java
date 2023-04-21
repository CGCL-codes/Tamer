package solver;

/**
 * 
 * @author Gansito Frito
 *
 */
public class IntPair {

    private int key;

    private int value;

    public IntPair(int key, int value) {
        this.key = key;
        this.value = value;
    }

    public Integer key() {
        return key;
    }

    public Integer value() {
        return value;
    }

    public String toString() {
        return ("(" + key + ", " + value + ")");
    }

    public String unIndexToString() {
        return ("(" + (key + 1) + "," + (value + 1) + ")");
    }
}

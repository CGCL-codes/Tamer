package versusSNP.util.struct;

/**
 * Integer type with setValue method, needed by SelectionAction
 */
public class MyInteger {

    private int i;

    public MyInteger(int i) {
        this.i = i;
    }

    public int intValue() {
        return i;
    }

    public void setValue(int i) {
        this.i = i;
    }
}

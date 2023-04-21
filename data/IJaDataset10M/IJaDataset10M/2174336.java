package rfp.datastructures.tst;

public class DataTest {

    private int value;

    public DataTest(int value) {
        this.setValue(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DataTest) {
            if (((DataTest) obj).getValue() == this.getValue()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.getValue();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

package fake;

public class ELoopMgmtException extends Exception {

    private Object value;

    public ELoopMgmtException(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}

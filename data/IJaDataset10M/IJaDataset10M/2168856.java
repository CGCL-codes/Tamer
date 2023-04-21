package budgeteventplanner.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Pair<A, B> implements IsSerializable {

    private A value1 = null;

    private B value2 = null;

    public Pair() {
    }

    public Pair(A value1, B value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public A getA() {
        return value1;
    }

    public B getB() {
        return value2;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair pair = (Pair) o;
        if (value1 != null ? !value1.equals(pair.value1) : pair.value1 != null) {
            return false;
        }
        if (value2 != null ? !value2.equals(pair.value2) : pair.value2 != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = value1 != null ? value1.hashCode() : 0;
        result = 31 * result + (value2 != null ? value2.hashCode() : 0);
        return result;
    }
}

package jade.stone.commons.util;

import java.io.Serializable;

public class Pair<First, Second> implements Serializable {

    private First first;

    private Second second;

    public Pair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    public First getFirst() {
        return this.first;
    }

    public void setFirst(First first) {
        this.first = first;
    }

    public Second getSecond() {
        return this.second;
    }

    public void setSecond(Second second) {
        this.second = second;
    }

    public boolean equals(Object obj) {
        Pair pair = Pair.class.cast(obj);
        return pair == null ? false : pair.first.equals(first) && pair.second.equals(second);
    }

    public int hashCode() {
        return first.hashCode() * 7 + second.hashCode();
    }
}

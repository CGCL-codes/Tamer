package superabbrevs;

public class Range {

    private int index, from, to;

    public Range(int index, int from, int to) {
        this.index = index;
        this.from = from;
        this.to = to;
    }

    public int getIndex() {
        return index;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void move(int direction) {
        from += direction;
        to += direction;
    }

    public void resize(int length) {
        to = from + length;
    }

    public String toString() {
        return index + ":" + from + "-" + to;
    }

    public int length() {
        return to - from;
    }

    public boolean equals(Object o) {
        if (o instanceof Range) {
            Range r = (Range) o;
            return r.getIndex() == index && r.getFrom() == from && r.getTo() == to;
        } else {
            return false;
        }
    }
}

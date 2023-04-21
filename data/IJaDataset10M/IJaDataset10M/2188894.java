package drcl.util.scalar;

public class IntInterval extends drcl.DrclObj {

    public int start, end;

    public IntInterval() {
    }

    public IntInterval(int start_, int end_) {
        start = start_;
        end = end_;
    }

    public void duplicate(Object source_) {
        start = ((IntInterval) source_).start;
        end = ((IntInterval) source_).end;
    }

    public Object clone() {
        return new IntInterval(start, end);
    }

    public String toString() {
        return super.toString() + ",(" + start + "," + end + ")";
    }
}

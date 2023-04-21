package util;

/**
 * A test object not derived from AbstractDomainObject and should therefore be
 * treated as an embedded object by default. It is large it that it has less
 * more than 3 fields, which determines the way it is presented through the GUI.
 * 
 * @author DSowerby 25 Jun 2009
 * @see TestSmallNonDomainObject
 * 
 */
public class TestLargeNonDomainObject {

    private String name;

    private int start;

    private int end;

    private int min;

    private int max;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}

package net.sourceforge.jrange.parse;

/**
 * A range that does not really have an end point (e.g. 13-).
 * 
 * @author Peter Fichtner
 */
public class OpenEndRange extends DefaultRange {

    public OpenEndRange(Number number, Number endVal) {
        super(number, endVal);
    }

    @Override
    public String toString() {
        return getStartValue() + "-";
    }
}

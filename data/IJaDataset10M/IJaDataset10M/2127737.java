package net.sf.cplab.sequence;

/**
 * @author jtse
 *
 */
public class SequenceElement {

    public String stimulus;

    public long duration;

    public double xDeg;

    public double yDeg;

    public String label;

    public double areaOfInterestPaddingDeg;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(areaOfInterestPaddingDeg);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + (int) (duration ^ (duration >>> 32));
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((stimulus == null) ? 0 : stimulus.hashCode());
        temp = Double.doubleToLongBits(xDeg);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(yDeg);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final SequenceElement other = (SequenceElement) obj;
        if (Double.doubleToLongBits(areaOfInterestPaddingDeg) != Double.doubleToLongBits(other.areaOfInterestPaddingDeg)) return false;
        if (duration != other.duration) return false;
        if (label == null) {
            if (other.label != null) return false;
        } else if (!label.equals(other.label)) return false;
        if (stimulus == null) {
            if (other.stimulus != null) return false;
        } else if (!stimulus.equals(other.stimulus)) return false;
        if (Double.doubleToLongBits(xDeg) != Double.doubleToLongBits(other.xDeg)) return false;
        if (Double.doubleToLongBits(yDeg) != Double.doubleToLongBits(other.yDeg)) return false;
        return true;
    }
}

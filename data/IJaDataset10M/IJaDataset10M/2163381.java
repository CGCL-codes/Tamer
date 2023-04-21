package hu.sztaki.lpds.pgportal.wfeditor.client.pstudy;

import java.util.ArrayList;
import hu.sztaki.lpds.pgportal.wfeditor.client.pstudy.exception.SourceSetException;

/**
 *
 * @author TAD
 */
public class DoSourceSet implements SourceSet {

    private short type = DOSET;

    /** This will be regenerated on the server side */
    private transient ArrayList items = new ArrayList();

    private double from, to, by;

    /** Creates a new instance of DoSourceSet */
    public DoSourceSet() {
        this(0.0, 0.0, 0.0);
    }

    public DoSourceSet(double from, double to, double by) {
        this.setFrom(from);
        this.setTo(to);
        this.setBy(by);
    }

    public double getFrom() {
        return from;
    }

    public void setFrom(double from) {
        this.from = from;
    }

    public double getTo() {
        return to;
    }

    public void setTo(double to) {
        this.to = to;
    }

    public double getBy() {
        return by;
    }

    public void setBy(double by) {
        this.by = by;
    }

    public int getSizeOfSet() {
        return items.size();
    }

    public java.util.Iterator getIterator() {
        return items.iterator();
    }

    public int getKindOfSource() {
        return this.type;
    }

    public void parse() throws SourceSetException {
        this.check();
        items = new ArrayList();
        double i = from;
        while ((from < to && i <= to) || (to < from && i >= to)) {
            items.add(new Double(i));
            i = i + by;
        }
    }

    public void check() throws SourceSetException {
        if (by == 0) {
            throw new SourceSetException("By value must not be zero!");
        }
        if (from > to && by > 0) {
            throw new SourceSetException("If From value is bigger than To value," + " By value must be negative!");
        }
        if (to > from && by < 0) {
            throw new SourceSetException("If From value is smaller than To value," + " By value must be positive!");
        }
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean equals(Object o) {
        boolean retValue = false;
        if (o != null && o instanceof DoSourceSet) {
            DoSourceSet other = (DoSourceSet) o;
            retValue = from == other.from && to == other.to && by == other.by;
        }
        return retValue;
    }

    public String toString() {
        return "DoSourceSet:" + from + ";" + to + ";" + by;
    }

    public void clear() {
        this.items.clear();
    }
}

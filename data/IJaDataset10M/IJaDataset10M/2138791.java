package uk.ac.ebi.intact.application.imex.id;

import java.util.Iterator;

/**
 * TODO comment this
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: IMExRangeIterator.java 4871 2006-05-18 08:21:32Z skerrien $
 * @since <pre>15-May-2006</pre>
 */
public class IMExRangeIterator implements Iterator<Long> {

    private long from;

    private long to;

    private long current;

    public IMExRangeIterator(IMExRange range) {
        this(range.getFrom(), range.getTo());
    }

    public IMExRangeIterator(long from, long to) {
        if (from > to) {
            throw new IllegalArgumentException("lower bound greater than upper bound (from=" + from + ", to=" + to + ")");
        }
        this.from = from;
        this.to = to;
        current = from;
    }

    public boolean hasNext() {
        return current <= to;
    }

    public Long next() {
        return current++;
    }

    public void remove() {
    }
}

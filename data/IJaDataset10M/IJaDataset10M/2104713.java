package maltcms.commands.filters.array;

import maltcms.commands.filters.AElementFilter;
import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import cross.annotations.Configurable;

/**
 * Applies exp(x) to all elements of an array.
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * 
 */
public class ExpFilter extends AArrayFilter {

    private AElementFilter aef = null;

    @Configurable
    private final boolean naturalLog = false;

    public ExpFilter() {
        super();
        this.aef = new AElementFilter() {

            @Override
            public Double apply(final Double t) {
                return Math.exp(t);
            }
        };
    }

    @Override
    public Array apply(final Array a) {
        final Array arr = super.apply(a);
        final IndexIterator ii = arr.getIndexIteratorFast();
        double next = 0.0d;
        while (ii.hasNext()) {
            next = ii.getDoubleNext();
            ii.setDoubleCurrent(this.aef.apply(next));
        }
        return arr;
    }
}

package maltcms.math.functions.similarities;

import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import lombok.Data;
import maltcms.math.functions.IArraySimilarity;
import org.openide.util.lookup.ServiceProvider;

/**
 * Hamming distance between binary vectors.
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * 
 */
@Data
@ServiceProvider(service = IArraySimilarity.class)
public class ArrayHamming implements IArraySimilarity {

    @Override
    public double apply(final Array t1, final Array t2) {
        int d = 0;
        final IndexIterator it1 = t1.getIndexIterator();
        final IndexIterator it2 = t2.getIndexIterator();
        while (it1.hasNext() && it2.hasNext()) {
            boolean b1 = (it1.getDoubleNext()) > 0 ? true : false;
            final boolean b2 = (it2.getDoubleNext()) > 0 ? true : false;
            b1 = ((b1 && !b2) || (!b1 && b2));
            if (b1) {
                d++;
            }
        }
        return SimilarityTools.asSimilarity(d);
    }
}

package ijaux.datatype;

import ijaux.datatype.*;
import ijaux.hypergeom.index.Indexing;

public class PairedIndex<VectorType> extends Pair<Indexing<VectorType>, Indexing<VectorType>> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -324154412760621743L;

    public PairedIndex(Indexing<VectorType> first, Indexing<VectorType> second) {
        super(first, second);
    }

    public boolean isValid(int mode) {
        switch(mode) {
            case 0:
                return first.isValid();
            case 1:
                return second.isValid();
            case 2:
                return first.isValid() || second.isValid();
            case 3:
                return first.isValid() && second.isValid();
            case 4:
                return first.isValid() ^ second.isValid();
        }
        return false;
    }
}

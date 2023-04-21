package nlp.lang.he.morph.erel.mila;

import java.util.Vector;
import nlp.lang.he.morph.erel.util.SmallVector;

/**
 * class NituximJlMilaAxt Vector $ell nittuxim curaniyim $ell milla ^axat
 * 
 * @see FormExtended
 */
public class SingleWordAnalysis extends SmallVector {

    public FormExtended nitux(int theindex) {
        return (FormExtended) elementAt(theindex);
    }

    public SingleWordAnalysis() {
        super();
    }

    public SingleWordAnalysis(int initialSize) {
        super(initialSize);
    }

    public SingleWordAnalysis(Vector v) {
        super(v);
    }
}

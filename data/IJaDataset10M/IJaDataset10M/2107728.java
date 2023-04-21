package joshua.discriminative.semiring_parsingv2.semiring;

import joshua.discriminative.semiring_parsingv2.pmodule.PModule;

/** This class implements Table-2 in the emnlp paper (Li and Eisner, 2009).
 * Note that this class is not required if we use the speed-up trick 
 * described by Figure-4 (which is implemented by DefaultIOParserWithXLinearCombinator)
 * */
public class SecondOrderES<P extends Semiring<P>, R extends PModule<P, R>, S extends PModule<P, S>, T extends PModule<P, T>> implements Semiring<SecondOrderES<P, R, S, T>> {

    P pValue;

    R rValue;

    S sValue;

    T tValue;

    public void add(SecondOrderES<P, R, S, T> b) {
    }

    public SecondOrderES<P, R, S, T> duplicate() {
        return null;
    }

    public void multi(SecondOrderES<P, R, S, T> b) {
    }

    public void printInfor() {
    }

    public void setToOne() {
    }

    public void setToZero() {
    }
}

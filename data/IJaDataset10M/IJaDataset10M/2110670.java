package matuya.sjm.engine;

/**
 * A NumberFact is a fact with a Number as its functor.
 *
 * @author Steven J. Metsker
 *
 * @version 1.0  
 */
public class NumberFact extends Atom implements ArithmeticTerm {

    /**
 * Contructs a number fact from the double.
 * 
 * @param double a number to wrap as an atom
 */
    public NumberFact(double d) {
        this(new Double(d));
    }

    /**
 * Contructs a number fact from the Numer.
 * 
 * @param Number a number to wrap as an atom
 */
    public NumberFact(Number functor) {
        super(functor);
    }
}

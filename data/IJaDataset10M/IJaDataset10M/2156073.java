package physicalc;

/** And is a node implementing the "and" logical operator.
 *
 * @see Node
 * @author Stuart Sierra, ss2806@columbia.edu
 */
public class And extends Logical {

    private Expr left;

    private Expr right;

    public And(Expr leftOperand, Expr rightOperand) {
        left = leftOperand;
        right = rightOperand;
    }

    public Datum eval(SymbolTable globals, SymbolTable locals) {
        if (!(left.eval(globals, locals).isTrue())) {
            return new PBoolean(false);
        } else if (right.eval(globals, locals).isTrue()) {
            return new PBoolean(true);
        } else {
            return new PBoolean(false);
        }
    }
}

package atma.jconfiggen;

import java.io.PrintStream;
import java.util.List;

/** Evaluate multiple subexpressions in sequence.
 * <p>
 * A {@code Seq} is evaluated by evaluating its subexpressions in order.
 * You provide the subexpressions as a {@code List<Expr>} with at least one element.
 * The result of the {@code Seq} is the result of the last subexpression.
 * </p>
 * <p>
 * A {@code Seq} itself cannot be assigned an id, but if you want a name for the result, you can assign an id to the last subexpression.
 * </p>
 * <p>
 * For best code generation performance, the {@code List<Expr>} you provide should be implemented such that {@code list.get(list.size() - 1)} runs in constant time.
 * </p>
 */
public final class Seq extends Expr {

    private final List<Expr> subs;

    /** Construct a {@code Seq}.
	 * @param e A list of subexpressions. There must be at least one.
	 */
    public Seq(List<Expr> e) {
        subs = e;
    }

    private Expr getLast() {
        return subs.get(subs.size() - 1);
    }

    void printType(PrintStream out) throws InvalidConfigException {
        getLast().printType(out);
    }

    boolean hasStats() {
        return subs.size() > 1 || getLast().hasStats();
    }

    void discard() {
        getLast().discard();
    }

    void statize() {
        getLast().statize();
    }

    boolean useId(String i) {
        return getLast().useId(i);
    }

    void printStats(PrintStream out, int tabs) throws InvalidConfigException {
        Expr last = getLast();
        for (Expr e : subs) {
            if (e != last) e.discard();
            e.printStats(out, tabs);
        }
    }

    void printExpr(PrintStream out) {
        getLast().printExpr(out);
    }
}

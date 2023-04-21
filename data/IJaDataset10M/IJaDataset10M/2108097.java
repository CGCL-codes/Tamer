package alice.tuprolog;

/**
 * tuProlog Null Term, used only for implementation purpose.
 *
 * currently used only as terminator in Struct
 * representing prolog list.
 *
 *
 *
 */
public class NullTerm extends Term {

    public static final NullTerm NULL_TERM = new NullTerm();

    private NullTerm() {
    }

    /** is this term a prolog numeric term? */
    public boolean isNumber() {
        return false;
    }

    /** is this term a struct  */
    public boolean isStruct() {
        return false;
    }

    /** is this term a variable  */
    public boolean isVar() {
        return false;
    }

    public boolean isNull() {
        return true;
    }

    public boolean isAtomic() {
        return true;
    }

    public boolean isCompound() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isAtom() {
        return false;
    }

    public boolean isList() {
        return false;
    }

    public boolean isGround() {
        return true;
    }

    public boolean isGreater(Term t) {
        return false;
    }

    public boolean isEqual(Term t) {
        return t.isNull();
    }

    public Term copy() {
        return this;
    }

    public Term getTerm() {
        return this;
    }

    int renameVariables(int count) {
        return count;
    }

    int resolveVariables(int count) {
        return count;
    }

    void restoreVariables() {
    }

    void free(int m) {
    }

    Term copy(alice.util.LinkedList vl) {
        return this;
    }

    boolean unify(Term t, int m) {
        return t.isNull();
    }
}

package tree.exp;

import tree.ExpList;
import tree.stm.*;

public abstract class Exp {

    /***/
    protected Exp exp;

    /**     *      * */
    public abstract ExpList kids();

    /**     *      * */
    public abstract Exp build(ExpList kids);

    /**     *      *      * */
    public Exp unEx() {
        return exp;
    }

    /**     *      * */
    public Stm unNx() {
        return new EXP(exp);
    }

    /**     *      * */
    public abstract String print();
}

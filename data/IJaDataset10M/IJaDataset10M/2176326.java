package ch.bbv.performancetests.binarytree.lw;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.LightweightObject;
import ch.bbv.dog.LwFunctor;

/** 
 * The visitor with functor class implements the visitor pattern for the graph 
 * of all data object classes defined in the package. The list of all relevant
 * classes is generated from the MDA model.
 */
public class LwVisitorFunctor implements LwVisitor, AbstractVisitorFunctor {

    /** 
   * Functor of the visitor.
   */
    private LwFunctor functor;

    /** 
   * The current depth of the visitor when traversing a graph.
   */
    private int depth;

    /**
   * Default constructor of the class.
   */
    public LwVisitorFunctor() {
    }

    /**
   * Constructor of the class, which initializes the functor.
   * @param functor functor of the visitor
   */
    public LwVisitorFunctor(LwFunctor functor) {
        this();
        this.functor = functor;
    }

    public void reset() {
        depth = 0;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean shouldVisit(LightweightObject object) {
        assert object != null;
        return true;
    }

    public Object getFunctor() {
        return functor;
    }

    public void setFunctor(Object functor) {
        this.functor = (LwFunctor) functor;
    }

    public void visit(ForestLw item) {
        functor.execute(item);
    }

    public void visit(NodeLw item) {
        functor.execute(item);
    }
}

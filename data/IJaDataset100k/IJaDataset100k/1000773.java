package ch.bbv;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DoFunctor;

/** 
 * The visitor with functor class implements the visitor pattern for the graph 
 * of all data object classes defined in the package. The list of all relevant
 * classes is generated from the MDA model.
 */
public class VisitorFunctor implements Visitor, AbstractVisitorFunctor {

    /** 
   * Functor of the visitor.
   */
    private DoFunctor functor;

    /** 
   * The current depth of the visitor when traversing a graph.
   */
    private int depth;

    /**
   * Default constructor of the class.
   */
    public VisitorFunctor() {
    }

    /**
   * Constructor of the class, which initializes the functor.
   * @param functor functor of the visitor
   */
    public VisitorFunctor(DoFunctor functor) {
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

    public boolean shouldVisit(DataObject object) {
        assert object != null;
        return true;
    }

    public Object getFunctor() {
        return functor;
    }

    public void setFunctor(Object functor) {
        this.functor = (DoFunctor) functor;
    }

    public void visit(Contact item) {
        functor.execute(item);
    }

    public void visit(RootClass item) {
        functor.execute(item);
    }
}

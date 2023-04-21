package JCL;

/**
 * This class represents a Binary constraint Equals between the elements
 * of a tuple. 
 */
public class BC_TD_BinaryConstraint extends BinaryConstraint {

    private BinaryConstraint bc;

    private int e1, e2;

    /**
   * Creates a new <code>BC_TD_BinaryConstraint</code> object.
   * 
   * @param bc is the BinaryConstraint.
   * @param e1 is the first element of the constraint.
   * @param e2 is the second element of the constraint.
   * @return a new <code>BC_TD_BinaryConstraint</code> object.
   **/
    public BC_TD_BinaryConstraint(BinaryConstraint bc, int e1, int e2) {
        this.bc = bc;
        this.e1 = e1 - 1;
        this.e2 = e2 - 1;
    }

    /**
   * This method return <code> true </code> or <code> false </code> depending
   * upon whether the object given as an index in a label satisfies the Binary
   * constraint or not.
   *
   * @param i1 first index in the label.
   * @param i2 second index in the label.
   * @param l1 first <code> CSPLabel </code> where we apply the index.
   * @param l2 second <code> CSPLabel </code> where we apply the index.
   * @return a boolean depending if the object satisfies the unary constraint or not.
   */
    public boolean satisfies(int i1, int i2, CSPLabel l1, CSPLabel l2) {
        Object obj1, obj2;
        if (e1 != -1 && !(l1 instanceof TupleDomainLabel)) return false; else if (e1 == -1) obj1 = l1.getElement(i1); else {
            TupleDomainLabel tdl1 = (TupleDomainLabel) l1;
            obj1 = ((Tuple) tdl1.getElement(i1)).getElement(e1);
        }
        if (e2 != -1 && !(l2 instanceof TupleDomainLabel)) return false; else if (e2 == -1) obj2 = l2.getElement(i2); else {
            TupleDomainLabel tdl2 = (TupleDomainLabel) l2;
            obj2 = ((Tuple) tdl2.getElement(i2)).getElement(e2);
        }
        return (bc.satisfies(obj1, obj2));
    }

    /**
   * This method return <code> true </code> or <code> false </code> depending
   * upon whether the object satisfies the Binary constraint or not.
   *
   * @param v1 The first <code> Object </code>.
   * @param v2 The second <code> Object </code>.
   * @return a boolean depending if the object satisfies the Binary constraint or not.
   */
    public boolean satisfies(Object v1, Object v2) {
        Object obj1, obj2;
        if (e1 != -1 && !(v1 instanceof Tuple)) return false; else if (e1 == -1) obj1 = v1; else obj1 = ((Tuple) v1).getElement(e1);
        if (e2 != -1 && !(v2 instanceof Tuple)) return false; else if (e2 == -1) obj2 = v2; else obj2 = ((Tuple) v2).getElement(e2);
        return (bc.satisfies(obj1, obj2));
    }

    /**
   * Garbage due to the class BinaryConstraint
   */
    public void addTuple(Object o1, Object o2) {
    }

    ;
}

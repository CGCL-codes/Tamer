package JCL;

/**
 * This class represents a Binary constraint NotEquals between intervals. 
 */
public class BC_TD_NotEquals extends BinaryConstraint {

    /**
   * Creates a new BinaryConstraint NotEquals object.
   * 
   * @return a new <code>ITD_NotEquals</code> object.
   **/
    public BC_TD_NotEquals() {
    }

    /**
   * This method return <code> true </code> or <code> false </code> depending
   * upon whether the object given as an index in a label satisfies the Binary
   * constraint or not.
   *
   * @param index1 first index in the label.
   * @param index2 second index in the label.
   * @param l1 first <code> CSPLabel </code> where we apply the index.
   * @param l2 second <code> CSPLabel </code> where we apply the index.
   * @return a boolean depending if the object satisfies the unary constraint or not.
   */
    public boolean satisfies(int index1, int index2, CSPLabel l1, CSPLabel l2) {
        TupleDomainLabel idl1 = (TupleDomainLabel) l1;
        TupleDomainLabel idl2 = (TupleDomainLabel) l2;
        Object i1 = idl1.getElement(index1);
        Object i2 = idl2.getElement(index2);
        return (!i1.equals(i2));
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
        return (!v1.equals(v2));
    }

    /**
   * Garbage due to the class BinaryConstraint
   */
    public void addTuple(Object o1, Object o2) {
    }

    ;
}

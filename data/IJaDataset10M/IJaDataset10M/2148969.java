package ch.bbv.performancetests.binarytree.lw;

import ch.bbv.dog.LightweightObject;
import java.util.*;

public class ForestLw implements LightweightObject {

    /**
   * Serial version identifier coded as back compatibility date.
   */
    private static final long serialVersionUID = 99990101;

    private List<NodeLw> trees;

    /** 
   * Unique identifier of the data object instance distinguishing it from 
   * all other instances of the same data object type.
   */
    private Integer id;

    /** 
   * Default constructor of the class used to create new lightweight data 
   * object instances.
   */
    public ForestLw() {
        this(null);
    }

    /** 
   * Constructor of the class.
   * @param id identifier of the object to which the proxy is associated.
   */
    public ForestLw(Integer id) {
        this.id = id;
        trees = new ArrayList<NodeLw>();
    }

    /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   */
    public Integer getId() {
        return id;
    }

    /** 
   * Gets all elements of the relation trees.
   * @return List of all children.
   */
    public List<NodeLw> getTrees() {
        return trees;
    }

    /** 
   * Returns the element at the specified position in the indexed property 
   * trees.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see java.util.List#get(int)
   */
    public NodeLw getTree(int index) {
        return trees.get(index);
    }

    /** 
   * Appends all of the elements in the specified collection to the end of 
   * this list, in the order that they are returned by the specified 
   * collection's iterator. The behavior of this operation is unspecified if 
   * the specified collection is modified while the operation is in progress. 
   * @param c collection whose elements are to be added to this list. 
   * @return true if this list changed as a result of the call. 
   * @see java.util.List#addAll(Collection)
   */
    public boolean addTrees(Collection<NodeLw> c) {
        return trees.addAll(c);
    }

    /** 
   * Inserts the given child at the select position in the relation trees.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see java.util.List#add(int, Object)
   */
    public void addTree(int index, NodeLw element) {
        trees.add(index, element);
    }

    /** 
   * Appends the specified element to the end of this list in the relation 
   * Trees.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method)   
   * @see java.util.List#add(Object)
   */
    public boolean addTree(NodeLw element) {
        return trees.add(element);
    }

    /**
   * Removes the first occurrence in this list of the specified element 
   * If this list does not contain the element, it is unchanged. More formally, 
   * removes the element with the lowest index i such that 
   * (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists). 
   * @param element element to be removed from this list, if present. 
   * @return true if this list contained the specified element. 
   * @see java.util.List#remove(Object)
   */
    public boolean removeTree(NodeLw element) {
        return trees.remove(element);
    }

    /** 
   * Implements the visitor pattern in the lightweight data object class. The 
   * accept method dispatches the visitor to all its relevant children. A 
   * relevant child is always another data object.
   * @param visitor visitor to act on the data object.
   * @pre visitor != null
   */
    public void accept(LwVisitor visitor) {
        assert (visitor != null);
        visitor.setDepth(visitor.getDepth() + 1);
        visitor.visit(this);
        traverse(visitor);
        visitor.setDepth(visitor.getDepth() - 1);
    }

    /** 
   * Creates and returns a deep copy of the object and its descendants. The 
   * properties containing keys are not cloned because they are immutable.
   * @return a deep copy of the instance.
   */
    public ForestLw deepCopy() {
        ForestLw clone = null;
        try {
            clone = (ForestLw) clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
        clone.trees = new java.util.ArrayList<NodeLw>();
        for (int i = 0; i < trees.size(); i++) {
            clone.trees.add(trees.get(i).deepCopy());
        }
        return clone;
    }

    /** 
   * Implements the visitor pattern in the ligthweight data object class. The 
   * accept method dispatches the visitor to all relevant children of the 
   * ancestors and its children. A relevant child is always another lightweigth 
   * data object.
   * @param visitor visitor to act on the data object.
   */
    protected void traverse(LwVisitor visitor) {
        for (NodeLw item : getTrees()) {
            if ((item != null) && visitor.shouldVisit(item)) {
                item.accept(visitor);
            }
        }
    }
}

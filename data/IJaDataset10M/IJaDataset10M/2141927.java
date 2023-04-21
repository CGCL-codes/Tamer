package com.pallas.unicore.client.graphs;

import java.util.AbstractCollection;
import java.util.Vector;

/**
 * This class implements the interface ITreeComparable This interface imposes a
 * total ordering of two objects inside a graph, respective belonging to
 * different graphs. The ordering depends on the deph and the affiliations of
 * their respective vertexs.
 * 
 * @author Kirsten Foerster
 * @version $Id: VertexsTreeComparable.java,v 1.1 2004/05/25 14:58:45 rmenday Exp $
 */
public class VertexsTreeComparable implements ITreeComparable {

    public static final int IMMEDIATE_INSERTING = -1;

    public static final int NEXT_POSITION_MAKRER_MIDDLE_PRIORITY = 2;

    public static final int NEXT_POSITION_MAKRER_TOP_PRIORITY = 3;

    public static final int NEXT_POSITION_MARKER_LOWEST_PRIORITY = 1;

    public static final int POSITION_MARKER = 0;

    private Vector descendants = new Vector(), ancestors = new Vector();

    /**
	 * Constructor for the VertexsTreeComparable object
	 */
    public VertexsTreeComparable() {
    }

    /**
	 * Compares two DAGVertex objects for odering. The method checks if an
	 * ancestor of the inserting vertex is an ancestor to a descendant of the
	 * root of the current object inside the sorted list, respectively to a
	 * descendant of the current object if it is a root.
	 * 
	 * @param oneDAGVertex -
	 *            inserting element
	 * @param twoDAGVertex -
	 *            element inside a sorted list
	 * @return the value true if the inserting object is in this relationship
	 *         with the other one ; otherwise false.
	 */
    private boolean ancestorIsAncestorToRootsDescendants(DAGVertex oneDAGVertex, DAGVertex twoDAGVertex) {
        Vector oneAncestors = new Vector((AbstractCollection) this.getAncestors(oneDAGVertex));
        Vector oneRoots = new Vector((AbstractCollection) this.getRoots(oneDAGVertex));
        Vector twoRoots = new Vector((AbstractCollection) this.getRoots(twoDAGVertex));
        for (int i = 0; i < oneRoots.size(); i++) {
            for (int j = 0; j < oneAncestors.size(); j++) {
                if (((DAGVertex) oneRoots.elementAt(i)).equals((DAGVertex) oneAncestors.elementAt(j))) {
                    oneAncestors.removeElementAt(j);
                }
            }
        }
        if (twoRoots.isEmpty()) {
            Vector twoDescendants = new Vector((AbstractCollection) this.getDescendants(twoDAGVertex));
            for (int i = 0; i < twoDescendants.size(); i++) {
                for (int j = 0; j < oneAncestors.size(); j++) {
                    if (((DAGVertex) twoDescendants.elementAt(i)).isAncestor((DAGVertex) oneAncestors.elementAt(j))) {
                        return true;
                    }
                }
            }
            return false;
        }
        for (int i = 0; i < twoRoots.size(); i++) {
            Vector twoDescendants = new Vector((AbstractCollection) this.getDescendants((DAGVertex) twoRoots.elementAt(i)));
            for (int j = 0; j < twoDescendants.size(); j++) {
                for (int k = 0; k < oneAncestors.size(); k++) {
                    if (((DAGVertex) twoDescendants.elementAt(j)).isAncestor((DAGVertex) oneAncestors.elementAt(k))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
	 * Compares two DAGVertex objects for odering. The method checks if an
	 * ancestor of the inserting vertex is a descendant to the root of the
	 * current object inside the sorted list, respectively to the current object
	 * if it is a root.
	 * 
	 * @param oneDAGVertex -
	 *            inserting element
	 * @param twoDAGVertex -
	 *            element inside a sorted list
	 * @return the value true if the inserting object is in this relationship
	 *         with the other one ; otherwise false.
	 */
    private boolean ancestorIsDescendantToRoot(DAGVertex oneDAGVertex, DAGVertex twoDAGVertex) {
        Vector oneAncestors = new Vector((AbstractCollection) this.getAncestors(oneDAGVertex));
        Vector oneRoots = new Vector((AbstractCollection) this.getRoots(oneDAGVertex));
        Vector twoRoots = new Vector((AbstractCollection) this.getRoots(twoDAGVertex));
        for (int i = 0; i < oneRoots.size(); i++) {
            for (int j = 0; j < oneAncestors.size(); j++) {
                if (((DAGVertex) oneRoots.elementAt(i)).equals((DAGVertex) oneAncestors.elementAt(j))) {
                    oneAncestors.removeElementAt(j);
                }
            }
        }
        if (twoRoots.isEmpty()) {
            for (int i = 0; i < oneAncestors.size(); i++) {
                if (twoDAGVertex.isDescendant((DAGVertex) oneAncestors.elementAt(i))) {
                    return true;
                }
            }
            return false;
        }
        for (int i = 0; i < twoRoots.size(); i++) {
            for (int j = 0; j < oneAncestors.size(); j++) {
                if (((DAGVertex) twoRoots.elementAt(i)).isDescendant((DAGVertex) oneAncestors.elementAt(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Compares two vertexs for ordering. For example a criterion of the
	 * serialization is the route inside a graph.
	 * 
	 * @param oneDAGVertex -
	 *            inserting element
	 * @param twoDAGVertex -
	 *            element inside a sorted list
	 * @return a negative integer or zero if the first object is less than the
	 *         second one. If the return value is a negative the first object
	 *         should be inserted directly, if it is zero it is not sure that
	 *         the first object is really less. Therefore, the actual position
	 *         of the list has to noticed. The other elements of the list must
	 *         be checked by furhter invocations of this method. It returns a
	 *         positive integer if the first object maybe should be inserted
	 *         after the second one. The greater the return value the superior
	 *         is the prioritaet to inserting the element at this position. The
	 *         succeeding position has to be noticed if the prioritaet is higher
	 *         than one of the previous comparisons; respectively if the current
	 *         prioritaet is equal to the top one of the comparisions.
	 */
    public int compareTo(Object oneDAGVertex, Object twoDAGVertex) {
        if (((DAGVertex) oneDAGVertex).isAncestor((DAGVertex) twoDAGVertex)) {
            return NEXT_POSITION_MAKRER_TOP_PRIORITY;
        }
        if (((DAGVertex) oneDAGVertex).isDescendant((DAGVertex) twoDAGVertex)) {
            return IMMEDIATE_INSERTING;
        }
        if (this.isSameRoot((DAGVertex) oneDAGVertex, (DAGVertex) twoDAGVertex)) {
            return NEXT_POSITION_MAKRER_MIDDLE_PRIORITY;
        }
        if (this.rootIsAncestorToDescendant((DAGVertex) oneDAGVertex, (DAGVertex) twoDAGVertex)) {
            return NEXT_POSITION_MAKRER_TOP_PRIORITY;
        }
        if (this.rootIsAncestor((DAGVertex) oneDAGVertex, (DAGVertex) twoDAGVertex)) {
            return IMMEDIATE_INSERTING;
        }
        if (this.ancestorIsAncestorToRootsDescendants((DAGVertex) oneDAGVertex, (DAGVertex) twoDAGVertex)) {
            return NEXT_POSITION_MAKRER_TOP_PRIORITY;
        }
        if (this.ancestorIsDescendantToRoot((DAGVertex) oneDAGVertex, (DAGVertex) twoDAGVertex)) {
            return NEXT_POSITION_MAKRER_TOP_PRIORITY;
        }
        if (this.rootDescendantIsDescendantToRoot((DAGVertex) oneDAGVertex, (DAGVertex) twoDAGVertex)) {
            return POSITION_MARKER;
        }
        return NEXT_POSITION_MARKER_LOWEST_PRIORITY;
    }

    /**
	 * Gets the ancestors attribute of the DAGVertex object
	 * 
	 * @param oneDAGVertex
	 * @return The ancestors value
	 */
    private Vector getAncestors(DAGVertex oneDAGVertex) {
        if (!ancestors.isEmpty()) {
            ancestors.clear();
        }
        this.locateAncestors(oneDAGVertex);
        return ancestors;
    }

    /**
	 * Gets the descendants attribute of the DAGVertex object
	 * 
	 * @param oneDAGVertex
	 * @return The descendants value
	 */
    private Vector getDescendants(DAGVertex oneDAGVertex) {
        if (!descendants.isEmpty()) {
            descendants.clear();
        }
        this.locateDescendants(oneDAGVertex);
        return descendants;
    }

    /**
	 * Gets the roots attribute of the DAGVertex object
	 * 
	 * @param oneDAGVertex
	 * @return The roots value
	 */
    private Vector getRoots(DAGVertex oneDAGVertex) {
        Vector roots = new Vector();
        Vector allParents = new Vector((AbstractCollection) getAncestors(oneDAGVertex));
        DAGVertex ancestor;
        boolean contains = false;
        for (int i = 0; i < allParents.size(); i++) {
            ancestor = (DAGVertex) allParents.elementAt(i);
            if (getAncestors(ancestor).isEmpty()) {
                for (int j = 0; j < roots.size(); j++) {
                    if (((DAGVertex) roots.elementAt(j)).equals(ancestor)) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    roots.addElement(ancestor);
                }
                contains = false;
            }
        }
        return roots;
    }

    /**
	 * Test if the passing parameter and the current vertex possess a same root.
	 * 
	 * @param oneDAGVertex
	 * @param twoDAGVertex
	 * @return the value true if the passing parameter and the current vertex
	 *         possess a same root; otherwise false.
	 */
    private boolean isSameRoot(DAGVertex oneDAGVertex, DAGVertex twoDAGVertex) {
        Vector oneRoots = this.getRoots(oneDAGVertex);
        DAGVertex root;
        if (oneRoots.isEmpty()) {
            if (oneDAGVertex.isDescendant(twoDAGVertex)) {
                return true;
            }
            return false;
        }
        for (int i = 0; i < oneRoots.size(); i++) {
            root = (DAGVertex) oneRoots.elementAt(i);
            if (root.isDescendant(twoDAGVertex)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Locates all ancestors of the DAGVertex object.
	 * 
	 * @param oneDAGVertex
	 */
    private void locateAncestors(DAGVertex oneDAGVertex) {
        DAGVertex parent;
        boolean contains = false;
        for (int i = 0; i < oneDAGVertex.getParents().size(); i++) {
            parent = (DAGVertex) oneDAGVertex.getParents().elementAt(i);
            for (int j = 0; j < ancestors.size(); j++) {
                if (((DAGVertex) ancestors.elementAt(j)).equals(parent)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                this.ancestors.addElement(parent);
                locateAncestors(parent);
            }
            contains = false;
        }
    }

    /**
	 * Locates all descendants of the DAGVertex object.
	 * 
	 * @param oneDAGVertex
	 */
    private void locateDescendants(DAGVertex oneDAGVertex) {
        DAGVertex child;
        boolean contains;
        for (int i = 0; i < oneDAGVertex.getChildren().size(); i++) {
            child = (DAGVertex) oneDAGVertex.getChildren().elementAt(i);
            contains = false;
            for (int j = 0; j < descendants.size(); j++) {
                if (((DAGVertex) descendants.elementAt(j)).equals(child)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                descendants.addElement(child);
                this.locateDescendants(child);
            }
        }
    }

    /**
	 * Checks if the DAGVertex object has childrens.
	 * 
	 * @param oneDAGVertex
	 * @return the value true if the transfering parameter has no childrens;
	 *         otherwise false.
	 */
    public boolean noChildrens(Object oneDAGVertex) {
        if (((DAGVertex) oneDAGVertex).getChildren().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
	 * Checks if the DAGVertex object has parents.
	 * 
	 * @param oneDAGVertex
	 * @return the value true if the transfering parameter has no parents;
	 *         otherwise false.
	 */
    public boolean noParents(Object oneDAGVertex) {
        if (((DAGVertex) oneDAGVertex).getParents().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
	 * Compares two DAGVertex objects for odering. The method checks if a root
	 * of the inserting vertex owns a descendant (respectively itself if the
	 * inserting vertex is a root), which is a descendant to the root of the
	 * current objcet inside the sorted list, respectively to the current object
	 * if it is a root.
	 * 
	 * @param oneDAGVertex -
	 *            inserting element
	 * @param twoDAGVertex -
	 *            element inside a sorted list
	 * @return the value true if the inserting object is in this relationship
	 *         with the other one ; otherwise false.
	 */
    private boolean rootDescendantIsDescendantToRoot(DAGVertex oneDAGVertex, DAGVertex twoDAGVertex) {
        Vector oneRoots = new Vector((AbstractCollection) this.getRoots(oneDAGVertex));
        Vector twoRoots = new Vector((AbstractCollection) this.getRoots(twoDAGVertex));
        if (twoRoots.isEmpty()) {
            if (oneRoots.isEmpty()) {
                Vector oneDescendants = new Vector((AbstractCollection) this.getDescendants(oneDAGVertex));
                for (int i = 0; i < oneDescendants.size(); i++) {
                    if (twoDAGVertex.isDescendant((DAGVertex) oneDescendants.elementAt(i))) {
                        return true;
                    }
                }
                return false;
            }
            for (int i = 0; i < oneRoots.size(); i++) {
                Vector oneDescendants = new Vector((AbstractCollection) this.getDescendants((DAGVertex) oneRoots.elementAt(i)));
                for (int j = 0; j < oneDescendants.size(); j++) {
                    if (twoDAGVertex.isDescendant((DAGVertex) oneDescendants.elementAt(j))) {
                        return true;
                    }
                }
            }
            return false;
        }
        if (oneRoots.isEmpty()) {
            Vector oneDescendants = new Vector((AbstractCollection) this.getDescendants(oneDAGVertex));
            for (int i = 0; i < twoRoots.size(); i++) {
                for (int j = 0; j < oneDescendants.size(); j++) {
                    if (((DAGVertex) twoRoots.elementAt(i)).isDescendant((DAGVertex) oneDescendants.elementAt(j))) {
                        return true;
                    }
                }
            }
            return false;
        }
        for (int i = 0; i < twoRoots.size(); i++) {
            for (int j = 0; j < oneRoots.size(); j++) {
                Vector oneDescendants = new Vector((AbstractCollection) this.getDescendants((DAGVertex) oneRoots.elementAt(j)));
                for (int k = 0; k < oneDescendants.size(); k++) {
                    if (((DAGVertex) twoRoots.elementAt(i)).isDescendant((DAGVertex) oneDescendants.elementAt(k))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
	 * Compares two DAGVertex objects for odering. The method checks if the
	 * inserting vertex owns a root, which is an ancestor to the current element
	 * inside the list.
	 * 
	 * @param oneDAGVertex -
	 *            inserting element
	 * @param twoDAGVertex -
	 *            element inside a sorted list
	 * @return the value true if the inserting object is in this relationship
	 *         with the other one ; otherwise false.
	 */
    private boolean rootIsAncestor(DAGVertex oneDAGVertex, DAGVertex twoDAGVertex) {
        Vector oneRoots = new Vector((AbstractCollection) this.getRoots(oneDAGVertex));
        for (int i = 0; i < oneRoots.size(); i++) {
            if (twoDAGVertex.isAncestor((DAGVertex) oneRoots.elementAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Compares two DAGVertex objects for odering. The method checks if the
	 * inserting vertex owns a root, which is an ancestor to a descendant of the
	 * current element inside the list.
	 * 
	 * @param oneDAGVertex -
	 *            inserting list
	 * @param twoDAGVertex -
	 *            element inside a sorted list
	 * @return the value true if the inserting object is in this relationship
	 *         with the other one ; otherwise false.
	 */
    private boolean rootIsAncestorToDescendant(DAGVertex oneDAGVertex, DAGVertex twoDAGVertex) {
        Vector twoDescendants = new Vector((AbstractCollection) this.getDescendants(twoDAGVertex));
        Vector oneRoots = new Vector((AbstractCollection) this.getRoots(oneDAGVertex));
        if (oneRoots.isEmpty()) {
            for (int i = 0; i < twoDescendants.size(); i++) {
                if (((DAGVertex) twoDescendants.elementAt(i)).isAncestor((DAGVertex) oneDAGVertex)) {
                    return true;
                }
                return false;
            }
        }
        for (int i = 0; i < twoDescendants.size(); i++) {
            for (int j = 0; j < oneRoots.size(); j++) {
                if (((DAGVertex) twoDescendants.elementAt(i)).isAncestor((DAGVertex) oneRoots.elementAt(j))) {
                    return true;
                }
            }
        }
        return false;
    }
}

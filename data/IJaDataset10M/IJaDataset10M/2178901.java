package ca.uwaterloo.gp.fmp.system;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.eclipse.emf.ecore.EObject;
import ca.uwaterloo.gp.fmp.Clonable;
import ca.uwaterloo.gp.fmp.ConfigState;
import ca.uwaterloo.gp.fmp.Feature;
import ca.uwaterloo.gp.fmp.FeatureGroup;
import ca.uwaterloo.gp.fmp.Node;
import ca.uwaterloo.gp.fmp.Project;

/**
 * @author Chang Hwan Peter Kim <chpkim@swen.uwaterloo.ca>,
 *         Michal Antkiewicz <mantkiew@swen.uwaterloo.ca>
 */
public class ModelNavigation {

    public static final ModelNavigation INSTANCE = new ModelNavigation();

    public static final String PATH_SEPARATOR = "/";

    /**
	 * @author: Peter
	 * This method returns the node starting at the given node 
	 * to the node pointed to the given path (of feature name). 
	 * Works only across solitary features and grouped features.
	 * Note that this method will skip any feature group in between
	 * a solitary feature and a grouped feature.
	 * Returns null if no node was found.  Use the PATH_SEPARATOR defined
	 * in this class. No PATH_SEPARATOR at the beginning of path.
	 */
    public List getNodes(Node startingNode, String path) {
        List nodes = new Vector();
        nodes.add(startingNode);
        StringTokenizer st = new StringTokenizer(path, PATH_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            List nodesFound = new Vector();
            for (Iterator nodesIterator = nodes.iterator(); nodesIterator.hasNext(); ) nodesFound.addAll(getNodesWithName(((Node) nodesIterator.next()).getChildren(), token));
            nodes.removeAll(nodes);
            nodes.addAll(nodesFound);
        }
        return nodes;
    }

    /**
	 * Returns the node with the given name amongst the featureChildren
	 * If one of the elements of featureChildren is a FeatureGroup,
	 * this method examiens its children too.
	 * @param featureChildren
	 * @param name
	 * @return
	 */
    protected List getNodesWithName(List children, String id) {
        List nodes = new Vector();
        for (Iterator iterator = children.iterator(); iterator.hasNext(); ) {
            Object obj = iterator.next();
            if (obj instanceof Clonable) {
                if (id.equals("*")) nodes.add(obj); else if (((Clonable) obj).getId() != null && ((Clonable) obj).getId().toLowerCase().indexOf(id.toLowerCase()) != -1) {
                    nodes.add(obj);
                    break;
                }
            } else if (obj instanceof FeatureGroup) {
                List groupResults = getNodesWithName(((Node) obj).getChildren(), id);
                nodes.addAll(groupResults);
                if (groupResults.size() > 0 && !id.equals("*")) break;
            }
        }
        return nodes;
    }

    /**
	 * Returns the first node (closest to the initial argument) with the given name 
	 * @author Chang Hwan Peter Kim <chpkim@swen.uwaterloo.ca>
	 */
    public Node findNodeWithName(Node node, String name) {
        Node returnNode = null;
        if ((node instanceof Feature && ((Feature) node).getName() != null && ((Feature) node).getName().equals(name))) {
            returnNode = node;
        } else {
            List children = node.getChildren();
            if (children != null) {
                for (int i = 0; i < children.size(); i++) {
                    Node aChildNode = findNodeWithName((Node) children.get(i), name);
                    if (aChildNode != null) {
                        returnNode = aChildNode;
                        break;
                    }
                }
            }
        }
        return returnNode;
    }

    /**
	 * @param node
	 * @return the root feature of a diagram containing the node.
	 */
    public Feature navigateToRootFeature(Node node) {
        if (node == null) throw new IllegalArgumentException("ModelNavigation.navigateToRootFeature(): node must not be null");
        Node curNode = node;
        if (curNode.eContainer() instanceof Project) return null;
        while (curNode != null && RoleQuery.INSTANCE.getNodeType(curNode) != RoleQuery.ROOT_FEATURE) {
            curNode = (Node) curNode.eContainer();
        }
        return (Feature) curNode;
    }

    /**
     * Given a node, this returns its grand-grand...-grand origin node. 
     * @param f
     * @return
     */
    public Node navigateToFirstOrigin(Node node) {
        if (node == null) throw new IllegalArgumentException("ModelNavigation.navigateToFirstOrigin(): node must not be null");
        Node curNode = node;
        while (curNode.getOrigin() != null) {
            curNode = curNode.getOrigin();
        }
        return curNode;
    }

    /**
     * @param clonables
     * @return nodes from the clonables list that are user or machine selected.
     */
    public List getSelectedClonables(List clonables) {
        List selectedClonables = new Vector();
        for (Iterator clonablesIterator = clonables.iterator(); clonablesIterator.hasNext(); ) {
            Object aux = clonablesIterator.next();
            if (aux instanceof Clonable) {
                Clonable clonable = (Clonable) aux;
                if (clonable.getState() == ConfigState.MACHINE_SELECTED_LITERAL || clonable.getState() == ConfigState.USER_SELECTED_LITERAL) selectedClonables.add(clonable);
            }
        }
        return selectedClonables;
    }

    /**
	 * Michal: this function retrieves a collection of all features in the model
	 */
    public Collection getAllFeatures(EObject node) {
        EObject aux = node;
        if (aux instanceof Project) return getChildrenRec(((Project) aux).getModel()); else {
            while (!(aux.eContainer() instanceof Project)) {
                aux = (Node) aux.eContainer();
            }
            return getChildrenRec(aux);
        }
    }

    private Collection getChildrenRec(EObject object) {
        LinkedList result = new LinkedList();
        Collection children = null;
        if (object instanceof Node) children = ((Node) object).getChildren();
        if (children != null) {
            for (Iterator i = children.iterator(); i.hasNext(); ) {
                EObject aux = (EObject) i.next();
                if (aux instanceof Feature) result.add(aux);
                result.addAll(getChildrenRec(aux));
            }
            return result;
        }
        return Collections.EMPTY_LIST;
    }
}

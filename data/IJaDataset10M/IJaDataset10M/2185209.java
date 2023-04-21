package unbbayes.prs.oobn.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import unbbayes.prs.INode;
import unbbayes.prs.exception.InvalidParentException;
import unbbayes.prs.oobn.IOOBNClass;
import unbbayes.prs.oobn.IOOBNNode;

/**
 * @author Shou Matsumoto
 *
 */
public class DefaultOOBNNode implements IOOBNNode {

    /** Load resource file from this package */
    private static ResourceBundle resource = unbbayes.util.ResourceController.newInstance().getBundle(unbbayes.prs.oobn.resources.Resources.class.getName());

    /** name of states. Please, use an implementation which uses equals() to compare elements or you'll experience trouble at inner instance input nodes */
    private List<String> stateNames = null;

    private Set<IOOBNNode> innerNodes = null;

    private IOOBNClass parentClass = null;

    private IOOBNNode upperInstance = null;

    private String name = null;

    private int type = TYPE_OUTPUT;

    private IOOBNNode originalClassNode = null;

    private Set<IOOBNNode> parents = null;

    private Set<IOOBNNode> children = null;

    private String description = null;

    /**
	 * 
	 */
    protected DefaultOOBNNode() {
        this.innerNodes = new HashSet<IOOBNNode>();
        this.stateNames = new ArrayList<String>();
        this.parents = new HashSet<IOOBNNode>();
        this.children = new HashSet<IOOBNNode>();
    }

    public static DefaultOOBNNode newInstance() {
        return new DefaultOOBNNode();
    }

    public IOOBNClass getParentClass() {
        return this.parentClass;
    }

    /**
	 * @param parentClass the parentClass to set
	 */
    public void setParentClass(IOOBNClass parentClass) {
        this.parentClass = parentClass;
    }

    /**
	 * @return the type
	 */
    public int getType() {
        return type;
    }

    /**
	 * This method also tests consistency for basic node types (input, output, private)
	 * @param type the type to set
	 */
    public void setType(int type) {
        if (type == TYPE_INPUT) {
            if (this.getOOBNParents().size() > 0) {
                throw new IllegalArgumentException(resource.getString("InputNodeHasNoParents"));
            }
        }
        this.type = type;
    }

    public IOOBNNode getUpperInstanceNode() {
        return this.upperInstance;
    }

    public void setUpperInstanceNode(IOOBNNode upperInstanceNode) {
        this.upperInstance = upperInstanceNode;
    }

    public void addInnerNode(IOOBNNode inner) {
        this.innerNodes.add(inner);
    }

    public Collection<IOOBNNode> getInnerNodes() {
        return this.innerNodes;
    }

    public String getName() {
        if ((this.getType() == this.TYPE_INSTANCE_INPUT) || (this.getType() == this.TYPE_INSTANCE_OUTPUT)) {
            return (this.getUpperInstanceNode().getName() + "_" + this.getOriginalClassNode().getName());
        }
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /**
	 * @return the stateNames
	 */
    public List<String> getStateNames() {
        return stateNames;
    }

    /**
	 * @param stateNames the stateNames to set
	 */
    public void setStateNames(List<String> stateNames) {
        this.stateNames = stateNames;
    }

    @Override
    public IOOBNNode clone() throws CloneNotSupportedException {
        DefaultOOBNNode clone = DefaultOOBNNode.newInstance();
        clone.setName(this.getName());
        clone.setParentClass(this.getParentClass());
        clone.setStateNames(this.getStateNames());
        clone.setType(this.getType());
        clone.setUpperInstanceNode(this.getUpperInstanceNode());
        return clone;
    }

    /**
	 * @return the originalClassNode
	 */
    public IOOBNNode getOriginalClassNode() {
        return originalClassNode;
    }

    /**
	 * @param originalClassNode the originalClassNode to set
	 */
    public void setOriginalClassNode(IOOBNNode originalClassNode) {
        this.originalClassNode = originalClassNode;
    }

    public void addParent(IOOBNNode node) {
        if ((this.getType() == this.TYPE_INPUT)) {
            throw new IllegalArgumentException(resource.getString("InputNodeHasNoParents"));
        }
        if ((this.getType() == this.TYPE_INSTANCE_OUTPUT)) {
            throw new IllegalArgumentException(resource.getString("InstanceOutputNodeHasNoParents"));
        }
        if ((this.getType() == this.TYPE_INSTANCE_INPUT)) {
            if (this.getOOBNParents().size() > 0) {
                throw new IllegalArgumentException(resource.getString("InstanceInputNodeHasNoMultipleParents"));
            }
            if (!this.getStateNames().equals(node.getStateNames())) {
                throw new IllegalArgumentException(resource.getString("InstanceInputTypeCompatibilityFailed"));
            }
        }
        if ((this.getType() == this.TYPE_INSTANCE)) {
            throw new IllegalArgumentException(resource.getString("PleaseAddParentToInstanceInputNodes"));
        }
        if ((node.getType() == node.TYPE_INSTANCE_INPUT)) {
            throw new IllegalArgumentException(resource.getString("PleaseAddChildToInstanceOutputNodes"));
        }
        this.parents.add(node);
        try {
            node.addChild(this);
        } catch (RuntimeException e) {
            this.parents.remove(node);
            throw e;
        }
    }

    public Set<IOOBNNode> getOOBNParents() {
        return this.parents;
    }

    public void addChild(IOOBNNode node) {
        if ((node.getType() == node.TYPE_INSTANCE_INPUT)) {
            if (this.getOOBNChildren() != null) {
                for (IOOBNNode child : this.getOOBNChildren()) {
                    if ((child.getType() == child.TYPE_INSTANCE_INPUT)) {
                        throw new IllegalArgumentException(resource.getString("NoNodeIsParentOf2InstanceInput"));
                    }
                }
            }
        }
        if ((this.getType() == this.TYPE_INSTANCE)) {
            throw new IllegalArgumentException(resource.getString("PleaseAddChildToInstanceOutputNodes"));
        }
        this.children.add(node);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof IOOBNNode) {
            if (this.getName().equals(((IOOBNNode) obj).getName())) {
                return true;
            }
        }
        return super.equals(obj);
    }

    public Set<IOOBNNode> getOOBNChildren() {
        return this.children;
    }

    public void addChildNode(INode child) throws InvalidParentException {
        this.addChild((IOOBNNode) child);
    }

    public void addParentNode(INode parent) throws InvalidParentException {
        this.addParent((IOOBNNode) parent);
    }

    public void appendState(String state) {
        this.getStateNames().add(state);
    }

    public List<INode> getAdjacentNodes() {
        Set<INode> ret = new HashSet<INode>();
        ret.addAll(this.getParentNodes());
        ret.addAll(this.getChildNodes());
        return new ArrayList<INode>(ret);
    }

    public List<INode> getChildNodes() {
        return new ArrayList<INode>(this.getOOBNChildren());
    }

    public String getDescription() {
        if (this.description == null) {
            if (this.getOriginalClassNode() != null) {
                return this.getOriginalClassNode().getDescription() + "." + this.getName();
            }
            return this.getName();
        }
        return this.description;
    }

    public List<INode> getParentNodes() {
        return new ArrayList<INode>(this.getOOBNParents());
    }

    public String getStateAt(int index) {
        return this.getStateNames().get(index);
    }

    public int getStatesSize() {
        return this.getStateNames().size();
    }

    public void removeChildNode(INode child) {
        this.getOOBNChildren().remove(child);
    }

    public void removeLastState() {
        if (this.getStateNames().size() > 0) {
            this.getStateNames().remove(this.getStatesSize() - 1);
        }
    }

    public void removeParentNode(INode parent) {
        this.getOOBNParents().remove(parent);
    }

    public void removeStateAt(int index) {
        this.getStateNames().remove(index);
    }

    public void setChildNodes(List<INode> children) {
        this.children = new HashSet(children);
    }

    public void setDescription(String text) {
        this.description = text;
    }

    public void setParentNodes(List<INode> parents) {
        this.parents = new HashSet(parents);
    }

    public void setStateAt(String state, int index) {
        this.getStateNames().set(index, state);
    }

    public void setStates(List<String> states) {
        this.setStateNames(states);
    }
}

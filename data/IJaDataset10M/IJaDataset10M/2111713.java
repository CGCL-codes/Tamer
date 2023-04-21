package main.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import y.base.Node;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;

/**Representation of the node in an ontology graph. The Entity
 * contains all information found in an ontology class.
 * @author Yuan An
 */
public class Entity implements OntoEle, Serializable {

    private Node refNode;

    private OWLClass refCls;

    private String name;

    private String label;

    private ArrayList<Attribute> attributes;

    private ArrayList<Entity> children;

    private ArrayList<Entity> parents;

    private ArrayList<Entity> disjoints;

    private boolean cloned;

    private boolean reified;

    private Entity clonedFrom;

    private int subscript;

    /**constructor: by passing an OWLClass from the ontology.
	 * 
	 * @param cls
	 */
    public Entity(OWLClass owlCls) {
        refNode = null;
        refCls = owlCls;
        name = owlCls.getBrowserText();
        label = name;
        attributes = new ArrayList<Attribute>();
        children = new ArrayList<Entity>();
        parents = new ArrayList<Entity>();
        disjoints = new ArrayList<Entity>();
        cloned = false;
        reified = false;
        subscript = 0;
    }

    public Entity makeCopy(int ss) {
        Entity ans = new Entity(refCls);
        ans.setCloned();
        ans.setClonedFrom(this);
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attr = attributes.get(i);
            Attribute newAttr = attr.makeCopy();
            ans.addAttribute(newAttr);
        }
        for (int i = 0; i < children.size(); i++) ans.addChild(children.get(i));
        for (int i = 0; i < parents.size(); i++) ans.addParent(parents.get(i));
        for (int i = 0; i < disjoints.size(); i++) ans.addDisjoint(disjoints.get(i));
        if (reified) ans.setReified();
        ans.setSubscript(ss);
        ans.setLabel(name + "_" + ss);
        return ans;
    }

    public Entity makeExactCopy() {
        Entity ans = new Entity(refCls);
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attr = attributes.get(i);
            Attribute newAttr = attr.makeExactCopy();
            ans.addAttribute(newAttr);
        }
        for (int i = 0; i < children.size(); i++) ans.addChild(children.get(i));
        for (int i = 0; i < parents.size(); i++) ans.addParent(parents.get(i));
        for (int i = 0; i < disjoints.size(); i++) ans.addDisjoint(disjoints.get(i));
        if (cloned) {
            ans.setCloned();
            ans.setClonedFrom(getClonedFrom());
        }
        if (reified) ans.setReified();
        ans.setSubscript(subscript);
        if (subscript == 0) ans.setLabel(name); else ans.setLabel(name + "_" + subscript);
        return ans;
    }

    /**return the attribute which corresponds to the given property
	 * 
	 * @param slot
	 * @return
	 */
    public Attribute getAttributeCorrespondingProperty(OWLProperty property) {
        Attribute ans = null;
        for (int i = 0; i < attributes.size(); i++) {
            if (property == attributes.get(i).getRefProperty()) {
                ans = attributes.get(i);
                break;
            }
        }
        return ans;
    }

    /**return the attribute which corresponds to the given name.
		 * 
		 * @param name
		 * @return
		 */
    public Attribute getAttributeCorrespondingName(String name) {
        Attribute ans = null;
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attrib = (Attribute) attributes.get(i);
            if (attrib.getName().equals(name)) {
                ans = attrib;
                break;
            }
        }
        return ans;
    }

    public Node getRefNode() {
        return refNode;
    }

    public void setRefNode(Node n) {
        refNode = n;
    }

    public OWLClass getRefCls() {
        return refCls;
    }

    public OWLClass getOWLCls() {
        return refCls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public ArrayList<Entity> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Entity> children) {
        this.children = children;
    }

    public ArrayList<Entity> getParents() {
        return parents;
    }

    public void setParents(ArrayList<Entity> parents) {
        this.parents = parents;
    }

    public ArrayList<Entity> getDisjoints() {
        return disjoints;
    }

    public void setDisjoints(ArrayList<Entity> disjoints) {
        this.disjoints = disjoints;
    }

    public boolean addChild(Entity child) {
        return children.add(child);
    }

    public boolean addAttribute(Attribute att) {
        att.setParent(this);
        return attributes.add(att);
    }

    public boolean addParent(Entity parent) {
        return parents.add(parent);
    }

    public boolean addDisjoint(Entity cls) {
        return disjoints.add(cls);
    }

    public Attribute removeAttribute(int index) {
        return attributes.remove(index);
    }

    public Entity removeParent(int index) {
        return parents.remove(index);
    }

    public Entity removeChild(int index) {
        return children.remove(index);
    }

    public Entity removeDisjoint(int index) {
        return disjoints.remove(index);
    }

    public int attributeCount() {
        return attributes.size();
    }

    public int childrenCount() {
        return children.size();
    }

    public int parentCount() {
        return parents.size();
    }

    public int disjointCount() {
        return disjoints.size();
    }

    public boolean isCloned() {
        return cloned;
    }

    public boolean isReified() {
        return reified;
    }

    public void setCloned() {
        cloned = true;
    }

    public Entity getClonedFrom() {
        return clonedFrom;
    }

    public void setClonedFrom(Entity clonedF) {
        clonedFrom = clonedF;
    }

    public void setReified() {
        reified = true;
    }

    public int getSubscript() {
        return subscript;
    }

    public void setSubscript(int subscript) {
        this.subscript = subscript;
    }

    public String display() {
        String ans = "";
        if (cloned) ans += "(Cloned)";
        if (reified) ans += "(Reified)";
        ans += "[PARENTS: ";
        for (int i = 0; i < parents.size(); i++) {
            ans += parents.get(i).getName() + ",";
        }
        ans += "]";
        if (subscript == 0) ans += getName() + "("; else ans += getLabel() + " (";
        ArrayList<Attribute> attributes = getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            Attribute att = attributes.get(i);
            ans += att.getName() + ":";
            if (att.getType() != null) ans += att.getType().getBrowserText();
            ans += ":" + att.getCardinality().getLower() + ":" + att.getCardinality().getUpper();
            if (att.isInherited()) ans += ":inHerit,"; else ans += ":Owned,";
        }
        ans += ")[CHILDREN: ";
        for (int i = 0; i < children.size(); i++) {
            ans += children.get(i).getName() + ",";
        }
        ans += "][DISJOINT: ";
        for (int i = 0; i < disjoints.size(); i++) {
            ans += disjoints.get(i).getName() + ",";
        }
        ans += "]";
        if (reified) ans += "[ReifiedRelationshipClass]";
        return ans;
    }

    public String displayAsPredicate() {
        String ans = "";
        if (subscript == 0) ans += getName() + "("; else ans += getLabel() + "(";
        ArrayList<Attribute> attributes = getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            Attribute att = attributes.get(i);
            if (i == attributes.size() - 1) ans += att.getName(); else ans += att.getName() + ",";
        }
        ans += ")";
        return ans;
    }
}

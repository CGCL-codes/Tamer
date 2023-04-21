package org.yawlfoundation.yawl.resourcing.resource.nonhuman;

import org.yawlfoundation.yawl.util.StringUtil;
import org.yawlfoundation.yawl.util.XNode;
import org.yawlfoundation.yawl.util.XNodeParser;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael Adams
 * @date 26/11/2010
 */
public class NonHumanSubCategory {

    private long _id;

    private String _name;

    private Set<NonHumanResource> _resources;

    public NonHumanSubCategory() {
        _resources = new HashSet<NonHumanResource>();
    }

    public NonHumanSubCategory(String name) {
        this();
        _name = name;
    }

    public NonHumanSubCategory(XNode node) {
        this();
        fromXNode(node);
    }

    public long getID() {
        return _id;
    }

    public void setID(long id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public Set<NonHumanResource> getResources() {
        return _resources;
    }

    public void setResources(Set<NonHumanResource> resources) {
        _resources = resources;
    }

    public boolean addResource(NonHumanResource resource) {
        return _resources.add(resource);
    }

    public boolean removeResource(NonHumanResource resource) {
        return _resources.remove(resource);
    }

    public boolean hasResource(NonHumanResource resourceToFind) {
        if (resourceToFind != null) {
            for (NonHumanResource resource : _resources) {
                String id = resource.getID();
                if ((id != null) && id.equals(resourceToFind.getID())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasResources() {
        return !_resources.isEmpty();
    }

    public String toString() {
        return String.format("NonHumanSubCategory: %s (%s)", getName(), getID());
    }

    public XNode toXNode() {
        XNode node = new XNode("nonHumanSubCategory");
        node.addAttribute("id", _id);
        node.addChild("name", _name);
        return node;
    }

    public String toXML() {
        return toXNode().toString();
    }

    public void fromXML(String xml) {
        fromXNode(new XNodeParser().parse(xml));
    }

    public void fromXNode(XNode node) {
        setID(StringUtil.strToLong(node.getAttributeValue("id"), -1));
        setName(node.getChildText("name"));
    }

    private long get_id() {
        return _id;
    }

    private void set_id(long id) {
        _id = id;
    }
}

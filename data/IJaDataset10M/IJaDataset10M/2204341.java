package org.dctmvfs.vfs.provider.dctm.client.attrs;

import java.util.ArrayList;
import java.util.List;

/**
 * An AttrGroup contains a number of attributes of a specific type and a number
 * of other AttrGroups. There is no hard binding between a type and an AttrGroup
 * because the choice for a set of attributes is application specific and might
 * depend on more than just the type. Its intended usage is that the group of
 * attributes is retrieved from the type in one query. AttrGroups can thus also
 * be used for special cases where the order of repeating attributes must be
 * synchronized. The referencing of other attribute groups can be used to create
 * inheritance and to modularize large groups of attributes.
 * 
 * @author kleij - at - users.sourceforge.net
 * 
 */
public class AttrGroup {

    private String groupName = null;

    private String groupType = null;

    private ArrayList references = new ArrayList();

    private ArrayList attributes = new ArrayList();

    public AttrGroup(String groupName, String groupType) {
        this.groupName = groupName;
        this.groupType = groupType;
    }

    public AttrGroup(String groupType) {
        this(groupType, groupType);
    }

    public String getName() {
        return this.groupName;
    }

    public String getType() {
        return this.groupType;
    }

    public void addReference(String groupName) {
        if (!references.contains(groupName)) {
            references.add(groupName);
        }
    }

    public void addReference(AttrGroup group) {
        addReference(group.getName());
    }

    public void addAttribute(AttrSpec attribute) {
        if (!attributes.contains(attribute)) {
            attributes.add(attribute);
        }
    }

    public List getReferences() {
        return this.references;
    }

    public List getAttributes() {
        return this.attributes;
    }
}

package org.opennms.netmgt.collectd;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Category;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.model.RrdRepository;

/**
 * A base (partial) implementation of CollectionResource, implementing common features (to reduce repeated code)
 * Typically used by the non-SNMP collectors (SNMP has it's own set of classes for this).  Provides a basic group of ag
 * Provides support, via addAttribute, getGroup, and getGroups, for basic "groups" of attributes.  
 * Also provides a sample "visit" implementation based on those groups, although this may well be overridden by subclasses
 * @author opennms
 *
 */
public abstract class AbstractCollectionResource implements CollectionResource {

    private Category log() {
        return ThreadCategory.getInstance(getClass());
    }

    protected CollectionAgent m_agent;

    private Map<AttributeGroupType, AttributeGroup> m_attributeGroups;

    protected AbstractCollectionResource(CollectionAgent agent) {
        m_agent = agent;
        m_attributeGroups = new HashMap<AttributeGroupType, AttributeGroup>();
    }

    public String getOwnerName() {
        return m_agent.getHostAddress();
    }

    public File getResourceDir(RrdRepository repository) {
        return new File(repository.getRrdBaseDir(), Integer.toString(m_agent.getNodeId()));
    }

    /**
     * Adds the given attribute into the collection for this resource
     * @param attr The Attribute to add
     */
    protected void addAttribute(CollectionAttribute attr) {
        AttributeGroup group = getGroup(attr.getAttributeType().getGroupType());
        log().debug("Adding attribute " + attr.getClass().getName() + ": " + attr + " to group " + group);
        group.addAttribute(attr);
    }

    /**
     * Finds, or creates, and returns the AttributeGroup for the given group Type
     * @param groupType
     * @return
     */
    protected AttributeGroup getGroup(AttributeGroupType groupType) {
        AttributeGroup group = m_attributeGroups.get(groupType);
        if (group == null) {
            group = new AttributeGroup(this, groupType);
            m_attributeGroups.put(groupType, group);
        }
        return group;
    }

    public void visit(CollectionSetVisitor visitor) {
        visitor.visitResource(this);
        for (AttributeGroup group : m_attributeGroups.values()) {
            group.visit(visitor);
        }
        visitor.completeResource(this);
    }

    public abstract int getType();

    public abstract boolean rescanNeeded();

    public abstract boolean shouldPersist(ServiceParameters params);

    public String getLabel() {
        return null;
    }
}

package org.opennms.netmgt.config.collector;

/**
 * <p>CollectionSetVisitor interface.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public interface CollectionSetVisitor {

    /**
     * <p>visitCollectionSet</p>
     *
     * @param set a {@link org.opennms.netmgt.collectd.CollectionSet} object.
     */
    void visitCollectionSet(CollectionSet set);

    /**
     * <p>visitResource</p>
     *
     * @param resource a {@link org.opennms.netmgt.config.collector.CollectionResource} object.
     */
    void visitResource(CollectionResource resource);

    /**
     * <p>visitGroup</p>
     *
     * @param group a {@link org.opennms.netmgt.collectd.AttributeGroup} object.
     */
    void visitGroup(AttributeGroup group);

    /**
     * <p>visitAttribute</p>
     *
     * @param attribute a {@link org.opennms.netmgt.collectd.CollectionAttribute} object.
     */
    void visitAttribute(CollectionAttribute attribute);

    /**
     * <p>completeAttribute</p>
     *
     * @param attribute a {@link org.opennms.netmgt.collectd.CollectionAttribute} object.
     */
    void completeAttribute(CollectionAttribute attribute);

    /**
     * <p>completeGroup</p>
     *
     * @param group a {@link org.opennms.netmgt.collectd.AttributeGroup} object.
     */
    void completeGroup(AttributeGroup group);

    /**
     * <p>completeResource</p>
     *
     * @param resource a {@link org.opennms.netmgt.config.collector.CollectionResource} object.
     */
    void completeResource(CollectionResource resource);

    /**
     * <p>completeCollectionSet</p>
     *
     * @param set a {@link org.opennms.netmgt.collectd.CollectionSet} object.
     */
    void completeCollectionSet(CollectionSet set);
}

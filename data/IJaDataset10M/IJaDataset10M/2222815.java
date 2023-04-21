package org.opennms.netmgt.protocols.xmp.collector;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.collectd.CollectionAgent;
import org.opennms.netmgt.collectd.ServiceCollector;
import org.opennms.netmgt.config.collector.CollectionSet;
import org.opennms.netmgt.config.collector.CollectionSetVisitor;

public class XmpCollectionSet implements CollectionSet {

    int status;

    boolean ignorePersistVar;

    CollectionAgent agent;

    XmpCollectionResource collectionResource;

    Set<XmpCollectionResource> listOfResources;

    private Date m_timestamp;

    XmpCollectionSet(CollectionAgent agent) {
        status = ServiceCollector.COLLECTION_SUCCEEDED;
        ignorePersistVar = false;
        this.agent = agent;
        listOfResources = new HashSet<XmpCollectionResource>();
        return;
    }

    private ThreadCategory log() {
        return ThreadCategory.getInstance(getClass());
    }

    /**
     * <p>addResource</p>
     *
     * @param aResource a {@link org.opennms.netmgt.protocols.xmp.collector.XmpCollectionResource} object.
     */
    public void addResource(XmpCollectionResource aResource) {
        listOfResources.add(aResource);
    }

    /**
     * <p>getResources</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    public Collection<XmpCollectionResource> getResources() {
        return listOfResources;
    }

    /**
     * <p>getCollectionAgent</p>
     *
     * @return a {@link org.opennms.netmgt.collectd.CollectionAgent} object.
     */
    public CollectionAgent getCollectionAgent() {
        return agent;
    }

    /**
     * <p>setCollectionAgent</p>
     *
     * @param agent a {@link org.opennms.netmgt.collectd.CollectionAgent} object.
     */
    public void setCollectionAgent(CollectionAgent agent) {
        this.agent = agent;
    }

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return a int.
     */
    public int getStatus() {
        return status;
    }

    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status a int.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * <p>setStatusSuccess</p>
     */
    public void setStatusSuccess() {
        this.status = ServiceCollector.COLLECTION_SUCCEEDED;
    }

    /**
     * <p>setStatusFailed</p>
     */
    public void setStatusFailed() {
        this.status = ServiceCollector.COLLECTION_FAILED;
    }

    /**
     * <p>ignorePersist</p>
     *
     * @return a boolean.
     */
    public boolean ignorePersist() {
        return ignorePersistVar;
    }

    /**
     * <p>ignorePersistTrue</p>
     */
    public void ignorePersistTrue() {
        ignorePersistVar = true;
    }

    /**
     * <p>ignorePersistFalse</p>
     */
    public void ignorePersistFalse() {
        ignorePersistVar = false;
    }

    /** {@inheritDoc} */
    public void visit(CollectionSetVisitor visitor) {
        log().debug("XmpCollectionSet: visit starting for set " + agent);
        visitor.visitCollectionSet(this);
        for (XmpCollectionResource resource : getResources()) {
            resource.visit(visitor);
        }
        visitor.completeCollectionSet(this);
    }

    public Date getCollectionTimestamp() {
        return m_timestamp;
    }

    public void setCollectionTimestamp(Date timestamp) {
        this.m_timestamp = timestamp;
    }
}

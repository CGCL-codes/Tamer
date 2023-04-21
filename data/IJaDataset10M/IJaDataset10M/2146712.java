package org.personalsmartspace.rms050;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.osgi.util.tracker.ServiceTracker;
import org.personalsmartspace.cm.broker.api.platform.ICtxBroker;
import org.personalsmartspace.cm.broker.api.platform.IInterCtxBroker;
import org.personalsmartspace.cm.model.api.pss3p.CtxModelType;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttribute;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntity;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntityIdentifier;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;
import org.personalsmartspace.cm.model.api.pss3p.ICtxModelObject;
import org.personalsmartspace.impl.Activator;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.pss_sm_api.api.pss3p.IServiceDiscovery;
import org.personalsmartspace.pss_sm_api.impl.PssService;

public class TestInterCtxPlatform extends TestCase {

    private static final String REMOTE_ENTITY = "remoteEntity";

    private static final String REMOTE_NAME_ATTRIBUTE = "remoteName";

    private static final String REMOTE_NAME_ATTRIBUTE_VALUE = "remoteNameValue";

    private static final String REMOVE_ATTRIBUTE = "removeAttribute";

    private static final String UPDATE_ATTRIBUTE = "updateAttribute";

    private static final String UPDATE_VALUE = "updated";

    /**
     * Context Broker service tracker
     */
    private ServiceTracker ctxBrokerPlatformTracker;

    private ServiceTracker serviceDiscoveryTracker;

    /**
     * Logger
     */
    private PSSLog log = new PSSLog(this);

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ctxBrokerPlatformTracker = new ServiceTracker(Activator.bundleContext, org.personalsmartspace.cm.broker.api.platform.ICtxBroker.class.getName(), null);
        ctxBrokerPlatformTracker.open();
        serviceDiscoveryTracker = new ServiceTracker(Activator.bundleContext, IServiceDiscovery.class.getName(), null);
        serviceDiscoveryTracker.open();
        log.info("=========================================================");
        resetTestDb();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        ctxBrokerPlatformTracker.close();
        serviceDiscoveryTracker.close();
    }

    private void resetTestDb() throws Exception {
        final List<ICtxIdentifier> identifiers = getPlatformCtxBroker().lookup(CtxModelType.ENTITY, REMOTE_ENTITY);
        if (0 == identifiers.size()) {
            log.warn("No remote entity found - Run ContextMgmt:SetupIntraPssPlatform on remote device");
        } else {
            final ICtxIdentifier entityId = identifiers.get(0);
            final ICtxEntity entity = (ICtxEntity) getPlatformCtxBroker().retrieve(entityId);
            for (ICtxAttribute attribute : entity.getAttributes()) {
                getPlatformCtxBroker().remove(attribute.getCtxIdentifier());
            }
        }
    }

    /**
     * Tests remote lookup of entity.
     * 
     * @throws Exception
     */
    public void testIntraPssLookup() throws Exception {
        final List<ICtxIdentifier> identifiers = getPlatformCtxBroker().lookup(CtxModelType.ENTITY, REMOTE_ENTITY);
        assertEquals("No identifiers found: make sure that you run ContextMgmt:SetupIntraPssPlatform on another node first", 1, identifiers.size());
        final ICtxIdentifier entityId = identifiers.get(0);
        log.info("Remote lookup returned: " + entityId);
        log.info("My Service Id = " + getPlatformCtxBroker().getLocalBrokerId());
        log.info("Remote service Id = " + entityId.getLocalServiceId());
        assertEquals("Wrong identifier type returned", CtxModelType.ENTITY, entityId.getModelType());
        assertEquals("Wrong entity type returned", REMOTE_ENTITY, entityId.getType());
        assertFalse("Home device of returned entity is this device", entityId.getLocalServiceId().equals(getPlatformCtxBroker().getLocalBrokerId()));
    }

    /**
     * Tests remote retrieval of entity.
     * 
     * @throws Exception
     */
    public void testIntraPssRetrieve() throws Exception {
        final List<ICtxIdentifier> identifiers = getPlatformCtxBroker().lookup(CtxModelType.ENTITY, REMOTE_ENTITY);
        assertEquals("No identifiers found: make sure that you run ContextMgmt:SetupIntraPssPlatform on another node first", 1, identifiers.size());
        final ICtxIdentifier entityId = identifiers.get(0);
        log.info("Remote lookup returned: " + entityId);
        final ICtxEntity entity = (ICtxEntity) getPlatformCtxBroker().retrieve(entityId);
        assertEquals("Wrong identifier type returned", CtxModelType.ENTITY, entity.getModelType());
        assertEquals("Wrong entity type returned", REMOTE_ENTITY, entity.getType());
        assertFalse("Home device of returned entity is this device", entity.getLocalServiceId().equals(getPlatformCtxBroker().getLocalBrokerId()));
    }

    /**
     * Tests create attribute.
     * 
     * @throws Exception
     */
    public void testIntraPssCreateAttribute() throws Exception {
        final List<ICtxIdentifier> identifiers = getPlatformCtxBroker().lookup(CtxModelType.ENTITY, REMOTE_ENTITY);
        assertEquals("No identifiers found: make sure that you run ContextMgmt:SetupIntraPssPlatform on another node first", 1, identifiers.size());
        final ICtxIdentifier entityId = identifiers.get(0);
        log.info("Remote lookup returned: " + entityId);
        final ICtxAttribute attribute = getPlatformCtxBroker().createAttribute((ICtxEntityIdentifier) entityId, REMOTE_NAME_ATTRIBUTE);
        assertEquals("Wrong identifier type returned", CtxModelType.ATTRIBUTE, attribute.getModelType());
        assertEquals("Wrong attribute type returned", REMOTE_NAME_ATTRIBUTE, attribute.getType());
        assertFalse("Home device of returned attribute is this device", attribute.getLocalServiceId().equals(getPlatformCtxBroker().getLocalBrokerId()));
    }

    public void testIntraPssRemove() throws Exception {
        ICtxEntity entity;
        final List<ICtxIdentifier> identifiers = getPlatformCtxBroker().lookup(CtxModelType.ENTITY, REMOTE_ENTITY);
        assertEquals("No identifiers found: make sure that you run ContextMgmt:SetupIntraPssPlatform on another node first", 1, identifiers.size());
        final ICtxEntityIdentifier entityId = (ICtxEntityIdentifier) identifiers.get(0);
        log.info("Remote lookup returned: " + entityId);
        getPlatformCtxBroker().createAttribute(entityId, REMOVE_ATTRIBUTE);
        entity = (ICtxEntity) getPlatformCtxBroker().retrieve(entityId);
        assertEquals(1, entity.getAttributes(REMOVE_ATTRIBUTE).size());
        final Iterator<ICtxAttribute> attributes = entity.getAttributes(REMOVE_ATTRIBUTE).iterator();
        final ICtxAttribute removeAttribute = attributes.next();
        final ICtxModelObject mo = getPlatformCtxBroker().remove(removeAttribute.getCtxIdentifier());
        entity = (ICtxEntity) getPlatformCtxBroker().retrieve(entityId);
        assertEquals("Attribute not removed", 0, entity.getAttributesSize());
        assertEquals("Incorrect model object returned", removeAttribute.getCtxIdentifier(), mo.getCtxIdentifier());
    }

    public void testIntraPssUpdate() throws Exception {
        ICtxEntity entity;
        Iterator<ICtxAttribute> attributes;
        final List<ICtxIdentifier> identifiers = getPlatformCtxBroker().lookup(CtxModelType.ENTITY, REMOTE_ENTITY);
        assertEquals("No identifiers found: make sure that you run ContextMgmt:SetupIntraPssPlatform on another node first", 1, identifiers.size());
        final ICtxEntityIdentifier entityId = (ICtxEntityIdentifier) identifiers.get(0);
        log.info("Remote lookup returned: " + entityId);
        getPlatformCtxBroker().createAttribute(entityId, UPDATE_ATTRIBUTE);
        entity = (ICtxEntity) getPlatformCtxBroker().retrieve(entityId);
        assertEquals(1, entity.getAttributes(UPDATE_ATTRIBUTE).size());
        attributes = entity.getAttributes(UPDATE_ATTRIBUTE).iterator();
        final ICtxAttribute updateAttribute = attributes.next();
        assertEquals(null, updateAttribute.getStringValue());
        updateAttribute.setStringValue(UPDATE_VALUE);
        final ICtxModelObject mo = getPlatformCtxBroker().update(updateAttribute);
        entity = (ICtxEntity) getPlatformCtxBroker().retrieve(entityId);
        attributes = entity.getAttributes(UPDATE_ATTRIBUTE).iterator();
        final ICtxAttribute updated = attributes.next();
        assertEquals("Attribute not updated", UPDATE_VALUE, updated.getStringValue());
        assertEquals("Incorrect model object returned", updated.getCtxIdentifier(), mo.getCtxIdentifier());
        assertEquals("Returned model object not updated", UPDATE_VALUE, ((ICtxAttribute) mo).getStringValue());
    }

    /**
     * Gets an instance of an {@link ICtxBroker} service.
     * 
     * @return  An instance of {@link ICtxBroker}
     */
    private ICtxBroker getPlatformCtxBroker() {
        return (ICtxBroker) ctxBrokerPlatformTracker.getService();
    }

    /**
     * Gets an instance of the service discovery service.
     * 
     * @return  An instance of {@link IServiceDiscovery}
     */
    private IServiceDiscovery getServiceDiscovery() {
        return (IServiceDiscovery) serviceDiscoveryTracker.getService();
    }

    /**
     * Returns the first remote instance of {@link ICtxBroker} that is discovered
     * 
     * @return First remote instance of {@link ICtxBroker}.
     * 
     */
    private PssService getTargetService() throws Exception {
        PssService target = null;
        final Collection<PssService> services = getServiceDiscovery().findByServiceType(IInterCtxBroker.class.getName());
        if (null != services) {
            for (PssService service : services) {
                if (!service.getServiceId().getLocalServiceId().equals(getPlatformCtxBroker().getLocalBrokerId())) {
                    log.debug("REMOTE (INTERNAL) CTX BROKER" + " : " + service.getServiceId() + " [ " + service.getPeerId() + "]");
                    target = service;
                    break;
                }
            }
        }
        return target;
    }
}

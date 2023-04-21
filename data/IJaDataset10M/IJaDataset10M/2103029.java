package org.neodatis.odb.core;

import org.neodatis.odb.OID;
import org.neodatis.odb.core.layers.layer1.ClassIntrospector;
import org.neodatis.odb.core.layers.layer1.ObjectIntrospector;
import org.neodatis.odb.core.layers.layer2.instance.IClassPool;
import org.neodatis.odb.core.layers.layer2.instance.InstanceBuilder;
import org.neodatis.odb.core.layers.layer3.ByteArrayConverter;
import org.neodatis.odb.core.layers.layer4.IBaseIdentification;
import org.neodatis.odb.core.layers.layer4.IOFileParameter;
import org.neodatis.odb.core.layers.layer4.OidProvider;
import org.neodatis.odb.core.query.IMatchingObjectAction;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.refactor.RefactorManager;
import org.neodatis.odb.core.session.Session;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.core.trigger.TriggerManager;

/**
 * This is the default Core Object Provider.
 * 
 * 
 * @author olivier
 *
 */
public interface CoreProvider extends ITwoPhaseInit {

    ByteArrayConverter getByteArrayConverter();

    /**
	 * Returns the Local Instance Builder
	 */
    public InstanceBuilder getLocalInstanceBuilder(Session session, ClassIntrospector classIntrospector, TriggerManager triggerManager);

    public InstanceBuilder getServerInstanceBuilder(Session session, ClassIntrospector classIntrospector, TriggerManager triggerManager);

    public ObjectIntrospector getLocalObjectIntrospector(Session session, ClassIntrospector classIntrospector, OidProvider oidProvider);

    public TriggerManager getLocalTriggerManager(SessionEngine engine);

    public ClassIntrospector getClassIntrospector(Session session, OidProvider oidProvider);

    public RefactorManager getRefactorManager(SessionEngine engine);

    /** Returns the query result handler for normal query result (that return a collection of objects)
	 * 
	 */
    public IMatchingObjectAction getCollectionQueryResultAction(SessionEngine engine, IQuery query, boolean inMemory, boolean returnObjects);

    public IClassPool getClassPool();

    public void resetClassDefinitions();

    public void removeLocalTriggerManager(SessionEngine engine);

    OID buildOid(String soid);

    /**
	 * @param inMemoryLayer4
	 */
    void setLayer4Class(Class layer4Class);

    public Class getLayer4Class();

    /**
	 * @param fileParameter
	 * @return
	 */
    SessionEngine getSessionEngine(IBaseIdentification baseIdentification);
}

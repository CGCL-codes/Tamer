package com.google.appengine.datanucleus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.NucleusContext;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.DiscriminatorStrategy;
import org.datanucleus.metadata.FieldRole;
import org.datanucleus.metadata.OrderMetaData;
import org.datanucleus.metadata.Relation;
import org.datanucleus.util.NucleusLogger;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortPredicate;

/**
 * Migration tool for moving from GAE v1 StorageVersion (children identified by parent key) to
 * GAE v2 StorageVersion (child keys stored in parents).
 * There are two ways that this can be used. 
 * The first (preferred) method will allow you to migrate an Entity at a time and so could be enabled 
 * using the Mapper API - see http://code.google.com/p/appengine-mapreduce/
 * <pre>
 * NucleusContext nucCtx = ((JDOPersistenceManagerFactory)pmf).getNucleusContext(); // For JDO
 * // NucleusContext nucCtx = ((JPAEntityManagerFactory)emf).getNucleusContext(); // For JPA
 * Migrator migrator = new Migrator(nucCtx);
 * migrator.migrate(entity, MyEntity.class);
 * </pre>
 * The second (alternative) method is for a bulk migration of a Collection of Entities of a type.
 * <pre>
 * NucleusContext nucCtx = ((JDOPersistenceManagerFactory)pmf).getNucleusContext(); // For JDO
 * // NucleusContext nucCtx = ((JPAEntityManagerFactory)emf).getNucleusContext(); // For JPA
 * Migrator.migrate(nucCtx, MyEntity.class, entityIter);
 * </pre>
 */
public class Migrator {

    NucleusContext nucCtx;

    public Migrator(NucleusContext nucCtx) {
        this.nucCtx = nucCtx;
    }

    /**
   * Method to migrate the provided Entity of the specified class.
   * Note that this updates the passed in Entity as required to include keys of related objects.
   * If it is isn't updated to migrate it then returns false.
   * Also returns false if the provided Entity doesn't represent an object of the required class.
   * @param entity The entity
   * @param cls The pojo class that this represents
   * @return Whether the Entity is updated (needs PUTting)
   */
    public boolean migrate(Entity entity, Class cls) {
        DatastoreManager storeMgr = (DatastoreManager) nucCtx.getStoreManager();
        ClassLoaderResolver clr = nucCtx.getClassLoaderResolver(null);
        AbstractClassMetaData cmd = nucCtx.getMetaDataManager().getMetaDataForClass(cls, clr);
        Collection<String> mdClasses = nucCtx.getMetaDataManager().getClassesWithMetaData();
        storeMgr.addClasses(mdClasses.toArray(new String[mdClasses.size()]), clr);
        if (cmd.hasDiscriminatorStrategy()) {
            String disProp = EntityUtils.getDiscriminatorPropertyName(storeMgr.getIdentifierFactory(), cmd.getDiscriminatorMetaDataForTable());
            if (disProp != null && entity.hasProperty(disProp)) {
                DiscriminatorStrategy discStr = cmd.getDiscriminatorStrategyForTable();
                String discValExpected = cls.getName();
                if (discStr == DiscriminatorStrategy.VALUE_MAP) {
                    discValExpected = (String) cmd.getDiscriminatorValue();
                }
                String discValActual = (String) entity.getProperty(disProp);
                if (discValActual != null && discValExpected != null && !discValActual.equals(discValExpected)) {
                    NucleusLogger.DATASTORE.info("Attempt to migrate " + entity + " as being of type " + cls.getName() + " but discriminator implies of different type");
                    return false;
                }
            }
        }
        return migrateEntity(nucCtx, cls, entity, cmd, clr, storeMgr);
    }

    /**
   * Method to migrate the provided Entities of the specified class.
   * Does a single PUT of all changed Entity objects.
   * @param nucCtx NucleusContext
   * @param cls The pojo class that these Entity objects represent
   * @param iter Iterator for the Entity objects
   */
    public static void migrate(NucleusContext nucCtx, Class cls, Iterable<Entity> iter) {
        DatastoreManager storeMgr = (DatastoreManager) nucCtx.getStoreManager();
        ClassLoaderResolver clr = nucCtx.getClassLoaderResolver(null);
        AbstractClassMetaData cmd = nucCtx.getMetaDataManager().getMetaDataForClass(cls, clr);
        Collection<String> mdClasses = nucCtx.getMetaDataManager().getClassesWithMetaData();
        storeMgr.addClasses(mdClasses.toArray(new String[mdClasses.size()]), clr);
        Set<Entity> changedEntities = new HashSet<Entity>();
        int[] relationFieldNumbers = cmd.getRelationMemberPositions(clr, nucCtx.getMetaDataManager());
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        if (relationFieldNumbers != null && relationFieldNumbers.length > 0) {
            for (Entity entity : iter) {
                boolean changed = migrateEntity(nucCtx, cls, entity, cmd, clr, storeMgr);
                if (changed) {
                    changedEntities.add(entity);
                }
            }
        }
        if (!changedEntities.isEmpty()) {
            NucleusLogger.DATASTORE_NATIVE.debug("Putting " + changedEntities.size() + " entities of class " + cls.getName());
            datastore.put(changedEntities);
        }
    }

    /**
   * Convenience method to migrate an Entity. Adds any necessary properties containing the child keys.
   * Returns whether it has been updated. The Entity is not PUT in the datastore in this method.
   * @param nucCtx NucleusContext
   * @param cls Class
   * @param entity Entity
   * @param cmd Metadata for the class
   * @param clr ClassLoader resolver
   * @param storeMgr Store Manager
   * @return Whether the entity is updated (ready for PUTting)
   */
    protected static boolean migrateEntity(NucleusContext nucCtx, Class cls, Entity entity, AbstractClassMetaData cmd, ClassLoaderResolver clr, DatastoreManager storeMgr) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        boolean changed = false;
        int[] relationFieldNumbers = cmd.getRelationMemberPositions(clr, nucCtx.getMetaDataManager());
        if (relationFieldNumbers == null || relationFieldNumbers.length == 0) {
            return false;
        }
        NucleusLogger.DATASTORE.info("Migrating Entity with key=" + entity.getKey() + " for class=" + cls.getName());
        for (int i = 0; i < relationFieldNumbers.length; i++) {
            AbstractMemberMetaData mmd = cmd.getMetaDataForManagedMemberAtAbsolutePosition(relationFieldNumbers[i]);
            if (MetaDataUtils.isOwnedRelation(mmd)) {
                int relationType = mmd.getRelationType(clr);
                if (relationType == Relation.ONE_TO_ONE_UNI || (relationType == Relation.ONE_TO_ONE_BI && mmd.getMappedBy() == null)) {
                    String propName = EntityUtils.getPropertyName(storeMgr.getIdentifierFactory(), mmd);
                    if (!entity.hasProperty(propName)) {
                        AbstractClassMetaData relCmd = nucCtx.getMetaDataManager().getMetaDataForClass(mmd.getTypeName(), clr);
                        String relKindName = EntityUtils.getKindName(storeMgr.getIdentifierFactory(), relCmd);
                        Query q2 = new Query(relKindName, entity.getKey());
                        PreparedQuery pq2 = datastore.prepare(q2);
                        Object value = null;
                        for (Entity childEntity : pq2.asIterable()) {
                            if (entity.getKey().equals(childEntity.getKey().getParent())) {
                                value = childEntity.getKey();
                                break;
                            }
                        }
                        changed = true;
                        entity.setProperty(propName, value);
                    }
                } else if (relationType == Relation.ONE_TO_MANY_UNI || relationType == Relation.ONE_TO_MANY_BI) {
                    String propName = EntityUtils.getPropertyName(storeMgr.getIdentifierFactory(), mmd);
                    if (!entity.hasProperty(propName)) {
                        AbstractClassMetaData childCmd = mmd.getCollection().getElementClassMetaData(clr, nucCtx.getMetaDataManager());
                        String childKindName = EntityUtils.getKindName(storeMgr.getIdentifierFactory(), childCmd);
                        Query q = new Query(childKindName, entity.getKey());
                        if (List.class.isAssignableFrom(mmd.getType())) {
                            List<Query.SortPredicate> sortPredicates = Utils.newArrayList();
                            boolean indexedList = true;
                            if (mmd.getOrderMetaData() != null && !mmd.getOrderMetaData().isIndexedList()) {
                                indexedList = false;
                            }
                            if (indexedList) {
                                String indexPropName = null;
                                OrderMetaData ordmd = mmd.getOrderMetaData();
                                if (ordmd != null) {
                                    String colName;
                                    if (ordmd.getColumnMetaData() != null && ordmd.getColumnMetaData().length > 0 && ordmd.getColumnMetaData()[0].getName() != null) {
                                        colName = ordmd.getColumnMetaData()[0].getName();
                                        indexPropName = storeMgr.getIdentifierFactory().newDatastoreFieldIdentifier(colName).getIdentifierName();
                                    }
                                }
                                if (indexPropName == null) {
                                    indexPropName = storeMgr.getIdentifierFactory().newForeignKeyFieldIdentifier(mmd, null, null, true, FieldRole.ROLE_INDEX).getIdentifierName();
                                }
                                Query.SortPredicate sortPredicate = new Query.SortPredicate(indexPropName, Query.SortDirection.ASCENDING);
                                sortPredicates.add(sortPredicate);
                            } else {
                                for (OrderMetaData.FieldOrder fieldOrder : mmd.getOrderMetaData().getFieldOrders()) {
                                    String orderMemberName = fieldOrder.getFieldName();
                                    AbstractMemberMetaData orderMemberMmd = childCmd.getMetaDataForMember(orderMemberName);
                                    String orderPropName = EntityUtils.getPropertyName(storeMgr.getIdentifierFactory(), orderMemberMmd);
                                    boolean isPrimaryKey = orderMemberMmd.isPrimaryKey();
                                    if (isPrimaryKey) {
                                        if (fieldOrder.isForward() && sortPredicates.isEmpty()) {
                                            break;
                                        }
                                        orderPropName = Entity.KEY_RESERVED_PROPERTY;
                                    }
                                    Query.SortPredicate sortPredicate = new Query.SortPredicate(orderPropName, fieldOrder.isForward() ? Query.SortDirection.ASCENDING : Query.SortDirection.DESCENDING);
                                    sortPredicates.add(sortPredicate);
                                    if (isPrimaryKey) {
                                        break;
                                    }
                                }
                            }
                            for (SortPredicate sp : sortPredicates) {
                                q.addSort(sp.getPropertyName(), sp.getDirection());
                            }
                        }
                        PreparedQuery pq = datastore.prepare(q);
                        List<Key> values = new ArrayList<Key>();
                        for (Entity childEntity : pq.asIterable()) {
                            if (entity.getKey().equals(childEntity.getKey().getParent())) {
                                values.add(childEntity.getKey());
                            }
                        }
                        changed = true;
                        entity.setProperty(propName, values);
                    }
                }
            }
        }
        NucleusLogger.DATASTORE.info("Migration of Entity with key=" + entity.getKey() + (changed ? " has been performed" : " didn't need any changes to the Entity"));
        return changed;
    }
}

package com.avaje.ebean.server.deploy;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.Query;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.cache.ServerCache;
import com.avaje.ebean.cache.ServerCacheManager;
import com.avaje.ebean.config.dbplatform.IdGenerator;
import com.avaje.ebean.config.dbplatform.IdType;
import com.avaje.ebean.event.BeanFinder;
import com.avaje.ebean.event.BeanPersistController;
import com.avaje.ebean.event.BeanPersistListener;
import com.avaje.ebean.internal.SpiEbeanServer;
import com.avaje.ebean.internal.SpiQuery;
import com.avaje.ebean.internal.TransactionEventTable.TableIUD;
import com.avaje.ebean.server.core.ConcurrencyMode;
import com.avaje.ebean.server.core.InternString;
import com.avaje.ebean.server.core.ReferenceOptions;
import com.avaje.ebean.server.deploy.id.IdBinder;
import com.avaje.ebean.server.deploy.id.IdBinderFactory;
import com.avaje.ebean.server.deploy.meta.DeployBeanDescriptor;
import com.avaje.ebean.server.deploy.meta.DeployBeanPropertyLists;
import com.avaje.ebean.server.el.ElComparator;
import com.avaje.ebean.server.el.ElComparatorCompound;
import com.avaje.ebean.server.el.ElComparatorProperty;
import com.avaje.ebean.server.el.ElPropertyChainBuilder;
import com.avaje.ebean.server.el.ElPropertyDeploy;
import com.avaje.ebean.server.el.ElPropertyValue;
import com.avaje.ebean.server.query.CQueryPlan;
import com.avaje.ebean.server.querydefn.OrmQueryDetail;
import com.avaje.ebean.server.reflect.BeanReflect;
import com.avaje.ebean.server.type.TypeManager;
import com.avaje.ebean.util.SortByClause;
import com.avaje.ebean.util.SortByClauseParser;
import com.avaje.ebean.util.SortByClause.Property;
import com.avaje.ebean.validation.factory.Validator;

/**
 * Describes Beans including their deployment information.
 */
public class BeanDescriptor<T> {

    private static final Logger logger = Logger.getLogger(BeanDescriptor.class.getName());

    private final ConcurrentHashMap<Integer, CQueryPlan> queryPlanCache = new ConcurrentHashMap<Integer, CQueryPlan>();

    private final ConcurrentHashMap<String, ElPropertyValue> elGetCache = new ConcurrentHashMap<String, ElPropertyValue>();

    private final ConcurrentHashMap<String, ElComparator<T>> comparatorCache = new ConcurrentHashMap<String, ElComparator<T>>();

    private final ConcurrentHashMap<String, BeanFkeyProperty> fkeyMap = new ConcurrentHashMap<String, BeanFkeyProperty>();

    /**
	 * The EbeanServer name. Same as the plugin name.
	 */
    private final String serverName;

    /**
	 * Type of Identity generation strategy used.
	 */
    private final IdType idType;

    private final IdGenerator idGenerator;

    /**
	 * The name of an IdGenerator (optional).
	 */
    private final String idGeneratorName;

    /**
	 * The database sequence next value (optional).
	 */
    private final String sequenceNextVal;

    /**
	 * The database sequence name (optional).
	 */
    private final String sequenceName;

    /**
	 * SQL used to return last inserted id.
	 * Used for Identity columns where getGeneratedKeys is not supported.
	 */
    private final String selectLastInsertedId;

    /**
	 * True if this is Table based for TableBeans.
	 */
    private final boolean tableGenerated;

    /**
	 * True if this is an Embedded bean.
	 */
    private final boolean embedded;

    /**
	 * True if this is a Meta bean.
	 */
    private final boolean meta;

    private final boolean autoFetchTunable;

    private final String lazyFetchIncludes;

    /**
	 * The concurrency mode for beans of this type.
	 */
    private final ConcurrencyMode concurrencyMode;

    /**
	 * The tables this bean is dependent on.
	 */
    private final String[] dependantTables;

    /**
	 * Extra deployment attributes.
	 */
    private final Map<String, String> extraAttrMap;

    /**
	 * The base database table.
	 */
    private final String baseTable;

    /**
	 * True if based on a table (or view) false if based on a raw sql select
	 * statement.
	 */
    private final boolean sqlSelectBased;

    /**
	 * Used to provide mechanism to new EntityBean instances. Generated code
	 * faster than reflection at this stage.
	 */
    private final BeanReflect beanReflect;

    /**
	 * Map of BeanProperty Linked so as to preserve order.
	 */
    private final LinkedHashMap<String, BeanProperty> propMap;

    /**
	 * The type of bean this describes.
	 */
    private final Class<T> beanType;

    /**
	 * This is not sent to a remote client.
	 */
    private final BeanDescriptorMap owner;

    /**
	 * The EntityBean type used to create new EntityBeans.
	 */
    private final Class<?> factoryType;

    /**
	 * Intercept pre post on insert,update,delete and postLoad(). Server side
	 * only.
	 */
    private volatile BeanPersistController persistController;

    /**
	 * Listens for post commit insert update and delete events.
	 */
    private volatile BeanPersistListener<T> persistListener;

    /**
	 * If set overrides the find implementation. Server side only.
	 */
    private final BeanFinder<T> beanFinder;

    /**
	 * The table joins for this bean.
	 */
    private final TableJoin[] derivedTableJoins;

    /**
	 * Inheritance information. Server side only.
	 */
    private final InheritInfo inheritInfo;

    /**
	 * Derived list of properties that make up the unique id.
	 */
    private final BeanProperty[] propertiesId;

    /**
	 * Derived list of properties that are used for version concurrency
	 * checking.
	 */
    private final BeanProperty[] propertiesVersion;

    /**
	 * Properties local to this type (not from a super type).
	 */
    private final BeanProperty[] propertiesLocal;

    private final BeanPropertyAssocOne<?> unidirectional;

    /**
	 * A hashcode of all the many property names.
	 * This is used to efficiently create sets of 
	 * loaded property names (for partial objects).
	 */
    private final int namesOfManyPropsHash;

    /**
	 * The set of names of the many properties.
	 */
    private final Set<String> namesOfManyProps;

    /**
	 * list of properties that are Lists/Sets/Maps (Derived).
	 */
    private final BeanPropertyAssocMany<?>[] propertiesMany;

    private final BeanPropertyAssocMany<?>[] propertiesManySave;

    private final BeanPropertyAssocMany<?>[] propertiesManyDelete;

    /**
	 * list of properties that are associated beans and not embedded (Derived).
	 */
    private final BeanPropertyAssocOne<?>[] propertiesOne;

    private final BeanPropertyAssocOne<?>[] propertiesOneImported;

    private final BeanPropertyAssocOne<?>[] propertiesOneImportedSave;

    private final BeanPropertyAssocOne<?>[] propertiesOneImportedDelete;

    private final BeanPropertyAssocOne<?>[] propertiesOneExported;

    private final BeanPropertyAssocOne<?>[] propertiesOneExportedSave;

    private final BeanPropertyAssocOne<?>[] propertiesOneExportedDelete;

    /**
	 * list of properties that are embedded beans.
	 */
    private final BeanPropertyAssocOne<?>[] propertiesEmbedded;

    /**
	 * List of the scalar properties excluding id and secondary table properties.
	 */
    private final BeanProperty[] propertiesBaseScalar;

    private final BeanProperty[] propertiesTransient;

    /**
	 * Set to true if the bean has version properties or an embedded bean has
	 * version properties.
	 */
    private final BeanProperty propertyFirstVersion;

    /**
	 * Set when the Id property is a single non-embedded property. Can make life
	 * simpler for this case.
	 */
    private final BeanProperty propertySingleId;

    /**
	 * The bean class name or the table name for MapBeans.
	 */
    private final String fullName;

    private final Map<String, DeployNamedQuery> namedQueries;

    private final Map<String, DeployNamedUpdate> namedUpdates;

    /**
	 * Logical to physical deployment mapping for use with updates.
	 * <p>
	 * Maps bean properties to db columns and the bean name to base table.
	 * </p>
	 */
    private Map<String, String> updateDeployMap;

    /**
	 * Has local validation rules.
	 */
    private final boolean hasLocalValidation;

    /**
	 * Has local or recursive validation rules.
	 */
    private final boolean hasCascadeValidation;

    /**
	 * Properties with local validation rules.
	 */
    private final BeanProperty[] propertiesValidationLocal;

    /**
	 * Properties with local or cascade validation rules.
	 */
    private final BeanProperty[] propertiesValidationCascade;

    private final Validator[] beanValidators;

    /**
	 * Flag used to determine if saves can be skipped.
	 */
    private final boolean saveRecurseSkippable;

    /**
	 * Flag used to determine if deletes can be skipped.
	 */
    private final boolean deleteRecurseSkippable;

    /**
	 * Make the TypeManager available for helping SqlSelect.
	 */
    private final TypeManager typeManager;

    private final IdBinder idBinder;

    private final String name;

    private final String baseTableAlias;

    /**
	 * If true then only changed properties get updated.
	 */
    private final boolean updateChangesOnly;

    private final ServerCacheManager cacheManager;

    private final ReferenceOptions referenceOptions;

    private SpiEbeanServer ebeanServer;

    private ServerCache beanCache;

    private ServerCache queryCache;

    /**
	 * Construct the BeanDescriptor.
	 */
    public BeanDescriptor(BeanDescriptorMap owner, TypeManager typeManager, DeployBeanDescriptor<T> deploy) {
        this.owner = owner;
        this.cacheManager = owner.getCacheManager();
        this.serverName = owner.getServerName();
        this.name = InternString.intern(deploy.getName());
        this.baseTableAlias = InternString.intern(name.substring(0, 1).toLowerCase());
        this.fullName = InternString.intern(deploy.getFullName());
        this.typeManager = typeManager;
        this.beanType = deploy.getBeanType();
        this.factoryType = deploy.getFactoryType();
        this.namedQueries = deploy.getNamedQueries();
        this.namedUpdates = deploy.getNamedUpdates();
        this.inheritInfo = deploy.getInheritInfo();
        this.beanFinder = deploy.getBeanFinder();
        this.persistController = deploy.getPersistController();
        this.persistListener = deploy.getPersistListener();
        this.referenceOptions = deploy.getReferenceOptions();
        this.idType = deploy.getIdType();
        this.idGeneratorName = InternString.intern(deploy.getIdGeneratorName());
        this.idGenerator = deploy.getIdGenerator();
        this.sequenceName = deploy.getSequenceName();
        this.sequenceNextVal = deploy.getSequenceNextVal();
        this.selectLastInsertedId = deploy.getSelectLastInsertedId();
        this.tableGenerated = deploy.isTableGenerated();
        this.embedded = deploy.isEmbedded();
        this.meta = deploy.isMeta();
        this.lazyFetchIncludes = InternString.intern(deploy.getLazyFetchIncludes());
        this.concurrencyMode = deploy.getConcurrencyMode();
        this.updateChangesOnly = deploy.isUpdateChangesOnly();
        this.dependantTables = deploy.getDependantTables();
        this.extraAttrMap = deploy.getExtraAttributeMap();
        this.baseTable = InternString.intern(deploy.getBaseTable());
        this.sqlSelectBased = deploy.isSqlSelectBased();
        this.beanReflect = deploy.getBeanReflect();
        this.autoFetchTunable = !tableGenerated && !embedded && !sqlSelectBased && (beanFinder == null);
        DeployBeanPropertyLists listHelper = new DeployBeanPropertyLists(owner, this, deploy);
        this.propMap = listHelper.getPropertyMap();
        this.propertiesTransient = listHelper.getTransients();
        this.propertiesBaseScalar = listHelper.getBaseScalar();
        this.propertiesId = listHelper.getId();
        this.propertiesVersion = listHelper.getVersion();
        this.propertiesEmbedded = listHelper.getEmbedded();
        this.propertiesLocal = listHelper.getLocal();
        this.unidirectional = listHelper.getUnidirectional();
        this.propertiesOne = listHelper.getOnes();
        this.propertiesOneExported = listHelper.getOneExported();
        this.propertiesOneExportedSave = listHelper.getOneExportedSave();
        this.propertiesOneExportedDelete = listHelper.getOneExportedDelete();
        this.propertiesOneImported = listHelper.getOneImported();
        this.propertiesOneImportedSave = listHelper.getOneImportedSave();
        this.propertiesOneImportedDelete = listHelper.getOneImportedDelete();
        this.propertiesMany = listHelper.getMany();
        this.propertiesManySave = listHelper.getManySave();
        this.propertiesManyDelete = listHelper.getManyDelete();
        this.namesOfManyProps = deriveManyPropNames();
        this.namesOfManyPropsHash = namesOfManyProps.hashCode();
        this.derivedTableJoins = listHelper.getTableJoin();
        this.propertyFirstVersion = listHelper.getFirstVersion();
        if (propertiesId.length == 1) {
            this.propertySingleId = propertiesId[0];
        } else {
            this.propertySingleId = null;
        }
        saveRecurseSkippable = (0 == (propertiesOneExportedSave.length + propertiesOneImportedSave.length + propertiesManySave.length));
        deleteRecurseSkippable = (0 == (propertiesOneExportedDelete.length + propertiesOneImportedDelete.length + propertiesManyDelete.length));
        this.propertiesValidationLocal = listHelper.getPropertiesWithValidators(false);
        this.propertiesValidationCascade = listHelper.getPropertiesWithValidators(true);
        this.beanValidators = listHelper.getBeanValidators();
        this.hasLocalValidation = (propertiesValidationLocal.length > 0 || beanValidators.length > 0);
        this.hasCascadeValidation = (propertiesValidationCascade.length > 0 || beanValidators.length > 0);
        this.idBinder = IdBinderFactory.createIdBinder(propertiesId);
    }

    /**
	 * Set the server. Primarily so that the Many's can lazy load.
	 */
    public void setEbeanServer(SpiEbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
        for (int i = 0; i < propertiesMany.length; i++) {
            propertiesMany[i].setEbeanServer(ebeanServer);
        }
    }

    /**
	 * Create a copy of this bean.
	 * <p>
	 * Originally for the purposes of returning a copy (rather than a shared
	 * instance) from the cache - where the app may want to change the data.
	 * </p>
	 */
    @SuppressWarnings("unchecked")
    public T createCopy(Object bean) {
        EntityBean orig = ((EntityBean) bean);
        EntityBean copy = (EntityBean) orig._ebean_createCopy();
        EntityBeanIntercept origEbi = orig._ebean_getIntercept();
        EntityBeanIntercept copyEbi = copy._ebean_getIntercept();
        origEbi.copyStateTo(copyEbi);
        return (T) copy;
    }

    /**
	 * Return the EbeanServer instance that owns this BeanDescriptor.
	 */
    public SpiEbeanServer getEbeanServer() {
        return ebeanServer;
    }

    /**
	 * Initialise the Id properties first.
	 * <p>
	 * These properties need to be initialised prior to the association properties 
	 * as they are used to get the imported and exported properties.
	 * </p>
	 */
    public void initialiseId() {
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("BeanDescriptor initialise " + fullName);
        }
        if (inheritInfo != null) {
            inheritInfo.setDescriptor(this);
        }
        if (embedded) {
            Iterator<BeanProperty> it = propertiesAll();
            while (it.hasNext()) {
                BeanProperty prop = it.next();
                prop.initialise();
            }
        } else {
            BeanProperty[] idProps = propertiesId();
            for (int i = 0; i < idProps.length; i++) {
                idProps[i].initialise();
            }
        }
    }

    /**
	 * Initialise the exported and imported parts for associated properties.
	 */
    public void initialiseOther() {
        if (!embedded) {
            Iterator<BeanProperty> it = propertiesAll();
            while (it.hasNext()) {
                BeanProperty prop = it.next();
                if (!prop.isId()) {
                    prop.initialise();
                }
            }
        }
        if (unidirectional != null) {
            unidirectional.initialise();
        }
        idBinder.initialise();
        if (tableGenerated || embedded) {
        } else {
            updateDeployMap = DeployUpdateMapFactory.build(this);
            for (DeployNamedUpdate namedUpdate : namedUpdates.values()) {
                DeployUpdateParser parser = new DeployUpdateParser(this);
                namedUpdate.initialise(parser);
            }
        }
    }

    /**
	 * Add objects to ElPropertyDeploy etc. These are used so that 
	 * expressions on foreign keys don't require an extra join.
	 */
    public void add(BeanFkeyProperty fkey) {
        fkeyMap.put(fkey.getName(), fkey);
    }

    public void initialiseFkeys() {
        for (int i = 0; i < propertiesOneImported.length; i++) {
            propertiesOneImported[i].addFkey();
        }
    }

    public boolean calculateUseCache(Boolean queryUseCache) {
        if (queryUseCache != null) {
            return queryUseCache;
        } else {
            if (referenceOptions != null) {
                return referenceOptions.isUseCache();
            } else {
                return false;
            }
        }
    }

    public boolean calculateReadOnly(Boolean queryReadOnly) {
        if (queryReadOnly != null) {
            return queryReadOnly;
        } else {
            if (referenceOptions != null) {
                return referenceOptions.isReadOnly();
            } else {
                return false;
            }
        }
    }

    /**
	 * Return the reference options.
	 */
    public ReferenceOptions getReferenceOptions() {
        return referenceOptions;
    }

    /**
	 * Execute the warming cache query (if defined) and load the cache.
	 */
    public void runCacheWarming() {
        if (referenceOptions == null) {
            return;
        }
        String warmingQuery = referenceOptions.getWarmingQuery();
        if (warmingQuery != null && warmingQuery.trim().length() > 0) {
            Query<T> query = ebeanServer.createQuery(beanType);
            query.setQuery(warmingQuery);
            query.setUseCache(true);
            query.setReadOnly(true);
            query.setLoadBeanCache(true);
            List<T> list = query.findList();
            if (logger.isLoggable(Level.INFO)) {
                String msg = "Loaded " + beanType + " cache with [" + list.size() + "] beans";
                logger.info(msg);
            }
        }
    }

    /**
	 * Return true if this object is the root level object in its
	 * entity inheritance.
	 */
    public boolean isInheritanceRoot() {
        return inheritInfo == null || inheritInfo.isRoot();
    }

    /**
	 * Return true if there is currently query caching for this type of bean.
	 */
    public boolean isQueryCaching() {
        return queryCache != null;
    }

    /**
	 * Return true if there is currently bean caching for this type of bean.
	 */
    public boolean isCaching() {
        return beanCache != null;
    }

    /**
	 * Invalidate parts of cache due to SqlUpdate or
	 * external modification etc.
	 */
    public void cacheNotify(TableIUD tableIUD) {
        if (tableIUD.isUpdateOrDelete()) {
            cacheClear();
        }
        queryCacheClear();
    }

    /**
	 * Clear the query cache.
	 */
    public void queryCacheClear() {
        if (queryCache != null) {
            queryCache.clear();
        }
    }

    /**
	 * Get a query result from the query cache.
	 */
    @SuppressWarnings("unchecked")
    public BeanCollection<T> queryCacheGet(Object id) {
        if (queryCache == null) {
            return null;
        } else {
            return (BeanCollection<T>) queryCache.get(id);
        }
    }

    /**
	 * Put a query result into the query cache.
	 */
    public void queryCachePut(Object id, BeanCollection<T> query) {
        if (queryCache == null) {
            queryCache = cacheManager.getQueryCache(beanType);
        }
        queryCache.put(id, query);
    }

    /**
	 * Clear the bean cache.
	 */
    public void cacheClear() {
        if (beanCache != null) {
            beanCache.clear();
        }
    }

    @SuppressWarnings("unchecked")
    public T cachePutObject(Object bean) {
        return cachePut((T) bean);
    }

    /**
	 * Put a bean into the bean cache.
	 */
    @SuppressWarnings("unchecked")
    public T cachePut(T bean) {
        if (beanCache == null) {
            beanCache = cacheManager.getBeanCache(beanType);
        }
        Object id = getId(bean);
        ((EntityBean) bean)._ebean_getIntercept().setSharedInstance();
        return (T) beanCache.put(id, bean);
    }

    /**
	 * Return a bean from the bean cache.
	 */
    @SuppressWarnings("unchecked")
    public T cacheGet(Object id) {
        if (beanCache == null) {
            return null;
        } else {
            return (T) beanCache.get(id);
        }
    }

    /**
	 * Remove a bean from the cache given its Id.
	 */
    public void cacheRemove(Object id) {
        if (beanCache != null) {
            beanCache.remove(id);
        }
    }

    /**
	 * Return the base table alias.
	 * This is always the first letter of the bean name.
	 */
    public String getBaseTableAlias() {
        return baseTableAlias;
    }

    public Object nextId() {
        if (idGenerator != null) {
            return idGenerator.nextId();
        } else {
            return null;
        }
    }

    public DeployPropertyParser createDeployPropertyParser() {
        return new DeployPropertyParser(this);
    }

    /**
	 * Convert the logical orm update statement into sql by converting the bean properties and bean name to
	 * database columns and table.
	 */
    public String convertOrmUpdateToSql(String ormUpdateStatement) {
        return new DeployUpdateParser(this).parse(ormUpdateStatement);
    }

    /**
	 * Reset the statistics on all the query plans.
	 */
    public void clearQueryStatistics() {
        Iterator<CQueryPlan> it = queryPlanCache.values().iterator();
        while (it.hasNext()) {
            CQueryPlan queryPlan = (CQueryPlan) it.next();
            queryPlan.resetStatistics();
        }
    }

    /**
	 * Execute the postLoad if a BeanPersistController exists for this bean.
	 */
    @SuppressWarnings("unchecked")
    public void postLoad(Object bean, Set<String> includedProperties) {
        BeanPersistController c = persistController;
        if (c != null) {
            c.postLoad((T) bean, includedProperties);
        }
    }

    /**
	 * Return the query plans for this BeanDescriptor.
	 */
    public Iterator<CQueryPlan> queryPlans() {
        return queryPlanCache.values().iterator();
    }

    public CQueryPlan getQueryPlan(Integer key) {
        return queryPlanCache.get(key);
    }

    public void putQueryPlan(Integer key, CQueryPlan plan) {
        queryPlanCache.put(key, plan);
    }

    /**
	 * Return the TypeManager.
	 */
    public TypeManager getTypeManager() {
        return typeManager;
    }

    /**
	 * Return true if updates should only include changed properties.
	 * Otherwise all loaded properties are included in the update.
	 */
    public boolean isUpdateChangesOnly() {
        return updateChangesOnly;
    }

    /**
	 * Return true if save does not recurse to other beans. That is return true
	 * if there are no assoc one or assoc many beans that cascade save.
	 */
    public boolean isSaveRecurseSkippable() {
        return saveRecurseSkippable;
    }

    /**
	 * Return true if delete does not recurse to other beans. That is return
	 * true if there are no assoc one or assoc many beans that cascade delete.
	 */
    public boolean isDeleteRecurseSkippable() {
        return deleteRecurseSkippable;
    }

    /**
	 * Return true if this type has local validation rules.
	 */
    public boolean hasLocalValidation() {
        return hasLocalValidation;
    }

    /**
	 * Return true if this type has local or cascading validation rules.
	 */
    public boolean hasCascadeValidation() {
        return hasCascadeValidation;
    }

    public InvalidValue validate(boolean cascade, Object bean) {
        if (!hasCascadeValidation) {
            return null;
        }
        List<InvalidValue> errList = null;
        Set<String> loadedProps = null;
        if (bean instanceof EntityBean) {
            EntityBeanIntercept ebi = ((EntityBean) bean)._ebean_getIntercept();
            loadedProps = ebi.getLoadedProps();
        }
        if (loadedProps != null) {
            Iterator<String> propIt = loadedProps.iterator();
            while (propIt.hasNext()) {
                String propName = (String) propIt.next();
                BeanProperty property = getBeanProperty(propName);
                if (property != null && property.hasValidationRules(cascade)) {
                    Object value = property.getValue(bean);
                    List<InvalidValue> errs = property.validate(cascade, value);
                    if (errs != null) {
                        if (errList == null) {
                            errList = new ArrayList<InvalidValue>();
                        }
                        errList.addAll(errs);
                    }
                }
            }
        } else {
            BeanProperty[] props = cascade ? propertiesValidationCascade : propertiesValidationLocal;
            for (int i = 0; i < props.length; i++) {
                BeanProperty prop = props[i];
                Object value = prop.getValue(bean);
                List<InvalidValue> errs = prop.validate(cascade, value);
                if (errs != null) {
                    if (errList == null) {
                        errList = new ArrayList<InvalidValue>();
                    }
                    errList.addAll(errs);
                }
            }
        }
        for (int i = 0; i < beanValidators.length; i++) {
            if (!beanValidators[i].isValid(bean)) {
                if (errList == null) {
                    errList = new ArrayList<InvalidValue>();
                }
                Validator v = beanValidators[i];
                errList.add(new InvalidValue(v.getKey(), v.getAttributes(), getFullName(), null, bean));
            }
        }
        if (errList == null) {
            return null;
        }
        return new InvalidValue(null, getFullName(), bean, InvalidValue.toArray(errList));
    }

    /**
	 * Return the many property included in the query or null if one is not.
	 */
    public BeanPropertyAssocMany<?> getManyProperty(SpiQuery<?> query) {
        OrmQueryDetail detail = query.getDetail();
        for (int i = 0; i < propertiesMany.length; i++) {
            if (detail.includes(propertiesMany[i].getName())) {
                return propertiesMany[i];
            }
        }
        return null;
    }

    /**
	 * Return the IdBinder which is helpful for handling the various types of
	 * Id.
	 */
    public IdBinder getIdBinder() {
        return idBinder;
    }

    /**
	 * Return the sql for binding an id. This is the columns with table alias
	 * that make up the id.
	 */
    public String getBindIdSql() {
        return idBinder.getBindIdSql(baseTableAlias);
    }

    /**
	 * Bind the idValue to the preparedStatement.
	 * <p>
	 * This takes care of the various id types such as embedded beans etc.
	 * </p>
	 */
    public int bindId(PreparedStatement pstmt, int index, Object idValue) throws SQLException {
        return idBinder.bindId(pstmt, index, idValue);
    }

    /**
	 * Return the id as an array of scalar bindable values.
	 * <p>
	 * This 'flattens' any EmbeddedId or multiple Id property cases.
	 * </p>
	 */
    public Object[] getBindIdValues(Object idValue) {
        return idBinder.getBindValues(idValue);
    }

    /**
	 * Return a named query.
	 */
    public DeployNamedQuery getNamedQuery(String name) {
        return namedQueries.get(name);
    }

    public DeployNamedQuery addNamedQuery(DeployNamedQuery deployNamedQuery) {
        return namedQueries.put(deployNamedQuery.getName(), deployNamedQuery);
    }

    /**
	 * Return a named update.
	 */
    public DeployNamedUpdate getNamedUpdate(String name) {
        return namedUpdates.get(name);
    }

    /**
	 * Create a plain vanilla object.
	 * <p>
	 * Used for EmbeddedId Bean construction.
	 * </p>
	 */
    public Object createVanillaBean() {
        return beanReflect.createVanillaBean();
    }

    /**
	 * Creates a new EntityBean without using the creation queue.
	 */
    public EntityBean createEntityBean() {
        try {
            EntityBean eb = (EntityBean) beanReflect.createEntityBean();
            EntityBeanIntercept ebi = eb._ebean_getIntercept();
            ebi.setInternalEbean(ebeanServer);
            return eb;
        } catch (Exception ex) {
            throw new PersistenceException(ex);
        }
    }

    /**
	 * Create a reference bean based on the id.
	 */
    @SuppressWarnings("unchecked")
    public T createReference(Object id, Object parent, ReferenceOptions options) {
        try {
            EntityBean eb = (EntityBean) beanReflect.createEntityBean();
            EntityBeanIntercept ebi = eb._ebean_getIntercept();
            ebi.setInternalEbean(ebeanServer);
            if (parent != null) {
                ebi.setParentBean(parent);
            }
            convertSetId(id, eb);
            if (options != null) {
                ebi.setUseCache(options.isUseCache());
                ebi.setReadOnly(options.isReadOnly());
            }
            ebi.setReference();
            return (T) eb;
        } catch (Exception ex) {
            throw new PersistenceException(ex);
        }
    }

    /**
	 * Return the BeanDescriptor of another bean type.
	 */
    public <U> BeanDescriptor<U> getBeanDescriptor(Class<U> otherType) {
        return owner.getBeanDescriptor(otherType);
    }

    /**
	 * Return the "shadow" property to support unidirectional relationships.
	 * <p>
	 * For bidirectional this is a real property on the bean. For unidirectional
	 * relationships we have this 'shadow' property which is not externally
	 * visible.
	 * </p>
	 */
    public BeanPropertyAssocOne<?> getUnidirectional() {
        return unidirectional;
    }

    /**
	 * Get a property value from a bean of this type.
	 */
    public Object getValue(Object bean, String property) {
        return getBeanProperty(property).getValue(bean);
    }

    /**
	 * Return true if this bean type should use IdGeneration.
	 * <p>
	 * If this is false and the Id is null it is assumed that a database auto
	 * increment feature is being used to populate the id.
	 * </p>
	 */
    public boolean isUseIdGenerator() {
        return idType == IdType.GENERATOR;
    }

    /**
	 * Return the class type this BeanDescriptor describes.
	 */
    public Class<T> getBeanType() {
        return beanType;
    }

    /**
	 * Return the class type this BeanDescriptor describes.
	 */
    public Class<?> getFactoryType() {
        return factoryType;
    }

    /**
	 * Return the bean class name this descriptor is used for.
	 * <p>
	 * If this BeanDescriptor is for a table then this returns the table name
	 * instead.
	 * </p>
	 */
    public String getFullName() {
        return fullName;
    }

    /**
	 * Return the short name of the entity bean.
	 */
    public String getName() {
        return name;
    }

    /**
	 * Summary description.
	 */
    public String toString() {
        return fullName;
    }

    /**
	 * Helper method to return the unique property. If only one property makes
	 * up the unique id then it's value is returned. If there is a concatenated
	 * unique id then a Map is built with the keys being the names of the
	 * properties that make up the unique id.
	 */
    public Object getId(Object bean) {
        if (propertySingleId != null) {
            return propertySingleId.getValue(bean);
        }
        LinkedHashMap<String, Object> idMap = new LinkedHashMap<String, Object>();
        for (int i = 0; i < propertiesId.length; i++) {
            Object value = propertiesId[i].getValue(bean);
            idMap.put(propertiesId[i].getName(), value);
        }
        return idMap;
    }

    /**
	 * Return false if the id is a simple scalar and false if it is embedded or concatenated.
	 */
    public boolean isComplexId() {
        return idBinder.isComplexId();
    }

    /**
	 * Return the default order by that may need to be added if a many property
	 * is included in the query.
	 */
    public String getDefaultOrderBy() {
        return idBinder.getDefaultOrderBy();
    }

    /**
	 * Convert the type of the idValue if required.
	 */
    public Object convertId(Object idValue) {
        return idBinder.convertSetId(idValue, null);
    }

    /**
	 * Convert and set the id value.
	 * <p>
	 * If the bean is not null, the id value is set to the id property of the
	 * bean after it has been converted to the correct type.
	 * </p>
	 */
    public Object convertSetId(Object idValue, Object bean) {
        return idBinder.convertSetId(idValue, bean);
    }

    /**
	 * Get a BeanProperty by its name.
	 */
    public BeanProperty getBeanProperty(String propName) {
        return (BeanProperty) propMap.get(propName);
    }

    public void sort(List<T> list, String sortByClause) {
        ElComparator<T> comparator = getElComparator(sortByClause);
        Collections.sort(list, comparator);
    }

    public ElComparator<T> getElComparator(String propNameOrSortBy) {
        ElComparator<T> c = comparatorCache.get(propNameOrSortBy);
        if (c == null) {
            c = createComparator(propNameOrSortBy);
            comparatorCache.put(propNameOrSortBy, c);
        }
        return c;
    }

    /**
	 * Return a Comparator for local sorting of lists.
	 * @param sortByClause list of property names with optional ASC or DESC suffix.
	 */
    @SuppressWarnings("unchecked")
    private ElComparator<T> createComparator(String sortByClause) {
        SortByClause sortBy = SortByClauseParser.parse(sortByClause);
        if (sortBy.size() == 1) {
            return createPropertyComparator(sortBy.getProperties().get(0));
        }
        ElComparator<T>[] comparators = new ElComparator[sortBy.size()];
        List<Property> sortProps = sortBy.getProperties();
        for (int i = 0; i < sortProps.size(); i++) {
            Property sortProperty = sortProps.get(i);
            comparators[i] = createPropertyComparator(sortProperty);
        }
        return new ElComparatorCompound<T>(comparators);
    }

    private ElComparator<T> createPropertyComparator(Property sortProp) {
        ElPropertyValue elGetValue = getElGetValue(sortProp.getName());
        Boolean nullsHigh = sortProp.getNullsHigh();
        if (nullsHigh == null) {
            nullsHigh = Boolean.TRUE;
        }
        return new ElComparatorProperty<T>(elGetValue, sortProp.isAscending(), nullsHigh);
    }

    public ElPropertyValue getElGetValue(String propName) {
        return getElPropertyValue(propName, false);
    }

    public ElPropertyDeploy getElPropertyDeploy(String propName) {
        ElPropertyDeploy fk = fkeyMap.get(propName);
        if (fk != null) {
            return fk;
        }
        return getElPropertyValue(propName, true);
    }

    private ElPropertyValue getElPropertyValue(String propName, boolean propertyDeploy) {
        ElPropertyValue elGetValue = elGetCache.get(propName);
        if (elGetValue == null) {
            elGetValue = buildElGetValue(propName, null, propertyDeploy);
            if (elGetValue == null) {
                return null;
            }
            if (elGetValue instanceof BeanFkeyProperty) {
                fkeyMap.put(propName, (BeanFkeyProperty) elGetValue);
            } else {
                elGetCache.put(propName, elGetValue);
            }
        }
        return elGetValue;
    }

    private ElPropertyValue buildElGetValue(String propName, ElPropertyChainBuilder chain, boolean propertyDeploy) {
        if (propertyDeploy && chain != null) {
            BeanFkeyProperty fk = fkeyMap.get(propName);
            if (fk != null) {
                return fk.create(chain.getExpression());
            }
        }
        int basePos = propName.indexOf('.');
        if (basePos > -1) {
            String baseName = propName.substring(0, basePos);
            String remainder = propName.substring(basePos + 1);
            BeanProperty assocProp = _findBeanProperty(baseName);
            if (assocProp == null) {
                return null;
            }
            BeanDescriptor<?> embDesc = ((BeanPropertyAssoc<?>) assocProp).getTargetDescriptor();
            if (chain == null) {
                chain = new ElPropertyChainBuilder(assocProp.isEmbedded(), propName);
            }
            chain.add(assocProp);
            if (assocProp.containsMany()) {
                chain.setContainsMany(true);
            }
            return embDesc.buildElGetValue(remainder, chain, propertyDeploy);
        }
        BeanProperty property = _findBeanProperty(propName);
        if (chain == null) {
            return property;
        } else {
            return chain.add(property).build();
        }
    }

    /**
	 * Find a BeanProperty including searching the inheritance hierarchy.
	 * <p>
	 * This searches this BeanDescriptor and then searches further down the
	 * inheritance tree (not up).
	 * </p>
	 */
    public BeanProperty findBeanProperty(String propName) {
        int basePos = propName.indexOf('.');
        if (basePos > -1) {
            String baseName = propName.substring(0, basePos);
            return _findBeanProperty(baseName);
        }
        return _findBeanProperty(propName);
    }

    private BeanProperty _findBeanProperty(String propName) {
        BeanProperty prop = propMap.get(propName);
        if (prop == null && inheritInfo != null) {
            return inheritInfo.findSubTypeProperty(propName);
        }
        return prop;
    }

    /**
	 * Return the name of the server this BeanDescriptor belongs to.
	 */
    public String getServerName() {
        return serverName;
    }

    /**
	 * Return true if queries for beans of this type are autoFetch tunable.
	 */
    public boolean isAutoFetchTunable() {
        return autoFetchTunable;
    }

    /**
	 * Returns the Inheritance mapping information. This will be null if this
	 * type of bean is not involved in any ORM inheritance mapping.
	 */
    public InheritInfo getInheritInfo() {
        return inheritInfo;
    }

    /**
	 * Return true if this was generated from jdbc meta data of a table. Returns
	 * false for normal beans.
	 */
    public boolean isTableGenerated() {
        return tableGenerated;
    }

    /**
	 * Return true if this is an embedded bean.
	 */
    public boolean isEmbedded() {
        return embedded;
    }

    /**
	 * Return true if this is an meta bean.
	 * <p>
	 * Those are Entity Beans in org.avaje.ebean.meta that hold meta
	 * data such as query performance statistics etc.
	 * </p>
	 */
    public boolean isMeta() {
        return meta;
    }

    /**
	 * Return the concurrency mode used for beans of this type.
	 */
    public ConcurrencyMode getConcurrencyMode() {
        return concurrencyMode;
    }

    /**
	 * Return the tables this bean is dependent on. This implies that if any of
	 * these tables are modified then cached beans may be invalidated.
	 */
    public String[] getDependantTables() {
        return dependantTables;
    }

    /**
	 * Return the beanListener.
	 */
    public BeanPersistListener<T> getPersistListener() {
        return persistListener;
    }

    /**
	 * Return the beanFinder. Usually null unless overriding the finder.
	 */
    public BeanFinder<T> getBeanFinder() {
        return beanFinder;
    }

    /**
	 * De-register the BeanPersistListener.
	 */
    @SuppressWarnings("unchecked")
    public void deregister(BeanPersistListener<?> listener) {
        BeanPersistListener<T> currListener = persistListener;
        if (currListener == null) {
        } else {
            BeanPersistListener<T> deregListener = (BeanPersistListener<T>) listener;
            if (currListener instanceof ChainedBeanPersistListener<?>) {
                persistListener = ((ChainedBeanPersistListener<T>) currListener).deregister(deregListener);
            } else if (currListener.equals(deregListener)) {
                persistListener = null;
            }
        }
    }

    /**
	 * De-register the BeanPersistController.
	 */
    public void deregister(BeanPersistController controller) {
        BeanPersistController c = persistController;
        if (c == null) {
        } else {
            if (c instanceof ChainedBeanPersistController) {
                persistController = ((ChainedBeanPersistController) c).deregister(controller);
            } else if (c.equals(controller)) {
                persistController = null;
            }
        }
    }

    /**
	 * Register the new BeanPersistController.
	 */
    @SuppressWarnings("unchecked")
    public void register(BeanPersistListener<?> newPersistListener) {
        if (!PersistListenerManager.isRegisterFor(beanType, newPersistListener)) {
        } else {
            BeanPersistListener<T> newListener = (BeanPersistListener<T>) newPersistListener;
            BeanPersistListener<T> currListener = persistListener;
            if (currListener == null) {
                persistListener = newListener;
            } else {
                if (currListener instanceof ChainedBeanPersistListener<?>) {
                    persistListener = ((ChainedBeanPersistListener<T>) currListener).register(newListener);
                } else {
                    persistListener = new ChainedBeanPersistListener<T>(currListener, newListener);
                }
            }
        }
    }

    /**
	 * Register the new BeanPersistController.
	 */
    public void register(BeanPersistController newController) {
        if (!newController.isRegisterFor(beanType)) {
        } else {
            BeanPersistController c = persistController;
            if (c == null) {
                persistController = newController;
            } else {
                if (c instanceof ChainedBeanPersistController) {
                    persistController = ((ChainedBeanPersistController) c).register(newController);
                } else {
                    persistController = new ChainedBeanPersistController(c, newController);
                }
            }
        }
    }

    /**
	 * Return the Controller.
	 */
    public BeanPersistController getPersistController() {
        return persistController;
    }

    /**
	 * Returns true if this bean is based on a table (or possibly view) and
	 * returns false if this bean is based on a raw sql select statement.
	 * <p>
	 * When false querying this bean is based on a supplied sql select statement
	 * placed in the orm xml file (as opposed to Ebean generated sql).
	 * </p>
	 */
    public boolean isSqlSelectBased() {
        return sqlSelectBased;
    }

    /**
	 * Return the base table. Only properties mapped to the base table are by
	 * default persisted.
	 */
    public String getBaseTable() {
        return baseTable;
    }

    /**
	 * Return the map of logical property names to database column names as well as the
	 * bean name to base table.
	 * <p>
	 * This is used in converting a logical update into sql.
	 * </p>
	 */
    public Map<String, String> getUpdateDeployMap() {
        return updateDeployMap;
    }

    /**
	 * Get a named extra attribute.
	 */
    public String getExtraAttribute(String key) {
        return (String) extraAttrMap.get(key);
    }

    /**
	 * Return the identity generation type.
	 */
    public IdType getIdType() {
        return idType;
    }

    /**
	 * Return the sequence name with nextval wrapping.
	 */
    public String getSequenceNextVal() {
        return sequenceNextVal;
    }

    /**
	 * Return the sequence name.
	 */
    public String getSequenceName() {
        return sequenceName;
    }

    /**
	 * Return the SQL used to return the last inserted id.
	 * <p>
	 * This is only used with Identity columns and getGeneratedKeys is not supported.
	 * </p>
	 */
    public String getSelectLastInsertedId() {
        return selectLastInsertedId;
    }

    /**
	 * Return the name of the IdGenerator that should be used with this type of
	 * bean. A null value could be used to specify the 'default' IdGenerator.
	 */
    public String getIdGeneratorName() {
        return idGeneratorName;
    }

    /**
	 * Return the includes for getReference().
	 */
    public String getLazyFetchIncludes() {
        return lazyFetchIncludes;
    }

    /**
	 * Return the TableJoins.
	 * <p>
	 * For properties mapped to secondary tables rather than the base table.
	 * </p>
	 */
    public TableJoin[] tableJoins() {
        return derivedTableJoins;
    }

    /**
	 * Return an Iterator of all BeanProperty. This includes transient
	 * properties.
	 */
    public Iterator<BeanProperty> propertiesAll() {
        return propMap.values().iterator();
    }

    /**
	 * Return the BeanProperty that make up the unique id.
	 * <p>
	 * The order of these properties can be relied on to be consistent if the
	 * bean itself doesn't change or the xml deployment order does not change.
	 * </p>
	 */
    public BeanProperty[] propertiesId() {
        return propertiesId;
    }

    /**
	 * Return the transient properties.
	 */
    public BeanProperty[] propertiesTransient() {
        return propertiesTransient;
    }

    /**
	 * If the Id is a single non-embedded property then returns that, otherwise
	 * returns null.
	 */
    public BeanProperty getSingleIdProperty() {
        return propertySingleId;
    }

    /**
	 * Return the beans that are embedded. These share the base table with the
	 * owner bean.
	 */
    public BeanPropertyAssocOne<?>[] propertiesEmbedded() {
        return propertiesEmbedded;
    }

    /**
	 * All the BeanPropertyAssocOne that are not embedded. These are effectively
	 * joined beans. For ManyToOne and OneToOne associations.
	 */
    public BeanPropertyAssocOne<?>[] propertiesOne() {
        return propertiesOne;
    }

    /**
	 * Returns ManyToOnes and OneToOnes on the imported owning side.
	 * <p>
	 * Excludes OneToOnes on the exported side.
	 * </p>
	 */
    public BeanPropertyAssocOne<?>[] propertiesOneImported() {
        return propertiesOneImported;
    }

    /**
	 * Imported Assoc Ones with cascade save true.
	 */
    public BeanPropertyAssocOne<?>[] propertiesOneImportedSave() {
        return propertiesOneImportedSave;
    }

    /**
	 * Imported Assoc Ones with cascade delete true.
	 */
    public BeanPropertyAssocOne<?>[] propertiesOneImportedDelete() {
        return propertiesOneImportedSave;
    }

    /**
	 * Returns OneToOnes that are on the exported side of a OneToOne.
	 * <p>
	 * These associations do not own the relationship.
	 * </p>
	 */
    public BeanPropertyAssocOne<?>[] propertiesOneExported() {
        return propertiesOneExported;
    }

    /**
	 * Exported assoc ones with cascade save.
	 */
    public BeanPropertyAssocOne<?>[] propertiesOneExportedSave() {
        return propertiesOneExportedSave;
    }

    /**
	 * Exported assoc ones with delete cascade.
	 */
    public BeanPropertyAssocOne<?>[] propertiesOneExportedDelete() {
        return propertiesOneExportedDelete;
    }

    private Set<String> deriveManyPropNames() {
        LinkedHashSet<String> names = new LinkedHashSet<String>();
        for (int i = 0; i < propertiesMany.length; i++) {
            names.add(propertiesMany[i].getName());
        }
        return Collections.unmodifiableSet(names);
    }

    /**
	 * Return a hash of the names of the many properties on this bean
	 * type. This is used for efficient building of included properties
	 * sets for partial objects.
	 */
    public int getNamesOfManyPropsHash() {
        return namesOfManyPropsHash;
    }

    /**
	 * Returns the set of many property names for this bean type.
	 */
    public Set<String> getNamesOfManyProps() {
        return namesOfManyProps;
    }

    /**
	 * All Assoc Many's for this descriptor.
	 */
    public BeanPropertyAssocMany<?>[] propertiesMany() {
        return propertiesMany;
    }

    /**
	 * Assoc Many's with save cascade.
	 */
    public BeanPropertyAssocMany<?>[] propertiesManySave() {
        return propertiesManySave;
    }

    /**
	 * Assoc Many's with delete cascade.
	 */
    public BeanPropertyAssocMany<?>[] propertiesManyDelete() {
        return propertiesManyDelete;
    }

    /**
	 * Return the first version property that exists on the bean. Returns null
	 * if no version property exists on the bean.
	 * <p>
	 * Note that this DOES NOT find a version property on an embedded bean.
	 * </p>
	 */
    public BeanProperty firstVersionProperty() {
        return propertyFirstVersion;
    }

    /**
	 * Returns 'Version' properties on this bean. These are 'Counter' or 'Update
	 * Timestamp' type properties. Note version properties can also be on
	 * embedded beans rather than on the bean itself.
	 */
    public BeanProperty[] propertiesVersion() {
        return propertiesVersion;
    }

    /**
	 * Scalar properties without the unique id or secondary table properties.
	 */
    public BeanProperty[] propertiesBaseScalar() {
        return propertiesBaseScalar;
    }

    /**
	 * Return the properties local to this type for inheritance.
	 */
    public BeanProperty[] propertiesLocal() {
        return propertiesLocal;
    }
}

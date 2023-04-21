package org.openware.job.data;

import org.openware.job.ejb.MDMSessionHome;
import org.openware.job.ejb.MDMSession;
import org.openware.job.ejb.MDMSessionBean;
import org.openware.job.servlet.ServletClient;
import org.openware.job.servlet.FindByArguments;
import org.openware.job.validation.IDateFieldValidator;
import org.openware.job.validation.INumberFieldValidator;
import org.openware.job.validation.IStringFieldValidator;
import org.openware.job.validation.SaveValidationException;
import org.openware.job.validation.PersistValidationException;
import org.openware.job.validation.PersistConfig;
import org.openware.job.cache.QueryCache;
import org.openware.job.cache.CacheManager;
import org.openware.job.cache.CacheInvalidationInfo;
import org.openware.job.cache.ListInvalidator;
import org.openware.job.generator.metadata.MetaData;
import org.apache.log4j.Category;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;
import java.rmi.RemoteException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * <code>PersistentManager</code> is basically a cache of 
 * <code>TableRow</code> objects.  
 *
 * @author Vincent Sheffer
 * @version $Revision: 1.114 $ $Date: 2004/10/26 07:06:30 $
 */
public class PersistentManager implements Serializable {

    private static Category log = Category.getInstance(PersistentManager.class.getName());

    /**
     * Constant used to specify that the connection mode for connecting
     * to the server component is a JOB servlet.
     */
    public static final int SERVLET = 0;

    /**
     * Constant used to specify that the persistent manager operates 
     * in a local (that is not connected to any server component) mode.
     */
    public static final int LOCAL = 1;

    /**
     * Constant used to specify that the connection mode for connecting
     * to the server component is the JOB EJB.
     */
    public static final int EJB = 2;

    /**
     * The JNDI name of the JOB session bean.
     */
    public static String JOB_SESSION_BEAN_JNDI_NAME = "org/openware/job/beans/JobManager";

    private static final Class[] persistConstructorArgTypes = { PersistentManager.class, DataShell.class };

    private final transient Object[] persistConstructorArgs = new Object[2];

    private static final long incrementKeysBy = 500;

    private transient MDMSession mdmSession = null;

    private transient MDMSessionBean beanInstance = null;

    private transient ServletClient servletClient = null;

    private transient Object lockObject = new Object();

    private transient HashMap oneToManyLists = new HashMap();

    private transient ShellComparator shellComparator = new ShellComparator();

    private transient QueryCache queryCache = new QueryCache();

    private transient MetaData metadata = null;

    private transient long lastAccessTime = System.currentTimeMillis();

    private long numKeysChosen = 0;

    private long nextKey = 0;

    private String dataSourceJNDIName = null;

    private HashMap dataObjects = new HashMap();

    private HashMap references = new HashMap();

    private LinkedList removedObjects = new LinkedList();

    private LinkedList createdObjects = new LinkedList();

    private LinkedList modifiedLists = new LinkedList();

    private String name = null;

    private String classInfoClassName = null;

    private transient ClassInfoBase classInfo = null;

    private int connectionMode = EJB;

    private String username = null;

    private String password = null;

    private long maxIdleTime = -1;

    private PersistentManager() {
    }

    /**
     * Create a new PersistentManager using the connection mode <code>EJB</code>.
     *
     * @param  cinfo  
     * @param  connectionString  The string used to connect to the JOB server
     *                           component.  The contents of this string varies
     *                           depending on the connection mode.  If the 
     *                           connection mode is <code>EJB</code> then this
     *                           is the the JNDI name for the datasource object 
     *                           used to talk to the database.  If the connection
     *                           mode is SERVLET then this is a valid URL to the
     *                           JOB servlet.
     * @param  connectionMode    One of <code>SERVLET</code>, <code>EJB</code>, or
     *                           <code>LOCAL</code>.
     */
    public PersistentManager(ClassInfoBase cinfo, String connectionString, int connectionMode) throws PersistException {
        this.classInfo = cinfo;
        if (this.classInfo != null) {
            this.classInfoClassName = cinfo.getClass().getName();
        }
        if (connectionMode != EJB && connectionMode != SERVLET && connectionMode != LOCAL) {
            throw new IllegalArgumentException("Invalid connection mode: " + connectionMode);
        }
        this.connectionMode = connectionMode;
        establishSession(null, connectionString);
    }

    /**
     * Create a new PersistentManager using the connection mode <code>EJB</code>.
     *
     * @param  cinfo  
     * @param  connectionString  The string used to connect to the JOB server
     *                           component.  The contents of this string varies
     *                           depending on the connection mode.  If the 
     *                           connection mode is <code>EJB</code> then this
     *                           is the the JNDI name for the datasource object 
     *                           used to talk to the database.  If the connection
     *                           mode is SERVLET then this is a valid URL to the
     *                           JOB servlet.
     */
    public PersistentManager(ClassInfoBase cinfo, String connectionString) throws PersistException {
        this(cinfo, connectionString, EJB);
    }

    public PersistentManager(Hashtable jndiEnv, String dataSourceName, ClassInfoBase cinfo) throws PersistException {
        this.classInfo = cinfo;
        this.classInfoClassName = cinfo.getClass().getName();
        this.dataSourceJNDIName = dataSourceName;
        establishSession(jndiEnv, dataSourceName);
    }

    public void setAuthenticationToken(String token) {
        lastAccessTime = System.currentTimeMillis();
        if (servletClient != null) {
            servletClient.setAuthenticationToken(token);
        }
    }

    /**
     * Set the username for the PersistentManger for audit 
     * purposes.
     */
    public void setUsername(String username) {
        lastAccessTime = System.currentTimeMillis();
        this.username = username;
    }

    /**
     * Set the maximum time that this persistent manager will be
     * kept in the cache manager before being removed.
     * 
     * @param  value  The value for 'maxIdleTime'.  -1 means to 
     */
    public void setMaxIdleTime(long value) {
        maxIdleTime = value;
    }

    public long getMaxIdleTime() {
        return maxIdleTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public String getUsername() {
        lastAccessTime = System.currentTimeMillis();
        return username;
    }

    public Object getLockObject() {
        lastAccessTime = System.currentTimeMillis();
        return lockObject;
    }

    public String getDataSourceJNDIName() {
        lastAccessTime = System.currentTimeMillis();
        return dataSourceJNDIName;
    }

    public String getName() {
        lastAccessTime = System.currentTimeMillis();
        return name;
    }

    public void setName(String value) {
        lastAccessTime = System.currentTimeMillis();
        this.name = value;
    }

    public void setClassInfo(ClassInfoBase cinfo) {
        lastAccessTime = System.currentTimeMillis();
        classInfo = cinfo;
    }

    public void changingPersist(Oid oid) {
        DataShell shell = null;
        shell = (DataShell) dataObjects.get(oid);
    }

    public ClassInfoBase getClassInfo() {
        lastAccessTime = System.currentTimeMillis();
        if (classInfo == null) {
            if (classInfoClassName != null) {
                try {
                    classInfo = (ClassInfoBase) Class.forName(classInfoClassName).newInstance();
                } catch (Exception e) {
                    log.error("Creating ClassInfo", e);
                }
            }
        }
        return classInfo;
    }

    public void setQueryCache(QueryCache queryCache) {
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            if (this.queryCache == null || this.queryCache != queryCache) {
                this.queryCache = null;
                this.queryCache = queryCache;
                if (log.isDebugEnabled()) {
                    log.debug("Setting queryCache to " + queryCache);
                }
            }
        }
    }

    public Object getReference(Oid parentOid) throws PersistException {
        Constructor cons = null;
        Object[] args = null;
        DataShell parentShell = null;
        String tableName = null;
        TableRow data = null;
        lastAccessTime = System.currentTimeMillis();
        parentShell = (DataShell) dataObjects.get(parentOid.toString());
        if (parentShell == null) {
            parentShell = getClassInfo().getDataShell(parentOid.getTableName());
            parentShell.getData().setHollow();
            parentShell.setPrimaryKey(parentOid.getKey());
        }
        if (parentShell.getData().isHollow()) {
            data = parentShell.getData();
            try {
                if (connectionMode == EJB) {
                    data = mdmSession.load(classInfo.getDba(), dataSourceJNDIName, data);
                } else if (connectionMode == LOCAL) {
                    data = beanInstance.load(classInfo.getDba(), dataSourceJNDIName, data);
                } else {
                    data = servletClient.load(classInfo.getDba(), data);
                }
                parentShell.setData(data);
                if (log.isDebugEnabled()) {
                    log.debug("getReference(" + name + "): " + parentShell.getData().getSummary());
                }
                dataObjects.put(parentOid.toString(), parentShell);
            } catch (Exception e) {
                e.printStackTrace();
                throw new PersistException("Can't get data for '" + parentOid + "': " + e);
            }
        }
        args = new Object[2];
        args[0] = this;
        if (parentShell.getData().getSubtypeTableName() != null) {
            tableName = parentShell.getData().getSubtypeTableName();
            args[1] = this.getHollowDataShell(this.getClassInfo().getDataShell(tableName), parentOid.getKey());
        } else {
            tableName = parentOid.getTableName();
            args[1] = parentShell;
        }
        cons = this.getClassInfo().getPersistConstructor(tableName);
        try {
            return cons.newInstance(args);
        } catch (InvocationTargetException e) {
            throw new PersistException(e.getTargetException().toString());
        } catch (Exception e) {
            throw new PersistException(e.toString());
        }
    }

    /**
     * Load the persist object with the given OID string.
     */
    public Persist loadPersist(String oidstr) throws PersistException {
        DataShell shell = null;
        Persist persist = null;
        Oid oid = null;
        String primaryKeyColumn = null;
        Iterator iter = null;
        lastAccessTime = System.currentTimeMillis();
        oid = new Oid(oidstr.trim());
        shell = getClassInfo().getDataShell(oid.getTableName());
        primaryKeyColumn = shell.getData().getPrimaryKeyColumn().getColumnName();
        iter = query(getClassInfo().getPersistType(oid.getTableName()), primaryKeyColumn + "=" + oid.getKey()).iterator();
        if (iter.hasNext()) {
            persist = (Persist) iter.next();
        }
        return persist;
    }

    public Persist getPersist(String oidstr) throws PersistException {
        DataShell shell = null;
        Constructor cons = null;
        Persist persist = null;
        Oid oid = null;
        lastAccessTime = System.currentTimeMillis();
        oid = new Oid(oidstr.trim());
        shell = getClassInfo().getDataShell(oid.getTableName());
        shell = getHollowDataShell(shell, oid.getKey());
        cons = getClassInfo().getPersistConstructor(oid.getTableName());
        persistConstructorArgs[1] = shell;
        try {
            persist = (Persist) cons.newInstance(persistConstructorArgs);
        } catch (InvocationTargetException e) {
            PersistException pe = null;
            pe = (PersistException) e.getTargetException();
            throw pe;
        } catch (Exception e) {
            throw new PersistException("Can't create persist object: " + e);
        }
        return persist;
    }

    private void establishSession(Hashtable jndiEnv, String connectionString) throws PersistException {
        try {
            InitialContext ctx = null;
            if (connectionMode == EJB) {
                MDMSessionHome home = null;
                Object obj = null;
                if (jndiEnv != null) {
                    ctx = new InitialContext(jndiEnv);
                } else {
                    ctx = new InitialContext();
                }
                obj = ctx.lookup(JOB_SESSION_BEAN_JNDI_NAME);
                home = (MDMSessionHome) obj;
                this.mdmSession = home.create();
                this.dataSourceJNDIName = connectionString;
            } else if (connectionMode == LOCAL) {
                this.beanInstance = new MDMSessionBean();
                beanInstance.setSessionContext(null);
                this.dataSourceJNDIName = connectionString;
            } else {
                servletClient = new ServletClient(connectionString);
            }
            persistConstructorArgs[0] = this;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException("Can't create 'MDMSession': " + e);
        }
    }

    MDMSession getMDMSession() {
        return mdmSession;
    }

    String nextKey() throws PersistException {
        synchronized (lockObject) {
            if (numKeysChosen == 0) {
                try {
                    if (connectionMode == EJB) {
                        nextKey = mdmSession.getKeys(dataSourceJNDIName, incrementKeysBy);
                    } else if (connectionMode == LOCAL) {
                        nextKey = beanInstance.getKeys(dataSourceJNDIName, incrementKeysBy);
                    } else {
                        nextKey = servletClient.getKeys(incrementKeysBy);
                    }
                } catch (Exception e) {
                    throw new PersistException("Can't get OID: " + e);
                }
                numKeysChosen++;
            } else {
                nextKey++;
                numKeysChosen++;
                if (numKeysChosen >= incrementKeysBy) {
                    numKeysChosen = 0;
                }
            }
        }
        return (new Long(nextKey)).toString();
    }

    /**
     * Add <code>TableRow</code> data to the <code>PersistentManager</code>
     *
     * @param  data  The <code>TableRow</code> data to add.
     * @param  key   The key to cache the data by.  If the key is <code>null</code>
     *               then get a new key by calling the <code>nextKey</code> method.
     * 
     * @return The <code>Oid</code> of <code>TableRow</code> data object.
     */
    public Oid addData(Class persistClass, DataShell shell, String key) throws PersistException {
        Oid oid = null;
        String newKey = null;
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            if (key == null) {
                newKey = nextKey();
            } else {
                newKey = key;
            }
            shell.getData().setPrimaryKey(newKey);
            oid = new Oid(shell.getData().getTableName(), newKey);
            if (shell.getData().isNew()) {
                if (!createdObjects.contains(shell)) {
                    createdObjects.add(shell);
                }
            }
            dataObjects.put(oid.toString(), shell);
            return oid;
        }
    }

    public void setForRemove(Oid oid) throws PersistException {
        DataShell shell = null;
        lastAccessTime = System.currentTimeMillis();
        shell = getData(oid);
        shell.getData().setRemoved();
        if (shell.getData().isNew()) {
            createdObjects.remove(shell);
            dataObjects.remove(oid);
        } else {
            removedObjects.add(shell);
        }
        if (log.isDebugEnabled()) {
            log.debug("SETTING FOR REMOVE " + oid);
        }
    }

    public void invalidateForTable(String tableName) {
        lastAccessTime = System.currentTimeMillis();
        if (queryCache != null) {
            queryCache.invalidateCacheForTableName(tableName);
        }
    }

    public Reference addReference(Oid childOid, String fkColName, Class parentType) throws PersistException {
        HashMap map = null;
        Reference ref = null;
        lastAccessTime = System.currentTimeMillis();
        map = (HashMap) references.get(childOid.toString());
        if (map == null) {
            map = new HashMap();
            references.put(childOid.toString(), map);
        }
        ref = (Reference) map.get(fkColName);
        if (ref == null) {
            ref = new Reference(this, parentType);
        }
        return ref;
    }

    public void addOneToManyList(Oid owningOid, String fkColName, Class persistType) throws PersistException {
        HashMap map = null;
        DataShell ownedShell = null;
        lastAccessTime = System.currentTimeMillis();
        try {
            ownedShell = classInfo.getDataShell(persistType);
        } catch (Exception e) {
            throw new PersistException("Can't create class: " + e);
        }
        synchronized (lockObject) {
            map = (HashMap) oneToManyLists.get(owningOid.getKey());
            if (map == null) {
                map = new HashMap();
                oneToManyLists.put(owningOid.getKey(), map);
            }
        }
        if (map.get(fkColName + "->" + ownedShell.getData().getTableName()) == null) {
            map.put(fkColName + "->" + ownedShell.getData().getTableName(), new OneToManyList(this, owningOid, fkColName, ownedShell));
        }
    }

    /**
     * Get a count of rows returned by the given query on the give
     * type.
     */
    public int count(Query query) throws PersistException {
        lastAccessTime = System.currentTimeMillis();
        try {
            DataShell shell = null;
            int result;
            shell = query.getDataShell();
            if (connectionMode == EJB) {
                result = mdmSession.count(classInfo.getDba(), dataSourceJNDIName, shell.getData(), query);
            } else if (connectionMode == LOCAL) {
                result = beanInstance.count(classInfo.getDba(), dataSourceJNDIName, shell.getData(), query);
            } else {
                result = servletClient.count(classInfo.getDba(), shell.getData(), query);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException("Count failed: " + e);
        }
    }

    public Query createQuery(Class persistType, String whereClause) throws PersistException {
        Query query = null;
        lastAccessTime = System.currentTimeMillis();
        query = new Query(whereClause);
        query.setDba(classInfo.getDba());
        query.setDataShell(classInfo.getDataShell(persistType));
        return query;
    }

    public Query createQuery(Class persistType) throws PersistException {
        Query query = null;
        lastAccessTime = System.currentTimeMillis();
        query = new Query();
        query.setDba(classInfo.getDba());
        query.setDataShell(classInfo.getDataShell(persistType));
        return query;
    }

    public Collection query(Class persistType) throws PersistException {
        Query query = null;
        lastAccessTime = System.currentTimeMillis();
        query = new Query();
        query.setDba(classInfo.getDba());
        query.setDataShell(classInfo.getDataShell(persistType));
        return query(query);
    }

    public Collection query(Class persistType, String whereClause) throws PersistException {
        Query query = null;
        lastAccessTime = System.currentTimeMillis();
        query = new Query();
        query.setWhereClause(whereClause);
        query.setDba(classInfo.getDba());
        query.setDataShell(classInfo.getDataShell(persistType));
        return query(query);
    }

    public Collection query(Class persistType, String whereClause, String[] joinTables) throws PersistException {
        Query query = null;
        lastAccessTime = System.currentTimeMillis();
        query = new Query();
        query.setWhereClause(whereClause);
        query.setJoinTables(joinTables);
        query.setDba(classInfo.getDba());
        query.setDataShell(classInfo.getDataShell(persistType));
        return query(query);
    }

    public Collection query(Class persistType, String whereClause, String[] joinTables, boolean escapeWhereClause) throws PersistException {
        Query query = null;
        lastAccessTime = System.currentTimeMillis();
        query = new Query();
        query.setWhereClause(whereClause);
        query.setJoinTables(joinTables);
        query.setEscapeWhereClause(escapeWhereClause);
        query.setDba(classInfo.getDba());
        query.setDataShell(classInfo.getDataShell(persistType));
        return query(query);
    }

    public Collection query(Query query) throws PersistException {
        lastAccessTime = System.currentTimeMillis();
        try {
            Iterator iter = null;
            LinkedList persistList = null;
            Constructor persistCon = null;
            Object[] conArgs = null;
            DataShell shell = null;
            Collection results = null;
            boolean foundInCache = false;
            conArgs = new Object[2];
            conArgs[0] = this;
            shell = query.getDataShell();
            synchronized (lockObject) {
                if (queryCache != null) {
                    results = queryCache.getFromCache(shell.getData(), query.getQueryCacheKey());
                }
                if (results == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Didn't find '" + query.getWhereClause() + "' in cache.");
                    }
                    if (connectionMode == EJB) {
                        results = mdmSession.findBy(classInfo.getDba(), dataSourceJNDIName, shell.getData(), query);
                    } else if (connectionMode == LOCAL) {
                        results = beanInstance.findBy(classInfo.getDba(), dataSourceJNDIName, shell.getData(), query);
                    } else {
                        results = servletClient.findBy(classInfo.getDba(), shell.getData(), query);
                    }
                    if (queryCache != null) {
                        queryCache.addToCache(shell.getData(), query.getQueryCacheKey(), query.getInvalidatedTables(), results);
                    }
                } else {
                    foundInCache = true;
                }
                iter = results.iterator();
            }
            persistList = new LinkedList();
            while (iter.hasNext()) {
                Persist persist = null;
                TableRow data = null;
                DataShell scShell = null;
                data = (TableRow) iter.next();
                try {
                    Oid oid = null;
                    oid = new Oid(data.getTableName(), data.getPrimaryKey());
                    shell = getDataNoLoad(oid);
                } catch (PersistException e) {
                }
                if (!foundInCache) {
                    if (shell != null && (shell.getData().isInvalid() || shell.getData().isDirty())) {
                    } else {
                        shell = getClassInfo().getDataShell(data.getTableName());
                        shell.setData(data);
                    }
                }
                if (data.getSubtypeTableName() != null) {
                    scShell = shell;
                    shell = getClassInfo().getDataShell(data.getSubtypeTableName());
                    shell.getData().setHollow();
                    shell.getData().setPrimaryKey(data.getPrimaryKey());
                    persistCon = getClassInfo().getPersistConstructor(data.getSubtypeTableName());
                } else {
                    persistCon = getClassInfo().getPersistConstructor(data.getTableName());
                }
                conArgs[1] = shell;
                if (shell.getData().isView()) {
                    View view = null;
                    view = (View) persistCon.newInstance(conArgs);
                    persistList.add(view);
                } else {
                    persist = (Persist) persistCon.newInstance(conArgs);
                    persistList.add(persist);
                    if (scShell != null) {
                        addData(null, scShell, scShell.getData().getPrimaryKey());
                    }
                }
            }
            return persistList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistException("Query failed: " + e);
        }
    }

    public void removeFromList(String owningPrimaryKey, String fkColName, String ownedTableName, Persist persist) throws PersistException {
        HashMap map = null;
        OneToManyList list = null;
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            map = (HashMap) oneToManyLists.get(owningPrimaryKey);
        }
        if (map != null) {
            list = (OneToManyList) map.get(fkColName + "->" + ownedTableName);
            if (list != null) {
                list.remove(persist);
                if (!modifiedLists.contains(list)) {
                    modifiedLists.add(list);
                }
            }
        }
    }

    public void invalidateList(String owningPrimaryKey, String fkColName, String ownedTableName) {
        HashMap map = null;
        OneToManyList list = null;
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            map = (HashMap) oneToManyLists.get(owningPrimaryKey);
        }
        if (map != null) {
            list = (OneToManyList) map.get(fkColName + "->" + ownedTableName);
            if (list != null) {
                list.invalidate();
            }
        }
    }

    /**
     * Add a <code>Persist</code>ent object to the <code>PersistentManager</code>.
     *
     * @param  owningOid  The <code>Oid</code> of the object to add the
     *                    <code>Persist</code> to.
     * @param  persist    The <code>Persist</code> object instance to add.
     */
    public void addToOneToManyList(Oid owningOid, String fkColName, Persist persist, Oid ownedOid) throws PersistException {
        HashMap map = null;
        OneToManyList list = null;
        DataShell ownedShell = null;
        DataShell owningShell = null;
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            map = (HashMap) oneToManyLists.get(owningOid.getKey());
            if (map == null) {
                addOneToManyList(owningOid, fkColName, persist.getClass());
                map = (HashMap) oneToManyLists.get(owningOid.getKey());
            }
        }
        list = (OneToManyList) map.get(fkColName + "->" + ownedOid.getTableName());
        if (list == null) {
            throw new PersistException("No list for '" + fkColName + "->" + ownedOid.getTableName() + "' (adding to list)");
        }
        ownedShell = getData(ownedOid);
        ownedShell.getData().setInternalValue(list.getForeignKeyColumnName(), owningOid.getKey());
        owningShell = getDataNoLoad(owningOid);
        list.add(persist);
        if (!modifiedLists.contains(list)) {
            modifiedLists.add(list);
        }
    }

    public Iterator getList(Oid owningOid, String fkColName, String tableName, String whereClause) throws PersistException {
        HashMap map = null;
        OneToManyList list = null;
        lastAccessTime = System.currentTimeMillis();
        map = (HashMap) oneToManyLists.get(owningOid.getKey());
        if (map == null) {
            throw new PersistException("No lists for '" + owningOid + "'");
        }
        list = (OneToManyList) map.get(fkColName + "->" + tableName);
        if (list == null) {
            throw new PersistException("No list for '" + fkColName + "->" + tableName + "' (getting list)");
        }
        return list.iterator(whereClause);
    }

    /**
     * The 1 side of a 1-to-many relationship is a reference.  If a 
     * <code>TableRow</code> object exists for the given data and 
     * pkey then return it.  If not, create a new one, set it's state
     * to be HOLLOW, set it's primary key value to be 'pkey', and 
     * return it.
     *
     * @param  shell The <code>DataShell</code> that is contained by the
     *               referenced <code>Persist</code> object.
     * @param  pkey  The primary key value of the <code>TableRow</code>
     *               object.
     *
     * @return Either the existing <code>DataShell</code> object or the
     *         newly created instance.
     */
    public DataShell getHollowDataShell(DataShell shell, String pkey) {
        Oid oid = null;
        DataShell tmp = null;
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            oid = new Oid(shell.getData().getTableName(), pkey);
            try {
                tmp = getData(oid);
            } catch (PersistException e) {
                tmp = shell;
                tmp.getData().setHollow();
                tmp.getData().setPrimaryKey(pkey);
            }
        }
        return tmp;
    }

    public DataShell getDataNoLoad(Oid oid) throws PersistException {
        DataShell dataShell = null;
        synchronized (lockObject) {
            dataShell = (DataShell) dataObjects.get(oid.toString());
            if (dataShell == null) {
                throw new PersistException("Unknown object for key '" + oid + "'");
            }
        }
        return dataShell;
    }

    public DataShell getData(Oid oid) throws PersistException {
        TableRow data = null;
        DataShell dataShell = null;
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            dataShell = (DataShell) dataObjects.get(oid.toString());
            if (dataShell == null) {
                throw new PersistNotFoundException("Unknown object for key '" + oid + "'");
            }
            data = dataShell.getData();
            if (data.isHollow()) {
                try {
                    if (connectionMode == EJB) {
                        data = mdmSession.load(classInfo.getDba(), dataSourceJNDIName, data);
                    } else if (connectionMode == LOCAL) {
                        data = beanInstance.load(classInfo.getDba(), dataSourceJNDIName, data);
                    } else {
                        data = servletClient.load(classInfo.getDba(), data);
                    }
                    dataShell.setData(data);
                    if (log.isDebugEnabled()) {
                        log.debug("getData(" + name + "): " + dataShell.getData().getSummary());
                    }
                    dataObjects.put(oid.toString(), dataShell);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PersistException("Can't get data for '" + oid + "': " + e);
                }
            }
        }
        return dataShell;
    }

    public CacheInvalidationInfo save() throws PersistException {
        lastAccessTime = System.currentTimeMillis();
        return save((String) null, null);
    }

    public CacheInvalidationInfo save(BusinessRules rules) throws PersistException {
        lastAccessTime = System.currentTimeMillis();
        return save((String) null, rules);
    }

    /**
     * Save the <code>PersistentManager</code> object.  
     *
     * @param  filename  If the <code>PersistentManager</code> can't access
     *                   the EJB server then serialize the <code>PersistentManager</code>
     *                   to a file with this file name.
     */
    public CacheInvalidationInfo save(String filename, BusinessRules rules) throws PersistException {
        CacheInvalidationInfo results = null;
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            if (filename == null || filename.trim().length() == 0) {
                results = saveToEJB(rules).getCacheInfo();
            } else {
                saveToFile(filename);
                results = null;
            }
            modifiedLists.clear();
        }
        results.setPersistentManagerReference(this.toString());
        return results;
    }

    private void saveToFile(String filename) throws PersistException {
        ObjectOutputStream out = null;
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            try {
                out = new ObjectOutputStream(new FileOutputStream(filename));
                out.writeObject(this);
                out.flush();
                out.close();
            } catch (Exception e) {
                log.error("Persisting", e);
                throw new PersistException("ERROR persisting to file: " + e);
            }
        }
    }

    /**
     * Validate all modified and new persist objects since
     * the last save.
     */
    public synchronized void validate() throws SaveValidationException {
        Iterator iter = null;
        SaveValidationException ex = null;
        lastAccessTime = System.currentTimeMillis();
        iter = dataObjects.values().iterator();
        while (iter.hasNext()) {
            DataShell shell = null;
            shell = (DataShell) iter.next();
            if (shell.getData().isDirty() && shell.getData().isValid()) {
                try {
                    shell.getData().setPersistConfig(getPersistConfig(shell.getData().getClassName()));
                    shell.getData().validate();
                } catch (PersistValidationException e) {
                    if (ex == null) {
                        ex = new SaveValidationException();
                    }
                    ex.addPersistInvalidationException(e);
                } catch (PersistException e) {
                    e.printStackTrace();
                }
            }
            if (shell.getData().isNew() && shell.getData().isValid()) {
                try {
                    shell.getData().validate();
                } catch (PersistValidationException e) {
                    if (ex == null) {
                        ex = new SaveValidationException();
                    }
                    ex.addPersistInvalidationException(e);
                } catch (PersistException e) {
                    e.printStackTrace();
                }
            }
        }
        if (ex != null) {
            throw ex;
        }
    }

    public LinkedList getSaveList() throws PersistException {
        Iterator iter = null;
        LinkedList list = null;
        lastAccessTime = System.currentTimeMillis();
        list = new LinkedList();
        iter = removedObjects.iterator();
        while (iter.hasNext()) {
            DataShell shell = null;
            shell = (DataShell) iter.next();
            if (shell.getData().isValid()) {
                list.add(shell.getData());
            }
        }
        Collections.sort(createdObjects, shellComparator);
        iter = createdObjects.iterator();
        while (iter.hasNext()) {
            DataShell shell = null;
            shell = (DataShell) iter.next();
            if (shell.getData().isValid()) {
                if (log.isDebugEnabled()) {
                    log.debug("ADDING OID FOR SAVE (createdObjects): " + shell.getData().getTableName() + "(" + shell.getPrimaryKey() + "," + "audit:" + shell.getData().audit() + ")");
                }
                list.add(shell.getData());
            }
        }
        iter = dataObjects.values().iterator();
        while (iter.hasNext()) {
            DataShell shell = null;
            shell = (DataShell) iter.next();
            if (shell.getData().isDirty() && shell.getData().isValid()) {
                if (log.isDebugEnabled()) {
                    log.debug("ADDING OID FOR SAVE (dataObjects): " + shell.getData().getTableName() + "(" + shell.getPrimaryKey() + "," + "audit:" + shell.getData().audit() + ")");
                }
                list.add(shell.getData());
            }
        }
        return list;
    }

    Collection getAuditTrailHistory(Oid oid) throws PersistException {
        try {
            Collection info = null;
            if (connectionMode == EJB) {
                info = mdmSession.loadAuditTrailHistory(getClassInfo().getDba(), dataSourceJNDIName, oid);
            } else if (connectionMode == LOCAL) {
                info = beanInstance.loadAuditTrailHistory(getClassInfo().getDba(), dataSourceJNDIName, oid);
            } else {
                info = servletClient.loadAuditTrailHistory(getClassInfo().getDba(), oid);
            }
            return info;
        } catch (RemoteException e) {
            e.printStackTrace();
            if (e.detail != null && e.detail instanceof DataIntegrityException) {
                throw (DataIntegrityException) e.detail;
            } else {
                throw new PersistException("Can't load audit trail info.");
            }
        }
    }

    public MetaData getMetaData() throws PersistException {
        lastAccessTime = System.currentTimeMillis();
        try {
            if (metadata == null) {
                if (connectionMode == EJB) {
                    metadata = mdmSession.loadMetaData(dataSourceJNDIName, getClassInfo().getDba());
                } else if (connectionMode == LOCAL) {
                    metadata = beanInstance.loadMetaData(dataSourceJNDIName, getClassInfo().getDba());
                } else {
                    metadata = servletClient.getMetaData(getClassInfo().getDba());
                }
            }
            return metadata;
        } catch (RemoteException e) {
            e.printStackTrace();
            if (e.detail != null && e.detail instanceof DataIntegrityException) {
                throw (DataIntegrityException) e.detail;
            } else {
                throw new PersistException("Can't save contents of cache: " + e);
            }
        }
    }

    public UpdateResults save(LinkedList saveList) throws PersistException {
        lastAccessTime = System.currentTimeMillis();
        try {
            if (connectionMode == EJB) {
                return mdmSession.save(classInfo.getDba(), dataSourceJNDIName, saveList);
            } else if (connectionMode == LOCAL) {
                return beanInstance.save(classInfo.getDba(), dataSourceJNDIName, saveList);
            } else {
                return servletClient.save(classInfo.getDba(), saveList);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            if (e.detail != null) {
                try {
                    throw (org.openware.job.data.DataIntegrityException) e.detail;
                } catch (ClassCastException e2) {
                    log.error("EXCEPTION = ", e.detail);
                }
            }
            throw new PersistException("Can't save contents of cache: " + e);
        }
    }

    /**
     * Do a raw update on the passed in shell.  The persistent manager
     * does nothing but call the raw update method in the EJB.  If you
     * want to change the state you will need to do this explicitly.
     * 
     * @param  shell  The table row to do the update on.
     *
     * @return <code>true</code> if the shell was updated, <code>false</code>
     *         otherwise.
     */
    public boolean rawUpdate(TableRow shell) throws PersistException {
        lastAccessTime = System.currentTimeMillis();
        if (shell.getRawUpdate() != null) {
            try {
                int numrows;
                if (connectionMode == EJB) {
                    numrows = mdmSession.rawUpdate(dataSourceJNDIName, shell);
                } else if (connectionMode == LOCAL) {
                    numrows = beanInstance.rawUpdate(dataSourceJNDIName, shell);
                } else {
                    numrows = servletClient.rawUpdate(shell);
                }
                return numrows == 1;
            } catch (RemoteException e) {
                e.printStackTrace();
                if (e.detail != null) {
                    throw new PersistException(e.detail.getMessage());
                } else {
                    throw new PersistException(e.getMessage());
                }
            }
        } else {
            throw new PersistException("No raw update implementation defined for: " + shell);
        }
    }

    public void update(UpdateResults ur) throws PersistException {
        Iterator iter = null;
        Collection coll = null;
        lastAccessTime = System.currentTimeMillis();
        if (ur != null) {
            if (log.isDebugEnabled()) {
                log.debug("UPDATING LOCAL CACHE...");
            }
            coll = ur.getDataShells();
            iter = coll.iterator();
            while (iter.hasNext()) {
                TableRow data = null;
                Oid oid = null;
                data = (TableRow) iter.next();
                oid = new Oid(data.getTableName(), data.getPrimaryKey());
                if (data.isRemoved()) {
                    dataObjects.remove(oid.toString());
                    if (log.isDebugEnabled()) {
                        log.debug(oid.toString() + " REMOVED FROM PMANAGER");
                    }
                } else {
                    DataShell shell = null;
                    shell = (DataShell) dataObjects.get(oid.toString());
                    if (shell == null) {
                        throw new PersistException("Couldn't find shell for " + oid);
                    }
                    shell.setData(data);
                    if (log.isDebugEnabled()) {
                        log.debug(oid.toString() + " UPDATED WITH OVN = " + shell.getData().getInternalValue("ObjectVersionNum"));
                    }
                }
            }
            removedObjects.clear();
            createdObjects.clear();
            CacheManager.updateCaches(this, ur.getCacheInfo());
            if (queryCache != null) {
                queryCache.invalidateCache(ur.getCacheInfo());
            }
        }
    }

    public PersistConfig getPersistConfig(String className) {
        lastAccessTime = System.currentTimeMillis();
        return getClassInfo().getValidationConfig().getPersistConfig(className);
    }

    private UpdateResults saveToEJB(BusinessRules rules) throws PersistException {
        UpdateResults ur = null;
        synchronized (lockObject) {
            LinkedList list = null;
            LinkedList newPersists = null;
            LinkedList modifiedPersists = null;
            HashMap oldModifiedPersists = null;
            newPersists = new LinkedList();
            modifiedPersists = new LinkedList();
            oldModifiedPersists = new HashMap();
            list = getSaveList();
            if (rules != null && rules.hasRules()) {
                Iterator iter = null;
                BusinessRuleExceptions exs = null;
                iter = list.iterator();
                while (iter.hasNext()) {
                    TableRow data = null;
                    Persist persist = null;
                    data = (TableRow) iter.next();
                    try {
                        persist = getPersist(data.getTableName() + "(" + data.getPrimaryKey() + ")");
                        if (persist.getDataShell().getData().isNew()) {
                            newPersists.add(persist);
                        }
                        if (persist.getDataShell().getData().isDirty()) {
                            modifiedPersists.add(persist);
                            oldModifiedPersists.put(persist.getOid(), persist.getDataShell().getData().getOldOne());
                        }
                        rules.testRule(persist);
                    } catch (BusinessRuleException e) {
                        if (exs == null) {
                            exs = new BusinessRuleExceptions();
                        }
                        e.setPersist(persist);
                        exs.addException(e);
                    }
                }
                if (exs != null) {
                    throw exs;
                }
            }
            ur = save(list);
            try {
                Iterator iter = null;
                iter = newPersists.iterator();
                while (iter.hasNext()) {
                    Persist persist = null;
                    persist = (Persist) iter.next();
                    rules.fireNewTriggers(persist);
                }
                iter = modifiedPersists.iterator();
                while (iter.hasNext()) {
                    Persist persist = null;
                    TableRow oldOne = null;
                    persist = (Persist) iter.next();
                    oldOne = (TableRow) oldModifiedPersists.get(persist.getOid());
                    persist.getDataShell().getData().setOldOne(oldOne);
                    try {
                        rules.fireModifiedTriggers(persist);
                    } catch (TriggerException e) {
                        throw e;
                    } finally {
                        persist.getDataShell().getData().setOldOne(null);
                    }
                }
            } catch (TriggerException e) {
                log.error("Couldn't fire trigger", e);
            } finally {
                update(ur);
            }
        }
        return ur;
    }

    /**
     * Clear out all of the caches so that everything will come
     * from database.  Any new items will <strong>not</strong>
     * be added to the database and any removed items will not 
     * be removed.  If you want to make sure your changes get 
     * commited you should call the <code>save</code> method 
     * before calling this method.
     */
    public void purgeCaches() {
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            Iterator values = null;
            removedObjects.clear();
            createdObjects.clear();
            values = dataObjects.values().iterator();
            while (values.hasNext()) {
                DataShell shell = null;
                shell = (DataShell) values.next();
                shell.getData().setHollow();
            }
            values = oneToManyLists.values().iterator();
            while (values.hasNext()) {
                Iterator iter = null;
                iter = ((HashMap) values.next()).values().iterator();
                while (iter.hasNext()) {
                    ((OneToManyList) iter.next()).invalidate(true);
                }
            }
            if (queryCache != null) {
                queryCache.purgeCache();
            }
        }
    }

    public void updateCache(CacheInvalidationInfo cacheInfo) {
        Iterator iter = null;
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            iter = cacheInfo.getDeletedOids();
            while (iter.hasNext()) {
                Oid oid = null;
                oid = (Oid) iter.next();
                dataObjects.remove(oid.toString());
                if (log.isDebugEnabled()) {
                    log.debug("UPDATE_CACHE: removing " + oid);
                }
            }
            iter = cacheInfo.getModifiedOids();
            while (iter.hasNext()) {
                Oid oid = null;
                DataShell shell = null;
                oid = (Oid) iter.next();
                shell = (DataShell) dataObjects.get(oid.toString());
                if (shell != null) {
                    if (!shell.getData().isDirty()) {
                        shell.getData().setHollow();
                        if (log.isDebugEnabled()) {
                            log.debug("UPDATE_CACHE: making hollow " + oid);
                        }
                    }
                }
            }
            iter = cacheInfo.getListInvalidators();
            while (iter.hasNext()) {
                ListInvalidator li = null;
                li = (ListInvalidator) iter.next();
                invalidateList(li.getOwningPrimaryKey(), li.getForeignKeyColumnName(), li.getOwnedTableName());
                if (log.isDebugEnabled()) {
                    log.debug("UPDATE_CACHE: invalidating list " + li);
                }
            }
            if (queryCache != null) {
                queryCache.invalidateCache(cacheInfo);
            }
        }
    }

    /**
     * Purge any objects that were created by then set to
     * be invalid.
     */
    public void purgeNewInvalidPersists() {
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            Iterator iter = null;
            iter = createdObjects.iterator();
            while (iter.hasNext()) {
                DataShell shell = null;
                shell = (DataShell) iter.next();
                if (shell.getData().isInvalid()) {
                    iter.remove();
                }
            }
            iter = dataObjects.keySet().iterator();
            while (iter.hasNext()) {
                DataShell shell = null;
                shell = (DataShell) iter.next();
                if (shell.getData().isNew() && shell.getData().isInvalid()) {
                    iter.remove();
                }
            }
        }
    }

    /**
     * Revert back to the state of the <code>PersistentManager</code>
     * at the time immediately after that last <code>save</code>
     * operation (or after the creation of the <code>PersistentManager</code>).
     */
    public void revert() {
        Iterator iter = null;
        LinkedList deleteObjects = null;
        lastAccessTime = System.currentTimeMillis();
        synchronized (lockObject) {
            iter = modifiedLists.iterator();
            while (iter.hasNext()) {
                ((OneToManyList) iter.next()).invalidate();
            }
            modifiedLists.clear();
            iter = createdObjects.iterator();
            while (iter.hasNext()) {
                DataShell shell = null;
                shell = (DataShell) iter.next();
                if (shell.getData().isValid()) {
                    iter.remove();
                }
            }
            iter = removedObjects.iterator();
            while (iter.hasNext()) {
                DataShell shell = null;
                shell = (DataShell) iter.next();
                shell.getData().setHollow();
                shell.getData().setValid();
            }
            removedObjects.clear();
            iter = dataObjects.keySet().iterator();
            deleteObjects = new LinkedList();
            while (iter.hasNext()) {
                DataShell shell = null;
                String key = null;
                key = (String) iter.next();
                shell = (DataShell) dataObjects.get(key);
                if (shell.getData().isNew()) {
                    if (shell.getData().isValid()) {
                        deleteObjects.add(key);
                    }
                } else {
                    shell.getData().setValid();
                    if (shell.getData().isDirty()) {
                        shell.getData().setHollow();
                    }
                }
            }
            iter = deleteObjects.iterator();
            while (iter.hasNext()) {
                String key = null;
                key = (String) iter.next();
                dataObjects.remove(key);
            }
        }
    }

    /**
     * Execute a raw query against the data source pointed by this
     * <code>PersistentManager</code>.  A <code>Collection</code>
     * of <code>DataRow</code> objects is returned.
     *
     * @param  query  The SQL Select statement to execute.
     *
     * @return A <code>Collection</code> of <code>DataRow</code> 
     *         objects is returned.
     */
    public Collection rawQuery(String query) throws PersistException {
        lastAccessTime = System.currentTimeMillis();
        try {
            if (connectionMode == EJB) {
                return mdmSession.rawQuery(query, dataSourceJNDIName);
            } else if (connectionMode == LOCAL) {
                return beanInstance.rawQuery(query, dataSourceJNDIName);
            } else {
                return servletClient.rawQuery(query);
            }
        } catch (RemoteException e) {
            log.error("Error doing raw query", e);
            if (e.detail != null) {
                throw new PersistException(e.detail.getMessage());
            } else {
                throw new PersistException(e.getMessage());
            }
        }
    }

    public Collection findBy(String jndiDataSource, TableRow data, Query query) throws RemoteException {
        lastAccessTime = System.currentTimeMillis();
        query.setEscapeWhereClause(true);
        if (connectionMode == EJB) {
            return mdmSession.findBy(classInfo.getDba(), jndiDataSource, data, query);
        } else if (connectionMode == LOCAL) {
            return beanInstance.findBy(classInfo.getDba(), jndiDataSource, data, query);
        } else {
            try {
                return servletClient.findBy(classInfo.getDba(), data, query);
            } catch (PersistException e) {
                throw new RemoteException("Error accessing servlet", e);
            }
        }
    }
}

package de.gstpl.data.cayenne;

import de.gstpl.data.ApplicationProperties;
import de.gstpl.data.IDBColumn;
import de.gstpl.data.DBException;
import de.gstpl.data.DBInternalException;
import de.gstpl.data.IDBProperties;
import de.gstpl.data.DBRuntimeException;
import de.gstpl.data.GDB;
import de.gstpl.data.ISchemaTableModel;
import de.gstpl.data.IPerson;
import de.gstpl.data.IRoom;
import de.gstpl.data.ISubject;
import de.gstpl.data.ITimeInterval;
import de.gstpl.data.ITimeable;
import de.gstpl.data.ITimetable;
import de.gstpl.data.cayenne.person.GroupCayenneImpl;
import de.gstpl.data.person.IGroup;
import de.gstpl.data.set.IYearSet;
import de.gstpl.gui.TextAreaHandler;
import de.gstpl.util.ServerEngine;
import de.gstpl.util.server.ConnectionProperties;
import de.peathal.resource.L;
import de.peathal.util.IWorker;
import de.peathal.util.WorkerImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.sql.DataSource;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.DbGenerator;
import org.apache.cayenne.access.MyEntitySorterUtil;
import org.apache.cayenne.access.QueryLogger;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.conf.DefaultConfiguration;
import org.apache.cayenne.conn.DriverDataSource;
import org.apache.cayenne.dba.AutoAdapter;
import org.apache.cayenne.dba.DbAdapter;
import org.apache.cayenne.dba.TypesMapping;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.map.Attribute;
import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjAttribute;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.query.NamedQuery;
import org.apache.cayenne.query.QueryChain;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.validation.ValidationResult;

/**
 * This class is the specific implementation of GDB for the database
 * layer Cayenne. More precise Cayenne is an object relational mapping tool
 * which connects the object oriented world with the table based sql world.
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class DBCayenneImpl extends GDB {

    private DBPropertiesCayenneImpl dbProperties;

    private DataContext context;

    private static final String USER_POSTFIX = "_U";

    /**
     * This variables is a placeholder for the tablename of all relations
     *  between persons and subjects.
     */
    private static final String PS = "PERSON_SUBJECT";

    /** This variables is a placeholder for the tablename of the relationship table: groupName_id and nameOfColumn */
    private static final String VT = "VISIBLE_TABLE";

    /** This variables is a placeholder for the tablename of the db properties. */
    private static final String DBP = "DB_PROPERTIES";

    /** This variables is a placeholder for the tablename of all groups. */
    private static final String GR = "GROUPY";

    /** This variables is a placeholder for the tablename of all persons. */
    private static final String P = "PERSON";

    /** This variables is a placeholder for the tablename of all rooms. */
    private static final String R = "ROOM";

    /** This variables is a placeholder for the tablename of all subject. */
    private static final String S = "SUBJECT";

    /** This variables is a placeholder for the tablename of all timeintervals. */
    private static final String TI = "TIME_INTERVAL";

    private static String[] getAllDbEntitiesAsString() {
        String[] str = { TI, R, PS, S, P, DBP, GR, VT };
        return str;
    }

    public DBCayenneImpl() {
    }

    @Override
    protected void initConfig() {
        try {
            org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(QueryLogger.class);
            if (ApplicationProperties.get().getLogDatabase()) {
                log4j.setLevel(org.apache.log4j.Level.INFO);
            } else {
                log4j.setLevel(org.apache.log4j.Level.WARN);
            }
            Configuration.configureCommonLogging(ApplicationProperties.get().getLogPropertiesLocation().toURI().toURL());
        } catch (MalformedURLException exc) {
            getDBLog().log(Level.WARNING, L.tr("Can't_change_logging_configuration!"), exc);
        }
        DefaultConfiguration config = new DefaultConfiguration();
        ConnectionProperties helper = ApplicationProperties.get().getConnectionProperties();
        if (ServerEngine.isStarted()) {
            helper = (ConnectionProperties) ServerEngine.get().getEmbeddedDBHint(helper);
        }
        config.setDataSourceFactory(new MyDataSourceFactory(helper));
        config.installConfigurationShutdownHook();
        Configuration.initializeSharedConfiguration(config);
        getDBLog().info(L.tr("._Connection:_") + helper);
    }

    CayenneTextAreaHandler logHandler;

    @Override
    public synchronized TextAreaHandler getLogTextArea() {
        if (logHandler == null) {
            logHandler = new CayenneTextAreaHandler();
            if (ApplicationProperties.get().getLogDatabase()) {
                org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(QueryLogger.class);
                log4j.addAppender(logHandler.getLog4J());
            }
        }
        return logHandler;
    }

    @Override
    public synchronized void initDB() throws DBException {
        try {
            if (isInited()) {
                return;
            }
            new GUIAppender().initGSTPLMenu();
            initConfig();
            context = DataContext.createDataContext();
            createSchema(false);
            dbProperties = _getDBProperties();
            dbProperties.setDefault();
            String v = getDBProperties().getVersion();
            getDBLog().info(L.tr("Database_version:_") + v);
            if (!"0.0.7".equals(v)) {
                getDBLog().warning(L.tr("It_is_not_save_to_use_this_database_(incompatible_version)!_Nevertheless_I_will_try_to_use_..._"));
            }
            getDBLog().info(L.tr("Map_file_location:_") + getDataMap().getLocation());
            successOfInitialization = true;
        } catch (Exception ex) {
            if (ex.getCause() != null && ex.getCause().getCause() instanceof SQLException) {
                throw new DBException(L.tr("Can't_init_database!_Server_down?"), ex);
            } else {
                throw new DBException(L.tr("Can't_init_database!"), ex);
            }
        }
    }

    /**
     * This method creates a schema with several tables specified in the DataMap.
     * If we overwrite the schema there could be a different mapping! So we
     * have to refresh the local DataMap.
     */
    private String createSchema(boolean overwrite) throws DBException {
        ValidationResult failures = null;
        DataMap map = getDataMap();
        try {
            ConnectionProperties cHelper = ApplicationProperties.get().getConnectionProperties();
            String url = cHelper.getUrl();
            if (ConnectionProperties.DERBY_CLIENT.equals(cHelper.getDriverName()) && url.indexOf("create=true") == -1) {
                if (!url.endsWith(";")) {
                    url += ";";
                }
                url += "create=true;";
            }
            Driver driver = (Driver) Class.forName(cHelper.getDriverName()).newInstance();
            DataSource dataSource = new DriverDataSource(driver, url, cHelper.getUserName(), cHelper.getPassword());
            DbAdapter adapter = new AutoAdapter(dataSource);
            DbGenerator generator = new DbGenerator(adapter, map);
            if (overwrite) {
                generator.setShouldDropTables(true);
                generator.setShouldDropPKSupport(true);
                getContext().unregisterObjects(Collections.singletonList(getDBProperties()));
            }
            String ret;
            if (overwrite || EMPTY.equals(ret = getAvailableTables(dataSource))) {
                getDBLog().info(L.tr("Start_creating_database_schema!"));
                generator.runGenerator(dataSource);
                failures = generator.getFailures();
                if (failures == null || !failures.hasFailures()) {
                    getDBLog().info(L.tr("Schema_Generation_Complete."));
                } else {
                    getDBLog().info(L.tr("Failures_while_generating_schema_for_database:"));
                    getDBLog().info(failures.toString());
                }
            } else {
                if (ALL.equals(ret)) {
                    getDBLog().info(L.tr("Schema_exists!_Nothing_to_do!"));
                } else {
                    getDBLog().severe(L.tr("Schema_incomplete!Missing_tables:") + ret);
                }
            }
        } catch (Exception exc) {
            throw new DBException(exc);
        }
        if (overwrite) {
            getContext().registerNewObject(dbProperties);
            dbProperties.saveDataMap();
            dbProperties.updateDataMap();
        }
        if (failures != null) {
            return failures.toString();
        } else {
            return L.tr("No_failures_while_creating_database_schema!");
        }
    }

    /**
     * This method creates the database schema. It will overwrite any existent
     * table!
     * @return a String describing the results: failures or other.
     */
    public synchronized String createSchema() throws DBException {
        return createSchema(true);
    }

    synchronized void setLocalMap(DataMap map) {
        EntityResolver er = getContext().getEntityResolver();
        er.setDataMaps(Collections.singletonList(map));
        getDBLog().info(L.tr("Local_map_updated."));
        MyEntitySorterUtil.refreshSorter(getContext().getParentDataDomain());
    }

    public static final String ALL = "All";

    public static final String EMPTY = "Emtpy";

    /**
     * This method returns true if gstpl is able to create the database schema.
     */
    private synchronized String getAvailableTables(DataSource dataSource) throws DBException {
        StringBuffer retTables = new StringBuffer();
        Connection c;
        try {
            c = dataSource.getConnection();
            try {
                String[] str = getAllDbEntitiesAsString();
                for (int i = 0; i < str.length; i++) {
                    executeRawSQL(c, "select * from " + str[i]);
                    retTables.append(str[i]);
                    if (i < retTables.length() - 1) {
                        retTables.append("; ");
                    }
                }
                return ALL;
            } catch (Exception ex) {
                getDBLog().log(Level.INFO, L.tr("Following_exception_can_be_ignored:"), ex);
            } finally {
                c.close();
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
        if (retTables.length() == 0) {
            return EMPTY;
        }
        return retTables.toString();
    }

    /**
     * get DataSource via dataSource = new DriverDataSource(driver, url,
     *               cHelper.getUserName(), cHelper.getPassword());
     *
     */
    private void executeRawSQL(Connection c, String str) throws Exception {
        Statement st = c.createStatement();
        try {
            st.executeQuery(str);
        } finally {
            st.close();
        }
    }

    /**
     * This method intis the current DataMap with the 'old' Map in IDBProperties.
     */
    public synchronized void undoMapChanges() {
        setLocalMap(dbProperties.getDataMap());
    }

    /**
     * This method returns the cayenne specific context. It is the central point
     * to do some queries etc. in cayenne.
     */
    private DataContext getContext() {
        return context;
    }

    private DataContext getTmpContext() {
        return context;
    }

    @Override
    public void saveChanges() throws DBException {
        try {
            synchronized (this) {
                getContext().commitChanges();
            }
            getDBLog().info(L.tr("Saved_changes!"));
        } catch (Exception ex) {
            throw new DBException(ex.getLocalizedMessage(), ex.getCause());
        }
    }

    @Override
    public synchronized void compressData() {
    }

    @Override
    public synchronized void rollback() {
        Collection modObj = getContext().modifiedObjects();
        getContext().rollbackChanges();
        for (Object o : modObj) {
            if (o == null) {
                continue;
            }
            synchronized (o) {
                if (o instanceof ITimeInterval) {
                    TimeIntervalCayenneImpl ti = (TimeIntervalCayenneImpl) o;
                    if (ti.getSubject() != null) {
                        ti.getSubject().isRegistered();
                        Collection persons = ti.getSubject().getPersons();
                        for (Object p : persons) {
                            ((PersonCayenneImpl) p).internalRollback();
                        }
                        ((SubjectCayenneImpl) ti.getSubject()).internalRollback();
                    }
                    if (ti.getRoom() != null) {
                        ((RoomCayenneImpl) ti.getRoom()).internalRollback();
                    }
                    ti.internalRollback();
                } else if (o instanceof ITimetable) {
                    ((TimetableCayenneImpl) o).internalRollback();
                } else {
                    assert false : "Should not happen!" + "no valid class";
                }
            }
        }
    }

    @Override
    public boolean isCayenne() {
        return true;
    }

    @Override
    public boolean hasChanges() {
        synchronized (this) {
            return getContext().hasChanges();
        }
    }

    @Override
    public <T> void dump(Class<T> interfaze) {
        Class<? extends T> impl = getImpl(interfaze);
        NamedQuery rawSelect = new NamedQuery("SelectTable", Collections.singletonMap("table", getTableName(impl)));
        getDBLog().info(L.tr("Dump_") + impl.toString());
        List l = null;
        try {
            synchronized (this) {
                l = getContext().performQuery(rawSelect);
            }
            for (Iterator iter = l.iterator(); iter.hasNext(); ) {
                getDBLog().info(iter.next().toString());
            }
        } catch (Exception ex) {
            getDBLog().log(Level.SEVERE, L.tr("Can't_query_database_to_dump_:") + impl.toString(), ex);
        }
    }

    @Override
    protected <T extends ITimeable> T create(boolean register, Class<T> interfaze, Map<String, Object> properties) throws DBException {
        T t = super.create(register, interfaze, properties);
        if (!register) {
            initTimeable((TimeableCayenneImpl) t);
        }
        return t;
    }

    @Override
    public ITimeInterval createTI(boolean register, int start, int duration, IYearSet yearSet, ISubject subject, IRoom room, boolean setReverseRelation) throws DBException {
        ITimeInterval ti = super.createTI(register, start, duration, yearSet, subject, room, setReverseRelation);
        if (!register) {
            initTimeable((TimeableCayenneImpl) ti);
        }
        return ti;
    }

    private void initTimeable(TimeableCayenneImpl tac) {
        for (String s : tac.getToManyRelationShips()) {
            tac.getMap().put(s, new ArrayList(0));
        }
        tac.assignID();
    }

    @Override
    public synchronized <T extends ITimeable> T register(T timeable) {
        _register((TimeableCayenneImpl) timeable, true);
        return timeable;
    }

    @Override
    public <T extends ITimeable> void unregister(Collection<T> timeables) {
        for (ITimeable timeable : timeables) {
            _register((TimeableCayenneImpl) timeable, false);
        }
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public <T extends ITimeable> List<T> getAll(Class<T> interfaze, String lastName) throws DBException {
        SelectQuery selectQ;
        List<T> l = null;
        Class clazz = getImpl(interfaze);
        synchronized (this) {
            try {
                selectQ = new SelectQuery(clazz);
                if ((IPerson.class.isAssignableFrom(clazz) || IRoom.class.isAssignableFrom(clazz) || ISubject.class.isAssignableFrom(clazz)) && lastName != null) {
                    Map params = Collections.singletonMap("aname", lastName);
                    selectQ.setQualifier(Expression.fromString("name = $aname").expWithParameters(params, false));
                }
                selectQ.setFetchLimit(getFetchLimit());
                l = getContext().performQuery(selectQ);
            } catch (Exception e) {
                throw new DBException(L.tr("Can't_query_database_to_get_all_entries_for:") + clazz.toString(), e);
            }
        }
        return l;
    }

    @Override
    public void simpleRemove(ITimeable timeable) throws DBException {
        if (timeable == null) {
            throw new DBRuntimeException(L.tr("Timeable_can't_be_null!"));
        }
        synchronized (this) {
            try {
                getContext().deleteObject((TimeableCayenneImpl) timeable);
            } catch (Exception ex) {
                throw new DBException(L.tr("Can't_remove_timeable:_") + timeable, ex);
            }
        }
    }

    private String[] keys = { "start", "duration", "roomId", "subjectId" };

    private Object[] values = new Object[4];

    @Override
    protected ITimeInterval getTI(int start, int duration, IYearSet yearSet, ISubject subject, IRoom room) {
        values[0] = Integer.valueOf(start);
        values[1] = Integer.valueOf(duration);
        values[2] = (room != null) ? room : null;
        values[3] = (subject != null) ? subject : null;
        List list;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(keys[0], values[0]);
        params.put(keys[1], values[1]);
        params.put(keys[2], values[2]);
        params.put(keys[3], values[3]);
        SelectQuery q = new SelectQuery(getImpl(ITimeInterval.class));
        q.setQualifier(Expression.fromString("startTime=$start and durationTime=$duration " + "and room=$roomId and subject=$subjectId").expWithParameters(params, false));
        synchronized (this) {
            list = getContext().performQuery(q);
        }
        ITimeInterval ti = null;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            ti = (ITimeInterval) iter.next();
            if (ti.getYearSet().equals(yearSet)) {
                break;
            }
            throw new DBInternalException(L.tr("Query_returns_not_only_one_TimeInterval") + L.tr("_the_first_was:") + ti);
        }
        return ti;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ITimeable> List<T> execute(Class<T> interfaze, String sqlString) throws Exception {
        SQLTemplate rawSelect = new SQLTemplate(getImpl(interfaze), sqlString);
        rawSelect.setFetchingDataRows(true);
        rawSelect.setFetchLimit(getFetchLimit());
        List<T> l;
        synchronized (this) {
            l = getContext().performQuery(rawSelect);
        }
        return l;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public <T extends ITimeable> Map<String, Class> getMixedHeader(Class<T> interfaze) {
        synchronized (this) {
            DbEntity entity = getDbEntity(getImpl(interfaze));
            Collection<Attribute> coll = new ArrayList<Attribute>(Collections.checkedCollection((Collection<Attribute>) entity.getAttributes(), Attribute.class));
            coll.addAll(Collections.checkedCollection((Collection<Attribute>) entity.getGeneratedAttributes(), Attribute.class));
            Iterator<Attribute> iter = coll.iterator();
            Map<String, Class> colDefinition = new HashMap<String, Class>();
            DbAttribute a;
            while (iter.hasNext()) {
                a = (DbAttribute) iter.next();
                try {
                    colDefinition.put(a.getName(), getClass(TypesMapping.getJavaBySqlType(a.getType())));
                } catch (Exception exc) {
                    getDBLog().log(Level.WARNING, L.tr("Can't_put:") + a.getName() + L.tr("_into_the_result_map!"), exc);
                }
            }
            return colDefinition;
        }
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public <T extends ITimeable> Map<String, IDBColumn> getHeader(Class<T> interfaze) {
        Map<String, IDBColumn> colDefinition;
        synchronized (this) {
            DbEntity entity = getDbEntity(getImpl(interfaze));
            Collection<Attribute> coll = new ArrayList<Attribute>(Collections.checkedCollection((Collection<Attribute>) entity.getAttributes(), Attribute.class));
            coll.addAll(Collections.checkedCollection((Collection<Attribute>) entity.getGeneratedAttributes(), Attribute.class));
            Iterator iter = coll.iterator();
            colDefinition = new HashMap<String, IDBColumn>();
            DbAttribute a;
            while (iter.hasNext()) {
                a = (DbAttribute) iter.next();
                colDefinition.put(a.getName(), new DBColumnCayenneImpl(a));
            }
        }
        return Collections.unmodifiableMap(colDefinition);
    }

    @Override
    public <T extends ITimeable> IDBColumn addColumn(Class<T> interfaze, String columnName, int type) throws DBException {
        if (columnName == null) {
            return null;
        }
        Class<? extends T> impl = getImpl(interfaze);
        columnName += DBCayenneImpl.USER_POSTFIX;
        DbEntity entity = getDbEntity(impl);
        DbAttribute dbA = new DbAttribute(columnName, type, entity);
        try {
            entity.addAttribute(dbA);
        } catch (IllegalArgumentException exc) {
            return null;
        }
        ObjEntity oEnt = getObjEntity(impl);
        ObjAttribute oAttr = new ObjAttribute(columnName, TypesMapping.getJavaBySqlType(type), oEnt);
        oAttr.setDbAttribute(dbA);
        oEnt.addAttribute(oAttr);
        saveChanges();
        return new DBColumnCayenneImpl(dbA, oAttr);
    }

    @Override
    public <T extends ITimeable> boolean removeColumn(Class<T> interfaze, String columnName) throws DBException {
        if (columnName == null || !isUserProperty(columnName)) {
            return false;
        }
        DbEntity entity = getDbEntity(getImpl(interfaze));
        if (entity.getAttribute(columnName) == null) {
            return false;
        }
        entity.removeAttribute(columnName);
        saveChanges();
        return true;
    }

    @Override
    public synchronized void update(ITimeable ta, String propName, Object value) throws IllegalArgumentException {
        String key = toObjPropertyName(ta, propName);
        if (key != null) {
            ((TimeableCayenneImpl) ta).writeProperty(key, value);
        }
    }

    /**
     * This method returns the name of the specified columnName for specified ta.
     * @return the property. If columnName is an ID it returns null.
     */
    private String toObjPropertyName(ITimeable ta, String columnName) {
        TimeableCayenneImpl taNew = (TimeableCayenneImpl) ta;
        DbEntity entity = getContext().getEntityResolver().lookupDbEntity(taNew);
        DbAttribute a = (DbAttribute) entity.getAttribute(columnName);
        if (a == null) {
            return null;
        }
        ObjAttribute oA = taNew.getObjEntity().getAttributeForDbAttribute(a);
        if (oA == null) {
            return null;
        }
        return oA.getName();
    }

    private <T extends ITimeable> DbEntity getDbEntity(Class<T> impl) {
        return getObjEntity(impl).getDbEntity();
    }

    private <T extends ITimeable> ObjEntity getObjEntity(Class<T> impl) {
        return getDataMap().getObjEntityForJavaClass(impl.getName());
    }

    public String getTableName(Class interfaze) {
        if (IPerson.class.isAssignableFrom(interfaze)) {
            return P;
        } else if (ISubject.class.isAssignableFrom(interfaze)) {
            return S;
        } else if (IRoom.class.isAssignableFrom(interfaze)) {
            return R;
        } else if (ITimeInterval.class.isAssignableFrom(interfaze)) {
            return TI;
        } else if (IGroup.class.isAssignableFrom(interfaze)) {
            return GR;
        } else if (IDBProperties.class.isAssignableFrom(interfaze)) {
            return DBP;
        } else {
            throw new UnsupportedOperationException(L.tr("Can't_get_table_for:_") + interfaze.toString());
        }
    }

    @Override
    public Class getClass(String tableName) {
        Class clazz;
        if (P.equals(tableName)) {
            clazz = PersonCayenneImpl.class;
        } else if (S.equals(tableName)) {
            clazz = SubjectCayenneImpl.class;
        } else if (R.equals(tableName)) {
            clazz = RoomCayenneImpl.class;
        } else if (TI.equals(tableName)) {
            clazz = TimeIntervalCayenneImpl.class;
        } else if (GR.equals(tableName)) {
            clazz = GroupCayenneImpl.class;
        } else if (DBP.equals(tableName)) {
            clazz = DBPropertiesCayenneImpl.class;
        } else {
            try {
                clazz = super.getClass(tableName);
            } catch (ClassNotFoundException ex) {
                throw new UnsupportedOperationException(ex.getCause());
            }
        }
        return clazz;
    }

    @Override
    public <T extends ITimeable> void addClasses(List<Class> result, Class<T> interfaze, Collection<String> columnNames) {
        Class<? extends T> impl = getImpl(interfaze);
        DbEntity dbEnt = getDbEntity(impl);
        ObjEntity oEnt = getObjEntity(impl);
        Iterator<String> colNamesIter = columnNames.iterator();
        DbAttribute a;
        String str = null;
        while (colNamesIter.hasNext()) {
            a = (DbAttribute) dbEnt.getAttribute(str = colNamesIter.next());
            if (a == null) {
                getDBLog().warning(L.tr("Can't_get_db_property_from_possible_unmapped:_") + str);
            } else if (a.isPrimaryKey() || a.isForeignKey()) {
                try {
                    result.add(Class.forName(TypesMapping.getJavaBySqlType(a.getType())));
                } catch (ClassNotFoundException exc) {
                    throw new DBRuntimeException(L.tr("Can't_get_java_class_from_string!_Attribute:") + str + L.tr(",_table_name:") + impl);
                }
            } else {
                ObjAttribute oA = oEnt.getAttributeForDbAttribute(a);
                if (oA == null) {
                    throw new DBRuntimeException(L.tr("Can't_get_property_for_attribute:_") + a.getName());
                }
                try {
                    Class attrClazz = getClass(oA.getType());
                    result.add(attrClazz);
                } catch (Exception ex) {
                    getDBLog().log(Level.WARNING, L.tr("Can't_get_java_class_from_string!_Attribute:") + str + L.tr(",_table_name:") + impl);
                    continue;
                }
            }
        }
    }

    /**
     * Taken from cayenne petstore example
     */
    @Override
    public void runScript(final Reader rawReader) throws Exception {
        BufferedReader reader = new BufferedReader(rawReader);
        DataMap map = getDataMap();
        QueryChain chain = new QueryChain();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.endsWith(";")) {
                line = line.substring(0, line.length() - 1);
            }
            chain.addQuery(new SQLTemplate(map, line));
        }
        try {
            reader.close();
        } catch (IOException e) {
            getDBLog().log(Level.WARNING, L.tr("IOException while closing reader!"), e);
        }
        if (!chain.isEmpty()) {
            getContext().performNonSelectingQuery(chain);
        }
    }

    @SuppressWarnings(value = "unchecked")
    protected void _register(TimeableCayenneImpl o, boolean register) {
        if (register != o.isRegistered()) {
            if (register) {
                String[] toMany = o.getToManyRelationShips();
                String[] toOne = o.getToOneRelationShips();
                Map<String, Object> props = o.getMap();
                getContext().registerNewObject(o);
                for (String s : toMany) {
                    Collection list = (Collection) props.get(s);
                    if (list != null) {
                        for (Object da : list) {
                            o.addToManyTarget((ITimeable) da, s, false);
                        }
                    }
                    props.remove(s);
                }
                for (String s : toOne) {
                    ITimeable da = (ITimeable) props.get(s);
                    if (da != null) {
                        o.setToOneTarget(da, s, false);
                    }
                    props.remove(s);
                }
                o.writeProperties(props);
                props.clear();
            } else {
                String[] toMany = o.getToManyRelationShips();
                String[] toOne = o.getToOneRelationShips();
                Map<String, Object> props = new HashMap<String, Object>();
                for (String s : toMany) {
                    Collection c = (Collection) o.readProperty(s);
                    List l = new ArrayList();
                    if (c != null) {
                        l.addAll(c);
                    }
                    props.put(s, l);
                }
                for (String s : toOne) {
                    props.put(s, o.readProperty(s));
                }
                Set<String> varKeys = getVariables(o.getClass()).keySet();
                for (String s : varKeys) {
                    props.put(s, o.readProperty(s));
                }
                getContext().deleteObject(o);
                for (String s : toMany) {
                    Collection list = (Collection) props.get(s);
                    for (Object da : list) {
                        o.addToManyTarget((ITimeable) da, s, false);
                    }
                    props.remove(s);
                }
                for (String s : toOne) {
                    ITimeable da = (ITimeable) props.get(s);
                    if (da != null) {
                        o.setToOneTarget(da, s, false);
                    }
                    props.remove(s);
                }
                o.assignID();
                o.writeProperties(props);
            }
        }
    }

    @Override
    public synchronized IDBProperties getDBProperties() {
        return dbProperties;
    }

    /**
     * This method is only for internal usage! Use IDBProperties.get() instead.
     */
    private synchronized DBPropertiesCayenneImpl _getDBProperties() throws DBException {
        SelectQuery selectQ;
        DBPropertiesCayenneImpl dbProp;
        List l = null;
        try {
            selectQ = new SelectQuery(getImpl(IDBProperties.class));
            selectQ.setFetchLimit(2);
            l = getContext().performQuery(selectQ);
        } catch (Exception e) {
            throw new DBException(e.getCause());
        }
        if (l.size() == 1) {
            dbProp = (DBPropertiesCayenneImpl) l.get(0);
        } else if (l.size() == 0) {
            if (!isInited()) {
                getDBLog().info(L.tr("DBProperties_are_null,_I_will_use_default."));
                dbProp = (DBPropertiesCayenneImpl) getContext().createAndRegisterNewObject(DBPropertiesCayenneImpl.class);
            } else {
                throw new DBInternalException(L.tr("Already_initialized_database."));
            }
        } else {
            throw new DBInternalException(L.tr("More_than_one_rows_in_DBProperties_are_not_allowed!"));
        }
        return dbProp;
    }

    synchronized DataMap getDataMap() {
        return getContext().getEntityResolver().lookupObjEntity(PersonCayenneImpl.class).getDataMap();
    }

    @Override
    public synchronized ITimeInterval cloneTI(ITimeInterval ti) {
        try {
            ITimeInterval newTI = ((TimeIntervalCayenneImpl) ti).initClone((TimeIntervalCayenneImpl) getImpl(ITimeInterval.class).newInstance());
            assert !newTI.isRegistered() : "timeinterval.registered should be false";
            return newTI;
        } catch (Exception ex) {
            throw new DBRuntimeException("Can't clone ITimeInterval: " + ti, ex);
        }
    }

    @Override
    public synchronized IWorker clear() {
        WorkerImpl w = new WorkerImpl() {

            @Override
            public String getName() {
                return "Cayenne-Clear";
            }

            @Override
            public void threadRun() throws Exception {
                setWorkingStatusString(CLEARING);
                setWorkingStatus(10);
                createSchema(true);
                setWorkingStatus(90);
                getContext().getEntityResolver().clearCache();
                getContext().getObjectStore().getDataRowCache().clear();
                finishedWork();
            }
        };
        return w;
    }

    void removeEntriesFromMap() {
        getDataMap().clearDbEntities();
        getDataMap().clearObjEntities();
        getDataMap().clearProcedures();
        getDataMap().clearQueries();
    }

    @Override
    public boolean isUserProperty(String name) {
        return name.endsWith(USER_POSTFIX);
    }

    @Override
    public <T extends ITimeable> ISchemaTableModel<T> getDBColumnTableModel(Class<T> tableName) {
        return new SchemaTableModelCayenneImpl<T>(tableName);
    }

    private static final String select = "select * from ";

    @Override
    public String getSelectAllStatement(Class aClass) {
        return select + getTableName(aClass);
    }

    @Override
    public synchronized void closeConnection() throws DBException {
        getTmpContext().commitChanges();
        successOfInitialization = false;
    }

    @Override
    public JDialog getEditSchemaDialog(JFrame frame) {
        return new EditSchemaDialog(frame).getComponent();
    }

    private Map<Class, Class> implList;

    @SuppressWarnings(value = "unchecked")
    @Override
    protected synchronized <T> Class<? extends T> getImpl(Class<T> timeableClass) {
        if (!timeableClass.isInterface()) {
            return timeableClass;
        }
        if (implList == null) {
            implList = new HashMap<Class, Class>(6);
            implList.put(IDBProperties.class, DBPropertiesCayenneImpl.class);
            implList.put(ITimeInterval.class, TimeIntervalCayenneImpl.class);
            implList.put(IRoom.class, RoomCayenneImpl.class);
            implList.put(ISubject.class, SubjectCayenneImpl.class);
            implList.put(IPerson.class, PersonCayenneImpl.class);
        }
        Class result = implList.get(timeableClass);
        assert result != null : timeableClass + " should not be null!";
        return result;
    }

    Map<Class, Class> implToInterfaze;

    @SuppressWarnings(value = "unchecked")
    @Override
    protected synchronized <T> Class<? extends T> getInterface(Class<T> interfazeOrImpl) {
        if (interfazeOrImpl.isInterface()) {
            return interfazeOrImpl;
        }
        if (implToInterfaze == null) {
            implToInterfaze = new HashMap<Class, Class>(6);
            implToInterfaze.put(TimeIntervalCayenneImpl.class, ITimeInterval.class);
            implToInterfaze.put(RoomCayenneImpl.class, IRoom.class);
            implToInterfaze.put(SubjectCayenneImpl.class, ISubject.class);
            implToInterfaze.put(PersonCayenneImpl.class, IPerson.class);
        }
        Class result = implToInterfaze.get(interfazeOrImpl);
        assert result != null : interfazeOrImpl + " should not be null!";
        return result;
    }

    @Override
    public <T> T rollback(T ta) {
        throw new UnsupportedOperationException("test this method");
    }
}

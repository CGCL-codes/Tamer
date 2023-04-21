package de.gstpl.data;

import de.gstpl.data.person.Group;
import de.gstpl.resource.language.L;
import de.gstpl.util.ConnectionHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
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
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.objectstyle.cayenne.DataObject;
import org.objectstyle.cayenne.DataObjectUtils;
import org.objectstyle.cayenne.DataRow;
import org.objectstyle.cayenne.access.DataContext;
import org.objectstyle.cayenne.access.DbGenerator;
import org.objectstyle.cayenne.access.MyEntitySorterUtil;
import org.objectstyle.cayenne.conf.Configuration;
import org.objectstyle.cayenne.conf.DefaultConfiguration;
import org.objectstyle.cayenne.conn.DriverDataSource;
import org.objectstyle.cayenne.dba.DbAdapter;
import org.objectstyle.cayenne.exp.Expression;
import org.objectstyle.cayenne.map.DataMap;
import org.objectstyle.cayenne.map.DbAttribute;
import org.objectstyle.cayenne.map.DbEntity;
import org.objectstyle.cayenne.map.EntityResolver;
import org.objectstyle.cayenne.map.ObjAttribute;
import org.objectstyle.cayenne.map.ObjEntity;
import org.objectstyle.cayenne.query.NamedQuery;
import org.objectstyle.cayenne.query.QueryChain;
import org.objectstyle.cayenne.query.SQLTemplate;
import org.objectstyle.cayenne.query.SelectQuery;
import org.objectstyle.cayenne.validation.ValidationResult;

/**
 * This class is a facade to the underlying database and relational-object mapper.
 *
 * @author Peter Karich
 */
public class GDB {

    private DataContext context;

    public static final String USER_POSTFIX = "_U";

    public static final String ID_POSTFIX = "_ID";

    public static final String APP_NAME = "GSTPL";

    /** This variables is a placeholder for the tablename of all persons. */
    public static final String P = "PERSON";

    /** This variables is a placeholder for the tablename of all rooms. */
    public static final String R = "ROOM";

    /** This variables is a placeholder for the tablename of all subject. */
    public static final String S = "SUBJECT";

    /** This variables is a placeholder for the tablename of all timeintervals. */
    public static final String TI = "TIMEINTERVAL";

    /** This variables is a placeholder for the tablename of all relations
     *  between persons and subjects. */
    public static final String PS = "PERSON_SUBJECT";

    /** This variables is a placeholder for the tablename of the db properties. */
    public static final String DBP = "DB_PROPERTIES";

    /** This variables is a placeholder for the tablename of all groups. */
    public static final String GR = "GROUPY";

    /** This variables is a placeholder for the tablename of the relationship table: groupName_id and nameOfColumn */
    public static final String VT = "VISIBLE_TABLE";

    private static String[] getAllDbEntitiesAsString() {
        String str[] = { TI, R, PS, S, P, DBP, GR, VT };
        return str;
    }

    private static final String OBJ_PROPERTY_NAME = "name";

    private List temporaryObjects;

    private List tmpTIs;

    private static GDB singleton = new GDB();

    public static GDB get() {
        return singleton;
    }

    private GDB() {
        temporaryObjects = new ArrayList(400);
        tmpTIs = new ArrayList(400);
    }

    private static final Logger log = Logger.getLogger("de.gstpl.data.GDB");

    /**
     * This methiod returns the database specific logger.
     */
    private Logger getDBLog() {
        return log;
    }

    /**
     * This method inits configuration, connection, logging, etc
     */
    void initConfig() {
        Configuration.getSharedConfiguration().shutdown();
        try {
            Configuration.configureCommonLogging(AppProperties.get().getLogPropertiesLocation().toURL());
        } catch (MalformedURLException exc) {
            getDBLog().log(Level.WARNING, L.tr("Can't_change_logging_configuration!"), exc);
        }
        DefaultConfiguration config = new DefaultConfiguration();
        ConnectionHelper helper = AppProperties.get().getConnection();
        config.setDataSourceFactory(new MyDataSourceFactory(helper));
        config.installConfigurationShutdownHook();
        Configuration.initializeSharedConfiguration(config);
        getDBLog().info(L.tr("._Connection:_") + helper);
    }

    /**
     * This method the programmer has to call to eventually connect to the
     * database and do some other stuff on start up.
     */
    public synchronized void initDB() throws DBException {
        try {
            initConfig();
            context = DataContext.createDataContext();
            createSchema(false);
            String v = DBProperties.get().getVersion();
            getDBLog().info(L.tr("Database_version:_") + v);
            if (!"0.0.6".equals(v)) getDBLog().warning(L.tr("It_is_not_save_to_use_this_database_(incompatible_version)!_Nevertheless_I_will_try_to_use_..._"));
        } catch (Exception ex) {
            if (ex.getCause() != null && ex.getCause().getCause() instanceof SQLException) throw new DBException(L.tr("Can't_init_database!_Server_down?"), ex); else throw new DBException(L.tr("Can't_init_database!"), ex);
        }
        getDBLog().info(L.tr("Map_file_location:_") + getDataMap().getLocation());
        success = true;
    }

    boolean success = false;

    public boolean isInited() {
        return success;
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
            ConnectionHelper cHelper = AppProperties.get().getConnection();
            String url = cHelper.getUrl(), adapterStr;
            if (cHelper.getDriverName() == ConnectionHelper.DERBY_CLIENT) {
                adapterStr = "org.objectstyle.cayenne.dba.derby.DerbyAdapter";
                if (url.indexOf("create=true") == -1) {
                    if (!url.endsWith(";")) url += ";";
                    url += "create=true;";
                }
            } else throw new DBRuntimeException(L.tr("Can't_create_tables_which_are_necessary_for_gstpl!!"));
            Driver driver = (Driver) Class.forName(cHelper.getDriverName()).newInstance();
            DataSource dataSource = new DriverDataSource(driver, url, cHelper.getUserName(), cHelper.getPassword());
            DbAdapter adapter = (DbAdapter) Class.forName(adapterStr).newInstance();
            DbGenerator generator = new DbGenerator(adapter, map);
            if (overwrite) {
                generator.setShouldDropTables(true);
                generator.setShouldDropPKSupport(true);
                getContext().unregisterObjects(Collections.singletonList(DBProperties.get()));
            }
            String ret;
            if (overwrite || (ret = getAvailableTables(dataSource)) == EMPTY) {
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
                if (ret == ALL) getDBLog().info(L.tr("Schema_exists!_Nothing_to_do!")); else getDBLog().severe(L.tr("Schema_incomplete!Missing_tables:") + ret);
            }
        } catch (Exception exc) {
            throw new DBException(exc);
        }
        if (overwrite) {
            getContext().registerNewObject(DBProperties.get());
            DBProperties.get().saveDataMap();
            DBProperties.get().updateDataMap();
        }
        if (failures != null) return failures.toString(); else return L.tr("No_failures_while_creating_database_schema!");
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

    public static final String ALL = "All", EMPTY = "Emtpy";

    /**
     * This method returns true if gstpl is able to create the database schema.
     */
    private synchronized String getAvailableTables(DataSource dataSource) throws DBException {
        StringBuffer retTables = new StringBuffer();
        Connection c;
        try {
            c = dataSource.getConnection();
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
        try {
            String str[] = getAllDbEntitiesAsString();
            SQLTemplate rawSelect;
            Statement st;
            for (int i = 0; i < str.length; i++) {
                st = c.createStatement();
                st.executeQuery("select * from " + str[i]);
                st.close();
                retTables.append(str[i]);
                if (i < retTables.length() - 1) retTables.append("; ");
            }
            c.close();
            return ALL;
        } catch (SQLException ex) {
            getDBLog().log(Level.INFO, L.tr("Following_exception_can_be_ignored:"), ex);
        }
        if (retTables.length() == 0) return EMPTY;
        return retTables.toString();
    }

    /**
     * get DataSource via dataSource = new DriverDataSource(driver, url,
     *               cHelper.getUserName(), cHelper.getPassword());
     *
     */
    private void executeRawSQL(DataSource dataSource, String str) throws DBException {
        try {
            Statement st = dataSource.getConnection().createStatement();
            st.executeQuery(str);
        } catch (SQLException ex) {
            throw new DBException(ex.getCause());
        }
    }

    /**
     * This method intis the current DataMap with the 'old' Map in DBProperties.
     */
    public synchronized void undoMapChanges() {
        setLocalMap(DBProperties.get().getDataMap());
    }

    /**
     * This method returns the cayenne specific context. It is the central point
     * to do some queries etc. in cayenne.
     */
    private DataContext getContext() {
        return context;
    }

    public void internalCommit() throws DBException {
        try {
            synchronized (this) {
                getContext().commitChanges();
            }
            getDBLog().info(L.tr("Saved_changes!"));
        } catch (Exception ex) {
            throw new DBException(L.tr("Cannot_persist_objects_to_database!"), ex);
        }
    }

    public void remove(List coll) throws DBException {
        synchronized (this) {
            TimeInterval tmp;
            Object o;
            for (int i = 0; i < coll.size(); i++) {
                if ((o = coll.get(i)) instanceof TimeInterval) {
                    (tmp = (TimeInterval) o).setToOneTarget("room", null, false);
                    tmp.setToOneTarget("subject", null, false);
                }
            }
            getContext().deleteObjects(coll);
            internalCommit();
        }
    }

    public void internalUndo() {
        synchronized (this) {
            getContext().rollbackChanges();
            getDBLog().info(L.tr("Undo_successful!"));
        }
    }

    /**
     * This method tries to persist all changes on all objects!
     */
    public void saveChanges() throws DBException {
        try {
            synchronized (this) {
                getContext().deleteObjects(tmpTIs);
                tmpTIs.clear();
                getContext().deleteObjects(temporaryObjects);
                temporaryObjects.clear();
                getContext().commitChanges();
            }
            getDBLog().info(L.tr("Saved_changes!"));
        } catch (Exception ex) {
            throw new DBException(L.tr("Cannot_persist_objects_to_database!"), ex);
        }
    }

    /**
     * This method rolls back all changes that was made after the previous commit.
     */
    public void undoChanges() {
        internalUndo();
    }

    /** @return true if this database has changes */
    public boolean hasChanges() {
        synchronized (this) {
            return getContext().hasChanges();
        }
    }

    /**
     * This method print the content of specified table to DB logger.
     * @param tableName accepted values: GDB.S, P, R, PS and  GDB.TI.
     */
    public void dump(String tableName) {
        NamedQuery rawSelect = new NamedQuery("SelectTable", Collections.singletonMap("table", tableName));
        getDBLog().info(L.tr("Dump_table_") + tableName);
        List l = null;
        try {
            synchronized (this) {
                l = getContext().performQuery(rawSelect);
            }
            for (Iterator iter = l.iterator(); iter.hasNext(); ) getDBLog().info(iter.next().toString());
        } catch (Exception ex) {
            getDBLog().log(Level.SEVERE, L.tr("Can't_query_database_to_dump_table:") + tableName, ex);
        }
    }

    /**
     * This method creates a new timeinterval with specified room and subject.
     *
     * @param from specifies the start time relative to
     * @param duration specifies the duration time (in minimal timeintervals).
     * @param subject discribes what will happen in this timeinterval.
     * @param room discribes where subject will happen.
     * @param setReverseRelation is true if subject and room (also) should
     * save (connect to) this timeinterval.
     * @see DBProperties#getMinTimeInterval()
     */
    public TimeInterval createTI(int from, int duration, Subject subject, Room room, boolean setReverseRelation) throws DBException {
        TimeInterval ti = null;
        try {
            synchronized (this) {
                ti = (TimeInterval) getContext().createAndRegisterNewObject(TimeInterval.class);
                ti.setStartTime(from);
                ti.setDuration(duration);
                addConnection(ti, subject, setReverseRelation);
                addConnection(ti, room, setReverseRelation);
            }
        } catch (Exception ex) {
            throw new DBException(L.tr("Can't_create_TimeInterval:_") + ti, ex);
        }
        return ti;
    }

    /**
     * This method creates a timetable of specified type.
     * @param name specifies the name of the newly created timetable.
     */
    public Timetable create(String type_table, String name) throws DBException {
        Map m = new HashMap();
        m.put(OBJ_PROPERTY_NAME, name);
        return create(type_table, m);
    }

    /**
     * This method creates a timetable of specified type.
     * @param name specifies the name of the newly created timetable.
     */
    public Timetable create(String type_table, Map properties) throws DBException {
        Timetable t = null;
        try {
            synchronized (this) {
                t = (Timetable) getContext().createAndRegisterNewObject(getClass(type_table));
                if (properties != null) t.writeProperties(properties);
            }
        } catch (Exception ex) {
            throw new DBException(L.tr("Can't_create_timetable_for_type:") + type_table, ex);
        }
        return t;
    }

    public Person createPerson(String name) throws DBException {
        return (Person) create(P, name);
    }

    public Room createRoom(String name) throws DBException {
        return (Room) create(R, name);
    }

    public Subject createSubject(String name) throws DBException {
        return (Subject) create(S, name);
    }

    public Timetable createDefault(String table) throws DBException {
        return create(table, getDefaultValues(table));
    }

    private static Object[] defValues = { L.tr("default"), new Integer(0), Boolean.FALSE, new Short(Short.MIN_VALUE) };

    private Map getDefaultValues(String table) {
        Map m = new HashMap();
        ObjEntity entity = getObjEntity(table);
        List all = new ArrayList(entity.getAttributes());
        Iterator aIter = all.iterator();
        ObjAttribute oA;
        DbAttribute dbA;
        Class clazz;
        while (aIter.hasNext()) {
            oA = (ObjAttribute) aIter.next();
            dbA = oA.getDbAttribute();
            if (dbA == null) continue;
            if (dbA.isMandatory() && !dbA.isPrimaryKey()) {
                try {
                    clazz = Class.forName(oA.getType());
                    for (int i = 0; i < defValues.length; i++) {
                        if (clazz.isAssignableFrom(defValues[i].getClass())) {
                            m.put(oA.getName(), defValues[i]);
                            break;
                        }
                    }
                } catch (Exception exc) {
                    getDBLog().log(Level.WARNING, L.tr("Can't_instantiate:") + GTypesMapping.getJavaBySqlType(dbA.getType()), exc);
                }
            }
        }
        return m;
    }

    public int getFetchLimit() {
        return AppProperties.get().getFetchLimit();
    }

    public void setFetchLimit(int limit) {
        AppProperties.get().setFetchLimit(limit);
    }

    /**
     * This method queries the database for all persons, subjects or rooms with
     * the specified name.
     * @return a list containing all queried timetables.
     */
    List getAll(Class clazz, String lastName) throws DBException {
        SelectQuery select;
        List l = null;
        synchronized (this) {
            try {
                select = new SelectQuery(clazz);
                if ((Person.class.equals(clazz) || Room.class.equals(clazz) || Subject.class.equals(clazz)) && lastName != null) {
                    Map params = Collections.singletonMap("aname", lastName);
                    select.setQualifier(Expression.fromString("name = $aname").expWithParameters(params, false));
                }
                select.setFetchLimit(getFetchLimit());
                l = getContext().performQuery(select);
            } catch (Exception e) {
                throw new DBException(L.tr("Can't_query_database_to_get_all_entries_for:") + clazz.toString(), e);
            }
        }
        return l;
    }

    /**
     * This method tests if the database already contains the specified timeable.
     */
    public boolean hasEqual(Timeable ta) throws DBException {
        synchronized (this) {
            List l = getAll(ta.getClass(), ((Timetable) ta).getName());
            return l.contains(ta);
        }
    }

    /**
     * This method removes a subject, room, person or a timeInterval from database.
     * @see #disconnectTIFrom()
     */
    public void remove(Timeable timeable) throws DBException {
        if (timeable == null) throw new DBRuntimeException(L.tr("Timeable_can't_be_null!"));
        synchronized (this) {
            try {
                getContext().deleteObject((TimeableImpl) timeable);
            } catch (Exception ex) {
                throw new DBException(L.tr("Can't_remove_timeable:_") + timeable, ex);
            }
        }
    }

    /**
     * This method returns an Iterator on all timeintervals of specified timeable.
     * If timeable does not directly contain timeintervals (e.g. Person) this method
     * will recursive get them.
     */
    public synchronized Iterator timeIntervalIteratorOf(Timeable timeable) throws DBSecException {
        if (timeable instanceof Person) {
            TreeSet t = new TreeSet();
            Iterator iter;
            try {
                iter = ((Timetable) timeable).getTimeableIterator();
            } catch (Exception ex) {
                throw new DBSecException(L.tr("You_have_no_access_on_timeable:_") + timeable.toString());
            }
            while (iter.hasNext()) {
                Iterator subjIter = ((Subject) iter.next()).getTimeableIterator();
                while (subjIter.hasNext()) t.add((TimeInterval) subjIter.next());
            }
            return t.iterator();
        } else if (timeable instanceof Room) {
            TreeSet t = new TreeSet();
            Iterator tIter = ((Timetable) timeable).getTimeableIterator();
            while (tIter.hasNext()) t.add((TimeInterval) tIter.next());
            return t.iterator();
        } else if (timeable instanceof Subject) {
            TreeSet t = new TreeSet();
            Iterator tIter = ((Timetable) timeable).getTimeableIterator();
            while (tIter.hasNext()) t.add((TimeInterval) tIter.next());
            return t.iterator();
        } else if (timeable instanceof TimeInterval) {
            List l = new ArrayList();
            l.add(timeable);
            return l.iterator();
        } else throw new DBRuntimeException(L.tr("Don't_know_other_types_than:_Person,_Subject,_Room_and_TimeInterval!"));
    }

    /**
     * This method adds a connection. It does sth. like 'from.connect(toT)'
     */
    public synchronized void addConnection(Timeable from, Timeable toT, boolean reverse) {
        if (toT == null || from == null) {
            return;
        }
        if (from instanceof Person && toT instanceof Subject) {
            ((Person) from).addConnection((Subject) toT, reverse);
        } else if (from instanceof TimeInterval && toT instanceof Subject) {
            Subject s = (Subject) toT;
            TimeInterval ti = (TimeInterval) from;
            ti.setSubject(s, reverse);
        } else if (from instanceof Subject && toT instanceof TimeInterval) {
            ((Subject) from).addConnection((TimeInterval) toT, reverse);
        } else if (from instanceof TimeInterval && toT instanceof Room) {
            Room r = (Room) toT;
            TimeInterval ti = (TimeInterval) from;
            ti.setRoom(r, reverse);
        } else if (from instanceof Room && toT instanceof TimeInterval) {
            ((Room) from).addConnection((TimeInterval) toT, reverse);
        } else {
            throw new DBRuntimeException(L.tr("Invalid_connect-combination!"), null);
        }
    }

    /**
     * This method removes a connection. It does sth. like 'from.removeConnection(toT)'
     * Since now person.removeConnection(subject) is implemented.
     */
    public boolean removeConnection(Timetable from, Timetable toT, boolean reverse) {
        boolean ret = false;
        if (from instanceof Person && toT instanceof Subject) {
            synchronized (this) {
                ret = from.removeConnection(toT, reverse);
            }
        } else {
            throw new DBRuntimeException(L.tr("Invalid_disconnect-combination!"), null);
        }
        return ret;
    }

    /**
     * This method looks up the specified TimeInterval.
     *
     * @param timeStr use "0800" for 08:00
     * @param duration use "80" for 80 minutes
     * @param day 0 until daysNoOfAWeek-1
     */
    public TimeInterval getTI(Subject subject, Room room, int day, String timeStr, String durationStr) {
        int start = TimeFormat.getStartTime(day, timeStr);
        int duration = TimeFormat.getDuration(durationStr);
        return getTI(start, duration, subject, room);
    }

    /**
     * This method creates (if necessary) a TimeInterval with specified room and
     * subject. It will connect room with created TI and subject with created TI,
     * if this is not already done.
     *
     * @param connect is true if you want to create connections "subject to the created timeinterval"
     * and "room to the created timeinterval". is false if not.<br>
     * if TI exists no explicit (dis)connection will happen.
     *
     */
    public TimeInterval createOrGetTI(Subject subject, Room room, int day, String timeStr, String durationStr, boolean setReverse) throws DBException {
        int start = TimeFormat.getStartTime(day, timeStr);
        int duration = TimeFormat.getDuration(durationStr);
        TimeInterval ti = getTI(start, duration, subject, room);
        if (ti == null) return createTI(start, duration, subject, room, setReverse);
        return ti;
    }

    String[] keys = { "start", "duration", "roomId", "subjectId" };

    Object[] values = new Object[4];

    /**
     * This method query the database for the specified timeinterval.
     * @return null if not found.
     */
    private TimeInterval getTI(int start, int duration, Subject subject, Room room) {
        values[0] = new Integer(start);
        values[1] = new Integer(duration);
        values[2] = (room != null) ? room : null;
        values[3] = (subject != null) ? subject : null;
        List l;
        Map params = new HashMap();
        params.put(keys[0], values[0]);
        params.put(keys[1], values[1]);
        params.put(keys[2], values[2]);
        params.put(keys[3], values[3]);
        SelectQuery q = new SelectQuery(TimeInterval.class);
        q.setQualifier(Expression.fromString("startTime=$start and durationTime=$duration and room=$roomId and subject=$subjectId").expWithParameters(params, false));
        synchronized (this) {
            l = getContext().performQuery(q);
        }
        if (l.size() < 1) return null; else if (l.size() == 1) return (TimeInterval) l.get(0);
        throw new DBInternalException(L.tr("Query_returns_not_only_one_TimeInterval") + L.tr("_the_first_was:") + l.get(0));
    }

    /**
     * This method replaces room and subject from all timeintervals in
     * specified tmpList.
     *
     * @param tmpList filled with TimeInterval's
     * @param reverse is true if you want that room and subject points to timeinterval.
     */
    public void setRoomAndSubjectTo(Collection tmpList, Subject s, Room r, boolean reverse) {
        Iterator iter = tmpList.iterator();
        TimeInterval ti;
        while (iter.hasNext()) {
            ti = (TimeInterval) iter.next();
            addConnection(ti, r, reverse);
            addConnection(ti, s, reverse);
        }
    }

    /**
     * This method registers the specified temporary object to make
     * it persistent.
     */
    public void registerTmp(TimeableImpl ta) {
        removeFromTmp(ta);
    }

    /**
     * This method queries the database with specified sqlString. The return
     * values should be a list with Map's where the values are
     * properties of your specified class.
     *
     * @return a list of java.util.Map's
     */
    public List execute(String tableName, String sqlString) throws Exception {
        SQLTemplate rawSelect = new SQLTemplate(getClass(tableName), sqlString);
        rawSelect.setFetchingDataRows(true);
        rawSelect.setFetchLimit(getFetchLimit());
        List l;
        synchronized (this) {
            l = getContext().performQuery(rawSelect);
        }
        return l;
    }

    private static final String availableSchemas[] = { "admin", "teacher", "students" };

    /**
     * This method gets the default schema of specified user.
     */
    public String getSchema(String userName) {
        int i = 0;
        for (; i < availableSchemas.length - 1; i++) {
            setSchema(availableSchemas[i]);
            SQLTemplate rawSelect = new SQLTemplate(P, "select * from " + P);
            getDBLog().info(L.tr("Try_to_get_some_data_in_schema_") + availableSchemas[i]);
            try {
                synchronized (this) {
                    rawSelect.setFetchLimit(1);
                    getContext().performQuery(rawSelect);
                }
            } catch (Exception ex) {
                continue;
            }
            break;
        }
        return availableSchemas[i];
    }

    private void setSchema(String schemaName) {
        NamedQuery rawSelect = new NamedQuery("SetSchema", Collections.singletonMap("schemaName", schemaName));
        synchronized (this) {
            getContext().performNonSelectingQuery(rawSelect);
        }
    }

    /**
     * This method returns the name of the table columns as String and its object types
     * as Class of the specified table.
     *
     * @return Map(ColumnName, Class)
     */
    public Map getMixedHeader(String tableName) {
        Map colDefinition;
        synchronized (this) {
            DbEntity entity = getDbEntity(tableName);
            Collection coll = new ArrayList(entity.getAttributes());
            coll.addAll(entity.getGeneratedAttributes());
            Iterator iter = coll.iterator();
            colDefinition = new HashMap();
            DbAttribute a;
            while (iter.hasNext()) {
                a = (DbAttribute) iter.next();
                try {
                    colDefinition.put(a.getName(), Class.forName(GTypesMapping.getJavaBySqlType(a.getType())));
                } catch (Exception exc) {
                    getDBLog().log(Level.WARNING, L.tr("Can't_put:") + a.getName() + L.tr("_into_the_result_map!"), exc);
                }
            }
        }
        return colDefinition;
    }

    /**
     * This method returns the column names and its other attributes.
     * It is not recommended to change the column properties! Use
     * getColumn(String) instead.
     * @return unmodifiable Map of (String, DBColumn)
     */
    public Map getHeader(String tableName) {
        Map colDefinition;
        synchronized (this) {
            DbEntity entity = getDbEntity(tableName);
            Collection coll = new ArrayList(entity.getAttributes());
            coll.addAll(entity.getGeneratedAttributes());
            Iterator iter = coll.iterator();
            colDefinition = new HashMap();
            DbAttribute a;
            while (iter.hasNext()) {
                a = (DbAttribute) iter.next();
                colDefinition.put(a.getName(), new DBColumn(a));
            }
        }
        return Collections.unmodifiableMap(colDefinition);
    }

    /**
     * This method adds a new column to the specified table.
     * By convention columnName should be something like "COLUMN_U" - "_U" stands
     * for user-defined, this means this column is not necessary for gstpl.
     *
     * @param tableName specifies the table name where you want to add the column.
     * @param columnName specifies the name of the column.
     * @param type specifies the type of the specified property. See
     * {@link java.sql.Types} e.g. {@link java.sql.Types#BLOB}
     *
     * @return null if a column with this name already exist!
     */
    public DBColumn addColumn(String tableName, String columnName, int type) throws DBException {
        if (columnName == null || !columnName.endsWith(USER_POSTFIX)) return null;
        DbEntity entity = getDbEntity(tableName);
        DbAttribute dbA = new DbAttribute(columnName, type, entity);
        try {
            entity.addAttribute(dbA);
        } catch (IllegalArgumentException exc) {
            return null;
        }
        ObjAttribute oAttr = null;
        if (!PS.equals(tableName)) {
            ObjEntity oEnt = getObjEntity(tableName);
            oAttr = new ObjAttribute(columnName, GTypesMapping.getJavaBySqlType(type), oEnt);
            oAttr.setDbAttribute(dbA);
            oEnt.addAttribute(oAttr);
        }
        internalCommit();
        return new DBColumn(dbA, oAttr);
    }

    /**
     * This method removes the specified column from specified table.
     * You can only remove user-defined columns.<br>
     * Warning: The underlying database (derby) does not support removing columns in
     * version 10.2
     * so this is only useful for creating the schema
     */
    public boolean removeColumn(String tableName, String columnName) throws DBException {
        if (columnName == null || !columnName.endsWith(USER_POSTFIX)) return false;
        DbEntity entity = getDbEntity(tableName);
        ObjEntity oEnt = null;
        boolean hasObjEnt = !PS.equals(tableName);
        if (hasObjEnt) {
            oEnt = getObjEntity(tableName);
        }
        if (entity.getAttribute(columnName) == null || (hasObjEnt && oEnt.getAttribute(columnName) == null)) return false;
        entity.removeAttribute(columnName);
        if (hasObjEnt) {
            oEnt.removeAttribute(columnName);
        }
        internalCommit();
        return true;
    }

    /**
     * This method updates the specified Timeable from the specified
     * Map(ColumnAsString, newValue).
     * Notice: You can't update the ObjectId!
     */
    public void update(Timeable ta, Map m) {
        Iterator iter = m.entrySet().iterator();
        Map.Entry entry;
        String key;
        while (iter.hasNext()) {
            entry = (Map.Entry) iter.next();
            key = toObjPropertyName(ta, entry.getKey().toString());
            if (key != null) ta.writeProperty(key, entry.getValue());
        }
    }

    /**
     * This method returns the name of the specified columnName for specified ta.
     * @return the property. If columnName is an ID it returns null.
     */
    private String toObjPropertyName(Timeable ta, String columnName) {
        TimeableImpl taNew = (TimeableImpl) ta;
        DbEntity entity = getContext().getEntityResolver().lookupDbEntity(taNew);
        DbAttribute a = (DbAttribute) entity.getAttribute(columnName);
        if (a == null) return null;
        ObjAttribute oA = taNew.getObjEntity().getAttributeForDbAttribute(a);
        if (oA == null) return null;
        return oA.getName();
    }

    private DbEntity getDbEntity(String tableName) {
        return getDataMap().getDbEntity(tableName);
    }

    private ObjEntity getObjEntity(String tableName) {
        return getDataMap().getObjEntityForJavaClass(getClass(tableName).getName());
    }

    private Class getClass(String tableName) {
        Class clazz;
        if (P.equals(tableName)) clazz = Person.class; else if (S.equals(tableName)) clazz = Subject.class; else if (R.equals(tableName)) clazz = Room.class; else if (TI.equals(tableName)) clazz = TimeInterval.class; else if (GR.equals(tableName)) clazz = Group.class; else if (DBP.equals(tableName)) clazz = DBProperties.class; else throw new UnsupportedOperationException(L.tr("Can't_get_class_for_table:_") + tableName);
        return clazz;
    }

    /**
     * This method adds the classes to the specified List
     * of the specified columnNames for specified table.
     */
    public void addClasses(List result, String tableName, Collection columnNames) {
        DbEntity dbEnt = getDbEntity(tableName);
        ObjEntity oEnt = getObjEntity(tableName);
        Iterator colNamesIter = columnNames.iterator();
        DbAttribute a;
        String str = null;
        try {
            while (colNamesIter.hasNext()) {
                a = (DbAttribute) dbEnt.getAttribute(str = colNamesIter.next().toString());
                if (a == null) {
                    getDBLog().warning(L.tr("Can't_get_db_property_from_possible_unmapped:_") + str);
                } else if (a.isPrimaryKey() || a.isForeignKey()) {
                    result.add(Class.forName(GTypesMapping.getJavaBySqlType(a.getType())));
                } else {
                    ObjAttribute oA = oEnt.getAttributeForDbAttribute(a);
                    if (oA == null) throw new DBRuntimeException(L.tr("Can't_get_property_for_attribute:_") + a.getName());
                    result.add(Class.forName(oA.getType()));
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new DBRuntimeException(L.tr("Can't_get_java_class_from_string!_Attribute:") + str + L.tr(",_table_name:") + tableName);
        }
    }

    public void runScript(String str) throws Exception {
        runScript(new StringReader(str));
    }

    public void runScript(InputStream is) throws Exception {
        runScript(new InputStreamReader(is));
    }

    /**
     * Taken from cayenne petstore example
     */
    public void runScript(Reader rawReader) throws Exception {
        BufferedReader reader = new BufferedReader(rawReader);
        DataMap map = getDataMap();
        QueryChain chain = new QueryChain();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) continue;
            if (line.endsWith(";")) line = line.substring(0, line.length() - 1);
            chain.addQuery(new SQLTemplate(map, line));
        }
        try {
            reader.close();
        } catch (IOException e) {
            getDBLog().log(Level.WARNING, L.tr("IOException while closing reader!"), e);
        }
        if (!chain.isEmpty()) getContext().performNonSelectingQuery(chain);
    }

    /**
     * This method returns a Map(String, Object) for specified timeable
     * and it reduces the keys of the map to specified column names (String's).
     * @param if names is null or if it is empty this method returns all available column names.
     */
    public Map objectToRow(Timeable ta, Collection names) {
        TimeableImpl tai = (TimeableImpl) ta;
        if (names == null || names.size() == 0) throw new DBRuntimeException(L.tr("You_sould_at_least_specify_one_column_in_names!_E.g._via:_names_=_getHeader(ta).values();"));
        Map m = new HashMap();
        Iterator namesIter = names.iterator();
        String s, prop;
        while (namesIter.hasNext()) {
            s = namesIter.next().toString();
            prop = toObjPropertyName(ta, s);
            if (prop != null) m.put(s, tai.readProperty(prop)); else m.put(s, new Integer(DataObjectUtils.intPKForObject(tai)));
        }
        return new DataRow(m);
    }

    /**
     * This method returns the object of specified row in table with specified
     * tableName.
     */
    public synchronized Object rowToObject(String tableName, Map row) throws DBSecException {
        DataRow dr = (DataRow) row;
        Object o = null;
        try {
            Class clazz = getClass(tableName);
            o = getContext().objectFromDataRow(clazz, dr, true);
        } catch (Exception ex) {
            throw new DBSecException(L.tr("You_have_not_enough_rights_to_get_objects_from_pure_rows!"), ex);
        }
        return o;
    }

    /**
     * This method creates a temporary timeinterval.
     * @see #removeTmp()
     */
    public TimeInterval createTmpTI(int start, int duration, Subject s, Room r, boolean setReverse) throws DBException {
        TimeInterval ti = createTI(start, duration, s, r, setReverse);
        addToTmp(ti);
        return ti;
    }

    /**
     * This method creates a temporary timeinterval.
     * @see #removeTmp()
     */
    public TimeInterval createTmpTI(int start, int duration) throws DBException {
        return createTmpTI(start, duration, null, null, false);
    }

    /**
     * This method creates a temporary timeinterval.
     * @see #removeTmp()
     * @see #createTmpTI(int start, int duration)
     */
    public TimeInterval createTmpTI(Subject s, Room r, int day, String timeStr, String durationStr, boolean reverse) throws DBException {
        return createTmpTI(TimeFormat.getStartTime(day, timeStr), TimeFormat.getDuration(durationStr), s, r, reverse);
    }

    /**
     * This method creates a temporary room.
     * @see #removeTmp()
     */
    Room createTmpRoom(String name) throws DBException {
        Room r = createRoom(name);
        addToTmp(r);
        return r;
    }

    Person createTmpPerson(String name) throws DBException {
        Person p = createPerson(name);
        addToTmp(p);
        return p;
    }

    Subject createTmpSubject(String name) throws DBException {
        Subject s = createSubject(name);
        addToTmp(s);
        return s;
    }

    private void addToTmp(DataObject ta) {
        if (ta instanceof TimeInterval) tmpTIs.add(ta); else temporaryObjects.add(ta);
    }

    private boolean removeFromTmp(DataObject ta) {
        if (ta instanceof TimeInterval) return tmpTIs.remove(ta); else return temporaryObjects.remove(ta);
    }

    /**
     * This method returns the properties of specified timeable.
     */
    public Map getProperties(TimeableImpl ta) {
        return ta.getObjEntity().getAttributeMap();
    }

    /**
     * This method is only for internal usage! Use DBProperties.get() instead.
     */
    synchronized DBProperties getDBProperties() throws DBException {
        SelectQuery select;
        DBProperties dbProp;
        List l = null;
        try {
            select = new SelectQuery(DBProperties.class);
            select.setFetchLimit(2);
            l = getContext().performQuery(select);
        } catch (Exception e) {
            throw new DBException(e.getCause());
        }
        if (l.size() == 1) {
            dbProp = (DBProperties) l.get(0);
        } else if (l.size() == 0) {
            if (!isInited()) {
                getDBLog().info(L.tr("DBProperties_are_null,_I_will_use_default."));
                dbProp = (DBProperties) getContext().createAndRegisterNewObject(DBProperties.class);
            } else throw new DBInternalException(L.tr("Already_initialized_database."));
        } else throw new DBInternalException(L.tr("More_than_one_rows_in_DBProperties_are_not_allowed!"));
        return dbProp;
    }

    synchronized DataMap getDataMap() {
        return getContext().getEntityResolver().lookupObjEntity(Person.class).getDataMap();
    }

    /**
     * This method removes all entries from this database!
     * Be sure that you know what you do do!
     */
    public synchronized void clear() throws DBException {
        createSchema(true);
    }

    synchronized void removeEntriesFromMap() {
        getDataMap().clearDbEntities();
        getDataMap().clearObjEntities();
        getDataMap().clearProcedures();
        getDataMap().clearQueries();
    }
}

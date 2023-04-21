package org.objectstyle.cayenne.access;

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
import java.util.ListIterator;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.objectstyle.cayenne.CayenneRuntimeException;
import org.objectstyle.cayenne.conn.DataSourceInfo;
import org.objectstyle.cayenne.conn.DriverDataSource;
import org.objectstyle.cayenne.dba.DbAdapter;
import org.objectstyle.cayenne.dba.PkGenerator;
import org.objectstyle.cayenne.dba.TypesMapping;
import org.objectstyle.cayenne.map.DataMap;
import org.objectstyle.cayenne.map.DbAttribute;
import org.objectstyle.cayenne.map.DbEntity;
import org.objectstyle.cayenne.map.DbJoin;
import org.objectstyle.cayenne.map.DbRelationship;
import org.objectstyle.cayenne.map.DerivedDbEntity;
import org.objectstyle.cayenne.validation.SimpleValidationFailure;
import org.objectstyle.cayenne.validation.ValidationResult;

/**
 * Utility class that generates database schema based on Cayenne mapping. It is a logical
 * counterpart of DbLoader class.
 * 
 * @author Andrus Adamchik
 */
public class DbGenerator {

    private Logger logObj = Logger.getLogger(DbGenerator.class);

    protected DbAdapter adapter;

    protected DataMap map;

    protected DataDomain domain;

    protected Map dropTables;

    protected Map createTables;

    protected Map createFK;

    protected List createPK;

    protected List dropPK;

    /**
     * Contains all DbEntities ordered considering their interdependencies.
     * DerivedDbEntities are filtered out of this list.
     */
    protected List dbEntitiesInInsertOrder;

    protected List dbEntitiesRequiringAutoPK;

    protected boolean shouldDropTables;

    protected boolean shouldCreateTables;

    protected boolean shouldDropPKSupport;

    protected boolean shouldCreatePKSupport;

    protected boolean shouldCreateFKConstraints;

    protected ValidationResult failures;

    /**
     * Creates and initializes new DbGenerator.
     */
    public DbGenerator(DbAdapter adapter, DataMap map) {
        this(adapter, map, Collections.EMPTY_LIST);
    }

    /**
     * Creates and initializes new DbGenerator instance.
     * 
     * @param adapter DbAdapter corresponding to the database
     * @param map DataMap whose entities will be used in schema generation
     * @param excludedEntities entities that should be ignored during schema generation
     */
    public DbGenerator(DbAdapter adapter, DataMap map, Collection excludedEntities) {
        this(adapter, map, excludedEntities, null);
    }

    /**
     * Creates and initializes new DbGenerator instance.
     * 
     * @param adapter DbAdapter corresponding to the database
     * @param map DataMap whose entities will be used in schema generation
     * @param excludedEntities entities that should be ignored during schema generation
     * @param domain optional DataDomain used to detect cross-database relationships.
     * @since 1.2
     */
    public DbGenerator(DbAdapter adapter, DataMap map, Collection excludedEntities, DataDomain domain) {
        if (adapter == null) {
            throw new IllegalArgumentException("Adapter must not be null.");
        }
        if (map == null) {
            throw new IllegalArgumentException("DataMap must not be null.");
        }
        this.domain = domain;
        this.map = map;
        this.adapter = adapter;
        prepareDbEntities(excludedEntities);
        resetToDefaults();
        buildStatements();
    }

    protected void resetToDefaults() {
        this.shouldDropTables = false;
        this.shouldDropPKSupport = false;
        this.shouldCreatePKSupport = true;
        this.shouldCreateTables = true;
        this.shouldCreateFKConstraints = true;
    }

    /**
     * Creates and stores internally a set of statements for database schema creation,
     * ignoring configured schema creation preferences. Statements are NOT executed in
     * this method.
     */
    protected void buildStatements() {
        dropTables = new HashMap();
        createTables = new HashMap();
        createFK = new HashMap();
        DbAdapter adapter = getAdapter();
        Iterator it = dbEntitiesInInsertOrder.iterator();
        boolean supportsFK = adapter.supportsFkConstraints();
        while (it.hasNext()) {
            DbEntity dbe = (DbEntity) it.next();
            String name = dbe.getName();
            dropTables.put(name, adapter.dropTable(dbe));
            createTables.put(name, adapter.createTable(dbe));
            if (supportsFK) {
                createFK.put(name, createFkConstraintsQueries(dbe));
            }
        }
        PkGenerator pkGenerator = adapter.getPkGenerator();
        dropPK = pkGenerator.dropAutoPkStatements(dbEntitiesRequiringAutoPK);
        createPK = pkGenerator.createAutoPkStatements(dbEntitiesRequiringAutoPK);
    }

    /**
     * Returns <code>true</code> if there is nothing to be done by this generator. If
     * <code>respectConfiguredSettings</code> is <code>true</code>, checks are done
     * applying currently configured settings, otherwise check is done, assuming that all
     * possible generated objects.
     */
    public boolean isEmpty(boolean respectConfiguredSettings) {
        if (dbEntitiesInInsertOrder.isEmpty() && dbEntitiesRequiringAutoPK.isEmpty()) {
            return true;
        }
        if (!respectConfiguredSettings) {
            return false;
        }
        return !(shouldDropTables || shouldCreateTables || shouldCreateFKConstraints || shouldCreatePKSupport || shouldDropPKSupport);
    }

    /** Returns DbAdapter associated with this DbGenerator. */
    public DbAdapter getAdapter() {
        return adapter;
    }

    /**
     * Returns a list of all schema statements that should be executed with the current
     * configuration.
     */
    public List configuredStatements() {
        List list = new ArrayList();
        if (shouldDropTables) {
            ListIterator it = dbEntitiesInInsertOrder.listIterator(dbEntitiesInInsertOrder.size());
            while (it.hasPrevious()) {
                DbEntity ent = (DbEntity) it.previous();
                list.add(dropTables.get(ent.getName()));
            }
        }
        if (shouldCreateTables) {
            Iterator it = dbEntitiesInInsertOrder.iterator();
            while (it.hasNext()) {
                DbEntity ent = (DbEntity) it.next();
                list.add(createTables.get(ent.getName()));
            }
        }
        if (shouldCreateFKConstraints && getAdapter().supportsFkConstraints()) {
            Iterator it = dbEntitiesInInsertOrder.iterator();
            while (it.hasNext()) {
                DbEntity ent = (DbEntity) it.next();
                List fks = (List) createFK.get(ent.getName());
                list.addAll(fks);
            }
        }
        if (shouldDropPKSupport) {
            list.addAll(dropPK);
        }
        if (shouldCreatePKSupport) {
            list.addAll(createPK);
        }
        return list;
    }

    /**
     * Creates a temporary DataSource out of DataSourceInfo and invokes
     * <code>public void runGenerator(DataSource ds)</code>.
     */
    public void runGenerator(DataSourceInfo dsi) throws Exception {
        this.failures = null;
        if (isEmpty(true)) {
            return;
        }
        Driver driver = (Driver) Class.forName(dsi.getJdbcDriver()).newInstance();
        DataSource dataSource = new DriverDataSource(driver, dsi.getDataSourceUrl(), dsi.getUserName(), dsi.getPassword());
        runGenerator(dataSource);
    }

    /**
     * Executes a set of commands to drop/create database objects. This is the main worker
     * method of DbGenerator. Command set is built based on pre-configured generator
     * settings.
     */
    public void runGenerator(DataSource ds) throws Exception {
        this.failures = null;
        Connection connection = ds.getConnection();
        try {
            if (shouldDropTables) {
                ListIterator it = dbEntitiesInInsertOrder.listIterator(dbEntitiesInInsertOrder.size());
                while (it.hasPrevious()) {
                    DbEntity ent = (DbEntity) it.previous();
                    safeExecute(connection, (String) dropTables.get(ent.getName()));
                }
            }
            List createdTables = new ArrayList();
            if (shouldCreateTables) {
                Iterator it = dbEntitiesInInsertOrder.iterator();
                while (it.hasNext()) {
                    DbEntity ent = (DbEntity) it.next();
                    safeExecute(connection, (String) createTables.get(ent.getName()));
                    createdTables.add(ent.getName());
                }
            }
            if (shouldCreateTables && shouldCreateFKConstraints && getAdapter().supportsFkConstraints()) {
                Iterator it = dbEntitiesInInsertOrder.iterator();
                while (it.hasNext()) {
                    DbEntity ent = (DbEntity) it.next();
                    if (createdTables.contains(ent.getName())) {
                        List fks = (List) createFK.get(ent.getName());
                        Iterator fkIt = fks.iterator();
                        while (fkIt.hasNext()) {
                            safeExecute(connection, (String) fkIt.next());
                        }
                    }
                }
            }
            if (shouldDropPKSupport) {
                List dropAutoPKSQL = getAdapter().getPkGenerator().dropAutoPkStatements(dbEntitiesRequiringAutoPK);
                Iterator it = dropAutoPKSQL.iterator();
                while (it.hasNext()) {
                    safeExecute(connection, (String) it.next());
                }
            }
            if (shouldCreatePKSupport) {
                List createAutoPKSQL = getAdapter().getPkGenerator().createAutoPkStatements(dbEntitiesRequiringAutoPK);
                Iterator it = createAutoPKSQL.iterator();
                while (it.hasNext()) {
                    safeExecute(connection, (String) it.next());
                }
            }
        } finally {
            connection.close();
        }
    }

    /**
     * Builds and executes a SQL statement, catching and storing SQL exceptions resulting
     * from invalid SQL. Only non-recoverable exceptions are rethrown.
     * 
     * @since 1.1
     */
    protected boolean safeExecute(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            QueryLogger.logQuery(sql, null);
            statement.execute(sql);
            return true;
        } catch (SQLException ex) {
            if (this.failures == null) {
                this.failures = new ValidationResult();
            }
            failures.addFailure(new SimpleValidationFailure(sql, ex.getMessage()));
            QueryLogger.logQueryError(ex);
            return false;
        } finally {
            statement.close();
        }
    }

    /**
     * Returns an array of queries to create foreign key constraints for a particular
     * DbEntity. Throws CayenneRuntimeException, if called for adapter that does not
     * support FK constraints.
     */
    public List createFkConstraintsQueries(DbEntity dbEnt) {
        if (!getAdapter().supportsFkConstraints()) {
            throw new CayenneRuntimeException("FK constraints are not supported by adapter.");
        }
        List list = new ArrayList();
        Iterator it = dbEnt.getRelationships().iterator();
        while (it.hasNext()) {
            DbRelationship rel = (DbRelationship) it.next();
            if (rel.isToMany()) {
                continue;
            }
            if (domain != null) {
                DataMap srcMap = rel.getSourceEntity().getDataMap();
                DataMap targetMap = rel.getTargetEntity().getDataMap();
                if (srcMap != null && targetMap != null && srcMap != targetMap) {
                    if (domain.lookupDataNode(srcMap) != domain.lookupDataNode(targetMap)) {
                        continue;
                    }
                }
            }
            if (rel.isToPK() && !rel.isToDependentPK()) {
                if (getAdapter().supportsUniqueConstraints()) {
                    DbRelationship reverse = rel.getReverseRelationship();
                    if (reverse != null && !reverse.isToMany() && !reverse.isToPK()) {
                        list.add(getAdapter().createUniqueConstraint((DbEntity) rel.getSourceEntity(), rel.getSourceAttributes()));
                    }
                }
                list.add(getAdapter().createFkConstraint(rel));
            }
        }
        return list;
    }

    /**
     * Returns an object representing a collection of failures that occurred on the last
     * "runGenerator" invocation, or null if there were no failures. Failures usually
     * indicate problems with generated DDL (such as "create...", "drop...", etc.) and
     * usually happen due to the DataMap being out of sync with the database.
     * 
     * @since 1.1
     */
    public ValidationResult getFailures() {
        return failures;
    }

    /**
     * Returns whether DbGenerator is configured to create primary key support for DataMap
     * entities.
     */
    public boolean shouldCreatePKSupport() {
        return shouldCreatePKSupport;
    }

    /**
     * Returns whether DbGenerator is configured to create tables for DataMap entities.
     */
    public boolean shouldCreateTables() {
        return shouldCreateTables;
    }

    public boolean shouldDropPKSupport() {
        return shouldDropPKSupport;
    }

    public boolean shouldDropTables() {
        return shouldDropTables;
    }

    public boolean shouldCreateFKConstraints() {
        return shouldCreateFKConstraints;
    }

    public void setShouldCreatePKSupport(boolean shouldCreatePKSupport) {
        this.shouldCreatePKSupport = shouldCreatePKSupport;
    }

    public void setShouldCreateTables(boolean shouldCreateTables) {
        this.shouldCreateTables = shouldCreateTables;
    }

    public void setShouldDropPKSupport(boolean shouldDropPKSupport) {
        this.shouldDropPKSupport = shouldDropPKSupport;
    }

    public void setShouldDropTables(boolean shouldDropTables) {
        this.shouldDropTables = shouldDropTables;
    }

    public void setShouldCreateFKConstraints(boolean shouldCreateFKConstraints) {
        this.shouldCreateFKConstraints = shouldCreateFKConstraints;
    }

    /**
     * Returns a DataDomain used by the DbGenerator to detect cross-database
     * relationships. By default DataDomain is null.
     * 
     * @since 1.2
     */
    public DataDomain getDomain() {
        return domain;
    }

    /**
     * Helper method that orders DbEntities to satisfy referential constraints and returns
     * an ordered list. It also filters out DerivedDbEntities.
     */
    private void prepareDbEntities(Collection excludedEntities) {
        if (excludedEntities == null) {
            excludedEntities = Collections.EMPTY_LIST;
        }
        List tables = new ArrayList();
        List tablesWithAutoPk = new ArrayList();
        Iterator it = map.getDbEntities().iterator();
        while (it.hasNext()) {
            DbEntity nextEntity = (DbEntity) it.next();
            if (nextEntity instanceof DerivedDbEntity) {
                continue;
            }
            if (nextEntity.getAttributes().size() == 0) {
                logObj.info("Skipping entity with no attributes: " + nextEntity.getName());
                continue;
            }
            if (excludedEntities.contains(nextEntity)) {
                continue;
            }
            boolean invalidAttributes = false;
            Iterator nextDbAtributes = nextEntity.getAttributes().iterator();
            while (nextDbAtributes.hasNext()) {
                DbAttribute attr = (DbAttribute) nextDbAtributes.next();
                if (attr.getType() == TypesMapping.NOT_DEFINED) {
                    logObj.info("Skipping entity, attribute type is undefined: " + nextEntity.getName() + "." + attr.getName());
                    invalidAttributes = true;
                    break;
                }
            }
            if (invalidAttributes) {
                continue;
            }
            tables.add(nextEntity);
            Iterator relationships = nextEntity.getRelationships().iterator();
            List pkAttributes = new ArrayList(nextEntity.getPrimaryKey());
            while (pkAttributes.size() > 0 && relationships.hasNext()) {
                DbRelationship nextRelationship = (DbRelationship) relationships.next();
                if (!nextRelationship.isToMasterPK()) {
                    continue;
                }
                Iterator joins = nextRelationship.getJoins().iterator();
                while (joins.hasNext()) {
                    DbJoin join = (DbJoin) joins.next();
                    pkAttributes.remove(join.getSource());
                }
            }
            if (pkAttributes.size() > 0) {
                tablesWithAutoPk.add(nextEntity);
            }
        }
        if (tables.size() > 1) {
            DataNode node = new DataNode("temp");
            node.addDataMap(map);
            node.getEntitySorter().sortDbEntities(tables, false);
        }
        this.dbEntitiesInInsertOrder = tables;
        this.dbEntitiesRequiringAutoPK = tablesWithAutoPk;
    }
}

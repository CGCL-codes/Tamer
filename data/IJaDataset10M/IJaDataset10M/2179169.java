package org.pixory.pxmodel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pixory.pxfoundation.PXComparableUtility;
import org.pixory.pxfoundation.PXIntegerUtility;
import org.pixory.pxfoundation.PXObjectUtility;
import org.pixory.pxfoundation.PXStringUtility;

/**
 * This class is designed to help ease the "evolution" of the Hibernate model
 * and accompanying hsqldb schema that underlie the ObjectStore. Each instance
 * (version number) knows how to generate DDL/DML to bring the schema
 * "up-to-date" from the previous instance.
 * 
 * All subclasses should follow singleton pattern where singleton is retrieved
 * with static method getInstance()
 * 
 * When there is no pre-existing DB, that is when an interrogated PXObjectStore
 * reports no tables (newly created), the Store is considered at version -1.
 * When there is no PXSystem table in an interrogated Store, it is at version 0.
 * All other versions come directly from the PXSystem table.
 * 
 * The SQL dialect here is specific to HSQL DB.
 * 
 * The actual ObjectStore database is tagged with a version number through the
 * PXSystem persistent class/table.
 */
class PXObjectStoreVersion {

    private static final Log LOG = LogFactory.getLog(PXObjectStoreVersion.class);

    public static final Dialect DIALECT = new HSQLDialect();

    public static final Integer PRE_DB_STORE_VERSION_NUMBER = new Integer(-1);

    public static final Integer PRE_TRACKING_STORE_VERSION_NUMBER = new Integer(0);

    private static final String SYSTEM_TABLE_NAME = "PX_SYSTEM";

    /**
	 * all of our hsql tables will be hsql type 'cached', but hibernate doesn't
	 * know anything about this; so this hack
	 */
    private static final String HSQL_TABLE_SPECIFIER = "cached table";

    private static PXObjectStoreVersion _nullInstance;

    private static List _allVersions;

    private Integer _versionNumber;

    private List _allPersistentClasses;

    protected PXObjectStoreVersion() {
    }

    /**
	 * guaranteed to be in ascending order, no breaks. Start at version 0, and
	 * then keep incrementing version and loading classes until get ClassNotFound
	 */
    static List getAllVersions() {
        if (_allVersions == null) {
            _allVersions = new ArrayList();
            String aParentClassName = PXObjectStoreVersion.class.getName();
            try {
                for (int i = 0; true; i++) {
                    String aClassName = aParentClassName + i;
                    Class aClass = Class.forName(aClassName);
                    PXObjectStoreVersion aVersionInstance = (PXObjectStoreVersion) PXObjectUtility.invokeTargetMethod("getInstance", null, aClass);
                    if (aVersionInstance != null) {
                        _allVersions.add(aVersionInstance);
                    } else {
                        String aMessage = "No instance for class: " + aClass;
                        LOG.warn(aMessage);
                    }
                }
            } catch (ClassNotFoundException e) {
                LOG.debug(e);
            } catch (Exception e) {
                LOG.warn(null, e);
            }
        }
        return _allVersions;
    }

    /**
	 * @return the instance that corresponds to the latest schema version
	 */
    static PXObjectStoreVersion getLatestVersion() {
        PXObjectStoreVersion getLatestVersion = null;
        List someVersions = getAllVersions();
        if ((someVersions != null) && (someVersions.size() > 0)) {
            getLatestVersion = (PXObjectStoreVersion) someVersions.get(someVersions.size() - 1);
        }
        return getLatestVersion;
    }

    static Integer getLatestVersionNumber() {
        Integer getLatestVersionNumber = null;
        PXObjectStoreVersion aLatestVersion = getLatestVersion();
        if (aLatestVersion != null) {
            getLatestVersionNumber = aLatestVersion.getVersionNumber();
        }
        return getLatestVersionNumber;
    }

    static PXObjectStoreVersion getVersion(Integer versionNumber) {
        PXObjectStoreVersion getVersion = null;
        if (versionNumber != null) {
            int aNumber = versionNumber.intValue();
            List someVersions = getAllVersions();
            if ((someVersions != null) && (someVersions.size() > 0) && (aNumber < someVersions.size())) {
                getVersion = (PXObjectStoreVersion) someVersions.get(aNumber);
            }
        }
        return getVersion;
    }

    /**
	 * @return List of PXObjectStoreVersion objects btween endpoints, inclusive
	 */
    static List getVersionsBetween(Integer startVersion, Integer endVersion) {
        List getVersionsBetween = null;
        if (startVersion == null) {
            startVersion = new Integer(0);
        }
        if (endVersion == null) {
            endVersion = getLatestVersionNumber();
        }
        List allVersions = getAllVersions();
        if ((allVersions != null) && (allVersions.size() > 0)) {
            Iterator aVersionIterator = allVersions.iterator();
            while (aVersionIterator.hasNext()) {
                PXObjectStoreVersion aVersion = (PXObjectStoreVersion) aVersionIterator.next();
                Integer aVersionNumber = aVersion.getVersionNumber();
                if ((PXComparableUtility.isGreaterThanOrEqualTo(aVersionNumber, startVersion)) && (PXComparableUtility.isLessThanOrEqualTo(aVersionNumber, endVersion))) {
                    if (getVersionsBetween == null) {
                        getVersionsBetween = new ArrayList();
                    }
                    getVersionsBetween.add(aVersion);
                }
            }
        }
        return getVersionsBetween;
    }

    /**
	 * @return List is guaranteed to be in ascending order with no breaks
	 */
    static List getVersionsUpTo(Integer versionNumber) {
        List getVersionsUpTo = null;
        if (versionNumber != null) {
            List allVersions = getAllVersions();
            if ((allVersions != null) && (allVersions.size() > 0)) {
                Iterator aVersionIterator = allVersions.iterator();
                while (aVersionIterator.hasNext()) {
                    PXObjectStoreVersion aVersion = (PXObjectStoreVersion) aVersionIterator.next();
                    if (PXComparableUtility.isLessThanOrEqualTo(aVersion.getVersionNumber(), versionNumber)) {
                        if (getVersionsUpTo == null) {
                            getVersionsUpTo = new ArrayList();
                        }
                        getVersionsUpTo.add(aVersion);
                    }
                }
            }
        }
        return getVersionsUpTo;
    }

    /**
	 * for subclasses
	 */
    Integer getVersionNumber() {
        if (_versionNumber == null) {
            Class aSuperClass = this.getClass().getSuperclass();
            if (aSuperClass == Object.class) {
                _versionNumber = PRE_DB_STORE_VERSION_NUMBER;
            } else {
                String aClassName = PXObjectUtility.getUnqualifiedClassName(this.getClass());
                String aSuperClassName = PXObjectUtility.getUnqualifiedClassName(this.getClass().getSuperclass());
                String aVersionString = PXStringUtility.removePrefix(aClassName, aSuperClassName);
                _versionNumber = new Integer(aVersionString);
            }
        }
        return _versionNumber;
    }

    /**
	 * @return an empty model-- no persistent classes, no schema, no nothin
	 */
    static PXObjectStoreVersion getNullInstance() {
        if (_nullInstance == null) {
            _nullInstance = new PXObjectStoreVersion();
        }
        return _nullInstance;
    }

    /**
	 * @param store
	 * @return the version number of the schema in the supplied store. -1
	 *         indicates that there is no existing schema (no db), so you are
	 *         starting from scratch. 0 indicates that the schema predates this
	 *         schema tracking technique (there is no schema tracking) table.
	 *         Otherwise, the value comes from the schema tracking column of
	 *         PX_SYSTEM. N.B. can't use Hibernate mapped classes to get this
	 *         value, since this method should work even if called before
	 *         Hibernate is fully configured
	 * 
	 * @throws PXObjectStoreException
	 */
    static Integer getDatabaseVersionNumber(PXObjectStore store) throws PXObjectStoreException {
        Integer getDatabaseVersionNumber = PRE_DB_STORE_VERSION_NUMBER;
        if (store != null) {
            Set someTableNames = store.getUserTableNames();
            LOG.debug("someTableNames: " + someTableNames);
            if ((someTableNames != null) && (someTableNames.size() > 0)) {
                getDatabaseVersionNumber = PRE_TRACKING_STORE_VERSION_NUMBER;
                if (someTableNames.contains(SYSTEM_TABLE_NAME)) {
                    getDatabaseVersionNumber = fetchStoreVersionNumber(store);
                }
            }
        }
        return getDatabaseVersionNumber;
    }

    protected void ensureCurrentStoredVersionNumber(PXObjectStore store, Integer storeVersion) throws PXObjectStoreException {
        if ((store != null) && (storeVersion != null)) {
            Integer aVersionNumber = this.getVersionNumber();
            if ((PXComparableUtility.isGreaterThan(aVersionNumber, storeVersion)) && (aVersionNumber.intValue() > 1)) {
                Session aSession = null;
                try {
                    aSession = store.createSession();
                    String findSystemsQuery = "from org.pixory.pxmodel.PXSystem as system";
                    List someSystems = aSession.createQuery(findSystemsQuery).list();
                    if ((someSystems != null) && (someSystems.size() > 0)) {
                        if (someSystems.size() == 1) {
                            PXSystem aSystem = (PXSystem) someSystems.get(0);
                            Transaction aTransaction = aSession.beginTransaction();
                            aSystem.setSchemaVersion(aVersionNumber);
                            aSystem.setSchemaUpdateDate(new Date());
                            aTransaction.commit();
                        } else {
                            LOG.warn("Wrong number of system records!");
                        }
                    } else {
                        LOG.warn("No system records!");
                    }
                } catch (Exception e) {
                    LOG.warn(null, e);
                    if (!(e instanceof PXObjectStoreException)) {
                        e = new PXObjectStoreException(e);
                    }
                    throw (PXObjectStoreException) e;
                } finally {
                    if (aSession != null) {
                        try {
                            aSession.close();
                        } catch (HibernateException e) {
                            LOG.warn(null, e);
                        }
                    }
                }
            }
        }
    }

    /**
	 * fetch SCHEMAVERSION column value from the PX_SYSTEM table
	 */
    private static Integer fetchStoreVersionNumber(PXObjectStore store) {
        Integer fetchStoreVersionNumber = null;
        if (store != null) {
            Session aSession = null;
            try {
                aSession = store.createSession();
                Connection aConnection = aSession.connection();
                List someResults = PXSqlUtility.executeQuery("select schemaversion from " + SYSTEM_TABLE_NAME, aConnection);
                if ((someResults != null) && (someResults.size() == 1)) {
                    Map aResultRow = (Map) someResults.get(0);
                    fetchStoreVersionNumber = (Integer) aResultRow.get("SCHEMAVERSION");
                } else {
                    String aMessage = "PXObjectStore contains invalid number of System records";
                    throw new PXObjectStoreException(aMessage);
                }
            } catch (Exception e) {
                LOG.warn(null, e);
            } finally {
                if (aSession != null) {
                    try {
                        aSession.close();
                    } catch (Exception e) {
                        LOG.warn(null, e);
                    }
                }
            }
        }
        return fetchStoreVersionNumber;
    }

    /**
	 * subclasses must override
	 * 
	 * @return List of persistent classes newly introduced by this schema version
	 */
    List getAddedPersistentClasses() {
        return null;
    }

    /**
	 * @return List of persistent Class objects representing all
	 *         PersistentObjects cumulatively added by all versions up to this
	 *         version
	 */
    protected List getAllPersistentClasses() {
        if (_allPersistentClasses == null) {
            _allPersistentClasses = new ArrayList();
            List someVersions = getVersionsUpTo(this.getVersionNumber());
            if (someVersions != null) {
                Iterator aVersionIterator = someVersions.iterator();
                while (aVersionIterator.hasNext()) {
                    PXObjectStoreVersion aVersion = (PXObjectStoreVersion) aVersionIterator.next();
                    List someAddedClasses = aVersion.getAddedPersistentClasses();
                    if ((someAddedClasses != null) && (someAddedClasses.size() > 0)) {
                        _allPersistentClasses.addAll(someAddedClasses);
                    }
                }
            }
        }
        return _allPersistentClasses;
    }

    /**
	 * @param store
	 *           to make current
	 * @throws PXObjectStoreException
	 */
    void ensureCurrent(PXObjectStore store) throws PXObjectStoreException {
        if (store != null) {
            Integer aStoreVersion = getDatabaseVersionNumber(store);
            LOG.info("store version: " + aStoreVersion);
            if (aStoreVersion != null) {
                List somePersistentClasses = this.getAllPersistentClasses();
                if (somePersistentClasses != null) {
                    store.addPersistentClasses(somePersistentClasses);
                }
                List someStatements = this.generateSchemaUpdateStatements(store);
                if (someStatements != null) {
                    this.applySchemaUpdateStatements(someStatements, store);
                }
                this.applyVersionsDML(store, aStoreVersion);
                this.ensureCurrentStoredVersionNumber(store, aStoreVersion);
            } else {
                throw new PXObjectStoreException("Invalid store version number");
            }
        }
    }

    private void applyVersionsDML(PXObjectStore store, Integer storeVersion) {
        if ((store != null) && (storeVersion != null)) {
            Integer aStartVersion = PXIntegerUtility.add(storeVersion, 1);
            List someVersions = getVersionsBetween(aStartVersion, this.getVersionNumber());
            if (someVersions != null) {
                Iterator aVersionIterator = someVersions.iterator();
                while (aVersionIterator.hasNext()) {
                    PXObjectStoreVersion aVersion = (PXObjectStoreVersion) aVersionIterator.next();
                    aVersion.applyDML(store);
                }
            }
        }
    }

    private List generateSchemaUpdateStatements(PXObjectStore store) throws PXObjectStoreException {
        List generate = null;
        if (store != null) {
            try {
                Configuration aConfiguation = store.getConfiguration();
                Session aSession = store.createSession();
                Connection aConnection = aSession.connection();
                DatabaseMetadata aMetaData = new DatabaseMetadata(aConnection, DIALECT);
                String[] someUpdateScripts = aConfiguation.generateSchemaUpdateScript(DIALECT, aMetaData);
                LOG.debug("someUpdateScripts: " + ArrayUtils.toString(someUpdateScripts));
                aSession.close();
                generate = Arrays.asList(someUpdateScripts);
            } catch (Exception e) {
                LOG.warn(null, e);
                if (!(e instanceof PXObjectStoreException)) {
                    e = new PXObjectStoreException(e);
                }
                throw (PXObjectStoreException) e;
            }
        }
        return generate;
    }

    private void applySchemaUpdateStatements(List statements, PXObjectStore store) {
        if ((statements != null) && (store != null)) {
            List someVersions = getVersionsUpTo(this.getVersionNumber());
            if (someVersions != null) {
                List someStatements = new ArrayList(statements);
                someStatements = this.modifyDDLScriptsForDialect(someStatements);
                Iterator aVersionIterator = someVersions.iterator();
                while (aVersionIterator.hasNext()) {
                    PXObjectStoreVersion aVersion = (PXObjectStoreVersion) aVersionIterator.next();
                    someStatements = aVersion.willExecuteSchemaCreationStatements(someStatements);
                }
                if (someStatements != null) {
                    store.applyUpdateStatements(someStatements);
                    store.applyUpdateStatement("CHECKPOINT");
                }
            }
        }
    }

    /**
	 * subclasses override; default implementation does nothing
	 */
    protected List willExecuteSchemaCreationStatements(List statements) {
        return statements;
    }

    /**
	 * subclasses override; default implementation does nothing
	 */
    protected void applyDML(PXObjectStore store) {
    }

    private List modifyDDLScriptsForDialect(List statements) {
        List modifyDDLScripts = null;
        if ((statements != null) && (statements.size() > 0)) {
            modifyDDLScripts = new ArrayList(statements.size());
            Iterator aStatementIterator = statements.iterator();
            while (aStatementIterator.hasNext()) {
                String aStatement = (String) aStatementIterator.next();
                aStatement = this.modifyDDLScriptForDialect(aStatement);
                modifyDDLScripts.add(aStatement);
            }
        }
        return modifyDDLScripts;
    }

    /**
	 * @param ddlScript
	 *           any ddl statement
	 * @return right now simply makes any "create table" into "create cached
	 *         table" and ensures that foreign key constraints are ON DELETE
	 *         CASCADEs, and fixes a bug in the "alter table add column"
	 *         statement
	 */
    private String modifyDDLScriptForDialect(String ddlScript) {
        String modifyDDLScriptForDialect = ddlScript;
        if (ddlScript != null) {
            if (ddlScript.startsWith("create table")) {
                modifyDDLScriptForDialect = ddlScript.replaceFirst("table", HSQL_TABLE_SPECIFIER);
            } else if ((StringUtils.contains(ddlScript, "add constraint")) && (StringUtils.contains(ddlScript, "foreign key"))) {
                modifyDDLScriptForDialect = ddlScript + " ON DELETE CASCADE";
            } else if ((ddlScript.startsWith("alter table")) && (StringUtils.contains(ddlScript, "add column"))) {
                modifyDDLScriptForDialect = ddlScript.replaceFirst("add column", "add column ");
            }
        }
        return modifyDDLScriptForDialect;
    }
}

package org.databene.jdbacl.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.databene.commons.ObjectNotFoundException;
import org.databene.commons.StringUtil;
import org.databene.commons.collection.OrderedNameMap;
import org.databene.commons.version.VersionNumber;
import org.databene.jdbacl.DatabaseDialect;
import org.databene.jdbacl.DatabaseDialectManager;
import org.databene.jdbacl.model.jdbc.JDBCDBImporter;

/**
 * Represents a database.<br/><br/>
 * Created: 06.01.2007 18:34:20
 * @author Volker Bergmann
 */
public class Database extends AbstractCompositeDBObject<DBCatalog> implements TableHolder, SequenceHolder {

    private static final long serialVersionUID = -1975619615948817919L;

    private String environment;

    private String productName;

    private VersionNumber productVersion;

    private Date importDate;

    private String user;

    private String tableInclusionPattern;

    private String tableExclusionPattern;

    private Set<String> reservedWords;

    private OrderedNameMap<DBCatalog> catalogs;

    private JDBCDBImporter importer;

    private boolean sequencesImported;

    private boolean triggersImported;

    private boolean packagesImported;

    private boolean checksImported;

    public Database(String environment) {
        this(environment, new JDBCDBImporter(environment), true);
    }

    public Database(String environment, String productName, String productVersion, Date importDate) {
        this(environment, null, false);
        this.productName = productName;
        this.productVersion = VersionNumber.valueOf(productVersion);
        this.importDate = importDate;
    }

    public Database(String environment, JDBCDBImporter importer, boolean prepopulate) {
        super(environment, "database");
        try {
            this.environment = environment;
            this.reservedWords = null;
            this.catalogs = OrderedNameMap.createCaseIgnorantMap();
            this.sequencesImported = false;
            this.triggersImported = false;
            this.packagesImported = false;
            this.checksImported = false;
            this.importer = importer;
            if (importer != null) {
                this.productName = importer.getDatabaseProductName();
                this.productVersion = importer.getDatabaseProductVersion();
                this.importDate = new Date();
                if (prepopulate) {
                    importer.importCatalogs(this);
                    importer.importSchemas(this);
                    importer.importAllTables(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Database import failed for environment " + environment, e);
        }
    }

    public String getEnvironment() {
        return environment;
    }

    public String getDatabaseProductName() {
        return productName;
    }

    public VersionNumber getDatabaseProductVersion() {
        return productVersion;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTableInclusionPattern() {
        return tableInclusionPattern;
    }

    public void setTableInclusionPattern(String tableInclusionPattern) {
        this.tableInclusionPattern = tableInclusionPattern;
    }

    public String getTableExclusionPattern() {
        return tableExclusionPattern;
    }

    public void setTableExclusionPattern(String tableExclusionPattern) {
        this.tableExclusionPattern = tableExclusionPattern;
    }

    public boolean isReservedWord(String word) {
        return getReservedWords().contains(word);
    }

    private Set<String> getReservedWords() {
        if (reservedWords == null) {
            try {
                Connection connection = (importer != null ? importer.getConnection() : null);
                DatabaseDialect dialect = DatabaseDialectManager.getDialectForProduct(productName, productVersion);
                reservedWords = dialect.getReservedWords(connection);
            } catch (Exception e) {
                throw new RuntimeException("Error fetching reserved words", e);
            }
        }
        return reservedWords;
    }

    public JDBCDBImporter getImporter() {
        return importer;
    }

    public List<DBCatalog> getComponents() {
        return catalogs.values();
    }

    public List<DBCatalog> getCatalogs() {
        return getComponents();
    }

    public DBCatalog getCatalog(String catalogName) {
        return catalogs.get(catalogName);
    }

    public void addCatalog(DBCatalog catalog) {
        catalog.setDatabase(this);
        catalogs.put(catalog.getName(), catalog);
    }

    public void removeCatalog(DBCatalog catalog) {
        catalogs.remove(catalog.getName());
        catalog.setOwner(null);
    }

    public DBSchema getSchema(String schemaName) {
        for (DBCatalog catalog : getCatalogs()) {
            DBSchema schema = catalog.getSchema(schemaName);
            if (schema != null) return schema;
        }
        throw new ObjectNotFoundException("Table '" + name + "'");
    }

    public List<DBTable> getTables() {
        return getTables(true);
    }

    public List<DBTable> getTables(boolean recursive) {
        if (!recursive) return new ArrayList<DBTable>();
        List<DBTable> tables = new ArrayList<DBTable>();
        for (DBCatalog catalog : getCatalogs()) for (DBTable table : catalog.getTables()) tables.add(table);
        return tables;
    }

    public DBTable getTable(String name) {
        return getTable(name, true);
    }

    public DBTable getTable(String name, boolean required) {
        for (DBCatalog catalog : getCatalogs()) for (DBTable table : catalog.getTables()) if (StringUtil.equalsIgnoreCase(table.getName(), name)) return table;
        if (required) throw new ObjectNotFoundException("Table '" + name + "'"); else return null;
    }

    public void removeTable(String tableName) {
        DBTable table = getTable(tableName, true);
        table.getSchema().removeTable(table);
    }

    public List<DBSequence> getSequences() {
        haveSequencesImported();
        return getSequences(true);
    }

    public List<DBSequence> getSequences(boolean recursive) {
        haveSequencesImported();
        if (!recursive) return new ArrayList<DBSequence>();
        List<DBSequence> sequences = new ArrayList<DBSequence>();
        for (DBCatalog catalog : getCatalogs()) for (DBSequence table : catalog.getSequences()) sequences.add(table);
        return sequences;
    }

    public synchronized void haveSequencesImported() {
        if (!sequencesImported) {
            if (importer != null) importer.importSequences(this);
            this.sequencesImported = true;
        }
    }

    public boolean isSequencesImported() {
        return sequencesImported;
    }

    public void setSequencesImported(boolean sequencesImported) {
        this.sequencesImported = sequencesImported;
    }

    public List<DBTrigger> getTriggers() {
        haveTriggersImported();
        List<DBTrigger> triggers = new ArrayList<DBTrigger>();
        for (DBCatalog catalog : getCatalogs()) for (DBSchema schema : catalog.getSchemas()) triggers.addAll(schema.getTriggers());
        return triggers;
    }

    public synchronized void haveTriggersImported() {
        if (!triggersImported) {
            try {
                if (importer != null) importer.importTriggers(this);
                triggersImported = true;
            } catch (SQLException e) {
                throw new RuntimeException("Import of database triggers failed: " + getName(), e);
            }
        }
    }

    public boolean isTriggersImported() {
        return triggersImported;
    }

    public void setTriggersImported(boolean triggersImported) {
        this.triggersImported = triggersImported;
    }

    public List<DBPackage> getPackages() {
        havePackagesImported();
        List<DBPackage> packages = new ArrayList<DBPackage>();
        for (DBCatalog catalog : getCatalogs()) for (DBSchema schema : catalog.getSchemas()) packages.addAll(schema.getPackages());
        return packages;
    }

    public synchronized void havePackagesImported() {
        if (!packagesImported) {
            try {
                packagesImported = true;
                if (importer != null) importer.importPackages(this);
            } catch (SQLException e) {
                throw new RuntimeException("Import of database packages failed: " + getName(), e);
            }
        }
    }

    public boolean isPackagesImported() {
        return packagesImported;
    }

    public void setPackagesImported(boolean packagesImported) {
        this.packagesImported = packagesImported;
    }

    public boolean isChecksImported() {
        return this.checksImported;
    }

    public void setChecksImported(boolean checksImported) {
        this.checksImported = checksImported;
    }

    public synchronized void haveChecksImported() {
        if (!isChecksImported()) if (importer != null) importer.importAllChecks(this);
    }
}

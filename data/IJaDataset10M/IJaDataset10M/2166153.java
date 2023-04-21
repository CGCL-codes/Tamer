package oracle.toplink.essentials.internal.ejb.cmp3.metadata.tables;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import oracle.toplink.essentials.internal.ejb.cmp3.metadata.MetadataHelper;
import oracle.toplink.essentials.internal.ejb.cmp3.metadata.MetadataLogger;
import oracle.toplink.essentials.internal.helper.DatabaseTable;

/**
 * Object to hold onto table metadata in a TopLink database table.
 * 
 * @author Guy Pelletier
 * @since TopLink EJB 3.0 Reference Implementation
 */
public class MetadataTable {

    protected String m_name;

    protected String m_schema;

    protected String m_catalog;

    protected MetadataLogger m_logger;

    protected DatabaseTable m_databaseTable;

    /**
     * INTERNAL:
     */
    public MetadataTable(MetadataLogger logger) {
        m_logger = logger;
        m_databaseTable = new DatabaseTable();
    }

    /**
     * INTERNAL:
     */
    public MetadataTable(Table table, MetadataLogger logger) {
        this(logger);
        if (table != null) {
            m_name = table.name();
            m_schema = table.schema();
            m_catalog = table.catalog();
            processName();
            processUniqueConstraints(table.uniqueConstraints());
        }
    }

    /**
     * INTERNAL:
     */
    public String getCatalog() {
        return m_catalog;
    }

    /**
     * INTERNAL:
     * The context should be overridden by subclasses for more specific
     * logging messages.
     */
    public String getCatalogContext() {
        return m_logger.TABLE_CATALOG;
    }

    /**
     * INTERNAL:
     */
    public DatabaseTable getDatabaseTable() {
        return m_databaseTable;
    }

    /**
     * INTERNAL:
     */
    public String getName() {
        return m_name;
    }

    /**
     * INTERNAL:
     * The context should be overridden by subclasses for more specific
     * logging messages.
     */
    public String getNameContext() {
        return m_logger.TABLE_NAME;
    }

    /**
     * INTERNAL:
     */
    public String getSchema() {
        return m_schema;
    }

    /**
     * INTERNAL:
     * The context should be overridden by subclasses for more specific
     * logging messages.
     */
    public String getSchemaContext() {
        return m_logger.TABLE_SCHEMA;
    }

    /**
     * INTERNAL:
     */
    public boolean loadedFromXML() {
        return false;
    }

    /**
     * INTERNAL:
     */
    protected void processName() {
        if (!m_name.equals("")) {
            setName(MetadataHelper.getFullyQualifiedTableName(m_name, m_catalog, m_schema));
        }
    }

    /**
     * INTERNAL:
     * Process the unique constraints for the given table.
     */
    protected void processUniqueConstraints(UniqueConstraint[] uniqueConstraints) {
        for (UniqueConstraint uniqueConstraint : uniqueConstraints) {
            m_databaseTable.addUniqueConstraints(uniqueConstraint.columnNames());
        }
    }

    /**
     * INTERNAL:
     */
    public void setName(String name) {
        m_databaseTable.setPossiblyQualifiedName(name);
    }
}

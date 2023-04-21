package net.sourceforge.dbtoolbox.extractor;

import java.sql.Connection;
import java.sql.SQLException;
import net.sourceforge.dbtoolbox.model.*;

/**
 * Extract all metadata from a generic database
 */
public class GenericDatabaseMDExtractor extends AbstractDatabaseMDExtractor {

    /**
     * Minimal constructor
     * @param connection JDBC connection
     */
    public GenericDatabaseMDExtractor(Connection connection) {
        super(connection);
    }

    /**
     * Create property extractor
     * @return Property extractor
     */
    protected MDExtractor<PropertyMD> createPropertyExtractor() throws SQLException {
        return new PropertyMDExtractor(this);
    }

    /**
     * Create catalog extractor
     * @return Catalog extractor
     */
    protected MDExtractor<CatalogMD> createCatalogExtractor() throws SQLException {
        return new CatalogMDExtractor(this);
    }

    /**
     * Create schema extractor
     * @return Schema extractor
     */
    protected MDExtractor<SchemaMD> createSchemaExtractor() throws SQLException {
        return new RepairSchemaMDProcessor(new SchemaMDExtractor(this));
    }

    /**
     * Create table extractor
     * @return Table extractor
     */
    protected MDExtractor<TableMD> createTableExtractor() throws SQLException {
        return new TableMDExtractor(this, null);
    }

    /**
     * Create view extractor
     * @return View extractor
     */
    protected MDExtractor<ViewMD> createViewExtractor() throws SQLException {
        return new ViewMDExtractor(this, null);
    }

    protected MDExtractor<PrimaryKeyConstraintMD> createPrimaryKeyConstraintExtractor() throws SQLException {
        return new PrimaryKeyConstraintMDExtractor(this);
    }

    protected MDExtractor<ForeignKeyConstraintMD> createForeignKeyConstraintExtractor() throws SQLException {
        return new ForeignKeyConstraintMDExtractor(this);
    }

    protected MDExtractor<IndexMD> createIndexExtractor() throws SQLException {
        return new IndexMDExtractor(this);
    }

    /**
     * Create procedure extractor
     * @return Procedure extractor
     */
    protected MDExtractor<ProcedureMD> createProcedureExtractor() throws SQLException {
        return new ProcedureMDExtractor(this, null);
    }

    /**
     * Create procedure arguments extractor
     * @return Procedure arguments extractor
     */
    protected MDExtractor<CallableColumnMD> createProcedureColumnExtractor() throws SQLException {
        return new ProcedureColumnMDExtractor(this, null, null);
    }

    /**
     * Create function extractor
     * @return Function extractor
     */
    protected MDExtractor<FunctionMD> createFunctionExtractor() throws SQLException {
        return new FunctionMDExtractor(this, null);
    }

    /**
     * Create function arguments extractor
     * @return Function arguments extractor
     */
    protected MDExtractor<CallableColumnMD> createFunctionColumnExtractor() throws SQLException {
        return new FunctionColumnMDExtractor(this, null, null);
    }

    /**
     * Create column extractor
     * @return Column exctractor
     */
    protected MDExtractor<TabularColumnMD> createTabularColumnExtractor() throws SQLException {
        return new TabularColumnMDExtractor(this, null, null);
    }

    /**
     * Create sequence extractor
     * @return Sequence extractor
     */
    protected MDExtractor<SequenceMD> createSequenceExtractor() throws SQLException {
        return new NoOpMDExtractor<SequenceMD>(this, "Sequence");
    }

    /**
     * Create trigger extractor
     * @return Trigger extractor
     */
    protected MDExtractor<TriggerMD> createTriggerExtractor() throws SQLException {
        return new NoOpMDExtractor<TriggerMD>(this, "Trigger");
    }
}

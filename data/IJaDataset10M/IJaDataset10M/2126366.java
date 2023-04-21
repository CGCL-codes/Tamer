package net.sourceforge.dbtoolbox.model;

/**
 * Base interface for schema elements such as: tables, procedures, views...
 */
public interface SchemaElementMD {

    /**
     * Get parent schema
     */
    public SchemaMD getSchema();
}

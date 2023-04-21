package schemacrawler.schema;

/**
 * Represents a single column mapping from a primary key column to a
 * foreign key column.
 * 
 * @author Sualeh Fatehi
 */
public interface ForeignKeyColumnMap extends ColumnMap, Comparable<ForeignKeyColumnMap> {

    /**
   * Gets the sequence in the foreign key.
   * 
   * @return Foreign key sequence
   */
    int getKeySequence();
}

package org.datanucleus.store.schema.table;

import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;

/**
 * Representation of a table for a class.
 */
public interface Table {

    /**
     * Accessor for the identifier for this table (its "name).
     * @return The table identifier
     */
    String getIdentifier();

    /**
     * Accessor for the primary class metadata for this table.
     * @return Class metadata
     */
    AbstractClassMetaData getClassMetaData();

    /**
     * Accessor for number of columns.
     * @return Number of cols
     */
    int getNumberOfColumns();

    /**
     * Accessor for the column for the specified member.
     * @param mmd Metadata for the member
     * @return The column (or null if invalid member)
     */
    BasicColumn getColumnForMember(AbstractMemberMetaData mmd);

    /**
     * Accessor for the column at the specified position (numbered from 0 to numcols-1).
     * @param pos Position of the column
     * @return The column at this position (or null if invalid position)
     */
    BasicColumn getColumnForPosition(int pos);
}

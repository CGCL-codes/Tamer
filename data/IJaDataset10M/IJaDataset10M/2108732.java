package org.sqlorm.metadatadumper;

/**
 * Marker interface to denote a certain class represents the name of a table
 * 
 * @author Kasper B. Graversen, (c) 2007-2008
 */
public interface ITableName {

    /** return the table name */
    String toString();
}

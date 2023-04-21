package com.antlersoft.ilanalyze.db;

/**
 * Interface implemented by objects that have a DBType
 * @author Michael A. MacDonald
 *
 */
public interface HasDBType {

    /**
	 * Gets the DBType that represents the type of this object
	 * @param db ILDB holding the analyzed system
	 * @return Type associated witht his object
	 */
    public DBType getDBType(ILDB db);
}

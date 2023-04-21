package mipt.rdb;

import mipt.search.Criterion;

/**
 * Collaborator of TableSQL who know table structure and field types
 * Fields are columns but their enumeration differs: e.g. fieldIndex = -1 denotes ID column; 
 * field enumeration is used if fieldIndex is specified by client (who "does not know" about id column)
 * @see mipt.search.Criterion 
 * @author Evdokimov
 */
public interface TableMetaInfo {

    /**
	 * Should be already quoted
	 */
    public abstract String getFullTableName();

    /**
	 * Returns schema name (can't be quoted) or null to access table without schema 
	 */
    public abstract String getSchema();

    /**
	 * Should be NOT quoted
	 * Often returns getFieldName(-1) isf it is not optimizeable
	 */
    public abstract String getIdColumnName();

    /**
	 * Return column count - 1 : -1 fieldIndex denotes ID column 
	 */
    public abstract int getFieldCount();

    /**
	 * Return column name (withod quotes) but enumeration differs: e.g. -1 denotes ID column 
	 */
    public abstract String getFieldName(int fieldIndex);

    /**
	 * Return column SQL type
	 */
    public abstract int getFieldType(int fieldIndex);

    /**
	 * Is for INSERT only?
	 */
    public abstract int getColumnIndex(int fieldIndex);

    /**
	 * Returns -1 if fieldIndex conforms to id column 
	 */
    public abstract int getFieldIndex(String fieldName);

    /**
	 * Extracts fieldIndex from Criterion; if by fieldName, works as getFieldIndex(String): 
			if(where.fieldName==null) return where.fieldIndex;
			return getFieldIndex(where.fieldName);
	 */
    public abstract int getFieldIndex(Criterion where);

    /**
	 * Quote the given string if settings say that table and column names should be quoted 
	 */
    public abstract String quote(String str);
}

package query.plan;

import metadata.Schema;
import query.scan.Scan;

/**
 * A query tree constructed for the purpose of cost comparison.
 * 
 * @author Alex
 */
public interface Plan {

    /**
	 * Creates the plan's corresponding scan.
	 * 
	 * @return scan
	 */
    public Scan open(Scan s);

    /**
	 * Returns an estimate of the number of block accesses that will occur when
	 * the scan is read to completion.
	 * 
	 * @return the estimated number of block accesses
	 */
    public int blocksAccessed();

    /**
	 * Returns an estimate of the number of records in the query's output table.
	 * 
	 * @return the estimated number of output records
	 */
    public int recordsOutput();

    /**
	 * Returns an estimate of the number of distinct values for the specified
	 * field in the query's output table.
	 * 
	 * @param fldname
	 *            the name of a field
	 * @return the estimated number of distinct field values in the output
	 */
    public int distinctValues(String fldName);

    /**
	 * Returns the schema of the query.
	 * 
	 * @return the query's schema
	 */
    public Schema getSchema();
}

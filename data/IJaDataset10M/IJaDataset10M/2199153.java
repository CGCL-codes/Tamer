package query.plan;

import query.scan.*;
import metadata.*;

/**
 * The Plan class corresponding to the <i>product</i> relational algebra
 * operator.
 * 
 * @author NIL
 */
public class ProductPlan implements Plan {

    private Plan p1, p2;

    private Schema schema = new Schema();

    /**
	 * Creates a new product node in the query tree, having the two specified
	 * subqueries.
	 * 
	 * @param p1
	 *            the left-hand subquery
	 * @param p2
	 *            the right-hand subquery
	 */
    public ProductPlan(Plan p1, Plan p2) {
        this.p1 = p1;
        this.p2 = p2;
        schema.addAll(p1.getSchema());
        schema.addAll(p2.getSchema());
    }

    public Scan open(Scan sca) {
        Scan s1 = p1.open(null);
        Scan s2 = p2.open(null);
        return new ProductScan(s1, s2);
    }

    public int blocksAccessed() {
        return 0;
    }

    public int recordsOutput() {
        return 0;
    }

    public int distinctValues(String fldName) {
        return 0;
    }

    public Schema getSchema() {
        return schema;
    }
}

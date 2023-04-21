package com.jedox.etl.core.node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A {@link ColumnNode}, which acts as a coordinate proxy in a vector data format (e.g. for a cube, where each coordinate addresses one cube dimension)
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public class CoordinateNode extends ColumnNode {

    /**
	 * Type of aggregation, which should be performed on the values of this column with the help of the internal persistence 
	 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
	 *
	 */
    protected static enum Aggregations {

        MIN, AVG, COUNT, SUM, MAX, NONE
    }

    private Aggregations aggregate = Aggregations.NONE;

    private static final Log log = LogFactory.getLog(CoordinateNode.class);

    public CoordinateNode(String name) {
        super(name);
    }

    public ColumnTypes getColumnType() {
        return ColumnTypes.coordinate;
    }

    private String printAggregations() {
        StringBuffer output = new StringBuffer();
        for (Aggregations a : Aggregations.values()) {
            output.append(a.toString() + " ");
        }
        return output.toString();
    }

    /**
	 * sets an aggregate function indicator for this column. Since this column is a simple proxy, which can only handle one value at a time, the aggregation work has to be done externally by {@link com.jedox.etl.core.transform.ITransforms Transforms} using this column type 
	 * @param aggregate
	 */
    public void setAggregateFunction(String aggregate) {
        try {
            this.aggregate = Aggregations.valueOf(aggregate.toUpperCase());
            switch(this.aggregate) {
                case SUM:
                    setValueType("java.lang.Double");
                    break;
                case AVG:
                    setValueType("java.lang.Double");
                    break;
                case COUNT:
                    setValueType("java.lang.Double");
                    break;
                default:
            }
        } catch (IllegalArgumentException e) {
            log.warn("AggregateFunction has to be set to either " + printAggregations() + ". No aggregation is set.");
            this.aggregate = Aggregations.NONE;
        }
    }

    /**
	 * gets the aggregate function name, which should be used to (externally) aggregate the values produced by this object in a time series 
	 * @return the aggregate function name
	 */
    public String getAggregateFunction() {
        return aggregate.toString();
    }

    /**
	 * Determines if this Column has an aggregate function name set. 
	 * @return true, if so
	 */
    public boolean hasAggregateFunction() {
        return !aggregate.equals(Aggregations.NONE);
    }

    public void mimic(IColumn source) {
        super.mimic(source);
        if (source instanceof CoordinateNode) {
            CoordinateNode n = (CoordinateNode) source;
            this.aggregate = n.aggregate;
        }
    }
}

package uk.ac.manchester.cs.snee.compiler.queryplan.expressions;

import uk.ac.manchester.cs.snee.metadata.schema.AttributeType;
import uk.ac.manchester.cs.snee.metadata.schema.SchemaMetadataException;

/** 
 * Represent attributes that hold the site id data 
 * was acquired at.
 */
public class IDAttribute extends Attribute {

    /**
	 * Construct a IDAttribute instance
	 * 
	 * @param extentName name of the extent the attribute appears in
	 * @param attrName name of the attribute as it appears in the schema
	 * @param attrType type of the attribute
	 * @throws SchemaMetadataException
	 */
    public IDAttribute(String extentName, String attrName, AttributeType attrType) throws SchemaMetadataException {
        super(extentName, attrName, attrType);
    }

    /**
	 * Construct a IDAttribute instance
	 * 
	 * @param extentName name of the extent the attribute appears in
	 * @param attrName name of the attribute as it appears in the schema
	 * @param attrLabel display label for the attribute
	 * @param attrType type of the attribute
	 * @throws SchemaMetadataException
	 */
    public IDAttribute(String extentName, String attrName, String attrLabel, AttributeType attrType) throws SchemaMetadataException {
        super(extentName, attrName, attrLabel, attrType);
    }

    public IDAttribute(Attribute attribute) throws SchemaMetadataException {
        super(attribute);
    }

    /**
	 * Finds the minimum value that this expression can return.
	 * @return The minimum value for this expressions
	 * @throws AssertionError If Expression returns a boolean.
	 */
    public double getMinValue() {
        throw new AssertionError("Illegal call to getMinValue");
    }

    /**
	 * Finds the maximum value that this expression can return.
	 * @return The maximum value for this expressions
	 * @throws AssertionError If Expression returns a boolean.
	 */
    public double getMaxValue() {
        throw new AssertionError("Illegal call to getMaxValue");
    }

    /**
	 * Finds the expected selectivity of this expression can return.
	 * @return The expected selectivity
	 * @throws AssertionError If Expression does not return a boolean.
	 */
    public double getSelectivity() {
        throw new AssertionError("Illegal call to getSelectivity");
    }

    /**
	 * Checks if the Expression can be directly used in an Aggregation Operator.
	 * Expressions such as attributes that can only be used inside a aggregation expression return false.
	 * 
	 * @return false as this expression is only valid in an aggregation if wrapped in an aggregate.. 
	 */
    public boolean allowedInAggregationOperator() {
        return false;
    }

    /**
	 * Checks if the Expression can be used in a Project Operator.
	 * 
	 * @return true  
	 */
    public boolean allowedInProjectOperator() {
        return true;
    }

    /**
	 * Converts this Expression to an Attribute.
	 * 
	 * @return This Expression.
	 */
    public Attribute toAttribute() {
        return this;
    }
}

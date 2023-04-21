package gate.creole.ontology;

/**
 * A CardinalityRestriction
 * 
 * @author Niraj Aswani
 * @author Johann Petrak
 *
 */
public interface CardinalityRestriction extends Restriction {

    /**
     * This method returns the cardinality value.
     */
    public String getValue();

    /**
     * This method returns the datatype uri.
     */
    public DataType getDataType();

    /**
   * Sets the cardinality value.
   * @throws InvalidValueException
   */
    public void setValue(String value, DataType dataType) throws InvalidValueException;
}

package javax.print.attribute;

/**
 * Marker interface for all attribute classes specifying the 
 * supported/allowed values for another printing attribute class.
 * <p>
 * A {@link javax.print.PrintService} instance for example provides
 * printing attribute classes implementing this interface to indicate
 * that a specific attribute type is supported and if the supported values.
 * </p><p>
 * E.g. a {@link javax.print.attribute.standard.JobPrioritySupported}
 * instance indicates that the attribute class 
 * {@link javax.print.attribute.standard.JobPriority} is supported and
 * provides the number of the possible priority levels.
 * </p>
 * 
 * @author Michael Koch (konqueror@gmx.de)
 */
public interface SupportedValuesAttribute extends Attribute {
}

package uk.ac.imperial.ma.metric.exceptions;

/**
 * This abstract class should be used as a base for all metric 
 * specific exceptions.
 *
 * @author Daniel J. R. May
 * @version 0.2, 18 February 2004
 */
public abstract class MetricExceptionImpl extends Exception implements MetricException {

    /** The type of exception. This should be given by one of 
	the fields defined in <code>MetricException</code>. */
    protected short type;

    /** The severity of this exception. This should be equal to one of 
	the fields <code>MetricException.WARNING</code>, 
	<code>MetricException.ERROR</code> or 
	<code>MetricException.FATAL</code>. */
    protected short severity;

    /**
     * Constructor.
     *
     * @param message a message describing the way in which this 
     * exception has come about. 
     * @param cause a <code>Throwable</code> which was the cause 
     * of this exception. This can be <code>null</code>.
     * @param severity The severity of this exception. This 
     * should be equal to one of the fields <code>MetricException.WARNING</code>,
     * <code>MetricException.ERROR</code> or <code>MetricException.FATAL</code>.
     */
    protected MetricExceptionImpl(String message, Throwable cause, short severity, short type) {
        super(message, cause);
        this.type = type;
        this.severity = severity;
    }

    /**
     * Gets the type of the exception.
     *
     * @return this should be one of the fields defined in 
     * <code>MetricException</code>
     * @see uk.ac.imperial.ma.metric.exceptions.MetricException
     */
    public short getType() {
        return type;
    }

    /**
     * Gets the severity of the exception.
     *
     * @return This should be equal to one of the fields 
     * <code>MetricExcption.WARNING</code>, <code>MetricException.ERROR</code> 
     * or <code>MetricException.FATAL</code>.
     * @see uk.ac.imperial.ma.metric.exceptions.MetricException
     */
    public short getSeverity() {
        return severity;
    }

    /**
     * Gets the severity as a <code>String</code>.
     * 
     * @return one of <code>"Warning"</code>, <code>"Error"</code>, 
     * <code>"Fatal Error"</code>, <code>"Unknown Severity Error"</code>.
     */
    public String getSeverityAsString() {
        switch(severity) {
            case MetricException.WARNING:
                return "Warning";
            case MetricException.ERROR:
                return "Error";
            case MetricException.FATAL:
                return "Fatal Error";
            default:
                return "Unknown Severity Error";
        }
    }
}

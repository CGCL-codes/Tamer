package net.sf.csv2sql.writers.exceptions;

/**
 * This exception is throwed when one parameter of any subclass
 * of AbstractWriter is invalid
 * @see net.sf.csv2sql.writers.AbstractWriter AbstractWriter
 * @author <a href="mailto:dconsonni@enter.it">Davide Consonni</a>
 */
public class InvalidParameterValueException extends WriterException {

    private String parameterName;

    private String parameterValue;

    public InvalidParameterValueException(String parameterName, String parameterValue) {
        super("invalid parameter value.", null);
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public String toString() {
        return "InvalidParameterValueException, " + "parametername=" + getParameterName() + ", parametervalue=" + getParameterValue();
    }
}

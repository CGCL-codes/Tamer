package javax.print.attribute.standard;

import java.util.Locale;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.TextSyntax;

/**
 * The <code>JobMessageFromOperator</code> printing attribute provides 
 * a message from an operator or a system administrator related to the
 * print job. This may include information why a certain action has been
 * taken on the print job like a modification.
 * <p>
 * <b>IPP Compatibility:</b> JobMessageFromOperator is an IPP 1.1 
 * attribute.
 * </p>
 * 
 * @author Michael Koch (konqueror@gmx.de)
 */
public final class JobMessageFromOperator extends TextSyntax implements PrintJobAttribute {

    private static final long serialVersionUID = -4620751846003142047L;

    /**
   * Creates a <code>JobMessageFromOperator</code> object.
   *
   * @param message the message
   * @param locale the locale to use, if <code>null</code> the default
   * locale is used.
   *
   * @exception NullPointerException if message is <code>null</code>.
   */
    public JobMessageFromOperator(String message, Locale locale) {
        super(message, locale);
    }

    /**
   * Tests if the given object is equal to this object.
   *
   * @param obj the object to test
   *
   * @return <code>true</code> if both objects are equal, 
   * <code>false</code> otherwise.
   */
    public boolean equals(Object obj) {
        if (!(obj instanceof JobMessageFromOperator)) return false;
        return super.equals(obj);
    }

    /**
   * Returns category of this class.
   *
   * @return The class <code>JobMessageFromOperator</code> itself.
   */
    public Class getCategory() {
        return JobMessageFromOperator.class;
    }

    /**
   * Returns the name of this attribute.
   *
   * @return The name "job-message-from-operator".
   */
    public String getName() {
        return "job-message-from-operator";
    }
}

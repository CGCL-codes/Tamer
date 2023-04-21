package multex;

import java.lang.String;

/**Problem-related exception with parameters and internationalizable
  message text.
  This class serves for the framework user as a base class for defining
  problem-related exceptions. It can be used to throw directly, too, but such
  exceptions cannot be handled individually nor get internationalized.

  <H2>Naming convention:</H2>
  All user-defined exception classes derived from Exc should have a name
  ending in Exc. The pattern is: <PRE>
    class ErrorconditionExc extends Exc { ... }
  </PRE>

  <H2>Parameters</H2>
  All elements of the Object[] are
  considered positional parameters of this exception. The corresponding
  placeholders in the message text patterns have the syntax: {i}
  with i being the number of the parameter in the range 0..9.
  <P>
  The exception parameters can
  be substituted into the corresponding message text pattern in a desired locale
  format by class {@link MsgText}, which itself makes use of class
  java.text.MessageFormat.
  <br>
  See the usage examples Copy, AwtCopy, SwingCopy in directory demo.

  <H2>Relation to Standards</H2>
  Class java.text.MesageFormat does much more in direction of internationalization
  than just to insert parameters into a message text pattern.
  You can e.g. specify different styles of formatting a parameter.
  E.g. formatting a parameter of type java.util.Date at position 0
  could be done by the following parameter formatting directive:
  {0,date}.

  @author Christoph Knabe, TFH Berlin, 1999-2000 Copyrighted
*/
public class Exc extends java.lang.Exception implements MultexException {

    public static final String className = "Exc";

    /**Convenience constructor null
  @see #Exc(String, Object[])
*/
    public Exc(final String i_defaultMessageTextPattern) {
        this(i_defaultMessageTextPattern, null);
    }

    /**Convenience constructor 0
  @see #Exc(String, Object[])
*/
    public Exc(final String i_defaultMessageTextPattern, final Object i_object0) {
        this(i_defaultMessageTextPattern, new Object[] { i_object0 });
    }

    /**Convenience constructor 1
  @see #Exc(String, Object[])
*/
    public Exc(final String i_defaultMessageTextPattern, final Object i_object0, final Object i_object1) {
        this(i_defaultMessageTextPattern, new Object[] { i_object0, i_object1 });
    }

    /**Convenience constructor 2
  @see #Exc(String, Object[])
*/
    public Exc(final String i_defaultMessageTextPattern, final Object i_object0, final Object i_object1, final Object i_object2) {
        this(i_defaultMessageTextPattern, new Object[] { i_object0, i_object1, i_object2 });
    }

    /**Convenience constructor 3
  @see #Exc(String, Object[])
*/
    public Exc(final String i_defaultMessageTextPattern, final Object i_object0, final Object i_object1, final Object i_object2, final Object i_object3) {
        this(i_defaultMessageTextPattern, new Object[] { i_object0, i_object1, i_object2, i_object3 });
    }

    /**Convenience constructor 4
  @see #Exc(String, Object[])
*/
    public Exc(final String i_defaultMessageTextPattern, final Object i_object0, final Object i_object1, final Object i_object2, final Object i_object3, final Object i_object4) {
        this(i_defaultMessageTextPattern, new Object[] { i_object0, i_object1, i_object2, i_object3, i_object4 });
    }

    /**Convenience constructor 5
  @see #Exc(String, Object[])
*/
    public Exc(final String i_defaultMessageTextPattern, final Object i_object0, final Object i_object1, final Object i_object2, final Object i_object3, final Object i_object4, final Object i_object5) {
        this(i_defaultMessageTextPattern, new Object[] { i_object0, i_object1, i_object2, i_object3, i_object4, i_object5 });
    }

    /**Convenience constructor 6
  @see #Exc(String, Object[])
*/
    public Exc(final String i_defaultMessageTextPattern, final Object i_object0, final Object i_object1, final Object i_object2, final Object i_object3, final Object i_object4, final Object i_object5, final Object i_object6) {
        this(i_defaultMessageTextPattern, new Object[] { i_object0, i_object1, i_object2, i_object3, i_object4, i_object5, i_object6 });
    }

    /**Convenience constructor 7
  @see #Exc(String, Object[])
*/
    public Exc(final String i_defaultMessageTextPattern, final Object i_object0, final Object i_object1, final Object i_object2, final Object i_object3, final Object i_object4, final Object i_object5, final Object i_object6, final Object i_object7) {
        this(i_defaultMessageTextPattern, new Object[] { i_object0, i_object1, i_object2, i_object3, i_object4, i_object5, i_object6, i_object7 });
    }

    /**Convenience constructor 8
  @see #Exc(String, Object[])
*/
    public Exc(final String i_defaultMessageTextPattern, final Object i_object0, final Object i_object1, final Object i_object2, final Object i_object3, final Object i_object4, final Object i_object5, final Object i_object6, final Object i_object7, final Object i_object8) {
        this(i_defaultMessageTextPattern, new Object[] { i_object0, i_object1, i_object2, i_object3, i_object4, i_object5, i_object6, i_object7, i_object8 });
    }

    /**Convenience constructor 9
  @see #Exc(String, Object[])
*/
    public Exc(final String i_defaultMessageTextPattern, final Object i_object0, final Object i_object1, final Object i_object2, final Object i_object3, final Object i_object4, final Object i_object5, final Object i_object6, final Object i_object7, final Object i_object8, final Object i_object9) {
        this(i_defaultMessageTextPattern, new Object[] { i_object0, i_object1, i_object2, i_object3, i_object4, i_object5, i_object6, i_object7, i_object8, i_object9 });
    }

    /** Constructs an Exc with a default message text pattern
* and exception parameters as an polymorphic Object[].
* <P>Example of defining an exception with parameters: <PRE>
*  public static class StartedExc extends Exc {
*    public StartedExc(final String i_driver, final java.util.Date i_date){
*      super("Copy-driver {0}, version of {1,date} was started",
*        new Object[]{i_driver,i_date}
*      );
*    }
*  }
* </PRE>
  Instead of creating an Object[] yourself you can use the corresponding
  convenience constructors.
*/
    public Exc(final String i_defaultMessageTextPattern, final Object[] i_parameters) {
        _defaultMessageTextPattern = i_defaultMessageTextPattern;
        _parameters = i_parameters;
        checkClass();
    }

    public String getMessage() {
        return Exc.getUserInformation(this);
    }

    public Object[] getParameters() {
        return _parameters;
    }

    static String getUserInformation(final MultexException i_exception) {
        final String text = i_exception.getDefaultMessageTextPattern();
        final Object[] parameters = i_exception.getParameters();
        final boolean hasText = text != null;
        final boolean hasParameters = parameters != null;
        if (!hasText && !hasParameters) {
            return null;
        }
        final StringBuffer result = new StringBuffer();
        if (hasText) {
            result.append(text);
        }
        if (hasParameters && parameters.length != 0) {
            for (int i = 0; i < parameters.length; i++) {
                result.append(Failure.lineSeparator);
                result.append("    ");
                Exc.appendParam(result, Integer.toString(i), parameters[i]);
            }
        }
        return result.toString();
    }

    static void appendParam(final StringBuffer io_buf, final String i_name, final Object i_value) {
        io_buf.append("{");
        io_buf.append(i_name);
        io_buf.append("} = '");
        io_buf.append(i_value);
        io_buf.append("'");
    }

    public String getDefaultMessageTextPattern() {
        return _defaultMessageTextPattern;
    }

    /**Checks that the class of this object is OK.
  In the moment this means, that its name ends in {@link #className}
*/
    public void checkClass() {
        checkClass(this, className);
    }

    static void checkClass(final Exception i_exception, final String i_suffix) {
        final Class exceptionClass = i_exception.getClass();
        final String exceptionName = exceptionClass.getName();
        if (!exceptionName.endsWith(i_suffix)) {
            System.err.println("The name of subclass " + exceptionName + " of " + i_suffix + " should end in " + i_suffix + "!");
        }
    }

    private String _defaultMessageTextPattern;

    /**Unnamed parameters of this exception.*/
    private Object[] _parameters;
}

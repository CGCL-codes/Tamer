package gnu.classpath.examples.CORBA.SimpleCommunication.communication;

import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.IDLEntity;

/**
 * Our user exception, thrown in the tests of handling the exceptions,
 * thrown on remote side. The exception contains the user - defined
 * data field that is transferred from client to the server when the
 * exception is thrown.
 *
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public class WeThrowThisException extends UserException implements IDLEntity {

    /** 
   * Use serialVersionUID for interoperability. 
   */
    private static final long serialVersionUID = 1;

    /**
   * Our specific field, transferred to client.
   */
    public int ourField;

    /**
   * Create the exception.
   *
   * @param _ourField the value of our specific field.
   */
    public WeThrowThisException(int _ourField) {
        ourField = _ourField;
    }
}

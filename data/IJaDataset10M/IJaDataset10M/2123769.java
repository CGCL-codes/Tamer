package simple.util.lease;

/**
 * The <code>LeaseException</code> is used to indicate that some
 * operation failed when attempting to use a <code>Lease</code>.
 * Typically this will be thrown when the lease object is used
 * after the expiry period of the lease has passed.
 *
 * @author Niall Gallagher
 */
public class LeaseException extends Exception {

    /**
    * This empty constructor is used if there is no 
    * explanation of the leasing exception required.
    */
    public LeaseException() {
        super();
    }

    /**
    * This constructor is used if there is a description
    * of the event that caused the exception required.
    * 
    * @param desc this is a description of the exception
    */
    public LeaseException(String desc) {
        super(desc);
    }
}

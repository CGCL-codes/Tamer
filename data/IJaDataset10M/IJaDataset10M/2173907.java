package atnf.atoms.util;

/**
 * Indicates that the operation was called in the incorrect
 * order.
 *
 * @author
 *  David G Loone
 *
 * @version $Id: Ex_OutOfOrder.java,v 1.5 2000/03/15 05:45:03 dloone Exp $
 */
public final class Ex_OutOfOrder extends Error {

    /**
   * The RCS id.
   */
    public static final String RCSID = "$Id: Ex_OutOfOrder.java,v 1.5 2000/03/15 05:45:03 dloone Exp $";

    /**
   * Default constructor.
   *
   * Make an out of order exception.
   */
    public Ex_OutOfOrder() {
        super();
    }

    /**
   * Constructor.
   *
   * Make an out of order exception.
   *
   * @param description
   *  A string to describe the exception.
   */
    public Ex_OutOfOrder(String description) {
        super(description);
    }
}

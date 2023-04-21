package reconcile.weka.gui.beans;

import java.util.Enumeration;

/**
 * Interface to something that can accept requests from a user to perform
 * some action
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version $Revision: 1.1 $
 * @since 1.0
 */
public interface UserRequestAcceptor {

    /**
   * Get a list of performable requests
   *
   * @return an <code>Enumeration</code> value
   */
    Enumeration enumerateRequests();

    /**
   * Perform the named request
   *
   * @param requestName a <code>String</code> value
   * @exception IllegalArgumentException if an error occurs
   */
    void performRequest(String requestName);
}

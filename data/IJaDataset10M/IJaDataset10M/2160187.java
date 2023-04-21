package se.kth.cid.component.lookup;

import se.kth.cid.component.*;
import se.kth.cid.util.*;

/** This exception is thrown by the format loaders if anything goes wrong.
 *
 *  @author Mikael Nilsson
 *  @version $Revision: 155 $
 */
public class FormatException extends LookupException {

    /** Constructs a FormatException with the specified detail message and the given URI.
   *
   * @param message the detail message.
   * @param uri the URI which caused problems.
   */
    public FormatException(String message, URI uri) {
        super(message, uri);
    }
}

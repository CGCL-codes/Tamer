package net.grinder.tools.tcpproxy;

/**
 * Interface for passing comments, inserted by the user during a capture with
 * the TCPProxy, to the TCPProxyFilters.
 *
 * @author Venelin Mitov
 * @version $Revision: 1 $
 */
public interface CommentSource {

    /**
   * Get the comments added by the user since the previous call to getComments()
   * until now. The returned comments are excluded from the underlying
   * collection and will not be returned in subsequent calls.
   *
   * @return An array of all the comments inserted after the previous call to
   *         getComments() up to now. If no comments have been inserted an empty
   *         array is returned.
   */
    String[] getComments();
}

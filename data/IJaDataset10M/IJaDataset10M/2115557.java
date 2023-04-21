package org.aigebi.rbac;

/**Authorization reponse code.
 * @author Ligong Xu
 * @version $Id: AuthorResponseCode.java 1 2007-09-22 18:10:03Z ligongx $
 */
public interface AuthorResponseCode {

    /** authorization status unknown */
    public static final int UNSET = -1;

    /** authorization successful */
    public static final int AUTHORIZED = 0;

    /** authorization failure, no specific reason */
    public static final int UNAUTHORIZED = 1;
}

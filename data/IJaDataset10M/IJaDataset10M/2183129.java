package cu.ftpd.filesystem;

/**
 * @author Markus Jevring <markus@jevring.net>
 * @since 2007-jul-31 : 12:54:20
 * @version $Id: PermissionException.java 258 2008-10-26 12:47:23Z jevring $
 */
public class PermissionException extends Exception {

    public PermissionException(String message) {
        super("Permission denied: " + message);
    }
}

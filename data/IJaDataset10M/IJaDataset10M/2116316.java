package supersync.file;

/** This class is an abstract permissions class.
 *
 * @author Brandon Drake
 */
public abstract class AbstractPermissions {

    /** Gets the permissions of the group associated with an object.
     */
    public abstract AbstractSubPermissions getGroupPermissions();

    /** Gets the permissions of the current user.
     */
    public abstract AbstractSubPermissions getUserPermissions();

    /** Gets the permissions of everyone who does not have specific permissions for a certain object.
     */
    public abstract AbstractSubPermissions getWorldPermissions();

    /** Sets the permissions of the group associated with an object.
     */
    public abstract void setGroupPermissions(AbstractSubPermissions l_value);

    /** Sets the permissions of the current user.
     */
    public abstract void setUserPermissions(AbstractSubPermissions l_value);

    /** Sets the permissions of everyone who does not have specific permissions for a certain object.
     */
    public abstract void setWorldPermissions(AbstractSubPermissions l_value);
}

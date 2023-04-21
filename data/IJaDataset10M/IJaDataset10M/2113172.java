package iwork.seheap2.util;

/**
 *
 * @author  Administrator
 */
public class PermissionHolder {

    public String name;

    public boolean group;

    public long expiry;

    public boolean delegate;

    /** Creates a new instance of PermissionHolder */
    public PermissionHolder() {
    }

    public String toString() {
        return name;
    }
}

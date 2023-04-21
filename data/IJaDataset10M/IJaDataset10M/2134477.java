package boogleclient.entities;

import java.util.Set;

/**
 *
 * @author martin
 */
public class Admin extends User {

    private Permission usersPermissions;

    private Permission publicationPermissions;

    private Permission warehousePermissions;

    private Permission commentPermissions;

    public Admin() {
    }

    public Admin(com.boogle.webservices.User user) {
        this.user = user;
    }

    /**
     * @return the usersPermissions
     */
    @Override
    public Permission getUsersPermissions() {
        return new Permission(true, true, true, true);
    }

    /**
     * @return the publicationPermissions
     */
    @Override
    public Permission getPublicationPermissions() {
        return new Permission(true, true, true, true);
    }

    /**
     * @return the warehousePermissions
     */
    @Override
    public Permission getWarehousePermissions() {
        return new Permission(true, true, true, true);
    }

    /**
     * @return the commentPermissions
     */
    @Override
    public Permission getCommentPermissions() {
        return new Permission(true, true, true, true);
    }
}

package com.threerings.jpkg;

/**
 * Simple data class to contain permissions for paths used in a PermissionsMap.
 */
public class PathPermissions {

    /**
     * Create a new permissions map which will be owned by the default user/group, e.g. root but
     * have the supplied file mode set.
     * @param mode the file permission mode, in represented octal, e.g. 0644.
     * @param recursive whether to apply this permission recursively down the path.
     */
    public PathPermissions(int mode, boolean recursive) {
        this(UnixStandardPermissions.ROOT_USER.getName(), UnixStandardPermissions.ROOT_GROUP.getName(), mode, recursive);
    }

    /**
     * Create a new permissions map with the given user and group owner.
     * @param mode the file permission mode, in represented octal, e.g. 0644.
     * @param recursive whether to apply this permission recursively down the path.
     */
    public PathPermissions(String user, String group, int mode, boolean recursive) {
        _user = user;
        _group = group;
        _uid = UnixStandardPermissions.ROOT_USER.getId();
        _gid = UnixStandardPermissions.ROOT_GROUP.getId();
        _mode = mode;
        _recursive = recursive;
    }

    /**
     * Create a new permissions map with the given uid and gid owner.
     * @param mode the file permission mode, represented in octal, e.g. 0644.
     * @param recursive whether to apply this permission recursively down the path.
     */
    public PathPermissions(int uid, int gid, int mode, boolean recursive) {
        _user = UnixStandardPermissions.ROOT_USER.getName();
        _group = UnixStandardPermissions.ROOT_GROUP.getName();
        _uid = uid;
        _gid = gid;
        _mode = mode;
        _recursive = recursive;
    }

    /**
     * The username to set as owner for this path.
     */
    public String getUser() {
        return _user;
    }

    /**
     * The groupname to set as owner for this path.
     */
    public String getGroup() {
        return _group;
    }

    /**
     * The user id to set as owner for this path.
     */
    public int getUid() {
        return _uid;
    }

    /**
     * The group id to set as owner for this path.
     */
    public int getGid() {
        return _gid;
    }

    /**
     * The file permission mode..
     */
    public int getMode() {
        return _mode;
    }

    /**
     * Whether this permission map applies recursively down the path.
     */
    public boolean isRecursive() {
        return _recursive;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("user=[").append(_user).append("], ");
        builder.append("group=[").append(_group).append("], ");
        builder.append("userId=[").append(_uid).append("], ");
        builder.append("groupId=[").append(_gid).append("], ");
        builder.append("mode=[").append(Integer.toOctalString(_mode)).append("], ");
        builder.append("recursive=[").append(_recursive).append("].");
        return builder.toString();
    }

    private final String _user;

    private final String _group;

    private final int _uid;

    private final int _gid;

    private final int _mode;

    private final boolean _recursive;
}

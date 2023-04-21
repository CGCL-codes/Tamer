package tabs;

/**
 *
 * @author peterf
 */
public class ACL {

    private String user;

    private String group;

    private String permission;

    public String GetACL() {
        String acl = "";
        if (user.isEmpty()) {
            acl = "+" + group + ":" + permission;
        } else if (group.isEmpty()) {
            acl = user + ":" + permission;
        } else acl = "ERROR: Both user and group exist!";
        return acl;
    }

    public void SetACL(String usr, String grp, String perm) {
        user = usr;
        group = grp;
        permission = perm;
    }
}

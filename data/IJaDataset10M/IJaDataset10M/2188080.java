package cn.vlabs.simpleAuth;

public class GroupPrincipal extends Principal {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8176432170586142064L;

    public GroupPrincipal(String groupName, String groupId) {
        super(groupName, groupId);
    }

    public void setGroupName(String groupName) {
        setName(groupName);
    }

    public String getGroupName() {
        return getName();
    }

    public int getGroupID() {
        return Integer.parseInt(super.getId());
    }
}

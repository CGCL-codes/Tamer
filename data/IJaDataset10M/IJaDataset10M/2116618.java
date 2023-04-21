package com.entelience.probe.mim.user;

public class UserAccount {

    private final AuthSystem as;

    private final int userBaseId;

    private final String userName;

    protected UserAccount(AuthSystem as, int userBaseId, String userName) {
        this.as = as;
        this.userBaseId = userBaseId;
        this.userName = userName;
    }

    public AuthSystem getAuthSystem() {
        return as;
    }

    public int getUserBaseId() {
        return userBaseId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUniqKey() {
        return as.getUniq() + "#" + userName;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        sb.append(" as=").append(as.toString());
        sb.append(" userBaseId=").append(userBaseId);
        sb.append(" userName=[").append(userName).append(']');
        return sb.toString();
    }
}

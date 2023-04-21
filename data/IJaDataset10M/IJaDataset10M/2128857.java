package org.foxtalkz;

public interface GroupChangeListener {

    public static final int GROUPMEMBER_CHANGED = 1;

    public void onGroupChange(Group group, int status);

    public void onDestroy(Group group);
}

package iwork.manager.launcher;

import java.util.List;

/**
 * Container for group information.
 * 
 * @author Ulf Ochsenfahrt (ulfjack@stanford.edu)
 */
class GroupEntry implements Cloneable {

    public String groupName;

    public List groupList;

    public GroupEntry(String groupName, List groupList) {
        this.groupName = groupName;
        this.groupList = groupList;
    }
}

package Protocol.contact;

import java.io.Serializable;

public class DeleteGroup_R implements Serializable {

    int userID;

    int groupID;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
}

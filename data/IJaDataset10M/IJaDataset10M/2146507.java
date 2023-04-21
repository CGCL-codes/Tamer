package gpsxml.gui.model;

import gpsxml.xml.User;
import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author PLAYER, Keith Ralph
 */
public class UserListDataModel extends AbstractListModel {

    ArrayList<User> userList;

    /** Creates a new instance of UserListDataModel */
    public UserListDataModel(ArrayList<User> userList) {
        this.userList = userList;
    }

    public Object getElementAt(int index) {
        return userList.get(index).getId();
    }

    public void remove(int index) {
        userList.remove(index);
        this.fireIntervalRemoved(this, index, index);
    }

    public void fireContentsAppended() {
        this.fireContentsChanged(this, getSize(), getSize());
    }

    public int getSize() {
        return userList.size();
    }
}

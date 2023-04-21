package fr.albin.jmessagesend.ihm;

import java.util.List;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fr.albin.jmessagesend.user.UserGroup;
import fr.albin.jmessagesend.user.UserGroups;
import fr.albin.jmessagesend.utils.ListUtils;

/**
 * Gives a graphical representation to the UserGroups class.
 * It is compounded of tabs. Each tab representing a group with
 * a JList for its toUsers.
 * @author avigier
 *
 */
public class UserGroupsTabbedPane extends JTabbedPane {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public UserGroupsTabbedPane(UserGroups userGroups) {
        super();
        this.userGroups = userGroups;
        this.createTabs();
    }

    private void createTabs() {
        UserGroup userGroup = null;
        JList jlist = null;
        LOGGER.debug("Creating the groups tabs");
        for (int i = 0; i < this.userGroups.getList().size(); i++) {
            userGroup = this.userGroups.get(i);
            jlist = new JList(userGroup.getSortedList().toArray());
            this.addTab(userGroup.getLabel(), jlist);
            LOGGER.debug("Tab " + userGroup.getLabel() + " successfuly created.");
        }
    }

    public List getSelectedUsersList() {
        List vector = new Vector();
        JList jlist = (JList) this.getSelectedComponent();
        if (jlist != null) {
            vector = ListUtils.arrayToList(jlist.getSelectedValues());
        }
        return vector;
    }

    private UserGroups userGroups;

    private static final Log LOGGER = LogFactory.getLog(UserGroupsTabbedPane.class);
}

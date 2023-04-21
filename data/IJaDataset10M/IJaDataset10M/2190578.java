package htmoo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class UserListPanel extends JPanel {

    private DefaultListModel listModel;

    private JList usersList;

    private JScrollPane userScroller;

    private Font myFont = new Font("Courier", Font.PLAIN, 12);

    private JLabel lblUsers;

    private MooUserCellRenderer cellRenderer = new MooUserCellRenderer();

    public UserListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        listModel = new DefaultListModel();
        usersList = new JList(listModel);
        userScroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        userScroller.getViewport().setView(usersList);
        usersList.setFont(myFont);
        usersList.setCellRenderer(cellRenderer);
        lblUsers = new JLabel("Users:");
        lblUsers.setFont(myFont);
        userScroller.getViewport().setPreferredSize(new Dimension(120, 300));
        add(lblUsers);
        add(userScroller);
    }

    public void addOnlineUser(MooUser u) {
        MooUser found = findUserById(u.getId());
        if (found == null) {
            listModel.addElement(u);
            System.out.println("Userlist, added " + u.getName());
        } else {
            found.copyData(u);
            System.out.println("Userlist, updated " + u.getName());
        }
        this.getParent().repaint();
    }

    public void setIdle(String id, boolean idle) {
        MooUser u = findUserById(id);
        if (u != null) {
            System.out.println("Setting " + u.getName() + ".idle to " + idle);
            u.setIdle(idle);
            this.getParent().repaint();
        }
    }

    private MooUser findUserById(String id) {
        MooUser retval = null;
        for (int i = 0; i < listModel.size(); i++) {
            MooUser u = (MooUser) listModel.get(i);
            if (u.getId().equals(id)) {
                retval = u;
                break;
            }
        }
        return retval;
    }
}

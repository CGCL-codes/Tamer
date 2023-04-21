package org.thole.phiirc.client.view.swing;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.ToolTipManager;
import org.thole.phiirc.client.controller.Controller;
import org.thole.phiirc.client.model.Channel;
import org.thole.phiirc.client.model.User;
import org.thole.phiirc.client.model.UserChannelPermission;
import org.thole.phiirc.client.view.interfaces.IUserList;
import org.thole.phiirc.client.view.swing.actions.UserQueryListener;

public class UserList extends JList implements IUserList {

    private static final long serialVersionUID = -3801706781635012308L;

    public UserList() {
        super();
        this.addMouseListener(new UserQueryListener());
        ToolTipManager.sharedInstance().setInitialDelay(0);
    }

    public void setUserListData(final ArrayList<String> userList) {
        setUserListData(new Vector<String>(userList));
    }

    @Override
    public void setUserListData(final Vector<String> userList) {
        this.setListData(userList);
    }

    @Override
    public String getToolTipText(final MouseEvent evt) {
        final int index = locationToIndex(evt.getPoint());
        Object item;
        if (index >= 0) {
            item = getModel().getElementAt(index);
        } else {
            item = null;
        }
        return createUserTooltip(item);
    }

    @Override
    public Point getToolTipLocation(final MouseEvent event) {
        return new Point(getWidth() * -1, getHeight() - 100);
    }

    /**
	 * Create a tooltip having some user information
	 * @param item
	 * @return tooltip 
	 */
    private String createUserTooltip(final Object item) {
        String tooltip = "";
        final String currTarg = Controller.getInstance().getConnector().getServer().getCurrentTarget();
        if (currTarg.charAt(0) == '#') {
            final User user = Controller.getInstance().getUWatcher().getUser(item.toString());
            final Channel chan = Controller.getInstance().getCWatcher().getChan(currTarg);
            UserChannelPermission perm = user.getChannels().get(chan);
            String chanrole = "";
            switch(perm) {
                case OPERATOR:
                    chanrole = "Operator";
                    break;
                case VOICE:
                    chanrole = "Voiced";
                    break;
                default:
                    chanrole = "";
                    break;
            }
            tooltip = "<HTML>Nick: " + user.getNick() + "<br>" + "Name: " + user.getName() + "<br>" + "Host: " + user.getHost() + "<br>" + "Realname: " + user.getRealname() + "<br>" + "Channel Role: " + chanrole + "</HTML>";
        }
        return tooltip;
    }
}

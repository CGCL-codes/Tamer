package org.actioncenters.listeners.contributionsservice;

import org.actioncenters.core.constants.Channels;
import org.actioncenters.core.contribution.data.ISystemRole;
import org.actioncenters.core.contribution.data.IUser;
import org.actioncenters.core.contribution.svc.notification.listeners.IAddUserRoleListener;

/**
 * @author dkjeldgaard
 */
public class AddUserRoleListener extends AbstractListener implements IAddUserRoleListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void userRoleAdded(IUser user) {
        if (user != null && user.getSystemRoles() != null) {
            for (ISystemRole systemRole : user.getSystemRoles()) {
                String channel = Channels.getAddSystemUserChannel(systemRole);
                publish(user, channel);
            }
        }
    }
}

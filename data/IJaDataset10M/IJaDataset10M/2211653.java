package org.ourgrid.common.statistics.beans.pair;

import org.ourgrid.common.statistics.beans.aggregator.AG_Login;
import org.ourgrid.common.statistics.beans.aggregator.AG_Peer;
import org.ourgrid.common.statistics.beans.aggregator.AG_User;
import org.ourgrid.common.statistics.beans.peer.Peer;
import org.ourgrid.common.statistics.beans.peer.User;
import org.ourgrid.peer.status.util.Toolkit;

public class UserPair implements AGPair {

    private final User user;

    private final AG_User userAg;

    public UserPair(User user, AG_User userAg) {
        this.user = user;
        this.userAg = userAg;
    }

    public void addAGChildren(Object children) {
        userAg.getLogins().add((AG_Login) children);
    }

    public PeerPair createParentPair() {
        return new PeerPair(getParent(), Toolkit.convertPeer(getParent()));
    }

    public AG_User getAGObject() {
        return userAg;
    }

    public User getObject() {
        return user;
    }

    public Peer getParent() {
        return user.getPeer();
    }

    public void setAGParent(Object parent) {
        userAg.setPeer((AG_Peer) parent);
    }
}

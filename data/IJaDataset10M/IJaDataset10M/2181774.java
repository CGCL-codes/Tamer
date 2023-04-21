package kz.simplex.photobox.action;

import kz.simplex.photobox.model.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("friendshipHome")
public class FriendshipHome extends EntityHome<Friendship> {

    @In(create = true)
    UserHome userHome;

    public void setFriendshipPK(FriendshipPK id) {
        setId(id);
    }

    public FriendshipPK getFriendshipPK() {
        return (FriendshipPK) getId();
    }

    public FriendshipHome() {
        setFriendshipPK(new FriendshipPK());
    }

    @Override
    public boolean isIdDefined() {
        if (getFriendshipPK().getUser1() == null) return false;
        if (getFriendshipPK().getUser2() == null) return false;
        return true;
    }

    @Override
    protected Friendship createInstance() {
        Friendship friendship = new Friendship();
        friendship.setId(new FriendshipPK());
        return friendship;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
        User user1 = userHome.getDefinedInstance();
        if (user1 != null) {
            getInstance().setUser1(user1);
        }
        User user2 = userHome.getDefinedInstance();
        if (user2 != null) {
            getInstance().setUser2(user2);
        }
    }

    public boolean isWired() {
        return true;
    }

    public Friendship getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }
}

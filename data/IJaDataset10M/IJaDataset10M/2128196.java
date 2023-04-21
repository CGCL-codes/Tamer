package org.albertsanso.web20.wines.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import org.albertsanso.web20.core.model.User;
import org.albertsanso.web20.core.model.UserToItemBaseRelation;

@Entity
public class RecommendedItemList extends UserToItemBaseRelation {

    private User targetUser;

    @OneToOne
    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }
}

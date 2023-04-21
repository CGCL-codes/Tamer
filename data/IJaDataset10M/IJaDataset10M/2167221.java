package furniture.model;

import javax.persistence.Entity;
import furniture.core.model.IdEntity;

@Entity
public class Permission extends IdEntity {

    private String name;

    private DummyUser user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DummyUser getUser() {
        return user;
    }

    public void setUser(DummyUser user) {
        this.user = user;
    }
}

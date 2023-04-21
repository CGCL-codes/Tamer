package eu.billewicz.notiary.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NkFriendStub {

    private Integer id;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

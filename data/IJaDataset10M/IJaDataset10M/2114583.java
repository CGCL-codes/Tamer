package eu.billewicz.notiary.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NkFriend {

    private Integer id;

    public NkFriend() {
    }

    public NkFriend(Integer id) {
        this.id = id;
    }

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

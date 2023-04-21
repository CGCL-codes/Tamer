package fi.foyt.cs.persistence.domainmodel.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.google.appengine.api.datastore.Key;

@Entity
public class TaskRewards {

    public Key getId() {
        return id;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key id;

    @Column(nullable = false)
    private Double money;
}

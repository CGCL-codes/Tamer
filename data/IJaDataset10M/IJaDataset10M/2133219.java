package idv.nightpig.lab03.one2many;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LabUser {

    private String account;

    private LabGroup group;

    private Long id;

    public String getAccount() {
        return account;
    }

    @ManyToOne
    public LabGroup getGroup() {
        return group;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setGroup(LabGroup group) {
        this.group = group;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

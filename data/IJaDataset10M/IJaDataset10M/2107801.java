package net.persister.example.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Park, chanwook
 *
 */
@Entity
public class Item {

    @Id
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;

    @Temporal(TemporalType.TIME)
    private Date used;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public Date getUsed() {
        return used;
    }

    public void setUsed(Date used) {
        this.used = used;
    }
}

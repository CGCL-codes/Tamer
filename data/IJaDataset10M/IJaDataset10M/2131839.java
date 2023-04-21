package com.luzan.bean;

import org.hibernate.annotations.Proxy;
import javax.persistence.*;
import java.util.Date;
import java.util.Calendar;

/**
 * Mail
 *
 * @author Alexander Bondar
 */
@Entity
@Proxy(lazy = false)
@Table(name = "mailposts")
public class MailPost implements ImplLiving {

    private int id;

    private Date modified;

    private Date created;

    public MailPost() {
        modified = Calendar.getInstance().getTime();
        created = Calendar.getInstance().getTime();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "modified")
    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}

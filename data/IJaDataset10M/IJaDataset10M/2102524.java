package org.lopatka.idonc.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Entity
@Table(name = "ADMINS", uniqueConstraints = @UniqueConstraint(columnNames = { "USER" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IdoncAdmin {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER")
    @LazyToOne(LazyToOneOption.FALSE)
    private IdoncUser user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdoncUser getUser() {
        return user;
    }

    public void setUser(IdoncUser user) {
        this.user = user;
    }
}

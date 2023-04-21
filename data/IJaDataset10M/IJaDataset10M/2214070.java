package org.boticelli.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@NamedQueries(value = { @NamedQuery(name = "FaqEntry.findByEntry", query = "select f from FaqEntry f where lower(f.entry) = lower(:entry) order by f.id") })
@Entity
@Table(name = "faq")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class FaqEntry implements Serializable {

    private static final long serialVersionUID = 7151075108925283814L;

    private Long id;

    private String entry;

    private String text;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Column(length = 50, unique = false)
    public String getEntry() {
        return entry;
    }

    @Column()
    public String getText() {
        return text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setText(String text) {
        this.text = text;
    }
}

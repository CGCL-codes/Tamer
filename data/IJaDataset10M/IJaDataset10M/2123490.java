package org.academ.jabber.entities.helps;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author moskvin
 */
@Entity
@Table(name = "helps_steps")
@NamedQueries({ @NamedQuery(name = "HelpsSteps.findAll", query = "SELECT h FROM HelpsSteps h"), @NamedQuery(name = "HelpsSteps.findById", query = "SELECT h FROM HelpsSteps h WHERE h.id = :id"), @NamedQuery(name = "HelpsSteps.findBySubject", query = "SELECT h FROM HelpsSteps h WHERE h.subject = :subject"), @NamedQuery(name = "HelpsSteps.findByLanguage", query = "SELECT h FROM HelpsSteps h WHERE h.language = :language"), @NamedQuery(name = "HelpsSteps.findByImage", query = "SELECT h FROM HelpsSteps h WHERE h.image = :image") })
public class HelpsSteps implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "subject")
    private String subject;

    @Column(name = "language")
    private String language;

    @Column(name = "image")
    private String image;

    @JoinColumn(name = "help_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Helps helpId;

    public HelpsSteps() {
    }

    public HelpsSteps(Integer id) {
        this.id = id;
    }

    public HelpsSteps(Integer id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Helps getHelpId() {
        return helpId;
    }

    public void setHelpId(Helps helpId) {
        this.helpId = helpId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HelpsSteps)) {
            return false;
        }
        HelpsSteps other = (HelpsSteps) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.academ.jabber.entities.helps.HelpsSteps[id=" + id + "]";
    }
}

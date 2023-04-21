package de.bwb.ekp.entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.annotations.AccessType;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Untergruppe
 * 
 * @copyright akquinet AG, 2007
 */
@Entity
@Table(name = "UNTERGRUPPE")
@SequenceGenerator(name = "untergruppe_seq", sequenceName = "UNTERGRUPPE_ID_SEQ", initialValue = 50)
public class Untergruppe implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private int version;

    private Hauptgruppe hauptgruppe;

    private String untergruppeName;

    private Set<Bedarfsspektrum> bedarfsspektrums = new HashSet<Bedarfsspektrum>(0);

    @Id
    @Column(name = "UNTERGRUPPE_ID", unique = true, nullable = false)
    @NotNull
    @AccessType(value = "field")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "untergruppe_seq")
    public int getId() {
        return this.id;
    }

    @Column(name = "VERSION")
    @Version
    @AccessType(value = "field")
    public int getVersion() {
        return this.version;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HAUPTGRUPPE_ID")
    public Hauptgruppe getHauptgruppe() {
        return this.hauptgruppe;
    }

    public void setHauptgruppe(final Hauptgruppe hauptgruppe) {
        this.hauptgruppe = hauptgruppe;
    }

    @Column(name = "UNTERGRUPPE_NAME", length = 60)
    @Length(max = 60)
    public String getUntergruppeName() {
        return this.untergruppeName;
    }

    public void setUntergruppeName(final String untergruppeName) {
        this.untergruppeName = untergruppeName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "untergruppe")
    public Set<Bedarfsspektrum> getBedarfsspektrums() {
        return this.bedarfsspektrums;
    }

    public void setBedarfsspektrums(final Set<Bedarfsspektrum> bedarfsspektrums) {
        this.bedarfsspektrums = bedarfsspektrums;
    }

    @Override
    public String toString() {
        return this.id + " " + this.untergruppeName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Untergruppe)) return false;
        final Untergruppe other = (Untergruppe) obj;
        if (this.getId() != other.getId()) {
            return false;
        }
        return true;
    }
}

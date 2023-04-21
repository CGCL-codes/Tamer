package de.bwb.ekp.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.annotations.AccessType;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Firmennamen
 * 
 * @copyright akquinet AG, 2007
 */
@Entity
@Table(name = "FIRMENNAMEN")
@SequenceGenerator(name = "firmennamen_seq", sequenceName = "FIRMANAMEN_ID_SEQ", initialValue = 50)
public class Firmennamen implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private int version;

    private Bewerbung bewerbung;

    private String firmaName;

    private String steuernummer;

    @Id
    @Column(name = "NAMEN_ID", unique = true, nullable = false)
    @NotNull
    @AccessType(value = "field")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "firmennamen_seq")
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
    @JoinColumn(name = "BEWERBUNG_ID", nullable = false)
    @NotNull
    public Bewerbung getBewerbung() {
        return this.bewerbung;
    }

    public void setBewerbung(final Bewerbung bewerbung) {
        this.bewerbung = bewerbung;
    }

    @Column(name = "FIRMA_NAME", nullable = false, length = 90)
    @NotNull
    @Length(max = 90)
    public String getFirmaName() {
        return this.firmaName;
    }

    public void setFirmaName(final String firmaName) {
        this.firmaName = firmaName;
    }

    @Column(name = "STEUERNUMMER", length = 30)
    @Length(max = 30)
    public String getSteuernummer() {
        return this.steuernummer;
    }

    public void setSteuernummer(final String steuernummer) {
        this.steuernummer = steuernummer;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.bewerbung == null) ? 0 : this.bewerbung.hashCode());
        result = prime * result + ((this.firmaName == null) ? 0 : this.firmaName.hashCode());
        result = prime * result + ((this.steuernummer == null) ? 0 : this.steuernummer.hashCode());
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
        if (!(obj instanceof Firmennamen)) return false;
        final Firmennamen other = (Firmennamen) obj;
        if (this.getBewerbung() == null) {
            if (other.getBewerbung() != null) {
                return false;
            }
        } else if (!this.getBewerbung().equals(other.getBewerbung())) {
            return false;
        }
        if (this.getFirmaName() == null) {
            if (other.getFirmaName() != null) {
                return false;
            }
        } else if (!this.getFirmaName().equals(other.getFirmaName())) {
            return false;
        }
        if (this.getSteuernummer() == null) {
            if (other.getSteuernummer() != null) {
                return false;
            }
        } else if (!this.getSteuernummer().equals(other.getSteuernummer())) {
            return false;
        }
        return true;
    }
}

package org.sltech.punchclock.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *
 * @author Juanjo
 */
@Entity
@Table(name = "clk_person_picture")
@NamedQueries({ @NamedQuery(name = "ClkPersonPicture.findAll", query = "SELECT c FROM ClkPersonPicture c"), @NamedQuery(name = "ClkPersonPicture.findByIdPerson", query = "SELECT c FROM ClkPersonPicture c WHERE c.idPerson = :idPerson"), @NamedQuery(name = "ClkPersonPicture.findByRecordVersion", query = "SELECT c FROM ClkPersonPicture c WHERE c.recordVersion = :recordVersion") })
public class ClkPersonPicture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_person", nullable = false)
    private Integer idPerson;

    @Basic(optional = false)
    @Lob
    @Column(name = "picture", nullable = false)
    private byte[] picture;

    @Version
    @Basic(optional = false)
    @Column(name = "record_version", nullable = false)
    private Timestamp recordVersion;

    @JoinColumn(name = "id_person", referencedColumnName = "id_person", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private ClkPerson clkPerson;

    public ClkPersonPicture() {
    }

    public ClkPersonPicture(Integer idPerson) {
        this.idPerson = idPerson;
    }

    public ClkPersonPicture(Integer idPerson, byte[] picture) {
        this.idPerson = idPerson;
        this.picture = picture;
    }

    public Integer getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Integer idPerson) {
        this.idPerson = idPerson;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Timestamp getRecordVersion() {
        return recordVersion;
    }

    public void setRecordVersion(Timestamp recordVersion) {
        this.recordVersion = recordVersion;
    }

    public ClkPerson getClkPerson() {
        return clkPerson;
    }

    public void setClkPerson(ClkPerson clkPerson) {
        this.clkPerson = clkPerson;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerson != null ? idPerson.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ClkPersonPicture)) {
            return false;
        }
        ClkPersonPicture other = (ClkPersonPicture) object;
        if ((this.idPerson == null && other.idPerson != null) || (this.idPerson != null && !this.idPerson.equals(other.idPerson))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sltech.punchclock.jpa.ClkPersonPicture[idPerson=" + idPerson + "]";
    }
}

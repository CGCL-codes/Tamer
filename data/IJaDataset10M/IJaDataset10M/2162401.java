package org.verus.ngl.sl.objectmodel.administration;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author edukondalu
 */
@Embeddable
public class GENERAL_SETUP_PMTPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "library_id")
    private int libraryId;

    @Basic(optional = false)
    @Column(name = "wef")
    @Temporal(TemporalType.TIMESTAMP)
    private Date wef;

    public GENERAL_SETUP_PMTPK() {
    }

    public GENERAL_SETUP_PMTPK(int libraryId, Date wef) {
        this.libraryId = libraryId;
        this.wef = wef;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public Date getWef() {
        return wef;
    }

    public void setWef(Date wef) {
        this.wef = wef;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) libraryId;
        hash += (wef != null ? wef.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GENERAL_SETUP_PMTPK)) {
            return false;
        }
        GENERAL_SETUP_PMTPK other = (GENERAL_SETUP_PMTPK) object;
        if (this.libraryId != other.libraryId) {
            return false;
        }
        if ((this.wef == null && other.wef != null) || (this.wef != null && !this.wef.equals(other.wef))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.verus.ngl.sl.objectmodel.administration.GENERAL_SETUP_PMTPK[libraryId=" + libraryId + ", wef=" + wef + "]";
    }
}

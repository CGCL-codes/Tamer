package org.punchclock.station.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *
 * @author Juanjo
 */
@Entity
@Table(name = "cls_location", catalog = "clockstation", schema = "")
@NamedQueries({ @NamedQuery(name = "ClsLocation.findAll", query = "SELECT c FROM ClsLocation c"), @NamedQuery(name = "ClsLocation.findByIdLocation", query = "SELECT c FROM ClsLocation c WHERE c.idLocation = :idLocation"), @NamedQuery(name = "ClsLocation.findByLocationName", query = "SELECT c FROM ClsLocation c WHERE c.locationName = :locationName"), @NamedQuery(name = "ClsLocation.findByLocationPassword", query = "SELECT c FROM ClsLocation c WHERE c.locationPassword = :locationPassword"), @NamedQuery(name = "ClsLocation.findByActive", query = "SELECT c FROM ClsLocation c WHERE c.active = :active"), @NamedQuery(name = "ClsLocation.findByLastIdPunch", query = "SELECT c FROM ClsLocation c WHERE c.lastIdPunch = :lastIdPunch"), @NamedQuery(name = "ClsLocation.findBySelected", query = "SELECT c FROM ClsLocation c WHERE c.selected = :selected"), @NamedQuery(name = "ClsLocation.findByRecordVersion", query = "SELECT c FROM ClsLocation c WHERE c.recordVersion = :recordVersion") })
public class ClsLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id_location", nullable = false)
    private Integer idLocation;

    @Column(name = "location_name", length = 20)
    private String locationName;

    @Column(name = "location_password", length = 45)
    private String locationPassword;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "last_id_punch", length = 45)
    private String lastIdPunch;

    @Basic(optional = false)
    @Column(name = "selected", nullable = false)
    private boolean selected;

    @Version
    @Basic(optional = false)
    @Column(name = "record_version", nullable = false)
    private Timestamp recordVersion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clsLocation")
    private List<ClsPunch> clsPunchList;

    public ClsLocation() {
    }

    public ClsLocation(Integer idLocation) {
        this.idLocation = idLocation;
    }

    public ClsLocation(Integer idLocation, boolean selected) {
        this.idLocation = idLocation;
        this.selected = selected;
    }

    public Integer getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(Integer idLocation) {
        this.idLocation = idLocation;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationPassword() {
        return locationPassword;
    }

    public void setLocationPassword(String locationPassword) {
        this.locationPassword = locationPassword;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getLastIdPunch() {
        return lastIdPunch;
    }

    public void setLastIdPunch(String lastIdPunch) {
        this.lastIdPunch = lastIdPunch;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Timestamp getRecordVersion() {
        return recordVersion;
    }

    public void setRecordVersion(Timestamp recordVersion) {
        this.recordVersion = recordVersion;
    }

    public List<ClsPunch> getClsPunchList() {
        return clsPunchList;
    }

    public void setClsPunchList(List<ClsPunch> clsPunchList) {
        this.clsPunchList = clsPunchList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLocation != null ? idLocation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ClsLocation)) {
            return false;
        }
        ClsLocation other = (ClsLocation) object;
        if ((this.idLocation == null && other.idLocation != null) || (this.idLocation != null && !this.idLocation.equals(other.idLocation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.punchclock.station.entities.ClsLocation[idLocation=" + idLocation + "]";
    }
}

package gov.nist.mel.emergency.cap;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author Guillaume Radde <guillaume.radde@nist.gov>
 */
@Entity
@Table(name = "response_type")
@NamedQueries({ @NamedQuery(name = "ResponseType.findByResponseTypeId", query = "SELECT r FROM ResponseType r WHERE r.responseTypeId = :responseTypeId"), @NamedQuery(name = "ResponseType.findByResponseType", query = "SELECT r FROM ResponseType r WHERE r.responseType = :responseType") })
public class ResponseType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_type_id", nullable = false)
    private Integer responseTypeId;

    @Column(name = "response_type", nullable = false)
    private String responseType = "None";

    @JoinColumn(name = "info_id", referencedColumnName = "info_id")
    @ManyToOne
    private Info infoId;

    public ResponseType() {
    }

    public ResponseType(Integer responseTypeId) {
        this.responseTypeId = responseTypeId;
    }

    public ResponseType(Integer responseTypeId, String responseType) {
        this.responseTypeId = responseTypeId;
        this.responseType = responseType;
    }

    @XmlTransient
    public Integer getResponseTypeId() {
        return responseTypeId;
    }

    public void setResponseTypeId(Integer responseTypeId) {
        this.responseTypeId = responseTypeId;
    }

    @XmlValue
    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    @XmlTransient
    public Info getInfoId() {
        return infoId;
    }

    public void setInfoId(Info infoId) {
        this.infoId = infoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (responseTypeId != null ? responseTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ResponseType)) {
            return false;
        }
        ResponseType other = (ResponseType) object;
        if ((this.responseTypeId == null && other.responseTypeId != null) || (this.responseTypeId != null && !this.responseTypeId.equals(other.responseTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "exercisecontrolsystem.entities.ResponseType[responseTypeId=" + responseTypeId + "]";
    }
}

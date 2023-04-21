package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "recodes", catalog = "dev", schema = "")
@NamedQueries({ @NamedQuery(name = "Recodes.findAll", query = "SELECT r FROM Recodes r"), @NamedQuery(name = "Recodes.findByRId", query = "SELECT r FROM Recodes r WHERE r.rId = :rId"), @NamedQuery(name = "Recodes.findByRDate", query = "SELECT r FROM Recodes r WHERE r.rDate = :rDate"), @NamedQuery(name = "Recodes.findByRHandle", query = "SELECT r FROM Recodes r WHERE r.rHandle = :rHandle"), @NamedQuery(name = "Recodes.findByRAcc", query = "SELECT r FROM Recodes r WHERE r.rAcc = :rAcc") })
public class Recodes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "R_ID", nullable = false)
    private Integer rId;

    @Lob
    @Column(name = "R_DATA", length = 65535)
    private String rData;

    @Column(name = "R_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rDate;

    @Column(name = "R_HANDLE")
    private Integer rHandle;

    @Column(name = "R_ACC")
    private Integer rAcc;

    @JoinColumn(name = "STUDENTS_ID", referencedColumnName = "S_ID", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Student studentsId;

    public Recodes() {
    }

    public Recodes(Integer rId) {
        this.rId = rId;
    }

    public Integer getRId() {
        return rId;
    }

    public void setRId(Integer rId) {
        this.rId = rId;
    }

    public String getRData() {
        return rData;
    }

    public void setRData(String rData) {
        this.rData = rData;
    }

    public Date getRDate() {
        return rDate;
    }

    public void setRDate(Date rDate) {
        this.rDate = rDate;
    }

    public Integer getRHandle() {
        return rHandle;
    }

    public void setRHandle(Integer rHandle) {
        this.rHandle = rHandle;
    }

    public Integer getRAcc() {
        return rAcc;
    }

    public void setRAcc(Integer rAcc) {
        this.rAcc = rAcc;
    }

    public Student getStudentsId() {
        return studentsId;
    }

    public void setStudentsId(Student studentsId) {
        this.studentsId = studentsId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rId != null ? rId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Recodes)) {
            return false;
        }
        Recodes other = (Recodes) object;
        if ((this.rId == null && other.rId != null) || (this.rId != null && !this.rId.equals(other.rId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Recodes[rId=" + rId + "]";
    }
}

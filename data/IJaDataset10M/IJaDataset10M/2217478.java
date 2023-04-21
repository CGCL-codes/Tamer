package org.gruposp2p.aularest.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.gruposp2p.aularest.server.LinkConstants;
import org.gruposp2p.aularest.utils.DateUtils;

/**
 *
 * @author jj
 */
@XmlType
@XmlRootElement(name = "absence")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "ABSENCE", catalog = "database", schema = "PUBLIC")
@NamedQueries({ @NamedQuery(name = "Absence.findAll", query = "SELECT a FROM Absence a"), @NamedQuery(name = "Absence.findById", query = "SELECT a FROM Absence a WHERE a.id = :id"), @NamedQuery(name = "Absence.findByDate", query = "SELECT a FROM Absence a WHERE a.date = :date"), @NamedQuery(name = "Absence.findByDescription", query = "SELECT a FROM Absence a WHERE a.description = :description"), @NamedQuery(name = "Absence.findByJustified", query = "SELECT a FROM Absence a WHERE a.justified = :justified") })
public class Absence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "DESCRIPTION", length = 511)
    @XmlElement(name = "description")
    private String description;

    @Basic(optional = false)
    @Column(name = "JUSTIFIED", nullable = false)
    private Serializable justified;

    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Student studentId;

    @JoinColumn(name = "SUBJECT_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Subject subjectId;

    @Transient
    @XmlElement(name = "absenceLink")
    private Link link;

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    @Transient
    @XmlElement(name = "justified")
    private Boolean isJustified;

    public Boolean getIsJustified() {
        return isJustified;
    }

    @Transient
    @XmlElement(name = "subjectLink")
    private Link subjectLink;

    public Link getSubjectLink() {
        return subjectLink;
    }

    @Transient
    @XmlElement(name = "studentLink")
    private Link studentLink;

    public Link getStudentLink() {
        return studentLink;
    }

    @Transient
    @XmlElement(name = "date")
    private XMLGregorianCalendar xmlDate;

    public Absence() {
    }

    public Absence(Integer id) {
        this.id = id;
    }

    public Absence(Integer id, Date date, Boolean justified) {
        this.id = id;
        this.date = date;
        this.justified = justified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Serializable getJustified() {
        return justified;
    }

    public void setJustified(Boolean justified) {
        this.justified = justified;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public Subject getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Subject subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Absence)) {
            return false;
        }
        Absence other = (Absence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.gruposp2p.restletjpa.model.Absence[id=" + id + "]";
    }

    @PostLoad
    public void postProcess() {
        link = new Link();
        link.setHref(LinkConstants.getAbsenceLink(id.toString()));
        link.setId(id.toString());
        if (date != null) {
            xmlDate = DateUtils.getXMLGregorianCalendar(date);
        }
        String name = subjectId.getName() + " - " + xmlDate.toString();
        link.setName(name);
        subjectLink = subjectId.getLink();
        studentLink = studentId.getLink();
        isJustified = (Boolean) justified;
    }

    /**
     * @return the xmlDate
     */
    public XMLGregorianCalendar getXmlDate() {
        return xmlDate;
    }
}

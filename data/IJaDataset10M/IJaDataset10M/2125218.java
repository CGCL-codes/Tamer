package org.gruposp2p.aularest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
@XmlRootElement(name = "itemcalificable")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "ITEMCALIFICABLE", catalog = "database", schema = "PUBLIC")
@NamedQueries({ @NamedQuery(name = "Itemcalificable.findAll", query = "SELECT i FROM Itemcalificable i"), @NamedQuery(name = "Itemcalificable.findById", query = "SELECT i FROM Itemcalificable i WHERE i.id = :id"), @NamedQuery(name = "Itemcalificable.findByName", query = "SELECT i FROM Itemcalificable i WHERE i.name = :name"), @NamedQuery(name = "Itemcalificable.findByDate", query = "SELECT i FROM Itemcalificable i WHERE i.date = :date"), @NamedQuery(name = "Itemcalificable.findByDescription", query = "SELECT i FROM Itemcalificable i WHERE i.description = :description") })
public class Itemcalificable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "NAME", nullable = false, length = 48)
    private String name;

    @Basic(optional = false)
    @Column(name = "DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "DESCRIPTION", length = 511)
    @XmlElement(name = "description")
    private String description;

    @ManyToMany(mappedBy = "itemcalificableCollection")
    private Collection<Competence> competenceCollection;

    @JoinColumn(name = "COURSEGROUP_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Coursegroup coursegroupId;

    @JoinColumn(name = "ITEMCALIFICABLETYPE_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Itemcalificabletype itemcalificabletypeId;

    @JoinColumn(name = "SUBJECT_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Subject subjectId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemcalificableId")
    private Collection<Score> scoreCollection;

    @Transient
    @XmlElement(name = "itemcalificableLink")
    private Link link;

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.name = link.getName();
        this.link = link;
    }

    @Transient
    @XmlElement(name = "date")
    private XMLGregorianCalendar xmlDate;

    public XMLGregorianCalendar getXMLGregorianCalendar() {
        return xmlDate;
    }

    @Transient
    @XmlElementWrapper(name = "competences")
    @XmlElement(name = "competenceLink")
    private Collection<Link> competencesLinks;

    public Collection<Link> getCompetencesLinks() {
        return competencesLinks;
    }

    @Transient
    @XmlElement(name = "itemcalificabletypeLink")
    private Link itemcalificabletypeLink;

    public Link getItemcalificabletypeLink() {
        return itemcalificabletypeLink;
    }

    @Transient
    @XmlElement(name = "subjectLink")
    private Link subjectLink;

    public Link getSubjectLink() {
        return subjectLink;
    }

    @Transient
    @XmlElement(name = "coursegroupLink")
    private Link coursegroupLink;

    public Link getCoursegroupLink() {
        return coursegroupLink;
    }

    @Transient
    @XmlElementWrapper(name = "scores")
    @XmlElement(name = "scoreLink")
    private Collection<Link> scores;

    public Collection<Link> getScoresLinks() {
        return scores;
    }

    public Itemcalificable() {
    }

    public Itemcalificable(Integer id) {
        this.id = id;
    }

    public Itemcalificable(Integer id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Collection<Competence> getCompetenceCollection() {
        return competenceCollection;
    }

    public void setCompetenceCollection(Collection<Competence> competenceCollection) {
        this.competenceCollection = competenceCollection;
    }

    public Coursegroup getCoursegroupId() {
        return coursegroupId;
    }

    public void setCoursegroupId(Coursegroup coursegroupId) {
        this.coursegroupId = coursegroupId;
    }

    public Itemcalificabletype getItemcalificabletypeId() {
        return itemcalificabletypeId;
    }

    public void setItemcalificabletypeId(Itemcalificabletype itemcalificabletypeId) {
        this.itemcalificabletypeId = itemcalificabletypeId;
    }

    public Subject getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Subject subjectId) {
        this.subjectId = subjectId;
    }

    public Collection<Score> getScoreCollection() {
        return scoreCollection;
    }

    public void setScoreCollection(Collection<Score> scoreCollection) {
        this.scoreCollection = scoreCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Itemcalificable)) {
            return false;
        }
        Itemcalificable other = (Itemcalificable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.gruposp2p.restletjpa.model.Itemcalificable[id=" + id + "]";
    }

    @PostLoad
    public void postProcess() {
        link = new Link();
        link.setHref(LinkConstants.getItemcalificableLink(id.toString()));
        link.setId(id.toString());
        link.setName(name);
        if (date != null) {
            xmlDate = DateUtils.getXMLGregorianCalendar(date);
        }
        if (competenceCollection != null) {
            competencesLinks = new ArrayList<Link>();
            for (Competence competence : competenceCollection) {
                competencesLinks.add(competence.getLink());
            }
        }
        if (scoreCollection != null) {
            scores = new ArrayList<Link>();
            for (Score score : scoreCollection) {
                scores.add(score.getLink());
            }
        }
        itemcalificabletypeLink = itemcalificabletypeId.getLink();
        subjectLink = subjectId.getLink();
        coursegroupLink = coursegroupId.getLink();
    }

    @PrePersist
    public void prePersist() {
        this.name = link.getName();
    }

    /**
     * @return the xmlDate
     */
    public XMLGregorianCalendar getXmlDate() {
        return xmlDate;
    }
}

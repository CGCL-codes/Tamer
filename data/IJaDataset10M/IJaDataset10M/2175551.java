package gov.nist.mel.emergency.cap;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.persistence.GeneratedValue;
import javax.persistence.Transient;

/**
 *
 * @author Guillaume Radde <guillaume.radde@nist.gov>
 */
@XmlRootElement(name = "alert", namespace = "urn:oasis:names:tc:emergency:cap:1.1")
@XmlType(propOrder = { "identifier", "sender", "sent", "status", "msgType", "source", "scope", "restriction", "addresses", "handlingCodeCollection", "note", "references", "incidents", "infoCollection" })
@Entity
@Table(name = "alert")
@NamedQueries({ @NamedQuery(name = "Alert.findByAlertId", query = "SELECT a FROM Alert a WHERE a.alertId = :alertId"), @NamedQuery(name = "Alert.findByIdentifier", query = "SELECT a FROM Alert a WHERE a.identifier = :identifier"), @NamedQuery(name = "Alert.findBySender", query = "SELECT a FROM Alert a WHERE a.sender = :sender"), @NamedQuery(name = "Alert.findBySent", query = "SELECT a FROM Alert a WHERE a.sent = :sent"), @NamedQuery(name = "Alert.findByStatus", query = "SELECT a FROM Alert a WHERE a.status = :status"), @NamedQuery(name = "Alert.findByMsgType", query = "SELECT a FROM Alert a WHERE a.msgType = :msgType"), @NamedQuery(name = "Alert.findBySource", query = "SELECT a FROM Alert a WHERE a.source = :source"), @NamedQuery(name = "Alert.findByScope", query = "SELECT a FROM Alert a WHERE a.scope = :scope"), @NamedQuery(name = "Alert.findByRestriction", query = "SELECT a FROM Alert a WHERE a.restriction = :restriction"), @NamedQuery(name = "Alert.findByAddresses", query = "SELECT a FROM Alert a WHERE a.addresses = :addresses"), @NamedQuery(name = "Alert.findByNote", query = "SELECT a FROM Alert a WHERE a.note = :note"), @NamedQuery(name = "Alert.findByReferences", query = "SELECT a FROM Alert a WHERE a.references = :references"), @NamedQuery(name = "Alert.findByIncidents", query = "SELECT a FROM Alert a WHERE a.incidents = :incidents"), @NamedQuery(name = "Alert.findAll", query = "SELECT a FROM Alert a") })
public class Alert implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlTransient
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id", nullable = false)
    private Integer alertId;

    @Column(name = "identifier", nullable = false)
    private String identifier;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "sent", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sent;

    @Column(name = "`status`", nullable = false)
    private String status;

    @Column(name = "msgType", nullable = false)
    private String msgType;

    @Column(name = "source")
    private String source;

    @Column(name = "scope", nullable = false)
    private String scope;

    @Column(name = "restriction")
    private String restriction;

    @Column(name = "addresses")
    private String addresses;

    @Column(name = "note")
    private String note;

    @Column(name = "`references`")
    private String references;

    @Column(name = "incidents")
    private String incidents;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alertId")
    private Collection<HandlingCode> handlingCodeCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alertId")
    private Collection<Info> infoCollection;

    public Alert() {
    }

    public Alert(Integer alertId) {
        this.alertId = alertId;
    }

    public Alert(Integer alertId, String identifier, String sender, Date sent, String status, String msgType, String scope) {
        this.alertId = alertId;
        this.identifier = identifier;
        this.sender = sender;
        this.sent = sent;
        this.status = status;
        this.msgType = msgType;
        this.scope = scope;
    }

    @XmlTransient
    public Integer getAlertId() {
        return alertId;
    }

    public void setAlertId(Integer alertId) {
        this.alertId = alertId;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getIncidents() {
        return incidents;
    }

    public void setIncidents(String incidents) {
        this.incidents = incidents;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1", name = "code")
    public Collection<HandlingCode> getHandlingCodeCollection() {
        return handlingCodeCollection;
    }

    public void setHandlingCodeCollection(Collection<HandlingCode> handlingCodeCollection) {
        this.handlingCodeCollection = handlingCodeCollection;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1", name = "info")
    public Collection<Info> getInfoCollection() {
        return infoCollection;
    }

    public void setInfoCollection(Collection<Info> infoCollection) {
        this.infoCollection = infoCollection;
    }

    @Transient
    private Alert self;

    @XmlTransient
    public Alert getSelf() {
        return this;
    }

    @SuppressWarnings("empty-statement")
    public void setSelf(Alert newSelf) {
        ;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alertId != null ? alertId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Alert)) {
            return false;
        }
        Alert other = (Alert) object;
        if ((this.alertId == null && other.alertId != null) || (this.alertId != null && !this.alertId.equals(other.alertId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Alert #" + this.identifier;
    }
}

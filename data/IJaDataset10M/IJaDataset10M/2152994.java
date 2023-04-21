package org.dcm4chee.arr.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Jan 23, 2007
 */
@Entity
@Table(name = "audit_record")
public class AuditRecord implements Serializable {

    private static final long serialVersionUID = -4138396955398529612L;

    private static final Logger log = Logger.getLogger(AuditRecord.class);

    private long pk;

    private Code eventID;

    private String eventAction;

    private int eventOutcome;

    private Date eventDateTime;

    private Date receiveDateTime;

    private Code eventType;

    private String enterpriseSiteID;

    private String sourceID;

    private int sourceType;

    private Collection<ActiveParticipant> activeParticipants;

    private Collection<ParticipantObject> participantObjects;

    private boolean iheYr4;

    private byte[] xmldata;

    @Id
    @GeneratedValue(generator = "hibseq")
    @GenericGenerator(name = "hibseq", strategy = "seqhilo", parameters = { @Parameter(name = "max_lo", value = "100"), @Parameter(name = "sequence", value = "audit_record_pk_seq") })
    @Column(name = "pk")
    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    @ManyToOne
    @JoinColumn(name = "event_id_fk")
    public Code getEventID() {
        return eventID;
    }

    public void setEventID(Code eventID) {
        this.eventID = eventID;
    }

    @ManyToOne
    @JoinColumn(name = "event_type_fk")
    public Code getEventType() {
        return eventType;
    }

    public void setEventType(Code eventTypeCode) {
        this.eventType = eventTypeCode;
    }

    @Column(name = "site_id")
    public String getEnterpriseSiteID() {
        return enterpriseSiteID;
    }

    public void setEnterpriseSiteID(String auditEnterpriseSiteID) {
        this.enterpriseSiteID = auditEnterpriseSiteID;
    }

    @Column(name = "source_id")
    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String auditSourceID) {
        this.sourceID = auditSourceID;
    }

    @Column(name = "source_type")
    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int typeCode) {
        this.sourceType = typeCode;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditRecord")
    public Collection<ActiveParticipant> getActiveParticipants() {
        return activeParticipants;
    }

    public void setActiveParticipants(Collection<ActiveParticipant> c) {
        this.activeParticipants = c;
    }

    public void addActiveParticipant(ActiveParticipant ap) {
        if (activeParticipants == null) {
            activeParticipants = new ArrayList<ActiveParticipant>(3);
        }
        activeParticipants.add(ap);
        ap.setAuditRecord(this);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auditRecord")
    public Collection<ParticipantObject> getParticipantObjects() {
        return participantObjects;
    }

    public void setParticipantObjects(Collection<ParticipantObject> c) {
        this.participantObjects = c;
    }

    public void addParticipantObject(ParticipantObject po) {
        if (participantObjects == null) {
            participantObjects = new ArrayList<ParticipantObject>(3);
        }
        participantObjects.add(po);
        po.setAuditRecord(this);
    }

    @Column(name = "event_action")
    public String getEventAction() {
        return eventAction;
    }

    public void setEventAction(String eventActionCode) {
        this.eventAction = eventActionCode;
    }

    @Column(name = "event_outcome")
    public int getEventOutcome() {
        return eventOutcome;
    }

    public void setEventOutcome(int eventOutcomeIndicator) {
        this.eventOutcome = eventOutcomeIndicator;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "event_date_time")
    @Index(name = "ar_event_date_time")
    public Date getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Date dt) {
        this.eventDateTime = dt;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "receive_date_time")
    @Index(name = "ar_receive_date_ti")
    public Date getReceiveDateTime() {
        return receiveDateTime;
    }

    public void setReceiveDateTime(Date dt) {
        this.receiveDateTime = dt;
    }

    @Column(name = "iheyr4")
    public boolean isIHEYr4() {
        return iheYr4;
    }

    public void setIHEYr4(boolean iheYr4) {
        this.iheYr4 = iheYr4;
    }

    @Lob
    @Column(name = "xmldata", length = 262144)
    public byte[] getXmldata() {
        return xmldata;
    }

    public void setXmldata(byte[] xmldata) {
        this.xmldata = xmldata;
    }

    @PostPersist
    public void postPersit() {
        if (log.isDebugEnabled()) log.debug("Created " + this.toString());
    }

    @PostRemove
    public void postRemove() {
        if (log.isDebugEnabled()) log.debug("Removed " + this.toString());
    }

    public String toString() {
        return "AuditRecord[pk=" + pk + ", eventDateTime=" + eventDateTime + ", receiveDateTime=" + receiveDateTime + ", sourceID=" + sourceID + "]";
    }
}

package de.objectcode.time4u.server.entities;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.TimePolicy;

@Entity
@Table(name = "T4U_TIMEPOLICIES")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "kind", length = 1)
public abstract class TimePolicyEntity {

    /** Primary key. */
    private String m_id;

    /** Revision number (increased every time something has changed). */
    private long m_revision;

    /** Client id of the last modification. */
    private long m_lastModifiedByClient;

    /** Flag if the timepolicy is deleted. */
    private boolean m_deleted;

    /** The person the time policy belongs too. */
    private PersonEntity m_person;

    protected Date m_validFrom;

    protected Date m_validUntil;

    /**
   * Default constructor for hibernate.
   */
    protected TimePolicyEntity() {
    }

    protected TimePolicyEntity(final String id, final long revision, final long lastModifiedByClient, final PersonEntity person) {
        m_id = id;
        m_revision = revision;
        m_lastModifiedByClient = lastModifiedByClient;
        m_person = person;
    }

    @Id
    @Column(length = 36)
    public String getId() {
        return m_id;
    }

    public void setId(final String id) {
        m_id = id;
    }

    public long getRevision() {
        return m_revision;
    }

    public void setRevision(final long revision) {
        m_revision = revision;
    }

    public long getLastModifiedByClient() {
        return m_lastModifiedByClient;
    }

    public void setLastModifiedByClient(final long lastModifiedByClient) {
        m_lastModifiedByClient = lastModifiedByClient;
    }

    public boolean isDeleted() {
        return m_deleted;
    }

    public void setDeleted(final boolean deleted) {
        m_deleted = deleted;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id")
    public PersonEntity getPerson() {
        return m_person;
    }

    public void setPerson(final PersonEntity person) {
        m_person = person;
    }

    public Date getValidFrom() {
        return m_validFrom;
    }

    public void setValidFrom(final Date validFrom) {
        m_validFrom = validFrom;
    }

    public Date getValidUntil() {
        return m_validUntil;
    }

    public void setValidUntil(final Date validUntil) {
        m_validUntil = validUntil;
    }

    /**
   * Write data to DTO.
   * 
   * @param status
   *          The DTO.
   */
    public void toDTO(final TimePolicy timePolicy) {
        timePolicy.setId(m_id);
        timePolicy.setRevision(m_revision);
        timePolicy.setLastModifiedByClient(m_lastModifiedByClient);
        timePolicy.setDeleted(m_deleted);
        timePolicy.setValidFrom(m_validFrom != null ? new CalendarDay(m_validFrom) : null);
        timePolicy.setValidUntil(m_validUntil != null ? new CalendarDay(m_validUntil) : null);
    }

    public void fromDTO(final TimePolicy timePolicy) {
        m_lastModifiedByClient = timePolicy.getLastModifiedByClient();
        m_deleted = timePolicy.isDeleted();
        m_validFrom = timePolicy.getValidFrom() != null ? timePolicy.getValidFrom().getDate() : null;
        m_validUntil = timePolicy.getValidUntil() != null ? timePolicy.getValidUntil().getDate() : null;
    }

    public abstract int getRegularTime(final Date day);
}

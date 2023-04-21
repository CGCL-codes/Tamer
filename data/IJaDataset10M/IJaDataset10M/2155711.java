package org.opensaas.jaudit.dao.hibernate;

import java.util.Date;
import org.opensaas.jaudit.AuditSubject;
import org.opensaas.jaudit.ResponsibleInformation;
import org.opensaas.jaudit.SessionRecordMutable;

@SuppressWarnings("serial")
class SimpleSessionRecord implements SessionRecordMutable {

    private Date _endedTs;

    private String _id;

    private Date _startedTs;

    private AuditSubject _system;

    private String _systemAddress;

    private String _sessionId;

    private ResponsibleInformation _responsibleInformation;

    /**
     * Constructor.
     * 
     * @param id
     *            the unique identifier.
     */
    public SimpleSessionRecord(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id is required.");
        }
        _id = id;
    }

    /**
     * {@inheritDoc}
     */
    public Date getEndedTs() {
        return _endedTs;
    }

    /**
     * {@inheritDoc}
     */
    public void setEndedTs(final Date endedTs) {
        _endedTs = endedTs;
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return _id;
    }

    /**
     * {@inheritDoc}
     */
    public Date getStartedTs() {
        return _startedTs;
    }

    /**
     * {@inheritDoc}
     */
    public void setStartedTs(final Date startedTs) {
        _startedTs = startedTs;
    }

    /**
     * {@inheritDoc}
     */
    public AuditSubject getSystem() {
        return _system;
    }

    /**
     * {@inheritDoc}
     */
    public void setSystem(final AuditSubject system) {
        _system = system;
    }

    /**
     * {@inheritDoc}
     */
    public String getSystemAddress() {
        return _systemAddress;
    }

    /**
     * {@inheritDoc}
     */
    public void setSystemAddress(final String systemAddress) {
        _systemAddress = systemAddress;
    }

    /**
     * {@inheritDoc}
     */
    public String getSessionId() {
        return _sessionId;
    }

    /**
     * {@inheritDoc}
     */
    public void setSessionId(final String sessionId) {
        _sessionId = sessionId;
    }

    /**
     * {@inheritDoc}
     */
    public ResponsibleInformation getResponsibleInformation() {
        return _responsibleInformation;
    }

    /**
     * {@inheritDoc}
     */
    public void setResponsibleInformation(final ResponsibleInformation responsibleInformation) {
        _responsibleInformation = responsibleInformation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleSessionRecord other = (SimpleSessionRecord) obj;
        return _id.equals(other._id);
    }

    /**
     * {@inheritDoc}
     */
    public void setId(String id) {
        _id = id;
    }
}

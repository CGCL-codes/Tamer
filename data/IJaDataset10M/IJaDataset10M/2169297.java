package com.sun.sgs.impl.service.transaction;

import com.sun.sgs.profile.ProfileParticipantDetail;

/**
 * Simple implementation of <code>ProfileParticipantDetail</code> that
 * is package-private and used by <code>TransactionImpl</code> to report
 * detail associated with each participant.
 */
class ProfileParticipantDetailImpl implements ProfileParticipantDetail {

    private final String name;

    private boolean prepared = false;

    private long prepareTime = 0;

    private boolean readOnly = false;

    private boolean committed = false;

    private long commitTime = 0;

    private boolean committedDirectly = false;

    private long abortTime = 0;

    /**
     * Creates an instance of <code>ProfileParticipantDetailImpl</code> for
     * the given named participant.
     *
     * @param participantName the name of the participant associated with
     *                        this detail
     */
    ProfileParticipantDetailImpl(String participantName) {
        this.name = participantName;
    }

    /**
     * {@inheritDoc}
     */
    public String getParticipantName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public boolean wasPrepared() {
        return prepared;
    }

    /**
     * {@inheritDoc}
     */
    public boolean wasReadOnly() {
        return readOnly;
    }

    /**
     * {@inheritDoc}
     */
    public boolean wasCommitted() {
        return committed;
    }

    /**
     * {@inheritDoc}
     */
    public boolean wasCommittedDirectly() {
        return committedDirectly;
    }

    /**
     * {@inheritDoc}
     */
    public long getPrepareTime() {
        return prepareTime;
    }

    /**
     * {@inheritDoc}
     */
    public long getCommitTime() {
        return commitTime;
    }

    /**
     * {@inheritDoc}
     */
    public long getAbortTime() {
        return abortTime;
    }

    /**
     * Sets the detail as associated with a participant that has been
     * prepared successfully. If <code>readOnlyParticipant</code> is
     * <code>true</code> then none of the other mutator methods should
     * be called after calling <code>setPrepared</code>.
     *
     * @param time the time in milliseconds that the participant spent
     *             preparing
     * @param readOnlyParticipant whether preparation ended with the
     *                            participant voting read-only
     */
    void setPrepared(long time, boolean readOnlyParticipant) {
        prepareTime = time;
        prepared = true;
        readOnly = readOnlyParticipant;
    }

    /**
     * Sets the detail as associated with a participant that has been
     * committed successfully. None of the other mutator methods should be
     * called after calling <code>setCommitted</code>.
     *
     * @param time the time in milliseconds that the participant spent
     *             committing
     */
    void setCommitted(long time) {
        committed = true;
        commitTime = time;
    }

    /**
     * Sets the detail as associated with a participant that has been
     * directly committed successfully. None of the other mutator methods
     * should be called after calling <code>setCommittedDirectly</code>.
     *
     * @param time the time in milliseconds that the participant spent
     *             preparing and committing
     */
    void setCommittedDirectly(long time) {
        setPrepared(time, false);
        setCommitted(time);
        committedDirectly = true;
    }

    /**
     * Sets the detail as associated with a participant that has been
     * aborted. None of the other mutator methods should be called after
     * calling <code>setAborted</code>.
     *
     * @param time the time in milliseconds that the participant spent
     *             aborting
     */
    void setAborted(long time) {
        abortTime = time;
    }
}

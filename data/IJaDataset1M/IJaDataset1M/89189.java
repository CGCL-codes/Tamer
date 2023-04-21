package org.waveprotocol.wave.model.account;

import org.waveprotocol.wave.model.util.Preconditions;
import org.waveprotocol.wave.model.wave.ParticipantId;
import java.util.Set;

/**
 * A wrapper for Indexability that provides default values for absent
 * assignments. Setting an indexability to the default value clears the
 * assignment.
 *
 */
public class DefaultingIndexability implements MutableIndexability {

    private final MutableIndexability target;

    private final IndexDecision defaultDecision;

    public DefaultingIndexability(final MutableIndexability indexability, IndexDecision defaultDecision) {
        Preconditions.checkNotNull(indexability, "indexability can't be null");
        this.target = indexability;
        this.defaultDecision = defaultDecision;
    }

    @Override
    public Set<ParticipantId> getIndexDecisions() {
        return target.getIndexDecisions();
    }

    @Override
    public IndexDecision getIndexability(ParticipantId participant) {
        IndexDecision result = target.getIndexability(participant);
        if (result == null) {
            return defaultDecision;
        }
        return result;
    }

    @Override
    public void setIndexability(ParticipantId participant, IndexDecision indexability) {
        if (indexability == defaultDecision) {
            target.setIndexability(participant, null);
        } else {
            target.setIndexability(participant, indexability);
        }
    }
}

package org.jactr.eclipse.runtime.production2;

import java.util.Collection;
import java.util.Collections;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.eclipse.runtime.session.data.ISessionData;
import org.jactr.eclipse.runtime.session.stream.AbstractRollingSessionDataStream;

public class ConflictResolutionSessionDataStream extends AbstractRollingSessionDataStream<ConflictResolutionData, ConflictResolutionData> implements IConflictResolutionDataStream {

    /**
   * Logger definition
   */
    private static final transient Log LOGGER = LogFactory.getLog(ConflictResolutionSessionDataStream.class);

    public ConflictResolutionSessionDataStream(ISessionData sessionData, double windowSize) {
        super("conflict", sessionData, windowSize);
    }

    @Override
    protected double getTime(ConflictResolutionData data) {
        return data.getTime();
    }

    @Override
    protected Collection<ConflictResolutionData> toOutputData(ConflictResolutionData input) {
        return Collections.singleton(input);
    }
}

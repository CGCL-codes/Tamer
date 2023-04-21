package com.ibm.tuningfork.infra.selector;

import com.ibm.tuningfork.infra.event.EventType;
import com.ibm.tuningfork.infra.event.TypedEvent;
import com.ibm.tuningfork.infra.sharing.ISharingConvertibleCallback;

public class IntegerEventDataSelector implements IIntegerDataSelector {

    protected final String eventName;

    protected final int dataPosition;

    protected final boolean allFeedlets;

    protected final int feedletId;

    public static final int NO_FEEDLET = -1;

    public IntegerEventDataSelector(int dataPosition) {
        this(null, dataPosition, NO_FEEDLET);
    }

    public IntegerEventDataSelector(String eventName, int dataPosition) {
        this(eventName, dataPosition, NO_FEEDLET);
    }

    public IntegerEventDataSelector(String eventName, int dataPosition, int feedletId) {
        this.eventName = eventName;
        this.dataPosition = dataPosition;
        this.feedletId = feedletId;
        this.allFeedlets = feedletId < 0;
    }

    public void collectReconstructionArguments(ISharingConvertibleCallback cb) throws Exception {
        cb.convert(eventName);
        cb.convert(dataPosition);
        if (!allFeedlets) cb.convert(feedletId);
    }

    public Integer match(final TypedEvent event) {
        final EventType eventType = event.getType();
        String name = eventType.getName();
        if ((eventName == null || name.equals(eventName)) && (allFeedlets || event.getFeedlet().getId() == feedletId) && dataPosition < event.numInts()) {
            int value = event.getInt(dataPosition);
            return new Integer(value);
        }
        return null;
    }
}

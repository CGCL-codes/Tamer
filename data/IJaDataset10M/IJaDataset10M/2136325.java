package alice.respect;

import alice.tuplecentre.*;

/**
 *
 *
 * @version 1.0
 */
public class ObservableEventReactionFail extends ObservableEventExt {

    public TriggeredReaction z;

    protected ObservableEventReactionFail(Object source, TriggeredReaction z) {
        super(source, ObservableEventExt.TYPE_REACTIONFAIL);
        this.z = z;
    }
}

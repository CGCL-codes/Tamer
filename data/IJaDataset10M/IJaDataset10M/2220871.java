package skycastle.gameagents.sensorystream;

/**
 * @author Hans H�ggstr�m
 */
public abstract class AbstractSensoryEvent implements SensoryEvent {

    private final long myTimestamp;

    private static final long serialVersionUID = -6112320747141043979L;

    public long getTimestamp() {
        return myTimestamp;
    }

    protected AbstractSensoryEvent() {
        myTimestamp = System.currentTimeMillis();
    }

    protected AbstractSensoryEvent(final long timestamp) {
        myTimestamp = timestamp;
    }
}

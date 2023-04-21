package krum.sectorzero.protocol;

/**
 * A server message indicating a pending shutdown.
 * 
 * Generated code - do not edit!
 * 
 * @see krum.sectorzero.codegen.Generator#generateMessageClasses
 * 
 */
public class ShutdownWarning extends ServerMessage {

    protected int delay;

    /**
     * Direct constructor.
     * 
     */
    public ShutdownWarning(String msg, int delay) {
        super(msg);
        this.type = DataTypes.SHUTDOWN_WARNING;
        setParameter("type", "SHUTDOWN_WARNING");
        this.delay = delay;
        setParameter("delay", delay);
    }

    /**
     * Type promotion constructor.
     * 
     */
    protected ShutdownWarning(Data arg, boolean enforceType) throws MessageException {
        super(arg, false);
        if (enforceType) {
            if (arg.type == DataTypes.SHUTDOWN_WARNING) {
                this.type = arg.type;
            } else throw new MessageException("Cannot construct ShutdownWarning from type " + arg.type.name());
        }
        this.delay = getInt("delay");
    }

    /**
     * Type promotion constructor.
     * 
     */
    public ShutdownWarning(Data arg) throws MessageException {
        this(arg, true);
    }

    /**
     * Number of seconds until the server shuts down. Only one
     * shutdown can be scheduled at a time.
     * 
     */
    public int getDelay() {
        return delay;
    }
}

package dalvik.system;

/**
 * Is thrown when the VM determines that a DEX file's cache is out of date, and
 * that there is no way to recreate it.
 *
 * @deprecated this is an internal Dalvik class that is not appropriate for
 *      general use. It will be removed from the public API in a future release.
 */
public class StaleDexCacheError extends VirtualMachineError {

    /**
     * Creates a new exception instance and initializes it with default values.
     */
    public StaleDexCacheError() {
        super();
    }

    /**
     * Creates a new exception instance and initializes it with a given message.
     * 
     * @param detailMessage the error message
     */
    public StaleDexCacheError(String detailMessage) {
        super(detailMessage);
    }
}

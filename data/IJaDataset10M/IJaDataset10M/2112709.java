package hla.rti1516e.exceptions;

/**
 * Public exception class RegionInUseForUpdateOrSubscription
 */
public final class RegionInUseForUpdateOrSubscription extends RTIexception {

    public RegionInUseForUpdateOrSubscription(String msg) {
        super(msg);
    }

    public RegionInUseForUpdateOrSubscription(String message, Throwable cause) {
        super(message, cause);
    }
}

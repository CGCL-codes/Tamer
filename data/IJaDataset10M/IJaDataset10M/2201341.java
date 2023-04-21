package android.os;

/**
 * The contents of a Parcel (usually during unmarshalling) does not
 * contain the expected data.
 */
public class ParcelFormatException extends RuntimeException {

    public ParcelFormatException() {
        super();
    }

    public ParcelFormatException(String reason) {
        super(reason);
    }
}

package android.pim.vcard.exception;

/**
 * VCardException thrown when VCard is nested without VCardParser's being notified.
 */
public class VCardNestedException extends VCardNotSupportedException {

    public VCardNestedException() {
        super();
    }

    public VCardNestedException(String message) {
        super(message);
    }
}

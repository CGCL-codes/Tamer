package nom.tam.fits;

/** This exception indicates that an error
  * was detected while parsing a FITS header record.
  */
public class BadHeaderException extends FitsException {

    public BadHeaderException() {
        super();
    }

    public BadHeaderException(String msg) {
        super(msg);
    }
}

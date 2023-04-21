package ie.omk.smpp;

/** NoSuchRequestException
  * @author Oran Kelly
  * @version 1.0
  */
public class NoSuchRequestException extends ie.omk.smpp.SMPPException {

    private int sequenceNum = -1;

    public NoSuchRequestException() {
    }

    /** Construct a new NoSuchRequestException with specified message.
      */
    public NoSuchRequestException(String s) {
        super(s);
    }

    /** Construct a new NoSuchRequestException.
      * @param sequenceNum The sequence number for which there is no request
      * packet.
      */
    public NoSuchRequestException(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    /** Get the request packet sequence number that caused this exception.
      */
    public int getSequenceNum() {
        return (this.sequenceNum);
    }
}

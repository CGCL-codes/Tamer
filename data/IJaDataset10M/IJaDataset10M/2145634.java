package gruni.jd3lib;

/**
 *  An exception that is thrown when invalid data is set in an ID3 tag.<br/>
 * <dl>
 * <dt><b>Version History:</b></dt>
 * <dt>1.3.1 - <small>2002.1023 by gruni</small></dt>
 * <dd>-Made sourcecode compliant to the Sun Coding Conventions</dd>
 * <dt>1.3 - <small>2002.0318 by helliker</small></dt>
 * <dd>-Inherits from ID3Exception now.</dd>
 *
 * <dt>1.2 - <small>2001.1019 by helliker</small></dt>
 * <dd>-All set for release.</dd>
 * </dl>
 * @author  Jonathan Hilliker
 * @version 1.3.1
 */
public class ID3FieldDataException extends ID3Exception {

    /**
   * Create an ID3FieldDataException with a default message
   *
   */
    public ID3FieldDataException() {
        super("Invalid data supplied to ID3 tag.");
    }

    /**
   * Create an ID3FieldDataException with the specified message
   *
   * @param msg a String specifying the specific problem encountered
   */
    public ID3FieldDataException(String msg) {
        super(msg);
    }
}

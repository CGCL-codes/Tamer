package edu.drexel.p2pftp.handler.message;

/**
 * Message header object
 * @author jliang
 *
 */
public class MessageHeader extends BaseObj {

    public enum HeaderNameEnum {

        P2PFTP_SENDER, P2PFTP_FILE_SIZE, P2PFTP_FILE_NAME, P2PFTP_USER_NAME, P2PFTP_PASSWORD, P2PFTP_ERROR_CODE, P2PFTP_VERSION, P2PFTP_MIMETYPE, P2PFTP_DATE, P2PFTP_FROM, P2PFTP_SHA1
    }

    private String _name;

    private String _value;

    public MessageHeader(String name, String value) {
        _name = name;
        _value = value;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
    }
}

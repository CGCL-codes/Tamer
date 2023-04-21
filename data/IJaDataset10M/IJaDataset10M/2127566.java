package org.jjsip.sip.header;

/**
 * SIP Header Subject.
 */
public class SubjectHeader extends Header {

    /** Creates a SubjectHeader with value <i>hvalue</i> */
    public SubjectHeader(String hvalue) {
        super(SipHeaderType.SUBJECT, hvalue);
    }

    /** Creates a new SubjectHeader equal to SubjectHeader <i>hd</i> */
    public SubjectHeader(Header hd) {
        super(hd);
    }

    /** Gets the subject */
    public String getSubject() {
        return value;
    }
}

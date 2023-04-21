package gov.lanl.COAS.odmg;

import org.omg.DsObservationValue.Multimedia;
import org.omg.DsObservationValue.MultimediaHelper;
import gov.lanl.Utility.CorbaHelper;

/**
 * ObservationValue containing Multimedia data.
 * 
 * 
 * @author
 * @version %I%, %G%
 */
public class Multimedia_ implements ObservationValue_, java.io.Serializable {

    public String content_type = "";

    public String other_mime_header_fields = "";

    public byte[] a_blob = new byte[0];

    public long total_size = 0;

    private static org.apache.xerces.impl.dv.util.Base64 base64 = new org.apache.xerces.impl.dv.util.Base64();

    /**
	 * Constructor declaration
	 * 
	 * 
	 * @see
	 */
    public Multimedia_() {
    }

    /**
	 * Constructor declaration
	 * 
	 * 
	 * @param content_type
	 * @param other_mime_header_fields
	 * @param a_blob
	 * @param total_size
	 * 
	 * @see
	 */
    public Multimedia_(String content_type, String other_mime_header_fields, byte[] a_blob, long total_size) {
        this.content_type = content_type;
        this.other_mime_header_fields = other_mime_header_fields;
        this.a_blob = a_blob;
        this.total_size = total_size;
    }

    /**
	 * Constructor declaration
	 * 
	 * 
	 * @param multimedia
	 * 
	 * @see
	 */
    public Multimedia_(org.omg.DsObservationValue.Multimedia multimedia) {
        content_type = multimedia.content_type;
        other_mime_header_fields = multimedia.other_mime_header_fields;
        a_blob = multimedia.a_blob;
        total_size = multimedia.total_size;
    }

    /**
	 * Method declaration
	 * 
	 * 
	 * @return
	 * 
	 * @see
	 */
    public org.omg.CORBA.Any[] toObservationValue() {
        org.omg.CORBA.Any[] value = new org.omg.CORBA.Any[1];
        org.omg.CORBA.ORB orb = CorbaHelper.getOrb();
        value[0] = orb.create_any();
        MultimediaHelper.insert(value[0], new Multimedia(content_type, other_mime_header_fields, a_blob, total_size, new gov.lanl.COAS.MultimediaIteratorImpl()._this(orb)));
        return value;
    }

    /**
	 * Method declaration
	 * 
	 * 
	 * @param indent
	 * 
	 * @return
	 * 
	 * @see
	 */
    public String toXML(String indent) {
        String xml;
        xml = indent + "<Multimedia ContentType=" + content_type + " OtherMimeHeaderFields=" + other_mime_header_fields + ">\n";
        xml += indent + "   <Blob>" + a_blob + "</Blob>\n";
        xml += indent + "</Multimedia>\n";
        return xml;
    }

    /**
	 * Method declaration
	 * 
	 * 
	 * @return
	 * 
	 * @see
	 */
    public java.lang.String toString() {
        return "Multimedia_ : Type : " + content_type + " MimeHeaderFields : " + other_mime_header_fields;
    }

    /**
     * comparator
     * @param src
     * @return
     */
    public boolean compare(ObservationValue_ src) {
        if (src instanceof Multimedia_) {
            Multimedia_ in = (Multimedia_) src;
            String blobStr = new String(base64.encode(a_blob));
            String inBlobStr = new String(base64.encode(in.a_blob));
            if (content_type.equals(in.content_type) && other_mime_header_fields.equals(in.other_mime_header_fields) && total_size == in.total_size && blobStr.equals(inBlobStr)) return true;
        }
        return false;
    }
}

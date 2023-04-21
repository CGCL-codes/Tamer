package org.dcm4cheri.hl7;

import org.dcm4che.hl7.HL7Segment;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <description> 
 *
 * @see <related>
 * @author  <a href="mailto:gunter@tiani.com">gunter zeilinger</a>
 * @version $Revision: 3922 $ $Date: 2005-10-05 12:26:16 -0400 (Wed, 05 Oct 2005) $
 *   
 * <p><b>Revisions:</b>
 *
 * <p><b>yyyymmdd author:</b>
 * <ul>
 * <li> explicit fix description (no line numbers but methods) go 
 *            beyond the cvs commit message
 * </ul>
 */
public class HL7SegmentImpl extends HL7FieldsImpl implements HL7Segment {

    private static final int MIN_LEN = 4;

    private static final byte[] DELIM = { (byte) '|', (byte) '~', (byte) '^', (byte) '&' };

    private final String id;

    static final ResourceBundle DICT = ResourceBundle.getBundle("org/dcm4cheri/hl7/HL7Dictionary");

    static String getName(String key, String defVal) {
        try {
            return DICT.getString(key);
        } catch (MissingResourceException e) {
            return defVal;
        }
    }

    HL7SegmentImpl(byte[] data, int off, int len) {
        super(data, off, len, DELIM);
        if (len < MIN_LEN || data[off + 3] != (byte) '|') {
            throw new IllegalArgumentException(toString());
        }
        this.id = super.get(0);
    }

    public String id() {
        return id;
    }

    public String get(int seq, int rep) {
        return super.get(new int[] { seq, rep - 1 });
    }

    public String get(int seq, int rep, int comp) {
        return super.get(new int[] { seq, rep - 1, comp - 1 });
    }

    public String get(int seq, int rep, int comp, int sub) {
        return super.get(new int[] { seq, rep - 1, comp - 1, sub - 1 });
    }

    public int size(int seq, int rep) {
        return super.size(new int[] { seq, rep - 1 });
    }

    public int size(int seq, int rep, int comp) {
        return super.size(new int[] { seq, rep - 1, comp - 1 });
    }

    StringBuffer toVerboseStringBuffer(StringBuffer sb) {
        sb.append(id).append(" - ").append(getName(id, ""));
        for (int i = 1, n = size(); i < n; ++i) {
            String key = id + '.' + i;
            sb.append("\n\t").append(key).append(": ").append(get(i)).append("\t\t//").append(getName(key, "????"));
        }
        return sb;
    }

    public String toVerboseString() {
        return toVerboseStringBuffer(new StringBuffer()).toString();
    }
}

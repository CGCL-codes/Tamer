package hambo.pim;

import java.util.*;

/**
 * This class identifies a part within a message.  Since a multipart can
 * contain a multipart, this class gives a <q>path</q> to the wanted part,
 * such as <q>the second part of the forth part of the third part of the
 * message</q>, or <q>3.4.2</q>.
 * <p>This class can be converted to a short string usefull in URLs, so you
 * can link to a path by using a messageid and this class.
 */
public class PartId {

    private PartId parent;

    private int i;

    /**
     * The char values of the allowed i values.  Note that this array MUST be
     * sorted.
     */
    private static char[] svalue = { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    /**
     * Create an id for a part of the message. The message is a multipart and
     * this is part i of that.
     * @param i the sequence number of this part within the message.
     */
    public PartId(int i) {
        this.parent = null;
        this.i = i;
    }

    /**
     * Create an id for a part of a part.
     * @param parent the <code>PartId</code> of the part that contains this
     * part.
     * @param i the sequence number of this part witin <code>parent</code>.
     */
    public PartId(PartId parent, int i) {
        this.parent = parent;
        this.i = i;
    }

    public PartId getParent() {
        return parent;
    }

    public int getIndex() {
        return i;
    }

    /**
     * Read a PartId from its serialized form.
     */
    public static PartId valueOf(String serial) {
        PartId value = null;
        for (int i = 0; i < serial.length(); ++i) {
            value = new PartId(value, Arrays.binarySearch(svalue, serial.charAt(i)));
        }
        return value;
    }

    /**
     * Serialize a PartId to a string.
     */
    public String toString() {
        if (parent != null) return parent.toString() + svalue[i]; else return String.valueOf(svalue[i]);
    }
}

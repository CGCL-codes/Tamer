package sasc.emv;

import sasc.iso7816.TagAndLength;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sasc.util.Util;

/**
 * Data Object List (DOL)
 *
 * In several instances, the terminal is asked to build a flexible list of data
 * elements to be passed to the card under the card‘s direction.
 * To minimise processing within the ICC, such a list is not TLV encoded but
 * is a single constructed field built by concatenating several data
 * elements together. Since the elements of the constructed field are not
 * TLV encoded, it is imperative that the ICC knows the format of this field
 * when the data is received. This is achieved by including a
 * Data Object List (DOL) in the ICC, specifying the format of the data to
 * be included in the constructed field.
 *
 * DOLs currently used in this specification include:
 * - the Processing Options Data Object List (PDOL) used with the GET PROCESSING OPTIONS command
 * - the Card Risk Management Data Object Lists (CDOL1 and CDOL2) used with the GENERATE APPLICATION CRYPTOGRAM (AC) command
 * - the Transaction Certificate Data Object List (TDOL) used to generate a TC Hash Value
 * - the Dynamic Data Authentication Data Object List (DDOL) used with the INTERNAL AUTHENTICATE command
 *
 * ---------------------------------------------------------------------------
 *
 * In other words, a DOL is sent from the ICC. This DOL contains only Tag ID bytes and length bytes.
 * The Terminal constructs the response, which contains only the VALUES for these tags.
 *
 * //TODO check DOL processing on page 55
 *
 * @author sasc
 */
public class DOL {

    public enum Type {

        PDOL("Processing Options Data Object List"), CDOL1("Card Risk Management Data Object List 1"), CDOL2("Card Risk Management Data Object List 2"), TDOL("Transaction Certificate Data Object List"), DDOL("Dynamic Data Authentication Data Object List");

        private String description;

        private Type(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return getDescription();
        }
    }

    private Type type;

    private List<TagAndLength> tagAndLengthList = new ArrayList<TagAndLength>();

    public DOL(Type type, byte[] data) {
        this.type = type;
        this.tagAndLengthList = EMVUtil.parseTagAndLength(data);
    }

    public List<TagAndLength> getTagAndLengthList() {
        return Collections.unmodifiableList(tagAndLengthList);
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        dump(new PrintWriter(sw), 0);
        return sw.toString();
    }

    public void dump(PrintWriter pw, int indent) {
        pw.println(Util.getSpaces(indent) + type.getDescription());
        String indentStr = Util.getSpaces(indent + 3);
        for (TagAndLength tagAndLength : tagAndLengthList) {
            int length = tagAndLength.getLength();
            pw.println(indentStr + tagAndLength.getTag().getName() + " (" + length + " " + (length == 1 ? "byte" : "bytes") + ")");
        }
    }
}

package a_vcard.android.syncml.pim.vcard;

import a_vcard.android.syncml.pim.VDataBuilder;
import a_vcard.android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class VCardParser {

    VCardParser_V21 mParser = null;

    public static final String VERSION_VCARD21 = "vcard2.1";

    public static final String VERSION_VCARD30 = "vcard3.0";

    public static final int VERSION_VCARD21_INT = 1;

    public static final int VERSION_VCARD30_INT = 2;

    String mVersion = null;

    private static final String TAG = "VCardParser";

    public VCardParser() {
    }

    /**
     * If version not given. Search from vcard string of the VERSION property.
     * Then instance mParser to appropriate parser.
     *
     * @param vcardStr
     *            the content of vcard data
     */
    private void judgeVersion(String vcardStr) {
        if (mVersion == null) {
            int verIdx = vcardStr.indexOf("\nVERSION:");
            if (verIdx == -1) mVersion = VERSION_VCARD21; else {
                String verStr = vcardStr.substring(verIdx, vcardStr.indexOf("\n", verIdx + 1));
                if (verStr.indexOf("2.1") > 0) mVersion = VERSION_VCARD21; else if (verStr.indexOf("3.0") > 0) mVersion = VERSION_VCARD30; else mVersion = VERSION_VCARD21;
            }
        }
        if (mVersion.equals(VERSION_VCARD21)) mParser = new VCardParser_V21();
        if (mVersion.equals(VERSION_VCARD30)) mParser = new VCardParser_V30();
    }

    /**
     * To make sure the vcard string has proper wrap character
     *
     * @param vcardStr
     *            the string to be checked
     * @return string after verified
     */
    private String verifyVCard(String vcardStr) {
        this.judgeVersion(vcardStr);
        vcardStr = vcardStr.replaceAll("\r\n", "\n");
        String[] strlist = vcardStr.split("\n");
        StringBuilder v21str = new StringBuilder("");
        for (int i = 0; i < strlist.length; i++) {
            if (strlist[i].indexOf(":") < 0) {
                if (strlist[i].length() == 0 && strlist[i + 1].indexOf(":") > 0) v21str.append(strlist[i]).append("\r\n"); else v21str.append(" ").append(strlist[i]).append("\r\n");
            } else v21str.append(strlist[i]).append("\r\n");
        }
        return v21str.toString();
    }

    /**
     * Set current version
     *
     * @param version
     *            the new version
     */
    private void setVersion(String version) {
        this.mVersion = version;
    }

    /**
     * Parse the given vcard string
     *
     * @param vcardStr
     *            to content to be parsed
     * @param encoding
     *            encoding of vcardStr
     * @param builder
     *            the data builder to hold data
     * @return true if the string is successfully parsed, else return false
     * @throws VCardException
     * @throws IOException
     */
    public boolean parse(String vcardStr, String encoding, VDataBuilder builder) throws VCardException, IOException {
        vcardStr = this.verifyVCard(vcardStr);
        boolean isSuccess = mParser.parse(new ByteArrayInputStream(vcardStr.getBytes(encoding)), encoding, builder);
        if (!isSuccess) {
            if (mVersion.equals(VERSION_VCARD21)) {
                this.setVersion(VERSION_VCARD30);
                return this.parse(vcardStr, builder);
            }
            throw new VCardException("parse failed.(even use 3.0 parser)");
        }
        return true;
    }

    /**
     * Parse the given vcard string with US-ASCII encoding
     *
     * @param vcardStr
     *            to content to be parsed
     * @param builder
     *            the data builder to hold data
     * @return true if the string is successfully parsed, else return false
     * @throws VCardException
     * @throws IOException
     */
    public boolean parse(String vcardStr, VDataBuilder builder) throws VCardException, IOException {
        return parse(vcardStr, "US-ASCII", builder);
    }
}

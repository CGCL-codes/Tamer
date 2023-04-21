package org.newsml.toolkit.conformance;

import org.newsml.toolkit.BaseNode;
import org.newsml.toolkit.NewsMLException;
import org.newsml.toolkit.Text;

/**
 * Validate the values of an xml:lang attributes in the document.
 *
 * <p>This test verifies that the value conforms to the syntactic
 * requirements of IETF RFC 1766, and warns about unrecognized
 * language or country codes.</p>
 *
 * @author Reuters PLC
 * @version 2.0
 * @see TestBase
 */
public class XMLLangTest extends TestBase {

    /**
     * ISO 639 two-letter language codes.
     */
    private static final String[] iso639Codes = { "AA", "AB", "AE", "AF", "AK", "AM", "AR", "AS", "AV", "AY", "AZ", "BA", "BE", "BG", "BH", "BI", "BM", "BN", "BO", "BR", "BS", "CA", "CE", "CH", "CO", "CR", "CS", "CU", "CV", "CY", "DA", "DE", "DV", "DZ", "EE", "EL", "EN", "EO", "ES", "ET", "EU", "FA", "FF", "FI", "FJ", "FO", "FR", "FY", "GA", "GD", "GL", "GN", "GU", "GV", "HA", "HE", "HI", "HO", "HR", "HU", "HY", "HZ", "IA", "ID", "IE", "IG", "IK", "IN", "IS", "IT", "IU", "IW", "JA", "JI", "JV", "KA", "KG", "KI", "KJ", "KK", "KL", "KM", "KN", "KO", "KR", "KS", "KU", "KV", "KW", "KY", "LA", "LB", "LG", "LN", "LO", "LT", "LU", "LV", "MG", "MH", "MI", "MK", "ML", "MN", "MO", "MR", "MS", "MT", "MY", "NA", "NB", "ND", "NE", "NG", "NL", "NN", "NO", "NR", "NV", "NY", "OC", "OJ", "OM", "OR", "OS", "PA", "PI", "PL", "PS", "PT", "QU", "RM", "RN", "RO", "RU", "RW", "SA", "SC", "SD", "SE", "SG", "SH", "SI", "SK", "SL", "SM", "SN", "SO", "SQ", "SR", "SS", "ST", "SU", "SV", "SW", "TA", "TE", "TG", "TH", "TI", "TK", "TL", "TN", "TO", "TR", "TS", "TT", "TW", "TY", "UG", "UK", "UR", "UZ", "VE", "VI", "VO", "WO", "XH", "YI", "YO", "ZA", "ZH", "ZU" };

    /**
     * Run tests for a Text node with a language code.
     *
     * @param node A Text node containing the value of an xml:lang
     * attribute.
     * @exception NewsMLException If the test fails.
     * @exception java.lang.ClassCastException If the node parameter is not
     *   an instance of {@link Text}. 
     * @see TestBase#run
     */
    public void run(BaseNode node, boolean useExternal) throws NewsMLException {
        String lang = ((Text) node).toString();
        if (!Util.isISOLang(lang)) error("Bad NewsML 1.04 RFC 1766 language syntax: " + lang);
        String tag;
        int pos = lang.indexOf("-");
        if (pos == -1) {
            tag = lang.toUpperCase();
        } else {
            tag = lang.substring(0, pos).toUpperCase();
        }
        if (tag.length() != 2) error("ISO 639 language code should have two letters: " + tag);
        if (!isISO639Code(tag)) warning("Unknown ISO 639 language code: " + tag);
    }

    /**
     * Test whether a string is an ISO 639 language code.
     *
     * @param The string to test.
     * @return true if the string is a two-letter ISO 639 language
     * code, false otherwise.
     */
    private static boolean isISO639Code(String code) {
        for (int i = 0; i < iso639Codes.length; i++) {
            if (code.equals(iso639Codes[i])) return true;
        }
        return false;
    }
}

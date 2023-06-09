package org.xmlcml.cml.chemdraw.components;

import nu.xom.Attribute;
import nu.xom.Node;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLLabel;

/**
 * 
 * @author pm286
 *
 */
public class CDXText extends CDXObject {

    static Logger LOG = Logger.getLogger(CDXText.class);

    static {
        LOG.setLevel(Level.INFO);
    }

    public static final int CODE = 0x8006;

    public static final String NAME = "Text";

    public static final String CDXNAME = "t";

    private String text = null;

    public CDXText() {
        super(CODE, NAME, CDXNAME);
    }

    /**
     * copy node .
     * @return Node
     */
    public Node copy() {
        return new CDXText(this);
    }

    /**
     * copy constructor
     * @param old
     */
    public CDXText(CDXText old) {
        super(old);
    }

    /** return ints for font
     * order is startChar fontIndex typeface? size color
     * @param s of form [[1 2 3 4 5]]
     * @return 5 ints
     */
    static int[] unpackStyleRunString(String s) {
        int[] ii = null;
        if (s != null) {
            String ss = s;
            if (ss.startsWith(FLBRAK) && ss.endsWith(FRBRAK)) {
                ss = ss.substring(FLBRAK.length(), ss.length() - FRBRAK.length());
                ii = org.xmlcml.euclid.Util.splitToIntArray(ss, S_SPACE);
                if (ii.length != 5) {
                    throw new RuntimeException("Bad font string; " + s);
                }
            } else {
                throw new RuntimeException("Bad font string: " + s);
            }
        }
        return ii;
    }

    /**
     * @param s
     * @return fontindex
     */
    static int getFontIndexFromTempText(String s) {
        return unpackStyleRunString(s)[1];
    }

    /**
     * @param s
     * @return typeFace (as integer)
     */
    static int getTypeFaceFromTempText(String s) {
        return unpackStyleRunString(s)[2];
    }

    /**
     * @param s
     * @return size
     */
    static int getSizeFromTempText(String s) {
        return unpackStyleRunString(s)[3];
    }

    /**
     * @param s
     * @return color index
     */
    static int getColorIndexFromTempText(String s) {
        return unpackStyleRunString(s)[4];
    }

    /** tidy node.
    * turn temp_Text attribute into attributes
    * of form [[1 2 3 4 5]] or [[1 2 3 4 5]][[6 7 8 9 10]]
    * this is messy; there can be more than one font; take the last one
    */
    public void addFontInfoFromTempText() {
        String t = this.getAttributeValue(TEMP_TEXT);
        if (t != null && !t.equals("")) {
            int idx1 = t.lastIndexOf(FRBRAK);
            if (idx1 != -1) {
                int idx0 = t.lastIndexOf(FRBRAK + FLBRAK);
                t = (idx0 == -1) ? t : t.substring(idx0 + FRBRAK.length());
                idx1 = t.lastIndexOf(FRBRAK);
                t = t.substring(0, idx1 + FRBRAK.length());
                addAttribute(new Attribute("fontIndex", "" + getFontIndexFromTempText(t)));
                addAttribute(new Attribute("typeFace", "" + getTypeFaceFromTempText(t)));
                addAttribute(new Attribute("size", "" + getSizeFromTempText(t)));
                addAttribute(new Attribute("color", "" + getColorIndexFromTempText(t)));
            }
        }
    }

    public void process2CML(CMLElement cmlNode) {
        addLabelToCMLElement(cmlNode);
    }

    /**
	 * @return text after [[..]]
	 */
    String getLabelTextFromTextTempAndSquareBrackets() {
        String vv = this.query("./@" + TEMP_TEXT).get(0).getValue();
        while (vv.startsWith("[[")) {
            int idx = vv.indexOf("]]");
            if (idx != -1) {
                vv = vv.substring(idx + 2);
            } else {
                break;
            }
        }
        return vv;
    }

    void addLabelToCMLElement(CMLElement element) {
        CMLLabel label = new CMLLabel();
        label.setDictRef("cdx:label");
        String vv = this.getLabelTextFromTextTempAndSquareBrackets();
        this.copyAttributesTo(label);
        label.setCMLValue(vv);
        element.appendChild(label);
    }

    /**
     * @return s
     */
    public String getString() {
        return "[" + text + "]";
    }
}

;

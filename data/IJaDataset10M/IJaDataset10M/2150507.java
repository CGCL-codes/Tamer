package org.web3d.x3d.palette.items;

import java.util.Arrays;
import java.util.Vector;
import javax.swing.text.JTextComponent;
import org.web3d.x3d.types.X3DGeometryNode;
import static org.web3d.x3d.types.X3DPrimitiveTypes.*;
import static org.web3d.x3d.types.X3DSchemaData.*;

/**
 * TEXT.java
 * Created on August 16, 2007, 10:49 AM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey, Don Brutzman
 * @version $Id$
 */
public class TEXT extends X3DGeometryNode {

    private String[] string, stringDefault;

    private SFFloat[] length, lengthDefault;

    private SFFloat maxExtent, maxExtentDefault;

    private boolean solid;

    private boolean insertCommas, insertLineBreaks = false;

    public TEXT() {
    }

    @Override
    public String getElementName() {
        return TEXT_ELNAME;
    }

    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return TEXTCustomizer.class;
    }

    @Override
    public void initialize() {
        string = stringDefault = parseIntoStringArray(TEXT_ATTR_STRING_DFLT, true);
        String[] sa;
        if (TEXT_ATTR_LENGTH_DFLT == null || TEXT_ATTR_LENGTH_DFLT.length() <= 0) sa = new String[] {}; else sa = parseX(TEXT_ATTR_LENGTH_DFLT);
        length = lengthDefault = parseToSFFloatArray(sa);
        maxExtent = maxExtentDefault = new SFFloat(TEXT_ATTR_MAXEXTENT_DFLT, 0.0f, null);
        solid = Boolean.parseBoolean(TEXT_ATTR_SOLID_DFLT);
    }

    @Override
    public void initializeFromJdom(org.jdom.Element root, JTextComponent comp) {
        super.initializeFromJdom(root, comp);
        org.jdom.Attribute attr = root.getAttribute(TEXT_ATTR_STRING_NAME);
        if (attr != null) string = parseIntoStringArray(attr.getValue(), true);
        attr = root.getAttribute(TEXT_ATTR_LENGTH_NAME);
        if (attr != null) {
            String[] sa = parseX(attr.getValue());
            length = parseToSFFloatArray(sa);
        }
        attr = root.getAttribute(TEXT_ATTR_MAXEXTENT_NAME);
        if (attr != null) maxExtent = new SFFloat(attr.getValue(), 0.0f, null);
        attr = root.getAttribute(TEXT_ATTR_SOLID_NAME);
        if (attr != null) solid = Boolean.parseBoolean(attr.getValue());
    }

    @Override
    public String createAttributes() {
        StringBuilder sb = new StringBuilder();
        if ((length != null) && !lengthFormatted().equals("") && (TEXT_ATTR_LENGTH_REQD || !arraysIdenticalOrNull(length, lengthDefault))) {
            sb.append(" ");
            sb.append(TEXT_ATTR_LENGTH_NAME);
            sb.append("='");
            sb.append(lengthFormatted());
            sb.append("'");
        }
        if (TEXT_ATTR_MAXEXTENT_REQD || !maxExtent.equals(maxExtentDefault)) {
            sb.append(" ");
            sb.append(TEXT_ATTR_MAXEXTENT_NAME);
            sb.append("='");
            sb.append(maxExtent);
            sb.append("'");
        }
        if (TEXT_ATTR_SOLID_REQD || solid != Boolean.parseBoolean(TEXT_ATTR_SOLID_DFLT)) {
            sb.append(" ");
            sb.append(TEXT_ATTR_SOLID_NAME);
            sb.append("='");
            sb.append(solid);
            sb.append("'");
        }
        if (TEXT_ATTR_STRING_REQD || (!Arrays.equals(string, stringDefault) && string.length > 0)) {
            sb.append(" ");
            sb.append(TEXT_ATTR_STRING_NAME);
            sb.append("='");
            sb.append(formatStringArray(string, insertCommas, insertLineBreaks));
            sb.append("'");
        }
        return sb.toString();
    }

    private Vector<String> parseString(String s) {
        Vector<String> v = new Vector<String>();
        s = s.trim();
        if (s.length() > 0) {
            if (!s.startsWith("\"")) v.add(s); else {
                s = s.substring(1);
                if (s.endsWith("\"")) s = s.substring(0, s.length() - 1);
                String[] sa = s.split("\"\\s*\"");
                for (String str : sa) {
                    v.add(str);
                }
            }
        }
        return v;
    }

    /**
   *
   * @return length field which is an MFFloat array, formatted
   */
    private String lengthFormatted() {
        if ((length == null) || (length.length == 0) || ((length.length == 1) && (length[0].toString().equals("0")))) return "";
        StringBuilder sb = new StringBuilder();
        for (SFFloat f : length) {
            sb.append(f.toString());
            sb.append(' ');
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private Vector<SFFloat> parseLength(String s) {
        Vector<SFFloat> v = new Vector<SFFloat>();
        s = s.trim();
        if (s.length() > 0) {
            String[] sa = s.split("\\s");
            for (String st : sa) {
                v.add(new SFFloat(st, 0.0f, null));
            }
        }
        return v;
    }

    public String[] getStringArray() {
        return this.string;
    }

    public void setStringArray(String[] newStringArray) {
        this.string = newStringArray;
    }

    public String getLength() {
        return lengthFormatted();
    }

    public void setLength(String newLength) {
        if ((newLength == null) || (newLength.length() == 0)) {
            this.length = null;
        } else {
            String[] sa = parseX(newLength);
            this.length = parseToSFFloatArray(sa);
        }
    }

    public String getMaxExtent() {
        return maxExtent.toString();
    }

    public void setMaxExtent(String maxExtent) {
        this.maxExtent = new SFFloat(maxExtent, 0.0f, null);
    }

    public boolean isSolid() {
        return solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    /**
     * @return the insertCommas value
     */
    public boolean isInsertCommas() {
        return insertCommas;
    }

    /**
     * @param insertCommas the insertCommas value to set
     */
    public void setInsertCommas(boolean insertCommas) {
        this.insertCommas = insertCommas;
    }

    /**
     * @return the insertLineBreaks value
     */
    public boolean isInsertLineBreaks() {
        return insertLineBreaks;
    }

    /**
     * @param insertLineBreaks the insertLineBreak values to set
     */
    public void setInsertLineBreaks(boolean insertLineBreaks) {
        this.insertLineBreaks = insertLineBreaks;
    }
}

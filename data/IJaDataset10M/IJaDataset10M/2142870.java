package org.xaware.server.engine.instruction.bizcomps.copybook;

import java.util.BitSet;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.xaware.shared.util.PictureClauseUtility;
import org.xaware.shared.util.XAwareConstants;

public class CopybookCOMP3Converter implements ICopybookFieldConverter {

    private PictureClauseUtility picClauseUtil = null;

    HashMap conversionParameters = null;

    /**
     * constructor
     */
    public CopybookCOMP3Converter() {
    }

    public String convertToString(final byte[] recordData, final int start, final int length, final String picValue, final String format, final HashMap encoding) throws Exception {
        boolean defaultFormat = true;
        boolean displayDollar = false;
        if (format != null && format != " ") {
            String nextToken = null;
            final StringTokenizer st = new StringTokenizer(format);
            while (st.hasMoreTokens()) {
                nextToken = st.nextToken();
                System.out.println(nextToken);
                if (nextToken.equalsIgnoreCase("false") || nextToken.equalsIgnoreCase(XAwareConstants.BIZCOMPONENT_ATTR_NO_FORMAT)) {
                    defaultFormat = false;
                    break;
                } else if (nextToken.equalsIgnoreCase(XAwareConstants.BIZCOMPONENT_ATTR_DISPLAY_DOLLAR)) {
                    displayDollar = true;
                    break;
                }
            }
        }
        StringBuffer buffer = new StringBuffer();
        try {
            for (int i = 0; i < length; i++) {
                final byte packedByte = recordData[i + start];
                final BitSet those_bits[] = bitSetsFromBase10(packedByte);
                final int hi = intBase10FromBitSet(those_bits[0]);
                final int lo = intBase10FromBitSet(those_bits[1]);
                if (i == length - 1) {
                    buffer.append(hi);
                    if ((lo == 11) || (lo == 13)) {
                        buffer.insert(0, '-');
                    }
                } else {
                    buffer.append(hi).append(lo);
                }
            }
        } catch (final Exception e) {
            throw e;
        }
        if (defaultFormat || displayDollar) {
            picClauseUtil = new PictureClauseUtility(defaultFormat, displayDollar);
            final String formatted = picClauseUtil.formatCobolNumber(buffer.toString(), picValue);
            buffer = new StringBuffer(formatted);
        }
        return buffer.toString();
    }

    /**
     * @param String
     * @return int
     */
    public int getFieldLength(final String fieldDescription) throws Exception {
        if (picClauseUtil == null) {
            picClauseUtil = new PictureClauseUtility();
        }
        return picClauseUtil.getFieldLength(fieldDescription, "COMP-3");
    }

    /**
     * Description: base 10 int changed to two 4 bit BitSet's
     * 
     * @param byte
     *            the byte the BitSet's are to be created from
     * @return BitSet[] the BitSet's created from the byte
     * @exception
     */
    private final BitSet[] bitSetsFromBase10(final byte hi) {
        final BitSet bits[] = new BitSet[2];
        bits[0] = new BitSet(4);
        bits[1] = new BitSet(4);
        int bb = new Integer(hi).intValue();
        if (bb < 0) {
            bb = bb + 256;
        }
        final int m7 = bb / 128;
        bb = (bb - (m7 * 128));
        if (m7 > 0) {
            bits[0].set(3);
        }
        final int m6 = bb / 64;
        bb = (bb - (m6 * 64));
        if (m6 > 0) {
            bits[0].set(2);
        }
        final int m5 = bb / 32;
        bb = (bb - (m5 * 32));
        if (m5 > 0) {
            bits[0].set(1);
        }
        final int m4 = bb / 16;
        bb = (bb - (m4 * 16));
        if (m4 > 0) {
            bits[0].set(0);
        }
        final int m3 = bb / 8;
        bb = (bb - (m3 * 8));
        if (m3 > 0) {
            bits[1].set(3);
        }
        final int m2 = bb / 4;
        bb = (bb - (m2 * 4));
        if (m2 > 0) {
            bits[1].set(2);
        }
        final int m1 = bb / 2;
        bb = (bb - (m1 * 2));
        if (m1 > 0) {
            bits[1].set(1);
        }
        final int m0 = bb;
        if (m0 > 0) {
            bits[1].set(0);
        }
        return (bits);
    }

    /**
     * Description: bit settings changed to base 10
     * 
     * @param BitSet
     *            the BitSet the decimal integer is to be created from
     * @return bb the int created from the BitSet
     * @exception
     */
    private final int intBase10FromBitSet(final BitSet bs) {
        final boolean m0 = bs.get(0);
        final boolean m1 = bs.get(1);
        final boolean m2 = bs.get(2);
        final boolean m3 = bs.get(3);
        int bb = 0;
        if (m0) {
            bb = bb + 1;
        }
        if (m1) {
            bb = bb + 2;
        }
        if (m2) {
            bb = bb + 4;
        }
        if (m3) {
            bb = bb + 8;
        }
        return (bb);
    }
}

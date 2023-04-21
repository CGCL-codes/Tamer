package com.google.typography.font.tools.conversion.eot;

import com.google.typography.font.sfntly.table.truetype.ControlValueTable;
import java.io.ByteArrayOutputStream;

/**
 * Implementation of compression of CVT table in CTF, as per section 5.2 of spec
 *
 * @author Raph Levien
 */
public class CvtEncoder {

    private static final int CVT_POS8 = 255;

    private static final int CVT_POS1 = CVT_POS8 - 7;

    private static final int CVT_NEG8 = CVT_POS1 - 1;

    private static final int CVT_NEG1 = CVT_NEG8 - 7;

    private static final int CVT_NEG0 = CVT_NEG1 - 1;

    private static final int CVT_WORDCODE = CVT_NEG0 - 1;

    private static final int CVT_LOWESTCODE = CVT_WORDCODE;

    private ByteArrayOutputStream cvtStream;

    public CvtEncoder() {
        cvtStream = new ByteArrayOutputStream();
    }

    public void encode(ControlValueTable cvtTable) {
        int numEntries = cvtTable.fwordCount();
        cvtStream.write(numEntries >> 8);
        cvtStream.write(numEntries & 0xff);
        int lastValue = 0;
        for (int i = 0; i < numEntries; i++) {
            int value = cvtTable.fword(i * 2);
            int deltaValue = (short) (value - lastValue);
            int absValue = Math.abs(deltaValue);
            int index = absValue / CVT_LOWESTCODE;
            if (index <= 8) {
                if (deltaValue < 0) {
                    cvtStream.write(CVT_NEG0 + index);
                    cvtStream.write(absValue - index * CVT_LOWESTCODE);
                } else {
                    if (index > 0) {
                        cvtStream.write(CVT_POS1 + index - 1);
                    }
                    cvtStream.write(absValue - index * CVT_LOWESTCODE);
                }
            } else {
                cvtStream.write(CVT_WORDCODE);
                cvtStream.write(deltaValue >> 8);
                cvtStream.write(deltaValue & 0xff);
            }
            lastValue = value;
        }
    }

    public byte[] toByteArray() {
        return cvtStream.toByteArray();
    }
}

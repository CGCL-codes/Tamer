package org.snsmeet.zxing.oned.rss.expanded.decoders;

import org.snsmeet.zxing.NotFoundException;
import org.snsmeet.zxing.common.BitArray;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 */
final class AI01393xDecoder extends AI01decoder {

    private static final int headerSize = 5 + 1 + 2;

    private static final int lastDigitSize = 2;

    private static final int firstThreeDigitsSize = 10;

    AI01393xDecoder(BitArray information) {
        super(information);
    }

    public String parseInformation() throws NotFoundException {
        if (this.information.size < headerSize + gtinSize) {
            throw NotFoundException.getNotFoundInstance();
        }
        StringBuffer buf = new StringBuffer();
        encodeCompressedGtin(buf, headerSize);
        int lastAIdigit = this.generalDecoder.extractNumericValueFromBitArray(headerSize + gtinSize, lastDigitSize);
        buf.append("(393");
        buf.append(lastAIdigit);
        buf.append(')');
        int firstThreeDigits = this.generalDecoder.extractNumericValueFromBitArray(headerSize + gtinSize + lastDigitSize, firstThreeDigitsSize);
        if (firstThreeDigits / 100 == 0) {
            buf.append('0');
        }
        if (firstThreeDigits / 10 == 0) {
            buf.append('0');
        }
        buf.append(firstThreeDigits);
        DecodedInformation generalInformation = this.generalDecoder.decodeGeneralPurposeField(headerSize + gtinSize + lastDigitSize + firstThreeDigitsSize, null);
        buf.append(generalInformation.getNewString());
        return buf.toString();
    }
}

package org.snsmeet.zxing.oned.rss.expanded.decoders;

import org.snsmeet.zxing.NotFoundException;
import org.snsmeet.zxing.common.BitArray;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 * @author Eduardo Castillejo, University of Deusto (eduardo.castillejo@deusto.es)
 */
final class AnyAIDecoder extends AbstractExpandedDecoder {

    private static final int HEADER_SIZE = 2 + 1 + 2;

    AnyAIDecoder(BitArray information) {
        super(information);
    }

    public String parseInformation() throws NotFoundException {
        StringBuffer buf = new StringBuffer();
        return this.generalDecoder.decodeAllCodes(buf, HEADER_SIZE);
    }
}

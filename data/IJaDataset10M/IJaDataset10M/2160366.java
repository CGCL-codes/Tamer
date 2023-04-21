package org.snsmeet.zxing.oned.rss.expanded.decoders;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 */
final class CurrentParsingState {

    int position;

    private int encoding;

    private static final int NUMERIC = 1;

    private static final int ALPHA = 2;

    private static final int ISO_IEC_646 = 4;

    CurrentParsingState() {
        this.position = 0;
        this.encoding = NUMERIC;
    }

    boolean isAlpha() {
        return this.encoding == ALPHA;
    }

    boolean isNumeric() {
        return this.encoding == NUMERIC;
    }

    boolean isIsoIec646() {
        return this.encoding == ISO_IEC_646;
    }

    void setNumeric() {
        this.encoding = NUMERIC;
    }

    void setAlpha() {
        this.encoding = ALPHA;
    }

    void setIsoIec646() {
        this.encoding = ISO_IEC_646;
    }
}

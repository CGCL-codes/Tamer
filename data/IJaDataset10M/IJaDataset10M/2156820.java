package atp.reporter.export;

import atp.reporter.product.RLineKey;

public class RHeaderMatrixCell {

    public final int rowspan, colspan;

    public final RLineKey key;

    RHeaderMatrixCell(RLineKey key, int rowspan, int colspan) {
        this.key = key;
        this.rowspan = rowspan;
        this.colspan = colspan;
    }

    public String toString() {
        return "'" + key.getTitle() + "' [r:" + rowspan + ",c:" + colspan + "]";
    }
}

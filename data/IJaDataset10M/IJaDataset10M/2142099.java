package org.intelligentsia.temporal.primitives;

import org.intelligentsia.temporal.TemporalFactory;

/**
 * Factory of LongTemporal.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class LongTemporalFactory implements TemporalFactory<LongTemporal> {

    @Override
    public LongTemporal newInstance(final LongTemporal result) {
        return new LongTemporal(result.getTransactionTime(), result.getValue());
    }
}

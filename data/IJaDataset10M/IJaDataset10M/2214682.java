package net.sf.bacchus.integration.spring;

import net.sf.bacchus.Record;
import org.springframework.batch.item.file.transform.LineAggregator;

/**
 * {@link LineAggregator} for ACH records delegates to line aggregators
 * specialized for each ACH record type.
 * @param <R> the record type this aggregates.
 */
public class RecordTypeLineAggregator<R extends Record> extends RecordTypeDispatcherSupport<R, LineAggregator<R>> implements LineAggregator<R> {

    /**
     * Line aggregator for ACH records delegates to the line aggregator
     * configured for the record type of the item.
     * @param item {@inheritDoc}
     * @return {@inheritDoc}
     * @throws Exception if thrown by the delegated line aggregator.
     */
    @Override
    public String aggregate(final R item) {
        final LineAggregator<R> aggregator = findDelegate(item);
        return aggregator == null ? null : aggregator.aggregate(item);
    }
}

package com.ibm.tuningfork.infra.stream.core;

import com.ibm.tuningfork.infra.data.ValueInterval;
import com.ibm.tuningfork.infra.sharing.ISharingConvertibleCallback;
import com.ibm.tuningfork.infra.stream.SampleMapper;
import com.ibm.tuningfork.infra.stream.expression.StreamOperand;
import com.ibm.tuningfork.infra.stream.precise.LazySampleStreamCursor;
import com.ibm.tuningfork.infra.stream.precise.RefinedLazySampleSummaryStreamCursor;
import com.ibm.tuningfork.infra.stream.precise.SampleStreamCursor;

/**
 * A lazily evaluated derived stream which applies a mapping function to each sample. The mapping can change both the
 * value and the time. RESTRICTIONS ON FUNCTION? PROBABLY AFFINE. PERHAPS DISALLOW GENERAL FUNCTION?
 *
 */
public class LazySampleStream extends SampleStream {

    protected final SampleStream data;

    protected final SampleMapper operator;

    public LazySampleStream(SampleStream data, SampleMapper operator) {
        this(data.getName() + " shifted " + operator.getName(), data, operator);
    }

    public LazySampleStream(String name, SampleStream data, SampleMapper operator) {
        super(name, "Apply Lazy Operator", StreamOperand.makeOperandList("Sample Stream ", data), data.getUnit());
        this.data = data;
        this.operator = operator;
    }

    @Override
    public void collectSpecificReconstructionArguments(ISharingConvertibleCallback cb) throws Exception {
        cb.convert(getName());
        cb.convert(data);
        cb.convert(operator);
    }

    public SampleStreamCursor newCursor(long minTime, long maxTime) {
        return new LazySampleStreamCursor(data, data.persistedSamples, operator, minTime, maxTime);
    }

    public SampleStreamCursor newCursor() {
        return newCursor(0, Long.MAX_VALUE);
    }

    public RefinedLazySampleSummaryStreamCursor newSummaryCursor(long minTime, long maxTime) {
        return new RefinedLazySampleSummaryStreamCursor(data, data.persistedSampleSummaries[0], operator, minTime - maxSummaryTimeWindowsTick[0], maxTime);
    }

    public RefinedLazySampleSummaryStreamCursor newSummaryCursor() {
        return newSummaryCursor(0, Long.MAX_VALUE);
    }

    public ValueInterval getDefaultValueRange() {
        ValueInterval range = data.getDefaultValueRange();
        if (range == null) {
            return null;
        }
        return operator.mapValueInterval(range);
    }

    public ValueInterval getObservedRange() {
        return operator.mapValueInterval(data.getObservedRange());
    }

    public boolean hasInfo() {
        return false;
    }

    public void waitForMore() {
        data.waitForMore();
    }

    public boolean isClosed() {
        return data.isClosed();
    }

    public long getLength() {
        return data.getLength();
    }

    public boolean isReady() {
        return data.isReady();
    }

    public boolean isRunning() {
        return data.isRunning();
    }

    public void start() {
        data.start();
    }
}

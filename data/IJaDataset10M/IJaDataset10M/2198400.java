package org.spantus.core.threshold.test;

import org.spantus.core.FrameValues;
import org.spantus.core.extractor.IExtractor;
import org.spantus.core.extractor.IExtractorConfig;

public class MockExtractor implements IExtractor {

    private IExtractorConfig config;

    private Double extractorSampleRate;

    public FrameValues calculate(Long sample, FrameValues values) {
        return calculateWindow(values);
    }

    public FrameValues calculateWindow(FrameValues window) {
        FrameValues rtn = new FrameValues();
        Double avg = 0D;
        for (Double float1 : window) {
            avg += float1;
        }
        avg /= window.size();
        rtn.add(avg);
        return rtn;
    }

    public FrameValues getOutputValues() {
        return null;
    }

    public IExtractorConfig getConfig() {
        return config;
    }

    public int getDimension() {
        return 0;
    }

    public Double getExtractorSampleRate() {
        return extractorSampleRate;
    }

    public void setExtractorSampleRate(Double extractorSampleRate) {
        this.extractorSampleRate = extractorSampleRate;
    }

    public String getName() {
        return null;
    }

    public void putValues(Long sample, FrameValues values) {
    }

    public void setConfig(IExtractorConfig config) {
        this.config = config;
    }

    public void flush() {
    }

    @Override
    public long getOffset() {
        return 0;
    }
}

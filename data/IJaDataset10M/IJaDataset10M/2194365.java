package org.spantus.work.services.test;

import org.spantus.core.FrameValues;
import org.spantus.core.FrameVectorValues;
import org.spantus.core.extractor.IExtractorConfig;
import org.spantus.core.extractor.IExtractorVector;

/**
 *
 * @author mondhs
 */
public class DummyExtractorVector implements IExtractorVector {

    private String name;

    private FrameVectorValues outputValues;

    public FrameVectorValues getOutputValues() {
        return outputValues;
    }

    public void setOutputValues(FrameVectorValues outputValues) {
        this.outputValues = outputValues;
    }

    public FrameVectorValues calculate(Long sample, FrameValues frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public FrameVectorValues calculateWindow(FrameValues window) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void putValues(Long sample, FrameValues values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void flush() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getExtractorSampleRate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IExtractorConfig getConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setConfig(IExtractorConfig config) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

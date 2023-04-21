package org.spantus.extractor;

import org.spantus.core.FrameValues;
import org.spantus.core.FrameVectorValues;
import org.spantus.core.extractor.ExtractorParam;
import org.spantus.core.extractor.IExtractorConfig;
import org.spantus.core.extractor.IExtractorVector;
import org.spantus.logger.Logger;

/**
 * 
 * @author Mindaugas Greibus
 * 
 * @since 0.0.1
 * Created Jun 3, 2009
 *
 */
public class ExtractorResultBuffer3D implements IExtractorVector {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ExtractorResultBuffer3D.class);

    private IExtractorVector extractor;

    private long offset = 0;

    FrameVectorValues outputValues = new FrameVectorValues();

    IExtractorConfig config;

    public ExtractorResultBuffer3D(IExtractorVector extractor) {
        this.extractor = extractor;
    }

    public void putValues(Long sample, FrameValues values) {
        calculate(sample, values);
    }

    public FrameVectorValues getOutputValues() {
        outputValues.setSampleRate(extractor.getExtractorSampleRate());
        return outputValues;
    }

    public void setOutputValues(FrameVectorValues outputValues) {
        this.outputValues = outputValues;
    }

    public ExtractorParam getParam() {
        return null;
    }

    public void setParam(ExtractorParam param) {
    }

    public String getName() {
        return extractor.getName();
    }

    public FrameVectorValues calculate(Long sample, FrameValues values) {
        FrameVectorValues val = extractor.calculate(sample, values);
        if (val == null) {
            return null;
        }
        getOutputValues().addAll(val);
        int i = getOutputValues().size() - getConfig().getBufferSize();
        while (i > 0) {
            getOutputValues().poll();
            offset++;
            i--;
        }
        val.setSampleRate(getExtractorSampleRate());
        return val;
    }

    public IExtractorConfig getConfig() {
        return extractor.getConfig();
    }

    public void setConfig(IExtractorConfig config) {
        extractor.setConfig(config);
    }

    public FrameVectorValues calculateWindow(FrameValues window) {
        return null;
    }

    public Double getExtractorSampleRate() {
        return getConfig().getSampleRate();
    }

    public void flush() {
        extractor.flush();
    }

    @Override
    public long getOffset() {
        return offset;
    }
}

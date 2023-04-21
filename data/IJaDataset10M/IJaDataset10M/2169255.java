package org.spantus.core.extractor;

import java.util.Map;

/**
 * To suport various signal formats(not only audio).
 * @author Mindaugas Greibus
 * 
 * Created Feb 22, 2010
 *
 */
public class SignalFormat {

    private Double sampleRate;

    private Double length;

    private Map<String, Object> parameters;

    public Double getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(Double sampleRate) {
        this.sampleRate = sampleRate;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}

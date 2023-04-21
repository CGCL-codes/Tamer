package de.tfhberlin.pdvl.funcgen;

import org.apache.log4j.*;
import java.lang.Math;

public class Signal {

    private Double frequency;

    private Double amplitude;

    private Double offset;

    private Waveform waveform;

    private static final double MAX_OFFSET = 5.0;

    private static final double MIN_AMPLITUDE = 0.05;

    private static final double MAX_AMPLITUDE = 10.0;

    private static final Logger LOG = Logger.getLogger(Signal.class);

    /*************************************************************************/
    public Double getAmplitude() {
        return amplitude;
    }

    /*************************************************************************/
    public boolean isAmplitudeValid() {
        boolean isValid = false;
        LOG.debug("isAmplitudeValid: amplitude: " + this.amplitude);
        double absAmplitude = Math.abs(this.amplitude.doubleValue());
        if (absAmplitude <= MAX_AMPLITUDE && absAmplitude >= MIN_AMPLITUDE) {
            isValid = true;
        }
        return isValid;
    }

    /*************************************************************************/
    public void setAmplitude(Double amplitude) {
        this.amplitude = amplitude;
    }

    /*************************************************************************/
    public Double getFrequency() {
        return frequency;
    }

    /*************************************************************************/
    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    /*************************************************************************/
    public boolean isFrequencyValid() {
        LOG.debug("isFrequencyValid: frequency: " + this.frequency);
        if (waveform == null) {
            throw new IllegalStateException("Waveform has to be set.");
        }
        return waveform.isValidFrequency(frequency.doubleValue());
    }

    /*************************************************************************/
    public Double getOffset() {
        return offset;
    }

    /*************************************************************************/
    public void setOffset(Double offset) {
        this.offset = offset;
    }

    /*************************************************************************/
    public boolean isOffsetValid() {
        boolean isValid = false;
        if (Math.abs(offset.doubleValue()) <= MAX_OFFSET) {
            isValid = true;
        }
        return isValid;
    }

    /*************************************************************************/
    public Waveform getWaveform() {
        return waveform;
    }

    /*************************************************************************/
    public void setWaveform(Waveform form) {
        this.waveform = form;
    }

    /*************************************************************************/
    public boolean isWaveformValid() {
        return waveform != null;
    }
}

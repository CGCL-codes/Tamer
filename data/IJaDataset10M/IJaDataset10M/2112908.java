package com.google.code.b0rx0r.advancedSamplerEngine.effect;

import com.google.code.b0rx0r.advancedSamplerEngine.Enqueueable;

public class StartpointEffect extends AbstractEffect {

    private int maxSamples;

    public StartpointEffect(Enqueueable wrapped, int maxSamples) {
        super(wrapped);
        this.maxSamples = maxSamples;
    }

    @Override
    public float getAudioData(int channel, long offset) {
        offset += maxSamples * modulation.getValue(0);
        return wrapped.getAudioData(channel, offset);
    }

    @Override
    public int getChannelCount() {
        return wrapped.getChannelCount();
    }

    @Override
    public long getLength() {
        int offset = (int) (maxSamples * modulation.getValue(0));
        return wrapped.getLength() - offset;
    }

    @Override
    public void prepareData(long start, long length) {
        int offset = (int) (maxSamples * modulation.getValue(0));
        wrapped.prepareData(start + offset, length);
    }
}
